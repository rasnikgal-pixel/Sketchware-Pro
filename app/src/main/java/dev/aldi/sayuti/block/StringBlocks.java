package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Блоки для работы со строками: регистр, обрезка, поиск, замена, разбиение.
 */
public final class StringBlocks {

    private StringBlocks() {}

    private static final String COLOR_FUNC = "#4a6cd4";
    private static final String PALETTE_FUNC = "7";

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

    public static void addBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("strToUpper", "s", "", "%1$s.toUpperCase()", "%s to uppercase %1$s"));

        list.add(blk("strToLower", "s", "", "%1$s.toLowerCase()", "%s to lowercase %1$s"));

        list.add(blk("strTrim", "s", "", "%1$s.trim()", "%s trimmed %1$s"));

        list.add(blk("strReplace", "s", "",
                "%1$s.replace(%2$s, %3$s)", "%1$s replace %2$s with %3$s"));

        list.add(blk("strSplitGet", "s", "",
                "((java.util.function.Supplier<String>) () -> { try { String[] _parts = %1$s.split(java.util.regex.Pattern.quote(%2$s)); int _i = (int)(%3$s); return (_i >= 0 && _i < _parts.length) ? _parts[_i] : \"\"; } catch (Throwable _t) { return \"\"; } }).get()",
                "%1$s split by %2$s get item %3$d"));

        list.add(blk("strSplitSize", "d", "",
                "(double) %1$s.split(java.util.regex.Pattern.quote(%2$s)).length",
                "%1$s split by %2$s count"));

        list.add(blk("strLength", "d", "", "(double) %1$s.length()", "length of %s %1$s"));

        list.add(blk("strContains", "b", "",
                "%1$s.contains(%2$s)", "%1$s contains %2$s"));

        list.add(blk("strStartsWith", "b", "",
                "%1$s.startsWith(%2$s)", "%1$s starts with %2$s"));

        list.add(blk("strEndsWith", "b", "",
                "%1$s.endsWith(%2$s)", "%1$s ends with %2$s"));

        list.add(blk("strIndexOf", "d", "",
                "(double) %1$s.indexOf(%2$s)", "index of %2$s in %1$s"));

        list.add(blk("strReverse", "s", "", "new StringBuilder(%1$s).reverse().toString()", "%s reversed %1$s"));
    }
}
