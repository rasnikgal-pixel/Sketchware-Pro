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
 * Языки: "ru" (русский), "en" (английский), "" (системный).
 */
public final class LocaleHelper {

    private static final String PREFS = "app_locale_prefs";
    private static final String KEY_LANG = "app_language";

    /** "ru", "en" или "" (системный). */
    public static final String LANG_SYSTEM = "";
    public static final String LANG_RU = "ru";
    public static final String LANG_EN = "en";

    private LocaleHelper() {}

    public static String getLanguage(Context ctx) {
        return prefs(ctx).getString(KEY_LANG, LANG_SYSTEM);
    }

    public static void setLanguage(Context ctx, String lang) {
        prefs(ctx).edit().putString(KEY_LANG, lang == null ? LANG_SYSTEM : lang).apply();
    }

    /** Применить язык ко всему приложению. Вызывать из Application.onCreate(). */
    public static void apply(Context ctx) {
        String lang = getLanguage(ctx);
        LocaleListCompat locales;
        if (lang == null || lang.isEmpty()) {
            locales = LocaleListCompat.getEmptyLocaleList();
        } else {
            locales = LocaleListCompat.forLanguageTags(lang);
        }
        AppCompatDelegate.setApplicationLocales(locales);
    }

    /** Обернуть Context нужной локалью (для API < 33). */
    public static Context wrap(Context ctx) {
        String lang = getLanguage(ctx);
        if (lang == null || lang.isEmpty()) return ctx;

        Locale locale = new Locale(lang);
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
