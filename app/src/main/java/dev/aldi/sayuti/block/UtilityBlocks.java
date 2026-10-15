package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Дополнительные блоки-утилиты (без разрешений): уведомления, буфер обмена,
 * батарея, HTTP, JSON, системная информация, вибрация, share, prefs,
 * шифрование, дата/время.
 *
 * <p>Регистрируются через {@link #addBlocks(ArrayList)}.</p>
 */
public final class UtilityBlocks {

    private UtilityBlocks() {}

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
        addNotificationBlocks(list);
        addClipboardBlocks(list);
        addBatteryBlocks(list);
        addSystemInfoBlocks(list);
        addJsonBlocks(list);
        addVibrationBlocks(list);
        addShareBlocks(list);
        addPrefsBlocks(list);
        addEncryptionBlocks(list);
        addDateTimeBlocks(list);
    }

    // ─────────────────────────────────────────────────────────────
    // Уведомления
    // ─────────────────────────────────────────────────────────────
    private static void addNotificationBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("notificationShow", " ", "",
                "((android.app.NotificationManager) getApplicationContext().getSystemService(android.content.Context.NOTIFICATION_SERVICE)).notify(1, new androidx.core.app.NotificationCompat.Builder(getApplicationContext(), \\"default\\").setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle(%1$s).setContentText(%2$s).setAutoCancel(true).build());",
                COLOR_OP, PALETTE_OP,
                "show notification title %s text %s"));

        list.add(blk("notificationCancel", " ", "",
                "((android.app.NotificationManager) getApplicationContext().getSystemService(android.content.Context.NOTIFICATION_SERVICE)).cancel(%1$d);",
                COLOR_OP, PALETTE_OP,
                "cancel notification id %d"));

        list.add(blk("notificationCancelAll", " ", "",
                "((android.app.NotificationManager) getApplicationContext().getSystemService(android.content.Context.NOTIFICATION_SERVICE)).cancelAll();",
                COLOR_OP, PALETTE_OP,
                "cancel all notifications"));
    }

    // ─────────────────────────────────────────────────────────────
    // Буфер обмена
    // ─────────────────────────────────────────────────────────────
    private static void addClipboardBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("clipboardCopy", " ", "",
                "((android.content.ClipboardManager) getApplicationContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)).setPrimaryClip(android.content.ClipData.newPlainText(\"text\", %1$s));",
                COLOR_OP, PALETTE_OP,
                "copy to clipboard text %s"));

        list.add(blk("clipboardPaste", "s", "",
                "(((android.content.ClipboardManager) getApplicationContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)).getPrimaryClip() != null && ((android.content.ClipboardManager) getApplicationContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)).getPrimaryClip().getItemCount() > 0) ? ((android.content.ClipboardManager) getApplicationContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)).getPrimaryClip().getItemAt(0).coerceToText(getApplicationContext()).toString() : \"\"",
                COLOR_FUNC, PALETTE_FUNC,
                "paste from clipboard"));

        list.add(blk("clipboardClear", " ", "",
                "((android.content.ClipboardManager) getApplicationContext().getSystemService(android.content.Context.CLIPBOARD_SERVICE)).clearPrimaryClip();",
                COLOR_OP, PALETTE_OP,
                "clear clipboard"));
    }

    // ─────────────────────────────────────────────────────────────
    // Батарея
    // ─────────────────────────────────────────────────────────────
    private static void addBatteryBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("batteryLevel", "d", "",
                "getApplicationContext().registerReceiver(null, new android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED)).getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, -1)",
                COLOR_FUNC, PALETTE_FUNC,
                "battery level percent"));

        list.add(blk("isCharging", "b", "",
                "getApplicationContext().registerReceiver(null, new android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED)).getIntExtra(android.os.BatteryManager.EXTRA_STATUS, -1) == android.os.BatteryManager.BATTERY_STATUS_CHARGING",
                COLOR_FUNC, PALETTE_FUNC,
                "battery is charging"));
    }

    // ─────────────────────────────────────────────────────────────
    // Системная информация
    // ─────────────────────────────────────────────────────────────
    private static void addSystemInfoBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("deviceModel", "s", "",
                "android.os.Build.MODEL",
                COLOR_FUNC, PALETTE_FUNC,
                "device model"));

        list.add(blk("androidVersion", "s", "",
                "android.os.Build.VERSION.RELEASE",
                COLOR_FUNC, PALETTE_FUNC,
                "android version"));

        list.add(blk("screenWidth", "d", "",
                "getApplicationContext().getResources().getDisplayMetrics().widthPixels",
                COLOR_FUNC, PALETTE_FUNC,
                "screen width pixels"));

        list.add(blk("screenHeight", "d", "",
                "getApplicationContext().getResources().getDisplayMetrics().heightPixels",
                COLOR_FUNC, PALETTE_FUNC,
                "screen height pixels"));
    }

    // ─────────────────────────────────────────────────────────────
    // JSON
    // ─────────────────────────────────────────────────────────────
    private static void addJsonBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("jsonGetString", "s", "",
                "new org.json.JSONObject(%1$s).getString(%2$s)",
                COLOR_FUNC, PALETTE_FUNC,
                "JSON %s get string key %s"));

        list.add(blk("jsonGetInt", "d", "",
                "new org.json.JSONObject(%1$s).getInt(%2$s)",
                COLOR_FUNC, PALETTE_FUNC,
                "JSON %s get int key %s"));

        list.add(blk("jsonGetBool", "b", "",
                "new org.json.JSONObject(%1$s).getBoolean(%2$s)",
                COLOR_FUNC, PALETTE_FUNC,
                "JSON %s get boolean key %s"));

        list.add(blk("jsonArrayLength", "d", "",
                "new org.json.JSONArray(%1$s).length()",
                COLOR_FUNC, PALETTE_FUNC,
                "JSON array %s length"));
    }

    // ─────────────────────────────────────────────────────────────
    // Вибрация
    // ─────────────────────────────────────────────────────────────
    private static void addVibrationBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("vibrateShort", " ", "",
                "((android.os.Vibrator) getApplicationContext().getSystemService(android.content.Context.VIBRATOR_SERVICE)).vibrate(200);",
                COLOR_OP, PALETTE_OP,
                "vibrate 200ms"));

        list.add(blk("vibratePattern", " ", "",
                "((android.os.Vibrator) getApplicationContext().getSystemService(android.content.Context.VIBRATOR_SERVICE)).vibrate(new long[]{%1$s}, -1);",
                COLOR_OP, PALETTE_OP,
                "vibrate pattern %s (comma-separated ms)"));
    }

    // ─────────────────────────────────────────────────────────────
    // Поделиться / Открыть
    // ─────────────────────────────────────────────────────────────
    private static void addShareBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("openUrl", " ", "",
                "startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(%1$s)));",
                COLOR_OP, PALETTE_OP,
                "open URL %s in browser"));

        list.add(blk("shareText", " ", "",
                "android.content.Intent _shareIntent = new android.content.Intent(android.content.Intent.ACTION_SEND);\n"
                + "_shareIntent.setType(\"text/plain\");\n"
                + "_shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, %1$s);\n"
                + "startActivity(android.content.Intent.createChooser(_shareIntent, %2$s));",
                COLOR_OP, PALETTE_OP,
                "share text %s with chooser title %s"));

        list.add(blk("sendEmail", " ", "",
                "android.content.Intent _emailIntent = new android.content.Intent(android.content.Intent.ACTION_SENDTO);\n"
                + "_emailIntent.setData(android.net.Uri.parse(\"mailto:\" + %1$s));\n"
                + "_emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, %2$s);\n"
                + "_emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, %3$s);\n"
                + "startActivity(_emailIntent);",
                COLOR_OP, PALETTE_OP,
                "send email to %s subject %s body %s"));
    }

    // ─────────────────────────────────────────────────────────────
    // Настройки (SharedPreferences)
    // ─────────────────────────────────────────────────────────────
    private static void addPrefsBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("prefsSave", " ", "",
                "getApplicationContext().getSharedPreferences(\"app_prefs\", android.content.Context.MODE_PRIVATE).edit().putString(%1$s, %2$s).apply();",
                COLOR_OP, PALETTE_OP,
                "prefs save key %s value %s"));

        list.add(blk("prefsLoad", "s", "",
                "getApplicationContext().getSharedPreferences(\"app_prefs\", android.content.Context.MODE_PRIVATE).getString(%1$s, %2$s)",
                COLOR_FUNC, PALETTE_FUNC,
                "prefs load key %s default %s"));

        list.add(blk("prefsRemove", " ", "",
                "getApplicationContext().getSharedPreferences(\"app_prefs\", android.content.Context.MODE_PRIVATE).edit().remove(%1$s).apply();",
                COLOR_OP, PALETTE_OP,
                "prefs remove key %s"));

        list.add(blk("prefsClear", " ", "",
                "getApplicationContext().getSharedPreferences(\"app_prefs\", android.content.Context.MODE_PRIVATE).edit().clear().apply();",
                COLOR_OP, PALETTE_OP,
                "prefs clear all"));
    }

    // ─────────────────────────────────────────────────────────────
    // Шифрование и хэши
    // ─────────────────────────────────────────────────────────────
    private static void addEncryptionBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("base64Encode", "s", "",
                "android.util.Base64.encodeToString(%1$s.getBytes(), android.util.Base64.NO_WRAP)",
                COLOR_FUNC, PALETTE_FUNC,
                "Base64 encode %s"));

        list.add(blk("base64Decode", "s", "",
                "new String(android.util.Base64.decode(%1$s, android.util.Base64.NO_WRAP))",
                COLOR_FUNC, PALETTE_FUNC,
                "Base64 decode %s"));

        list.add(blk("md5", "s", "",
                "pro.sketchware.utility.UtilityBlocksHashHelper.md5(%1$s)",
                COLOR_FUNC, PALETTE_FUNC,
                "MD5 hash of %s"));

        list.add(blk("sha256", "s", "",
                "pro.sketchware.utility.UtilityBlocksHashHelper.sha256(%1$s)",
                COLOR_FUNC, PALETTE_FUNC,
                "SHA-256 hash of %s"));
    }

    // ─────────────────────────────────────────────────────────────
    // Дата и время
    // ─────────────────────────────────────────────────────────────
    private static void addDateTimeBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("nowMillis", "d", "",
                "System.currentTimeMillis()",
                COLOR_FUNC, PALETTE_FUNC,
                "current time in millis"));

        list.add(blk("formatDate", "s", "",
                "new java.text.SimpleDateFormat(%1$s).format(new java.util.Date(%2$s))",
                COLOR_FUNC, PALETTE_FUNC,
                "format date pattern %s time %d"));

        list.add(blk("parseDate", "d", "",
                "new java.text.SimpleDateFormat(%1$s).parse(%2$s).getTime()",
                COLOR_FUNC, PALETTE_FUNC,
                "parse date %s pattern %s to millis"));
    }
}
