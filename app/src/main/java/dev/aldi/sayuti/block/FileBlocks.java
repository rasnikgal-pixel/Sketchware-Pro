package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Блоки для работы с файлами. Используют FileUtil (уже регистрируется
 * в проекте пользователя через yq.java).
 * Разрешения: READ/WRITE_EXTERNAL_STORAGE (через yq.java).
 */
public final class FileBlocks {

    private FileBlocks() {}

    private static final String COLOR_OP = "#5cb722";
    private static final String COLOR_FUNC = "#4a6cd4";
    private static final String PALETTE_OP = "3";
    private static final String PALETTE_FUNC = "7";

    private static HashMap<String, Object> blk(String name, String type, String typeName,
                                                 String code, String color, String palette,
                                                 String spec) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("type", type);
        m.put("typeName", typeName);
        m.put("code", code);
        m.put("color", color);
        m.put("palette", palette);
        m.put("spec", spec);
        return m;
    }

    public static void addBlocks(ArrayList<HashMap<String, Object>> list) {
        addFileReadWriteBlocks(list);
        addFileOpsBlocks(list);
        addFilePathBlocks(list);
    }

    // ─────────────────────────────────────────────────────────────
    // Чтение / запись
    // ─────────────────────────────────────────────────────────────
    private static void addFileReadWriteBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("fileReadText", "s", "",
                "FileUtil.readFile(%1$s)",
                COLOR_FUNC, PALETTE_FUNC,
                "read text from file %s"));

        list.add(blk("fileReadTextIfExist", "s", "",
                "FileUtil.readFileIfExist(%1$s)",
                COLOR_FUNC, PALETTE_FUNC,
                "read text from file %s (if exists)"));

        list.add(blk("fileWriteText", " ", "",
                "FileUtil.writeFile(%1$s, %2$s);",
                COLOR_OP, PALETTE_OP,
                "write text %2$s to file %1$s"));

        list.add(blk("fileAppendText", " ", "",
                "FileUtil.writeFile(%1$s, FileUtil.readFileIfExist(%1$s) + %2$s);",
                COLOR_OP, PALETTE_OP,
                "append text %2$s to file %1$s"));
    }

    // ─────────────────────────────────────────────────────────────
    // Операции с файлами
    // ─────────────────────────────────────────────────────────────
    private static void addFileOpsBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("fileDelete", " ", "",
                "FileUtil.deleteFile(%1$s);",
                COLOR_OP, PALETTE_OP,
                "delete file %s"));

        list.add(blk("fileExists", "b", "",
                "FileUtil.isExistFile(%1$s)",
                COLOR_FUNC, PALETTE_FUNC,
                "file %s exists"));

        list.add(blk("fileIsDirectory", "b", "",
                "FileUtil.isDirectory(%1$s)",
                COLOR_FUNC, PALETTE_FUNC,
                "path %s is a directory"));

        list.add(blk("fileSize", "d", "",
                "(double) FileUtil.getFileLength(%1$s)",
                COLOR_FUNC, PALETTE_FUNC,
                "size of file %s in bytes"));

        list.add(blk("fileCopyTo", " ", "",
                "FileUtil.copyFile(%1$s, %2$s);",
                COLOR_OP, PALETTE_OP,
                "copy file %1$s to %2$s"));

        list.add(blk("fileMoveTo", " ", "",
                "FileUtil.moveFile(%1$s, %2$s);",
                COLOR_OP, PALETTE_OP,
                "move file %1$s to %2$s"));

        list.add(blk("fileMkdirs", " ", "",
                "FileUtil.makeDir(%1$s);",
                COLOR_OP, PALETTE_OP,
                "create directory %s"));

        list.add(blk("fileListDir", "s", "",
                "(() -> { StringBuilder _sb = new StringBuilder(); for (String _s : FileUtil.listFiles(%1$s, %2$s)) { _sb.append(_s).append(\"\\n\"); } return _sb.toString(); }).get()",
                COLOR_FUNC, PALETTE_FUNC,
                "list files in %s with extension %s"));
    }

    // ─────────────────────────────────────────────────────────────
    // Пути и имена
    // ─────────────────────────────────────────────────────────────
    private static void addFilePathBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("externalStorageDir", "s", "",
                "FileUtil.getExternalStorageDir()",
                COLOR_FUNC, PALETTE_FUNC,
                "external storage directory"));

        list.add(blk("fileGetName", "s", "",
                "FileUtil.getFileNameNoExtension(%1$s)",
                COLOR_FUNC, PALETTE_FUNC,
                "file name (no extension) of %s"));

        list.add(blk("fileGetExtension", "s", "",
                "FileUtil.getFileExtension(%1$s)",
                COLOR_FUNC, PALETTE_FUNC,
                "extension of file %s"));
    }
}
