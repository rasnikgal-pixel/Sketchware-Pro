package pro.sketchware.ui;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.List;

import pro.sketchware.R;

/**
 * Диалог выбора иконки для экрана.
 * Возвращает строку:
 *  - "@drawable/ic_xxx" — встроенная Material Icon
 *  - "image_name"       — имя файла из проекта
 *  - ""                 — по умолчанию (сброс)
 */
public class IconPickerDialog {

    public interface Callback {
        void onIconPicked(String icon);
    }

    // Список популярных Material Icons (имя ресурса без префикса)
    private static final String[] MATERIAL_ICONS = {
        "ic_mtrl_home", "ic_mtrl_settings", "ic_mtrl_star", "ic_mtrl_favorite",
        "ic_mtrl_person", "ic_mtrl_people", "ic_mtrl_search", "ic_mtrl_menu",
        "ic_mtrl_add", "ic_mtrl_edit", "ic_mtrl_delete", "ic_mtrl_save",
        "ic_mtrl_close", "ic_mtrl_check", "ic_mtrl_arrow_left", "ic_mtrl_arrow_right",
        "ic_mtrl_arrow_up", "ic_mtrl_arrow_down", "ic_mtrl_dashboard", "ic_mtrl_list",
        "ic_mtrl_image", "ic_mtrl_play", "ic_mtrl_pause", "ic_mtrl_volume",
        "ic_mtrl_lock", "ic_mtrl_cloud", "ic_mtrl_email", "ic_mtrl_call",
        "ic_mtrl_message", "ic_mtrl_notifications", "ic_mtrl_calendar", "ic_mtrl_time",
        "ic_mtrl_camera", "ic_mtrl_location", "ic_mtrl_wifi", "ic_mtrl_bluetooth",
        "ic_mtrl_shopping_cart", "ic_mtrl_shopping_bag", "ic_mtrl_attach_money", "ic_mtrl_credit_card",
        "ic_mtrl_work", "ic_mtrl_business", "ic_mtrl_school", "ic_mtrl_book",
        "ic_mtrl_music_note", "ic_mtrl_movie", "ic_mtrl_photo", "ic_mtrl_videocam",
        "ic_mtrl_map", "ic_mtrl_navigation", "ic_mtrl_directions_car", "ic_mtrl_flight",
        "ic_mtrl_train", "ic_mtrl_directions_bike", "ic_mtrl_fitness_center", "ic_mtrl_restaurant",
        "ic_mtrl_local_cafe", "ic_mtrl_hotel", "ic_mtrl_beach_access", "ic_mtrl_terrain",
        "ic_mtrl_wb_sunny", "ic_mtrl_cloud_queue", "ic_mtrl_ac_unit", "ic_mtrl_bolt",
        "ic_mtrl_eco", "ic_mtrl_local_florist", "ic_mtrl_pets", "ic_mtrl_cake",
        "ic_mtrl_card_giftcard", "ic_mtrl_celebration", "ic_mtrl_emoji_events", "ic_mtrl_military_tech",
        "ic_mtrl_extension", "ic_mtrl_psychology", "ic_mtrl_lightbulb", "ic_mtrl_build",
        "ic_mtrl_construction", "ic_mtrl_code", "ic_mtrl_bug_report", "ic_mtrl_security"
    };

    private final Context context;
    private final String currentIcon;
    private final Callback callback;

    public IconPickerDialog(Context context, String currentIcon, Callback callback) {
        this.context = context;
        this.currentIcon = currentIcon;
        this.callback = callback;
    }

    public void show() {
        // Верхняя часть — превью + кнопка сброса
        LinearLayout root = new LinearLayout(context);
        root.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(16);
        root.setPadding(pad, pad, pad, pad);

        // Превью
        ImageView preview = new ImageView(context);
        LinearLayout.LayoutParams previewLp = new LinearLayout.LayoutParams(dp(64), dp(64));
        previewLp.gravity = Gravity.CENTER_HORIZONTAL;
        preview.setLayoutParams(previewLp);
        setPreview(preview, currentIcon);
        root.addView(preview);

        // Сетка Material Icons (GridLayout через rows)
        ScrollView scroll = new ScrollView(context);
        LinearLayout.LayoutParams scrollLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(300));
        scrollLp.topMargin = dp(16);
        scroll.setLayoutParams(scrollLp);

        LinearLayout grid = new LinearLayout(context);
        grid.setOrientation(LinearLayout.VERTICAL);

        int perRow = 5;
        List<String> row = new ArrayList<>();
        for (String icon : MATERIAL_ICONS) {
            row.add(icon);
            if (row.size() == perRow) {
                grid.addView(makeRow(row, preview));
                row.clear();
            }
        }
        if (!row.isEmpty()) grid.addView(makeRow(row, preview));

        scroll.addView(grid);
        root.addView(scroll);

        // Ссылка "По умолчанию"
        TextView reset = new TextView(context);
        reset.setText("Сбросить к стандартной");
        reset.setPadding(0, dp(12), 0, 0);
        reset.setTextColor(0xFF6750A4);
        reset.setOnClickListener(v -> {
            setPreview(preview, "");
            callback.onIconPicked("");
        });
        root.addView(reset);

        new AlertDialog.Builder(context)
                .setTitle("Выберите иконку")
                .setView(root)
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    // Превью уже обновлено ранее через клики
                    // callback был вызван с каждым кликом — здесь ничего
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private LinearLayout makeRow(List<String> icons, ImageView preview) {
        LinearLayout rowLayout = new LinearLayout(context);
        rowLayout.setOrientation(LinearLayout.HORIZONTAL);
        rowLayout.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        for (String name : icons) {
            ImageView iv = new ImageView(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(48), 1f);
            lp.setMargins(dp(4), dp(4), dp(4), dp(4));
            iv.setLayoutParams(lp);
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv.setPadding(dp(4), dp(4), dp(4), dp(4));
            iv.setBackgroundResource(R.drawable.single_choice_background);

            int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            if (resId != 0) iv.setImageResource(resId);

            iv.setOnClickListener(v -> {
                String value = "@drawable/" + name;
                setPreview(preview, value);
                callback.onIconPicked(value);
            });

            rowLayout.addView(iv);
        }

        // Заполним оставшиеся ячейки, если ряд короче
        for (int i = icons.size(); i < 5; i++) {
            View spacer = new View(context);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, dp(48), 1f);
            spacer.setLayoutParams(lp);
            rowLayout.addView(spacer);
        }
        return rowLayout;
    }

    private void setPreview(ImageView iv, String icon) {
        if (icon == null || icon.isEmpty()) {
            iv.setImageResource(R.drawable.ic_mtrl_image);
            return;
        }
        if (icon.startsWith("@drawable/")) {
            String name = icon.substring("@drawable/".length());
            int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            if (resId != 0) {
                iv.setImageResource(resId);
            } else {
                iv.setImageResource(R.drawable.ic_mtrl_image);
            }
        } else {
            // Из проекта — пока просто заглушка
            iv.setImageResource(R.drawable.ic_mtrl_image);
        }
    }

    private int dp(int v) {
        return Math.round(v * context.getResources().getDisplayMetrics().density);
    }
}
