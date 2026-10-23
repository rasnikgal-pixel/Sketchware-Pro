package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Блоки валют: форматирование суммы в рубли, гривны, фунты, доллары, юани, рупии.
 */
public final class CurrencyBlocks {

    private CurrencyBlocks() {}

    private static final String COLOR_FUNC = "#2e7d32";
    private static final String PALETTE_FUNC = "10";

    private static HashMap<String, Object> blk(String name, String type, String typeName,
                                                 String code, String spec) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("type", type);
        m.put("typeName", typeName);
        m.put("code", code);
        m.put("color", COLOR_FUNC);
        m.put("palette", PALETTE_FUNC);
        m.put("spec", spec);
        return m;
    }

    private static String moneyBlock(String localeLang, String localeCountry, String fallback) {
        return "((new Object() { public String f() { try { java.text.NumberFormat _nf = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale(\"" + localeLang + "\", \"" + localeCountry + "\")); _nf.setMinimumFractionDigits(2); _nf.setMaximumFractionDigits(2); return _nf.format(%1$s); } catch (Throwable _t) { return \"" + fallback + "\"; } } }).f())";
    }

    public static void addBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("currencyMoneyToStrRU", "s", "",
                moneyBlock("ru", "RU", "0,00 \u20BD"),
                "🇷🇺 Russia: money %s"));

        list.add(blk("currencyMoneyToStrUA", "s", "",
                moneyBlock("uk", "UA", "0,00 \u20B4"),
                "🇺🇦 Ukraine: money %s"));

        list.add(blk("currencyMoneyToStrGB", "s", "",
                moneyBlock("en", "GB", "0.00 \u00A3"),
                "🇬🇧 UK: money %s"));

        list.add(blk("currencyMoneyToStrUS", "s", "",
                moneyBlock("en", "US", "$0.00"),
                "🇺🇸 USA: money %s"));

        list.add(blk("currencyMoneyToStrCN", "s", "",
                moneyBlock("zh", "CN", "0.00 \u00A5"),
                "🇨🇳 China: money %s"));

        list.add(blk("currencyMoneyToStrIN", "s", "",
                moneyBlock("hi", "IN", "0.00 \u20B9"),
                "🇮🇳 India: money %s"));
    }
}
