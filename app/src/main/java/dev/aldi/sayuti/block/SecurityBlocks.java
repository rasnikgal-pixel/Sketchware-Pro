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
                "((java.util.function.Supplier<Double>) () -> { android.database.Cursor _c = getContentResolver().query(android.net.Uri.parse(\"content://sms/inbox\"), null, null, null, null); double _n = (_c != null) ? _c.getCount() : 0; if (_c != null) _c.close(); return _n; }).get()",
                COLOR_FUNC, PALETTE_FUNC,
                "SMS inbox count"));

        list.add(block("smsGetBodyByIndex", "s", "",
                "((java.util.function.Supplier<String>) () -> { android.database.Cursor _c = getContentResolver().query(android.net.Uri.parse(\"content://sms/inbox\"), null, null, null, null); String _r = \"\"; try { if (_c != null && _c.moveToPosition((int)(%1$d))) { _r = _c.getString(_c.getColumnIndexOrThrow(\"body\")); } } catch (Throwable _t) {} finally { if (_c != null) _c.close(); } return _r; }).get()",
                COLOR_FUNC, PALETTE_FUNC,
                "SMS body at index %d"));
    }

    // ─────────────────────────────────────────────────────────────
    // Контакты
    // ─────────────────────────────────────────────────────────────
    private static void addContactsBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("contactsGetCount", "d", "",
                "((java.util.function.Supplier<Double>) () -> { android.database.Cursor _c = getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null); double _n = (_c != null) ? _c.getCount() : 0; if (_c != null) _c.close(); return _n; }).get()",
                COLOR_FUNC, PALETTE_FUNC,
                "contacts count"));

        list.add(block("contactsReadAllNames", "s", "",
                "((java.util.function.Supplier<String>) () -> { StringBuilder _sb = new StringBuilder(); android.database.Cursor _c = getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null); try { if (_c != null) { while (_c.moveToNext()) { String _n = _c.getString(_c.getColumnIndexOrThrow(android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)); if (_n != null) _sb.append(_n).append(\"\\n\"); } } } catch (Throwable _t) {} finally { if (_c != null) _c.close(); } return _sb.toString(); }).get()",
                COLOR_FUNC, PALETTE_FUNC,
                "read all contact names"));

        list.add(block("contactsReadByName", "s", "",
                "((java.util.function.Supplier<String>) () -> { String _res = \"\"; android.database.Cursor _c = getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + \" = ?\", new String[]{%1$s}, null); try { if (_c != null && _c.moveToFirst()) { _res = _c.getString(_c.getColumnIndexOrThrow(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER)); } } catch (Throwable _t) {} finally { if (_c != null) _c.close(); } return _res; }).get()",
                COLOR_FUNC, PALETTE_FUNC,
                "phone number of contact %s"));

        list.add(block("contactsAdd", " ", "",
                "try { android.content.ContentValues _cv = new android.content.ContentValues(); _cv.put(android.provider.ContactsContract.Data.MIMETYPE, android.provider.ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE); _cv.put(android.provider.ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, %1$s); getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, _cv); } catch (Throwable _t) {}",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "add contact with name %s"));
    }

    // ─────────────────────────────────────────────────────────────
    // Звонки
    // ─────────────────────────────────────────────────────────────
    private static void addCallBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("callPhoneDirect", " ", "",
                "try { startActivity(new android.content.Intent(android.content.Intent.ACTION_CALL, android.net.Uri.parse(\"tel:\" + %1$s))); } catch (Throwable _t) {}",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "call phone number %s"));

        list.add(block("callPhoneDial", " ", "",
                "try { startActivity(new android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse(\"tel:\" + %1$s))); } catch (Throwable _t) {}",
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
                "try { android.graphics.Bitmap _b = android.graphics.BitmapFactory.decodeFile(%1$s); if (_b != null) android.app.WallpaperManager.getInstance(getApplicationContext()).setBitmap(_b); } catch (Throwable _t) {}",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "set wallpaper from file %s"));
    }

    // ─────────────────────────────────────────────────────────────
    // Будильники
    // ─────────────────────────────────────────────────────────────
    private static void addAlarmBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("alarmSet", " ", "",
                "try { android.content.Intent _alarmIntent = new android.content.Intent(getApplicationContext(), %1$s.class); android.app.PendingIntent _pi = android.app.PendingIntent.getActivity(getApplicationContext(), 0, _alarmIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE); android.app.AlarmManager _am = (android.app.AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE); if (_am != null) _am.setExact(android.app.AlarmManager.RTC_WAKEUP, (long)(%2$d), _pi); } catch (Throwable _t) {}",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "set alarm at %d start activity %m.activity"));

        list.add(block("alarmCancel", " ", "",
                "try { android.content.Intent _alarmIntent = new android.content.Intent(getApplicationContext(), %1$s.class); android.app.PendingIntent _pi = android.app.PendingIntent.getActivity(getApplicationContext(), 0, _alarmIntent, android.app.PendingIntent.FLAG_UPDATE_CURRENT | android.app.PendingIntent.FLAG_IMMUTABLE); android.app.AlarmManager _am = (android.app.AlarmManager) getSystemService(android.content.Context.ALARM_SERVICE); if (_am != null) _am.cancel(_pi); } catch (Throwable _t) {}",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "cancel alarm start activity %m.activity"));
    }

    // ─────────────────────────────────────────────────────────────
    // WakeLock
    // ─────────────────────────────────────────────────────────────
    private static void addWakelockBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("wakelockAcquire", " ", "",
                "try { android.os.PowerManager _pm = (android.os.PowerManager) getSystemService(android.content.Context.POWER_SERVICE); if (_pm != null) { android.os.PowerManager.WakeLock _wl = _pm.newWakeLock(android.os.PowerManager.PARTIAL_WAKE_LOCK, \"Sketchware::Wakelock\"); if (_wl != null) _wl.acquire(); } } catch (Throwable _t) {}",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "acquire partial wakelock"));

        list.add(block("wakelockRelease", " ", "",
                "try { android.os.PowerManager _pm = (android.os.PowerManager) getSystemService(android.content.Context.POWER_SERVICE); if (_pm != null) { android.os.PowerManager.WakeLock _wl = _pm.newWakeLock(android.os.PowerManager.PARTIAL_WAKE_LOCK, \"Sketchware::Wakelock\"); if (_wl != null && _wl.isHeld()) _wl.release(); } } catch (Throwable _t) {}",
                COLOR_OPERATOR, PALETTE_OPERATOR,
                "release partial wakelock"));
    }

    // ─────────────────────────────────────────────────────────────
    // Аккаунты
    // ─────────────────────────────────────────────────────────────
    private static void addAccountsBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("accountsGetCount", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { android.accounts.Account[] _a = android.accounts.AccountManager.get(getApplicationContext()).getAccounts(); return (double)(_a != null ? _a.length : 0); } catch (Throwable _t) { return 0.0; } }).get()",
                COLOR_FUNC, PALETTE_FUNC,
                "accounts count"));

        list.add(block("accountsGetNames", "s", "",
                "((java.util.function.Supplier<String>) () -> { StringBuilder _sb = new StringBuilder(); try { android.accounts.Account[] _accounts = android.accounts.AccountManager.get(getApplicationContext()).getAccounts(); if (_accounts != null) { for (android.accounts.Account _a : _accounts) { if (_a != null && _a.name != null) _sb.append(_a.name).append(\"\\n\"); } } } catch (Throwable _t) {} return _sb.toString(); }).get()",
                COLOR_FUNC, PALETTE_FUNC,
                "accounts names"));
    }
    // ─────────────────────────────────────────────────────────────
    // Состояние телефона
    // ─────────────────────────────────────────────────────────────
    private static void addPhoneStateBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("getOperatorName", "s", "",
                "((java.util.function.Supplier<String>) () -> { try { android.telephony.TelephonyManager _tm = (android.telephony.TelephonyManager) getSystemService(android.content.Context.TELEPHONY_SERVICE); return _tm != null ? _tm.getNetworkOperatorName() : \"\"; } catch (Throwable _t) { return \"\"; } }).get()",
                COLOR_FUNC, PALETTE_FUNC,
                "get carrier name"));

        list.add(block("getNetworkCountryIso", "s", "",
                "((java.util.function.Supplier<String>) () -> { try { android.telephony.TelephonyManager _tm = (android.telephony.TelephonyManager) getSystemService(android.content.Context.TELEPHONY_SERVICE); return _tm != null ? _tm.getNetworkCountryIso() : \"\"; } catch (Throwable _t) { return \"\"; } }).get()",
                COLOR_FUNC, PALETTE_FUNC,
                "get network country ISO"));

        list.add(block("isCallActive", "b", "",
                "((java.util.function.Supplier<Boolean>) () -> { try { android.telephony.TelephonyManager _tm = (android.telephony.TelephonyManager) getSystemService(android.content.Context.TELEPHONY_SERVICE); return _tm != null && _tm.getCallState() != android.telephony.TelephonyManager.CALL_STATE_IDLE; } catch (Throwable _t) { return false; } }).get()",
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
                "((java.util.function.Supplier<String>) () -> { try { return getPackageManager().getPackageInfo(%1$s, 0).versionName; } catch (Throwable _t) { return \"\"; } }).get()",
                COLOR_FUNC, PALETTE_FUNC,
                "version of package %s"));

        list.add(block("getInstalledAppsCount", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { java.util.List<android.content.pm.PackageInfo> _l = getPackageManager().getInstalledPackages(0); return _l != null ? (double) _l.size() : 0.0; } catch (Throwable _t) { return 0.0; } }).get()",
                COLOR_FUNC, PALETTE_FUNC,
                "installed apps count"));
    }

    // ─────────────────────────────────────────────────────────────
    // Wi-Fi
    // ─────────────────────────────────────────────────────────────
    private static void addWifiBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(block("wifiIsEnabled", "b", "",
                "((java.util.function.Supplier<Boolean>) () -> { try { android.net.wifi.WifiManager _wm = (android.net.wifi.WifiManager) getApplicationContext().getSystemService(android.content.Context.WIFI_SERVICE); return _wm != null && _wm.isWifiEnabled(); } catch (Throwable _t) { return false; } }).get()",
                COLOR_FUNC, PALETTE_FUNC,
                "Wi-Fi is enabled"));
    }

}
