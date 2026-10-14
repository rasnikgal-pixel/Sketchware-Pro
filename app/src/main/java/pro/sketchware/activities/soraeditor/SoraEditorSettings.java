package pro.sketchware.activities.soraeditor;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Настройки редактора SORA.
 */
public class SoraEditorSettings {

    private static final String PREFS = "sora_editor_prefs";

    // Ключи
    private static final String KEY_XML_HIGHLIGHT = "xml_highlight";
    private static final String KEY_FONT_SIZE = "font_size";
    private static final String KEY_WORD_WRAP = "word_wrap";

    // Значения по умолчанию
    private static final boolean DEFAULT_XML_HIGHLIGHT = false; // по умолчанию ВЫКЛ (может тормозить)
    private static final int DEFAULT_FONT_SIZE = 14;
    private static final boolean DEFAULT_WORD_WRAP = false;
    private static final String KEY_FOLD_ENABLED = "fold_enabled";
    private static final String KEY_BRACKET_HIGHLIGHT = "bracket_highlight";
    private static final boolean DEFAULT_FOLD_ENABLED = true;
    private static final boolean DEFAULT_BRACKET_HIGHLIGHT = true;

    private final SharedPreferences prefs;

    public SoraEditorSettings(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public boolean isXmlHighlightEnabled() {
        return prefs.getBoolean(KEY_XML_HIGHLIGHT, DEFAULT_XML_HIGHLIGHT);
    }

    public void setXmlHighlightEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_XML_HIGHLIGHT, enabled).apply();
    }

    public int getFontSize() {
        return prefs.getInt(KEY_FONT_SIZE, DEFAULT_FONT_SIZE);
    }

    public void setFontSize(int size) {
        if (size < 8) size = 8;
        if (size > 32) size = 32;
        prefs.edit().putInt(KEY_FONT_SIZE, size).apply();
    }

    private static final String KEY_LAST_SEARCH = "last_search_query";

    public String getLastSearchQuery() {
        return prefs.getString(KEY_LAST_SEARCH, "");
    }

    public void setLastSearchQuery(String query) {
        prefs.edit().putString(KEY_LAST_SEARCH, query == null ? "" : query).apply();
    }

    public boolean isWordWrapEnabled() {
        return prefs.getBoolean(KEY_WORD_WRAP, DEFAULT_WORD_WRAP);
    }

    public void setWordWrapEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_WORD_WRAP, enabled).apply();
    }

    public boolean isFoldEnabled() {
        return prefs.getBoolean(KEY_FOLD_ENABLED, DEFAULT_FOLD_ENABLED);
    }

    public void setFoldEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_FOLD_ENABLED, enabled).apply();
    }

    public boolean isBracketHighlightEnabled() {
        return prefs.getBoolean(KEY_BRACKET_HIGHLIGHT, DEFAULT_BRACKET_HIGHLIGHT);
    }

    public void setBracketHighlightEnabled(boolean enabled) {
        prefs.edit().putBoolean(KEY_BRACKET_HIGHLIGHT, enabled).apply();
    }
}
