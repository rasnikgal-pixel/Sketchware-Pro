package pro.sketchware.utility.theme;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;

import androidx.appcompat.app.AppCompatDelegate;

public class ThemeManager {

    public static final int THEME_SYSTEM = 0;
    public static final int THEME_LIGHT = 1;
    public static final int THEME_DARK = 2;

    // Custom Material3 color themes
    public static final int THEME_PURPLE_DARK = 10;
    public static final int THEME_BLACK = 11;
    public static final int THEME_BLUE = 12;
    public static final int THEME_GREEN = 13;
    public static final int THEME_GOLD = 14;

    private static final String[] THEME_NAMES = {
            "Стандартная (Light/Dark)",
            "Светлая",
            "Тёмная",
            "Фиолетовая тёмная",
            "Чёрная (AMOLED)",
            "Синяя",
            "Зелёная",
            "Золотая"
    };

    private static final int[] THEME_IDS = {
            THEME_SYSTEM, THEME_LIGHT, THEME_DARK,
            THEME_PURPLE_DARK, THEME_BLACK, THEME_BLUE, THEME_GREEN, THEME_GOLD
    };

    public static String[] getThemeNames() {
        return THEME_NAMES;
    }

    public static int[] getThemeIds() {
        return THEME_IDS;
    }

    public static String getThemeName(int themeId) {
        for (int i = 0; i < THEME_IDS.length; i++) {
            if (THEME_IDS[i] == themeId) return THEME_NAMES[i];
        }
        return THEME_NAMES[0];
    }

    /** Returns the style resource ID for the given custom theme, or 0 if not a custom theme. */
    public static int getThemeStyleRes(int themeId) {
        switch (themeId) {
            case THEME_PURPLE_DARK: return pro.sketchware.R.style.Theme_SketchwarePro_PurpleDark;
            case THEME_BLACK:       return pro.sketchware.R.style.Theme_SketchwarePro_Black;
            case THEME_BLUE:        return pro.sketchware.R.style.Theme_SketchwarePro_Blue;
            case THEME_GREEN:       return pro.sketchware.R.style.Theme_SketchwarePro_Green;
            case THEME_GOLD:        return pro.sketchware.R.style.Theme_SketchwarePro_Gold;
            default:                return 0;
        }
    }

    /** Called from BaseAppCompatActivity.onCreate() BEFORE super.onCreate(). */
    public static void applyCustomTheme(Context context) {
        int themeId = getCurrentTheme(context);
        int styleRes = getThemeStyleRes(themeId);
        if (styleRes != 0 && context instanceof android.app.Activity) {
            ((android.app.Activity) context).setTheme(styleRes);
        }
    }

    /** Saves a selected theme id (for custom color themes too). */
    public static void setTheme(Context context, int themeId) {
        saveTheme(context, themeId);
        // Also update day/night mode for standard themes
        switch (themeId) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            case THEME_SYSTEM:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                break;
            default:
                // Custom themes are always dark
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    private static final String THEME_PREF = "themedata";
    private static final String THEME_KEY = "idetheme";

    public static void applyTheme(Context context, int type) {
        saveTheme(context, type);

        switch (type) {
            case THEME_LIGHT:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                break;
            case THEME_DARK:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                break;
            default:
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
        }
    }

    public static int getCurrentTheme(Context context) {
        return getPreferences(context).getInt(THEME_KEY, THEME_SYSTEM);
    }

    public static boolean isSystemTheme(Context context) {
        return getCurrentTheme(context) == THEME_SYSTEM;
    }

    public static int getSystemAppliedTheme(Context context) {
        int nightModeFlags = context.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        return switch (nightModeFlags) {
            case Configuration.UI_MODE_NIGHT_NO -> THEME_LIGHT;
            case Configuration.UI_MODE_NIGHT_YES -> THEME_DARK;
            default -> THEME_SYSTEM;
        };
    }

    private static void saveTheme(Context context, int theme) {
        getPreferences(context).edit().putInt(THEME_KEY, theme).apply();
    }

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(THEME_PREF, Context.MODE_PRIVATE);
    }
}