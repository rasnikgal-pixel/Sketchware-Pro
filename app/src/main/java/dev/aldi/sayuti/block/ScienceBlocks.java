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

        // 🖼️ Image: получить ширину изображения
        list.add(blk("sciImageWidth", "d", "",
                "((new Object() { public double f() { try { android.graphics.BitmapFactory.Options _o = new android.graphics.BitmapFactory.Options(); _o.inJustDecodeBounds = true; android.graphics.BitmapFactory.decodeFile(%1$s, _o); return (double) _o.outWidth; } catch (Throwable _t) { return 0.0; } } }).f())",
                "image width of %1$s"));

        // 🖼️ Image: получить высоту изображения
        list.add(blk("sciImageHeight", "d", "",
                "((new Object() { public double f() { try { android.graphics.BitmapFactory.Options _o = new android.graphics.BitmapFactory.Options(); _o.inJustDecodeBounds = true; android.graphics.BitmapFactory.decodeFile(%1$s, _o); return (double) _o.outHeight; } catch (Throwable _t) { return 0.0; } } }).f())",
                "image height of %1$s"));

        // 🖼️ Image: сохранить в галерею
        list.add(blk("sciSaveToGallery", " ", "",
                "((new Object() { public void f() { try { android.graphics.Bitmap _b = android.graphics.BitmapFactory.decodeFile(%1$s); java.io.File _out = new java.io.File(android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_PICTURES), %2$s); java.io.FileOutputStream _os = new java.io.FileOutputStream(_out); _b.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, _os); _os.flush(); _os.close(); android.media.MediaScannerConnection.scanFile(%3$s, new String[]{_out.getAbsolutePath()}, null, null); } catch (Throwable _t) {} } }).f());",
                "save image %1$s to gallery as %2$s from %m.context"));

        // 🎵 Audio: воспроизвести звук
        list.add(blk("sciPlaySound", " ", "",
                "((new Object() { public void f() { try { android.media.MediaPlayer _mp = new android.media.MediaPlayer(); _mp.setDataSource(%1$s); _mp.prepare(); _mp.start(); } catch (Throwable _t) {} } }).f());",
                "play sound %1$s"));

        // 📞 Contacts: получить имя по номеру
        list.add(blk("sciGetContactName", "d", "",
                "((new Object() { public String f() { try { android.net.Uri _u = android.net.Uri.withAppendedPath(android.provider.ContactsContract.PhoneLookup.CONTENT_FILTER_URI, android.net.Uri.encode(%1$s)); android.database.Cursor _c = %2$s.getContentResolver().query(_u, new String[]{android.provider.ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null); if (_c != null && _c.moveToFirst()) { String _n = _c.getString(0); _c.close(); return _n; } return %1$s; } catch (Throwable _t) { return %1$s; } } }).f())",
                "contact name for %1$s from %m.context"));

        // 📅 Date: добавить дни к дате (dd.MM.yyyy)
        list.add(blk("sciAddDays", "d", "",
                "((new Object() { public String f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); _c.add(java.util.Calendar.DAY_OF_MONTH, (int)(%2$s)); return _fmt.format(_c.getTime()); } catch (Throwable _t) { return %1$s; } } }).f())",
                "add %2$d days to %1$s"));

        // 📅 Date: день недели (1-7)
        list.add(blk("sciDayOfWeek", "d", "",
                "((new Object() { public double f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); return (double) _c.get(java.util.Calendar.DAY_OF_WEEK); } catch (Throwable _t) { return 0.0; } } }).f())",
                "day of week for %1$s"));

        // 🔐 AES: зашифровать с ключом
        list.add(blk("sciAesEncryptKey", "d", "",
                "((new Object() { public String f() { try { javax.crypto.Cipher _c = javax.crypto.Cipher.getInstance(\"AES/ECB/PKCS5Padding\"); _c.init(javax.crypto.Cipher.ENCRYPT_MODE, new javax.crypto.spec.SecretKeySpec(%2$s.getBytes(), \"AES\")); return android.util.Base64.encodeToString(_c.doFinal(%1$s.getBytes()), android.util.Base64.NO_WRAP); } catch (Throwable _t) { return \"\"; } } }).f())",
                "aes encrypt %1$s with key %2$s"));

        // 🔐 AES: расшифровать с ключом
        list.add(blk("sciAesDecryptKey", "d", "",
                "((new Object() { public String f() { try { javax.crypto.Cipher _c = javax.crypto.Cipher.getInstance(\"AES/ECB/PKCS5Padding\"); _c.init(javax.crypto.Cipher.DECRYPT_MODE, new javax.crypto.spec.SecretKeySpec(%2$s.getBytes(), \"AES\")); return new String(_c.doFinal(android.util.Base64.decode(%1$s, android.util.Base64.NO_WRAP))); } catch (Throwable _t) { return \"\"; } } }).f())",
                "aes decrypt %1$s with key %2$s"));

        // 🎨 UI: установить цвет фона
        list.add(blk("sciSetBackgroundColor", " ", "",
                "%1$s.setBackgroundColor((int)(%2$s));",
                "set background color %2$d on %1$s"));

        // 🎨 UI: установить отступы (padding)
        list.add(blk("sciSetPadding", " ", "",
                "%1$s.setPadding((int)(%2$s), (int)(%3$s), (int)(%4$s), (int)(%5$s));",
                "set padding %1$s left %2$d top %3$d right %4$d bottom %5$d"));

        // 🎨 UI: установить поворот
        list.add(blk("sciSetRotation", " ", "",
                "%1$s.setRotation((float)(%2$s));",
                "set rotation %2$s on %1$s"));

        // 🎨 UI: установить масштаб по X
        list.add(blk("sciSetScaleX", " ", "",
                "%1$s.setScaleX((float)(%2$s));",
                "set scaleX %2$s on %1$s"));

        // 🎨 UI: установить масштаб по Y
        list.add(blk("sciSetScaleY", " ", "",
                "%1$s.setScaleY((float)(%2$s));",
                "set scaleY %2$s on %1$s"));

        // 🎨 UI: установить тень (elevation)
        list.add(blk("sciSetElevation", " ", "",
                "%1$s.setElevation((float)(%2$s));",
                "set elevation %2$s on %1$s"));

        // 🎨 UI: включить/выключить виджет
        list.add(blk("sciSetEnabled", " ", "",
                "%1$s.setEnabled(%2$s);",
                "set enabled %2$b on %1$s"));

        // 🎨 UI: сделать кликабельным
        list.add(blk("sciSetClickable", " ", "",
                "%1$s.setClickable(%2$s);",
                "set clickable %2$b on %1$s"));

        // 🎨 UI: сделать фокусируемым
        list.add(blk("sciSetFocusable", " ", "",
                "%1$s.setFocusable(%2$s);",
                "set focusable %2$b on %1$s"));

        // 🎨 UI: установить подсказку (для EditText)
        list.add(blk("sciSetHintText", " ", "",
                "%1$s.setHint(%2$s);",
                "set hint %2$s on %1$s"));

        // 📋 List: получить подсписок (с index по index)
        list.add(blk("sciListSubList", "d", "",
                "new java.util.ArrayList(%1$s.subList(Math.min((int)(%2$s), %1$s.size()), Math.min((int)(%3$s), %1$s.size())))",
                "sub list %1$s from %2$d to %3$d"));

        // 📋 List: объединить два списка
        list.add(blk("sciListConcat", " ", "",
                "((new Object() { public void f() { try { %3$s.clear(); %3$s.addAll(%1$s); %3$s.addAll(%2$s); } catch (Throwable _t) {} } }).f());",
                "concat %1$s and %2$s into %3$s"));

        // 📋 List: заполнить список значением
        list.add(blk("sciListFill", " ", "",
                "((new Object() { public void f() { try { java.util.Collections.fill(%1$s, %2$s); } catch (Throwable _t) {} } }).f());",
                "fill %1$s with %2$s"));

        // 📋 List: бинарный поиск (предполагает сортировку)
        list.add(blk("sciListBinarySearch", "d", "",
                "(double) java.util.Collections.binarySearch(%1$s, %2$s)",
                "binary search %2$s in %1$s"));

        // 📋 List: максимальный элемент
        list.add(blk("sciListMax", "d", "",
                "((new Object() { public String f() { try { return %1$s.isEmpty() ? \"\" : String.valueOf(java.util.Collections.max((java.util.Collection) %1$s)); } catch (Throwable _t) { return \"\"; } } }).f())",
                "max of %1$s"));

        // 📋 List: минимальный элемент
        list.add(blk("sciListMin", "d", "",
                "((new Object() { public String f() { try { return %1$s.isEmpty() ? \"\" : String.valueOf(java.util.Collections.min((java.util.Collection) %1$s)); } catch (Throwable _t) { return \"\"; } } }).f())",
                "min of %1$s"));

        // 📋 List: фильтр (только элементы, содержащие подстроку)
        list.add(blk("sciListFilterContains", " ", "",
                "((new Object() { public void f() { try { java.util.ArrayList _tmp = new java.util.ArrayList(); for (Object _o : %1$s) { if (_o != null && _o.toString().contains(%2$s)) _tmp.add(_o); } %1$s.clear(); %1$s.addAll(_tmp); } catch (Throwable _t) {} } }).f());",
                "filter %1$s contains %2$s"));

        // 📋 List: удалить элемент по значению
        list.add(blk("sciListRemoveValue", " ", "",
                "((new Object() { public void f() { try { %1$s.remove(%2$s); } catch (Throwable _t) {} } }).f());",
                "remove %2$s from %1$s"));

        // 📋 List: удалить элемент по индексу
        list.add(blk("sciListRemoveIndex", " ", "",
                "((new Object() { public void f() { try { int _i = (int)(%2$s); if (_i >= 0 && _i < %1$s.size()) %1$s.remove(_i); } catch (Throwable _t) {} } }).f());",
                "remove index %2$d from %1$s"));

        // 📋 List: вставить элемент по индексу
        list.add(blk("sciListInsert", " ", "",
                "((new Object() { public void f() { try { int _i = (int)(%2$s); if (_i >= 0 && _i <= %1$s.size()) %1$s.add(_i, %3$s); } catch (Throwable _t) {} } }).f());",
                "insert %3$s at %2$d in %1$s"));

        // 📋 List: заменить элемент по индексу
        list.add(blk("sciListSet", " ", "",
                "((new Object() { public void f() { try { int _i = (int)(%2$s); if (_i >= 0 && _i < %1$s.size()) %1$s.set(_i, %3$s); } catch (Throwable _t) {} } }).f());",
                "set %3$s at %2$d in %1$s"));

        // 📋 List: получить элемент по индексу
        list.add(blk("sciListGet", "d", "",
                "((new Object() { public String f() { try { int _i = (int)(%2$s); return (_i >= 0 && _i < %1$s.size()) ? String.valueOf(%1$s.get(_i)) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "get %2$d from %1$s"));

        // 📋 List: размер списка (число)
        list.add(blk("sciListSizeNum", "d", "",
                "(double)(%1$s.size())",
                "size of %1$s"));

        // 📋 List: очистить список
        list.add(blk("sciListClear", " ", "",
                "%1$s.clear();",
                "clear %1$s"));

        // 📋 List: проверить, содержит ли список элемент
        list.add(blk("sciListHas", "b", "",
                "%1$s.contains(%2$s)",
                "%1$s contains %2$s"));

        // 📋 List: преобразовать в массив строк
        list.add(blk("sciListToArray", "d", "",
                "%1$s.toArray()",
                "to array from %1$s"));

        // 📋 List: удалить все вхождения значения
        list.add(blk("sciListRemoveAll", " ", "",
                "((new Object() { public void f() { try { java.util.ArrayList _tmp = new java.util.ArrayList(); for (Object _o : %1$s) { if (_o == null || !_o.equals(%2$s)) _tmp.add(_o); } %1$s.clear(); %1$s.addAll(_tmp); } catch (Throwable _t) {} } }).f());",
                "remove all %2$s from %1$s"));

        // 📋 List: оставить только уникальные (по строке)
        list.add(blk("sciListDistinctStr", " ", "",
                "((new Object() { public void f() { try { java.util.LinkedHashSet _s = new java.util.LinkedHashSet(); for (Object _o : %1$s) _s.add(String.valueOf(_o)); %1$s.clear(); %1$s.addAll(_s); } catch (Throwable _t) {} } }).f());",
                "distinct strings in %1$s"));

        // 📋 List: соединить в строку с разделителем (для чисел)
        list.add(blk("sciListJoinNum", "d", "",
                "((new Object() { public String f() { try { StringBuilder _sb = new StringBuilder(); for (int _i = 0; _i < %1$s.size(); _i++) { if (_i > 0) _sb.append(%2$s); _sb.append(%1$s.get(_i)); } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "join numbers %1$s with %2$s"));

        // 📋 List: найти первый индекс с подстрокой
        list.add(blk("sciListFindContains", "d", "",
                "((new Object() { public double f() { try { for (int _i = 0; _i < %1$s.size(); _i++) { if (String.valueOf(%1$s.get(_i)).contains(%2$s)) return (double)_i; } return -1.0; } catch (Throwable _t) { return -1.0; } } }).f())",
                "find %2$s in %1$s"));

        // 📋 List: создать список из строки (разделитель)
        list.add(blk("sciListFromString", " ", "",
                "((new Object() { public void f() { try { %2$s.clear(); for (String _s : %1$s.split(%3$s)) %2$s.add(_s); } catch (Throwable _t) {} } }).f());",
                "split %1$s by %3$s into %2$s"));

        // 📋 List: обратная сортировка
        list.add(blk("sciListSortDesc", " ", "",
                "((new Object() { public void f() { try { java.util.Collections.sort(%1$s); java.util.Collections.reverse(%1$s); } catch (Throwable _t) {} } }).f());",
                "sort descending %1$s"));

        // 📋 List: первый индекс (поиск)
        list.add(blk("sciListFirstIndex", "d", "",
                "(double)(%1$s.indexOf(%2$s))",
                "first index of %2$s in %1$s"));

        // 📋 List: последний индекс
        list.add(blk("sciListLastIndex", "d", "",
                "(double)(%1$s.lastIndexOf(%2$s))",
                "last index of %2$s in %1$s"));

        // 📋 List: проверить, пустой ли список
        list.add(blk("sciListIsEmptyList", "b", "",
                "%1$s.isEmpty()",
                "%1$s is empty"));

        // 📋 List: преобразовать в строку
        list.add(blk("sciListToString", "d", "",
                "%1$s.toString()",
                "%1$s to string"));

        // 📁 File: переименовать
        list.add(blk("sciFileRename", " ", "",
                "((new Object() { public void f() { try { new java.io.File(%1$s).renameTo(new java.io.File(%2$s)); } catch (Throwable _t) {} } }).f());",
                "rename file %1$s to %2$s"));

        // 📁 File: полный путь
        list.add(blk("sciFileAbsolutePath", "d", "",
                "new java.io.File(%1$s).getAbsolutePath()",
                "absolute path of %1$s"));

        // 📁 File: родительская папка
        list.add(blk("sciFileParent", "d", "",
                "((new Object() { public String f() { try { return new java.io.File(%1$s).getParent(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "parent of %1$s"));

        // 📁 File: рекурсивный список файлов
        list.add(blk("sciFileListRecursive", " ", "",
                "((new Object() { public void f() { try { java.util.ArrayList _r = new java.util.ArrayList(); java.io.File[] _fs = new java.io.File(%1$s).listFiles(); if (_fs != null) { for (java.io.File _x : _fs) { if (_x.isDirectory()) { java.io.File[] _sub = _x.listFiles(); if (_sub != null) { for (java.io.File _y : _sub) _r.add(_y.getAbsolutePath()); } } else { _r.add(_x.getAbsolutePath()); } } } %2$s.clear(); %2$s.addAll(_r); } catch (Throwable _t) {} } }).f());",
                "list files recursively from %1$s into %2$s"));

        // 📁 File: удалить папку со всем содержимым
        list.add(blk("sciFileDeleteRecursive", " ", "",
                "((new Object() { public void f() { try { java.io.File _d = new java.io.File(%1$s); if (_d.isDirectory()) { java.io.File[] _fs = _d.listFiles(); if (_fs != null) { for (java.io.File _x : _fs) _x.delete(); } } _d.delete(); } catch (Throwable _t) {} } }).f());",
                "delete folder %1$s recursively"));

        // 📁 File: размер в КБ
        list.add(blk("sciFileSizeKB", "d", "",
                "(double)(new java.io.File(%1$s).length() / 1024)",
                "size KB of %1$s"));

        // 📁 File: размер в МБ
        list.add(blk("sciFileSizeMB", "d", "",
                "(double)(new java.io.File(%1$s).length() / (1024 * 1024))",
                "size MB of %1$s"));

        // 📁 File: скрытый ли файл
        list.add(blk("sciFileIsHidden", "b", "",
                "new java.io.File(%1$s).isHidden()",
                "file %1$s is hidden"));

        // 📁 File: дата последнего изменения (millis)
        list.add(blk("sciFileLastModified", "d", "",
                "(double)(new java.io.File(%1$s).lastModified())",
                "last modified of %1$s"));

        // 📁 File: создать пустой файл
        list.add(blk("sciFileCreateNew", "b", "",
                "((new Object() { public boolean f() { try { return new java.io.File(%1$s).createNewFile(); } catch (Throwable _t) { return false; } } }).f())",
                "create new file %1$s"));

        // 📁 File: MIME-тип по расширению
        list.add(blk("sciFileMimeType", "d", "",
                "((new Object() { public String f() { try { String _n = new java.io.File(%1$s).getName(); String _e = _n.substring(_n.lastIndexOf('.') + 1).toLowerCase(); if (_e.equals(\"jpg\") || _e.equals(\"jpeg\")) return \"image/jpeg\"; if (_e.equals(\"png\")) return \"image/png\"; if (_e.equals(\"pdf\")) return \"application/pdf\"; if (_e.equals(\"txt\")) return \"text/plain\"; if (_e.equals(\"mp3\")) return \"audio/mpeg\"; if (_e.equals(\"mp4\")) return \"video/mp4\"; return \"application/octet-stream\"; } catch (Throwable _t) { return \"application/octet-stream\"; } } }).f())",
                "mime type of %1$s"));

        // 📁 File: общее место на диске (МБ)
        list.add(blk("sciFileTotalSpace", "d", "",
                "(double)(new java.io.File(%1$s).getTotalSpace() / (1024 * 1024))",
                "total space MB of %1$s"));

        // 📁 File: свободное место на диске (МБ)
        list.add(blk("sciFileFreeSpace", "d", "",
                "(double)(new java.io.File(%1$s).getFreeSpace() / (1024 * 1024))",
                "free space MB of %1$s"));

        // 📁 File: доступное место (МБ)
        list.add(blk("sciFileUsableSpace", "d", "",
                "(double)(new java.io.File(%1$s).getUsableSpace() / (1024 * 1024))",
                "usable space MB of %1$s"));

        // 📁 File: является ли файлом (не папкой)
        list.add(blk("sciFileIsFile", "b", "",
                "new java.io.File(%1$s).isFile()",
                "file %1$s is a file"));

        // 📁 File: копировать рекурсивно
        list.add(blk("sciFileCopyRecursive", " ", "",
                "((new Object() { public void f() { try { java.io.File _src = new java.io.File(%1$s); java.io.File _dst = new java.io.File(%2$s); if (_src.isDirectory()) { _dst.mkdirs(); java.io.File[] _fs = _src.listFiles(); if (_fs != null) { for (java.io.File _x : _fs) { java.io.File _to = new java.io.File(_dst, _x.getName()); java.io.FileInputStream _is = new java.io.FileInputStream(_x); java.io.FileOutputStream _os = new java.io.FileOutputStream(_to); byte[] _b = new byte[4096]; int _len; while ((_len = _is.read(_b)) > 0) _os.write(_b, 0, _len); _is.close(); _os.close(); } } } } catch (Throwable _t) {} } }).f());",
                "copy folder %1$s to %2$s recursively"));

        // 📁 File: переместить рекурсивно
        list.add(blk("sciFileMoveRecursive", " ", "",
                "((new Object() { public void f() { try { java.io.File _src = new java.io.File(%1$s); java.io.File _dst = new java.io.File(%2$s); _src.renameTo(_dst); } catch (Throwable _t) {} } }).f());",
                "move folder %1$s to %2$s"));

        // 📁 File: получить расширение файла
        list.add(blk("sciFileExtOnly", "d", "",
                "((new Object() { public String f() { try { String _n = new java.io.File(%1$s).getName(); int _i = _n.lastIndexOf('.'); return _i > 0 ? _n.substring(_i + 1) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "extension of %1$s"));

        // 📁 File: прочитать первые N символов
        list.add(blk("sciFileReadFirst", "d", "",
                "((new Object() { public String f() { try { java.io.RandomAccessFile _r = new java.io.RandomAccessFile(%1$s, \"r\"); byte[] _b = new byte[(int)(%2$s)]; _r.readFully(_b); _r.close(); return new String(_b); } catch (Throwable _t) { return \"\"; } } }).f())",
                "read first %2$d chars of %1$s"));

        // 📁 File: записать в конец файла
        list.add(blk("sciFileAppendText", " ", "",
                "((new Object() { public void f() { try { java.io.FileWriter _w = new java.io.FileWriter(%1$s, true); _w.append(%2$s); _w.close(); } catch (Throwable _t) {} } }).f());",
                "append %2$s to file %1$s"));

        // 🌐 Network: скачать файл по URL
        list.add(blk("sciDownloadFile", " ", "",
                "((new Object() { public void f() { try { java.io.InputStream _is = new java.net.URL(%1$s).openStream(); java.io.FileOutputStream _os = new java.io.FileOutputStream(%2$s); byte[] _b = new byte[4096]; int _len; while ((_len = _is.read(_b)) > 0) _os.write(_b, 0, _len); _is.close(); _os.close(); } catch (Throwable _t) {} } }).f());",
                "download %1$s to %2$s"));

        // 🌐 Network: URL-кодировать
        list.add(blk("sciUrlEncode", "d", "",
                "((new Object() { public String f() { try { return java.net.URLEncoder.encode(%1$s, \"UTF-8\"); } catch (Throwable _t) { return %1$s; } } }).f())",
                "url encode %1$s"));

        // 🌐 Network: URL-декодировать
        list.add(blk("sciUrlDecode", "d", "",
                "((new Object() { public String f() { try { return java.net.URLDecoder.decode(%1$s, \"UTF-8\"); } catch (Throwable _t) { return %1$s; } } }).f())",
                "url decode %1$s"));

        // 🌐 Network: получить элемент JSON-массива
        list.add(blk("sciJsonArrayGet", "d", "",
                "((new Object() { public String f() { try { org.json.JSONArray _a = new org.json.JSONArray(%1$s); int _i = (int)(%2$s); return (_i >= 0 && _i < _a.length()) ? _a.getString(_i) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "json array %1$s get %2$d"));

        // 🌐 Network: JSON-массив → размер
        list.add(blk("sciJsonArrayLength", "d", "",
                "((new Object() { public double f() { try { return (double) new org.json.JSONArray(%1$s).length(); } catch (Throwable _t) { return 0.0; } } }).f())",
                "json array %1$s length"));

        // 🌐 Network: JSON-объект → число
        list.add(blk("sciJsonGetInt", "d", "",
                "((new Object() { public double f() { try { return new org.json.JSONObject(%1$s).optDouble(%2$s, 0.0); } catch (Throwable _t) { return 0.0; } } }).f())",
                "json get int %2$s from %1$s"));

        // 🌐 Network: JSON-объект → boolean
        list.add(blk("sciJsonGetBool", "b", "",
                "((new Object() { public boolean f() { try { return new org.json.JSONObject(%1$s).optBoolean(%2$s, false); } catch (Throwable _t) { return false; } } }).f())",
                "json get bool %2$s from %1$s"));

        // 🌐 Network: HTTP PUT
        list.add(blk("sciHttpPut", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"PUT\"); _c.setDoOutput(true); java.io.OutputStream _os = _c.getOutputStream(); _os.write(%2$s.getBytes(\"UTF-8\")); _os.flush(); _os.close(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http put %1$s data %2$s"));

        // 🌐 Network: HTTP DELETE
        list.add(blk("sciHttpDelete", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"DELETE\"); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http delete %1$s"));

        // 🌐 Network: код ответа HTTP
        list.add(blk("sciHttpGetResponseCode", "d", "",
                "((new Object() { public double f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"HEAD\"); return (double) _c.getResponseCode(); } catch (Throwable _t) { return 0.0; } } }).f())",
                "http response code for %1$s"));

        // 🌐 Network: валидный ли URL
        list.add(blk("sciIsUrlValid", "b", "",
                "((new Object() { public boolean f() { try { new java.net.URL(%1$s); return true; } catch (Throwable _t) { return false; } } }).f())",
                "url %1$s is valid"));

        // 🌐 Network: домен из URL
        list.add(blk("sciGetDomain", "d", "",
                "((new Object() { public String f() { try { return new java.net.URL(%1$s).getHost(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "domain of %1$s"));

        // 🌐 Network: параметр из URL
        list.add(blk("sciGetUrlParam", "d", "",
                "((new Object() { public String f() { try { String _q = new java.net.URL(%1$s).getQuery(); if (_q != null) { for (String _p : _q.split(\"&\")) { String[] _kv = _p.split(\"=\"); if (_kv.length == 2 && _kv[0].equals(%2$s)) return java.net.URLDecoder.decode(_kv[1], \"UTF-8\"); } } return \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "url param %2$s from %1$s"));

        // 🌐 Network: собрать URL с параметрами
        list.add(blk("sciBuildUrl", "d", "",
                "((new Object() { public String f() { try { if (!%1$s.contains(\"?\")) return %1$s + \"?\" + java.net.URLEncoder.encode(%2$s, \"UTF-8\") + \"=\" + java.net.URLEncoder.encode(%3$s, \"UTF-8\"); return %1$s + \"&\" + java.net.URLEncoder.encode(%2$s, \"UTF-8\") + \"=\" + java.net.URLEncoder.encode(%3$s, \"UTF-8\"); } catch (Throwable _t) { return %1$s; } } }).f())",
                "build url %1$s with %2$s=%3$s"));

        // 🌐 Network: POST JSON
        list.add(blk("sciHttpPostJson", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"POST\"); _c.setRequestProperty(\"Content-Type\", \"application/json\"); _c.setDoOutput(true); java.io.OutputStream _os = _c.getOutputStream(); _os.write(%2$s.getBytes(\"UTF-8\")); _os.flush(); _os.close(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http post json to %1$s data %2$s"));

        // 🌐 Network: GET с таймаутом
        list.add(blk("sciHttpGetTimeout", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setConnectTimeout((int)(%2$s)); _c.setReadTimeout((int)(%2$s)); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http get %1$s timeout %2$d ms"));

        // 🌐 Network: IP-адрес устройства
        list.add(blk("sciGetIpAddress", "d", "",
                "((new Object() { public String f() { try { java.util.Enumeration _e = java.net.NetworkInterface.getNetworkInterfaces(); while (_e.hasMoreElements()) { java.net.NetworkInterface _n = (java.net.NetworkInterface) _e.nextElement(); java.util.Enumeration _a = _n.getInetAddresses(); while (_a.hasMoreElements()) { java.net.InetAddress _i = (java.net.InetAddress) _a.nextElement(); if (!_i.isLoopbackAddress() && _i instanceof java.net.Inet4Address) return _i.getHostAddress(); } } return \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "device ip address"));

        // 🌐 Network: пинг хоста
        list.add(blk("sciPingHost", "d", "",
                "((new Object() { public double f() { try { java.net.InetAddress _a = java.net.InetAddress.getByName(%1$s); long _s = System.currentTimeMillis(); _a.isReachable(3000); return (double)(System.currentTimeMillis() - _s); } catch (Throwable _t) { return -1.0; } } }).f())",
                "ping %1$s"));

        // 🎵 Media: воспроизвести звук (ресурс)
        list.add(blk("sciPlaySoundRes", " ", "",
                "android.media.MediaPlayer.create(%2$s, %1$s).start();",
                "play sound resource %1$d from %m.context"));

        // 🎵 Media: остановить все звуки
        list.add(blk("sciStopAllSounds", " ", "",
                "((android.media.AudioManager) %1$s.getSystemService(android.content.Context.AUDIO_SERVICE)).abandonAudioFocus(null);",
                "stop all sounds from %m.context"));

        // 🎵 Media: длительность аудио (мс)
        list.add(blk("sciAudioDuration", "d", "",
                "((new Object() { public double f() { try { android.media.MediaPlayer _mp = new android.media.MediaPlayer(); _mp.setDataSource(%1$s); _mp.prepare(); double _d = (double) _mp.getDuration(); _mp.release(); return _d; } catch (Throwable _t) { return 0.0; } } }).f())",
                "duration of audio %1$s"));

        // 📷 Camera: открыть камеру
        list.add(blk("sciOpenCamera", " ", "",
                "%1$s.startActivity(new android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE));",
                "open camera from %m.context"));

        // 📷 Camera: сделать фото (Intent)
        list.add(blk("sciTakePhoto", " ", "",
                "%2$s.startActivityForResult(new android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), (int)(%1$s));",
                "take photo request %1$d from %m.activity"));

        // 📍 Location: получить последнюю известную локацию (широта)
        list.add(blk("sciLocationLat", "d", "",
                "((new Object() { public double f() { try { android.location.LocationManager _lm = (android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE); android.location.Location _l = _lm.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER); return _l != null ? _l.getLatitude() : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "location latitude from %m.context"));

        // 📍 Location: получить последнюю известную локацию (долгота)
        list.add(blk("sciLocationLon", "d", "",
                "((new Object() { public double f() { try { android.location.LocationManager _lm = (android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE); android.location.Location _l = _lm.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER); return _l != null ? _l.getLongitude() : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "location longitude from %m.context"));

        // 📱 Sensor: данные акселерометра X
        list.add(blk("sciAccelX", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "accelerometer X from %m.context"));

        // 📳 Vibrate: вибрация с паттерном
        list.add(blk("sciVibratePattern", " ", "",
                "((android.os.Vibrator) %2$s.getSystemService(android.content.Context.VIBRATOR_SERVICE)).vibrate(new long[]{(long)(%1$s), 100, (long)(%1$s)}, -1);",
                "vibrate pattern %1$d from %m.context"));

        // 🔦 Flashlight: включить фонарик
        list.add(blk("sciFlashlightOn", " ", "",
                "((new Object() { public void f() { try { android.hardware.camera2.CameraManager _cm = (android.hardware.camera2.CameraManager) %1$s.getSystemService(android.content.Context.CAMERA_SERVICE); _cm.setTorchMode(_cm.getCameraIdList()[0], true); } catch (Throwable _t) {} } }).f());",
                "flashlight on from %m.context"));

        // 🔦 Flashlight: выключить фонарик
        list.add(blk("sciFlashlightOff", " ", "",
                "((new Object() { public void f() { try { android.hardware.camera2.CameraManager _cm = (android.hardware.camera2.CameraManager) %1$s.getSystemService(android.content.Context.CAMERA_SERVICE); _cm.setTorchMode(_cm.getCameraIdList()[0], false); } catch (Throwable _t) {} } }).f());",
                "flashlight off from %m.context"));

        // ☀️ Screen: не выключать экран
        list.add(blk("sciKeepScreenOn", " ", "",
                "%1$s.getWindow().addFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);",
                "keep screen on from %m.activity"));

        // ☀️ Screen: можно выключать экран
        list.add(blk("sciAllowScreenOff", " ", "",
                "%1$s.getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);",
                "allow screen off from %m.activity"));

        // 🎵 Audio: громкость музыки (0-100)
        list.add(blk("sciGetVolume", "d", "",
                "((new Object() { public double f() { try { android.media.AudioManager _am = (android.media.AudioManager) %1$s.getSystemService(android.content.Context.AUDIO_SERVICE); int _max = _am.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC); int _cur = _am.getStreamVolume(android.media.AudioManager.STREAM_MUSIC); return (double)(_cur * 100 / _max); } catch (Throwable _t) { return 0.0; } } }).f())",
                "get volume from %m.context"));

        // 🔇 Audio: беззвучный режим
        list.add(blk("sciSetSilent", " ", "",
                "((android.media.AudioManager) %1$s.getSystemService(android.content.Context.AUDIO_SERVICE)).setRingerMode(android.media.AudioManager.RINGER_MODE_SILENT);",
                "set silent mode from %m.context"));

        // 🔊 Audio: обычный режим
        list.add(blk("sciSetNormal", " ", "",
                "((android.media.AudioManager) %1$s.getSystemService(android.content.Context.AUDIO_SERVICE)).setRingerMode(android.media.AudioManager.RINGER_MODE_NORMAL);",
                "set normal mode from %m.context"));

        // 🎤 Audio: начать запись (требует разрешения)
        list.add(blk("sciStartRecording", " ", "",
                "((new Object() { public void f() { try { android.media.MediaRecorder _mr = new android.media.MediaRecorder(); _mr.setAudioSource(android.media.MediaRecorder.AudioSource.MIC); _mr.setOutputFormat(android.media.MediaRecorder.OutputFormat.THREE_GPP); _mr.setAudioEncoder(android.media.MediaRecorder.AudioEncoder.AMR_NB); _mr.setOutputFile(%1$s); _mr.prepare(); _mr.start(); } catch (Throwable _t) {} } }).f());",
                "start recording to %1$s"));

        // 🎤 Audio: остановить запись
        list.add(blk("sciStopRecording", " ", "",
                "((new Object() { public void f() { try { android.media.MediaRecorder _mr = new android.media.MediaRecorder(); _mr.stop(); _mr.release(); } catch (Throwable _t) {} } }).f());",
                "stop recording"));

        // 📞 Phone: получить оператора связи
        list.add(blk("sciGetCarrier", "d", "",
                "((new Object() { public String f() { try { android.telephony.TelephonyManager _tm = (android.telephony.TelephonyManager) %1$s.getSystemService(android.content.Context.TELEPHONY_SERVICE); return _tm.getNetworkOperatorName(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "carrier name from %m.context"));

        // 📞 Phone: тип сети (WiFi/Mobile)
        list.add(blk("sciGetNetworkClass", "d", "",
                "((new Object() { public String f() { try { android.telephony.TelephonyManager _tm = (android.telephony.TelephonyManager) %1$s.getSystemService(android.content.Context.TELEPHONY_SERVICE); int _t = _tm.getNetworkType(); return _t == 13 ? \"4G\" : _t == 3 ? \"3G\" : _t == 2 ? \"EDGE\" : \"Unknown\"; } catch (Throwable _t) { return \"Unknown\"; } } }).f())",
                "network class from %m.context"));

        // 📱 Device: серийный номер
        list.add(blk("sciDeviceSerial", "d", "",
                "android.os.Build.SERIAL",
                "device serial"));

        // 📱 Device: производитель
        list.add(blk("sciDeviceBrand", "d", "",
                "android.os.Build.BRAND",
                "device brand"));

        // 📱 Device: версия SDK (int)
        list.add(blk("sciSdkInt", "d", "",
                "(double) android.os.Build.VERSION.SDK_INT",
                "sdk int"));

        // 📱 Device: язык системы
        list.add(blk("sciSystemLanguage", "d", "",
                "java.util.Locale.getDefault().getLanguage()",
                "system language"));

        // 📱 Device: часовой пояс
        list.add(blk("sciSystemTimezone", "d", "",
                "java.util.TimeZone.getDefault().getID()",
                "system timezone"));

        // 📱 Device: разрешение экрана (ширина)
        list.add(blk("sciScreenWidth", "d", "",
                "((new Object() { public double f() { try { android.util.DisplayMetrics _dm = new android.util.DisplayMetrics(); %1$s.getWindowManager().getDefaultDisplay().getMetrics(_dm); return (double) _dm.widthPixels; } catch (Throwable _t) { return 0.0; } } }).f())",
                "screen width from %m.activity"));

        // 📱 Device: разрешение экрана (высота)
        list.add(blk("sciScreenHeight", "d", "",
                "((new Object() { public double f() { try { android.util.DisplayMetrics _dm = new android.util.DisplayMetrics(); %1$s.getWindowManager().getDefaultDisplay().getMetrics(_dm); return (double) _dm.heightPixels; } catch (Throwable _t) { return 0.0; } } }).f())",
                "screen height from %m.activity"));

        // 📱 Device: плотность экрана (dpi)
        list.add(blk("sciScreenDpi", "d", "",
                "((new Object() { public double f() { try { android.util.DisplayMetrics _dm = new android.util.DisplayMetrics(); %1$s.getWindowManager().getDefaultDisplay().getMetrics(_dm); return (double) _dm.densityDpi; } catch (Throwable _t) { return 0.0; } } }).f())",
                "screen dpi from %m.activity"));
    }
}