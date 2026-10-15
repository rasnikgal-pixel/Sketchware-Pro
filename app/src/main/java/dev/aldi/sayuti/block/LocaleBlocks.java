package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

public final class LocaleBlocks {

    private LocaleBlocks() {}

    private static final String COLOR_FUNC = "#4a6cd4";
    private static final String PALETTE_FUNC = "7";
    private static final String COLOR_OP = "#5cb722";
    private static final String PALETTE_OP = "3";

    private static HashMap<String, Object> blk(String name, String type, String typeName,
                                                 String code, String color, String palette,
                                                 String spec) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("type", type);
        m.put("typeName", typeName);
        m.put("code", code);
        m.put("color", color);
        m.put("palette", palette);
        m.put("spec", spec);
        return m;
    }

    public static void addBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("formatNumber", "s", "",
                "java.text.NumberFormat.getInstance().format(%1$s)",
                COLOR_FUNC, PALETTE_FUNC, "format number %d"));

        list.add(blk("formatCurrency", "s", "",
                "java.text.NumberFormat.getCurrencyInstance().format(%1$s)",
                COLOR_FUNC, PALETTE_FUNC, "format currency %d"));

        list.add(blk("formatPercent", "s", "",
                "java.text.NumberFormat.getPercentInstance().format(%1$s)",
                COLOR_FUNC, PALETTE_FUNC, "format percent %d"));

        list.add(blk("setLocale", " ", "",
                "androidx.appcompat.app.AppCompatDelegate.setApplicationLocales(new android.os.LocaleList(new java.util.Locale(%1$s)));",
                COLOR_OP, PALETTE_OP, "set locale to %s"));
    }
}
