package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

public final class RegexBlocks {

    private RegexBlocks() {}

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
        list.add(blk("regexMatches", "b", "",
                "%1$s.matches(%2$s)",
                "%s fully matches regex %s"));

        list.add(blk("regexFind", "s", "",
                "((java.util.function.Supplier<String>) () -> { try { java.util.regex.Matcher _m = java.util.regex.Pattern.compile(%2$s).matcher(%1$s); return _m.find() ? _m.group() : \"\"; } catch (Throwable _t) { return \"\"; } }).get()",
                "first match of regex %s in %s"));

        list.add(blk("regexFindAll", "s", "",
                "((java.util.function.Supplier<String>) () -> { StringBuilder _sb = new StringBuilder(); try { java.util.regex.Matcher _m = java.util.regex.Pattern.compile(%2$s).matcher(%1$s); while (_m.find()) { _sb.append(_m.group()).append(\"\\n\"); } } catch (Throwable _t) {} return _sb.toString(); }).get()",
                "all matches of regex %s in %s"));

        list.add(blk("regexReplaceAll", "s", "",
                "%1$s.replaceAll(%2$s, %3$s)",
                "%s replace all regex %s with %s"));

        list.add(blk("regexReplaceFirst", "s", "",
                "%1$s.replaceFirst(%2$s, %3$s)",
                "%s replace first regex %s with %s"));

        list.add(blk("regexSplit", "s", "",
                "((java.util.function.Supplier<String>) () -> { StringBuilder _sb = new StringBuilder(); for (String _s : %1$s.split(%2$s)) { _sb.append(_s).append(\"\\n\"); } return _sb.toString(); }).get()",
                "%s split by regex %s"));

        list.add(blk("regexGroup", "s", "",
                "((java.util.function.Supplier<String>) () -> { try { java.util.regex.Matcher _m = java.util.regex.Pattern.compile(%2$s).matcher(%1$s); return (_m.find() && _m.groupCount() >= (int)(%3$s)) ? _m.group((int)(%3$s)) : \"\"; } catch (Throwable _t) { return \"\"; } }).get()",
                "group %d of regex %s in %s"));

        list.add(blk("regexIsValid", "b", "", "((java.util.function.Supplier<Boolean>) () -> { try { java.util.regex.Pattern.compile(%1$s); return true; } catch (Throwable _t) { return false; } }).get()", "regex %s is valid"));
    }
}
