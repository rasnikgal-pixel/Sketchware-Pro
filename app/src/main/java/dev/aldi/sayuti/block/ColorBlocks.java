package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

public final class ColorBlocks {

    private ColorBlocks() {}

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
        list.add(blk("colorFromRgb", "d", "", "(double) android.graphics.Color.rgb((int)(%1$s), (int)(%2$s), (int)(%3$s))", "color from R %d G %d B %d %1$s %2$s %3$s"));

        list.add(blk("colorFromArgb", "d", "", "(double) android.graphics.Color.argb((int)(%1$s), (int)(%2$s), (int)(%3$s), (int)(%4$s))", "color from A %d R %d G %d B %d %1$s %2$s %3$s %4$s"));

        list.add(blk("colorRed", "d", "", "(double) android.graphics.Color.red((int)(%1$s))", "red of color %d %1$s"));

        list.add(blk("colorGreen", "d", "", "(double) android.graphics.Color.green((int)(%1$s))", "green of color %d %1$s"));

        list.add(blk("colorBlue", "d", "", "(double) android.graphics.Color.blue((int)(%1$s))", "blue of color %d %1$s"));
    }
}
