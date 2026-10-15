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
                "((java.util.function.Supplier<Double>) () -> { try { float _d = getResources().getDisplayMetrics().density; if (_d <= 0f) _d = 1f; return (double)(int)(%1$s * _d + 0.5f); } catch (Throwable _t) { return %1$s; } }).get()",
                COLOR_FUNC, PALETTE_FUNC, "%d dp to px"));

        list.add(blk("pxToDp", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { float _d = getResources().getDisplayMetrics().density; if (_d <= 0f) _d = 1f; return (double)(int)(%1$s / _d + 0.5f); } catch (Throwable _t) { return %1$s; } }).get()",
                COLOR_FUNC, PALETTE_FUNC, "%d px to dp"));

        list.add(blk("getStatusBarHeight", "d", "",
                "(double) getResources().getIdentifier(\"status_bar_height\", \"dimen\", \"android\")",
                COLOR_FUNC, PALETTE_FUNC, "status bar height resource"));

        list.add(blk("getScreenDensity", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double) getResources().getDisplayMetrics().density; } catch (Throwable _t) { return 1.0; } }).get()",
                COLOR_FUNC, PALETTE_FUNC, "screen density"));

        list.add(blk("getScreenDpi", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double) getResources().getDisplayMetrics().densityDpi; } catch (Throwable _t) { return 160.0; } }).get()",
                COLOR_FUNC, PALETTE_FUNC, "screen dpi"));

    }
}
