package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

public final class MathBlocks {

    private MathBlocks() {}

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
        list.add(blk("mathClamp", "d", "",
                "Math.max(%2$s, Math.min(%3$s, %1$s))",
                "clamp %1$s between %2$s and %3$s"));

        list.add(blk("mathLerp", "d", "",
                "(%2$s + (%3$s - %2$s) * %1$s)",
                "lerp %1$s from %2$s to %3$s"));

        list.add(blk("mathDistance2d", "d", "",
                "Math.hypot(%3$s - %1$s, %4$s - %2$s)",
                "distance between (%1$s, %2$s) and (%3$s, %4$s)"));

        list.add(blk("mathDegToRad", "d", "",
                "Math.toRadians(%1$s)", "%s degrees to radians"));

        list.add(blk("mathRadToDeg", "d", "",
                "Math.toDegrees(%1$s)", "%s radians to degrees"));

        list.add(blk("mathSign", "d", "",
                "Math.signum(%1$s)", "sign of %s"));

        list.add(blk("mathLog2", "d", "",
                "(Math.log(%1$s) / Math.log(2))", "log2 of %s"));

        list.add(blk("mathRandomFloat", "d", "",
                "Math.random()", "random float 0..1"));

        // Число -> строка
        list.add(blk("mathNumToStr", "s", "",
                "String.valueOf(%1$s)",
                "number %1$s to string"));

        // Строка -> число (безопасно, 0 если не число)
        list.add(blk("mathStrToNum", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return Double.parseDouble(%1$s.trim()); } catch (Throwable _t) { return 0.0; } }).get()",
                "string %1$s to number (0 if invalid)"));

        // Сумма -> текст в рублях и копейках
        list.add(blk("mathMoneyToStr", "s", "",
                "((java.util.function.Supplier<String>) () -> { try { java.text.NumberFormat _nf = java.text.NumberFormat.getCurrencyInstance(new java.util.Locale(\"ru\", \"RU\")); _nf.setMinimumFractionDigits(2); _nf.setMaximumFractionDigits(2); return _nf.format(%1$s); } catch (Throwable _t) { return \"0,00 \u20BD\"; } }).get()",
                "money %1$s as rubles and kopecks"));
    }
}
