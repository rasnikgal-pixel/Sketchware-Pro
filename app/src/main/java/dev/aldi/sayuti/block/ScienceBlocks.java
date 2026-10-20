package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Блоки палитры «Наука»: электротехника, механика, химия.
 * Формулы с префиксом раздела в spec (⚡, 🔧, 🧪) для поиска.
 */
public final class ScienceBlocks {

    private ScienceBlocks() {}

    private static final String COLOR_FUNC = "#6a1b9a";
    private static final String PALETTE_FUNC = "11";

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
        // Закон Ома
        list.add(blk("sciOhmI", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "Ohm: I = %1$s / %2$s"));

        list.add(blk("sciOhmU", "d", "",
                "(double)((double)(%1$s * %2$s))",
                "Ohm: U = %1$s * %2$s"));

        list.add(blk("sciOhmR", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "Ohm: R = %1$s / %2$s"));

        // Мощность
        list.add(blk("sciPowerUI", "d", "",
                "(double)((double)(%1$s * %2$s))",
                "Power: P = %1$s * %2$s"));

        list.add(blk("sciPowerI2R", "d", "",
                "(double)((double)(%1$s * %1$s * %2$s))",
                "Power: P = %1$s² * %2$s"));

        list.add(blk("sciPowerU2R", "d", "",
                "(double)((double)(%1$s * %1$s / %2$s))",
                "Power: P = %1$s² / %2$s"));

        // Реактивное сопротивление
        list.add(blk("sciXc", "d", "",
                "(double)((double)(1.0 / (2.0 * Math.PI * %1$s * %2$s)))",
                "Xc = 1 / (2π * %1$s * %2$s)"));

        list.add(blk("sciXl", "d", "",
                "(double)((double)(2.0 * Math.PI * %1$s * %2$s))",
                "Xl = 2π * %1$s * %2$s"));

        // Механика
        list.add(blk("sciSpeed", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "Mechanics: v = %1$s / %2$s"));

        list.add(blk("sciAccel", "d", "",
                "(double)((double)((%1$s - %2$s) / %3$s))",
                "Mechanics: a = (%1$s - %2$s) / %3$s"));

        list.add(blk("sciForce", "d", "",
                "(double)((double)(%1$s * %2$s))",
                "Mechanics: F = %1$s * %2$s"));

        // Химия
        list.add(blk("sciMolarMass", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "M = %1$s / %2$s"));

        list.add(blk("sciDensity", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "density ρ = %1$s / %2$s"));

        list.add(blk("sciPressure", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "pressure P = %1$s / %2$s"));

        list.add(blk("sciWork", "d", "",
                "(double)((double)(%1$s * %2$s))",
                "work A = %1$s * %2$s"));

        list.add(blk("sciEnergy", "d", "",
                "(double)((double)(%1$s * 9.8 * %2$s))",
                "energy E = %1$s * 9.8 * %2$s"));

        list.add(blk("sciMechPower", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "power N = %1$s / %2$s"));

        list.add(blk("sciGravity", "d", "",
                "(double)((double)(6.674e-11 * %1$s * %2$s / (%3$s * %3$s)))",
                "gravity F = G·%1$s·%2$s/%3$s²"));

        list.add(blk("sciConcentration", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "concentration C = %1$s / %2$s"));

        list.add(blk("sciSolutionMass", "d", "",
                "(double)((double)(%1$s + %2$s))",
                "solution mass = %1$s + %2$s"));

        list.add(blk("sciAmount", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "amount n = %1$s / %2$s"));

        list.add(blk("sciMolarVolume", "d", "",
                "(double)((double)(%1$s * 22.4))",
                "molar volume V = %1$s * 22.4"));

        list.add(blk("sciVat", "d", "",
                "(double)((double)(%1$s * %2$s / 100.0))",
                "VAT = %1$s * %2$s / 100"));

        list.add(blk("sciVatIncluded", "d", "",
                "(double)((double)(%1$s * (1.0 + %2$s / 100.0)))",
                "sum with VAT = %1$s * (1 + %2$s/100)"));

        list.add(blk("sciVatExtract", "d", "",
                "(double)((double)(%1$s * %2$s / (100.0 + %2$s)))",
                "extract VAT = %1$s * %2$s / (100 + %2$s)"));

        list.add(blk("sciPercent", "d", "",
                "(double)((double)(%1$s * %2$s / 100.0))",
                "percent of number = %1$s * %2$s / 100"));

        list.add(blk("sciAddPercent", "d", "",
                "(double)((double)(%1$s * (1.0 + %2$s / 100.0)))",
                "number + percent = %1$s * (1 + %2$s/100)"));

