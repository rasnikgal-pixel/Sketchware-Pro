package com.besome.sketch.lib.base;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import androidx.annotation.DrawableRes;

import mod.hey.studios.util.Helper;
import pro.sketchware.R;
import pro.sketchware.databinding.WidgetLayoutBinding;

public class BaseWidget extends LinearLayout {
    @DrawableRes
    private int widgetImgResId;
    private int widgetType;

    private final WidgetLayoutBinding binding;

    public BaseWidget(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.widget_layout, this, true);
        binding = WidgetLayoutBinding.bind(this);

        setOrientation(HORIZONTAL);
        setGravity(Gravity.CENTER);

        initialize();
        applyDesignerSettings();
    }

    private void initialize() {
        setBackgroundResource(R.drawable.icon_bg);
        setDrawingCacheEnabled(true);
    }

    public int getWidgetImageResId() {
        return widgetImgResId;
    }

    public String getWidgetName() {
        return Helper.getText(binding.tvWidget);
    }

    public void setWidgetName(String widgetName) {
        binding.tvWidget.setText(widgetName);
    }

    public int getWidgetType() {
        return widgetType;
    }

    public void setWidgetType(a widgetType) {
        this.widgetType = widgetType.ordinal();
    }

    public void setWidgetImage(@DrawableRes int image) {
        widgetImgResId = image;
        binding.imgWidget.setImageResource(image);
    }

    public void setWidgetNameTextSize(float sizeSp) {
        binding.tvWidget.setTextSize(sizeSp);
    }

    /** Размер иконки (dp). */
    public void setWidgetIconSize(int dp) {
        android.view.ViewGroup.LayoutParams lp = binding.imgWidget.getLayoutParams();
        lp.width = dpToPx(dp);
        lp.height = dpToPx(dp);
        binding.imgWidget.setLayoutParams(lp);
    }

    /** Показывать ли подписи. */
    public void setWidgetLabelsVisible(boolean visible) {
        binding.tvWidget.setVisibility(visible ? VISIBLE : GONE);
    }

    /**
     * Применить текущие настройки Дизайнера (размер иконок, текста, подписи).
     * Вызывается автоматически из конструктора.
     */
    public void applyDesignerSettings() {
        try {
            android.content.Context ctx = getContext();
            String iconSize = pro.sketchware.settings.DesignerSettingsStore.getIconSize(ctx);
            String textSize = pro.sketchware.settings.DesignerSettingsStore.getTextSize(ctx);
            boolean showLabels = pro.sketchware.settings.DesignerSettingsStore.isShowLabels(ctx);

            // Иконка
            int dp;
            switch (iconSize) {
                case "compact": dp = 11; break;
                case "large":   dp = 18; break;
                case "normal":
                default:        dp = 14; break;
            }
            setWidgetIconSize(dp);

            // Текст
            float sp;
            switch (textSize) {
                case "small":  sp = 10f; break;
                case "large":  sp = 13f; break;
                case "normal":
                default:       sp = 11f; break;
            }
            setWidgetNameTextSize(sp);

            // Подписи
            setWidgetLabelsVisible(showLabels);
        } catch (Throwable t) {
            // Не критично — если настройки недоступны, оставляем дефолты
        }
    }

    /** Пересчитать dp в px. */
    private int dpToPx(int dp) {
        return Math.round(dp * getResources().getDisplayMetrics().density);
    }

    public enum a {
        a,
        b
    }
}
