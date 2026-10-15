package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Дополнительные блоки для работы с системными API Android, требующими
 * разрешений: SMS, контакты, звонки, NFC, биометрия, обои, будильники,
 * wakelock, аккаунты, состояние телефона, установленные приложения, Wi-Fi.
 *
 * <p>Разрешения для этих блоков регистрируются в {@code yq.java} через
 * {@code N.addExtraPermission("...")} и записываются в манифест через
 * {@code Ix.java}.</p>
 *
 * <p>Использование: {@code SecurityBlocks.addBlocks(arrayList)} — вызывается
 * из {@code BlocksHandler.builtInBlocks(...)}.</p>
 */
public final class SecurityBlocks {

    private SecurityBlocks() {}

    /** Цвет блоков — зелёный (совпадает с палитрой "Операторы"). */
    private static final String COLOR_OPERATOR = "#5cb722";
    /** Палитра "Операторы" (id 3). */
    private static final String PALETTE_OPERATOR = "3";

    /** Цвет блоков-функций — синий. */
    private static final String COLOR_FUNC = "#4a6cd4";
    /** Палитра "Функции компонентов" (id 7). */
    private static final String PALETTE_FUNC = "7";

    private static HashMap<String, Object> block(String name, String type, String typeName,
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
        addSmsBlocks(list);
        addContactsBlocks(list);
        addCallBlocks(list);
        addNfcBlocks(list);
        addBiometricBlocks(list);
        addWallpaperBlocks(list);
        addAlarmBlocks(list);
        addWakelockBlocks(list);
        addAccountsBlocks(list);
        addPhoneStateBlocks(list);
        addPackagesBlocks(list);
        addWifiBlocks(list);
    }

