package pro.sketchware.smartdrop;


import pro.sketchware.R;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import java.io.File;
import java.util.Arrays;

import mod.hey.studios.project.backup.BackupFactory;
import mod.hilal.saif.activities.tools.ConfigActivity;

/**
 * Автоматический бэкап проекта после успешной сборки APK.
 * Работает только если включён тумблер в настройках.
 * Выполняется в фоновом потоке — не блокирует UI.
 */
public final class AutoBackupManager {

    private static final String TAG = "AutoBackup";

    private AutoBackupManager() {}

    /**
     * Запускает фоновый бэкап проекта. Безопасно вызывать из UI-потока.
     */
    public static void runBackup(Context context, String scId, String projectName) {
        if (context == null || scId == null || projectName == null) return;
        if (!ConfigActivity.isAutoBackupEnabled()) return;

        final Context appCtx = context.getApplicationContext();
        new Thread(() -> doBackup(appCtx, scId, projectName), "AutoBackupThread").start();
    }

    /** Выполняет бэкап в фоновом потоке. */
    private static void doBackup(Context context, String scId, String projectName) {
        try {
            BackupFactory bm = new BackupFactory(scId);
            bm.setBackupLocalLibs(true);
            bm.setBackupCustomBlocks(true);
            bm.backup(context, projectName);

            File outFile = bm.getOutFile();
            if (outFile == null) {
                // Ошибка — файл не создан
                logError(context, scId, "backup() returned null outFile");
                showToastSafe(context, getString(R.string.auto_auto_backup_manager_001));
                return;
            }

            // Успех — удалить старые сверх лимита
            int keepCount = ConfigActivity.getAutoBackupKeepCount();
            cleanupOldBackups(projectName, keepCount);

            // Лог + Toast
            logOk(context, scId, outFile.getAbsolutePath());
            showToastSafe(context, "Автобэкап: " + outFile.getName());

        } catch (Throwable t) {
            logError(context, scId, String.valueOf(t));
            showToastSafe(context, getString(R.string.auto_auto_backup_manager_002));
        }
    }

    /** Удаляет старые .swb сверх лимита (0 = без ограничений). */
    private static void cleanupOldBackups(String projectName, int keepCount) {
        if (keepCount <= 0) return;
        try {
            String projectNameOnly = projectName.replace("_d", "").replace(File.separator, "");
            File dir = new File(BackupFactory.getBackupDir(), projectNameOnly);
            if (!dir.exists() || !dir.isDirectory()) return;

            File[] files = dir.listFiles((d, name) -> name.endsWith("." + BackupFactory.EXTENSION));
            if (files == null || files.length <= keepCount) return;

            // Свежие вперёд
            Arrays.sort(files, (a, b) -> Long.compare(b.lastModified(), a.lastModified()));

            // Удаляем начиная с индекса keepCount
            for (int i = keepCount; i < files.length; i++) {
                try { files[i].delete(); } catch (Throwable ignored) {}
            }
        } catch (Throwable ignored) {}
    }

    /** Toast из фонового потока — через UI-поток. */
    private static void showToastSafe(Context context, String msg) {
        try {
            new Handler(Looper.getMainLooper()).post(() ->
                    Toast.makeText(context, msg, Toast.LENGTH_LONG).show());
        } catch (Throwable ignored) {}
    }

    /** Лог успеха. */
    private static void logOk(Context context, String scId, String path) {
        try {
            DebugLogger.get(context).i(TAG, "auto_backup_ok", scId + " -> " + path);
        } catch (Throwable ignored) {}
    }

    /** Лог ошибки. */
    private static void logError(Context context, String scId, String err) {
        try {
            DebugLogger.get(context).e(TAG, "auto_backup_error", new Exception(scId + ": " + err));
        } catch (Throwable ignored) {}
    }
}
