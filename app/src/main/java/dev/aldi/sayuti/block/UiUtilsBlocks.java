package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

public final class UiUtilsBlocks {

    private UiUtilsBlocks() {}

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
        list.add(blk("dpToPx", "d", "",
                "(double) ((int) (%1$s * getResources().getDisplayMetrics().density + 0.5f))",
                COLOR_FUNC, PALETTE_FUNC, "%d dp to px"));

        list.add(blk("pxToDp", "d", "",
                "(double) ((int) (%1$s / getResources().getDisplayMetrics().density + 0.5f))",
                COLOR_FUNC, PALETTE_FUNC, "%d px to dp"));

        list.add(blk("getStatusBarHeight", "d", "",
                "(double) getResources().getIdentifier(\"status_bar_height\", \"dimen\", \"android\")",
                COLOR_FUNC, PALETTE_FUNC, "status bar height resource"));

        list.add(blk("getScreenDensity", "d", "",
                "(double) getResources().getDisplayMetrics().density",
                COLOR_FUNC, PALETTE_FUNC, "screen density"));

        list.add(blk("getScreenDpi", "d", "",
                "(double) getResources().getDisplayMetrics().densityDpi",
                COLOR_FUNC, PALETTE_FUNC, "screen dpi"));

    }
}
