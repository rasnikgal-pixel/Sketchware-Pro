package pro.sketchware.activities.soraeditor;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import io.github.rosemoe.sora.widget.CodeEditor;
import mod.hey.studios.code.SrcCodeEditor;
import mod.jbk.code.CodeEditorLanguages;
import pro.sketchware.R;
import pro.sketchware.utility.FileUtil;

/**
 * Один фрагмент = один файл в редакторе SORA.
 * Редактор создаётся в onViewCreated, язык ставится ДО текста.
 */
public class SoraEditorFragment extends Fragment {

    public static final String ARG_FILE_PATH = "file_path";

    private CodeEditor editor;
    private String filePath;
    private boolean loaded = false;
    private boolean modified = false;
    private String initialContent = "";
    private OnModifiedListener modifiedListener;
    private int pendingLine = -1;

    public interface OnModifiedListener {
        void onModifiedChanged(String filePath, boolean modified);
    }

    public static SoraEditorFragment newInstance(String filePath) {
        SoraEditorFragment f = new SoraEditorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_FILE_PATH, filePath);
        f.setArguments(args);
        return f;
    }

    public void setOnModifiedListener(OnModifiedListener listener) {
        this.modifiedListener = listener;
    }

    public String getFilePath() {
        return filePath;
    }

    public boolean isModified() {
        return modified;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filePath = getArguments().getString(ARG_FILE_PATH);
        }
        if (savedInstanceState != null) {
            modified = savedInstanceState.getBoolean("modified", false);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_sora_editor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        editor = view.findViewById(R.id.editor);
        // Загружаем сразу после создания view
        loadEditor();
    }

    private void loadEditor() {
        if (loaded || editor == null || filePath == null) return;
        loaded = true;

        // 1. Настройки (размер шрифта, wrap)
        try {
            SrcCodeEditor.loadCESettings(requireContext(), editor, "sora_editor", false);
        } catch (Throwable ignored) {
        }

        // 2. Настройки SORA (шрифт, wrap, XML-подсветка)
        SoraEditorSettings settings = new SoraEditorSettings(requireContext());
        try {
            editor.setTextSize(settings.getFontSize());
        } catch (Throwable ignored) {
        }
        try {
            editor.setWordwrap(settings.isWordWrapEnabled());
        } catch (Throwable ignored) {
        }

        // 3. Язык подсветки — ДО текста (Java/Kotlin), XML — отдельно после setText
        final int lang = SoraFileCollector.getLanguageForFile(filePath);
        try {
            if (lang == 0) {
                // Java — сразу
                editor.setEditorLanguage(new io.github.rosemoe.sora.langs.java.JavaLanguage());
            } else if (lang == 1) {
                editor.setEditorLanguage(CodeEditorLanguages.loadTextMateLanguage(
                        CodeEditorLanguages.SCOPE_NAME_KOTLIN));
            }
            // XML — поставим ниже, если включена настройка
        } catch (Throwable ignored) {
        }

        // 4. Тема по приложению
        try {
            boolean isDark = pro.sketchware.utility.ThemeUtils.isDarkThemeEnabled(requireContext());
            SrcCodeEditor.selectTheme(editor, isDark ? 3 : 0);
        } catch (Throwable ignored) {
        }

        // 5. Читаем файл
        String content = null;
        try {
            content = FileUtil.readFile(filePath);
        } catch (Throwable ignored) {
        }
        initialContent = content == null ? "" : content;

        // 6. Ставим текст
        editor.setText(initialContent);

        // 7. Курсор в начало
        try {
            editor.setSelection(0, 0);
        } catch (Throwable ignored) {
        }

        // 8. XML-подсветка отключена (ломает отображение даже с postDelayed)
        // Возможно, добавим позже через другой способ

        // 6b. Применить отложенный переход к строке
        if (pendingLine >= 0) {
            final int ln = pendingLine;
            pendingLine = -1;
            editor.post(() -> goToLine(ln));
        }

        // 7. Отслеживание изменений
        try {
            editor.getText().addContentListener(new io.github.rosemoe.sora.text.ContentListener() {
                @Override
                public void beforeReplace(@NonNull io.github.rosemoe.sora.text.Content content) {
                }

                @Override
                public void afterInsert(@NonNull io.github.rosemoe.sora.text.Content content,
                                        int startLine, int startColumn, int endLine, int endColumn,
                                        @NonNull CharSequence insertedContent) {
                    onContentChanged();
                }

                @Override
                public void afterDelete(@NonNull io.github.rosemoe.sora.text.Content content,
                                        int startLine, int startColumn, int endLine, int endColumn,
                                        @NonNull CharSequence deletedContent) {
                    onContentChanged();
                }
            });
        } catch (Throwable ignored) {
        }
    }

    private void onContentChanged() {
        if (!modified) {
            modified = true;
            if (modifiedListener != null) {
                modifiedListener.onModifiedChanged(filePath, true);
            }
        }
    }

    public boolean save() {
        if (editor == null || filePath == null) return false;
        try {
            FileUtil.writeFile(filePath, editor.getText().toString());
            modified = false;
            if (modifiedListener != null) {
                modifiedListener.onModifiedChanged(filePath, false);
            }
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public String getCode() {
        return editor == null ? "" : editor.getText().toString();
    }

    /** Перейти к строке. */
    /** Запросить переход к строке. */
    public void requestJumpToLine(int line) {
        if (line < 0) return;
        if (loaded && editor != null) {
            goToLine(line);
        } else {
            pendingLine = line;
        }
    }

    /** Перейти к строке. */
    public void goToLine(int line) {
        if (editor == null || line < 0) return;
        try {
            int total = editor.getText().getLineCount();
            if (total <= 0) return;
            if (line >= total) line = total - 1;
            if (line < 0) line = 0;
            final int ln = line;
            editor.setSelection(ln, 0);
            editor.post(() -> {
                try {
                    editor.setSelection(ln, 0);
                    editor.ensureSelectionVisible();
                } catch (Throwable ignored) {
                }
            });
        } catch (Throwable ignored) {
        }
    }

    public void undo() {
        if (editor != null) {
            try { editor.undo(); } catch (Throwable ignored) {}
        }
    }

    public void redo() {
        if (editor != null) {
            try { editor.redo(); } catch (Throwable ignored) {}
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean("modified", modified);
    }
}
