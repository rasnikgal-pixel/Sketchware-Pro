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
 * Ленивая загрузка: CodeEditor инициализируется только когда вкладка становится видимой.
 */
public class SoraEditorFragment extends Fragment {

    public static final String ARG_FILE_PATH = "file_path";

    private CodeEditor editor;
    private String filePath;
    private boolean loaded = false;
    private boolean modified = false;
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
        if (getArguments() != null) {
            filePath = getArguments().getString(ARG_FILE_PATH);
        }
        // Настройки из общих (размер шрифта, wrap) — тему ставим отдельно
        SrcCodeEditor.loadCESettings(requireContext(), editor, "sora_editor", false);

        // Тема редактора следует за темой приложения
        boolean isDark = pro.sketchware.utility.ThemeUtils.isDarkThemeEnabled(requireContext());
        // 3 = Darcula (тёмная), 0 = Default (светлая)
        SrcCodeEditor.selectTheme(editor, isDark ? 3 : 0);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!loaded && editor != null && filePath != null) {
            loadEditor();
            loaded = true;
        }
    }

    private void loadEditor() {
        // Язык подсветки по расширению
        int lang = SoraFileCollector.getLanguageForFile(filePath);
        if (lang == 2) {
            editor.setEditorLanguage(CodeEditorLanguages.loadTextMateLanguage(
                    CodeEditorLanguages.SCOPE_NAME_XML));
        } else if (lang == 0) {
            editor.setEditorLanguage(new io.github.rosemoe.sora.langs.java.JavaLanguage());
        } else if (lang == 1) {
            editor.setEditorLanguage(CodeEditorLanguages.loadTextMateLanguage(
                    CodeEditorLanguages.SCOPE_NAME_KOTLIN));
        }

        // Чтение файла
        String content = FileUtil.readFile(filePath);
        editor.setText(content == null ? "" : content);

        // Отслеживание изменений
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
    }

    private void onContentChanged() {
        if (!modified) {
            modified = true;
            if (modifiedListener != null) {
                modifiedListener.onModifiedChanged(filePath, true);
            }
        }
    }

    /**
     * Сохраняет содержимое редактора в файл.
     * @return true если сохранение удалось.
     */
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

    /**
     * Возвращает код текущего файла.
     */
    public String getCode() {
        return editor == null ? "" : editor.getText().toString();
    }

    public void undo() {
        if (editor != null) editor.undo();
    }

    public void redo() {
        if (editor != null) editor.redo();
    }
}
