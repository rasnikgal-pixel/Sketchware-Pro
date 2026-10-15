package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

public final class DateTimeBlocks {

    private DateTimeBlocks() {}

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
        list.add(blk("dateYear", "d", "",
                "(double) java.util.Calendar.getInstance().get(java.util.Calendar.YEAR)",
                "current year"));

        list.add(blk("dateMonth", "d", "",
                "(double) (java.util.Calendar.getInstance().get(java.util.Calendar.MONTH) + 1)",
                "current month (1-12)"));

        list.add(blk("dateDay", "d", "",
                "(double) java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_MONTH)",
                "current day of month"));

        list.add(blk("dateHour", "d", "",
                "(double) java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)",
                "current hour (0-23)"));

        list.add(blk("dateMinute", "d", "",
                "(double) java.util.Calendar.getInstance().get(java.util.Calendar.MINUTE)",
                "current minute"));

        list.add(blk("dateSecond", "d", "",
                "(double) java.util.Calendar.getInstance().get(java.util.Calendar.SECOND)",
                "current second"));
    }
}
