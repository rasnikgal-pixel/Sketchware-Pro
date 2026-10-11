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

        // 2. Язык подсветки — ДО текста
        try {
            int lang = SoraFileCollector.getLanguageForFile(filePath);
            if (lang == 0) {
                // Java — подсветка есть
                editor.setEditorLanguage(new io.github.rosemoe.sora.langs.java.JavaLanguage());
            } else if (lang == 1) {
                editor.setEditorLanguage(CodeEditorLanguages.loadTextMateLanguage(
                        CodeEditorLanguages.SCOPE_NAME_KOTLIN));
            }
            // XML: не ставим TextMate (ломает отображение), оставляем без подсветки
        } catch (Throwable ignored) {
        }

        // 3. Тема по приложению
        try {
            boolean isDark = pro.sketchware.utility.ThemeUtils.isDarkThemeEnabled(requireContext());
            SrcCodeEditor.selectTheme(editor, isDark ? 3 : 0);
        } catch (Throwable ignored) {
        }

        // 4. Читаем файл
        String content = null;
        try {
            content = FileUtil.readFile(filePath);
        } catch (Throwable ignored) {
        }
        initialContent = content == null ? "" : content;

        // 5. Ставим текст ПОСЛЕ всего остального
        editor.setText(initialContent);

        // 6. Курсор в начало
        try {
            editor.setSelection(0, 0);
        } catch (Throwable ignored) {
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
