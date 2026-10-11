package pro.sketchware.activities.soraeditor;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import a.a.a.wq;
import pro.sketchware.utility.FileUtil;

/**
 * Собирает список файлов проекта, доступных для ручного редактирования
 * в SORA редакторе.
 *
 * Включает: XML, Java, Kotlin, Gradle, ProGuard, JSON, properties, txt.
 * Исключает: сгенерированные файлы, служебные данные Sketchware, бинарники.
 */
public class SoraFileCollector {

    /** Расширения файлов, которые можно редактировать. */
    private static final List<String> EDITABLE_EXTENSIONS = Arrays.asList(
            "xml", "java", "kt", "gradle", "pro", "json", "properties", "txt"
    );

    /** Папки внутри проекта, которые не нужно показывать. */
    private static final List<String> EXCLUDED_DIRS = Arrays.asList(
            "/build/",
            "/gen/",
            "/.gradle/",
            "/.idea/"
    );

    private final String scId;

    public SoraFileCollector(String scId) {
        this.scId = scId;
    }

    /**
     * Возвращает список абсолютных путей к файлам проекта,
     * которые можно редактировать.
     */
    public List<String> collect() {
        List<String> result = new ArrayList<>();

        // 1. Основные файлы проекта: .sketchware/mysc/<sc_id>/app/src/main/
        String projectRoot = wq.d(scId);
        if (projectRoot != null) {
            File srcMain = new File(projectRoot, "app/src/main");
            File searchRoot = srcMain.exists() && srcMain.isDirectory()
                    ? srcMain : new File(projectRoot);
            if (searchRoot.exists() && searchRoot.isDirectory()) {
                collectFrom(searchRoot, result);
            }
        }

        // 2. ProGuard-правила: .sketchware/data/<sc_id>/proguard-rules.pro
        String dataRoot = wq.b(scId);
        if (dataRoot != null) {
            File proguard = new File(dataRoot, "proguard-rules.pro");
            if (proguard.exists() && proguard.isFile()) {
                result.add(proguard.getAbsolutePath());
            }
        }

        result.sort(String::compareTo);
        return result;
    }

    private void collectFrom(File root, List<String> result) {
        for (String ext : EDITABLE_EXTENSIONS) {
            List<File> found = FileUtil.listFilesRecursively(root, ext);
            if (found == null) continue;
            for (File f : found) {
                if (f == null || !f.isFile()) continue;
                if (isExcluded(f.getAbsolutePath())) continue;
                String abs = f.getAbsolutePath();
                if (!result.contains(abs)) {
                    result.add(abs);
                }
            }
        }
    }

    private boolean isExcluded(String absolutePath) {
        String normalized = absolutePath.replace(File.separatorChar, '/');
        for (String dir : EXCLUDED_DIRS) {
            if (normalized.contains(dir)) return true;
        }
        return false;
    }

    /**
     * Возвращает короткое имя файла относительно корня проекта.
     * Используется для заголовка вкладки.
     */
    public String getShortName(String absolutePath) {
        String projectRoot = wq.d(scId);
        if (projectRoot == null) return new File(absolutePath).getName();
        String normalizedRoot = projectRoot.replace(File.separatorChar, '/');
        String normalizedPath = absolutePath.replace(File.separatorChar, '/');
        if (normalizedPath.startsWith(normalizedRoot)) {
            String rel = normalizedPath.substring(normalizedRoot.length());
            if (rel.startsWith("/")) rel = rel.substring(1);
            return rel;
        }
        return new File(absolutePath).getName();
    }

    /**
     * Определяет язык подсветки по расширению файла.
     * 0 - Java, 1 - Kotlin, 2 - XML, -1 - нет подсветки.
     */
    public static int getLanguageForFile(String path) {
        String lower = path.toLowerCase(Locale.ROOT);
        if (lower.endsWith(".java")) return 0;
        if (lower.endsWith(".kt")) return 1;
        if (lower.endsWith(".xml")) return 2;
        return -1;
    }
}
