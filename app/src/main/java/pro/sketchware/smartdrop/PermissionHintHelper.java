package pro.sketchware.smartdrop;

import android.content.Context;
import android.content.res.Configuration;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Подсказки о разрешениях Android для фичи "block-dependency-hints".
 *
 * Связывает компоненты Sketchware с разрешениями Android, которые те требуют:
 *   Camera          -> CAMERA
 *   LocationManager -> ACCESS_FINE_LOCATION
 *   SpeechToText    -> RECORD_AUDIO
 *   BluetoothConnect-> BLUETOOTH, BLUETOOTH_ADMIN
 *   FilePicker      -> READ_EXTERNAL_STORAGE
 *   RequestNetwork  -> INTERNET, ACCESS_NETWORK_STATE
 *   Vibrator        -> VIBRATE
 *
 * Данные читаются из assets/smartdrop/permission_map.json и permission_info.json.
 * Локализация — 5 языков (ru/en/zh/hi/uk), с fallback на en.
 */
public final class PermissionHintHelper {

    private static final String MAP_ASSET = "smartdrop/permission_map.json";
    private static final String INFO_ASSET = "smartdrop/permission_info.json";

    private static Map<String, List<String>> componentToPerms;
    private static Map<String, Map<String, Map<String, String>>> permInfo;

    private PermissionHintHelper() {}

    private static synchronized void ensureLoaded(Context ctx) {
        if (componentToPerms != null && permInfo != null) return;

        // Карта компонент -> разрешения
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(ctx.getAssets().open(MAP_ASSET)))) {
            Type t = new TypeToken<Map<String, List<String>>>() {}.getType();
            Map<String, List<String>> m = new Gson().fromJson(r, t);
            componentToPerms = (m != null) ? m : new HashMap<>();
        } catch (Throwable t) {
            componentToPerms = new HashMap<>();
        }

        // Информация о разрешениях
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(ctx.getAssets().open(INFO_ASSET)))) {
            Type t = new TypeToken<Map<String, Map<String, Map<String, String>>>>() {}.getType();
            Map<String, Map<String, Map<String, String>>> m = new Gson().fromJson(r, t);
            permInfo = (m != null) ? m : new HashMap<>();
        } catch (Throwable t) {
            permInfo = new HashMap<>();
        }
    }

    /** @return код языка: ru, en, zh, hi, uk (fallback: en). */
    private static String lang(Context ctx) {
        String code = "en";
        try {
            Configuration c = ctx.getResources().getConfiguration();
            if (c.locale != null && c.locale.getLanguage() != null) {
                code = c.locale.getLanguage();
            }
        } catch (Throwable ignored) {}
        return switch (code) {
            case "ru", "en", "zh", "hi", "uk" -> code;
            default -> "en";
        };
    }

    /** @return список разрешений (например, ["CAMERA"]), требующихся для компонента. */
    public static List<String> getPermissionsForComponent(Context ctx, String componentName) {
        ensureLoaded(ctx);
        if (componentName == null) return Collections.emptyList();
        List<String> list = componentToPerms.get(componentName);
        return (list != null) ? list : Collections.emptyList();
    }

    /** @return человеческое название разрешения (например, "Камера"). */
    public static String getTitle(Context ctx, String permName) {
        return pick(ctx, permName, "title", permName);
    }

    /** @return тег разрешения ("Опасное" / "Обычное"). */
    public static String getTag(Context ctx, String permName) {
        return pick(ctx, permName, "tag", "");
    }

    /** @return описание разрешения (одна-две строки для диалога). */
    public static String getDescription(Context ctx, String permName) {
        return pick(ctx, permName, "desc", "");
    }

    private static String pick(Context ctx, String permName, String field, String fallback) {
        ensureLoaded(ctx);
        Map<String, Map<String, String>> info = permInfo.get(permName);
        if (info == null) return fallback;
        Map<String, String> values = info.get(field);
        if (values == null) return fallback;
        String lang = lang(ctx);
        String v = values.get(lang);
        if (v == null || v.isEmpty()) v = values.get("en");
        if (v == null || v.isEmpty()) v = fallback;
        return v;
    }

    /**
     * Возвращает список отформатированных описаний разрешений для компонента,
     * готовых для показа в диалоге.
     */
    public static List<PermissionDescription> describeForComponent(Context ctx, String componentName) {
        List<PermissionDescription> out = new ArrayList<>();
        for (String perm : getPermissionsForComponent(ctx, componentName)) {
            out.add(new PermissionDescription(
                    perm,
                    getTitle(ctx, perm),
                    getTag(ctx, perm),
                    getDescription(ctx, perm)));
        }
        return out;
    }

    /** Простая модель для передачи в UI. */
    public static final class PermissionDescription {
        public final String name;
        public final String title;
        public final String tag;
        public final String description;

        public PermissionDescription(String name, String title, String tag, String description) {
            this.name = name;
            this.title = title;
            this.tag = tag;
            this.description = description;
        }
    }
}
