package pro.sketchware.smartdrop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pro.sketchware.R;

/**
 * Журнал отладки приложения.
 *
 * Пишет одновременно в два файла для каждого языка:
 *   - /storage/emulated/0/SketchwareProData/logs/log_<lang>.txt   (открытая папка)
 *   - getExternalFilesDir("logs")/log_<lang>.txt                   (скрытая папка приложения)
 *
 * Язык сообщения берётся из строковых ресурсов по ключу.
 * Ротация при 1 MB: log_<lang>.txt → log_<lang>.1.txt (старый удаляется).
 *
 * Все записи асинхронные (ExecutorService), не блокируют UI.
 */
public class DebugLogger {

    private static final String TAG = "DebugLogger";
    private static final String PREFS = "debug_logger_prefs";
    private static final String KEY_ENABLED = "enabled";
    private static final String KEY_VERBOSE = "verbose";
    private static final long MAX_FILE_SIZE = 1024 * 1024; // 1 MB

    private static DebugLogger instance;

    private final Context appContext;
    private final File openDir;
    private final File hiddenDir;
    private final ExecutorService executor;
    private final SimpleDateFormat dateFormat;
    private volatile boolean enabled;
    private volatile boolean verbose;

    private DebugLogger(Context context) {
        this.appContext = context.getApplicationContext();
        this.executor = Executors.newSingleThreadExecutor();
        this.dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.US);

        SharedPreferences prefs = appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
        this.enabled = prefs.getBoolean(KEY_ENABLED, true);
        this.verbose = prefs.getBoolean(KEY_VERBOSE, false);

        // Открытая папка: /storage/emulated/0/SketchwareProData/logs
        File storage = Environment.getExternalStorageDirectory();
        this.openDir = new File(storage, "SketchwareProData/logs");
        // Скрытая папка: /Android/data/pro.sketchware/files/logs
        File ext = appContext.getExternalFilesDir(null);
        this.hiddenDir = ext != null ? new File(ext, "logs") : null;

