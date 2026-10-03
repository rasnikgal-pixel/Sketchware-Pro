package mod.jbk.util;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import pro.sketchware.utility.FileUtil;

/**
 * Writes a simple JSON journal of block-logic issues detected in projects.
 * File: /sdcard/.sketchware/block_logic_journal.json
 */
public final class BlockLogicJournal {

    private static final String FILE_PATH = FileUtil.getExternalStorageDir()
            + "/.sketchware/block_logic_journal.json";
    private static final int MAX_ENTRIES = 200;

    private BlockLogicJournal() {}

    public static void record(String scId, String eventName, List<String> issues) {
        if (issues == null || issues.isEmpty()) return;
        try {
            JSONArray arr = load();
            JSONObject entry = new JSONObject();
            entry.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US).format(new Date()));
            entry.put("sc_id", scId == null ? "" : scId);
            entry.put("event", eventName == null ? "" : eventName);
            JSONArray iss = new JSONArray();
            for (String s : issues) iss.put(s);
            entry.put("issues", iss);
            arr.put(entry);

            while (arr.length() > MAX_ENTRIES) {
                arr.remove(0);
            }

            FileUtil.writeFile(FILE_PATH, arr.toString(2));
        } catch (Throwable ignored) {}
    }

    private static JSONArray load() {
        try {
            if (!FileUtil.isExistFile(FILE_PATH)) return new JSONArray();
            String json = FileUtil.readFile(FILE_PATH);
            if (json == null || json.trim().isEmpty()) return new JSONArray();
            return new JSONArray(json);
        } catch (Throwable t) {
            return new JSONArray();
        }
    }
}