    // ─────────────────────────────────────────────────────────────
    // SMS
    // ─────────────────────────────────────────────────────────────
    private static void addSmsBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("smsSend", " ", "",
                "android.telephony.SmsManager.getDefault().sendTextMessage(%1$s, null, %2$s, null, null);",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "send SMS to %s with text %s"));

        list.add(block("smsGetCount", "d", "",
                "getContentResolver().query(android.net.Uri.parse(\"content://sms/inbox\"), null, null, null, null).getCount()",
                COLOR_FUNC, PALETTE_FUNC,
                "SMS inbox count"));

        list.add(block("smsGetBodyByIndex", "s", "",
                "((android.database.Cursor) getContentResolver().query(android.net.Uri.parse(\"content://sms/inbox\"), null, null, null, null)).moveToPosition(%1$d);\n"
                + "((android.database.Cursor) getContentResolver().query(android.net.Uri.parse(\"content://sms/inbox\"), null, null, null, null)).getString(((android.database.Cursor) getContentResolver().query(android.net.Uri.parse(\"content://sms/inbox\"), null, null, null, null)).getColumnIndexOrThrow(\"body\"))",
                COLOR_FUNC, PALETTE_FUNC,
                "SMS body at index %d"));
    }

    // ─────────────────────────────────────────────────────────────
    // Контакты
    // ─────────────────────────────────────────────────────────────
    private static void addContactsBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("contactsGetCount", "d", "",
                "getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null).getCount()",
                COLOR_FUNC, PALETTE_FUNC,
                "contacts count"));

        list.add(block("contactsReadAllNames", "s", "",
                "android.database.Cursor _c = getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);\n"
                + "StringBuilder _sb = new StringBuilder();\n"
                + "while (_c.moveToNext()) { _sb.append(_c.getString(_c.getColumnIndexOrThrow(android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))).append(\"\\n\"); }\n"
                + "_c.close(); _sb.toString()",
                COLOR_FUNC, PALETTE_FUNC,
                "read all contact names"));

        list.add(block("contactsReadByName", "s", "",
                "android.database.Cursor _c = getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + \" = ?\", new String[]{%1$s}, null);\n"
                + "String _res = \"\";\n"
                + "if (_c.moveToFirst()) { _res = _c.getString(_c.getColumnIndexOrThrow(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER)); }\n"
                + "_c.close(); _res",
                COLOR_FUNC, PALETTE_FUNC,
                "phone number of contact %s"));

        list.add(block("contactsAdd", " ", "",
                "android.content.ContentValues _cv = new android.content.ContentValues();\n"
                + "_cv.put(android.provider.ContactsContract.Data.MIMETYPE, android.provider.ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE);\n"
                + "_cv.put(android.provider.ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, %1$s);\n"
                + "getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, _cv);",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "add contact with name %s"));
    }

    // ─────────────────────────────────────────────────────────────
    // Звонки
    // ─────────────────────────────────────────────────────────────
    private static void addCallBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("callPhoneDirect", " ", "",
                "startActivity(new android.content.Intent(android.content.Intent.ACTION_CALL, android.net.Uri.parse(\"tel:\" + %1$s)));",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "call phone number %s"));

        list.add(block("callPhoneDial", " ", "",
                "startActivity(new android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse(\"tel:\" + %1$s)));",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "open dialer with number %s"));
    }

    // ─────────────────────────────────────────────────────────────
    // NFC
    // ─────────────────────────────────────────────────────────────
    private static void addNfcBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("nfcIsAvailable", "b", "",
                "(android.nfc.NfcAdapter.getDefaultAdapter(getApplicationContext()) != null)",
                COLOR_FUNC, PALETTE_FUNC,
                "NFC is available"));

        list.add(block("nfcIsEnabled", "b", "",
                "(android.nfc.NfcAdapter.getDefaultAdapter(getApplicationContext()) != null && android.nfc.NfcAdapter.getDefaultAdapter(getApplicationContext()).isEnabled())",
                COLOR_FUNC, PALETTE_FUNC,
                "NFC is enabled"));
    }

    // ─────────────────────────────────────────────────────────────
    // Биометрия
    // ─────────────────────────────────────────────────────────────
    private static void addBiometricBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("biometricIsAvailable", "b", "",
                "new androidx.biometric.BiometricManager(getApplicationContext()).canAuthenticate(androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_WEAK | androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL) == androidx.biometric.BiometricManager.BIOMETRIC_SUCCESS",
                COLOR_FUNC, PALETTE_FUNC,
                "biometric is available"));

        list.add(block("biometricAuthenticate", " ", "",
                "new androidx.biometric.BiometricPrompt((androidx.fragment.app.FragmentActivity) %1$s, androidx.core.content.ContextCompat.getMainExecutor(%1$s), new androidx.biometric.BiometricPrompt.AuthenticationCallback() { @Override public void onAuthenticationSucceeded(androidx.biometric.BiometricPrompt.AuthenticationResult _r) { } }).authenticate(new androidx.biometric.BiometricPrompt.PromptInfo.Builder().setTitle(%2$s).setNegativeButtonText(\"Cancel\").build());",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "biometric authenticate in %m.activity title %s"));
    }

    // ─────────────────────────────────────────────────────────────
    // Обои
    // ─────────────────────────────────────────────────────────────
    private static void addWallpaperBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("wallpaperSetFromFile", " ", "",
                "android.app.WallpaperManager.getInstance(getApplicationContext()).setBitmap(android.graphics.BitmapFactory.decodeFile(%1$s));",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "set wallpaper from file %s"));
    }

    // ─────────────────────────────────────────────────────────────
    // Будильники
    // ─────────────────────────────────────────────────────────────
    private static void addAlarmBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("alarmSet", " ", "",
                "android.content.Intent _alarmIntent = new android.content.Intent(getApplicationContext(), %1$s.class);\n"
                + "android.app.PendingIntent _pi = android.app.PendingIntent.getActivity(getApplicationContext(), 0, _alarmIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE);\n"
                + "((android.app.AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE)).setExact(android.app.AlarmManager.RTC_WAKEUP, %2$d, _pi);",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "set alarm at %d start activity %m.activity"));

        list.add(block("alarmCancel", " ", "",
                "android.content.Intent _alarmIntent = new android.content.Intent(getApplicationContext(), %1$s.class);\n"
                + "android.app.PendingIntent _pi = android.app.PendingIntent.getActivity(getApplicationContext(), 0, _alarmIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE);\n"
                + "((android.app.AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE)).cancel(_pi);",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "cancel alarm start activity %m.activity"));
    }

    // ─────────────────────────────────────────────────────────────
    // WakeLock
    // ─────────────────────────────────────────────────────────────
    private static void addWakelockBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("wakelockAcquire", " ", "",
                "android.os.PowerManager.WakeLock _wl = ((android.os.PowerManager) getSystemService(android.content.Context.POWER_SERVICE)).newWakeLock(android.os.PowerManager.PARTIAL_WAKE_LOCK, \"Sketchware::Wakelock\");\n"
                + "_wl.acquire();",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "acquire partial wakelock"));

        list.add(block("wakelockRelease", " ", "",
                "android.os.PowerManager.WakeLock _wl = ((android.os.PowerManager) getSystemService(android.content.Context.POWER_SERVICE)).newWakeLock(android.os.PowerManager.PARTIAL_WAKE_LOCK, \"Sketchware::Wakelock\");\n"
                + "if (_wl.isHeld()) _wl.release();",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "release partial wakelock"));
    }

    // ─────────────────────────────────────────────────────────────
    // Аккаунты
    // ─────────────────────────────────────────────────────────────
    private static void addAccountsBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("accountsGetCount", "d", "",
                "android.accounts.AccountManager.get(getApplicationContext()).getAccounts().length",
                COLOR_FUNC, PALETTE_FUNC,
                "accounts count"));

        list.add(block("accountsGetNames", "s", "",
                "android.accounts.Account[] _accounts = android.accounts.AccountManager.get(getApplicationContext()).getAccounts();\n"
                + "StringBuilder _sb = new StringBuilder();\n"
                + "for (android.accounts.Account _a : _accounts) { _sb.append(_a.name).append(\"\\n\"); }\n"
                + "_sb.toString()",
                COLOR_FUNC, PALETTE_FUNC,
                "accounts names"));
    }
    // ─────────────────────────────────────────────────────────────
    // Состояние телефона
    // ─────────────────────────────────────────────────────────────
    private static void addPhoneStateBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("getOperatorName", "s", "",
                "((android.telephony.TelephonyManager) getSystemService(android.content.Context.TELEPHONY_SERVICE)).getNetworkOperatorName()",
                COLOR_FUNC, PALETTE_FUNC,
                "get carrier name"));

        list.add(block("getNetworkCountryIso", "s", "",
                "((android.telephony.TelephonyManager) getSystemService(android.content.Context.TELEPHONY_SERVICE)).getNetworkCountryIso()",
                COLOR_FUNC, PALETTE_FUNC,
                "get network country ISO"));

        list.add(block("isCallActive", "b", "",
                "(((android.telephony.TelephonyManager) getSystemService(android.content.Context.TELEPHONY_SERVICE)).getCallState() != android.telephony.TelephonyManager.CALL_STATE_IDLE)",
                COLOR_FUNC, PALETTE_FUNC,
                "call is active"));
    }

    // ─────────────────────────────────────────────────────────────
    // Установленные приложения
    // ─────────────────────────────────────────────────────────────
    private static void addPackagesBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("isAppInstalled", "b", "",
                "(getPackageManager().getLaunchIntentForPackage(%1$s) != null)",
                COLOR_FUNC, PALETTE_FUNC,
                "app %s is installed"));

        list.add(block("getPackageVersionName", "s", "",
                "getPackageManager().getPackageInfo(%1$s, 0).versionName",
                COLOR_FUNC, PALETTE_FUNC,
                "version of package %s"));

        list.add(block("getInstalledAppsCount", "d", "",
                "getPackageManager().getInstalledPackages(0).size()",
                COLOR_FUNC, PALETTE_FUNC,
                "installed apps count"));
    }

    // ─────────────────────────────────────────────────────────────
    // Wi-Fi
    // ─────────────────────────────────────────────────────────────
    private static void addWifiBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("wifiIsEnabled", "b", "",
                "((android.net.wifi.WifiManager) getApplicationContext().getSystemService(android.content.Context.WIFI_SERVICE)).isWifiEnabled()",
                COLOR_FUNC, PALETTE_FUNC,
                "Wi-Fi is enabled"));
    }

}
