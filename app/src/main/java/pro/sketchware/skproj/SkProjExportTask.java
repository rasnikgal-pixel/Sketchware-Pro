package pro.sketchware.skproj;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;

import pro.sketchware.dialogs.ProgressDialog;

/**
 * Запускает экспорт проекта в .skproj с UI-прогрессом.
 */
public final class SkProjExportTask {

    private SkProjExportTask() {}

    /** Результат экспорта. */
    public interface Callback {
        void onSuccess(File outFile);
        void onError(String message);
    }

    /**
     * Запускает экспорт в фоне. Показывает ProgressDialog.
     * Callback вызывается на UI-потоке.
     */
    public static void run(Activity activity, String scId, Callback callback) {
        if (activity == null || scId == null) {
            if (callback != null) callback.onError("Некорректные параметры");
            return;
        }

        final ProgressDialog dlg = new ProgressDialog(activity);
        dlg.setMessage("Экспорт проекта...");
        dlg.setCancelable(false);
        dlg.show();

        new Thread(() -> {
            File out = null;
            String err = null;
            try {
                SkProjExporter ex = new SkProjExporter(activity.getApplicationContext(), scId);
                out = ex.export();
                if (out == null) err = ex.error;
            } catch (Throwable t) {
                err = String.valueOf(t);
            }

            final File fOut = out;
            final String fErr = err;
            activity.runOnUiThread(() -> {
                try { dlg.dismiss(); } catch (Throwable ignored) {}
                if (callback == null) return;
                if (fOut != null) callback.onSuccess(fOut);
                else callback.onError(fErr != null ? fErr : "Неизвестная ошибка");
            });
        }, "SkProjExportThread").start();
    }

    /** Показывает диалог успешного экспорта с кнопками действий. */
    public static void showResultDialog(Activity activity, File outFile) {
        if (activity == null || outFile == null) return;
        try {
            String name = outFile.getName();
            long size = outFile.length();
            String sizeStr = String.format(java.util.Locale.US, "%.2f МБ", size / 1048576.0);
            String parent = outFile.getParent();

            String message = "Файл: " + name + "\n"
                    + "Размер: " + sizeStr + "\n"
                    + "Путь: " + parent;

            new MaterialAlertDialogBuilder(activity)
                    .setTitle("\u2705 Экспорт завершён")
                    .setMessage(message)
                    .setPositiveButton("Открыть папку", (d, w) -> openFolder(activity, outFile.getParentFile()))
                    .setNeutralButton("Поделиться", (d, w) -> shareFile(activity, outFile))
                    .setNegativeButton("Закрыть", null)
                    .show();
        } catch (Throwable t) {
            Toast.makeText(activity, "Экспорт выполнен: " + outFile.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }
    }

    private static void openFolder(Activity activity, File folder) {
        if (activity == null || folder == null) return;
        try {
            // Android 11+ не даёт открыть папку через ACTION_VIEW.
            // Показываем путь и пытаемся открыть через стандартный file:// (может сработать на некоторых устройствах).
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse("file://" + folder.getAbsolutePath()), "resource/folder");
            activity.startActivity(intent);
        } catch (Throwable t) {
            Toast.makeText(activity, "Папка: " + folder.getAbsolutePath(), Toast.LENGTH_LONG).show();
        }
    }

    private static void shareFile(Activity activity, File file) {
        if (activity == null || file == null) return;
        try {
            Uri uri = FileProvider.getUriForFile(activity,
                    activity.getPackageName() + ".provider", file);
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/octet-stream");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            activity.startActivity(Intent.createChooser(intent, "Поделиться .skproj"));
        } catch (Throwable t) {
            Toast.makeText(activity, "Не удалось отправить: " + t.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}