        list.add(blk("sciSubPercent", "d", "",
                "(double)((double)(%1$s * (1.0 - %2$s / 100.0)))",
                "number - percent = %1$s * (1 - %2$s/100)"));

        list.add(blk("sciProfit", "d", "",
                "(double)((double)(%1$s - %2$s))",
                "profit = %1$s - %2$s"));

        list.add(blk("sciMargin", "d", "",
                "(double)((double)((%1$s - %2$s) / %1$s * 100.0))",
                "margin % = (%1$s - %2$s) / %1$s * 100"));

        list.add(blk("sciMarkup", "d", "",
                "(double)((double)((%1$s - %2$s) / %2$s * 100.0))",
                "markup % = (%1$s - %2$s) / %2$s * 100"));

        list.add(blk("sciCompound", "d", "",
                "(double)((double)(%1$s * Math.pow(1.0 + %2$s / 100.0, %3$s)))",
                "compound interest = %1$s * (1 + %2$s/100)^%3$s"));

        list.add(blk("sciAnnuity", "d", "",
                "(double)((double)(%1$s * (%2$s / 100.0) / (1.0 - Math.pow(1.0 + %2$s / 100.0, -%3$s))))",
                "annuity payment = %1$s * %2$s / (1 - (1+%2$s)^-%3$s)"));

        list.add(blk("sciUsn6", "d", "",
                "(double)((double)(%1$s * 6.0 / 100.0))",
                "USN 6% = %1$s * 6 / 100"));

        list.add(blk("sciUsn15", "d", "",
                "(double)((double)((%1$s - %2$s) * 15.0 / 100.0))",
                "USN 15% = (%1$s - %2$s) * 15 / 100"));

        // Validation: email
        list.add(blk("sciIsValidEmail", "b", "",
                "android.util.Patterns.EMAIL_ADDRESS.matcher(%1$s).matches()",
                "%1$s is valid email"));

        // Validation: пустая строка (с учётом пробелов)
        list.add(blk("sciIsEmptyString", "b", "",
                "%1$s.trim().isEmpty()",
                "%1$s is empty"));

        // Color: parse "#RRGGBB" -> int
        list.add(blk("sciParseColor", "d", "",
                "(double)((double) android.graphics.Color.parseColor(%1$s))",
                "parse %1$s to int"));

        // Color: add alpha (0-255) to color
        list.add(blk("sciColorWithAlpha", "d", "",
                "(double)((double) (((int)(%2$s) & 0xFFFFFF) | (((int)(%1$s) & 0xFF) << 24)))",
                "alpha %1$d on color %2$s"));

        // Date: текущая дата в формате
        list.add(blk("sciFormatDateNow", "d", "",
                "((new Object() { public String f() { try { return new java.text.SimpleDateFormat(%1$s, java.util.Locale.getDefault()).format(new java.util.Date()); } catch (Throwable _t) { return \"\"; } } }).f())",
                "now in format %1$s"));

        // Date: строка с датой -> миллисекунды
        list.add(blk("sciParseDate", "d", "",
                "(double)((double) new java.text.SimpleDateFormat(%2$s, java.util.Locale.getDefault()).parse(%1$s).getTime())",
                "parse %1$s with format %2$s to millis"));

        // File: имя файла из пути
        list.add(blk("sciGetFileName", "d", "",
                "((new Object() { public String f() { try { return new java.io.File(%1$s).getName(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "name of %1$s"));

        // File: расширение файла из пути
        list.add(blk("sciGetFileExtension", "d", "",
                "((new Object() { public String f() { try { String _n = new java.io.File(%1$s).getName(); int _i = _n.lastIndexOf('.'); return _i > 0 ? _n.substring(_i + 1) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "extension of %1$s"));

        // Math: округлить до N знаков
        list.add(blk("sciRoundTo", "d", "",
                "(double)((double) java.math.BigDecimal.valueOf((double)(%1$s)).setScale((int)(%2$s), java.math.RoundingMode.HALF_UP).doubleValue())",
                "round %1$s to %2$d decimals"));

        // Network: есть ли интернет
        list.add(blk("sciIsNetworkAvailable", "b", "",
                "((new Object() { public boolean f() { try { android.net.ConnectivityManager _cm = (android.net.ConnectivityManager) getSystemService(android.content.Context.CONNECTIVITY_SERVICE); android.net.NetworkInfo _ni = _cm.getActiveNetworkInfo(); return _ni != null && _ni.isConnected(); } catch (Throwable _t) { return false; } } }).f())",
                "is available"));





    }
}