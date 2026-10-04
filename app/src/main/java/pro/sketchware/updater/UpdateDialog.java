package pro.sketchware.updater;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import androidx.appcompat.app.AlertDialog;

import pro.sketchware.BuildConfig;

/**
 * Диалог «Доступно обновление».
 */
public class UpdateDialog {

    /** Показать диалог обновления. */
    public static void show(Activity activity, UpdateInfo info) {
        if (activity == null || activity.isFinishing() || info == null) return;

        StringBuilder msg = new StringBuilder();
        msg.append("Установлено: ").append(BuildConfig.VERSION_NAME)
                .append(" (").append(BuildConfig.VERSION_CODE).append(")\n");
        msg.append("Доступно: ").append(info.versionName)
                .append(" (").append(info.versionCode).append(")\n\n");

        if (info.changelog != null && !info.changelog.isEmpty()) {
            msg.append("Что нового:\n");
            msg.append(info.changelog);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(activity)
                .setTitle("Доступно обновление")
                .setMessage(msg.toString())
                .setCancelable(!info.required);

        // Кнопка "Скачать"
        builder.setPositiveButton("Скачать", (d, w) -> {
            openDownload(activity, info.downloadUrl);
        });

        // Если не обязательно — кнопка "Позже"
        if (!info.required) {
            builder.setNegativeButton("Позже", (d, w) -> {
                // Запомним, что пользователь пропустил эту версию
                UpdateChecker.skipVersion(activity, info.versionCode);
                d.dismiss();
            });
        }

        builder.show();
    }

    /** Открыть ссылку на APK в браузере. */
    private static void openDownload(Context ctx, String url) {
        if (url == null || url.isEmpty()) return;
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            ctx.startActivity(intent);
        } catch (Exception ignored) {}
    }
}
