package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

public final class ListBlocks {

    private ListBlocks() {}

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
        list.add(blk("listSize", "d", "", "(double) %1$s.size()", "size of list %m.list %1$s"));

        list.add(blk("listIsEmpty", "b", "", "%1$s.isEmpty()", "list %m.list is empty %1$s"));

        list.add(blk("listContains", "b", "", "%1$s.contains(%2$s)", "list %m.list contains %s %1$s %2$s"));

        list.add(blk("listIndexOf", "d", "",
                "(double) %1$s.indexOf(%2$s)", "index of %2$s in list %1$s"));

        list.add(blk("listSort", " ", "", "java.util.Collections.sort(%1$s);", "sort list %m.list %1$s"));

        list.add(blk("listReverse", " ", "", "java.util.Collections.reverse(%1$s);", "reverse list %m.list %1$s"));

        // Удалить строку таблицы по индексу
        list.add(blk("listMapRemoveRow", " ", "",
                "%1$s.remove((int)(%2$s));",
                "remove row %2$d from %1$s"));

        // Удалить ключ (столбец) во всех строках таблицы
        list.add(blk("listMapRemoveKey", " ", "",
                "for (Object _row : %1$s) { if (_row instanceof java.util.Map) ((java.util.Map)_row).remove(%2$s); }",
                "remove key %2$s from all rows of %1$s"));

        // Получить список ключей (столбцов) таблицы
        list.add(blk("listMapGetKeys", " ", "", "%2$s = new java.util.ArrayList<String>(((java.util.Map)%1$s.get(0)).keySet());", "get keys of %1$s into %m.listStr %2$s"));

        // Создать пустую таблицу
        list.add(blk("listMapCreateEmpty", " ", "", "%1$s = new java.util.ArrayList<java.util.HashMap<String,Object>>();", "create empty table in %m.listMap %1$s"));
    }
}
