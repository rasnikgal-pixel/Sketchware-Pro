package mod.jbk.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pro.sketchware.utility.FileUtil;

/**
 * Manages the user's favorite blocks shown in the "Favorites" palette category
 * (id 100). Storage: .sketchware/data/favorite_blocks.json
 *
 * Each entry: {"name":"<blockId>","type":"<s|d|b| >","typeName":"<optional>"}.
 */
public final class FavoriteBlocksManager {

    public static final int FAVORITE_PALETTE_ID = 100;
    public static final int FAVORITE_PALETTE_COLOR = 0xffd4af37; // gold

    private static final String FILE_PATH = FileUtil.getExternalStorageDir()
            + "/.sketchware/data/favorite_blocks.json";

    private FavoriteBlocksManager() {}

    /** Returns the list of favorite blocks (never null). Also seeds defaults on first run. */
    public static List<Map<String, String>> getAll() {
        if (!FileUtil.isExistFile(FILE_PATH)) {
            seedDefaults();
        }
        try {
            String json = FileUtil.readFile(FILE_PATH);
            if (json == null || json.trim().isEmpty()) return new ArrayList<>();
            List<Map<String, String>> list = new Gson().fromJson(json,
                    new TypeToken<List<Map<String, String>>>() {}.getType());
            return list != null ? list : new ArrayList<>();
        } catch (Throwable t) {
            return new ArrayList<>();
        }
    }

    public static boolean contains(String name) {
        if (name == null || name.isEmpty()) return false;
        for (Map<String, String> entry : getAll()) {
            if (name.equals(entry.get("name"))) return true;
        }
        return false;
    }

    public static void add(String name, String type, String typeName) {
        if (name == null || name.isEmpty()) return;
        if (contains(name)) return;
        List<Map<String, String>> list = getAll();
        Map<String, String> entry = new LinkedHashMap<>();
        entry.put("name", name);
        entry.put("type", type == null ? " " : type);
        entry.put("typeName", typeName == null ? "" : typeName);
        list.add(entry);
        save(list);
    }

    public static void remove(String name) {
        if (name == null || name.isEmpty()) return;
        List<Map<String, String>> list = getAll();
        list.removeIf(e -> name.equals(e.get("name")));
        save(list);
    }

    private static void save(List<Map<String, String>> list) {
        try {
            FileUtil.writeFile(FILE_PATH, new Gson().toJson(list));
        } catch (Throwable ignored) {}
    }

    private static void seedDefaults() {
        List<Map<String, String>> list = new ArrayList<>();
        // Defaults: common blocks that almost every project uses.
        addTo(list, "customToast", " ", "");
        addTo(list, "dialogShow", " ", "");
        addTo(list, "dialogDismiss", " ", "");
        addTo(list, "setVarBoolean", " ", "");
        addTo(list, "setVarInt", " ", "");
        addTo(list, "setVarString", " ", "");
        addTo(list, "mathPi", "d", "");
        addTo(list, "mathRandom", "d", "");
        addTo(list, "getResString", "s", "");
        addTo(list, "fileutilread", "s", "");
        addTo(list, "fileutilwrite", " ", "");
        save(list);
    }

    private static void addTo(List<Map<String, String>> list, String name, String type, String typeName) {
        Map<String, String> entry = new LinkedHashMap<>();
        entry.put("name", name);
        entry.put("type", type);
        entry.put("typeName", typeName);
        list.add(entry);
    }
}
