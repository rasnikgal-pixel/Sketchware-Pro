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

        // ✅ Validation: email
        list.add(blk("sciIsValidEmail", "b", "",
                "((java.util.function.Supplier<Boolean>) () -> { try { return android.util.Patterns.EMAIL_ADDRESS.matcher(%1$s).matches(); } catch (Throwable _t) { return false; } }).get()",
                "✅ Validation: %1$s is valid email"));

        // ✅ Validation: пустая строка (с учётом пробелов)
        list.add(blk("sciIsEmptyString", "b", "",
                "%1$s.trim().isEmpty()",
                "✅ Validation: %1$s is empty"));

        // 🎨 Color: parse "#RRGGBB" -> int
        list.add(blk("sciParseColor", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double) android.graphics.Color.parseColor(%1$s); } catch (Throwable _t) { return 0.0; } }).get()",
                "🎨 Color: parse %1$s to int"));

        // 🎨 Color: add alpha (0-255) to color
        list.add(blk("sciColorWithAlpha", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double) (((int)(%2$s) & 0xFFFFFF) | (((int)(%1$s) & 0xFF) << 24)); } catch (Throwable _t) { return 0.0; } }).get()",
                "🎨 Color: alpha %1$d on color %2$s"));

        // 📅 Date: текущая дата в формате
        list.add(blk("sciFormatDateNow", "s", "",
                "((java.util.function.Supplier<String>) () -> { try { return new java.text.SimpleDateFormat(%1$s, java.util.Locale.getDefault()).format(new java.util.Date()); } catch (Throwable _t) { return \"\"; } }).get()",
                "📅 Date: now in format %1$s"));

        // 📅 Date: строка с датой -> миллисекунды
        list.add(blk("sciParseDate", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double) new java.text.SimpleDateFormat(%2$s, java.util.Locale.getDefault()).parse(%1$s).getTime(); } catch (Throwable _t) { return 0.0; } }).get()",
                "📅 Date: parse %1$s with format %2$s to millis"));

        // 📁 File: имя файла из пути
        list.add(blk("sciGetFileName", "s", "",
                "((java.util.function.Supplier<String>) () -> { try { return new java.io.File(%1$s).getName(); } catch (Throwable _t) { return \"\"; } }).get()",
                "📁 File: name of %1$s"));

        // 📁 File: расширение файла из пути
        list.add(blk("sciGetFileExtension", "s", "",
                "((java.util.function.Supplier<String>) () -> { try { String _n = new java.io.File(%1$s).getName(); int _i = _n.lastIndexOf('.'); return _i > 0 ? _n.substring(_i + 1) : \"\"; } catch (Throwable _t) { return \"\"; } }).get()",
                "📁 File: extension of %1$s"));

        // 📐 Math: округлить до N знаков
        list.add(blk("sciRoundTo", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double) java.math.BigDecimal.valueOf((double)(%1$s)).setScale((int)(%2$s), java.math.RoundingMode.HALF_UP).doubleValue(); } catch (Throwable _t) { return 0.0; } }).get()",
                "📐 Math: round %1$s to %2$d decimals"));

        // 🌐 Network: есть ли интернет
        list.add(blk("sciIsNetworkAvailable", "b", "",
                "((java.util.function.Supplier<Boolean>) () -> { try { android.net.ConnectivityManager _cm = (android.net.ConnectivityManager) getSystemService(android.content.Context.CONNECTIVITY_SERVICE); android.net.NetworkInfo _ni = _cm.getActiveNetworkInfo(); return _ni != null && _ni.isConnected(); } catch (Throwable _t) { return false; } }).get()",
                "🌐 Network: is available"));




        // 💰 Accounting: Сумма прописью (рубли и копейки)
        list.add(blk("sciMoneyToWords", "s", "",
                "((java.util.function.Supplier<String>) () -> { try { " +
                "double _val = Math.abs((double)(%1$s)); " +
                "long _rub = (long) _val; " +
                "int _kop = (int) Math.round((_val - _rub) * 100); " +
                "if (_kop >= 100) { _kop = 0; _rub++; } " +
                "String[] _ed = {\"\", \"один\", \"два\", \"три\", \"четыре\", \"пять\", \"шесть\", \"семь\", \"восемь\", \"девять\"}; " +
                "String[] _edf = {\"\", \"одна\", \"две\", \"три\", \"четыре\", \"пять\", \"шесть\", \"семь\", \"восемь\", \"девять\"}; " +
                "String[] _des = {\"\", \"десять\", \"двадцать\", \"тридцать\", \"сорок\", \"пятьдесят\", \"шестьдесят\", \"семьдесят\", \"восемьдесят\", \"девяносто\"}; " +
                "String[] _sot = {\"\", \"сто\", \"двести\", \"триста\", \"четыреста\", \"пятьсот\", \"шестьсот\", \"семьсот\", \"восемьсот\", \"девятьсот\"}; " +
                "String[] _teens = {\"десять\", \"одиннадцать\", \"двенадцать\", \"тринадцать\", \"четырнадцать\", \"пятнадцать\", \"шестнадцать\", \"семнадцать\", \"восемнадцать\", \"девятнадцать\"}; " +
                "String[] _tys = {\"тысяча\", \"тысячи\", \"тысяч\"}; " +
                "String[] _mils = {\"миллион\", \"миллиона\", \"миллионов\"}; " +
                "String[] _mlds = {\"миллиард\", \"миллиарда\", \"миллиардов\"}; " +
                "java.util.function.BiFunction<Long, String[], String> _fmt = (n, forms) -> { " +
                "  long _n100 = n % 100; " +
                "  if (_n100 >= 11 && _n100 <= 19) return forms[2]; " +
                "  long _n10 = n % 10; " +
                "  if (_n10 == 1) return forms[0]; " +
                "  if (_n10 >= 2 && _n10 <= 4) return forms[1]; " +
                "  return forms[2]; " +
                "}; " +
                "StringBuilder _res = new StringBuilder(); " +
                "if (_rub == 0) { _res.append(\"ноль рублей, \"); } " +
                "else { " +
                "  long _r = _rub; " +
                "  if (_r / 1000000000 > 0) { _res.append(_fmt.apply(_r / 1000000000, _mlds)).append(\" \"); _r %= 1000000000; } " +
                "  if (_r / 1000000 > 0) { _res.append(_fmt.apply(_r / 1000000, _mils)).append(\" \"); _r %= 1000000; } " +
                "  if (_r / 1000 > 0) { long _t = _r / 1000; _res.append(_fmt.apply(_t, _tys)).append(\" \"); _r %= 1000; } " +
                "  if (_r > 0) { _res.append(_fmt.apply(_r, new String[]{\"рубль\", \"рубля\", \"рублей\"})).append(\" \"); } " +
                "  _res.append(\", \"); " +
                "} " +
                "if (_kop == 0) { _res.append(\"ноль копеек\"); } " +
                "else { _res.append(_fmt.apply((long)_kop, new String[]{\"копейка\", \"копейки\", \"копеек\"})).append(\" \"); } " +
                "String _s = _res.toString().replace(\",  \", \", \").replace(\" , \", \", \").trim(); " +
                "return _s; " +
                "} catch (Throwable _t) { return \"\"; } }).get()",
                "💰 Accounting: sum to words (rubles and kopecks)"));
    }
}
