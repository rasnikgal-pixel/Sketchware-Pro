package pro.sketchware.settings;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Хранилище настроек Дизайнера.
 *
 * Все настройки Дизайнера: внешний вид палитры, поведение вкладок,
 * размеры иконок, ширина палитры, аккордеон, поиск и т.д.
 */
public final class DesignerSettingsStore {

    private static final String PREFS = "designer_settings";

    // ─── Поведение вкладок ──────────────────────────────────────
    /** Аккордеон: при открытии одной категории все остальные сворачиваются. */
    private static final String KEY_ACCORDION = "accordion";
    /** Стартовое состояние: "expanded" / "collapsed" / "first_expanded". */
    private static final String KEY_TABS_START = "tabs_start_state";
    /** Показывать счётчик виджетов в заголовке категории. */
    private static final String KEY_SHOW_COUNT = "show_count";
    /** Анимация сворачивания/разворачивания. */
    private static final String KEY_ANIMATION = "animation";

    // ─── Внешний вид палитры ────────────────────────────────────
    /** Размер иконок палитры: "compact" / "normal" / "large". */
    private static final String KEY_ICON_SIZE = "icon_size";
    /** Размер текста палитры: "small" / "normal" / "large". */
    private static final String KEY_TEXT_SIZE = "text_size";
    /** Ширина палитры: "narrow" / "normal" / "wide". */
    private static final String KEY_PALETTE_WIDTH = "palette_width";
    /** Показывать подписи под иконками. */
    private static final String KEY_SHOW_LABELS = "show_labels";

    // ─── Поведение виджетов ─────────────────────────────────────
    /** Двойной тап: "properties" / "menu" / "none". */
    private static final String KEY_DOUBLE_TAP = "double_tap_action";
    /** Долгий тап: "properties" / "menu" / "none". */
    private static final String KEY_LONG_TAP = "long_tap_action";
    /** Показывать тултипы при долгом тапе. */
    private static final String KEY_TOOLTIPS = "tooltips";

    // ─── Быстрые действия ───────────────────────────────────────
    /** Показывать кнопку "Свернуть все" в шапке палитры. */
    private static final String KEY_SHOW_COLLAPSE_ALL = "show_collapse_all";
    /** Показывать строку поиска над палитрой. */
    private static final String KEY_SHOW_SEARCH = "show_search";

    private static final String KEY_MIGRATED_V1 = "migrated_v1";

    private static final String KEY_CHANGE_COUNTER = "change_counter";

    private static final String KEY_CHANGE_COUNTER = "change_counter";

    private DesignerSettingsStore() {}

    /** Счётчик изменений — увеличивается при каждом setX. Используется для детекта изменений. */
    public static long getChangeCounter(Context ctx) {
        return prefs(ctx).getLong(KEY_CHANGE_COUNTER, 0L);
    }

    private static void bumpCounter(Context ctx) {
        prefs(ctx).edit()
                .putLong(KEY_CHANGE_COUNTER, getChangeCounter(ctx) + 1)
                .apply();
    }

    /** Однократная миграция при первом запуске после релиза с новыми дефолтами. */
    public static void migrateIfNeeded(Context ctx) {
        SharedPreferences p = prefs(ctx);
        if (!p.getBoolean(KEY_MIGRATED_V1, false)) {
            p.edit()
                    .putBoolean(KEY_ACCORDION, true)
                    .putString(KEY_TABS_START, "first_expanded")
                    .putBoolean(KEY_MIGRATED_V1, true)
                    .apply();
        }
    }

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    // ─── Геттеры ────────────────────────────────────────────────

    public static boolean isAccordion(Context ctx) {
        return prefs(ctx).getBoolean(KEY_ACCORDION, true);
    }

    public static String getTabsStartState(Context ctx) {
        return prefs(ctx).getString(KEY_TABS_START, "first_expanded");
    }

    public static boolean isShowCount(Context ctx) {
        return prefs(ctx).getBoolean(KEY_SHOW_COUNT, false);
    }

