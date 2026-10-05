package pro.sketchware.updater;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import pro.sketchware.smartdrop.DebugLogger;
import pro.sketchware.BuildConfig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Проверка обновлений через update.json в GitHub.
 */
public class UpdateChecker {

    /** Основной источник (raw.githubusercontent.com). */
    private static final String URL_PRIMARY =
            "https://raw.githubusercontent.com/rasnikgal-pixel/Sketchware-Pro/main/update.json";
    /** Fallback (jsDelivr CDN) — используется если основной недоступен. */
    private static final String URL_FALLBACK =
            "https://cdn.jsdelivr.net/gh/rasnikgal-pixel/Sketchware-Pro@main/update.json";
    private static final String[] URLS = { URL_PRIMARY, URL_FALLBACK };
    private static final String PREFS = "update_checker_prefs";
    private static final String KEY_LAST_CHECK = "last_check_time";
    private static final String KEY_SKIPPED_VERSION = "skipped_version";
    private static final String KEY_ENABLED = "check_enabled";
    private static final String KEY_PERIOD = "check_period";
    private static final String KEY_MIGRATED = "migrated_period_v2";

    /** Периоды проверки. */
    public static final String PERIOD_ALWAYS = "always";
    public static final String PERIOD_DAILY = "daily";
    public static final String PERIOD_3DAYS = "3days";
    public static final String PERIOD_6DAYS = "6days";
    public static final String PERIOD_9DAYS = "9days";

    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private final Handler mainHandler = new Handler(Looper.getMainLooper());

    public interface Callback {
        void onUpdateAvailable(UpdateInfo info);
        void onUpToDate();
        void onError(String message);
    }

    public static boolean isEnabled(Context ctx) {
        return prefs(ctx).getBoolean(KEY_ENABLED, true);
    }

    public static void setEnabled(Context ctx, boolean enabled) {
        prefs(ctx).edit().putBoolean(KEY_ENABLED, enabled).apply();
    }

    public static String getPeriod(Context ctx) {
        return prefs(ctx).getString(KEY_PERIOD, PERIOD_ALWAYS);
    }

    public static void setPeriod(Context ctx, String period) {
        prefs(ctx).edit().putString(KEY_PERIOD, period).apply();
    }

    public static long getLastCheck(Context ctx) {
        return prefs(ctx).getLong(KEY_LAST_CHECK, 0);
    }

    public static int getSkippedVersion(Context ctx) {
        return prefs(ctx).getInt(KEY_SKIPPED_VERSION, -1);
    }

    public static void skipVersion(Context ctx, int versionCode) {
        prefs(ctx).edit().putInt(KEY_SKIPPED_VERSION, versionCode).apply();
    }

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    /**
     * Одноразовая миграция дефолтов.
     * Раньше XML default был "daily", из-за чего PreferenceManager записывал
     * daily в prefs при первом заходе в настройки. Теперь мы форсируем
     * PERIOD_ALWAYS однократно при первом запуске новой версии.
     */
    public static void migrateDefaultsIfNeeded(Context ctx) {
        SharedPreferences p = prefs(ctx);
        if (!p.getBoolean(KEY_MIGRATED, false)) {
            p.edit()
                    .putString(KEY_PERIOD, PERIOD_ALWAYS)
                    .putBoolean(KEY_MIGRATED, true)
                    .apply();
            DebugLogger.get(ctx).i("Updater", "log_updater_migrated", PERIOD_ALWAYS);
        }
    }

    /** Проверить, надо ли запускать проверку сейчас (учитывая период). */
    public static boolean shouldCheckNow(Context ctx) {
        if (!isEnabled(ctx)) return false;

        String period = getPeriod(ctx);
        if (PERIOD_ALWAYS.equals(period)) return true;

        long last = getLastCheck(ctx);
        long now = System.currentTimeMillis();
        long diff = now - last;

        long needed;
        switch (period) {
            case PERIOD_DAILY: needed = 24L * 60 * 60 * 1000; break;
            case PERIOD_3DAYS: needed = 3L * 24 * 60 * 60 * 1000; break;
            case PERIOD_6DAYS: needed = 6L * 24 * 60 * 60 * 1000; break;
            case PERIOD_9DAYS: needed = 9L * 24 * 60 * 60 * 1000; break;
            default: needed = 24L * 60 * 60 * 1000;
        }

        return diff >= needed;
    }

    /** Фоновая проверка. Учитывает настройки и период. */
    public void checkIfNeeded(Context ctx, int currentVersionCode, boolean userInitiated, Callback callback) {
        if (!userInitiated && !shouldCheckNow(ctx)) {
            DebugLogger.get(ctx).i("Updater", "log_updater_skipped_period", "");
            return;
        }

        DebugLogger.get(ctx).i("Updater", "log_updater_check_started", String.valueOf(currentVersionCode));
        executor.execute(() -> {
            try {
                UpdateInfo info = fetch(ctx);
                prefs(ctx).edit().putLong(KEY_LAST_CHECK, System.currentTimeMillis()).apply();

                mainHandler.post(() -> {
                    if (info == null) {
                        DebugLogger.get(ctx).w("Updater", "log_updater_parse_error", "null info");
                        callback.onError("Failed to parse update.json");
                        return;
                    }
                    if (!info.isNewerThan(currentVersionCode)) {
                        DebugLogger.get(ctx).i("Updater", "log_updater_up_to_date",
                                BuildConfig.VERSION_NAME);
                        callback.onUpToDate();
                        return;
                    }
                    DebugLogger.get(ctx).i("Updater", "log_updater_available", info.toString());

                    // Если текущая версия ниже minVersion — обновление обязательно
                    if (info.isBelowMin(currentVersionCode)) {
                        info.required = true;
                    }

                    // Если пользователь пропустил эту версию — не показывать (кроме required)
                    if (!userInitiated
                            && !info.required
                            && info.versionCode == getSkippedVersion(ctx)) {
                        callback.onUpToDate();
                        return;
                    }
                    callback.onUpdateAvailable(info);
                });
            } catch (Exception e) {
                DebugLogger.get(ctx).e("Updater", "log_updater_check_failed", e);
                mainHandler.post(() -> callback.onError(e.getMessage()));
            }
        });
    }

    /** Загрузить и распарсить update.json. Пробует основной URL, затем fallback. */
    private UpdateInfo fetch(Context ctx) throws Exception {
        Exception lastError = null;
        for (int i = 0; i < URLS.length; i++) {
            String url = URLS[i];
            try {
                UpdateInfo info = fetchFrom(url);
                if (info != null) {
                    if (i > 0) {
                        DebugLogger.get(ctx).i("Updater", "log_updater_fallback_used", url);
                    }
                    return info;
                }
            } catch (Exception e) {
                lastError = e;
                DebugLogger.get(ctx).w("Updater", "log_updater_source_failed",
                        "[" + i + "] " + url + " -> " + e.getMessage());
            }
        }
        if (lastError != null) throw lastError;
        throw new Exception("All update URLs failed");
    }

    /** Одна попытка загрузки по конкретному URL. */
    private UpdateInfo fetchFrom(String url) throws Exception {
        HttpURLConnection conn = null;
        try {
            URL u = new URL(url);
            conn = (HttpURLConnection) u.openConnection();
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(15000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            conn.connect();

            int code = conn.getResponseCode();
            if (code != 200) {
                throw new Exception("HTTP " + code + " for " + url);
            }

            StringBuilder sb = new StringBuilder();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = r.readLine()) != null) sb.append(line);
            }
            return new Gson().fromJson(sb.toString(), UpdateInfo.class);
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
}
