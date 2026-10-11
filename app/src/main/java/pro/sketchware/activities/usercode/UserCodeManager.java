package pro.sketchware.activities.usercode;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import a.a.a.wq;
import a.a.a.yq;
import pro.sketchware.utility.FileUtil;

/**
 * Управляет пользовательскими Java-классами в папке user/.
 *
 * Файлы из этой папки не перезаписываются Sketchware,
 * но компилируются вместе с проектом.
 */
public class UserCodeManager {

    public static final String USER_FOLDER = "user";

    private final String scId;

    public UserCodeManager(String scId) {
        this.scId = scId;
    }

    /** Абсолютный путь к папке user/ проекта. */
    public String getUserDir() {
        try {
            yq projectData = new yq(
                    pro.sketchware.SketchApplication.getContext(), scId);
            String javaPath = projectData.javaFilesPath;
            String pkgPath = projectData.packageNameAsFolders;
            if (javaPath == null || pkgPath == null) return null;
            return javaPath + File.separator + pkgPath + File.separator + USER_FOLDER;
        } catch (Throwable t) {
            return null;
        }
    }

    /** Пакет для пользовательских классов. */
    public String getUserPackage() {
        try {
            yq projectData = new yq(
                    pro.sketchware.SketchApplication.getContext(), scId);
            String pkg = projectData.packageName;
            if (pkg == null) return null;
            return pkg + "." + USER_FOLDER;
        } catch (Throwable t) {
            return null;
        }
    }

    /** Создаёт папку user/, если её нет. */
    public boolean ensureUserDir() {
        String path = getUserDir();
        if (path == null) return false;
        File dir = new File(path);
        if (dir.exists() && dir.isDirectory()) return true;
        try {
            FileUtil.makeDir(path);
            return dir.exists();
        } catch (Throwable t) {
            return false;
        }
    }

    /** Список .java файлов в папке user/. */
    public List<File> listFiles() {
        List<File> result = new ArrayList<>();
        String path = getUserDir();
        if (path == null) return result;
        File dir = new File(path);
        if (!dir.exists() || !dir.isDirectory()) return result;
        File[] files = dir.listFiles();
        if (files == null) return result;
        for (File f : files) {
            if (f.isFile() && f.getName().endsWith(".java")) {
                result.add(f);
            }
        }
        result.sort((a, b) -> a.getName().compareToIgnoreCase(b.getName()));
        return result;
    }

    /** Проверка — существует ли файл. */
    public boolean fileExists(String className) {
        String path = getUserDir();
        if (path == null) return false;
        return new File(path, className + ".java").exists();
    }

    /** Валидация имени класса. */
    public static boolean isValidClassName(String name) {
        if (name == null || name.isEmpty()) return false;
        if (!Character.isJavaIdentifierStart(name.charAt(0))) return false;
        for (int i = 1; i < name.length(); i++) {
            if (!Character.isJavaIdentifierPart(name.charAt(i))) return false;
        }
        // Запрещаем ключевые слова
        String[] keywords = {"class", "public", "private", "protected", "static",
                "final", "void", "int", "boolean", "String", "if", "else",
                "for", "while", "return", "new", "this", "super"};
        for (String kw : keywords) {
            if (kw.equals(name)) return false;
        }
        return true;
    }

    /** Создаёт новый файл с шаблоном. */
    public boolean createFile(String className, int template) {
        if (!isValidClassName(className)) return false;
        if (fileExists(className)) return false;
        if (!ensureUserDir()) return false;

        String pkg = getUserPackage();
        if (pkg == null) return false;
        String path = getUserDir();
        if (path == null) return false;

        String code = getTemplate(pkg, className, template);
        try {
            FileUtil.writeFile(path + File.separator + className + ".java", code);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /** Удаляет файл. */
    public boolean deleteFile(String className) {
        String path = getUserDir();
        if (path == null) return false;
        File f = new File(path, className + ".java");
        if (!f.exists()) return false;
        return f.delete();
    }

    /** Переименовывает файл (заменяет имя класса внутри). */
    public boolean renameFile(String oldName, String newName) {
        if (!isValidClassName(newName)) return false;
        String path = getUserDir();
        if (path == null) return false;
        File oldFile = new File(path, oldName + ".java");
        File newFile = new File(path, newName + ".java");
        if (!oldFile.exists() || newFile.exists()) return false;
        try {
            String content = FileUtil.readFile(oldFile.getAbsolutePath());
            if (content != null) {
                content = content.replaceAll(
                        "\\bclass\\s+" + oldName + "\\b",
                        "class " + newName);
                content = content.replaceAll(
                        "\\binterface\\s+" + oldName + "\\b",
                        "interface " + newName);
                // Простая замена конструкторов — по имени класса
                content = content.replaceAll(
                        "\\b" + oldName + "\\s*\\(",
                        newName + "(");
                FileUtil.writeFile(newFile.getAbsolutePath(), content);
            } else {
                return oldFile.renameTo(newFile);
            }
            oldFile.delete();
            return newFile.exists();
        } catch (Throwable t) {
            return false;
        }
    }

    /** Шаблоны кода. */
    public static String getTemplate(String pkg, String className, int template) {
        switch (template) {
            case 0: // Пустой класс
                return "package " + pkg + ";\n\n" +
                        "public class " + className + " {\n" +
                        "    // Твой код\n" +
                        "}\n";
            case 1: // Утилита со статическими методами
                return "package " + pkg + ";\n\n" +
                        "public final class " + className + " {\n\n" +
                        "    private " + className + "() {\n" +
                        "        // Запрет создания экземпляра\n" +
                        "    }\n\n" +
                        "    public static void hello() {\n" +
                        "        // Пример статического метода\n" +
                        "    }\n" +
                        "}\n";
            case 2: // Кастомный View
                return "package " + pkg + ";\n\n" +
                        "import android.content.Context;\n" +
                        "import android.util.AttributeSet;\n" +
                        "import android.view.View;\n\n" +
                        "public class " + className + " extends View {\n\n" +
                        "    public " + className + "(Context context) {\n" +
                        "        super(context);\n" +
                        "        init();\n" +
                        "    }\n\n" +
                        "    public " + className + "(Context context, AttributeSet attrs) {\n" +
                        "        super(context, attrs);\n" +
                        "        init();\n" +
                        "    }\n\n" +
                        "    private void init() {\n" +
                        "        // Инициализация\n" +
                        "    }\n" +
                        "}\n";
            case 3: // Интерфейс
                return "package " + pkg + ";\n\n" +
                        "public interface " + className + " {\n" +
                        "    void onAction();\n" +
                        "}\n";
            default:
                return getTemplate(pkg, className, 0);
        }
    }
}
