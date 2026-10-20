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






        // 🌐 HTTP: GET запрос (возвращает текст ответа)
        list.add(blk("sciHttpGet", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"GET\"); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) { _sb.append(_l); } _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http get %1$s"));

        // 🌐 HTTP: POST запрос (отправляет данные, возвращает ответ)
        list.add(blk("sciHttpPost", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"POST\"); _c.setDoOutput(true); java.io.OutputStream _os = _c.getOutputStream(); _os.write(%2$s.getBytes(\"UTF-8\")); _os.flush(); _os.close(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) { _sb.append(_l); } _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http post %1$s data %2$s"));

        // 📧 Email: отправка через почтовый клиент
        list.add(blk("sciSendEmail", " ", "",
                "try { android.content.Intent _i = new android.content.Intent(android.content.Intent.ACTION_SENDTO); _i.setData(android.net.Uri.parse(\"mailto:\")); _i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{%1$s}); _i.putExtra(android.content.Intent.EXTRA_SUBJECT, %2$s); _i.putExtra(android.content.Intent.EXTRA_TEXT, %3$s); %4$s.startActivity(android.content.Intent.createChooser(_i, \"Send email\")); } catch (Throwable _t) {}",
                "send email to %1$s subject %2$s body %3$s from %m.context"));

        // 📁 File: имя файла без расширения
        list.add(blk("sciGetFileNameNoExt", "d", "",
                "((new Object() { public String f() { try { String _n = new java.io.File(%1$s).getName(); int _i = _n.lastIndexOf('.'); return _i > 0 ? _n.substring(0, _i) : _n; } catch (Throwable _t) { return \"\"; } } }).f())",
                "file name without extension %1$s"));

        // 📁 File: полный путь к файлу (абсолютный)
        list.add(blk("sciGetFilePath", "d", "",
                "((new Object() { public String f() { try { return new java.io.File(%1$s).getAbsolutePath(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "file path %1$s"));

        // 📝 Text: удалить строку по номеру
        list.add(blk("sciTextDeleteLine", " ", "",
                "((new Object() { public void f() { try { String[] _l = %1$s.split(\"\\n\", -1); StringBuilder _sb = new StringBuilder(); for (int _i = 0; _i < _l.length; _i++) { if (_i != (int)(%2$s) - 1) { _sb.append(_l[_i]).append(\"\\n\"); } } %1$s = _sb.toString().trim(); } catch (Throwable _t) {} } }).f());",
                "delete line %2$d from %1$s"));

        // 📝 Text: получить текст строки по номеру
        list.add(blk("sciTextGetLine", "d", "",
                "((new Object() { public String f() { try { String[] _l = %1$s.split(\"\\n\", -1); int _n = (int)(%2$s) - 1; return (_n >= 0 && _n < _l.length) ? _l[_n] : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "get line %2$d from %1$s"));

        // 📝 Text: очистить текст
        list.add(blk("sciTextClear", " ", "",
                "%1$s = \"\";",
                "clear text %1$s"));

        // 📝 Text: длина текста
        list.add(blk("sciTextLength", "d", "",
                "(double)(%1$s.length())",
                "length of %1$s"));

        // 📝 Text: длина без пробелов
        list.add(blk("sciTextLengthNoSpaces", "d", "",
                "(double)(%1$s.replace(\" \", \"\").length())",
                "length without spaces of %1$s"));

        // 📝 Text: верхний регистр
        list.add(blk("sciTextUpper", "d", "",
                "%1$s.toUpperCase()",
                "uppercase of %1$s"));

        // 📝 Text: нижний регистр
        list.add(blk("sciTextLower", "d", "",
                "%1$s.toLowerCase()",
                "lowercase of %1$s"));

        // 📝 Text: длина строки по номеру
        list.add(blk("sciTextLineLength", "d", "",
                "((new Object() { public double f() { try { String[] _l = %1$s.split(\"\\n\", -1); int _n = (int)(%2$s) - 1; return (_n >= 0 && _n < _l.length) ? (double)_l[_n].length() : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "line %2$d length of %1$s"));

        // 📝 Text: реверс строк
        list.add(blk("sciTextReverseLines", "d", "",
                "((new Object() { public String f() { try { String[] _l = %1$s.split(\"\\n\", -1); StringBuilder _sb = new StringBuilder(); for (int _i = _l.length - 1; _i >= 0; _i--) { _sb.append(_l[_i]); if (_i > 0) _sb.append(\"\\n\"); } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "reverse lines of %1$s"));

        // 📝 Text: символ по индексу
        list.add(blk("sciTextCharAt", "d", "",
                "((new Object() { public String f() { try { int _i = (int)(%2$s); return (_i >= 0 && _i < %1$s.length()) ? String.valueOf(%1$s.charAt(_i)) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "char at %2$d of %1$s"));

        // 📝 Text: пустой ли текст
        list.add(blk("sciTextIsEmpty", "b", "",
                "%1$s.trim().isEmpty()",
                "is empty %1$s"));

        // 📝 Text: реверс текста (все символы)
        list.add(blk("sciTextReverse", "d", "",
                "new StringBuilder(%1$s).reverse().toString()",
                "reverse text of %1$s"));

        // 📝 Text: заменить текст в диапазоне
        list.add(blk("sciTextReplaceRange", " ", "",
                "((new Object() { public void f() { try { int _s = (int)(%2$s); int _l = (int)(%3$s); %1$s = %1$s.substring(0, _s) + %4$s + %1$s.substring(_s + _l); } catch (Throwable _t) {} } }).f());",
                "replace in %1$s from %2$d length %3$d with %4$s"));

        // 📝 Text: подстрока с индекса
        list.add(blk("sciTextSubstring", "d", "",
                "((new Object() { public String f() { try { int _s = (int)(%2$s); return (_s >= 0 && _s < %1$s.length()) ? %1$s.substring(_s) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "substring from %2$d of %1$s"));

        // 📝 Text: капитализация слов
        list.add(blk("sciTextCapitalize", "d", "",
                "((new Object() { public String f() { try { String[] _w = %1$s.split(\" \"); StringBuilder _sb = new StringBuilder(); for (String _s : _w) { if (!_s.isEmpty()) { _sb.append(Character.toUpperCase(_s.charAt(0))).append(_s.substring(1).toLowerCase()).append(\" \"); } } return _sb.toString().trim(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "capitalize words of %1$s"));

        // 📝 Text: удалить все пробелы
        list.add(blk("sciTextRemoveSpaces", "d", "",
                "%1$s.replace(\" \", \"\")",
                "remove spaces from %1$s"));

        // 📝 Text: удалить однострочные комментарии (//)
        list.add(blk("sciTextRemoveLineComments", "d", "",
                "%1$s.replaceAll(\"//.*\", \"\")",
                "remove // comments from %1$s"));

        // 📝 Text: удалить все комментарии (// и /* */)
        list.add(blk("sciTextRemoveAllComments", "d", "",
                "%1$s.replaceAll(\"//.*|/\\\\*[\\\\s\\\\S]*?\\\\*/\", \"\")",
                "remove all comments from %1$s"));

        // 📝 Text: установить подсказку (hint) для поля
        list.add(blk("sciTextSetHint", " ", "",
                "%1$s.setHint(%2$s);",
                "set hint %2$s on %1$s"));

        // 🛡️ Security: MD5
        list.add(blk("sciMd5", "d", "",
                "((new Object() { public String f() { try { java.security.MessageDigest _md = java.security.MessageDigest.getInstance(\"MD5\"); byte[] _b = _md.digest(%1$s.getBytes()); StringBuilder _sb = new StringBuilder(); for (byte _x : _b) _sb.append(String.format(\"%02x\", _x)); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "md5 of %1$s"));

        // 🛡️ Security: Base64 Encode
        list.add(blk("sciBase64Encode", "d", "",
                "android.util.Base64.encodeToString(%1$s.getBytes(), android.util.Base64.NO_WRAP)",
                "base64 encode %1$s"));

        // 🛡️ Security: Base64 Decode
        list.add(blk("sciBase64Decode", "d", "",
                "new String(android.util.Base64.decode(%1$s, android.util.Base64.NO_WRAP))",
                "base64 decode %1$s"));

        // 🛡️ Security: SHA-256
        list.add(blk("sciSha256", "d", "",
                "((new Object() { public String f() { try { java.security.MessageDigest _md = java.security.MessageDigest.getInstance(\"SHA-256\"); byte[] _b = _md.digest(%1$s.getBytes()); StringBuilder _sb = new StringBuilder(); for (byte _x : _b) _sb.append(String.format(\"%02x\", _x)); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "sha256 of %1$s"));

        // 📅 Date: текущая дата (dd.MM.yyyy)
        list.add(blk("sciDateToday", "d", "",
                "new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()).format(new java.util.Date())",
                "today date"));

        // 📅 Date: текущее время (HH:mm)
        list.add(blk("sciTimeNow", "d", "",
                "new java.text.SimpleDateFormat(\"HH:mm\", java.util.Locale.getDefault()).format(new java.util.Date())",
                "current time"));

        // 📅 Date: разница в днях между двумя датами (dd.MM.yyyy)
        list.add(blk("sciDaysBetween", "d", "",
                "((new Object() { public double f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()); long _d1 = _fmt.parse(%1$s).getTime(); long _d2 = _fmt.parse(%2$s).getTime(); return (double) Math.abs((_d2 - _d1) / (1000 * 60 * 60 * 24)); } catch (Throwable _t) { return 0.0; } } }).f())",
                "days between %1$s and %2$s"));

        // 📱 Device: модель устройства
        list.add(blk("sciDeviceModel", "d", "",
                "android.os.Build.MODEL",
                "device model"));

        // 📱 Device: версия Android
        list.add(blk("sciAndroidVersion", "d", "",
                "android.os.Build.VERSION.RELEASE",
                "android version"));

        // 📱 Device: заряд батареи (%)
        list.add(blk("sciBatteryLevel", "d", "",
                "((new Object() { public double f() { try { android.content.IntentFilter _if = new android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED); android.content.Intent _b = getApplicationContext().registerReceiver(null, _if); int _lvl = _b.getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, -1); int _scale = _b.getIntExtra(android.os.BatteryManager.EXTRA_SCALE, -1); return (double)(_lvl * 100 / (float)_scale); } catch (Throwable _t) { return 0.0; } } }).f())",
                "battery level"));

        // 📋 List: объединить два списка в строку через разделитель
        list.add(blk("sciListJoin", "d", "",
                "((new Object() { public String f() { try { StringBuilder _sb = new StringBuilder(); for (int _i = 0; _i < %1$s.size(); _i++) { if (_i > 0) _sb.append(%2$s); _sb.append(%1$s.get(_i)); } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "join list %1$s with %2$s"));

        // 📋 List: отсортировать список (строки/числа)
        list.add(blk("sciListSort", " ", "",
                "((new Object() { public void f() { try { java.util.Collections.sort(%1$s); } catch (Throwable _t) {} } }).f());",
                "sort list %1$s"));

        // 📋 List: перевернуть список
        list.add(blk("sciListReverse", " ", "",
                "((new Object() { public void f() { try { java.util.Collections.reverse(%1$s); } catch (Throwable _t) {} } }).f());",
                "reverse list %1$s"));

        // 📋 List: удалить дубликаты (для List String)
        list.add(blk("sciListUnique", " ", "",
                "((new Object() { public void f() { try { java.util.LinkedHashSet _set = new java.util.LinkedHashSet(%1$s); %1$s.clear(); %1$s.addAll(_set); } catch (Throwable _t) {} } }).f());",
                "remove duplicates from %1$s"));

        // 📋 List: получить первый элемент
        list.add(blk("sciListFirst", "d", "",
                "((new Object() { public String f() { try { return %1$s.isEmpty() ? \"\" : String.valueOf(%1$s.get(0)); } catch (Throwable _t) { return \"\"; } } }).f())",
                "first of %1$s"));

        // 📋 List: получить последний элемент
        list.add(blk("sciListLast", "d", "",
                "((new Object() { public String f() { try { return %1$s.isEmpty() ? \"\" : String.valueOf(%1$s.get(%1$s.size() - 1)); } catch (Throwable _t) { return \"\"; } } }).f())",
                "last of %1$s"));

        // 📋 List: найти индекс элемента
        list.add(blk("sciListIndexOf", "d", "",
                "(double)(%1$s.indexOf(%2$s))",
                "index of %2$s in %1$s"));

        // 📋 List: создать копию списка
        list.add(blk("sciListCopy", " ", "",
                "((new Object() { public void f() { try { %2$s.clear(); %2$s.addAll(%1$s); } catch (Throwable _t) {} } }).f());",
                "copy %1$s to %2$s"));

        // 📋 List: перемешать список (случайно)
        list.add(blk("sciListShuffle", " ", "",
                "((new Object() { public void f() { try { java.util.Collections.shuffle(%1$s); } catch (Throwable _t) {} } }).f());",
                "shuffle %1$s"));

        // 📦 JSON: получить значение по ключу (из строки JSON)
        list.add(blk("sciJsonGet", "d", "",
                "((new Object() { public String f() { try { org.json.JSONObject _o = new org.json.JSONObject(%1$s); return _o.optString(%2$s, \"\"); } catch (Throwable _t) { return \"\"; } } }).f())",
                "json get %2$s from %1$s"));

        // 📦 JSON: создать строку JSON из ключа и значения
        list.add(blk("sciJsonCreate", "d", "",
                "((new Object() { public String f() { try { org.json.JSONObject _o = new org.json.JSONObject(); _o.put(%1$s, %2$s); return _o.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "json create key %1$s value %2$s"));

        // 📦 JSON: массив из списка строк
        list.add(blk("sciJsonArray", "d", "",
                "new org.json.JSONArray(%1$s).toString()",
                "json array from list %1$s"));

        // 🗄️ SQLite: открыть/создать БД
        list.add(blk("sciSqliteOpen", " ", "",
                "((new Object() { public void f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); _db.close(); } catch (Throwable _t) {} } }).f());",
                "open sqlite db %1$s from %m.context"));

        // 🗄️ SQLite: выполнить запрос (INSERT/UPDATE/DELETE)
        list.add(blk("sciSqliteExec", " ", "",
                "((new Object() { public void f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); _db.execSQL(%3$s); _db.close(); } catch (Throwable _t) {} } }).f());",
                "sqlite exec %3$s in %1$s"));

        // 🗄️ SQLite: запрос SELECT (возвращает курсор в виде строки)
        list.add(blk("sciSqliteQuery", "d", "",
                "((new Object() { public String f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); android.database.Cursor _c = _db.rawQuery(%3$s, null); StringBuilder _sb = new StringBuilder(); while (_c.moveToNext()) { for (int _i = 0; _i < _c.getColumnCount(); _i++) { _sb.append(_c.getString(_i)).append(\" | \"); } _sb.append(\"\\n\"); } _c.close(); _db.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "sqlite query %3$s in %1$s"));

        // 🎲 Random: случайное целое от min до max
        list.add(blk("sciRandomInt", "d", "",
                "(double)(new java.util.Random().nextInt((int)(%2$s - %1$s + 1)) + (int)(%1$s))",
                "random int from %1$d to %2$d"));

        // 🎲 Random: случайная строка из набора символов
        list.add(blk("sciRandomString", "d", "",
                "((new Object() { public String f() { try { String _chars = \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\"; StringBuilder _sb = new StringBuilder(); java.util.Random _r = new java.util.Random(); for (int _i = 0; _i < (int)(%1$s); _i++) _sb.append(_chars.charAt(_r.nextInt(_chars.length()))); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "random string length %1$d"));

        // 🎲 Random: случайный элемент из списка
        list.add(blk("sciRandomFromList", "d", "",
                "((new Object() { public String f() { try { return %1$s.isEmpty() ? \"\" : String.valueOf(%1$s.get(new java.util.Random().nextInt(%1$s.size()))); } catch (Throwable _t) { return \"\"; } } }).f())",
                "random from list %1$s"));

        // 🔔 Notification: показать уведомление (Toast)
        list.add(blk("sciShowToast", " ", "",
                "android.widget.Toast.makeText(%2$s, %1$s, android.widget.Toast.LENGTH_SHORT).show();",
                "show toast %1$s from %m.context"));

        // 🔔 Notification: показать системное уведомление
        list.add(blk("sciShowNotification", " ", "",
                "((new Object() { public void f() { try { androidx.core.app.NotificationCompat.Builder _b = new androidx.core.app.NotificationCompat.Builder(%1$s, \"default\").setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle(%2$s).setContentText(%3$s).setAutoCancel(true); androidx.core.app.NotificationManagerCompat.from(%1$s).notify((int)System.currentTimeMillis(), _b.build()); } catch (Throwable _t) {} } }).f());",
                "show notification %2$s %3$s from %m.context"));

        // 📱 Intent: открыть ссылку в браузере
        list.add(blk("sciOpenUrl", " ", "",
                "%2$s.startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(%1$s)));",
                "open url %1$s from %m.context"));

        // 📱 Intent: позвонить по номеру
        list.add(blk("sciCallPhone", " ", "",
                "%2$s.startActivity(new android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse(\"tel:\" + %1$s)));",
                "call %1$s from %m.context"));

        // 📱 Intent: отправить SMS
        list.add(blk("sciSendSms", " ", "",
                "%3$s.startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(\"sms:\" + %1$s + \"?body=\" + android.net.Uri.encode(%2$s))));",
                "send sms to %1$s text %2$s from %m.context"));

        // 📱 Intent: открыть другое приложение по пакету
        list.add(blk("sciOpenApp", " ", "",
                "try { %2$s.startActivity(%2$s.getPackageManager().getLaunchIntentForPackage(%1$s)); } catch (Throwable _t) {}",
                "open app %1$s from %m.context"));

        // 📱 Intent: поделиться текстом
        list.add(blk("sciShareText", " ", "",
                "android.content.Intent _s = new android.content.Intent(android.content.Intent.ACTION_SEND); _s.setType(\"text/plain\"); _s.putExtra(android.content.Intent.EXTRA_TEXT, %1$s); %2$s.startActivity(android.content.Intent.createChooser(_s, \"Share\"));",
                "share text %1$s from %m.context"));

        // 📁 File: прочитать текст из файла
        list.add(blk("sciFileRead", "d", "",
                "((new Object() { public String f() { try { java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.FileReader(%1$s)); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) { _sb.append(_l).append(\"\\n\"); } _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "read file %1$s"));

        // 📁 File: записать текст в файл
        list.add(blk("sciFileWrite", " ", "",
                "((new Object() { public void f() { try { java.io.FileWriter _w = new java.io.FileWriter(%1$s); _w.write(%2$s); _w.close(); } catch (Throwable _t) {} } }).f());",
                "write %2$s to file %1$s"));

        // 📁 File: проверить существование файла
        list.add(blk("sciFileExists", "b", "",
                "new java.io.File(%1$s).exists()",
                "file %1$s exists"));

        // 📁 File: удалить файл
        list.add(blk("sciFileDelete", " ", "",
                "((new Object() { public void f() { try { new java.io.File(%1$s).delete(); } catch (Throwable _t) {} } }).f());",
                "delete file %1$s"));

        // 🌐 HTTP: GET с заголовком
        list.add(blk("sciHttpGetHeader", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestProperty(%2$s, %3$s); _c.setRequestMethod(\"GET\"); _c.setConnectTimeout(5000); _c.setReadTimeout(5000); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) { _sb.append(_l); } _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http get %1$s header %2$s: %3$s"));

        // 🎨 Color: смешать два цвета (50/50)
        list.add(blk("sciColorMix", "d", "",
                "(((int)(%1$s) & 0xFF000000) | ((((int)(%1$s) & 0xFF0000) + ((int)(%2$s) & 0xFF0000)) / 2 & 0xFF0000) | ((((int)(%1$s) & 0xFF00) + ((int)(%2$s) & 0xFF00)) / 2 & 0xFF00) | ((((int)(%1$s) & 0xFF) + ((int)(%2$s) & 0xFF)) / 2 & 0xFF))",
                "mix color %1$s and %2$s"));

        // 🎨 Color: получить прозрачность (0-255)
        list.add(blk("sciColorAlpha", "d", "",
                "(double)(((int)(%1$s) >> 24) & 0xFF)",
                "alpha of %1$s"));

        // 🎨 Color: HEX в цвет (int)
        list.add(blk("sciColorFromHex", "d", "",
                "android.graphics.Color.parseColor(%1$s)",
                "color from hex %1$s"));

        // 🎨 Color: цвет в HEX-строку
        list.add(blk("sciColorToHex", "d", "",
                "String.format(\"#%08X\", (int)(%1$s))",
                "color %1$s to hex"));

        // 📊 Progress: установить прогресс (0-100)
        list.add(blk("sciSetProgress", " ", "",
                "%1$s.setProgress((int)(%2$s));",
                "set progress %2$d on %1$s"));

        // 📊 Progress: показать/скрыть прогресс-бар
        list.add(blk("sciSetProgressVisible", " ", "",
                "%1$s.setVisibility((%2$s) ? android.view.View.VISIBLE : android.view.View.GONE);",
                "set progress %1$s visible %2$s"));

        // 📳 Vibrate: вибрация (миллисекунды)
        list.add(blk("sciVibrate", " ", "",
                "((android.os.Vibrator) %2$s.getSystemService(android.content.Context.VIBRATOR_SERVICE)).vibrate((long)(%1$s));",
                "vibrate %1$d ms from %m.context"));

        // 🧮 Calc: вычислить математическое выражение из строки
        list.add(blk("sciCalcExpression", "d", "",
                "((new Object() { public double f() { try { javax.script.ScriptEngine _e = new javax.script.ScriptEngineManager().getEngineByName(\"js\"); return ((Number) _e.eval(%1$s)).doubleValue(); } catch (Throwable _t) { return 0.0; } } }).f())",
                "calc expression %1$s"));

        // 🔐 AES: зашифровать текст
        list.add(blk("sciAesEncrypt", "d", "",
                "((new Object() { public String f() { try { javax.crypto.Cipher _c = javax.crypto.Cipher.getInstance(\"AES/ECB/PKCS5Padding\"); _c.init(javax.crypto.Cipher.ENCRYPT_MODE, new javax.crypto.spec.SecretKeySpec(\"0123456789abcdef\".getBytes(), \"AES\")); return android.util.Base64.encodeToString(_c.doFinal(%1$s.getBytes()), android.util.Base64.NO_WRAP); } catch (Throwable _t) { return \"\"; } } }).f())",
                "aes encrypt %1$s"));

        // 🔐 AES: расшифровать текст
        list.add(blk("sciAesDecrypt", "d", "",
                "((new Object() { public String f() { try { javax.crypto.Cipher _c = javax.crypto.Cipher.getInstance(\"AES/ECB/PKCS5Padding\"); _c.init(javax.crypto.Cipher.DECRYPT_MODE, new javax.crypto.spec.SecretKeySpec(\"0123456789abcdef\".getBytes(), \"AES\")); return new String(_c.doFinal(android.util.Base64.decode(%1$s, android.util.Base64.NO_WRAP))); } catch (Throwable _t) { return \"\"; } } }).f())",
                "aes decrypt %1$s"));

        // 📦 ZIP: распаковать архив
        list.add(blk("sciUnzip", " ", "",
                "((new Object() { public void f() { try { java.util.zip.ZipInputStream _z = new java.util.zip.ZipInputStream(new java.io.FileInputStream(%1$s)); java.util.zip.ZipEntry _e; while ((_e = _z.getNextEntry()) != null) { java.io.File _o = new java.io.File(%2$s, _e.getName()); if (_e.isDirectory()) _o.mkdirs(); else { new java.io.File(_o.getParent()).mkdirs(); java.io.FileOutputStream _os = new java.io.FileOutputStream(_o); byte[] _b = new byte[4096]; int _len; while ((_len = _z.read(_b)) > 0) _os.write(_b, 0, _len); _os.close(); } } _z.close(); } catch (Throwable _t) {} } }).f());",
                "unzip %1$s to %2$s"));

        // 📦 ZIP: создать архив
        list.add(blk("sciZip", " ", "",
                "((new Object() { public void f() { try { java.util.zip.ZipOutputStream _z = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(%2$s)); java.io.File[] _fs = new java.io.File(%1$s).listFiles(); if (_fs != null) { for (java.io.File _file : _fs) { java.io.FileInputStream _is = new java.io.FileInputStream(_file); _z.putNextEntry(new java.util.zip.ZipEntry(_file.getName())); byte[] _b = new byte[4096]; int _len; while ((_len = _is.read(_b)) > 0) _z.write(_b, 0, _len); _is.close(); _z.closeEntry(); } } _z.close(); } catch (Throwable _t) {} } }).f());",
                "zip folder %1$s to %2$s"));

        // 📶 Network: тип подключения (WiFi / Mobile / None)
        list.add(blk("sciNetworkType", "d", "",
                "((new Object() { public String f() { try { android.net.ConnectivityManager _cm = (android.net.ConnectivityManager) getSystemService(android.content.Context.CONNECTIVITY_SERVICE); android.net.NetworkInfo _ni = _cm.getActiveNetworkInfo(); if (_ni == null || !_ni.isConnected()) return \"None\"; return _ni.getType() == android.net.ConnectivityManager.TYPE_WIFI ? \"WiFi\" : \"Mobile\"; } catch (Throwable _t) { return \"None\"; } } }).f())",
                "network type"));

        // ☀️ Screen: установить яркость (0-255)
        list.add(blk("sciSetBrightness", " ", "",
                "((new Object() { public void f() { try { android.view.WindowManager.LayoutParams _p = %2$s.getWindow().getAttributes(); _p.screenBrightness = (float)((int)(%1$s)) / 255.0f; %2$s.getWindow().setAttributes(_p); } catch (Throwable _t) {} } }).f());",
                "set brightness %1$d from %m.activity"));

        // 🔊 Volume: установить громкость медиа (0-100)
        list.add(blk("sciSetVolume", " ", "",
                "((new Object() { public void f() { try { android.media.AudioManager _am = (android.media.AudioManager) %2$s.getSystemService(android.content.Context.AUDIO_SERVICE); int _max = _am.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC); _am.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, (int)(%1$s) * _max / 100, 0); } catch (Throwable _t) {} } }).f());",
                "set volume %1$d%% from %m.context"));

        // 📦 Archive: открыть архив (7z, rar, zip) через другое приложение
        list.add(blk("sciOpenArchive", " ", "",
                "try { android.content.Intent _i = new android.content.Intent(android.content.Intent.ACTION_VIEW); _i.setDataAndType(android.net.Uri.fromFile(new java.io.File(%1$s)), \"*/*\"); _i.addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION); %2$s.startActivity(android.content.Intent.createChooser(_i, \"Open archive\")); } catch (Throwable _t) {}",
                "open archive %1$s from %m.context"));
    }
}