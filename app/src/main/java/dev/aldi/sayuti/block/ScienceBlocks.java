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
        // ⚡ Закон Ома
        list.add(blk("sciOhmI", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s / %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "⚡ Ohm: I = U / R"));

        list.add(blk("sciOhmU", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "⚡ Ohm: U = I * R"));

        list.add(blk("sciOhmR", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s / %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "⚡ Ohm: R = U / I"));

        // ⚡ Мощность
        list.add(blk("sciPowerUI", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "⚡ Power: P = U * I"));

        list.add(blk("sciPowerI2R", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * %1$s * %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "⚡ Power: P = I² * R"));

        list.add(blk("sciPowerU2R", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * %1$s / %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "⚡ Power: P = U² / R"));

        // ⚡ Реактивное сопротивление
        list.add(blk("sciXc", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(1.0 / (2.0 * Math.PI * %1$s * %2$s)); } catch (Throwable _t) { return 0.0; } }).get()",
                "⚡ Xc = 1 / (2πfC)"));

        list.add(blk("sciXl", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(2.0 * Math.PI * %1$s * %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "⚡ Xl = 2πfL"));

        // 🔧 Механика
        list.add(blk("sciSpeed", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s / %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "🔧 Mechanics: v = s / t"));

        list.add(blk("sciAccel", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)((%1$s - %2$s) / %3$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "🔧 Mechanics: a = (v - v0) / t"));

        list.add(blk("sciForce", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "🔧 Mechanics: F = m * a"));

        // 🧪 Химия
        list.add(blk("sciMolarMass", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s / %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "🧪 Chemistry: M = m / n"));

        list.add(blk("sciDensity", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s / %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "🔧 Physics: density ρ = m / V"));

        list.add(blk("sciPressure", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s / %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "🔧 Physics: pressure P = F / S"));

        list.add(blk("sciWork", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "🔧 Physics: work A = F * s"));

        list.add(blk("sciEnergy", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * 9.8 * %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "🔧 Physics: energy E = mgh (g=9.8)"));

        list.add(blk("sciMechPower", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s / %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "🔧 Physics: power N = A / t"));

        list.add(blk("sciGravity", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(6.674e-11 * %1$s * %2$s / (%3$s * %3$s)); } catch (Throwable _t) { return 0.0; } }).get()",
                "🔧 Physics: gravity F = G·m1·m2/r²"));

        list.add(blk("sciConcentration", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s / %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "🧪 Chemistry: concentration C = n / V"));

        list.add(blk("sciSolutionMass", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s + %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "🧪 Chemistry: solution mass = m1 + m2"));

        list.add(blk("sciAmount", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s / %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "🧪 Chemistry: amount n = m / M"));

        list.add(blk("sciMolarVolume", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * 22.4); } catch (Throwable _t) { return 0.0; } }).get()",
                "🧪 Chemistry: molar volume V = n · 22.4"));

        list.add(blk("sciVat", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * %2$s / 100.0); } catch (Throwable _t) { return 0.0; } }).get()",
                "💰 Accounting: VAT = sum · rate / 100"));

        list.add(blk("sciVatIncluded", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * (1.0 + %2$s / 100.0)); } catch (Throwable _t) { return 0.0; } }).get()",
                "💰 Accounting: sum with VAT"));

        list.add(blk("sciVatExtract", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * %2$s / (100.0 + %2$s)); } catch (Throwable _t) { return 0.0; } }).get()",
                "💰 Accounting: extract VAT"));

        list.add(blk("sciPercent", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * %2$s / 100.0); } catch (Throwable _t) { return 0.0; } }).get()",
                "💰 Accounting: percent of number"));

        list.add(blk("sciAddPercent", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * (1.0 + %2$s / 100.0)); } catch (Throwable _t) { return 0.0; } }).get()",
                "💰 Accounting: number + percent"));

        list.add(blk("sciSubPercent", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * (1.0 - %2$s / 100.0)); } catch (Throwable _t) { return 0.0; } }).get()",
                "💰 Accounting: number - percent"));

        list.add(blk("sciProfit", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s - %2$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "💰 Accounting: profit = income - expense"));

        list.add(blk("sciMargin", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)((%1$s - %2$s) / %1$s * 100.0); } catch (Throwable _t) { return 0.0; } }).get()",
                "💰 Accounting: margin % = (profit/income)·100"));

        list.add(blk("sciMarkup", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)((%1$s - %2$s) / %2$s * 100.0); } catch (Throwable _t) { return 0.0; } }).get()",
                "💰 Accounting: markup % = (profit/cost)·100"));

        list.add(blk("sciCompound", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * Math.pow(1.0 + %2$s / 100.0, %3$s)); } catch (Throwable _t) { return 0.0; } }).get()",
                "💰 Accounting: compound interest"));

        list.add(blk("sciAnnuity", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * (%2$s / 100.0) / (1.0 - Math.pow(1.0 + %2$s / 100.0, -%3$s))); } catch (Throwable _t) { return 0.0; } }).get()",
                "💰 Accounting: annuity payment"));

        list.add(blk("sciUsn6", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)(%1$s * 6.0 / 100.0); } catch (Throwable _t) { return 0.0; } }).get()",
                "💰 Accounting: USN 6%"));

        list.add(blk("sciUsn15", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double)((%1$s - %2$s) * 15.0 / 100.0); } catch (Throwable _t) { return 0.0; } }).get()",
                "💰 Accounting: USN 15%"));
    }
}
