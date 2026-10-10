package pro.sketchware.palette;


import mod.hey.studios.util.Helper;
import pro.sketchware.R;
import android.content.Context;
import android.content.SharedPreferences;

/**
 * Настройки отображения категорий (вкладок) в палитре виджетов дизайнера.
 *
 * Для каждой категории — три состояния:
 *   STATE_EXPANDED (0) — показать развёрнутой (по умолчанию)
 *   STATE_COLLAPSED (1) — показать свёрнутой (по тапу на заголовок — разворачивается)
 *   STATE_HIDDEN (2) — скрыть категорию полностью
 */
public final class WidgetTabsSettings {

    public static final int STATE_EXPANDED = 0;
    public static final int STATE_COLLAPSED = 1;
    public static final int STATE_HIDDEN = 2;

    private static final String PREFS = "widget_tabs_settings";
    private static final String KEY_PREFIX = "state_";

    private WidgetTabsSettings() {}

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public static int getState(Context ctx, String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) return STATE_EXPANDED;
        return prefs(ctx).getInt(KEY_PREFIX + categoryName, STATE_EXPANDED);
    }

    public static void setState(Context ctx, String categoryName, int state) {
        if (categoryName == null || categoryName.isEmpty()) return;
        prefs(ctx).edit()
                .putInt(KEY_PREFIX + categoryName, state)
                .putBoolean("explicit_" + categoryName, true)
                .apply();
    }

    /** Установить состояние БЕЗ пометки «пользователь настроил вручную». */
    public static void setStateQuiet(Context ctx, String categoryName, int state) {
        if (categoryName == null || categoryName.isEmpty()) return;
        prefs(ctx).edit().putInt(KEY_PREFIX + categoryName, state).apply();
    }

    /** Была ли категория явно настроена пользователем. */
    public static boolean isExplicit(Context ctx, String categoryName) {
        if (categoryName == null || categoryName.isEmpty()) return false;
        return prefs(ctx).getBoolean("explicit_" + categoryName, false);
    }

    /** Список категорий, которые можно настраивать. */
    public static final String[] CATEGORIES = {
        Helper.getResString(R.string.auto_widget_tabs_settings_001),
        "AndroidX",
        Helper.getResString(R.string.auto_widget_tabs_settings_002),
        Helper.getResString(R.string.auto_widget_tabs_settings_003),
        Helper.getResString(R.string.auto_widget_tabs_settings_004),
        "Google",
        Helper.getResString(R.string.auto_widget_tabs_settings_005)
    };

    /** Категории, которые нельзя скрыть полностью (только свернуть). */
    public static final String[] ALWAYS_VISIBLE = {
        Helper.getResString(R.string.auto_widget_tabs_settings_006),
        Helper.getResString(R.string.auto_widget_tabs_settings_007)
    };
}