    public static boolean isAnimation(Context ctx) {
        return prefs(ctx).getBoolean(KEY_ANIMATION, true);
    }

    public static String getIconSize(Context ctx) {
        return prefs(ctx).getString(KEY_ICON_SIZE, "normal");
    }

    public static String getTextSize(Context ctx) {
        return prefs(ctx).getString(KEY_TEXT_SIZE, "normal");
    }

    public static String getPaletteWidth(Context ctx) {
        return prefs(ctx).getString(KEY_PALETTE_WIDTH, "normal");
    }

    public static boolean isShowLabels(Context ctx) {
        return prefs(ctx).getBoolean(KEY_SHOW_LABELS, true);
    }

    public static String getDoubleTapAction(Context ctx) {
        return prefs(ctx).getString(KEY_DOUBLE_TAP, "properties");
    }

    public static String getLongTapAction(Context ctx) {
        return prefs(ctx).getString(KEY_LONG_TAP, "menu");
    }

    public static boolean isTooltips(Context ctx) {
        return prefs(ctx).getBoolean(KEY_TOOLTIPS, true);
    }

    public static boolean isShowCollapseAll(Context ctx) {
        return prefs(ctx).getBoolean(KEY_SHOW_COLLAPSE_ALL, true);
    }

    public static boolean isShowSearch(Context ctx) {
        return prefs(ctx).getBoolean(KEY_SHOW_SEARCH, false);
    }

    // ─── Сеттеры ────────────────────────────────────────────────

    public static void setAccordion(Context ctx, boolean v) {
        prefs(ctx).edit().putBoolean(KEY_ACCORDION, v).apply();
        bumpCounter(ctx);
    }

    public static void setTabsStartState(Context ctx, String v) {
        prefs(ctx).edit().putString(KEY_TABS_START, v).apply();
        bumpCounter(ctx);
    }

    public static void setShowCount(Context ctx, boolean v) {
        prefs(ctx).edit().putBoolean(KEY_SHOW_COUNT, v).apply();
        bumpCounter(ctx);
    }

    public static void setAnimation(Context ctx, boolean v) {
        prefs(ctx).edit().putBoolean(KEY_ANIMATION, v).apply();
        bumpCounter(ctx);
    }

    public static void setIconSize(Context ctx, String v) {
        prefs(ctx).edit().putString(KEY_ICON_SIZE, v).apply();
        bumpCounter(ctx);
    }

    public static void setTextSize(Context ctx, String v) {
        prefs(ctx).edit().putString(KEY_TEXT_SIZE, v).apply();
        bumpCounter(ctx);
    }

    public static void setPaletteWidth(Context ctx, String v) {
        prefs(ctx).edit().putString(KEY_PALETTE_WIDTH, v).apply();
        bumpCounter(ctx);
    }

    public static void setShowLabels(Context ctx, boolean v) {
        prefs(ctx).edit().putBoolean(KEY_SHOW_LABELS, v).apply();
        bumpCounter(ctx);
    }

    public static void setDoubleTapAction(Context ctx, String v) {
        prefs(ctx).edit().putString(KEY_DOUBLE_TAP, v).apply();
        bumpCounter(ctx);
    }

    public static void setLongTapAction(Context ctx, String v) {
        prefs(ctx).edit().putString(KEY_LONG_TAP, v).apply();
        bumpCounter(ctx);
    }

    public static void setTooltips(Context ctx, boolean v) {
        prefs(ctx).edit().putBoolean(KEY_TOOLTIPS, v).apply();
        bumpCounter(ctx);
    }

    public static void setShowCollapseAll(Context ctx, boolean v) {
        prefs(ctx).edit().putBoolean(KEY_SHOW_COLLAPSE_ALL, v).apply();
        bumpCounter(ctx);
    }

    public static void setShowSearch(Context ctx, boolean v) {
        prefs(ctx).edit().putBoolean(KEY_SHOW_SEARCH, v).apply();
        bumpCounter(ctx);
    }
}