        ensureDirs();
    }

    public static synchronized DebugLogger get(Context context) {
        if (instance == null) {
            instance = new DebugLogger(context);
        }
        return instance;
    }

    private void ensureDirs() {
        try {
            if (!openDir.exists() && !openDir.mkdirs()) {
                Log.w(TAG, "Cannot create open log dir: " + openDir);
            }
        } catch (Exception e) {
            Log.w(TAG, "openDir creation failed", e);
        }
        try {
            if (hiddenDir != null && !hiddenDir.exists() && !hiddenDir.mkdirs()) {
                Log.w(TAG, "Cannot create hidden log dir: " + hiddenDir);
            }
        } catch (Exception e) {
            Log.w(TAG, "hiddenDir creation failed", e);
        }
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean value) {
        enabled = value;
        appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit().putBoolean(KEY_ENABLED, value).apply();
    }

    public boolean isVerbose() {
        return verbose;
    }

    public void setVerbose(boolean value) {
        verbose = value;
        appContext.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                .edit().putBoolean(KEY_VERBOSE, value).apply();
    }

    // ─────────────────────────────────────────────────────────────
    // Публичный API
    // ─────────────────────────────────────────────────────────────

    public void d(String tag, String key, String arg) {
        write("DEBUG", tag, key, arg, null);
    }

    public void i(String tag, String key, String arg) {
        write("INFO", tag, key, arg, null);
    }

    public void w(String tag, String key, String arg) {
        write("WARN", tag, key, arg, null);
    }

    public void e(String tag, String key, Throwable ex) {
        write("ERROR", tag, key, "", ex);
    }

    // ─────────────────────────────────────────────────────────────
    // Внутренняя логика
    // ─────────────────────────────────────────────────────────────

    private void write(String level, String tag, String key, String arg, Throwable ex) {
        if (!enabled) return;
        if ("DEBUG".equals(level) && !verbose) return;

        String ruMsg = resolveString(key, true, arg, ex);
        String enMsg = resolveString(key, false, arg, ex);
        String ts = dateFormat.format(new Date());

        String ruLine = ts + " | " + level + " | " + tag + " | " + ruMsg + "\n";
        String enLine = ts + " | " + level + " | " + tag + " | " + enMsg + "\n";

        executor.execute(() -> {
            writeTo(openDir, "log_ru.txt", ruLine);
            writeTo(openDir, "log_en.txt", enLine);
            if (hiddenDir != null) {
                writeTo(hiddenDir, "log_ru.txt", ruLine);
                writeTo(hiddenDir, "log_en.txt", enLine);
            }
        });
    }

    private String resolveString(String key, boolean russian, String arg, Throwable ex) {
        try {
            int resId = appContext.getResources().getIdentifier(key, "string", appContext.getPackageName());
            if (resId == 0) {
                return key + (arg.isEmpty() ? "" : " (" + arg + ")");
            }
            // Для RU берём значение по умолчанию (values/strings_log.xml),
            // для EN — принудительно из values-en через конфиг
            android.content.res.Configuration cfg = new android.content.res.Configuration(
                    appContext.getResources().getConfiguration());
            cfg.setLocale(russian ? Locale.forLanguageTag("ru") : Locale.ENGLISH);
            android.content.res.Resources res = appContext.getResources();
            // createConfigurationContext возвращает Resources с нужной локалью
            android.content.res.Resources localized = appContext
                    .createConfigurationContext(cfg).getResources();
            String template = localized.getString(resId);
            String base = arg.isEmpty() ? template : String.format(template, arg);
            if (ex != null) {
                base += " | " + Log.getStackTraceString(ex);
            }
            return base;
        } catch (Exception e) {
            return key + (arg.isEmpty() ? "" : " (" + arg + ")");
        }
    }

    private void writeTo(File dir, String name, String line) {
        if (dir == null || !dir.exists()) return;
        File f = new File(dir, name);
        try {
            if (f.exists() && f.length() > MAX_FILE_SIZE) {
                rotate(f);
            }
            try (FileWriter fw = new FileWriter(f, true)) {
                fw.write(line);
            }
        } catch (IOException e) {
            Log.w(TAG, "writeTo failed: " + f, e);
        }
    }

    private void rotate(File f) {
        File old = new File(f.getParentFile(), f.getName().replace(".txt", ".1.txt"));
        if (old.exists()) old.delete();
        if (!f.renameTo(old)) {
            Log.w(TAG, "rotate rename failed: " + f);
        }
    }

    // ─────────────────────────────────────────────────────────────
    // Управление файлами (для UI)
    // ─────────────────────────────────────────────────────────────

    public File getOpenLogFile(boolean russian) {
        return new File(openDir, russian ? "log_ru.txt" : "log_en.txt");
    }

    public File getHiddenLogFile(boolean russian) {
        return hiddenDir != null ? new File(hiddenDir, russian ? "log_ru.txt" : "log_en.txt") : null;
    }

    public String readLog(boolean russian) {
        File f = getOpenLogFile(russian);
        if (!f.exists()) f = getHiddenLogFile(russian);
        if (f == null || !f.exists()) return "";
        try {
            StringBuilder sb = new StringBuilder();
            try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(f))) {
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line).append('\n');
                }
            }
            return sb.toString();
        } catch (IOException e) {
            return "";
        }
    }

    public void clearLogs() {
        executor.execute(() -> {
            deleteFile(getOpenLogFile(true));
            deleteFile(getOpenLogFile(false));
            deleteFile(getHiddenLogFile(true));
            deleteFile(getHiddenLogFile(false));
            deleteFile(new File(openDir, "log_ru.1.txt"));
            deleteFile(new File(openDir, "log_en.1.txt"));
            if (hiddenDir != null) {
                deleteFile(new File(hiddenDir, "log_ru.1.txt"));
                deleteFile(new File(hiddenDir, "log_en.1.txt"));
            }
        });
    }

    private void deleteFile(File f) {
        try {
            if (f != null && f.exists()) f.delete();
        } catch (Exception ignored) {
        }
    }
}
