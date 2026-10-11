package pro.sketchware.i18n;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.LocaleList;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;

import java.util.Locale;

/**
 * Помощник переключения языка интерфейса.
 * Поддерживаемые языки: ru, en, zh, hi, uk.
 * LANG_SYSTEM ("") — следует за системным языком, но если системный
 * не входит в список поддерживаемых — принудительно английский.
 */
public final class LocaleHelper {

    private static final String PREFS = "app_locale_prefs";
    private static final String KEY_LANG = "app_language";

    public static final String LANG_SYSTEM = "";
    public static final String LANG_RU = "ru";
    public static final String LANG_EN = "en";
    public static final String LANG_ZH = "zh";
    public static final String LANG_HI = "hi";
    public static final String LANG_UK = "uk";

    private LocaleHelper() {}

    public static String getLanguage(Context ctx) {
        return prefs(ctx).getString(KEY_LANG, LANG_SYSTEM);
    }

    public static void setLanguage(Context ctx, String lang) {
        prefs(ctx).edit().putString(KEY_LANG, lang == null ? LANG_SYSTEM : lang).apply();
    }

    /** Поддерживается ли язык приложением. */
    public static boolean isSupported(String lang) {
        return LANG_RU.equals(lang) || LANG_EN.equals(lang)
                || LANG_ZH.equals(lang) || LANG_HI.equals(lang)
                || LANG_UK.equals(lang);
    }

    /**
     * Итоговый язык для применения.
     * - Если пользователь выбрал язык — он.
     * - Если "Системный" и системный язык поддерживается — он.
     * - Иначе — английский (LANG_EN).
     */
    public static String getEffectiveLanguage(Context ctx) {
        String saved = getLanguage(ctx);
        if (isSupported(saved)) {
            return saved;
        }
        // Системный
        String sys = Locale.getDefault().getLanguage();
        if (isSupported(sys)) {
            return sys;
        }
        return LANG_EN;
    }

    /**
     * Язык для справки.
     * ru / uk — русская справка; en / zh / hi — английская.
     */
    public static String getHelpLanguage(Context ctx) {
        String eff = getEffectiveLanguage(ctx);
        if (LANG_RU.equals(eff) || LANG_UK.equals(eff)) {
            return LANG_RU;
        }
        return LANG_EN;
    }

    /** Применить язык ко всему приложению. Вызывать из Application.onCreate(). */
    public static void apply(Context ctx) {
        String eff = getEffectiveLanguage(ctx);
        LocaleListCompat locales = LocaleListCompat.forLanguageTags(eff);
        AppCompatDelegate.setApplicationLocales(locales);
    }

    /** Обернуть Context нужной локалью (для API < 33). */
    public static Context wrap(Context ctx) {
        String eff = getEffectiveLanguage(ctx);

        Locale locale = new Locale(eff);
        Locale.setDefault(locale);

        Configuration config = new Configuration(ctx.getResources().getConfiguration());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            LocaleList list = new LocaleList(locale);
            LocaleList.setDefault(list);
            config.setLocales(list);
        } else {
            config.locale = locale;
        }
        return ctx.createConfigurationContext(config);
    }

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
