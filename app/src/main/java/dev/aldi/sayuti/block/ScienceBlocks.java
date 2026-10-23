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
                "Ohm: I = %s / %s"));

        list.add(blk("sciOhmU", "d", "",
                "(double)((double)(%1$s * %2$s))",
                "Ohm: U = %s * %s"));

        list.add(blk("sciOhmR", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "Ohm: R = %s / %s"));

        // Мощность
        list.add(blk("sciPowerUI", "d", "",
                "(double)((double)(%1$s * %2$s))",
                "Power: P = %s * %s"));

        list.add(blk("sciPowerI2R", "d", "",
                "(double)((double)(%1$s * %1$s * %2$s))",
                "Power: P = %s² * %s"));

        list.add(blk("sciPowerU2R", "d", "",
                "(double)((double)(%1$s * %1$s / %2$s))",
                "Power: P = %s² / %s"));

        // Реактивное сопротивление
        list.add(blk("sciXc", "d", "",
                "(double)((double)(1.0 / (2.0 * Math.PI * %1$s * %2$s)))",
                "Xc = 1 / (2π * %s * %s)"));

        list.add(blk("sciXl", "d", "",
                "(double)((double)(2.0 * Math.PI * %1$s * %2$s))",
                "Xl = 2π * %s * %s"));

        // Механика
        list.add(blk("sciSpeed", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "Mechanics: v = %s / %s"));

        list.add(blk("sciAccel", "d", "",
                "(double)((double)((%1$s - %2$s) / %3$s))",
                "Mechanics: a = (%s - %s) / %s"));

        list.add(blk("sciForce", "d", "",
                "(double)((double)(%1$s * %2$s))",
                "Mechanics: F = %s * %s"));

        // Химия
        list.add(blk("sciMolarMass", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "M = %s / %s"));

        list.add(blk("sciDensity", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "density ρ = %s / %s"));

        list.add(blk("sciPressure", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "pressure P = %s / %s"));

        list.add(blk("sciWork", "d", "",
                "(double)((double)(%1$s * %2$s))",
                "work A = %s * %s"));

        list.add(blk("sciEnergy", "d", "",
                "(double)((double)(%1$s * 9.8 * %2$s))",
                "energy E = %s * 9.8 * %s"));

        list.add(blk("sciMechPower", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "power N = %s / %s"));

        list.add(blk("sciGravity", "d", "",
                "(double)((double)(6.674e-11 * %1$s * %2$s / (%3$s * %3$s)))",
                "gravity F = G·%s·%s/%s²"));

        list.add(blk("sciConcentration", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "concentration C = %s / %s"));

        list.add(blk("sciSolutionMass", "d", "",
                "(double)((double)(%1$s + %2$s))",
                "solution mass = %s + %s"));

        list.add(blk("sciAmount", "d", "",
                "(double)((double)(%1$s / %2$s))",
                "amount n = %s / %s"));

        list.add(blk("sciMolarVolume", "d", "",
                "(double)((double)(%1$s * 22.4))",
                "molar volume V = %s * 22.4"));

        list.add(blk("sciVat", "d", "",
                "(double)((double)(%1$s * %2$s / 100.0))",
                "VAT = %s * %s / 100"));

        list.add(blk("sciVatIncluded", "d", "",
                "(double)((double)(%1$s * (1.0 + %2$s / 100.0)))",
                "sum with VAT = %s * (1 + %s/100)"));

        list.add(blk("sciVatExtract", "d", "",
                "(double)((double)(%1$s * %2$s / (100.0 + %2$s)))",
                "extract VAT = %s * %s / (100 + %s)"));

        list.add(blk("sciPercent", "d", "",
                "(double)((double)(%1$s * %2$s / 100.0))",
                "percent of number = %s * %s / 100"));

        list.add(blk("sciAddPercent", "d", "",
                "(double)((double)(%1$s * (1.0 + %2$s / 100.0)))",
                "number + percent = %s * (1 + %s/100)"));

        list.add(blk("sciSubPercent", "d", "",
                "(double)((double)(%1$s * (1.0 - %2$s / 100.0)))",
                "number - percent = %s * (1 - %s/100)"));

        list.add(blk("sciProfit", "d", "",
                "(double)((double)(%1$s - %2$s))",
                "profit = %s - %s"));

        list.add(blk("sciMargin", "d", "",
                "(double)((double)((%1$s - %2$s) / %1$s * 100.0))",
                "margin % = (%s - %s) / %s * 100"));

        list.add(blk("sciMarkup", "d", "",
                "(double)((double)((%1$s - %2$s) / %2$s * 100.0))",
                "markup % = (%s - %s) / %s * 100"));

        list.add(blk("sciCompound", "d", "",
                "(double)((double)(%1$s * Math.pow(1.0 + %2$s / 100.0, %3$s)))",
                "compound interest = %s * (1 + %s/100)^%s"));

        list.add(blk("sciAnnuity", "d", "",
                "(double)((double)(%1$s * (%2$s / 100.0) / (1.0 - Math.pow(1.0 + %2$s / 100.0, -%3$s))))",
                "annuity payment = %s * %s / (1 - (1+%s)^-%s)"));

        list.add(blk("sciUsn6", "d", "",
                "(double)((double)(%1$s * 6.0 / 100.0))",
                "USN 6% = %s * 6 / 100"));

        list.add(blk("sciUsn15", "d", "",
                "(double)((double)((%1$s - %2$s) * 15.0 / 100.0))",
                "USN 15% = (%s - %s) * 15 / 100"));

        // Validation: email
        list.add(blk("sciIsValidEmail", "b", "",
                "android.util.Patterns.EMAIL_ADDRESS.matcher(%1$s).matches()",
                "%s is valid email"));

        // Validation: пустая строка (с учётом пробелов)
        list.add(blk("sciIsEmptyString", "b", "",
                "%1$s.trim().isEmpty()",
                "%s is empty"));

        // Color: parse "#RRGGBB" - int
        list.add(blk("sciParseColor", "d", "",
                "(double)((double) android.graphics.Color.parseColor(%1$s))",
                "parse %s to int"));

        // Color: add alpha (0-255) to color
        list.add(blk("sciColorWithAlpha", "d", "",
                "(double)((double) (((int)(%2$s) & 0xFFFFFF) | (((int)(%1$s) & 0xFF) << 24)))",
                "alpha %d on color %s"));

        // Date: текущая дата в формате
        list.add(blk("sciFormatDateNow", "d", "",
                "((new Object() { public String f() { try { return new java.text.SimpleDateFormat(%1$s, java.util.Locale.getDefault()).format(new java.util.Date()); } catch (Throwable _t) { return \"\"; } } }).f())",
                "now in format %s"));

        // Date: строка с датой - миллисекунды
        list.add(blk("sciParseDate", "d", "",
                "(double)((double) new java.text.SimpleDateFormat(%2$s, java.util.Locale.getDefault()).parse(%1$s).getTime())",
                "parse %s with format %s to millis"));

        // File: имя файла из пути
        list.add(blk("sciGetFileName", "d", "",
                "((new Object() { public String f() { try { return new java.io.File(%1$s).getName(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "name of %s"));

        // File: расширение файла из пути
        list.add(blk("sciGetFileExtension", "d", "",
                "((new Object() { public String f() { try { String _n = new java.io.File(%1$s).getName(); int _i = _n.lastIndexOf('.'); return _i > 0 ? _n.substring(_i + 1) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "extension of %s"));

        // Math: округлить до N знаков
        list.add(blk("sciRoundTo", "d", "",
                "(double)((double) java.math.BigDecimal.valueOf((double)(%1$s)).setScale((int)(%2$s), java.math.RoundingMode.HALF_UP).doubleValue())",
                "round %s to %d decimals"));

        // Network: есть ли интернет
        list.add(blk("sciIsNetworkAvailable", "b", "",
                "((new Object() { public boolean f() { try { android.net.ConnectivityManager _cm = (android.net.ConnectivityManager) getSystemService(android.content.Context.CONNECTIVITY_SERVICE); android.net.NetworkInfo _ni = _cm.getActiveNetworkInfo(); return _ni != null && _ni.isConnected(); } catch (Throwable _t) { return false; } } }).f())",
                "is available"));






        // 🌐 HTTP: GET запрос (возвращает текст ответа)
        list.add(blk("sciHttpGet", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"GET\"); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) { _sb.append(_l); } _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http get %s"));

        // 🌐 HTTP: POST запрос (отправляет данные, возвращает ответ)
        list.add(blk("sciHttpPost", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"POST\"); _c.setDoOutput(true); java.io.OutputStream _os = _c.getOutputStream(); _os.write(%2$s.getBytes(\"UTF-8\")); _os.flush(); _os.close(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) { _sb.append(_l); } _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http post %s data %s"));

        // 📧 Email: отправка через почтовый клиент
        list.add(blk("sciSendEmail", " ", "",
                "try { android.content.Intent _i = new android.content.Intent(android.content.Intent.ACTION_SENDTO); _i.setData(android.net.Uri.parse(\"mailto:\")); _i.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{%1$s}); _i.putExtra(android.content.Intent.EXTRA_SUBJECT, %2$s); _i.putExtra(android.content.Intent.EXTRA_TEXT, %3$s); %4$s.startActivity(android.content.Intent.createChooser(_i, \"Send email\")); } catch (Throwable _t) {}",
                "send email to subject body from %m.context"));

        // 📁 File: имя файла без расширения
        list.add(blk("sciGetFileNameNoExt", "d", "",
                "((new Object() { public String f() { try { String _n = new java.io.File(%1$s).getName(); int _i = _n.lastIndexOf('.'); return _i > 0 ? _n.substring(0, _i) : _n; } catch (Throwable _t) { return \"\"; } } }).f())",
                "file name without extension %s"));

        // 📁 File: полный путь к файлу (абсолютный)
        list.add(blk("sciGetFilePath", "d", "",
                "((new Object() { public String f() { try { return new java.io.File(%1$s).getAbsolutePath(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "file path %s"));

        // 📝 Text: удалить строку по номеру
        list.add(blk("sciTextDeleteLine", " ", "",
                "((new Object() { public void f() { try { String[] _l = %1$s.split(\"\\n\", -1); StringBuilder _sb = new StringBuilder(); for (int _i = 0; _i < _l.length; _i++) { if (_i != (int)(%2$s) - 1) { _sb.append(_l[_i]).append(\"\\n\"); } } %1$s = _sb.toString().trim(); } catch (Throwable _t) {} } }).f());",
                "delete line %d from %s"));

        // 📝 Text: получить текст строки по номеру
        list.add(blk("sciTextGetLine", "d", "",
                "((new Object() { public String f() { try { String[] _l = %1$s.split(\"\\n\", -1); int _n = (int)(%2$s) - 1; return (_n >= 0 && _n < _l.length) ? _l[_n] : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "get line %d from %s"));

        // 📝 Text: очистить текст
        list.add(blk("sciTextClear", " ", "",
                "%1$s = \"\";",
                "clear text %s"));

        // 📝 Text: длина текста
        list.add(blk("sciTextLength", "d", "",
                "(double)(%1$s.length())",
                "length of %s"));

        // 📝 Text: длина без пробелов
        list.add(blk("sciTextLengthNoSpaces", "d", "",
                "(double)(%1$s.replace(\" \", \"\").length())",
                "length without spaces of %s"));

        // 📝 Text: верхний регистр
        list.add(blk("sciTextUpper", "d", "",
                "%1$s.toUpperCase()",
                "uppercase of %s"));

        // 📝 Text: нижний регистр
        list.add(blk("sciTextLower", "d", "",
                "%1$s.toLowerCase()",
                "lowercase of %s"));

        // 📝 Text: длина строки по номеру
        list.add(blk("sciTextLineLength", "d", "",
                "((new Object() { public double f() { try { String[] _l = %1$s.split(\"\\n\", -1); int _n = (int)(%2$s) - 1; return (_n >= 0 && _n < _l.length) ? (double)_l[_n].length() : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "line %d length of %s"));

        // 📝 Text: реверс строк
        list.add(blk("sciTextReverseLines", "d", "",
                "((new Object() { public String f() { try { String[] _l = %1$s.split(\"\\n\", -1); StringBuilder _sb = new StringBuilder(); for (int _i = _l.length - 1; _i >= 0; _i--) { _sb.append(_l[_i]); if (_i > 0) _sb.append(\"\\n\"); } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "reverse lines of %s"));

        // 📝 Text: символ по индексу
        list.add(blk("sciTextCharAt", "d", "",
                "((new Object() { public String f() { try { int _i = (int)(%2$s); return (_i >= 0 && _i < %1$s.length()) ? String.valueOf(%1$s.charAt(_i)) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "char at %d of %s"));

        // 📝 Text: пустой ли текст
        list.add(blk("sciTextIsEmpty", "b", "",
                "%1$s.trim().isEmpty()",
                "is empty %s"));

        // 📝 Text: реверс текста (все символы)
        list.add(blk("sciTextReverse", "d", "",
                "new StringBuilder(%1$s).reverse().toString()",
                "reverse text of %s"));

        // 📝 Text: заменить текст в диапазоне
        list.add(blk("sciTextReplaceRange", " ", "",
                "((new Object() { public void f() { try { int _s = (int)(%2$s); int _l = (int)(%3$s); %1$s = %1$s.substring(0, _s) + %4$s + %1$s.substring(_s + _l); } catch (Throwable _t) {} } }).f());",
                "replace in %s from %d length %d with %s"));

        // 📝 Text: подстрока с индекса
        list.add(blk("sciTextSubstring", "d", "",
                "((new Object() { public String f() { try { int _s = (int)(%2$s); return (_s >= 0 && _s < %1$s.length()) ? %1$s.substring(_s) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "substring from %d of %s"));

        // 📝 Text: капитализация слов
        list.add(blk("sciTextCapitalize", "d", "",
                "((new Object() { public String f() { try { String[] _w = %1$s.split(\" \"); StringBuilder _sb = new StringBuilder(); for (String _s : _w) { if (!_s.isEmpty()) { _sb.append(Character.toUpperCase(_s.charAt(0))).append(_s.substring(1).toLowerCase()).append(\" \"); } } return _sb.toString().trim(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "capitalize words of %s"));

        // 📝 Text: удалить все пробелы
        list.add(blk("sciTextRemoveSpaces", "d", "",
                "%1$s.replace(\" \", \"\")",
                "remove spaces from %s"));

        // 📝 Text: удалить однострочные комментарии (//)
        list.add(blk("sciTextRemoveLineComments", "d", "",
                "%1$s.replaceAll(\"//.*\", \"\")",
                "remove // comments from %s"));

        // 📝 Text: удалить все комментарии (// и /* */)
        list.add(blk("sciTextRemoveAllComments", "d", "",
                "%1$s.replaceAll(\"//.*|/\\\\*[\\\\s\\\\S]*?\\\\*/\", \"\")",
                "remove all comments from %s"));

        // 📝 Text: установить подсказку (hint) для поля
        list.add(blk("sciTextSetHint", " ", "",
                "%1$s.setHint(%2$s);",
                "set hint %s on %s"));

        // 🛡️ Security: MD5
        list.add(blk("sciMd5", "d", "",
                "((new Object() { public String f() { try { java.security.MessageDigest _md = java.security.MessageDigest.getInstance(\"MD5\"); byte[] _b = _md.digest(%1$s.getBytes()); StringBuilder _sb = new StringBuilder(); for (byte _x : _b) _sb.append(String.format(\"%02x\", _x)); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "md5 of %s"));

        // 🛡️ Security: Base64 Encode
        list.add(blk("sciBase64Encode", "d", "",
                "android.util.Base64.encodeToString(%1$s.getBytes(), android.util.Base64.NO_WRAP)",
                "base64 encode %s"));

        // 🛡️ Security: Base64 Decode
        list.add(blk("sciBase64Decode", "d", "",
                "new String(android.util.Base64.decode(%1$s, android.util.Base64.NO_WRAP))",
                "base64 decode %s"));

        // 🛡️ Security: SHA-256
        list.add(blk("sciSha256", "d", "",
                "((new Object() { public String f() { try { java.security.MessageDigest _md = java.security.MessageDigest.getInstance(\"SHA-256\"); byte[] _b = _md.digest(%1$s.getBytes()); StringBuilder _sb = new StringBuilder(); for (byte _x : _b) _sb.append(String.format(\"%02x\", _x)); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "sha256 of %s"));

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
                "days between %s and %s"));

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
                "join list %s with %s"));

        // 📋 List: отсортировать список (строки/числа)
        list.add(blk("sciListSort", " ", "",
                "((new Object() { public void f() { try { java.util.Collections.sort(%1$s); } catch (Throwable _t) {} } }).f());",
                "sort list %s"));

        // 📋 List: перевернуть список
        list.add(blk("sciListReverse", " ", "",
                "((new Object() { public void f() { try { java.util.Collections.reverse(%1$s); } catch (Throwable _t) {} } }).f());",
                "reverse list %s"));

        // 📋 List: удалить дубликаты (для List String)
        list.add(blk("sciListUnique", " ", "",
                "((new Object() { public void f() { try { java.util.LinkedHashSet _set = new java.util.LinkedHashSet(%1$s); %1$s.clear(); %1$s.addAll(_set); } catch (Throwable _t) {} } }).f());",
                "remove duplicates from %s"));

        // 📋 List: получить первый элемент
        list.add(blk("sciListFirst", "d", "",
                "((new Object() { public String f() { try { return %1$s.isEmpty() ? \"\" : String.valueOf(%1$s.get(0)); } catch (Throwable _t) { return \"\"; } } }).f())",
                "first of %s"));

        // 📋 List: получить последний элемент
        list.add(blk("sciListLast", "d", "",
                "((new Object() { public String f() { try { return %1$s.isEmpty() ? \"\" : String.valueOf(%1$s.get(%1$s.size() - 1)); } catch (Throwable _t) { return \"\"; } } }).f())",
                "last of %s"));

        // 📋 List: найти индекс элемента
        list.add(blk("sciListIndexOf", "d", "",
                "(double)(%1$s.indexOf(%2$s))",
                "index of %s in %s"));

        // 📋 List: создать копию списка
        list.add(blk("sciListCopy", " ", "",
                "((new Object() { public void f() { try { %2$s.clear(); %2$s.addAll(%1$s); } catch (Throwable _t) {} } }).f());",
                "copy %s to %s"));

        // 📋 List: перемешать список (случайно)
        list.add(blk("sciListShuffle", " ", "",
                "((new Object() { public void f() { try { java.util.Collections.shuffle(%1$s); } catch (Throwable _t) {} } }).f());",
                "shuffle %s"));

        // 📦 JSON: получить значение по ключу (из строки JSON)
        list.add(blk("sciJsonGet", "d", "",
                "((new Object() { public String f() { try { org.json.JSONObject _o = new org.json.JSONObject(%1$s); return _o.optString(%2$s, \"\"); } catch (Throwable _t) { return \"\"; } } }).f())",
                "json get %s from %s"));

        // 📦 JSON: создать строку JSON из ключа и значения
        list.add(blk("sciJsonCreate", "d", "",
                "((new Object() { public String f() { try { org.json.JSONObject _o = new org.json.JSONObject(); _o.put(%1$s, %2$s); return _o.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "json create key %s value %s"));

        // 📦 JSON: массив из списка строк
        list.add(blk("sciJsonArray", "d", "",
                "new org.json.JSONArray(%1$s).toString()",
                "json array from list %s"));

        // 🗄️ SQLite: открыть/создать БД
        list.add(blk("sciSqliteOpen", " ", "",
                "((new Object() { public void f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); _db.close(); } catch (Throwable _t) {} } }).f());",
                "open sqlite db from %m.context"));

        // 🗄️ SQLite: выполнить запрос (INSERT/UPDATE/DELETE)
        list.add(blk("sciSqliteExec", " ", "",
                "((new Object() { public void f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); _db.execSQL(%3$s); _db.close(); } catch (Throwable _t) {} } }).f());",
                "sqlite exec %s in %s"));

        // 🗄️ SQLite: запрос SELECT (возвращает курсор в виде строки)
        list.add(blk("sciSqliteQuery", "d", "",
                "((new Object() { public String f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); android.database.Cursor _c = _db.rawQuery(%3$s, null); StringBuilder _sb = new StringBuilder(); while (_c.moveToNext()) { for (int _i = 0; _i < _c.getColumnCount(); _i++) { _sb.append(_c.getString(_i)).append(\" | \"); } _sb.append(\"\\n\"); } _c.close(); _db.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "sqlite query %s in %s"));

        // 🎲 Random: случайное целое от min до max
        list.add(blk("sciRandomInt", "d", "",
                "(double)(new java.util.Random().nextInt((int)(%2$s - %1$s + 1)) + (int)(%1$s))",
                "random int from %d to %d"));

        // 🎲 Random: случайная строка из набора символов
        list.add(blk("sciRandomString", "d", "",
                "((new Object() { public String f() { try { String _chars = \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\"; StringBuilder _sb = new StringBuilder(); java.util.Random _r = new java.util.Random(); for (int _i = 0; _i < (int)(%1$s); _i++) _sb.append(_chars.charAt(_r.nextInt(_chars.length()))); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "random string length %d"));

        // 🎲 Random: случайный элемент из списка
        list.add(blk("sciRandomFromList", "d", "",
                "((new Object() { public String f() { try { return %1$s.isEmpty() ? \"\" : String.valueOf(%1$s.get(new java.util.Random().nextInt(%1$s.size()))); } catch (Throwable _t) { return \"\"; } } }).f())",
                "random from list %s"));

        // 🔔 Notification: показать уведомление (Toast)
        list.add(blk("sciShowToast", " ", "",
                "android.widget.Toast.makeText(%2$s, %1$s, android.widget.Toast.LENGTH_SHORT).show();",
                "show toast from %m.context"));

        // 🔔 Notification: показать системное уведомление
        list.add(blk("sciShowNotification", " ", "",
                "((new Object() { public void f() { try { androidx.core.app.NotificationCompat.Builder _b = new androidx.core.app.NotificationCompat.Builder(%1$s, \"default\").setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle(%2$s).setContentText(%3$s).setAutoCancel(true); androidx.core.app.NotificationManagerCompat.from(%1$s).notify((int)System.currentTimeMillis(), _b.build()); } catch (Throwable _t) {} } }).f());",
                "show notification from %m.context"));

        // 📱 Intent: открыть ссылку в браузере
        list.add(blk("sciOpenUrl", " ", "",
                "%2$s.startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(%1$s)));",
                "open url from %m.context"));

        // 📱 Intent: позвонить по номеру
        list.add(blk("sciCallPhone", " ", "",
                "%2$s.startActivity(new android.content.Intent(android.content.Intent.ACTION_DIAL, android.net.Uri.parse(\"tel:\" + %1$s)));",
                "call from %m.context"));

        // 📱 Intent: отправить SMS
        list.add(blk("sciSendSms", " ", "",
                "%3$s.startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(\"sms:\" + %1$s + \"?body=\" + android.net.Uri.encode(%2$s))));",
                "send sms to text from %m.context"));

        // 📱 Intent: открыть другое приложение по пакету
        list.add(blk("sciOpenApp", " ", "",
                "try { %2$s.startActivity(%2$s.getPackageManager().getLaunchIntentForPackage(%1$s)); } catch (Throwable _t) {}",
                "open app from %m.context"));

        // 📱 Intent: поделиться текстом
        list.add(blk("sciShareText", " ", "",
                "android.content.Intent _s = new android.content.Intent(android.content.Intent.ACTION_SEND); _s.setType(\"text/plain\"); _s.putExtra(android.content.Intent.EXTRA_TEXT, %1$s); %2$s.startActivity(android.content.Intent.createChooser(_s, \"Share\"));",
                "share text from %m.context"));

        // 📁 File: прочитать текст из файла
        list.add(blk("sciFileRead", "d", "",
                "((new Object() { public String f() { try { java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.FileReader(%1$s)); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) { _sb.append(_l).append(\"\\n\"); } _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "read file %s"));

        // 📁 File: записать текст в файл
        list.add(blk("sciFileWrite", " ", "",
                "((new Object() { public void f() { try { java.io.FileWriter _w = new java.io.FileWriter(%1$s); _w.write(%2$s); _w.close(); } catch (Throwable _t) {} } }).f());",
                "write %s to file %s"));

        // 📁 File: проверить существование файла
        list.add(blk("sciFileExists", "b", "",
                "new java.io.File(%1$s).exists()",
                "file %s exists"));

        // 📁 File: удалить файл
        list.add(blk("sciFileDelete", " ", "",
                "((new Object() { public void f() { try { new java.io.File(%1$s).delete(); } catch (Throwable _t) {} } }).f());",
                "delete file %s"));

        // 🌐 HTTP: GET с заголовком
        list.add(blk("sciHttpGetHeader", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestProperty(%2$s, %3$s); _c.setRequestMethod(\"GET\"); _c.setConnectTimeout(5000); _c.setReadTimeout(5000); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) { _sb.append(_l); } _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http get %s header %s: %s"));

        // 🎨 Color: смешать два цвета (50/50)
        list.add(blk("sciColorMix", "d", "",
                "(((int)(%1$s) & 0xFF000000) | ((((int)(%1$s) & 0xFF0000) + ((int)(%2$s) & 0xFF0000)) / 2 & 0xFF0000) | ((((int)(%1$s) & 0xFF00) + ((int)(%2$s) & 0xFF00)) / 2 & 0xFF00) | ((((int)(%1$s) & 0xFF) + ((int)(%2$s) & 0xFF)) / 2 & 0xFF))",
                "mix color %s and %s"));

        // 🎨 Color: получить прозрачность (0-255)
        list.add(blk("sciColorAlpha", "d", "",
                "(double)(((int)(%1$s) >> 24) & 0xFF)",
                "alpha of %s"));

        // 🎨 Color: HEX в цвет (int)
        list.add(blk("sciColorFromHex", "d", "",
                "android.graphics.Color.parseColor(%1$s)",
                "color from hex %s"));

        // 🎨 Color: цвет в HEX-строку
        list.add(blk("sciColorToHex", "d", "",
                "String.format(\"#%08X\", (int)(%1$s))",
                "color %s to hex"));

        // 📊 Progress: установить прогресс (0-100)
        list.add(blk("sciSetProgress", " ", "",
                "%1$s.setProgress((int)(%2$s));",
                "set progress %d on %s"));

        // 📊 Progress: показать/скрыть прогресс-бар
        list.add(blk("sciSetProgressVisible", " ", "",
                "%1$s.setVisibility((%2$s) ? android.view.View.VISIBLE : android.view.View.GONE);",
                "set progress %s visible %s"));

        // 📳 Vibrate: вибрация (миллисекунды)
        list.add(blk("sciVibrate", " ", "",
                "((android.os.Vibrator) %2$s.getSystemService(android.content.Context.VIBRATOR_SERVICE)).vibrate((long)(%1$s));",
                "vibrate %d ms from %m.context"));

        // 🧮 Calc: вычислить математическое выражение из строки
        list.add(blk("sciCalcExpression", "d", "",
                "((new Object() { public double f() { try { javax.script.ScriptEngine _e = new javax.script.ScriptEngineManager().getEngineByName(\"js\"); return ((Number) _e.eval(%1$s)).doubleValue(); } catch (Throwable _t) { return 0.0; } } }).f())",
                "calc expression %s"));

        // 🔐 AES: зашифровать текст
        list.add(blk("sciAesEncrypt", "d", "",
                "((new Object() { public String f() { try { javax.crypto.Cipher _c = javax.crypto.Cipher.getInstance(\"AES/ECB/PKCS5Padding\"); _c.init(javax.crypto.Cipher.ENCRYPT_MODE, new javax.crypto.spec.SecretKeySpec(\"0123456789abcdef\".getBytes(), \"AES\")); return android.util.Base64.encodeToString(_c.doFinal(%1$s.getBytes()), android.util.Base64.NO_WRAP); } catch (Throwable _t) { return \"\"; } } }).f())",
                "aes encrypt %s"));

        // 🔐 AES: расшифровать текст
        list.add(blk("sciAesDecrypt", "d", "",
                "((new Object() { public String f() { try { javax.crypto.Cipher _c = javax.crypto.Cipher.getInstance(\"AES/ECB/PKCS5Padding\"); _c.init(javax.crypto.Cipher.DECRYPT_MODE, new javax.crypto.spec.SecretKeySpec(\"0123456789abcdef\".getBytes(), \"AES\")); return new String(_c.doFinal(android.util.Base64.decode(%1$s, android.util.Base64.NO_WRAP))); } catch (Throwable _t) { return \"\"; } } }).f())",
                "aes decrypt %s"));

        // 📦 ZIP: распаковать архив
        list.add(blk("sciUnzip", " ", "",
                "((new Object() { public void f() { try { java.util.zip.ZipInputStream _z = new java.util.zip.ZipInputStream(new java.io.FileInputStream(%1$s)); java.util.zip.ZipEntry _e; while ((_e = _z.getNextEntry()) != null) { java.io.File _o = new java.io.File(%2$s, _e.getName()); if (_e.isDirectory()) _o.mkdirs(); else { new java.io.File(_o.getParent()).mkdirs(); java.io.FileOutputStream _os = new java.io.FileOutputStream(_o); byte[] _b = new byte[4096]; int _len; while ((_len = _z.read(_b)) > 0) _os.write(_b, 0, _len); _os.close(); } } _z.close(); } catch (Throwable _t) {} } }).f());",
                "unzip %s to %s"));

        // 📦 ZIP: создать архив
        list.add(blk("sciZip", " ", "",
                "((new Object() { public void f() { try { java.util.zip.ZipOutputStream _z = new java.util.zip.ZipOutputStream(new java.io.FileOutputStream(%2$s)); java.io.File[] _fs = new java.io.File(%1$s).listFiles(); if (_fs != null) { for (java.io.File _file : _fs) { java.io.FileInputStream _is = new java.io.FileInputStream(_file); _z.putNextEntry(new java.util.zip.ZipEntry(_file.getName())); byte[] _b = new byte[4096]; int _len; while ((_len = _is.read(_b)) > 0) _z.write(_b, 0, _len); _is.close(); _z.closeEntry(); } } _z.close(); } catch (Throwable _t) {} } }).f());",
                "zip folder %s to %s"));

        // 📶 Network: тип подключения (WiFi / Mobile / None)
        list.add(blk("sciNetworkType", "d", "",
                "((new Object() { public String f() { try { android.net.ConnectivityManager _cm = (android.net.ConnectivityManager) getSystemService(android.content.Context.CONNECTIVITY_SERVICE); android.net.NetworkInfo _ni = _cm.getActiveNetworkInfo(); if (_ni == null || !_ni.isConnected()) return \"None\"; return _ni.getType() == android.net.ConnectivityManager.TYPE_WIFI ? \"WiFi\" : \"Mobile\"; } catch (Throwable _t) { return \"None\"; } } }).f())",
                "network type"));

        // ☀️ Screen: установить яркость (0-255)
        list.add(blk("sciSetBrightness", " ", "",
                "((new Object() { public void f() { try { android.view.WindowManager.LayoutParams _p = %2$s.getWindow().getAttributes(); _p.screenBrightness = (float)((int)(%1$s)) / 255.0f; %2$s.getWindow().setAttributes(_p); } catch (Throwable _t) {} } }).f());",
                "set brightness %d from %m.activity"));

        // 🔊 Volume: установить громкость медиа (0-100)
        list.add(blk("sciSetVolume", " ", "",
                "((new Object() { public void f() { try { android.media.AudioManager _am = (android.media.AudioManager) %2$s.getSystemService(android.content.Context.AUDIO_SERVICE); int _max = _am.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC); _am.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, (int)(%1$s) * _max / 100, 0); } catch (Throwable _t) {} } }).f());",
                "set volume %d%% from %m.context"));

        // 📦 Archive: открыть архив (7z, rar, zip) через другое приложение
        list.add(blk("sciOpenArchive", " ", "",
                "try { android.content.Intent _i = new android.content.Intent(android.content.Intent.ACTION_VIEW); _i.setDataAndType(android.net.Uri.fromFile(new java.io.File(%1$s)), \"*/*\"); _i.addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION); %2$s.startActivity(android.content.Intent.createChooser(_i, \"Open archive\")); } catch (Throwable _t) {}",
                "open archive from %m.context"));

        // 🖼️ Image: получить ширину изображения
        list.add(blk("sciImageWidth", "d", "",
                "((new Object() { public double f() { try { android.graphics.BitmapFactory.Options _o = new android.graphics.BitmapFactory.Options(); _o.inJustDecodeBounds = true; android.graphics.BitmapFactory.decodeFile(%1$s, _o); return (double) _o.outWidth; } catch (Throwable _t) { return 0.0; } } }).f())",
                "image width of %s"));

        // 🖼️ Image: получить высоту изображения
        list.add(blk("sciImageHeight", "d", "",
                "((new Object() { public double f() { try { android.graphics.BitmapFactory.Options _o = new android.graphics.BitmapFactory.Options(); _o.inJustDecodeBounds = true; android.graphics.BitmapFactory.decodeFile(%1$s, _o); return (double) _o.outHeight; } catch (Throwable _t) { return 0.0; } } }).f())",
                "image height of %s"));

        // 🖼️ Image: сохранить в галерею
        list.add(blk("sciSaveToGallery", " ", "",
                "((new Object() { public void f() { try { android.graphics.Bitmap _b = android.graphics.BitmapFactory.decodeFile(%1$s); java.io.File _out = new java.io.File(android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_PICTURES), %2$s); java.io.FileOutputStream _os = new java.io.FileOutputStream(_out); _b.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, _os); _os.flush(); _os.close(); android.media.MediaScannerConnection.scanFile(%3$s, new String[]{_out.getAbsolutePath()}, null, null); } catch (Throwable _t) {} } }).f());",
                "save image to gallery as from %m.context"));

        // 🎵 Audio: воспроизвести звук
        list.add(blk("sciPlaySound", " ", "",
                "((new Object() { public void f() { try { android.media.MediaPlayer _mp = new android.media.MediaPlayer(); _mp.setDataSource(%1$s); _mp.prepare(); _mp.start(); } catch (Throwable _t) {} } }).f());",
                "play sound %s"));

        // 📞 Contacts: получить имя по номеру
        list.add(blk("sciGetContactName", "d", "",
                "((new Object() { public String f() { try { android.net.Uri _u = android.net.Uri.withAppendedPath(android.provider.ContactsContract.PhoneLookup.CONTENT_FILTER_URI, android.net.Uri.encode(%1$s)); android.database.Cursor _c = %2$s.getContentResolver().query(_u, new String[]{android.provider.ContactsContract.PhoneLookup.DISPLAY_NAME}, null, null, null); if (_c != null && _c.moveToFirst()) { String _n = _c.getString(0); _c.close(); return _n; } return %1$s; } catch (Throwable _t) { return %1$s; } } }).f())",
                "contact name for from %m.context"));

        // 📅 Date: добавить дни к дате (dd.MM.yyyy)
        list.add(blk("sciAddDays", "d", "",
                "((new Object() { public String f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); _c.add(java.util.Calendar.DAY_OF_MONTH, (int)(%2$s)); return _fmt.format(_c.getTime()); } catch (Throwable _t) { return %1$s; } } }).f())",
                "add %d days to %s"));

        // 📅 Date: день недели (1-7)
        list.add(blk("sciDayOfWeek", "d", "",
                "((new Object() { public double f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); return (double) _c.get(java.util.Calendar.DAY_OF_WEEK); } catch (Throwable _t) { return 0.0; } } }).f())",
                "day of week for %s"));

        // 🔐 AES: зашифровать с ключом
        list.add(blk("sciAesEncryptKey", "d", "",
                "((new Object() { public String f() { try { javax.crypto.Cipher _c = javax.crypto.Cipher.getInstance(\"AES/ECB/PKCS5Padding\"); _c.init(javax.crypto.Cipher.ENCRYPT_MODE, new javax.crypto.spec.SecretKeySpec(%2$s.getBytes(), \"AES\")); return android.util.Base64.encodeToString(_c.doFinal(%1$s.getBytes()), android.util.Base64.NO_WRAP); } catch (Throwable _t) { return \"\"; } } }).f())",
                "aes encrypt %s with key %s"));

        // 🔐 AES: расшифровать с ключом
        list.add(blk("sciAesDecryptKey", "d", "",
                "((new Object() { public String f() { try { javax.crypto.Cipher _c = javax.crypto.Cipher.getInstance(\"AES/ECB/PKCS5Padding\"); _c.init(javax.crypto.Cipher.DECRYPT_MODE, new javax.crypto.spec.SecretKeySpec(%2$s.getBytes(), \"AES\")); return new String(_c.doFinal(android.util.Base64.decode(%1$s, android.util.Base64.NO_WRAP))); } catch (Throwable _t) { return \"\"; } } }).f())",
                "aes decrypt %s with key %s"));

        // 🎨 UI: установить цвет фона
        list.add(blk("sciSetBackgroundColor", " ", "",
                "%1$s.setBackgroundColor((int)(%2$s));",
                "set background color %d on %s"));

        // 🎨 UI: установить отступы (padding)
        list.add(blk("sciSetPadding", " ", "",
                "%1$s.setPadding((int)(%2$s), (int)(%3$s), (int)(%4$s), (int)(%5$s));",
                "set padding %s left %d top %d right %d bottom %d"));

        // 🎨 UI: установить поворот
        list.add(blk("sciSetRotation", " ", "",
                "%1$s.setRotation((float)(%2$s));",
                "set rotation %s on %s"));

        // 🎨 UI: установить масштаб по X
        list.add(blk("sciSetScaleX", " ", "",
                "%1$s.setScaleX((float)(%2$s));",
                "set scaleX %s on %s"));

        // 🎨 UI: установить масштаб по Y
        list.add(blk("sciSetScaleY", " ", "",
                "%1$s.setScaleY((float)(%2$s));",
                "set scaleY %s on %s"));

        // 🎨 UI: установить тень (elevation)
        list.add(blk("sciSetElevation", " ", "",
                "%1$s.setElevation((float)(%2$s));",
                "set elevation %s on %s"));

        // 🎨 UI: включить/выключить виджет
        list.add(blk("sciSetEnabled", " ", "",
                "%1$s.setEnabled(%2$s);",
                "set enabled %b on %s"));

        // 🎨 UI: сделать кликабельным
        list.add(blk("sciSetClickable", " ", "",
                "%1$s.setClickable(%2$s);",
                "set clickable %b on %s"));

        // 🎨 UI: сделать фокусируемым
        list.add(blk("sciSetFocusable", " ", "",
                "%1$s.setFocusable(%2$s);",
                "set focusable %b on %s"));

        // 🎨 UI: установить подсказку (для EditText)
        list.add(blk("sciSetHintText", " ", "",
                "%1$s.setHint(%2$s);",
                "set hint %s on %s"));

        // 📋 List: получить подсписок (с index по index)
        list.add(blk("sciListSubList", "d", "",
                "new java.util.ArrayList(%1$s.subList(Math.min((int)(%2$s), %1$s.size()), Math.min((int)(%3$s), %1$s.size())))",
                "sub list %s from %d to %d"));

        // 📋 List: объединить два списка
        list.add(blk("sciListConcat", " ", "",
                "((new Object() { public void f() { try { %3$s.clear(); %3$s.addAll(%1$s); %3$s.addAll(%2$s); } catch (Throwable _t) {} } }).f());",
                "concat %s and %s into %s"));

        // 📋 List: заполнить список значением
        list.add(blk("sciListFill", " ", "",
                "((new Object() { public void f() { try { java.util.Collections.fill(%1$s, %2$s); } catch (Throwable _t) {} } }).f());",
                "fill %s with %s"));

        // 📋 List: бинарный поиск (предполагает сортировку)
        list.add(blk("sciListBinarySearch", "d", "",
                "(double) java.util.Collections.binarySearch(%1$s, %2$s)",
                "binary search %s in %s"));

        // 📋 List: максимальный элемент
        list.add(blk("sciListMax", "d", "",
                "((new Object() { public String f() { try { return %1$s.isEmpty() ? \"\" : String.valueOf(java.util.Collections.max((java.util.Collection) %1$s)); } catch (Throwable _t) { return \"\"; } } }).f())",
                "max of %s"));

        // 📋 List: минимальный элемент
        list.add(blk("sciListMin", "d", "",
                "((new Object() { public String f() { try { return %1$s.isEmpty() ? \"\" : String.valueOf(java.util.Collections.min((java.util.Collection) %1$s)); } catch (Throwable _t) { return \"\"; } } }).f())",
                "min of %s"));

        // 📋 List: фильтр (только элементы, содержащие подстроку)
        list.add(blk("sciListFilterContains", " ", "",
                "((new Object() { public void f() { try { java.util.ArrayList _tmp = new java.util.ArrayList(); for (Object _o : %1$s) { if (_o != null && _o.toString().contains(%2$s)) _tmp.add(_o); } %1$s.clear(); %1$s.addAll(_tmp); } catch (Throwable _t) {} } }).f());",
                "filter %s contains %s"));

        // 📋 List: удалить элемент по значению
        list.add(blk("sciListRemoveValue", " ", "",
                "((new Object() { public void f() { try { %1$s.remove(%2$s); } catch (Throwable _t) {} } }).f());",
                "remove %s from %s"));

        // 📋 List: удалить элемент по индексу
        list.add(blk("sciListRemoveIndex", " ", "",
                "((new Object() { public void f() { try { int _i = (int)(%2$s); if (_i >= 0 && _i < %1$s.size()) %1$s.remove(_i); } catch (Throwable _t) {} } }).f());",
                "remove index %d from %s"));

        // 📋 List: вставить элемент по индексу
        list.add(blk("sciListInsert", " ", "",
                "((new Object() { public void f() { try { int _i = (int)(%2$s); if (_i >= 0 && _i <= %1$s.size()) %1$s.add(_i, %3$s); } catch (Throwable _t) {} } }).f());",
                "insert %s at %d in %s"));

        // 📋 List: заменить элемент по индексу
        list.add(blk("sciListSet", " ", "",
                "((new Object() { public void f() { try { int _i = (int)(%2$s); if (_i >= 0 && _i < %1$s.size()) %1$s.set(_i, %3$s); } catch (Throwable _t) {} } }).f());",
                "set %s at %d in %s"));

        // 📋 List: получить элемент по индексу
        list.add(blk("sciListGet", "d", "",
                "((new Object() { public String f() { try { int _i = (int)(%2$s); return (_i >= 0 && _i < %1$s.size()) ? String.valueOf(%1$s.get(_i)) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "get %d from %s"));

        // 📋 List: размер списка (число)
        list.add(blk("sciListSizeNum", "d", "",
                "(double)(%1$s.size())",
                "size of %s"));

        // 📋 List: очистить список
        list.add(blk("sciListClear", " ", "",
                "%1$s.clear();",
                "clear %s"));

        // 📋 List: проверить, содержит ли список элемент
        list.add(blk("sciListHas", "b", "",
                "%1$s.contains(%2$s)",
                "%s contains %s"));

        // 📋 List: преобразовать в массив строк
        list.add(blk("sciListToArray", "d", "",
                "%1$s.toArray()",
                "to array from %s"));

        // 📋 List: удалить все вхождения значения
        list.add(blk("sciListRemoveAll", " ", "",
                "((new Object() { public void f() { try { java.util.ArrayList _tmp = new java.util.ArrayList(); for (Object _o : %1$s) { if (_o == null || !_o.equals(%2$s)) _tmp.add(_o); } %1$s.clear(); %1$s.addAll(_tmp); } catch (Throwable _t) {} } }).f());",
                "remove all %s from %s"));

        // 📋 List: оставить только уникальные (по строке)
        list.add(blk("sciListDistinctStr", " ", "",
                "((new Object() { public void f() { try { java.util.LinkedHashSet _s = new java.util.LinkedHashSet(); for (Object _o : %1$s) _s.add(String.valueOf(_o)); %1$s.clear(); %1$s.addAll(_s); } catch (Throwable _t) {} } }).f());",
                "distinct strings in %s"));

        // 📋 List: соединить в строку с разделителем (для чисел)
        list.add(blk("sciListJoinNum", "d", "",
                "((new Object() { public String f() { try { StringBuilder _sb = new StringBuilder(); for (int _i = 0; _i < %1$s.size(); _i++) { if (_i > 0) _sb.append(%2$s); _sb.append(%1$s.get(_i)); } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "join numbers %s with %s"));

        // 📋 List: найти первый индекс с подстрокой
        list.add(blk("sciListFindContains", "d", "",
                "((new Object() { public double f() { try { for (int _i = 0; _i < %1$s.size(); _i++) { if (String.valueOf(%1$s.get(_i)).contains(%2$s)) return (double)_i; } return -1.0; } catch (Throwable _t) { return -1.0; } } }).f())",
                "find %s in %s"));

        // 📋 List: создать список из строки (разделитель)
        list.add(blk("sciListFromString", " ", "",
                "((new Object() { public void f() { try { %2$s.clear(); for (String _s : %1$s.split(%3$s)) %2$s.add(_s); } catch (Throwable _t) {} } }).f());",
                "split %s by %s into %s"));

        // 📋 List: обратная сортировка
        list.add(blk("sciListSortDesc", " ", "",
                "((new Object() { public void f() { try { java.util.Collections.sort(%1$s); java.util.Collections.reverse(%1$s); } catch (Throwable _t) {} } }).f());",
                "sort descending %s"));

        // 📋 List: первый индекс (поиск)
        list.add(blk("sciListFirstIndex", "d", "",
                "(double)(%1$s.indexOf(%2$s))",
                "first index of %s in %s"));

        // 📋 List: последний индекс
        list.add(blk("sciListLastIndex", "d", "",
                "(double)(%1$s.lastIndexOf(%2$s))",
                "last index of %s in %s"));

        // 📋 List: проверить, пустой ли список
        list.add(blk("sciListIsEmptyList", "b", "",
                "%1$s.isEmpty()",
                "%s is empty"));

        // 📋 List: преобразовать в строку
        list.add(blk("sciListToString", "d", "",
                "%1$s.toString()",
                "%s to string"));

        // 📁 File: переименовать
        list.add(blk("sciFileRename", " ", "",
                "((new Object() { public void f() { try { new java.io.File(%1$s).renameTo(new java.io.File(%2$s)); } catch (Throwable _t) {} } }).f());",
                "rename file %s to %s"));

        // 📁 File: полный путь
        list.add(blk("sciFileAbsolutePath", "d", "",
                "new java.io.File(%1$s).getAbsolutePath()",
                "absolute path of %s"));

        // 📁 File: родительская папка
        list.add(blk("sciFileParent", "d", "",
                "((new Object() { public String f() { try { return new java.io.File(%1$s).getParent(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "parent of %s"));

        // 📁 File: рекурсивный список файлов
        list.add(blk("sciFileListRecursive", " ", "",
                "((new Object() { public void f() { try { java.util.ArrayList _r = new java.util.ArrayList(); java.io.File[] _fs = new java.io.File(%1$s).listFiles(); if (_fs != null) { for (java.io.File _x : _fs) { if (_x.isDirectory()) { java.io.File[] _sub = _x.listFiles(); if (_sub != null) { for (java.io.File _y : _sub) _r.add(_y.getAbsolutePath()); } } else { _r.add(_x.getAbsolutePath()); } } } %2$s.clear(); %2$s.addAll(_r); } catch (Throwable _t) {} } }).f());",
                "list files recursively from %s into %s"));

        // 📁 File: удалить папку со всем содержимым
        list.add(blk("sciFileDeleteRecursive", " ", "",
                "((new Object() { public void f() { try { java.io.File _d = new java.io.File(%1$s); if (_d.isDirectory()) { java.io.File[] _fs = _d.listFiles(); if (_fs != null) { for (java.io.File _x : _fs) _x.delete(); } } _d.delete(); } catch (Throwable _t) {} } }).f());",
                "delete folder %s recursively"));

        // 📁 File: размер в КБ
        list.add(blk("sciFileSizeKB", "d", "",
                "(double)(new java.io.File(%1$s).length() / 1024)",
                "size KB of %s"));

        // 📁 File: размер в МБ
        list.add(blk("sciFileSizeMB", "d", "",
                "(double)(new java.io.File(%1$s).length() / (1024 * 1024))",
                "size MB of %s"));

        // 📁 File: скрытый ли файл
        list.add(blk("sciFileIsHidden", "b", "",
                "new java.io.File(%1$s).isHidden()",
                "file %s is hidden"));

        // 📁 File: дата последнего изменения (millis)
        list.add(blk("sciFileLastModified", "d", "",
                "(double)(new java.io.File(%1$s).lastModified())",
                "last modified of %s"));

        // 📁 File: создать пустой файл
        list.add(blk("sciFileCreateNew", "b", "",
                "((new Object() { public boolean f() { try { return new java.io.File(%1$s).createNewFile(); } catch (Throwable _t) { return false; } } }).f())",
                "create new file %s"));

        // 📁 File: MIME-тип по расширению
        list.add(blk("sciFileMimeType", "d", "",
                "((new Object() { public String f() { try { String _n = new java.io.File(%1$s).getName(); String _e = _n.substring(_n.lastIndexOf('.') + 1).toLowerCase(); if (_e.equals(\"jpg\") || _e.equals(\"jpeg\")) return \"image/jpeg\"; if (_e.equals(\"png\")) return \"image/png\"; if (_e.equals(\"pdf\")) return \"application/pdf\"; if (_e.equals(\"txt\")) return \"text/plain\"; if (_e.equals(\"mp3\")) return \"audio/mpeg\"; if (_e.equals(\"mp4\")) return \"video/mp4\"; return \"application/octet-stream\"; } catch (Throwable _t) { return \"application/octet-stream\"; } } }).f())",
                "mime type of %s"));

        // 📁 File: общее место на диске (МБ)
        list.add(blk("sciFileTotalSpace", "d", "",
                "(double)(new java.io.File(%1$s).getTotalSpace() / (1024 * 1024))",
                "total space MB of %s"));

        // 📁 File: свободное место на диске (МБ)
        list.add(blk("sciFileFreeSpace", "d", "",
                "(double)(new java.io.File(%1$s).getFreeSpace() / (1024 * 1024))",
                "free space MB of %s"));

        // 📁 File: доступное место (МБ)
        list.add(blk("sciFileUsableSpace", "d", "",
                "(double)(new java.io.File(%1$s).getUsableSpace() / (1024 * 1024))",
                "usable space MB of %s"));

        // 📁 File: является ли файлом (не папкой)
        list.add(blk("sciFileIsFile", "b", "",
                "new java.io.File(%1$s).isFile()",
                "file %s is a file"));

        // 📁 File: копировать рекурсивно
        list.add(blk("sciFileCopyRecursive", " ", "",
                "((new Object() { public void f() { try { java.io.File _src = new java.io.File(%1$s); java.io.File _dst = new java.io.File(%2$s); if (_src.isDirectory()) { _dst.mkdirs(); java.io.File[] _fs = _src.listFiles(); if (_fs != null) { for (java.io.File _x : _fs) { java.io.File _to = new java.io.File(_dst, _x.getName()); java.io.FileInputStream _is = new java.io.FileInputStream(_x); java.io.FileOutputStream _os = new java.io.FileOutputStream(_to); byte[] _b = new byte[4096]; int _len; while ((_len = _is.read(_b)) > 0) _os.write(_b, 0, _len); _is.close(); _os.close(); } } } } catch (Throwable _t) {} } }).f());",
                "copy folder %s to %s recursively"));

        // 📁 File: переместить рекурсивно
        list.add(blk("sciFileMoveRecursive", " ", "",
                "((new Object() { public void f() { try { java.io.File _src = new java.io.File(%1$s); java.io.File _dst = new java.io.File(%2$s); _src.renameTo(_dst); } catch (Throwable _t) {} } }).f());",
                "move folder %s to %s"));

        // 📁 File: получить расширение файла
        list.add(blk("sciFileExtOnly", "d", "",
                "((new Object() { public String f() { try { String _n = new java.io.File(%1$s).getName(); int _i = _n.lastIndexOf('.'); return _i > 0 ? _n.substring(_i + 1) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "extension of %s"));

        // 📁 File: прочитать первые N символов
        list.add(blk("sciFileReadFirst", "d", "",
                "((new Object() { public String f() { try { java.io.RandomAccessFile _r = new java.io.RandomAccessFile(%1$s, \"r\"); byte[] _b = new byte[(int)(%2$s)]; _r.readFully(_b); _r.close(); return new String(_b); } catch (Throwable _t) { return \"\"; } } }).f())",
                "read first %d chars of %s"));

        // 📁 File: записать в конец файла
        list.add(blk("sciFileAppendText", " ", "",
                "((new Object() { public void f() { try { java.io.FileWriter _w = new java.io.FileWriter(%1$s, true); _w.append(%2$s); _w.close(); } catch (Throwable _t) {} } }).f());",
                "append %s to file %s"));

        // 🌐 Network: скачать файл по URL
        list.add(blk("sciDownloadFile", " ", "",
                "((new Object() { public void f() { try { java.io.InputStream _is = new java.net.URL(%1$s).openStream(); java.io.FileOutputStream _os = new java.io.FileOutputStream(%2$s); byte[] _b = new byte[4096]; int _len; while ((_len = _is.read(_b)) > 0) _os.write(_b, 0, _len); _is.close(); _os.close(); } catch (Throwable _t) {} } }).f());",
                "download %s to %s"));

        // 🌐 Network: URL-кодировать
        list.add(blk("sciUrlEncode", "d", "",
                "((new Object() { public String f() { try { return java.net.URLEncoder.encode(%1$s, \"UTF-8\"); } catch (Throwable _t) { return %1$s; } } }).f())",
                "url encode %s"));

        // 🌐 Network: URL-декодировать
        list.add(blk("sciUrlDecode", "d", "",
                "((new Object() { public String f() { try { return java.net.URLDecoder.decode(%1$s, \"UTF-8\"); } catch (Throwable _t) { return %1$s; } } }).f())",
                "url decode %s"));

        // 🌐 Network: получить элемент JSON-массива
        list.add(blk("sciJsonArrayGet", "d", "",
                "((new Object() { public String f() { try { org.json.JSONArray _a = new org.json.JSONArray(%1$s); int _i = (int)(%2$s); return (_i >= 0 && _i < _a.length()) ? _a.getString(_i) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "json array %s get %d"));

        // 🌐 Network: JSON-массив → размер
        list.add(blk("sciJsonArrayLength", "d", "",
                "((new Object() { public double f() { try { return (double) new org.json.JSONArray(%1$s).length(); } catch (Throwable _t) { return 0.0; } } }).f())",
                "json array %s length"));

        // 🌐 Network: JSON-объект → число
        list.add(blk("sciJsonGetInt", "d", "",
                "((new Object() { public double f() { try { return new org.json.JSONObject(%1$s).optDouble(%2$s, 0.0); } catch (Throwable _t) { return 0.0; } } }).f())",
                "json get int %s from %s"));

        // 🌐 Network: JSON-объект → boolean
        list.add(blk("sciJsonGetBool", "b", "",
                "((new Object() { public boolean f() { try { return new org.json.JSONObject(%1$s).optBoolean(%2$s, false); } catch (Throwable _t) { return false; } } }).f())",
                "json get bool %s from %s"));

        // 🌐 Network: HTTP PUT
        list.add(blk("sciHttpPut", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"PUT\"); _c.setDoOutput(true); java.io.OutputStream _os = _c.getOutputStream(); _os.write(%2$s.getBytes(\"UTF-8\")); _os.flush(); _os.close(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http put %s data %s"));

        // 🌐 Network: HTTP DELETE
        list.add(blk("sciHttpDelete", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"DELETE\"); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http delete %s"));

        // 🌐 Network: код ответа HTTP
        list.add(blk("sciHttpGetResponseCode", "d", "",
                "((new Object() { public double f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"HEAD\"); return (double) _c.getResponseCode(); } catch (Throwable _t) { return 0.0; } } }).f())",
                "http response code for %s"));

        // 🌐 Network: валидный ли URL
        list.add(blk("sciIsUrlValid", "b", "",
                "((new Object() { public boolean f() { try { new java.net.URL(%1$s); return true; } catch (Throwable _t) { return false; } } }).f())",
                "url %s is valid"));

        // 🌐 Network: домен из URL
        list.add(blk("sciGetDomain", "d", "",
                "((new Object() { public String f() { try { return new java.net.URL(%1$s).getHost(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "domain of %s"));

        // 🌐 Network: параметр из URL
        list.add(blk("sciGetUrlParam", "d", "",
                "((new Object() { public String f() { try { String _q = new java.net.URL(%1$s).getQuery(); if (_q != null) { for (String _p : _q.split(\"&\")) { String[] _kv = _p.split(\"=\"); if (_kv.length == 2 && _kv[0].equals(%2$s)) return java.net.URLDecoder.decode(_kv[1], \"UTF-8\"); } } return \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "url param %s from %s"));

        // 🌐 Network: собрать URL с параметрами
        list.add(blk("sciBuildUrl", "d", "",
                "((new Object() { public String f() { try { if (!%1$s.contains(\"?\")) return %1$s + \"?\" + java.net.URLEncoder.encode(%2$s, \"UTF-8\") + \"=\" + java.net.URLEncoder.encode(%3$s, \"UTF-8\"); return %1$s + \"&\" + java.net.URLEncoder.encode(%2$s, \"UTF-8\") + \"=\" + java.net.URLEncoder.encode(%3$s, \"UTF-8\"); } catch (Throwable _t) { return %1$s; } } }).f())",
                "build url %s with %s=%s"));

        // 🌐 Network: POST JSON
        list.add(blk("sciHttpPostJson", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"POST\"); _c.setRequestProperty(\"Content-Type\", \"application/json\"); _c.setDoOutput(true); java.io.OutputStream _os = _c.getOutputStream(); _os.write(%2$s.getBytes(\"UTF-8\")); _os.flush(); _os.close(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http post json to %s data %s"));

        // 🌐 Network: GET с таймаутом
        list.add(blk("sciHttpGetTimeout", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setConnectTimeout((int)(%2$s)); _c.setReadTimeout((int)(%2$s)); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http get %s timeout %d ms"));

        // 🌐 Network: IP-адрес устройства
        list.add(blk("sciGetIpAddress", "d", "",
                "((new Object() { public String f() { try { java.util.Enumeration _e = java.net.NetworkInterface.getNetworkInterfaces(); while (_e.hasMoreElements()) { java.net.NetworkInterface _n = (java.net.NetworkInterface) _e.nextElement(); java.util.Enumeration _a = _n.getInetAddresses(); while (_a.hasMoreElements()) { java.net.InetAddress _i = (java.net.InetAddress) _a.nextElement(); if (!_i.isLoopbackAddress() && _i instanceof java.net.Inet4Address) return _i.getHostAddress(); } } return \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "device ip address"));

        // 🌐 Network: пинг хоста
        list.add(blk("sciPingHost", "d", "",
                "((new Object() { public double f() { try { java.net.InetAddress _a = java.net.InetAddress.getByName(%1$s); long _s = System.currentTimeMillis(); _a.isReachable(3000); return (double)(System.currentTimeMillis() - _s); } catch (Throwable _t) { return -1.0; } } }).f())",
                "ping %s"));

        // 🎵 Media: воспроизвести звук (ресурс)
        list.add(blk("sciPlaySoundRes", " ", "",
                "android.media.MediaPlayer.create(%2$s, %1$s).start();",
                "play sound resource %d from %m.context"));

        // 🎵 Media: остановить все звуки
        list.add(blk("sciStopAllSounds", " ", "",
                "((android.media.AudioManager) %1$s.getSystemService(android.content.Context.AUDIO_SERVICE)).abandonAudioFocus(null);",
                "stop all sounds from %m.context"));

        // 🎵 Media: длительность аудио (мс)
        list.add(blk("sciAudioDuration", "d", "",
                "((new Object() { public double f() { try { android.media.MediaPlayer _mp = new android.media.MediaPlayer(); _mp.setDataSource(%1$s); _mp.prepare(); double _d = (double) _mp.getDuration(); _mp.release(); return _d; } catch (Throwable _t) { return 0.0; } } }).f())",
                "duration of audio %s"));

        // 📷 Camera: открыть камеру
        list.add(blk("sciOpenCamera", " ", "",
                "%1$s.startActivity(new android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE));",
                "open camera from %m.context"));

        // 📷 Camera: сделать фото (Intent)
        list.add(blk("sciTakePhoto", " ", "",
                "%2$s.startActivityForResult(new android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), (int)(%1$s));",
                "take photo request %d from %m.activity"));

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
                "vibrate pattern %d from %m.context"));

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
                "start recording to %s"));

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

        // 📁 File: прочитать байты
        list.add(blk("sciFileReadBytes", "d", "",
                "((new Object() { public String f() { try { java.io.FileInputStream _is = new java.io.FileInputStream(%1$s); byte[] _b = new byte[(int) new java.io.File(%1$s).length()]; _is.read(_b); _is.close(); return new String(_b); } catch (Throwable _t) { return \"\"; } } }).f())",
                "read bytes of %s"));

        // 📁 File: записать байты
        list.add(blk("sciFileWriteBytes", " ", "",
                "((new Object() { public void f() { try { java.io.FileOutputStream _os = new java.io.FileOutputStream(%1$s); _os.write(%2$s.getBytes()); _os.close(); } catch (Throwable _t) {} } }).f());",
                "write bytes %s to %s"));

        // 📁 File: количество строк в файле
        list.add(blk("sciFileLineCount", "d", "",
                "((new Object() { public double f() { try { java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.FileReader(%1$s)); int _c = 0; while (_r.readLine() != null) _c++; _r.close(); return (double) _c; } catch (Throwable _t) { return 0.0; } } }).f())",
                "line count of %s"));

        // 📁 File: хеш файла (MD5)
        list.add(blk("sciFileHash", "d", "",
                "((new Object() { public String f() { try { java.io.FileInputStream _is = new java.io.FileInputStream(%1$s); java.security.MessageDigest _md = java.security.MessageDigest.getInstance(\"MD5\"); byte[] _buf = new byte[4096]; int _len; while ((_len = _is.read(_buf)) > 0) _md.update(_buf, 0, _len); _is.close(); byte[] _d = _md.digest(); StringBuilder _sb = new StringBuilder(); for (byte _x : _d) _sb.append(String.format(\"%02x\", _x)); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "file hash of %s"));

        // 📁 File: кодировка файла (UTF-8 / ASCII)
        list.add(blk("sciFileEncoding", "d", "",
                "((new Object() { public String f() { try { byte[] _b = new byte[4]; java.io.FileInputStream _is = new java.io.FileInputStream(%1$s); _is.read(_b); _is.close(); if ((_b[0] & 0xFF) == 0xEF && (_b[1] & 0xFF) == 0xBB && (_b[2] & 0xFF) == 0xBF) return \"UTF-8 (BOM)\"; return \"UTF-8\"; } catch (Throwable _t) { return \"unknown\"; } } }).f())",
                "encoding of %s"));

        // 📁 File: разделить файл на части
        list.add(blk("sciFileSplit", " ", "",
                "((new Object() { public void f() { try { java.io.File _f = new java.io.File(%1$s); long _size = _f.length(); int _parts = (int)(%2$s); long _chunk = _size / _parts; java.io.FileInputStream _is = new java.io.FileInputStream(_f); for (int _i = 0; _i < _parts; _i++) { byte[] _b = new byte[(int) _chunk]; _is.read(_b); java.io.FileOutputStream _os = new java.io.FileOutputStream(%1$s + \".part\" + _i); _os.write(_b); _os.close(); } _is.close(); } catch (Throwable _t) {} } }).f());",
                "split %s into %d parts"));

        // 📁 File: объединить части
        list.add(blk("sciFileMerge", " ", "",
                "((new Object() { public void f() { try { java.io.FileOutputStream _os = new java.io.FileOutputStream(%2$s); for (int _i = 0; _i < (int)(%3$s); _i++) { java.io.FileInputStream _is = new java.io.FileInputStream(%1$s + \".part\" + _i); byte[] _b = new byte[4096]; int _len; while ((_len = _is.read(_b)) > 0) _os.write(_b, 0, _len); _is.close(); } _os.close(); } catch (Throwable _t) {} } }).f());",
                "merge %s parts into %s count %d"));

        // 📁 File: сжать в GZIP
        list.add(blk("sciFileGzip", " ", "",
                "((new Object() { public void f() { try { java.io.FileInputStream _is = new java.io.FileInputStream(%1$s); java.util.zip.GZIPOutputStream _os = new java.util.zip.GZIPOutputStream(new java.io.FileOutputStream(%2$s)); byte[] _b = new byte[4096]; int _len; while ((_len = _is.read(_b)) > 0) _os.write(_b, 0, _len); _is.close(); _os.close(); } catch (Throwable _t) {} } }).f());",
                "gzip %s to %s"));

        // 📁 File: разжать GZIP
        list.add(blk("sciFileGunzip", " ", "",
                "((new Object() { public void f() { try { java.util.zip.GZIPInputStream _is = new java.util.zip.GZIPInputStream(new java.io.FileInputStream(%1$s)); java.io.FileOutputStream _os = new java.io.FileOutputStream(%2$s); byte[] _b = new byte[4096]; int _len; while ((_len = _is.read(_b)) > 0) _os.write(_b, 0, _len); _is.close(); _os.close(); } catch (Throwable _t) {} } }).f());",
                "gunzip %s to %s"));

        // 📁 File: права доступа (читаемый)
        list.add(blk("sciFileCanRead", "b", "",
                "new java.io.File(%1$s).canRead()",
                "file %s is readable"));

        // 📁 File: права доступа (записываемый)
        list.add(blk("sciFileCanWrite", "b", "",
                "new java.io.File(%1$s).canWrite()",
                "file %s is writable"));

        // 📁 File: канонический путь
        list.add(blk("sciFileCanonicalPath", "d", "",
                "((new Object() { public String f() { try { return new java.io.File(%1$s).getCanonicalPath(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "canonical path of %s"));

        // 📁 File: свободное место (КБ)
        list.add(blk("sciFileFreeSpaceKB", "d", "",
                "(double)(new java.io.File(%1$s).getFreeSpace() / 1024)",
                "free space KB of %s"));

        // 📁 File: общее место (КБ)
        list.add(blk("sciFileTotalSpaceKB", "d", "",
                "(double)(new java.io.File(%1$s).getTotalSpace() / 1024)",
                "total space KB of %s"));

        // 📁 File: скопировать папку (только файлы)
        list.add(blk("sciFileCopyDir", " ", "",
                "((new Object() { public void f() { try { java.io.File _src = new java.io.File(%1$s); java.io.File _dst = new java.io.File(%2$s); _dst.mkdirs(); java.io.File[] _fs = _src.listFiles(); if (_fs != null) { for (java.io.File _x : _fs) { if (_x.isFile()) { java.io.FileInputStream _is = new java.io.FileInputStream(_x); java.io.FileOutputStream _os = new java.io.FileOutputStream(new java.io.File(_dst, _x.getName())); byte[] _b = new byte[4096]; int _len; while ((_len = _is.read(_b)) > 0) _os.write(_b, 0, _len); _is.close(); _os.close(); } } } } catch (Throwable _t) {} } }).f());",
                "copy dir %s to %s"));

        // 📁 File: переместить папку
        list.add(blk("sciFileMoveDir", " ", "",
                "((new Object() { public void f() { try { new java.io.File(%1$s).renameTo(new java.io.File(%2$s)); } catch (Throwable _t) {} } }).f());",
                "move dir %s to %s"));

        // 📁 File: размер файла в байтах
        list.add(blk("sciFileSizeBytes", "d", "",
                "(double)(new java.io.File(%1$s).length())",
                "size bytes of %s"));

        // 📁 File: путь к папке загрузок
        list.add(blk("sciDownloadDir", "d", "",
                "android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()",
                "download directory path"));

        // 📁 File: путь к папке документов
        list.add(blk("sciDocumentsDir", "d", "",
                "android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOCUMENTS).getAbsolutePath()",
                "documents directory path"));

        // 📁 File: путь к папке изображений
        list.add(blk("sciPicturesDir", "d", "",
                "android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_PICTURES).getAbsolutePath()",
                "pictures directory path"));

        // 🌐 Network: HTTP PATCH
        list.add(blk("sciHttpPatch", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"PATCH\"); _c.setDoOutput(true); java.io.OutputStream _os = _c.getOutputStream(); _os.write(%2$s.getBytes(\"UTF-8\")); _os.flush(); _os.close(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http patch %s data %s"));

        // 🌐 Network: HTTP HEAD
        list.add(blk("sciHttpHead", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"HEAD\"); StringBuilder _sb = new StringBuilder(); for (java.util.Map.Entry<String, java.util.List<String>> _e : _c.getHeaderFields().entrySet()) { _sb.append(_e.getKey()).append(\": \").append(_e.getValue()).append(\"\\n\"); } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http head %s"));

        // 🌐 Network: скачать с прогрессом (в файл)
        list.add(blk("sciDownloadWithProgress", " ", "",
                "((new Object() { public void f() { try { java.net.URL _u = new java.net.URL(%1$s); java.net.HttpURLConnection _c = (java.net.HttpURLConnection) _u.openConnection(); _c.connect(); int _total = _c.getContentLength(); java.io.InputStream _is = _c.getInputStream(); java.io.FileOutputStream _os = new java.io.FileOutputStream(%2$s); byte[] _b = new byte[4096]; int _len; long _down = 0; while ((_len = _is.read(_b)) > 0) { _os.write(_b, 0, _len); _down += _len; } _is.close(); _os.close(); } catch (Throwable _t) {} } }).f());",
                "download with progress %s to %s"));

        // 🌐 Network: проверить SSL (валидный ли сертификат)
        list.add(blk("sciCheckSsl", "b", "",
                "((new Object() { public boolean f() { try { javax.net.ssl.HttpsURLConnection _c = (javax.net.ssl.HttpsURLConnection) new java.net.URL(%1$s).openConnection(); _c.connect(); return true; } catch (Throwable _t) { return false; } } }).f())",
                "ssl valid for %s"));

        // 🌐 Network: заголовок ответа
        list.add(blk("sciGetResponseHeader", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); return _c.getHeaderField(%2$s); } catch (Throwable _t) { return \"\"; } } }).f())",
                "response header %s of %s"));

        // 🌐 Network: WebSocket подключение (упрощённо)
        list.add(blk("sciWebSocketConnect", " ", "",
                "try { new java.net.Socket(%1$s, (int)(%2$s)).close(); } catch (Throwable _t) {}",
                "websocket connect %s port %d"));

        // 🌐 Network: ping с таймаутом
        list.add(blk("sciPingTimeout", "d", "",
                "((new Object() { public double f() { try { java.net.InetAddress _a = java.net.InetAddress.getByName(%1$s); long _s = System.currentTimeMillis(); if (_a.isReachable((int)(%2$s))) return (double)(System.currentTimeMillis() - _s); return -1.0; } catch (Throwable _t) { return -1.0; } } }).f())",
                "ping %s timeout %d ms"));

        // 🌐 Network: получить все заголовки ответа
        list.add(blk("sciGetAllHeaders", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"HEAD\"); StringBuilder _sb = new StringBuilder(); for (java.util.Map.Entry<String, java.util.List<String>> _e : _c.getHeaderFields().entrySet()) { _sb.append(_e.getKey()).append(\"=\").append(_e.getValue()).append(\";\"); } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "all headers of %s"));

        // 🌐 Network: отправить форму (POST)
        list.add(blk("sciHttpPostForm", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"POST\"); _c.setDoOutput(true); _c.setRequestProperty(\"Content-Type\", \"application/x-www-form-urlencoded\"); java.io.OutputStream _os = _c.getOutputStream(); _os.write(%2$s.getBytes(\"UTF-8\")); _os.flush(); _os.close(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http post form %s data %s"));

        // 🌐 Network: базовая аутентификация
        list.add(blk("sciHttpBasicAuth", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); String _auth = android.util.Base64.encodeToString((%2$s + \":\" + %3$s).getBytes(), android.util.Base64.NO_WRAP); _c.setRequestProperty(\"Authorization\", \"Basic \" + _auth); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http basic auth %s user %s pass %s"));

        // 🌐 Network: следовать редиректам
        list.add(blk("sciHttpFollowRedirects", " ", "",
                "java.net.HttpURLConnection.setFollowRedirects(%1$s);",
                "follow redirects %b"));

        // 🌐 Network: JSON из ответа (получить поле)
        list.add(blk("sciHttpJsonField", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return new org.json.JSONObject(_sb.toString()).optString(%2$s); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http json field %s from %s"));

        // 🌐 Network: проверить доступность URL (HEAD)
        list.add(blk("sciUrlAvailable", "b", "",
                "((new Object() { public boolean f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"HEAD\"); return _c.getResponseCode() >= 200 && _c.getResponseCode() < 400; } catch (Throwable _t) { return false; } } }).f())",
                "url %s is available"));

        // 🌐 Network: получить размер файла по URL
        list.add(blk("sciUrlFileSize", "d", "",
                "((new Object() { public double f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"HEAD\"); return (double) _c.getContentLength(); } catch (Throwable _t) { return -1.0; } } }).f())",
                "file size at %s"));

        // 🌐 Network: получить тип контента
        list.add(blk("sciUrlContentType", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"HEAD\"); return _c.getContentType(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "content type of %s"));

        // 🌐 Network: URI-кодирование (пробелы в %20)
        list.add(blk("sciUrlEncodeSpace", "d", "",
                "((new Object() { public String f() { try { return java.net.URLEncoder.encode(%1$s, \"UTF-8\").replace(\"+\", \"%20\"); } catch (Throwable _t) { return %1$s; } } }).f())",
                "url encode space %s"));

        // 🌐 Network: URI-декодирование (плюсы в пробелы)
        list.add(blk("sciUrlDecodePlus", "d", "",
                "((new Object() { public String f() { try { return java.net.URLDecoder.decode(%1$s.replace(\"+\", \"%20\"), \"UTF-8\"); } catch (Throwable _t) { return %1$s; } } }).f())",
                "url decode plus %s"));

        // 🌐 Network: кодировать параметры URL
        list.add(blk("sciUrlEncodeParams", "d", "",
                "((new Object() { public String f() { try { StringBuilder _sb = new StringBuilder(); String[] _parts = %1$s.split(\"&\"); for (String _p : _parts) { String[] _kv = _p.split(\"=\"); if (_kv.length == 2) _sb.append(java.net.URLEncoder.encode(_kv[0], \"UTF-8\")).append(\"=\").append(java.net.URLEncoder.encode(_kv[1], \"UTF-8\")).append(\"&\"); } return _sb.toString(); } catch (Throwable _t) { return %1$s; } } }).f())",
                "url encode params %s"));

        // 🌐 Network: получить заголовок User-Agent
        list.add(blk("sciUserAgent", "d", "",
                "System.getProperty(\"http.agent\")",
                "user agent"));

        // 🌐 Network: установить таймаут по умолчанию
        list.add(blk("sciSetTimeout", " ", "",
                "java.net.HttpURLConnection.setDefaultReadTimeout((int)(%1$s));",
                "set default timeout %d ms"));

        // 📝 Text: количество слов
        list.add(blk("sciTextWordCount", "d", "",
                "(double)(%1$s.trim().isEmpty() ? 0 : %1$s.trim().split(\"\\\\s+\").length)",
                "word count of %s"));

        // 📝 Text: количество предложений
        list.add(blk("sciTextSentenceCount", "d", "",
                "(double)(%1$s.split(\"[.!?]+\").length)",
                "sentence count of %s"));

        // 📝 Text: обрезать до N символов
        list.add(blk("sciTextTruncate", "d", "",
                "((new Object() { public String f() { try { int _n = (int)(%2$s); return %1$s.length() > _n ? %1$s.substring(0, _n) + \"...\" : %1$s; } catch (Throwable _t) { return %1$s; } } }).f())",
                "truncate %s to %d chars"));

        // 📝 Text: перенос строк по N символов
        list.add(blk("sciTextWrap", "d", "",
                "((new Object() { public String f() { try { StringBuilder _sb = new StringBuilder(); int _n = (int)(%2$s); for (int _i = 0; _i < %1$s.length(); _i += _n) { _sb.append(%1$s.substring(_i, Math.min(_i + _n, %1$s.length()))).append(\"\\n\"); } return _sb.toString(); } catch (Throwable _t) { return %1$s; } } }).f())",
                "wrap %s by %d chars"));

        // 📝 Text: добавить отступы
        list.add(blk("sciTextIndent", "d", "",
                "((new Object() { public String f() { try { StringBuilder _sb = new StringBuilder(); for (String _l : %1$s.split(\"\\n\")) { _sb.append(%2$s).append(_l).append(\"\\n\"); } return _sb.toString(); } catch (Throwable _t) { return %1$s; } } }).f())",
                "indent %s with %s"));

        // 📝 Text: убрать HTML-теги
        list.add(blk("sciTextStripHtml", "d", "",
                "%1$s.replaceAll(\"<[^>]*>\", \"\")",
                "strip html from %s"));

        // 📝 Text: экранировать HTML
        list.add(blk("sciTextEscapeHtml", "d", "",
                "android.text.TextUtils.htmlEncode(%1$s)",
                "escape html in %s"));

        // 📝 Text: убрать экранирование HTML
        list.add(blk("sciTextUnescapeHtml", "d", "",
                "android.text.Html.fromHtml(%1$s).toString()",
                "unescape html in %s"));

        // 📝 Text: сравнить без учёта регистра
        list.add(blk("sciTextEqualsIgnoreCase", "b", "",
                "%1$s.equalsIgnoreCase(%2$s)",
                "%s equals ignore case %s"));

        // 📝 Text: только цифры?
        list.add(blk("sciTextIsNumeric", "b", "",
                "%1$s.matches(\"\\\\d+\")",
                "%s is numeric"));

        // 📝 Text: только буквы?
        list.add(blk("sciTextIsAlpha", "b", "",
                "%1$s.matches(\"[a-zA-Z]+\")",
                "%s is alpha"));

        // 📝 Text: только буквы и цифры?
        list.add(blk("sciTextIsAlphanumeric", "b", "",
                "%1$s.matches(\"[a-zA-Z0-9]+\")",
                "%s is alphanumeric"));

        // 📝 Text: заменить первое вхождение
        list.add(blk("sciTextReplaceFirst", "d", "",
                "%1$s.replaceFirst(%2$s, %3$s)",
                "replace first %s with %s in %s"));

        // 📝 Text: удалить все пробелы (включая табы)
        list.add(blk("sciTextRemoveAllSpaces", "d", "",
                "%1$s.replaceAll(\"\\\\s+\", \"\")",
                "remove all spaces from %s"));

        // 📝 Text: нормализовать пробелы (множественные в один)
        list.add(blk("sciTextNormalizeSpaces", "d", "",
                "%1$s.replaceAll(\"\\\\s+\", \" \").trim()",
                "normalize spaces in %s"));

        // 📝 Text: первая буква заглавная
        list.add(blk("sciTextFirstUpper", "d", "",
                "((new Object() { public String f() { try { return %1$s.isEmpty() ? %1$s : Character.toUpperCase(%1$s.charAt(0)) + %1$s.substring(1); } catch (Throwable _t) { return %1$s; } } }).f())",
                "first letter upper of %s"));

        // 📝 Text: последняя буква заглавная
        list.add(blk("sciTextLastUpper", "d", "",
                "((new Object() { public String f() { try { return %1$s.isEmpty() ? %1$s : %1$s.substring(0, %1$s.length() - 1) + Character.toUpperCase(%1$s.charAt(%1$s.length() - 1)); } catch (Throwable _t) { return %1$s; } } }).f())",
                "last letter upper of %s"));

        // 📝 Text: поменять регистр (инвертировать)
        list.add(blk("sciTextSwapCase", "d", "",
                "((new Object() { public String f() { try { StringBuilder _sb = new StringBuilder(); for (char _c : %1$s.toCharArray()) { _sb.append(Character.isUpperCase(_c) ? Character.toLowerCase(_c) : Character.toUpperCase(_c)); } return _sb.toString(); } catch (Throwable _t) { return %1$s; } } }).f())",
                "swap case of %s"));

        // 📝 Text: убрать символы (оставить буквы и цифры)
        list.add(blk("sciTextKeepAlphanumeric", "d", "",
                "%1$s.replaceAll(\"[^a-zA-Z0-9]\", \"\")",
                "keep alphanumeric in %s"));

        // 📝 Text: удалить дубликаты строк
        list.add(blk("sciTextUniqueLines", "d", "",
                "((new Object() { public String f() { try { java.util.LinkedHashSet _s = new java.util.LinkedHashSet(java.util.Arrays.asList(%1$s.split(\"\\n\"))); StringBuilder _sb = new StringBuilder(); for (Object _o : _s) _sb.append(_o).append(\"\\n\"); return _sb.toString().trim(); } catch (Throwable _t) { return %1$s; } } }).f())",
                "unique lines of %s"));

        // 📊 List: получить случайный элемент
        list.add(blk("sciListRandom", "d", "",
                "((new Object() { public String f() { try { return %1$s.isEmpty() ? \"\" : String.valueOf(%1$s.get(new java.util.Random().nextInt(%1$s.size()))); } catch (Throwable _t) { return \"\"; } } }).f())",
                "random from %s"));

        // 📊 List: получить среднее значение (для чисел)
        list.add(blk("sciListAvg", "d", "",
                "((new Object() { public double f() { try { double _s = 0; for (Object _o : %1$s) _s += Double.parseDouble(String.valueOf(_o)); return %1$s.isEmpty() ? 0.0 : _s / %1$s.size(); } catch (Throwable _t) { return 0.0; } } }).f())",
                "average of %s"));

        // 📊 List: сумма значений (для чисел)
        list.add(blk("sciListSum", "d", "",
                "((new Object() { public double f() { try { double _s = 0; for (Object _o : %1$s) _s += Double.parseDouble(String.valueOf(_o)); return _s; } catch (Throwable _t) { return 0.0; } } }).f())",
                "sum of %s"));

        // 📊 List: медиана (для чисел)
        list.add(blk("sciListMedian", "d", "",
                "((new Object() { public double f() { try { java.util.ArrayList _l = new java.util.ArrayList(%1$s); java.util.Collections.sort(_l); int _n = _l.size(); if (_n == 0) return 0.0; if (_n % 2 == 0) return (Double.parseDouble(String.valueOf(_l.get(_n/2 - 1))) + Double.parseDouble(String.valueOf(_l.get(_n/2)))) / 2.0; return Double.parseDouble(String.valueOf(_l.get(_n/2))); } catch (Throwable _t) { return 0.0; } } }).f())",
                "median of %s"));

        // 📊 List: режим (самый частый элемент)
        list.add(blk("sciListMode", "d", "",
                "((new Object() { public String f() { try { java.util.HashMap _m = new java.util.HashMap(); for (Object _o : %1$s) _m.put(_o, _m.containsKey(_o) ? (int)_m.get(_o) + 1 : 1); Object _best = null; int _max = 0; for (Object _k : _m.keySet()) { int _v = (int)_m.get(_k); if (_v > _max) { _max = _v; _best = _k; } } return String.valueOf(_best); } catch (Throwable _t) { return \"\"; } } }).f())",
                "mode of %s"));

        // 📊 List: только уникальные (по значению)
        list.add(blk("sciListUniqueValues", "d", "",
                "((new Object() { public String f() { try { java.util.LinkedHashSet _s = new java.util.LinkedHashSet(%1$s); StringBuilder _sb = new StringBuilder(); for (Object _o : _s) _sb.append(_o).append(\",\"); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "unique values of %s"));

        // 📊 List: найти все индексы элемента
        list.add(blk("sciListAllIndexes", "d", "",
                "((new Object() { public String f() { try { StringBuilder _sb = new StringBuilder(); for (int _i = 0; _i < %1$s.size(); _i++) { if (%1$s.get(_i).equals(%2$s)) _sb.append(_i).append(\",\"); } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "all indexes of %s in %s"));

        // 📊 List: удалить пустые элементы
        list.add(blk("sciListRemoveEmpty", " ", "",
                "((new Object() { public void f() { try { java.util.ArrayList _tmp = new java.util.ArrayList(); for (Object _o : %1$s) { if (_o != null && !String.valueOf(_o).trim().isEmpty()) _tmp.add(_o); } %1$s.clear(); %1$s.addAll(_tmp); } catch (Throwable _t) {} } }).f());",
                "remove empty from %s"));

        // 📊 List: заменить все вхождения
        list.add(blk("sciListReplaceAll", " ", "",
                "((new Object() { public void f() { try { java.util.ArrayList _tmp = new java.util.ArrayList(); for (Object _o : %1$s) { _tmp.add(_o.equals(%2$s) ? %3$s : _o); } %1$s.clear(); %1$s.addAll(_tmp); } catch (Throwable _t) {} } }).f());",
                "replace all %s with %s in %s"));

        // 📊 List: обрезать до N элементов
        list.add(blk("sciListTake", " ", "",
                "((new Object() { public void f() { try { while (%1$s.size() > (int)(%2$s)) %1$s.remove(%1$s.size() - 1); } catch (Throwable _t) {} } }).f());",
                "take first %d from %s"));

        // 📊 List: пропустить первые N элементов
        list.add(blk("sciListDrop", " ", "",
                "((new Object() { public void f() { try { for (int _i = 0; _i < (int)(%2$s) && !%1$s.isEmpty(); _i++) %1$s.remove(0); } catch (Throwable _t) {} } }).f());",
                "drop first %d from %s"));

        // 📊 List: проверить, все ли элементы true
        list.add(blk("sciListAllTrue", "b", "",
                "((new Object() { public boolean f() { try { for (Object _o : %1$s) { if (!Boolean.parseBoolean(String.valueOf(_o))) return false; } return true; } catch (Throwable _t) { return false; } } }).f())",
                "all true in %s"));

        // 📊 List: проверить, есть ли хотя бы один true
        list.add(blk("sciListAnyTrue", "b", "",
                "((new Object() { public boolean f() { try { for (Object _o : %1$s) { if (Boolean.parseBoolean(String.valueOf(_o))) return true; } return false; } catch (Throwable _t) { return false; } } }).f())",
                "any true in %s"));

        // 📊 List: проверить, все ли false
        list.add(blk("sciListNoneTrue", "b", "",
                "((new Object() { public boolean f() { try { for (Object _o : %1$s) { if (Boolean.parseBoolean(String.valueOf(_o))) return false; } return true; } catch (Throwable _t) { return false; } } }).f())",
                "none true in %s"));

        // 📊 List: разбить на группы по N
        list.add(blk("sciListChunk", " ", "",
                "((new Object() { public void f() { try { int _n = (int)(%2$s); java.util.ArrayList _tmp = new java.util.ArrayList(); for (int _i = 0; _i < %1$s.size(); _i += _n) _tmp.add(new java.util.ArrayList(%1$s.subList(_i, Math.min(_i + _n, %1$s.size())))); %1$s.clear(); %1$s.addAll(_tmp); } catch (Throwable _t) {} } }).f());",
                "chunk %s by %d"));

        // 📊 List: пересечение списков
        list.add(blk("sciListIntersect", " ", "",
                "((new Object() { public void f() { try { java.util.ArrayList _tmp = new java.util.ArrayList(%1$s); _tmp.retainAll(%2$s); %1$s.clear(); %1$s.addAll(_tmp); } catch (Throwable _t) {} } }).f());",
                "intersect %s with %s"));

        // 📊 List: объединение (без дублей)
        list.add(blk("sciListUnion", " ", "",
                "((new Object() { public void f() { try { java.util.LinkedHashSet _s = new java.util.LinkedHashSet(%1$s); _s.addAll(%2$s); %1$s.clear(); %1$s.addAll(_s); } catch (Throwable _t) {} } }).f());",
                "union %s and %s"));

        // 📊 List: разница (в %1$s, но не в %2$s)
        list.add(blk("sciListDifference", " ", "",
                "((new Object() { public void f() { try { java.util.ArrayList _tmp = new java.util.ArrayList(%1$s); _tmp.removeAll(%2$s); %1$s.clear(); %1$s.addAll(_tmp); } catch (Throwable _t) {} } }).f());",
                "difference %s minus %s"));

        // 📊 List: симметричная разница
        list.add(blk("sciListSymmetricDiff", " ", "",
                "((new Object() { public void f() { try { java.util.LinkedHashSet _a = new java.util.LinkedHashSet(%1$s); java.util.LinkedHashSet _b = new java.util.LinkedHashSet(%2$s); java.util.LinkedHashSet _u = new java.util.LinkedHashSet(_a); _u.addAll(_b); java.util.LinkedHashSet _i = new java.util.LinkedHashSet(_a); _i.retainAll(_b); _u.removeAll(_i); %1$s.clear(); %1$s.addAll(_u); } catch (Throwable _t) {} } }).f());",
                "symmetric difference %s and %s"));

        // 🎨 UI: шрифт (жирный/обычный)
        list.add(blk("sciSetBold", " ", "",
                "%1$s.setTypeface(null, (%2$s) ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL);",
                "set bold %b on %s"));

        // 🎨 UI: курсив
        list.add(blk("sciSetItalic", " ", "",
                "%1$s.setTypeface(null, (%2$s) ? android.graphics.Typeface.ITALIC : android.graphics.Typeface.NORMAL);",
                "set italic %b on %s"));

        // 🎨 UI: межбуквенный интервал
        list.add(blk("sciSetLetterSpacing", " ", "",
                "%1$s.setLetterSpacing((float)(%2$s));",
                "letter spacing %s on %s"));

        // 🎨 UI: межстрочный интервал
        list.add(blk("sciSetLineSpacing", " ", "",
                "%1$s.setLineSpacing((float)(%2$s), 1.0f);",
                "line spacing %s on %s"));

        // 🎨 UI: тень текста
        list.add(blk("sciSetShadow", " ", "",
                "%1$s.setShadowLayer((float)(%1$s), (float)(%3$s), (float)(%4$s), (int)(%2$s));",
                "shadow radius %s color %d on %s offset %s"));

        // 🎨 UI: гравитация (внутри контейнера)
        list.add(blk("sciSetGravity", " ", "",
                "%1$s.setGravity((int)(%2$s));",
                "set gravity %d on %s"));

        // 🎨 UI: минимальная ширина
        list.add(blk("sciSetMinWidth", " ", "",
                "%1$s.setMinimumWidth((int)(%2$s));",
                "min width %d on %s"));

        // 🎨 UI: максимальная ширина
        list.add(blk("sciSetMaxWidth", " ", "",
                "%1$s.setMaxWidth((int)(%2$s));",
                "max width %d on %s"));

        // 🎨 UI: минимальная высота
        list.add(blk("sciSetMinHeight", " ", "",
                "%1$s.setMinimumHeight((int)(%2$s));",
                "min height %d on %s"));

        // 🎨 UI: максимальная высота
        list.add(blk("sciSetMaxHeight", " ", "",
                "%1$s.setMaxHeight((int)(%2$s));",
                "max height %d on %s"));

        // 🎨 UI: прозрачность (0-255)
        list.add(blk("sciSetAlpha", " ", "",
                "%1$s.setAlpha((float)(%2$s) / 255.0f);",
                "set alpha %d on %s"));

        // 🎨 UI: цвет текста
        list.add(blk("sciSetTextColor", " ", "",
                "%1$s.setTextColor((int)(%2$s));",
                "set text color %d on %s"));

        // 🎨 UI: размер текста (sp)
        list.add(blk("sciSetTextSizeSp", " ", "",
                "%1$s.setTextSize((float)(%2$s));",
                "set text size %s sp on %s"));

        // 🎨 UI: фон (drawable)
        list.add(blk("sciSetBackground", " ", "",
                "%1$s.setBackgroundResource((int)(%2$s));",
                "set background resource %d on %s"));

        // 🎨 UI: отступы (margin) через LayoutParams
        list.add(blk("sciSetMargin", " ", "",
                "((new Object() { public void f() { try { android.view.ViewGroup.MarginLayoutParams _p = (android.view.ViewGroup.MarginLayoutParams) %1$s.getLayoutParams(); _p.setMargins((int)(%2$s), (int)(%3$s), (int)(%4$s), (int)(%5$s)); %1$s.setLayoutParams(_p); } catch (Throwable _t) {} } }).f());",
                "set margin %s left %d top %d right %d bottom %d"));

        // 🎨 UI: ширина (LayoutParams)
        list.add(blk("sciSetWidth", " ", "",
                "((new Object() { public void f() { try { android.view.ViewGroup.LayoutParams _p = %1$s.getLayoutParams(); _p.width = (int)(%2$s); %1$s.setLayoutParams(_p); } catch (Throwable _t) {} } }).f());",
                "set width %d on %s"));

        // 🎨 UI: высота (LayoutParams)
        list.add(blk("sciSetHeight", " ", "",
                "((new Object() { public void f() { try { android.view.ViewGroup.LayoutParams _p = %1$s.getLayoutParams(); _p.height = (int)(%2$s); %1$s.setLayoutParams(_p); } catch (Throwable _t) {} } }).f());",
                "set height %d on %s"));

        // 🎨 UI: вес (weight) в LinearLayout
        list.add(blk("sciSetWeight", " ", "",
                "((new Object() { public void f() { try { android.widget.LinearLayout.LayoutParams _p = (android.widget.LinearLayout.LayoutParams) %1$s.getLayoutParams(); _p.weight = (float)(%2$s); %1$s.setLayoutParams(_p); } catch (Throwable _t) {} } }).f());",
                "set weight %s on %s"));

        // 🎨 UI: показать клавиатуру
        list.add(blk("sciShowKeyboard", " ", "",
                "((android.view.inputmethod.InputMethodManager) %2$s.getSystemService(android.content.Context.INPUT_METHOD_SERVICE)).showSoftInput(%1$s, 0);",
                "show keyboard for from %m.context"));

        // 🎨 UI: скрыть клавиатуру
        list.add(blk("sciHideKeyboard", " ", "",
                "((android.view.inputmethod.InputMethodManager) %2$s.getSystemService(android.content.Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(%1$s.getWindowToken(), 0);",
                "hide keyboard from via %m.context"));

        // 📱 Device: количество ядер CPU
        list.add(blk("sciCpuCores", "d", "",
                "(double) Runtime.getRuntime().availableProcessors()",
                "cpu cores"));

        // 📱 Device: общая оперативная память (МБ)
        list.add(blk("sciRamTotal", "d", "",
                "((new Object() { public double f() { try { android.app.ActivityManager _am = (android.app.ActivityManager) %1$s.getSystemService(android.content.Context.ACTIVITY_SERVICE); android.app.ActivityManager.MemoryInfo _mi = new android.app.ActivityManager.MemoryInfo(); _am.getMemoryInfo(_mi); return (double) (_mi.totalMem / (1024 * 1024)); } catch (Throwable _t) { return 0.0; } } }).f())",
                "ram total MB from %m.context"));

        // 📱 Device: свободная оперативная память (МБ)
        list.add(blk("sciRamFree", "d", "",
                "((new Object() { public double f() { try { android.app.ActivityManager _am = (android.app.ActivityManager) %1$s.getSystemService(android.content.Context.ACTIVITY_SERVICE); android.app.ActivityManager.MemoryInfo _mi = new android.app.ActivityManager.MemoryInfo(); _am.getMemoryInfo(_mi); return (double) (_mi.availMem / (1024 * 1024)); } catch (Throwable _t) { return 0.0; } } }).f())",
                "ram free MB from %m.context"));

        // 📱 Device: заряд батареи (уровень)
        list.add(blk("sciBatteryPercent", "d", "",
                "((new Object() { public double f() { try { android.content.Intent _i = %1$s.registerReceiver(null, new android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED)); int _l = _i.getIntExtra(android.os.BatteryManager.EXTRA_LEVEL, -1); int _s = _i.getIntExtra(android.os.BatteryManager.EXTRA_SCALE, -1); return (double)(_l * 100 / (float)_s); } catch (Throwable _t) { return 0.0; } } }).f())",
                "battery percent from %m.context"));

        // 📱 Device: батарея заряжается?
        list.add(blk("sciBatteryCharging", "b", "",
                "((new Object() { public boolean f() { try { android.content.Intent _i = %1$s.registerReceiver(null, new android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED)); int _s = _i.getIntExtra(android.os.BatteryManager.EXTRA_STATUS, -1); return _s == android.os.BatteryManager.BATTERY_STATUS_CHARGING || _s == android.os.BatteryManager.BATTERY_STATUS_FULL; } catch (Throwable _t) { return false; } } }).f())",
                "battery charging from %m.context"));

        // 📱 Device: серийный номер
        list.add(blk("sciDeviceSerial2", "d", "",
                "android.os.Build.SERIAL",
                "device serial"));

        // 📱 Device: производитель
        list.add(blk("sciDeviceManufacturer", "d", "",
                "android.os.Build.MANUFACTURER",
                "device manufacturer"));

        // 📱 Device: модель
        list.add(blk("sciDeviceModel2", "d", "",
                "android.os.Build.MODEL",
                "device model"));

        // 📱 Device: версия Android (SDK int)
        list.add(blk("sciSdkInt2", "d", "",
                "(double) android.os.Build.VERSION.SDK_INT",
                "sdk int"));

        // 📱 Device: язык системы
        list.add(blk("sciSystemLang", "d", "",
                "java.util.Locale.getDefault().getLanguage()",
                "system language"));

        // 📱 Device: страна системы
        list.add(blk("sciSystemCountry", "d", "",
                "java.util.Locale.getDefault().getCountry()",
                "system country"));

        // 📱 Device: часовой пояс
        list.add(blk("sciSystemTz", "d", "",
                "java.util.TimeZone.getDefault().getID()",
                "system timezone"));

        // 📱 Device: разрешение экрана (ширина)
        list.add(blk("sciScreenW", "d", "",
                "((new Object() { public double f() { try { android.util.DisplayMetrics _dm = new android.util.DisplayMetrics(); %1$s.getWindowManager().getDefaultDisplay().getMetrics(_dm); return (double) _dm.widthPixels; } catch (Throwable _t) { return 0.0; } } }).f())",
                "screen width from %m.activity"));

        // 📱 Device: разрешение экрана (высота)
        list.add(blk("sciScreenH", "d", "",
                "((new Object() { public double f() { try { android.util.DisplayMetrics _dm = new android.util.DisplayMetrics(); %1$s.getWindowManager().getDefaultDisplay().getMetrics(_dm); return (double) _dm.heightPixels; } catch (Throwable _t) { return 0.0; } } }).f())",
                "screen height from %m.activity"));

        // 📱 Device: плотность экрана (dpi)
        list.add(blk("sciScreenDpi2", "d", "",
                "((new Object() { public double f() { try { android.util.DisplayMetrics _dm = new android.util.DisplayMetrics(); %1$s.getWindowManager().getDefaultDisplay().getMetrics(_dm); return (double) _dm.densityDpi; } catch (Throwable _t) { return 0.0; } } }).f())",
                "screen dpi from %m.activity"));

        // 📱 Device: версия приложения (name)
        list.add(blk("sciAppVersionName", "d", "",
                "((new Object() { public String f() { try { return %1$s.getPackageManager().getPackageInfo(%1$s.getPackageName(), 0).versionName; } catch (Throwable _t) { return \"\"; } } }).f())",
                "app version name from %m.context"));

        // 📱 Device: версия приложения (code)
        list.add(blk("sciAppVersionCode", "d", "",
                "((new Object() { public double f() { try { return (double) %1$s.getPackageManager().getPackageInfo(%1$s.getPackageName(), 0).versionCode; } catch (Throwable _t) { return 0.0; } } }).f())",
                "app version code from %m.context"));

        // 📱 Device: имя пакета
        list.add(blk("sciAppPackage", "d", "",
                "%1$s.getPackageName()",
                "app package from %m.context"));

        // 📱 Device: имя приложения
        list.add(blk("sciAppName", "d", "",
                "((new Object() { public String f() { try { return %1$s.getApplicationInfo().loadLabel(%1$s.getPackageManager()).toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "app name from %m.context"));

        // 📱 Device: есть ли вибрация
        list.add(blk("sciHasVibrator", "b", "",
                "((android.os.Vibrator) %1$s.getSystemService(android.content.Context.VIBRATOR_SERVICE)).hasVibrator()",
                "has vibrator from %m.context"));

        // 📱 Device: есть ли камера
        list.add(blk("sciHasCamera", "b", "",
                "((new Object() { public boolean f() { try { return %1$s.getPackageManager().hasSystemFeature(android.content.pm.PackageManager.FEATURE_CAMERA_ANY); } catch (Throwable _t) { return false; } } }).f())",
                "has camera from %m.context"));

        // 🛡️ Security: HMAC-SHA256
        list.add(blk("sciHmacSha256", "d", "",
                "((new Object() { public String f() { try { javax.crypto.Mac _m = javax.crypto.Mac.getInstance(\"HmacSHA256\"); _m.init(new javax.crypto.spec.SecretKeySpec(%2$s.getBytes(), \"HmacSHA256\")); byte[] _b = _m.doFinal(%1$s.getBytes()); StringBuilder _sb = new StringBuilder(); for (byte _x : _b) _sb.append(String.format(\"%02x\", _x)); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "hmac sha256 of %s key %s"));

        // 🛡️ Security: PBKDF2 (пароль → хеш)
        list.add(blk("sciPbkdf2", "d", "",
                "((new Object() { public String f() { try { javax.crypto.spec.PBEKeySpec _s = new javax.crypto.spec.PBEKeySpec(%1$s.toCharArray(), %2$s.getBytes(), 1000, 256); javax.crypto.SecretKeyFactory _f = javax.crypto.SecretKeyFactory.getInstance(\"PBKDF2WithHmacSHA256\"); byte[] _b = _f.generateSecret(_s).getEncoded(); StringBuilder _sb = new StringBuilder(); for (byte _x : _b) _sb.append(String.format(\"%02x\", _x)); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "pbkdf2 of %s salt %s"));

        // 🛡️ Security: SHA-1 (файл)
        list.add(blk("sciSha1File", "d", "",
                "((new Object() { public String f() { try { java.io.FileInputStream _is = new java.io.FileInputStream(%1$s); java.security.MessageDigest _md = java.security.MessageDigest.getInstance(\"SHA-1\"); byte[] _buf = new byte[4096]; int _len; while ((_len = _is.read(_buf)) > 0) _md.update(_buf, 0, _len); _is.close(); byte[] _d = _md.digest(); StringBuilder _sb = new StringBuilder(); for (byte _x : _d) _sb.append(String.format(\"%02x\", _x)); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "sha1 of file %s"));

        // 🛡️ Security: SHA-256 (файл)
        list.add(blk("sciSha256File", "d", "",
                "((new Object() { public String f() { try { java.io.FileInputStream _is = new java.io.FileInputStream(%1$s); java.security.MessageDigest _md = java.security.MessageDigest.getInstance(\"SHA-256\"); byte[] _buf = new byte[4096]; int _len; while ((_len = _is.read(_buf)) > 0) _md.update(_buf, 0, _len); _is.close(); byte[] _d = _md.digest(); StringBuilder _sb = new StringBuilder(); for (byte _x : _d) _sb.append(String.format(\"%02x\", _x)); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "sha256 of file %s"));

        // 🛡️ Security: AES-256 (с ключом 32 символа)
        list.add(blk("sciAes256Encrypt", "d", "",
                "((new Object() { public String f() { try { javax.crypto.Cipher _c = javax.crypto.Cipher.getInstance(\"AES/CBC/PKCS5Padding\"); javax.crypto.spec.IvParameterSpec _iv = new javax.crypto.spec.IvParameterSpec(new byte[16]); _c.init(javax.crypto.Cipher.ENCRYPT_MODE, new javax.crypto.spec.SecretKeySpec(%2$s.getBytes(), \"AES\"), _iv); return android.util.Base64.encodeToString(_c.doFinal(%1$s.getBytes()), android.util.Base64.NO_WRAP); } catch (Throwable _t) { return \"\"; } } }).f())",
                "aes256 encrypt %s key %s"));

        // 🛡️ Security: AES-256 decrypt
        list.add(blk("sciAes256Decrypt", "d", "",
                "((new Object() { public String f() { try { javax.crypto.Cipher _c = javax.crypto.Cipher.getInstance(\"AES/CBC/PKCS5Padding\"); javax.crypto.spec.IvParameterSpec _iv = new javax.crypto.spec.IvParameterSpec(new byte[16]); _c.init(javax.crypto.Cipher.DECRYPT_MODE, new javax.crypto.spec.SecretKeySpec(%2$s.getBytes(), \"AES\"), _iv); return new String(_c.doFinal(android.util.Base64.decode(%1$s, android.util.Base64.NO_WRAP))); } catch (Throwable _t) { return \"\"; } } }).f())",
                "aes256 decrypt %s key %s"));

        // 🛡️ Security: сравнение с постоянным временем (защита от timing attack)
        list.add(blk("sciConstantTimeEquals", "b", "",
                "((new Object() { public boolean f() { try { byte[] _a = %1$s.getBytes(); byte[] _b = %2$s.getBytes(); if (_a.length != _b.length) return false; int _r = 0; for (int _i = 0; _i < _a.length; _i++) _r |= _a[_i] ^ _b[_i]; return _r == 0; } catch (Throwable _t) { return false; } } }).f())",
                "constant time equals %s and %s"));

        // 🛡️ Security: SHA-512 (файл)
        list.add(blk("sciSha512File", "d", "",
                "((new Object() { public String f() { try { java.io.FileInputStream _is = new java.io.FileInputStream(%1$s); java.security.MessageDigest _md = java.security.MessageDigest.getInstance(\"SHA-512\"); byte[] _buf = new byte[4096]; int _len; while ((_len = _is.read(_buf)) > 0) _md.update(_buf, 0, _len); _is.close(); byte[] _d = _md.digest(); StringBuilder _sb = new StringBuilder(); for (byte _x : _d) _sb.append(String.format(\"%02x\", _x)); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "sha512 of file %s"));

        // 🛡️ Security: генерация случайного ключа (hex)
        list.add(blk("sciRandomKey", "d", "",
                "((new Object() { public String f() { try { byte[] _b = new byte[(int)(%1$s)]; new java.security.SecureRandom().nextBytes(_b); StringBuilder _sb = new StringBuilder(); for (byte _x : _b) _sb.append(String.format(\"%02x\", _x)); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "random key length %d"));

        // 🛡️ Security: генерация случайной соли (Base64)
        list.add(blk("sciRandomSalt", "d", "",
                "((new Object() { public String f() { try { byte[] _b = new byte[(int)(%1$s)]; new java.security.SecureRandom().nextBytes(_b); return android.util.Base64.encodeToString(_b, android.util.Base64.NO_WRAP); } catch (Throwable _t) { return \"\"; } } }).f())",
                "random salt length %d"));

        // 🛡️ Security: хеш с солью (SHA-256)
        list.add(blk("sciHashWithSalt", "d", "",
                "((new Object() { public String f() { try { java.security.MessageDigest _md = java.security.MessageDigest.getInstance(\"SHA-256\"); _md.update(%2$s.getBytes()); byte[] _b = _md.digest(%1$s.getBytes()); StringBuilder _sb = new StringBuilder(); for (byte _x : _b) _sb.append(String.format(\"%02x\", _x)); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "hash %s with salt %s"));

        // 🛡️ Security: Base64 URL-safe
        list.add(blk("sciBase64UrlEncode", "d", "",
                "android.util.Base64.encodeToString(%1$s.getBytes(), android.util.Base64.URL_SAFE | android.util.Base64.NO_WRAP)",
                "base64 url encode %s"));

        // 🛡️ Security: Base64 URL-safe decode
        list.add(blk("sciBase64UrlDecode", "d", "",
                "new String(android.util.Base64.decode(%1$s, android.util.Base64.URL_SAFE | android.util.Base64.NO_WRAP))",
                "base64 url decode %s"));

        // 🛡️ Security: XOR шифрование
        list.add(blk("sciXorEncrypt", "d", "",
                "((new Object() { public String f() { try { byte[] _a = %1$s.getBytes(); byte[] _k = %2$s.getBytes(); for (int _i = 0; _i < _a.length; _i++) _a[_i] ^= _k[_i % _k.length]; return new String(_a); } catch (Throwable _t) { return \"\"; } } }).f())",
                "xor encrypt %s key %s"));

        // 🛡️ Security: XOR дешифрование (то же, что и шифрование)
        list.add(blk("sciXorDecrypt", "d", "",
                "((new Object() { public String f() { try { byte[] _a = %1$s.getBytes(); byte[] _k = %2$s.getBytes(); for (int _i = 0; _i < _a.length; _i++) _a[_i] ^= _k[_i % _k.length]; return new String(_a); } catch (Throwable _t) { return \"\"; } } }).f())",
                "xor decrypt %s key %s"));

        // 🛡️ Security: CRC32 (контрольная сумма)
        list.add(blk("sciCrc32", "d", "",
                "((new Object() { public double f() { try { java.util.zip.CRC32 _c = new java.util.zip.CRC32(); _c.update(%1$s.getBytes()); return (double) _c.getValue(); } catch (Throwable _t) { return 0.0; } } }).f())",
                "crc32 of %s"));

        // 🛡️ Security: SHA-256 (HMAC с файлом)
        list.add(blk("sciFileHmac", "d", "",
                "((new Object() { public String f() { try { java.io.FileInputStream _is = new java.io.FileInputStream(%1$s); javax.crypto.Mac _m = javax.crypto.Mac.getInstance(\"HmacSHA256\"); _m.init(new javax.crypto.spec.SecretKeySpec(%2$s.getBytes(), \"HmacSHA256\")); byte[] _buf = new byte[4096]; int _len; while ((_len = _is.read(_buf)) > 0) _m.update(_buf, 0, _len); _is.close(); byte[] _b = _m.doFinal(); StringBuilder _sb = new StringBuilder(); for (byte _x : _b) _sb.append(String.format(\"%02x\", _x)); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "hmac of file %s key %s"));

        // 🛡️ Security: проверка пароля (сравнение с хешем)
        list.add(blk("sciCheckPassword", "b", "",
                "((new Object() { public boolean f() { try { java.security.MessageDigest _md = java.security.MessageDigest.getInstance(\"SHA-256\"); byte[] _b = _md.digest(%1$s.getBytes()); StringBuilder _sb = new StringBuilder(); for (byte _x : _b) _sb.append(String.format(\"%02x\", _x)); return _sb.toString().equals(%2$s); } catch (Throwable _t) { return false; } } }).f())",
                "check password %s against hash %s"));

        // 🛡️ Security: заглушка (AES ключ 16 байт)
        list.add(blk("sciAesKey", "d", "",
                "((new Object() { public String f() { try { byte[] _k = new byte[16]; new java.security.SecureRandom().nextBytes(_k); return android.util.Base64.encodeToString(_k, android.util.Base64.NO_WRAP); } catch (Throwable _t) { return \"\"; } } }).f())",
                "generate aes key"));

        // 📅 Date: добавить месяцы
        list.add(blk("sciAddMonths", "d", "",
                "((new Object() { public String f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); _c.add(java.util.Calendar.MONTH, (int)(%2$s)); return _fmt.format(_c.getTime()); } catch (Throwable _t) { return %1$s; } } }).f())",
                "add %d months to %s"));

        // 📅 Date: добавить годы
        list.add(blk("sciAddYears", "d", "",
                "((new Object() { public String f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); _c.add(java.util.Calendar.YEAR, (int)(%2$s)); return _fmt.format(_c.getTime()); } catch (Throwable _t) { return %1$s; } } }).f())",
                "add %d years to %s"));

        // 📅 Date: добавить часы
        list.add(blk("sciAddHours", "d", "",
                "((new Object() { public String f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy HH:mm\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); _c.add(java.util.Calendar.HOUR_OF_DAY, (int)(%2$s)); return _fmt.format(_c.getTime()); } catch (Throwable _t) { return %1$s; } } }).f())",
                "add %d hours to %s"));

        // 📅 Date: добавить минуты
        list.add(blk("sciAddMinutes", "d", "",
                "((new Object() { public String f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy HH:mm\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); _c.add(java.util.Calendar.MINUTE, (int)(%2$s)); return _fmt.format(_c.getTime()); } catch (Throwable _t) { return %1$s; } } }).f())",
                "add %d minutes to %s"));

        // 📅 Date: формат "относительно" (5 минут назад)
        list.add(blk("sciRelativeTime", "d", "",
                "((new Object() { public String f() { try { long _diff = System.currentTimeMillis() - Long.parseLong(%1$s); long _s = _diff / 1000; if (_s < 60) return _s + \" сек назад\"; if (_s < 3600) return (_s / 60) + \" мин назад\"; if (_s < 86400) return (_s / 3600) + \" ч назад\"; return (_s / 86400) + \" дн назад\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "relative time of %s"));

        // 📅 Date: ISO-формат (yyyy-MM-dd'T'HH:mm:ss)
        list.add(blk("sciToIso", "d", "",
                "new java.text.SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss\", java.util.Locale.getDefault()).format(new java.util.Date())",
                "current iso time"));

        // 📅 Date: парсинг ISO
        list.add(blk("sciParseIso", "d", "",
                "((new Object() { public double f() { try { return (double) new java.text.SimpleDateFormat(\"yyyy-MM-dd'T'HH:mm:ss\", java.util.Locale.getDefault()).parse(%1$s).getTime(); } catch (Throwable _t) { return 0.0; } } }).f())",
                "parse iso %s to millis"));

        // 📅 Date: номер недели в году
        list.add(blk("sciWeekNumber", "d", "",
                "((new Object() { public double f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); return (double) _c.get(java.util.Calendar.WEEK_OF_YEAR); } catch (Throwable _t) { return 0.0; } } }).f())",
                "week number of %s"));

        // 📅 Date: високосный год?
        list.add(blk("sciIsLeapYear", "b", "",
                "((new Object() { public boolean f() { try { int _y = Integer.parseInt(%1$s); return (_y % 4 == 0 && _y % 100 != 0) || _y % 400 == 0; } catch (Throwable _t) { return false; } } }).f())",
                "year %s is leap"));

        // 📅 Date: возраст по дате рождения
        list.add(blk("sciAge", "d", "",
                "((new Object() { public double f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()); java.util.Calendar _b = java.util.Calendar.getInstance(); _b.setTime(_fmt.parse(%1$s)); java.util.Calendar _n = java.util.Calendar.getInstance(); int _age = _n.get(java.util.Calendar.YEAR) - _b.get(java.util.Calendar.YEAR); if (_n.get(java.util.Calendar.DAY_OF_YEAR) < _b.get(java.util.Calendar.DAY_OF_YEAR)) _age--; return (double) _age; } catch (Throwable _t) { return 0.0; } } }).f())",
                "age from %s"));

        // 📅 Date: первый день месяца
        list.add(blk("sciFirstDayOfMonth", "d", "",
                "((new Object() { public String f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); _c.set(java.util.Calendar.DAY_OF_MONTH, 1); return _fmt.format(_c.getTime()); } catch (Throwable _t) { return %1$s; } } }).f())",
                "first day of month %s"));

        // 📅 Date: последний день месяца
        list.add(blk("sciLastDayOfMonth", "d", "",
                "((new Object() { public String f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); _c.set(java.util.Calendar.DAY_OF_MONTH, _c.getActualMaximum(java.util.Calendar.DAY_OF_MONTH)); return _fmt.format(_c.getTime()); } catch (Throwable _t) { return %1$s; } } }).f())",
                "last day of month %s"));

        // 📅 Date: день недели (текст)
        list.add(blk("sciDayOfWeekName", "d", "",
                "((new Object() { public String f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); String[] _d = {\"\", \"Вс\", \"Пн\", \"Вт\", \"Ср\", \"Чт\", \"Пт\", \"Сб\"}; return _d[_c.get(java.util.Calendar.DAY_OF_WEEK)]; } catch (Throwable _t) { return \"\"; } } }).f())",
                "day of week name %s"));

        // 📅 Date: месяц (текст)
        list.add(blk("sciMonthName", "d", "",
                "((new Object() { public String f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); String[] _m = {\"\", \"Янв\", \"Фев\", \"Мар\", \"Апр\", \"Май\", \"Июн\", \"Июл\", \"Авг\", \"Сен\", \"Окт\", \"Ноя\", \"Дек\"}; return _m[_c.get(java.util.Calendar.MONTH) + 1]; } catch (Throwable _t) { return \"\"; } } }).f())",
                "month name %s"));

        // 📅 Date: разница в часах
        list.add(blk("sciHoursBetween", "d", "",
                "((new Object() { public double f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy HH:mm\", java.util.Locale.getDefault()); long _d1 = _fmt.parse(%1$s).getTime(); long _d2 = _fmt.parse(%2$s).getTime(); return (double) Math.abs((_d2 - _d1) / (1000 * 60 * 60)); } catch (Throwable _t) { return 0.0; } } }).f())",
                "hours between %s and %s"));

        // 📅 Date: разница в минутах
        list.add(blk("sciMinutesBetween", "d", "",
                "((new Object() { public double f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy HH:mm\", java.util.Locale.getDefault()); long _d1 = _fmt.parse(%1$s).getTime(); long _d2 = _fmt.parse(%2$s).getTime(); return (double) Math.abs((_d2 - _d1) / (1000 * 60)); } catch (Throwable _t) { return 0.0; } } }).f())",
                "minutes between %s and %s"));

        // 📅 Date: сейчас в миллисекундах
        list.add(blk("sciNowMillis", "d", "",
                "(double) System.currentTimeMillis()",
                "now in millis"));

        // 📅 Date: добавить секунды
        list.add(blk("sciAddSeconds", "d", "",
                "((new Object() { public String f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy HH:mm:ss\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); _c.add(java.util.Calendar.SECOND, (int)(%2$s)); return _fmt.format(_c.getTime()); } catch (Throwable _t) { return %1$s; } } }).f())",
                "add %d seconds to %s"));

        // 📅 Date: формат даты и времени
        list.add(blk("sciFormatDateTime", "d", "",
                "new java.text.SimpleDateFormat(%1$s, java.util.Locale.getDefault()).format(new java.util.Date())",
                "format current date with %s"));

        // 📅 Date: получить день месяца из даты
        list.add(blk("sciDayOfMonth", "d", "",
                "((new Object() { public double f() { try { java.text.SimpleDateFormat _fmt = new java.text.SimpleDateFormat(\"dd.MM.yyyy\", java.util.Locale.getDefault()); java.util.Calendar _c = java.util.Calendar.getInstance(); _c.setTime(_fmt.parse(%1$s)); return (double) _c.get(java.util.Calendar.DAY_OF_MONTH); } catch (Throwable _t) { return 0.0; } } }).f())",
                "day of month of %s"));

        // 📶 Bluetooth: включён ли
        list.add(blk("sciBtEnabled", "b", "",
                "((android.bluetooth.BluetoothManager) %1$s.getSystemService(android.content.Context.BLUETOOTH_SERVICE)).getAdapter().isEnabled()",
                "bluetooth enabled from %m.context"));

        // 📶 Bluetooth: имя устройства
        list.add(blk("sciBtName", "d", "",
                "((android.bluetooth.BluetoothManager) %1$s.getSystemService(android.content.Context.BLUETOOTH_SERVICE)).getAdapter().getName()",
                "bluetooth name from %m.context"));

        // 📶 Wi-Fi: включён ли
        list.add(blk("sciWifiEnabled", "b", "",
                "((android.net.wifi.WifiManager) %1$s.getSystemService(android.content.Context.WIFI_SERVICE)).isWifiEnabled()",
                "wifi enabled from %m.context"));

        // 📶 Wi-Fi: имя сети (SSID)
        list.add(blk("sciWifiSsid", "d", "",
                "((android.net.wifi.WifiManager) %1$s.getSystemService(android.content.Context.WIFI_SERVICE)).getConnectionInfo().getSSID()",
                "wifi ssid from %m.context"));

        // 📶 Wi-Fi: IP-адрес
        list.add(blk("sciWifiIp", "d", "",
                "((new Object() { public String f() { try { int _ip = ((android.net.wifi.WifiManager) %1$s.getSystemService(android.content.Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress(); return String.format(\"%%d.%%d.%%d.%%d\", (_ip & 0xff), (_ip >> 8 & 0xff), (_ip >> 16 & 0xff), (_ip >> 24 & 0xff)); } catch (Throwable _t) { return \"\"; } } }).f())",
                "wifi ip from %m.context"));

        // 📶 NFC: доступен ли
        list.add(blk("sciNfcAvailable", "b", "",
                "%1$s.getPackageManager().hasSystemFeature(android.content.pm.PackageManager.FEATURE_NFC)",
                "nfc available from %m.context"));

        // 📶 NFC: включён ли
        list.add(blk("sciNfcEnabled", "b", "",
                "((android.nfc.NfcManager) %1$s.getSystemService(android.content.Context.NFC_SERVICE)).getDefaultAdapter().isEnabled()",
                "nfc enabled from %m.context"));

        // 🗄️ DB: создать таблицу
        list.add(blk("sciDbCreateTable", " ", "",
                "((new Object() { public void f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); _db.execSQL(\"CREATE TABLE IF NOT EXISTS \" + %3$s + \" (\" + %4$s + \")\"); _db.close(); } catch (Throwable _t) {} } }).f());",
                "db create table columns from %m.context"));

        // 🗄️ DB: удалить таблицу
        list.add(blk("sciDbDropTable", " ", "",
                "((new Object() { public void f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); _db.execSQL(\"DROP TABLE IF EXISTS \" + %3$s); _db.close(); } catch (Throwable _t) {} } }).f());",
                "db drop table from %m.context"));

        // 🗄️ DB: количество строк в таблице
        list.add(blk("sciDbCount", "d", "",
                "((new Object() { public double f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); android.database.Cursor _c = _db.rawQuery(\"SELECT COUNT(*) FROM \" + %3$s, null); double _n = 0; if (_c.moveToFirst()) _n = _c.getDouble(0); _c.close(); _db.close(); return _n; } catch (Throwable _t) { return 0.0; } } }).f())",
                "db count rows in from %m.context"));

        // 🗄️ DB: очистить таблицу
        list.add(blk("sciDbClear", " ", "",
                "((new Object() { public void f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); _db.execSQL(\"DELETE FROM \" + %3$s); _db.close(); } catch (Throwable _t) {} } }).f());",
                "db clear table from %m.context"));

        // 🎵 Media: громкость (0-100)
        list.add(blk("sciMediaVolume", "d", "",
                "((new Object() { public double f() { try { android.media.AudioManager _am = (android.media.AudioManager) %1$s.getSystemService(android.content.Context.AUDIO_SERVICE); int _max = _am.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC); int _cur = _am.getStreamVolume(android.media.AudioManager.STREAM_MUSIC); return (double)(_cur * 100 / _max); } catch (Throwable _t) { return 0.0; } } }).f())",
                "media volume from %m.context"));

        // 🎵 Media: установить громкость (0-100)
        list.add(blk("sciSetMediaVolume", " ", "",
                "((new Object() { public void f() { try { android.media.AudioManager _am = (android.media.AudioManager) %1$s.getSystemService(android.content.Context.AUDIO_SERVICE); int _max = _am.getStreamMaxVolume(android.media.AudioManager.STREAM_MUSIC); _am.setStreamVolume(android.media.AudioManager.STREAM_MUSIC, (int)(%2$s) * _max / 100, 0); } catch (Throwable _t) {} } }).f());",
                "set media volume %d from %m.context"));

        // 🎵 Media: беззвучный режим
        list.add(blk("sciSetRingerSilent", " ", "",
                "((android.media.AudioManager) %1$s.getSystemService(android.content.Context.AUDIO_SERVICE)).setRingerMode(android.media.AudioManager.RINGER_MODE_SILENT);",
                "set ringer silent from %m.context"));

        // 🎵 Media: виброрежим
        list.add(blk("sciSetRingerVibrate", " ", "",
                "((android.media.AudioManager) %1$s.getSystemService(android.content.Context.AUDIO_SERVICE)).setRingerMode(android.media.AudioManager.RINGER_MODE_VIBRATE);",
                "set ringer vibrate from %m.context"));

        // 🎵 Media: обычный режим
        list.add(blk("sciSetRingerNormal", " ", "",
                "((android.media.AudioManager) %1$s.getSystemService(android.content.Context.AUDIO_SERVICE)).setRingerMode(android.media.AudioManager.RINGER_MODE_NORMAL);",
                "set ringer normal from %m.context"));

        // 🎵 Media: проиграть звук уведомления
        list.add(blk("sciPlayNotificationSound", " ", "",
                "((android.media.RingtoneManager) null); try { android.media.Ringtone _r = android.media.RingtoneManager.getRingtone(%1$s, android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION)); if (_r != null) _r.play(); } catch (Throwable _t) {}",
                "play notification sound from %m.context"));

        // 🎵 Media: проиграть звук будильника
        list.add(blk("sciPlayAlarmSound", " ", "",
                "try { android.media.Ringtone _r = android.media.RingtoneManager.getRingtone(%1$s, android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_ALARM)); if (_r != null) _r.play(); } catch (Throwable _t) {}",
                "play alarm sound from %m.context"));

        // 🎵 Media: проиграть звук звонка
        list.add(blk("sciPlayRingtone", " ", "",
                "try { android.media.Ringtone _r = android.media.RingtoneManager.getRingtone(%1$s, android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_RINGTONE)); if (_r != null) _r.play(); } catch (Throwable _t) {}",
                "play ringtone from %m.context"));

        // 🎵 Media: остановить все рингтоны
        list.add(blk("sciStopRingtones", " ", "",
                "try { android.media.RingtoneManager.getRingtone(%1$s, android.media.RingtoneManager.getDefaultUri(android.media.RingtoneManager.TYPE_NOTIFICATION)).stop(); } catch (Throwable _t) {}",
                "stop ringtones from %m.context"));

        // 🎵 Media: звук нажаттия (click)
        list.add(blk("sciPlayClickSound", " ", "",
                "%1$s.playSoundEffect(android.media.AudioManager.FX_KEY_CLICK);",
                "play click sound from %m.context"));

        // 🎵 Media: звук ошибки
        list.add(blk("sciPlayErrorSound", " ", "",
                "%1$s.playSoundEffect(android.media.AudioManager.FX_KEYPRESS_STANDARD);",
                "play error sound from %m.context"));

        // 🎵 Media: звук успеха
        list.add(blk("sciPlaySuccessSound", " ", "",
                "%1$s.playSoundEffect(android.media.AudioManager.FX_KEYPRESS_RETURN);",
                "play success sound from %m.context"));

        // 🎵 Media: вибрация с паттерном
        list.add(blk("sciVibratePattern2", " ", "",
                "((android.os.Vibrator) %2$s.getSystemService(android.content.Context.VIBRATOR_SERVICE)).vibrate(new long[]{0, (long)(%1$s), 100, (long)(%1$s)}, -1);",
                "vibrate pattern %d from %m.context"));

        // 🎵 Media: двойная вибрация
        list.add(blk("sciVibrateDouble", " ", "",
                "((android.os.Vibrator) %1$s.getSystemService(android.content.Context.VIBRATOR_SERVICE)).vibrate(new long[]{0, 100, 100, 100}, -1);",
                "vibrate double from %m.context"));

        // 🎵 Media: длинная вибрация
        list.add(blk("sciVibrateLong", " ", "",
                "((android.os.Vibrator) %1$s.getSystemService(android.content.Context.VIBRATOR_SERVICE)).vibrate(1000);",
                "vibrate long from %m.context"));

        // 🎵 Media: отменить вибрацию
        list.add(blk("sciVibrateCancel", " ", "",
                "((android.os.Vibrator) %1$s.getSystemService(android.content.Context.VIBRATOR_SERVICE)).cancel();",
                "cancel vibration from %m.context"));

        // 🎵 Media: воспроизвести видео (URL)
        list.add(blk("sciPlayVideoUrl", " ", "",
                "try { android.content.Intent _i = new android.content.Intent(android.content.Intent.ACTION_VIEW); _i.setDataAndType(android.net.Uri.parse(%1$s), \"video/*\"); %2$s.startActivity(_i); } catch (Throwable _t) {}",
                "play video url from %m.context"));

        // 🎵 Media: воспроизвести аудио (URL)
        list.add(blk("sciPlayAudioUrl", " ", "",
                "try { android.content.Intent _i = new android.content.Intent(android.content.Intent.ACTION_VIEW); _i.setDataAndType(android.net.Uri.parse(%1$s), \"audio/*\"); %2$s.startActivity(_i); } catch (Throwable _t) {}",
                "play audio url from %m.context"));

        // 🎵 Media: открыть изображение
        list.add(blk("sciOpenImage", " ", "",
                "try { android.content.Intent _i = new android.content.Intent(android.content.Intent.ACTION_VIEW); _i.setDataAndType(android.net.Uri.parse(%1$s), \"image/*\"); %2$s.startActivity(_i); } catch (Throwable _t) {}",
                "open image from %m.context"));

        // 🎵 Media: открыть PDF
        list.add(blk("sciOpenPdf", " ", "",
                "try { android.content.Intent _i = new android.content.Intent(android.content.Intent.ACTION_VIEW); _i.setDataAndType(android.net.Uri.parse(%1$s), \"application/pdf\"); %2$s.startActivity(_i); } catch (Throwable _t) {}",
                "open pdf from %m.context"));

        // 🎵 Media: открыть текстовый файл
        list.add(blk("sciOpenTextFile", " ", "",
                "try { android.content.Intent _i = new android.content.Intent(android.content.Intent.ACTION_VIEW); _i.setDataAndType(android.net.Uri.parse(%1$s), \"text/plain\"); %2$s.startActivity(_i); } catch (Throwable _t) {}",
                "open text file from %m.context"));

        // 🎵 Media: поделиться файлом
        list.add(blk("sciShareFile2", " ", "",
                "try { android.content.Intent _i = new android.content.Intent(android.content.Intent.ACTION_SEND); _i.setType(\"*/*\"); _i.putExtra(android.content.Intent.EXTRA_STREAM, android.net.Uri.parse(%1$s)); %2$s.startActivity(android.content.Intent.createChooser(_i, \"Share\")); } catch (Throwable _t) {}",
                "share file from %m.context"));

        // 🎵 Media: сохранить изображение в галерею
        list.add(blk("sciSaveImageToGallery2", " ", "",
                "((new Object() { public void f() { try { android.graphics.Bitmap _b = android.graphics.BitmapFactory.decodeFile(%1$s); java.io.File _o = new java.io.File(android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_PICTURES), %2$s); java.io.FileOutputStream _os = new java.io.FileOutputStream(_o); _b.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, _os); _os.flush(); _os.close(); android.media.MediaScannerConnection.scanFile(%3$s, new String[]{_o.getAbsolutePath()}, null, null); } catch (Throwable _t) {} } }).f());",
                "save image to gallery as from %m.context"));

        // 🎵 Media: получить обложку аудио
        list.add(blk("sciAudioAlbumArt", "d", "",
                "((new Object() { public String f() { try { android.media.MediaMetadataRetriever _m = new android.media.MediaMetadataRetriever(); _m.setDataSource(%1$s); byte[] _b = _m.getEmbeddedPicture(); _m.release(); return _b != null ? android.util.Base64.encodeToString(_b, android.util.Base64.NO_WRAP) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "album art of %s"));

        // 🎵 Media: длительность видео (мс)
        list.add(blk("sciVideoDuration", "d", "",
                "((new Object() { public double f() { try { android.media.MediaMetadataRetriever _m = new android.media.MediaMetadataRetriever(); _m.setDataSource(%1$s); String _d = _m.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_DURATION); _m.release(); return _d != null ? Double.parseDouble(_d) : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "video duration of %s"));

        // 🎵 Media: разрешение видео (ширина)
        list.add(blk("sciVideoWidth", "d", "",
                "((new Object() { public double f() { try { android.media.MediaMetadataRetriever _m = new android.media.MediaMetadataRetriever(); _m.setDataSource(%1$s); String _w = _m.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH); _m.release(); return _w != null ? Double.parseDouble(_w) : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "video width of %s"));

        // 🎵 Media: разрешение видео (высота)
        list.add(blk("sciVideoHeight", "d", "",
                "((new Object() { public double f() { try { android.media.MediaMetadataRetriever _m = new android.media.MediaMetadataRetriever(); _m.setDataSource(%1$s); String _h = _m.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT); _m.release(); return _h != null ? Double.parseDouble(_h) : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "video height of %s"));

        // 📍 Location: широта (GPS)
        list.add(blk("sciGpsLat", "d", "",
                "((new Object() { public double f() { try { android.location.LocationManager _lm = (android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE); android.location.Location _l = _lm.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER); return _l != null ? _l.getLatitude() : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "gps latitude from %m.context"));

        // 📍 Location: долгота (GPS)
        list.add(blk("sciGpsLon", "d", "",
                "((new Object() { public double f() { try { android.location.LocationManager _lm = (android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE); android.location.Location _l = _lm.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER); return _l != null ? _l.getLongitude() : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "gps longitude from %m.context"));

        // 📍 Location: высота над уровнем моря
        list.add(blk("sciGpsAltitude", "d", "",
                "((new Object() { public double f() { try { android.location.LocationManager _lm = (android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE); android.location.Location _l = _lm.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER); return _l != null ? _l.getAltitude() : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "gps altitude from %m.context"));

        // 📍 Location: скорость (м/с)
        list.add(blk("sciGpsSpeed", "d", "",
                "((new Object() { public double f() { try { android.location.LocationManager _lm = (android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE); android.location.Location _l = _lm.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER); return _l != null ? _l.getSpeed() : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "gps speed from %m.context"));

        // 📍 Location: расстояние между двумя точками (метры)
        list.add(blk("sciDistanceBetween", "d", "",
                "((new Object() { public double f() { try { float[] _r = new float[1]; android.location.Location.distanceBetween((double)(%1$s), (double)(%2$s), (double)(%3$s), (double)(%4$s), _r); return (double) _r[0]; } catch (Throwable _t) { return 0.0; } } }).f())",
                "distance between (%s, %s) and (%s, %s)"));

        // 📍 Location: GPS включён?
        list.add(blk("sciGpsEnabled", "b", "",
                "((android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE)).isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)",
                "gps enabled from %m.context"));

        // 📱 Sensor: акселерометр X
        list.add(blk("sciSensorAccelX", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "accelerometer X from %m.context"));

        // 📱 Sensor: акселерометр Y
        list.add(blk("sciSensorAccelY", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "accelerometer Y from %m.context"));

        // 📱 Sensor: акселерометр Z
        list.add(blk("sciSensorAccelZ", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "accelerometer Z from %m.context"));

        // 📱 Sensor: гироскоп X
        list.add(blk("sciSensorGyroX", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_GYROSCOPE); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "gyroscope X from %m.context"));

        // 📱 Sensor: компас (азимут)
        list.add(blk("sciSensorCompass", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_ORIENTATION); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "compass azimuth from %m.context"));

        // 📱 Sensor: датчик света (люкс)
        list.add(blk("sciSensorLight", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_LIGHT); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "light sensor from %m.context"));

        // 📱 Sensor: датчик приближения (см)
        list.add(blk("sciSensorProximity", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_PROXIMITY); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "proximity sensor from %m.context"));

        // 📱 Sensor: шагомер
        list.add(blk("sciSensorStep", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_STEP_COUNTER); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "step counter from %m.context"));

        // 📱 Sensor: пульс
        list.add(blk("sciSensorHeart", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_HEART_RATE); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "heart rate sensor from %m.context"));

        // 📞 Phone: получить IMEI (требует READ_PHONE_STATE)
        list.add(blk("sciImei", "d", "",
                "((new Object() { public String f() { try { android.telephony.TelephonyManager _tm = (android.telephony.TelephonyManager) %1$s.getSystemService(android.content.Context.TELEPHONY_SERVICE); return _tm.getImei(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "device imei from %m.context"));

        // 📞 Phone: SIM-серийный номер
        list.add(blk("sciSimSerial", "d", "",
                "((new Object() { public String f() { try { android.telephony.TelephonyManager _tm = (android.telephony.TelephonyManager) %1$s.getSystemService(android.content.Context.TELEPHONY_SERVICE); return _tm.getSimSerialNumber(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "sim serial from %m.context"));

        // 📞 Phone: номер телефона
        list.add(blk("sciPhoneNumber", "d", "",
                "((new Object() { public String f() { try { android.telephony.TelephonyManager _tm = (android.telephony.TelephonyManager) %1$s.getSystemService(android.content.Context.TELEPHONY_SERVICE); return _tm.getLine1Number(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "phone number from %m.context"));

        // 📞 Phone: код страны сети
        list.add(blk("sciNetworkCountry", "d", "",
                "((new Object() { public String f() { try { android.telephony.TelephonyManager _tm = (android.telephony.TelephonyManager) %1$s.getSystemService(android.content.Context.TELEPHONY_SERVICE); return _tm.getNetworkCountryIso(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "network country from %m.context"));

        // 📞 Phone: имя оператора
        list.add(blk("sciOperatorName", "d", "",
                "((new Object() { public String f() { try { android.telephony.TelephonyManager _tm = (android.telephony.TelephonyManager) %1$s.getSystemService(android.content.Context.TELEPHONY_SERVICE); return _tm.getNetworkOperatorName(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "operator name from %m.context"));

        // 📞 Phone: тип сети (2G/3G/4G)
        list.add(blk("sciNetworkType2", "d", "",
                "((new Object() { public String f() { try { android.telephony.TelephonyManager _tm = (android.telephony.TelephonyManager) %1$s.getSystemService(android.content.Context.TELEPHONY_SERVICE); int _t = _tm.getNetworkType(); return _t == 13 ? \"4G\" : _t == 3 ? \"3G\" : _t == 2 ? \"EDGE\" : \"Unknown\"; } catch (Throwable _t) { return \"Unknown\"; } } }).f())",
                "network type from %m.context"));

        // 📞 Phone: авиарежим?
        list.add(blk("sciAirplaneMode", "b", "",
                "((new Object() { public boolean f() { try { return android.provider.Settings.System.getInt(%1$s.getContentResolver(), android.provider.Settings.System.AIRPLANE_MODE_ON) == 1; } catch (Throwable _t) { return false; } } }).f())",
                "airplane mode from %m.context"));

        // 📞 Phone: режим вибрации?
        list.add(blk("sciVibrateMode", "b", "",
                "((android.media.AudioManager) %1$s.getSystemService(android.content.Context.AUDIO_SERVICE)).getRingerMode() == android.media.AudioManager.RINGER_MODE_VIBRATE",
                "vibrate mode from %m.context"));

        // 📞 Phone: экран заблокирован?
        list.add(blk("sciScreenLocked", "b", "",
                "((android.app.KeyguardManager) %1$s.getSystemService(android.content.Context.KEYGUARD_SERVICE)).isKeyguardLocked()",
                "screen locked from %m.context"));

        // 📞 Phone: устройство заряжается?
        list.add(blk("sciIsCharging", "b", "",
                "((new Object() { public boolean f() { try { android.content.Intent _i = %1$s.registerReceiver(null, new android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED)); int _s = _i.getIntExtra(android.os.BatteryManager.EXTRA_STATUS, -1); return _s == android.os.BatteryManager.BATTERY_STATUS_CHARGING || _s == android.os.BatteryManager.BATTERY_STATUS_FULL; } catch (Throwable _t) { return false; } } }).f())",
                "is charging from %m.context"));

        // 📞 Phone: режим разработчика?
        list.add(blk("sciDevMode", "b", "",
                "android.provider.Settings.Global.getInt(%1$s.getContentResolver(), android.provider.Settings.Global.DEVELOPMENT_SETTINGS_ENABLED, 0) == 1",
                "developer mode from %m.context"));

        // 📞 Phone: эмулятор?
        list.add(blk("sciIsEmulator", "b", "",
                "android.os.Build.FINGERPRINT.startsWith(\"generic\") || android.os.Build.MODEL.contains(\"google_sdk\")",
                "is emulator"));

        // 📞 Phone: время работы (uptime, мс)
        list.add(blk("sciUptime", "d", "",
                "(double) android.os.SystemClock.elapsedRealtime()",
                "uptime millis"));

        // 📞 Phone: версия ядра
        list.add(blk("sciKernelVersion", "d", "",
                "System.getProperty(\"os.version\")",
                "kernel version"));

        // 📞 Phone: версия Java
        list.add(blk("sciJavaVersion", "d", "",
                "System.getProperty(\"java.version\")",
                "java version"));

        // 📞 Phone: версия ОС
        list.add(blk("sciOsVersion", "d", "",
                "System.getProperty(\"os.name\") + \" \" + System.getProperty(\"os.version\")",
                "os version"));

        // 📞 Phone: архитектура
        list.add(blk("sciArchitecture", "d", "",
                "System.getProperty(\"os.arch\")",
                "architecture"));

        // 📞 Phone: количество ядер
        list.add(blk("sciCores", "d", "",
                "(double) Runtime.getRuntime().availableProcessors()",
                "cpu cores"));

        // 📞 Phone: свободная память (МБ)
        list.add(blk("sciFreeMemory", "d", "",
                "(double) (Runtime.getRuntime().freeMemory() / (1024 * 1024))",
                "free memory MB"));

        // 📞 Phone: общая память (МБ)
        list.add(blk("sciTotalMemory", "d", "",
                "(double) (Runtime.getRuntime().totalMemory() / (1024 * 1024))",
                "total memory MB"));

        // 📞 Phone: максимальная память (МБ)
        list.add(blk("sciMaxMemory", "d", "",
                "(double) (Runtime.getRuntime().maxMemory() / (1024 * 1024))",
                "max memory MB"));

        // 📱 Sensor: акселерометр X (реальный)
        list.add(blk("sciAccelXReal", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "accelerometer X real from %m.context"));

        // 📱 Sensor: акселерометр Y (реальный)
        list.add(blk("sciAccelYReal", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "accelerometer Y real from %m.context"));

        // 📱 Sensor: акселерометр Z (реальный)
        list.add(blk("sciAccelZReal", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "accelerometer Z real from %m.context"));

        // 📱 Sensor: гироскоп X (реальный)
        list.add(blk("sciGyroXReal", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_GYROSCOPE); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "gyroscope X real from %m.context"));

        // 📱 Sensor: гироскоп Y (реальный)
        list.add(blk("sciGyroYReal", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_GYROSCOPE); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "gyroscope Y real from %m.context"));

        // 📱 Sensor: гироскоп Z (реальный)
        list.add(blk("sciGyroZReal", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_GYROSCOPE); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "gyroscope Z real from %m.context"));

        // 📱 Sensor: магнитометр X
        list.add(blk("sciMagXReal", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "magnetometer X real from %m.context"));

        // 📱 Sensor: магнитометр Y
        list.add(blk("sciMagYReal", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "magnetometer Y real from %m.context"));

        // 📱 Sensor: магнитометр Z
        list.add(blk("sciMagZReal", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_MAGNETIC_FIELD); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "magnetometer Z real from %m.context"));

        // 📱 Sensor: линейное ускорение X
        list.add(blk("sciLinearAccelX", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_LINEAR_ACCELERATION); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "linear accel X from %m.context"));

        // 📱 Sensor: линейное ускорение Y
        list.add(blk("sciLinearAccelY", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_LINEAR_ACCELERATION); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "linear accel Y from %m.context"));

        // 📱 Sensor: линейное ускорение Z
        list.add(blk("sciLinearAccelZ", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_LINEAR_ACCELERATION); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "linear accel Z from %m.context"));

        // 📱 Sensor: гравитация X
        list.add(blk("sciGravityX", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_GRAVITY); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "gravity X from %m.context"));

        // 📱 Sensor: гравитация Y
        list.add(blk("sciGravityY", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_GRAVITY); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "gravity Y from %m.context"));

        // 📱 Sensor: гравитация Z
        list.add(blk("sciGravityZ", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_GRAVITY); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "gravity Z from %m.context"));

        // 📱 Sensor: вращение X
        list.add(blk("sciRotationX", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_ROTATION_VECTOR); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "rotation X from %m.context"));

        // 📱 Sensor: вращение Y
        list.add(blk("sciRotationY", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_ROTATION_VECTOR); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "rotation Y from %m.context"));

        // 📱 Sensor: вращение Z
        list.add(blk("sciRotationZ", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_ROTATION_VECTOR); float[] _v = new float[3]; return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "rotation Z from %m.context"));

        // 📱 Sensor: влажность
        list.add(blk("sciHumidity", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_RELATIVE_HUMIDITY); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "humidity from %m.context"));

        // 📱 Sensor: температура
        list.add(blk("sciAmbientTemp", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_AMBIENT_TEMPERATURE); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "ambient temperature from %m.context"));

        // 📱 Sensor: давление (барометр)
        list.add(blk("sciPressureSensor", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_PRESSURE); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "pressure sensor from %m.context"));

        // 📱 Sensor: количество шагов (с момента загрузки)
        list.add(blk("sciStepCounter", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_STEP_COUNTER); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "step counter from %m.context"));

        // 📱 Sensor: детектор шагов (для фонового)
        list.add(blk("sciStepDetector", "d", "",
                "((new Object() { public double f() { try { android.hardware.SensorManager _sm = (android.hardware.SensorManager) %1$s.getSystemService(android.content.Context.SENSOR_SERVICE); android.hardware.Sensor _s = _sm.getDefaultSensor(android.hardware.Sensor.TYPE_STEP_DETECTOR); return _s != null ? 0.0 : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "step detector from %m.context"));

        // 📍 Location: широта (сеть)
        list.add(blk("sciNetLat", "d", "",
                "((new Object() { public double f() { try { android.location.LocationManager _lm = (android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE); android.location.Location _l = _lm.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER); return _l != null ? _l.getLatitude() : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "network latitude from %m.context"));

        // 📍 Location: долгота (сеть)
        list.add(blk("sciNetLon", "d", "",
                "((new Object() { public double f() { try { android.location.LocationManager _lm = (android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE); android.location.Location _l = _lm.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER); return _l != null ? _l.getLongitude() : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "network longitude from %m.context"));

        // 📍 Location: GPS-провайдер включён?
        list.add(blk("sciGpsProvider", "b", "",
                "((android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE)).isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)",
                "gps provider enabled from %m.context"));

        // 📍 Location: сетевой провайдер включён?
        list.add(blk("sciNetProvider", "b", "",
                "((android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE)).isProviderEnabled(android.location.LocationManager.NETWORK_PROVIDER)",
                "network provider enabled from %m.context"));

        // 📍 Location: включено ли определение местоположения
        list.add(blk("sciLocationEnabled", "b", "",
                "((android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE)).isLocationEnabled()",
                "location enabled from %m.context"));

        // 📍 Location: последнее обновление (millis)
        list.add(blk("sciLocationTime", "d", "",
                "((new Object() { public double f() { try { android.location.LocationManager _lm = (android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE); android.location.Location _l = _lm.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER); return _l != null ? (double) _l.getTime() : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "last location time from %m.context"));

        // 📍 Location: точность (метры)
        list.add(blk("sciLocationAccuracy", "d", "",
                "((new Object() { public double f() { try { android.location.LocationManager _lm = (android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE); android.location.Location _l = _lm.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER); return _l != null ? (double) _l.getAccuracy() : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "location accuracy from %m.context"));

        // 📍 Location: направление (градусы)
        list.add(blk("sciLocationBearing", "d", "",
                "((new Object() { public double f() { try { android.location.LocationManager _lm = (android.location.LocationManager) %1$s.getSystemService(android.content.Context.LOCATION_SERVICE); android.location.Location _l = _lm.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER); return _l != null ? (double) _l.getBearing() : 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "location bearing from %m.context"));

        // 📞 Phone: получить список контактов (имена)
        list.add(blk("sciContactsNames", "d", "",
                "((new Object() { public String f() { try { android.database.Cursor _c = %1$s.getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{android.provider.ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME}, null, null, null); StringBuilder _sb = new StringBuilder(); if (_c != null) { while (_c.moveToNext()) { _sb.append(_c.getString(0)).append(\"\\n\"); } _c.close(); } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "contacts names from %m.context"));

        // 📞 Phone: получить список номеров
        list.add(blk("sciContactsNumbers", "d", "",
                "((new Object() { public String f() { try { android.database.Cursor _c = %1$s.getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null); StringBuilder _sb = new StringBuilder(); if (_c != null) { while (_c.moveToNext()) { _sb.append(_c.getString(0)).append(\"\\n\"); } _c.close(); } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "contacts numbers from %m.context"));

        // 📞 Phone: количество контактов
        list.add(blk("sciContactsCount", "d", "",
                "((new Object() { public double f() { try { android.database.Cursor _c = %1$s.getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{android.provider.ContactsContract.CommonDataKinds.Phone._ID}, null, null, null); double _n = _c != null ? _c.getCount() : 0; if (_c != null) _c.close(); return _n; } catch (Throwable _t) { return 0.0; } } }).f())",
                "contacts count from %m.context"));

        // 📞 Phone: получить email контакта
        list.add(blk("sciContactEmail", "d", "",
                "((new Object() { public String f() { try { android.database.Cursor _c = %1$s.getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Email.CONTENT_URI, new String[]{android.provider.ContactsContract.CommonDataKinds.Email.ADDRESS}, android.provider.ContactsContract.CommonDataKinds.Email.CONTACT_ID + \" = ?\", new String[]{%1$s}, null); if (_c != null && _c.moveToFirst()) { String _e = _c.getString(0); _c.close(); return _e; } return \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "contact email from %m.context"));

        // 📞 Phone: добавить контакт (имя + номер)
        list.add(blk("sciAddContact", " ", "",
                "((new Object() { public void f() { try { android.content.ContentValues _v = new android.content.ContentValues(); _v.put(android.provider.ContactsContract.Data.MIMETYPE, android.provider.ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE); _v.put(android.provider.ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME, %1$s); android.net.Uri _u = %2$s.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, _v); if (_u != null) { android.content.ContentValues _p = new android.content.ContentValues(); _p.put(android.provider.ContactsContract.Data.MIMETYPE, android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE); _p.put(android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER, %3$s); _p.put(android.provider.ContactsContract.Data.CONTACT_ID, android.content.ContentUris.parseId(_u)); _p.put(android.provider.ContactsContract.CommonDataKinds.Phone.TYPE, android.provider.ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE); %2$s.getContentResolver().insert(android.provider.ContactsContract.Data.CONTENT_URI, _p); } } catch (Throwable _t) {} } }).f());",
                "add contact name number from %m.context"));

        // 📞 Phone: количество SMS в Inbox
        list.add(blk("sciSmsCount", "d", "",
                "((new Object() { public double f() { try { android.database.Cursor _c = %1$s.getContentResolver().query(android.net.Uri.parse(\"content://sms/inbox\"), new String[]{\"_id\"}, null, null, null); double _n = _c != null ? _c.getCount() : 0; if (_c != null) _c.close(); return _n; } catch (Throwable _t) { return 0.0; } } }).f())",
                "sms inbox count from %m.context"));

        // 📞 Phone: текст SMS по индексу
        list.add(blk("sciSmsBody", "d", "",
                "((new Object() { public String f() { try { android.database.Cursor _c = %1$s.getContentResolver().query(android.net.Uri.parse(\"content://sms/inbox\"), new String[]{\"body\"}, null, null, \"date DESC\"); String _b = \"\"; if (_c != null) { if (_c.moveToPosition((int)(%2$s))) _b = _c.getString(0); _c.close(); } return _b; } catch (Throwable _t) { return \"\"; } } }).f())",
                "sms body at %d from %m.context"));

        // 📞 Phone: номер отправителя SMS
        list.add(blk("sciSmsAddress", "d", "",
                "((new Object() { public String f() { try { android.database.Cursor _c = %1$s.getContentResolver().query(android.net.Uri.parse(\"content://sms/inbox\"), new String[]{\"address\"}, null, null, \"date DESC\"); String _a = \"\"; if (_c != null) { if (_c.moveToPosition((int)(%2$s))) _a = _c.getString(0); _c.close(); } return _a; } catch (Throwable _t) { return \"\"; } } }).f())",
                "sms address at %d from %m.context"));

        // 📞 Phone: дата SMS (millis)
        list.add(blk("sciSmsDate", "d", "",
                "((new Object() { public double f() { try { android.database.Cursor _c = %1$s.getContentResolver().query(android.net.Uri.parse(\"content://sms/inbox\"), new String[]{\"date\"}, null, null, \"date DESC\"); double _d = 0; if (_c != null) { if (_c.moveToPosition((int)(%2$s))) _d = _c.getDouble(0); _c.close(); } return _d; } catch (Throwable _t) { return 0.0; } } }).f())",
                "sms date at %d from %m.context"));

        // 📞 Phone: количество звонков (история)
        list.add(blk("sciCallLogCount", "d", "",
                "((new Object() { public double f() { try { android.database.Cursor _c = %1$s.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI, new String[]{android.provider.CallLog.Calls._ID}, null, null, null); double _n = _c != null ? _c.getCount() : 0; if (_c != null) _c.close(); return _n; } catch (Throwable _t) { return 0.0; } } }).f())",
                "call log count from %m.context"));

        // 📞 Phone: номер звонка по индексу
        list.add(blk("sciCallLogNumber", "d", "",
                "((new Object() { public String f() { try { android.database.Cursor _c = %1$s.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI, new String[]{android.provider.CallLog.Calls.NUMBER}, null, null, \"date DESC\"); String _n = \"\"; if (_c != null) { if (_c.moveToPosition((int)(%2$s))) _n = _c.getString(0); _c.close(); } return _n; } catch (Throwable _t) { return \"\"; } } }).f())",
                "call log number at %d from %m.context"));

        // 📞 Phone: тип звонка
        list.add(blk("sciCallLogType", "d", "",
                "((new Object() { public double f() { try { android.database.Cursor _c = %1$s.getContentResolver().query(android.provider.CallLog.Calls.CONTENT_URI, new String[]{android.provider.CallLog.Calls.TYPE}, null, null, \"date DESC\"); double _t = 0; if (_c != null) { if (_c.moveToPosition((int)(%2$s))) _t = _c.getDouble(0); _c.close(); } return _t; } catch (Throwable _t) { return 0.0; } } }).f())",
                "call log type at %d from %m.context"));

        // 📞 Phone: удалить контакт
        list.add(blk("sciDeleteContact", " ", "",
                "((new Object() { public void f() { try { android.database.Cursor _c = %1$s.getContentResolver().query(android.provider.ContactsContract.CommonDataKinds.Phone.CONTENT_URI, new String[]{android.provider.ContactsContract.CommonDataKinds.Phone.CONTACT_ID}, android.provider.ContactsContract.CommonDataKinds.Phone.NUMBER + \" = ?\", new String[]{%2$s}, null); if (_c != null && _c.moveToFirst()) { long _id = _c.getLong(0); _c.close(); %1$s.getContentResolver().delete(android.provider.ContactsContract.RawContacts.CONTENT_URI, android.provider.ContactsContract.RawContacts.CONTACT_ID + \" = ?\", new String[]{String.valueOf(_id)}); } } catch (Throwable _t) {} } }).f());",
                "delete contact by number from %m.context"));

        // 📁 File: записать в конец файла
        list.add(blk("sciFileAppend", " ", "",
                "((new Object() { public void f() { try { java.io.FileWriter _w = new java.io.FileWriter(%1$s, true); _w.write(%2$s); _w.close(); } catch (Throwable _t) {} } }).f());",
                "append %s to %s"));

        // 📁 File: создать папку
        list.add(blk("sciFileMkdirs", "b", "",
                "new java.io.File(%1$s).mkdirs()",
                "mkdirs %s"));

        // 📁 File: получить все файлы в папке
        list.add(blk("sciFileList", "d", "",
                "((new Object() { public String f() { try { java.io.File _d = new java.io.File(%1$s); StringBuilder _sb = new StringBuilder(); java.io.File[] _fs = _d.listFiles(); if (_fs != null) { for (java.io.File _f : _fs) { _sb.append(_f.getName()).append(\"\\n\"); } } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "list files in %s"));

        // 📁 File: получить все папки в директории
        list.add(blk("sciDirList", "d", "",
                "((new Object() { public String f() { try { java.io.File _d = new java.io.File(%1$s); StringBuilder _sb = new StringBuilder(); java.io.File[] _fs = _d.listFiles(); if (_fs != null) { for (java.io.File _f : _fs) { if (_f.isDirectory()) _sb.append(_f.getName()).append(\"\\n\"); } } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "list dirs in %s"));

        // 📁 File: получить размер папки (МБ)
        list.add(blk("sciDirSize", "d", "",
                "((new Object() { public double f() { try { long _s = 0; java.util.Stack _st = new java.util.Stack(); _st.push(new java.io.File(%1$s)); while (!_st.isEmpty()) { java.io.File _f = (java.io.File) _st.pop(); if (_f.isDirectory()) { java.io.File[] _fs = _f.listFiles(); if (_fs != null) { for (java.io.File _c : _fs) _st.push(_c); } } else { _s += _f.length(); } } return (double) (_s / (1024 * 1024)); } catch (Throwable _t) { return 0.0; } } }).f())",
                "dir size MB of %s"));

        // 📁 File: проверить, пуста ли папка
        list.add(blk("sciDirEmpty", "b", "",
                "((new Object() { public boolean f() { try { java.io.File _d = new java.io.File(%1$s); java.io.File[] _fs = _d.listFiles(); return _fs == null || _fs.length == 0; } catch (Throwable _t) { return false; } } }).f())",
                "dir %s is empty"));

        // 📁 File: получить путь к external storage
        list.add(blk("sciExternalStorage", "d", "",
                "android.os.Environment.getExternalStorageDirectory().getAbsolutePath()",
                "external storage path"));

        // 📁 File: получить путь к DCIM
        list.add(blk("sciDcimDir", "d", "",
                "android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DCIM).getAbsolutePath()",
                "dcim directory path"));

        // 📁 File: получить путь к Music
        list.add(blk("sciMusicDir", "d", "",
                "android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_MUSIC).getAbsolutePath()",
                "music directory path"));

        // 📁 File: получить путь к Movies
        list.add(blk("sciMoviesDir", "d", "",
                "android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_MOVIES).getAbsolutePath()",
                "movies directory path"));

        // 📁 File: получить путь к Downloads
        list.add(blk("sciDownloadsDir", "d", "",
                "android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS).getAbsolutePath()",
                "downloads directory path"));

        // 📁 File: существует ли папка
        list.add(blk("sciDirExists", "b", "",
                "new java.io.File(%1$s).isDirectory()",
                "dir %s exists"));

        // 📁 File: скопировать файл
        list.add(blk("sciFileCopy", "b", "",
                "((new Object() { public boolean f() { try { java.io.FileInputStream _is = new java.io.FileInputStream(%1$s); java.io.FileOutputStream _os = new java.io.FileOutputStream(%2$s); byte[] _b = new byte[4096]; int _len; while ((_len = _is.read(_b)) > 0) _os.write(_b, 0, _len); _is.close(); _os.close(); return true; } catch (Throwable _t) { return false; } } }).f())",
                "copy file %s to %s"));

        // 📁 File: переместить файл
        list.add(blk("sciFileMove", "b", "",
                "new java.io.File(%1$s).renameTo(new java.io.File(%2$s))",
                "move file %s to %s"));

        // 📁 File: удалить файл
        list.add(blk("sciFileDelete2", "b", "",
                "new java.io.File(%1$s).delete()",
                "delete file %s"));

        // 📁 File: размер файла в КБ
        list.add(blk("sciFileSizeKB2", "d", "",
                "(double)(new java.io.File(%1$s).length() / 1024)",
                "file size KB of %s"));

        // 📁 File: дата изменения файла
        list.add(blk("sciFileLastModified2", "d", "",
                "(double) new java.io.File(%1$s).lastModified()",
                "last modified of %s"));

        // 📁 File: скрытый файл
        list.add(blk("sciFileIsHidden2", "b", "",
                "new java.io.File(%1$s).isHidden()",
                "file %s is hidden"));

        // 📁 File: только для чтения
        list.add(blk("sciFileReadOnly", "b", "",
                "new java.io.File(%1$s).canRead() && !new java.io.File(%1$s).canWrite()",
                "file %s is read only"));

        // 📁 File: сделать файл только для чтения
        list.add(blk("sciSetReadOnly", "b", "",
                "new java.io.File(%1$s).setReadOnly()",
                "set read only %s"));

        // 📁 File: получить MIME-тип
        list.add(blk("sciMimeType", "d", "",
                "((new Object() { public String f() { try { String _n = new java.io.File(%1$s).getName(); String _e = _n.substring(_n.lastIndexOf('.') + 1).toLowerCase(); if (_e.equals(\"jpg\") || _e.equals(\"jpeg\")) return \"image/jpeg\"; if (_e.equals(\"png\")) return \"image/png\"; if (_e.equals(\"pdf\")) return \"application/pdf\"; if (_e.equals(\"txt\")) return \"text/plain\"; return \"application/octet-stream\"; } catch (Throwable _t) { return \"application/octet-stream\"; } } }).f())",
                "mime type of %s"));

        // 📁 File: получить все файлы с расширением
        list.add(blk("sciFileFindByExt", "d", "",
                "((new Object() { public String f() { try { java.io.File _d = new java.io.File(%1$s); StringBuilder _sb = new StringBuilder(); java.io.File[] _fs = _d.listFiles(); if (_fs != null) { for (java.io.File _f : _fs) { if (_f.getName().endsWith(\".\" + %2$s)) _sb.append(_f.getAbsolutePath()).append(\"\\n\"); } } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "find files with ext %s in %s"));

        // 📁 File: проверить, является ли файл картинкой
        list.add(blk("sciIsImage", "b", "",
                "((new Object() { public boolean f() { try { String _n = new java.io.File(%1$s).getName().toLowerCase(); return _n.endsWith(\".jpg\") || _n.endsWith(\".jpeg\") || _n.endsWith(\".png\") || _n.endsWith(\".gif\") || _n.endsWith(\".bmp\") || _n.endsWith(\".webp\"); } catch (Throwable _t) { return false; } } }).f())",
                "file %s is image"));

        // 📁 File: проверить, является ли файл видео
        list.add(blk("sciIsVideo", "b", "",
                "((new Object() { public boolean f() { try { String _n = new java.io.File(%1$s).getName().toLowerCase(); return _n.endsWith(\".mp4\") || _n.endsWith(\".avi\") || _n.endsWith(\".mkv\") || _n.endsWith(\".mov\") || _n.endsWith(\".3gp\"); } catch (Throwable _t) { return false; } } }).f())",
                "file %s is video"));

        // 📁 File: проверить, является ли файл аудио
        list.add(blk("sciIsAudio", "b", "",
                "((new Object() { public boolean f() { try { String _n = new java.io.File(%1$s).getName().toLowerCase(); return _n.endsWith(\".mp3\") || _n.endsWith(\".wav\") || _n.endsWith(\".ogg\") || _n.endsWith(\".m4a\") || _n.endsWith(\".flac\"); } catch (Throwable _t) { return false; } } }).f())",
                "file %s is audio"));

        // 📁 File: проверить, является ли файл текстом
        list.add(blk("sciIsText", "b", "",
                "((new Object() { public boolean f() { try { String _n = new java.io.File(%1$s).getName().toLowerCase(); return _n.endsWith(\".txt\") || _n.endsWith(\".json\") || _n.endsWith(\".xml\") || _n.endsWith(\".html\") || _n.endsWith(\".css\") || _n.endsWith(\".js\") || _n.endsWith(\".java\") || _n.endsWith(\".kt\"); } catch (Throwable _t) { return false; } } }).f())",
                "file %s is text"));

        // 📁 File: получить владельца файла (требует root)
        list.add(blk("sciFileOwner", "d", "",
                "((new Object() { public String f() { try { java.io.File _f = new java.io.File(%1$s); return _f.getParentFile().getName() + \"/\" + _f.getName(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "file owner of %s"));

        // 📁 File: сравнить два файла (размер)
        list.add(blk("sciFileCompare", "b", "",
                "new java.io.File(%1$s).length() == new java.io.File(%2$s).length()",
                "files %s and %s same size"));

        // 📁 File: получить все файлы рекурсивно
        list.add(blk("sciFileListAll", "d", "",
                "((new Object() { public String f() { try { StringBuilder _sb = new StringBuilder(); java.util.Stack _st = new java.util.Stack(); _st.push(new java.io.File(%1$s)); while (!_st.isEmpty()) { java.io.File _f = (java.io.File) _st.pop(); if (_f.isDirectory()) { java.io.File[] _fs = _f.listFiles(); if (_fs != null) { for (java.io.File _c : _fs) _st.push(_c); } } else { _sb.append(_f.getAbsolutePath()).append(\"\\n\"); } } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "list all files in %s recursively"));

        // 📁 File: получить размер папки в КБ
        list.add(blk("sciDirSizeKB", "d", "",
                "((new Object() { public double f() { try { long _s = 0; java.util.Stack _st = new java.util.Stack(); _st.push(new java.io.File(%1$s)); while (!_st.isEmpty()) { java.io.File _f = (java.io.File) _st.pop(); if (_f.isDirectory()) { java.io.File[] _fs = _f.listFiles(); if (_fs != null) { for (java.io.File _c : _fs) _st.push(_c); } } else { _s += _f.length(); } } return (double) (_s / 1024); } catch (Throwable _t) { return 0.0; } } }).f())",
                "dir size KB of %s"));

        // 📁 File: удалить папку (рекурсивно)
        list.add(blk("sciDeleteDirRecursive", " ", "",
                "((new Object() { public void f() { try { java.io.File _d = new java.io.File(%1$s); if (_d.isDirectory()) { java.io.File[] _fs = _d.listFiles(); if (_fs != null) { for (java.io.File _c : _fs) _c.delete(); } } _d.delete(); } catch (Throwable _t) {} } }).f());",
                "delete dir %s recursively"));

        // 📁 File: создать пустой файл
        list.add(blk("sciCreateFile", "b", "",
                "((new Object() { public boolean f() { try { return new java.io.File(%1$s).createNewFile(); } catch (Throwable _t) { return false; } } }).f())",
                "create file %s"));

        // 📁 File: получить имя файла из пути
        list.add(blk("sciFileNameFromPath", "d", "",
                "new java.io.File(%1$s).getName()",
                "file name from %s"));

        // 📁 File: получить расширение файла
        list.add(blk("sciFileExtFromPath", "d", "",
                "((new Object() { public String f() { try { String _n = new java.io.File(%1$s).getName(); return _n.substring(_n.lastIndexOf('.') + 1); } catch (Throwable _t) { return \"\"; } } }).f())",
                "file extension from %s"));

        // 🎨 UI: установить шрифт (monospace)
        list.add(blk("sciSetMonospace", " ", "",
                "%1$s.setTypeface(android.graphics.Typeface.MONOSPACE);",
                "set monospace on %s"));

        // 🎨 UI: установить шрифт (serif)
        list.add(blk("sciSetSerif", " ", "",
                "%1$s.setTypeface(android.graphics.Typeface.SERIF);",
                "set serif on %s"));

        // 🎨 UI: установить шрифт (sans-serif)
        list.add(blk("sciSetSansSerif", " ", "",
                "%1$s.setTypeface(android.graphics.Typeface.SANS_SERIF);",
                "set sans-serif on %s"));

        // 🎨 UI: установить шрифт (bold)
        list.add(blk("sciSetTypefaceBold", " ", "",
                "%1$s.setTypeface(null, android.graphics.Typeface.BOLD);",
                "set bold on %s"));

        // 🎨 UI: установить шрифт (italic)
        list.add(blk("sciSetTypefaceItalic", " ", "",
                "%1$s.setTypeface(null, android.graphics.Typeface.ITALIC);",
                "set italic on %s"));

        // 🎨 UI: установить шрифт (bold+italic)
        list.add(blk("sciSetTypefaceBoldItalic", " ", "",
                "%1$s.setTypeface(null, android.graphics.Typeface.BOLD_ITALIC);",
                "set bold italic on %s"));

        // 🎨 UI: межбуквенный интервал (em)
        list.add(blk("sciSetLetterSpacingEm", " ", "",
                "%1$s.setLetterSpacing((float)(%2$s));",
                "letter spacing %s em on %s"));

        // 🎨 UI: межстрочный интервал (multiplier)
        list.add(blk("sciSetLineSpacingMult", " ", "",
                "%1$s.setLineSpacing(0, (float)(%2$s));",
                "line spacing multiplier %s on %s"));

        // 🎨 UI: тень текста
        list.add(blk("sciSetTextShadow", " ", "",
                "%1$s.setShadowLayer((float)(%2$s), (float)(%3$s), (float)(%4$s), (int)(%5$s));",
                "text shadow %s radius %s dx %s dy %s color %d"));

        // 🎨 UI: выравнивание текста (left)
        list.add(blk("sciSetTextLeft", " ", "",
                "%1$s.setGravity(android.view.Gravity.LEFT);",
                "text left %s"));

        // 🎨 UI: выравнивание текста (center)
        list.add(blk("sciSetTextCenter", " ", "",
                "%1$s.setGravity(android.view.Gravity.CENTER);",
                "text center %s"));

        // 🎨 UI: выравнивание текста (right)
        list.add(blk("sciSetTextRight", " ", "",
                "%1$s.setGravity(android.view.Gravity.RIGHT);",
                "text right %s"));

        // 🎨 UI: отступ (padding) сверху
        list.add(blk("sciSetPaddingTop", " ", "",
                "%1$s.setPadding(%1$s.getPaddingLeft(), (int)(%2$s), %1$s.getPaddingRight(), %1$s.getPaddingBottom());",
                "padding top %d on %s"));

        // 🎨 UI: отступ (padding) снизу
        list.add(blk("sciSetPaddingBottom", " ", "",
                "%1$s.setPadding(%1$s.getPaddingLeft(), %1$s.getPaddingTop(), %1$s.getPaddingRight(), (int)(%2$s));",
                "padding bottom %d on %s"));

        // 🎨 UI: отступ (padding) слева
        list.add(blk("sciSetPaddingLeft", " ", "",
                "%1$s.setPadding((int)(%2$s), %1$s.getPaddingTop(), %1$s.getPaddingRight(), %1$s.getPaddingBottom());",
                "padding left %d on %s"));

        // 🎨 UI: отступ (padding) справа
        list.add(blk("sciSetPaddingRight", " ", "",
                "%1$s.setPadding(%1$s.getPaddingLeft(), %1$s.getPaddingTop(), (int)(%2$s), %1$s.getPaddingBottom());",
                "padding right %d on %s"));

        // 🎨 UI: ширина (dp)
        list.add(blk("sciSetWidthDp", " ", "",
                "((new Object() { public void f() { try { android.view.ViewGroup.LayoutParams _p = %1$s.getLayoutParams(); _p.width = (int) android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, (float)(%2$s), %3$s.getResources().getDisplayMetrics()); %1$s.setLayoutParams(_p); } catch (Throwable _t) {} } }).f());",
                "set width %d dp on from %m.context"));

        // 🎨 UI: высота (dp)
        list.add(blk("sciSetHeightDp", " ", "",
                "((new Object() { public void f() { try { android.view.ViewGroup.LayoutParams _p = %1$s.getLayoutParams(); _p.height = (int) android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, (float)(%2$s), %3$s.getResources().getDisplayMetrics()); %1$s.setLayoutParams(_p); } catch (Throwable _t) {} } }).f());",
                "set height %d dp on from %m.context"));

        // 🎨 UI: минимальная ширина (dp)
        list.add(blk("sciSetMinWidthDp", " ", "",
                "%1$s.setMinimumWidth((int) android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, (float)(%2$s), %3$s.getResources().getDisplayMetrics()));",
                "min width %d dp on from %m.context"));

        // 🎨 UI: минимальная высота (dp)
        list.add(blk("sciSetMinHeightDp", " ", "",
                "%1$s.setMinimumHeight((int) android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, (float)(%2$s), %3$s.getResources().getDisplayMetrics()));",
                "min height %d dp on from %m.context"));

        // 🎨 UI: максимальная ширина (dp)
        list.add(blk("sciSetMaxWidthDp", " ", "",
                "%1$s.setMaxWidth((int) android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, (float)(%2$s), %3$s.getResources().getDisplayMetrics()));",
                "max width %d dp on from %m.context"));

        // 🎨 UI: максимальная высота (dp)
        list.add(blk("sciSetMaxHeightDp", " ", "",
                "%1$s.setMaxHeight((int) android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, (float)(%2$s), %3$s.getResources().getDisplayMetrics()));",
                "max height %d dp on from %m.context"));

        // 🎬 Animation: fade in (с задержкой)
        list.add(blk("sciFadeInDelay", " ", "",
                "%1$s.animate().alpha(1.0f).setStartDelay((long)(%3$s)).setDuration((long)(%2$s)).start();",
                "fade in %s over %d ms delay %d ms"));

        // 🎬 Animation: fade out (с задержкой)
        list.add(blk("sciFadeOutDelay", " ", "",
                "%1$s.animate().alpha(0.0f).setStartDelay((long)(%3$s)).setDuration((long)(%2$s)).start();",
                "fade out %s over %d ms delay %d ms"));

        // 🎬 Animation: slide up
        list.add(blk("sciSlideUp", " ", "",
                "%1$s.animate().translationY(-(float)(%2$s)).setDuration((long)(%3$s)).start();",
                "slide up %s to -Y %s over %d ms"));

        // 🎬 Animation: slide left
        list.add(blk("sciSlideLeft", " ", "",
                "%1$s.animate().translationX(-(float)(%2$s)).setDuration((long)(%3$s)).start();",
                "slide left %s to -X %s over %d ms"));

        // 🎬 Animation: scale up
        list.add(blk("sciScaleUp", " ", "",
                "%1$s.animate().scaleX((float)(%2$s)).scaleY((float)(%2$s)).setDuration((long)(%3$s)).start();",
                "scale up %s to %s over %d ms"));

        // 🎬 Animation: scale down
        list.add(blk("sciScaleDown", " ", "",
                "%1$s.animate().scaleX((float)(%2$s)).scaleY((float)(%2$s)).setDuration((long)(%3$s)).start();",
                "scale down %s to %s over %d ms"));

        // 🎬 Animation: rotate to 360
        list.add(blk("sciRotate360", " ", "",
                "%1$s.animate().rotation(360f).setDuration((long)(%2$s)).start();",
                "rotate %s 360 over %d ms"));

        // 🎬 Animation: bounce (отскок)
        list.add(blk("sciBounce", " ", "",
                "((new Object() { public void f() { try { android.animation.ObjectAnimator _a = android.animation.ObjectAnimator.ofFloat(%1$s, \"translationY\", 0f, -50f, 0f); _a.setDuration((long)(%2$s)); _a.setInterpolator(new android.view.animation.BounceInterpolator()); _a.start(); } catch (Throwable _t) {} } }).f());",
                "bounce %s over %d ms"));

        // 🎬 Animation: shake (тряска)
        list.add(blk("sciShake", " ", "",
                "((new Object() { public void f() { try { android.animation.ObjectAnimator _a = android.animation.ObjectAnimator.ofFloat(%1$s, \"translationX\", 0f, 25f, -25f, 25f, -25f, 15f, -15f, 6f, -6f, 0f); _a.setDuration((long)(%2$s)); _a.start(); } catch (Throwable _t) {} } }).f());",
                "shake %s over %d ms"));

        // 🎬 Animation: flip (переворот)
        list.add(blk("sciFlip", " ", "",
                "((new Object() { public void f() { try { android.animation.ObjectAnimator _a = android.animation.ObjectAnimator.ofFloat(%1$s, \"rotationY\", 0f, 180f, 360f); _a.setDuration((long)(%2$s)); _a.start(); } catch (Throwable _t) {} } }).f());",
                "flip %s over %d ms"));

        // 🎬 Animation: blink (мигание)
        list.add(blk("sciBlink", " ", "",
                "((new Object() { public void f() { try { android.animation.ObjectAnimator _a = android.animation.ObjectAnimator.ofFloat(%1$s, \"alpha\", 1f, 0f, 1f); _a.setDuration((long)(%2$s)); _a.setRepeatCount(android.animation.ValueAnimator.INFINITE); _a.start(); } catch (Throwable _t) {} } }).f());",
                "blink %s over %d ms"));

        // 🎬 Animation: stop animation
        list.add(blk("sciStopAnimation", " ", "",
                "%1$s.animate().cancel();",
                "stop animation %s"));

        // 🖼️ Custom View: фон с градиентом (радиальный)
        list.add(blk("sciSetRadialGradient", " ", "",
                "((new Object() { public void f() { try { android.graphics.drawable.GradientDrawable _g = new android.graphics.drawable.GradientDrawable(); _g.setGradientType(android.graphics.drawable.GradientDrawable.RADIAL_GRADIENT); _g.setColors(new int[]{(int)(%2$s), (int)(%3$s)}); %1$s.setBackground(_g); } catch (Throwable _t) {} } }).f());",
                "radial gradient %d to %d on %s"));

        // 🖼️ Custom View: фон с градиентом (линейный)
        list.add(blk("sciSetLinearGradient", " ", "",
                "((new Object() { public void f() { try { android.graphics.drawable.GradientDrawable _g = new android.graphics.drawable.GradientDrawable(android.graphics.drawable.GradientDrawable.Orientation.LEFT_RIGHT, new int[]{(int)(%2$s), (int)(%3$s)}); %1$s.setBackground(_g); } catch (Throwable _t) {} } }).f());",
                "linear gradient %d to %d on %s"));

        // 🖼️ Custom View: фон с обводкой и скруглением
        list.add(blk("sciSetRoundBorder", " ", "",
                "((new Object() { public void f() { try { android.graphics.drawable.GradientDrawable _g = new android.graphics.drawable.GradientDrawable(); _g.setCornerRadius((float)(%2$s)); _g.setStroke((int)(%3$s), (int)(%4$s)); _g.setColor((int)(%5$s)); %1$s.setBackground(_g); } catch (Throwable _t) {} } }).f());",
                "round border %s radius %s width %d color %d bg %d"));

        // 🖼️ Custom View: прозрачность (0-255)
        list.add(blk("sciSetAlphaInt", " ", "",
                "%1$s.setAlpha((float)(%2$s) / 255.0f);",
                "alpha %d on %s"));

        // 🖼️ Custom View: поворот (0-360)
        list.add(blk("sciSetRotationDeg", " ", "",
                "%1$s.setRotation((float)(%2$s));",
                "rotation %s deg on %s"));

        // 🖼️ Custom View: масштаб X
        list.add(blk("sciSetScaleX2", " ", "",
                "%1$s.setScaleX((float)(%2$s));",
                "scale X %s on %s"));

        // 🖼️ Custom View: масштаб Y
        list.add(blk("sciSetScaleY2", " ", "",
                "%1$s.setScaleY((float)(%2$s));",
                "scale Y %s on %s"));

        // 🖼️ Custom View: тень (elevation)
        list.add(blk("sciSetElevation2", " ", "",
                "%1$s.setElevation((float)(%2$s));",
                "elevation %s on %s"));

        // 🖼️ Custom View: цвет фона (hex)
        list.add(blk("sciSetBgHex", " ", "",
                "%1$s.setBackgroundColor(android.graphics.Color.parseColor(%2$s));",
                "background hex %s on %s"));

        // 🖼️ Custom View: цвет текста (hex)
        list.add(blk("sciSetTextColorHex", " ", "",
                "%1$s.setTextColor(android.graphics.Color.parseColor(%2$s));",
                "text color hex %s on %s"));

        // 🖼️ Custom View: радиус скругления (dp)
        list.add(blk("sciSetCornerRadiusDp", " ", "",
                "((new Object() { public void f() { try { android.graphics.drawable.GradientDrawable _g = new android.graphics.drawable.GradientDrawable(); _g.setCornerRadius((float) android.util.TypedValue.applyDimension(android.util.TypedValue.COMPLEX_UNIT_DIP, (float)(%2$s), %3$s.getResources().getDisplayMetrics())); %1$s.setBackground(_g); } catch (Throwable _t) {} } }).f());",
                "corner radius %d dp on from %m.context"));

        // 🖼️ Custom View: фон (color)
        list.add(blk("sciSetBgColor", " ", "",
                "%1$s.setBackgroundColor((int)(%2$s));",
                "background color %d on %s"));

        // 🖼️ Custom View: цвет текста (int)
        list.add(blk("sciSetTextColorInt", " ", "",
                "%1$s.setTextColor((int)(%2$s));",
                "text color %d on %s"));

        // 🖼️ Custom View: размер текста (sp)
        list.add(blk("sciSetTextSizeSp2", " ", "",
                "%1$s.setTextSize((float)(%2$s));",
                "text size %s sp on %s"));

        // 🖼️ Custom View: размер текста (px)
        list.add(blk("sciSetTextSizePx", " ", "",
                "%1$s.setTextSize(android.util.TypedValue.COMPLEX_UNIT_PX, (float)(%2$s));",
                "text size %s px on %s"));

        // 🖼️ Custom View: размер текста (dp)
        list.add(blk("sciSetTextSizeDp", " ", "",
                "%1$s.setTextSize(android.util.TypedValue.COMPLEX_UNIT_DIP, (float)(%2$s));",
                "text size %s dp on %s"));

        // 🖼️ Custom View: видимость (invisible/gone/visible)
        list.add(blk("sciSetVisibility2", " ", "",
                "%1$s.setVisibility((int)(%2$s));",
                "visibility %d on %s"));

        // 🖼️ Custom View: кликабельность
        list.add(blk("sciSetClickable2", " ", "",
                "%1$s.setClickable(%2$s);",
                "clickable %b on %s"));

        // 🖼️ Custom View: фокусируемость
        list.add(blk("sciSetFocusable2", " ", "",
                "%1$s.setFocusable(%2$s);",
                "focusable %b on %s"));

        // 🖼️ Custom View: включен/выключен
        list.add(blk("sciSetEnabled2", " ", "",
                "%1$s.setEnabled(%2$s);",
                "enabled %b on %s"));

        // 🖼️ Custom View: выделение (selected)
        list.add(blk("sciSetSelected", " ", "",
                "%1$s.setSelected(%2$s);",
                "selected %b on %s"));

        // 🖼️ Custom View: активирован (activated)
        list.add(blk("sciSetActivated", " ", "",
                "%1$s.setActivated(%2$s);",
                "activated %b on %s"));

        // 🌐 Network: HTTP GET (с возвратом заголовков)
        list.add(blk("sciHttpGetFull", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"GET\"); StringBuilder _sb = new StringBuilder(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http get full %s"));

        // 🌐 Network: HTTP HEAD (получить все заголовки)
        list.add(blk("sciHttpHeadFull", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"HEAD\"); StringBuilder _sb = new StringBuilder(); for (java.util.Map.Entry<String, java.util.List<String>> _e : _c.getHeaderFields().entrySet()) { _sb.append(_e.getKey()).append(\": \").append(_e.getValue()).append(\"\\n\"); } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http head full %s"));

        // 🌐 Network: HTTP OPTIONS
        list.add(blk("sciHttpOptions", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"OPTIONS\"); StringBuilder _sb = new StringBuilder(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http options %s"));

        // 🌐 Network: скачать файл (с прогрессом)
        list.add(blk("sciDownloadFile", " ", "",
                "((new Object() { public void f() { try { java.net.URL _u = new java.net.URL(%1$s); java.net.HttpURLConnection _c = (java.net.HttpURLConnection) _u.openConnection(); _c.connect(); int _total = _c.getContentLength(); java.io.InputStream _is = _c.getInputStream(); java.io.FileOutputStream _os = new java.io.FileOutputStream(%2$s); byte[] _b = new byte[4096]; int _len; long _down = 0; while ((_len = _is.read(_b)) > 0) { _os.write(_b, 0, _len); _down += _len; } _is.close(); _os.close(); } catch (Throwable _t) {} } }).f());",
                "download %s to %s"));

        // 🌐 Network: загрузить файл (POST)
        list.add(blk("sciUploadFile", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"POST\"); _c.setDoOutput(true); _c.setRequestProperty(\"Content-Type\", \"multipart/form-data\"); java.io.FileInputStream _is = new java.io.FileInputStream(%2$s); java.io.OutputStream _os = _c.getOutputStream(); byte[] _b = new byte[4096]; int _len; while ((_len = _is.read(_b)) > 0) _os.write(_b, 0, _len); _is.close(); _os.flush(); _os.close(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "upload %s to %s"));

        // 🌐 Network: JSON → список строк
        list.add(blk("sciJsonToStringList", " ", "",
                "((new Object() { public void f() { try { org.json.JSONArray _a = new org.json.JSONArray(%1$s); %2$s.clear(); for (int _i = 0; _i < _a.length(); _i++) %2$s.add(_a.getString(_i)); } catch (Throwable _t) {} } }).f());",
                "json %s to list %s"));

        // 🌐 Network: JSON → список чисел
        list.add(blk("sciJsonToNumberList", " ", "",
                "((new Object() { public void f() { try { org.json.JSONArray _a = new org.json.JSONArray(%1$s); %2$s.clear(); for (int _i = 0; _i < _a.length(); _i++) %2$s.add(_a.getDouble(_i)); } catch (Throwable _t) {} } }).f());",
                "json %s to number list %s"));

        // 🌐 Network: список строк → JSON
        list.add(blk("sciStringListToJson", "d", "",
                "new org.json.JSONArray(%1$s).toString()",
                "list %s to json"));

        // 🌐 Network: список чисел → JSON
        list.add(blk("sciNumberListToJson", "d", "",
                "new org.json.JSONArray(%1$s).toString()",
                "number list %s to json"));

        // 🌐 Network: Map → JSON
        list.add(blk("sciMapToJson", "d", "",
                "new org.json.JSONObject(%1$s).toString()",
                "map %s to json"));

        // 🌐 Network: JSON → Map
        list.add(blk("sciJsonToMap", "d", "",
                "((new Object() { public String f() { try { org.json.JSONObject _o = new org.json.JSONObject(%1$s); java.util.Iterator _i = _o.keys(); StringBuilder _sb = new StringBuilder(); while (_i.hasNext()) { String _k = (String) _i.next(); _sb.append(_k).append(\"=\").append(_o.getString(_k)).append(\";\"); } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "json %s to map"));

        // 🌐 Network: JSON → размер
        list.add(blk("sciJsonLength", "d", "",
                "((new Object() { public double f() { try { return (double) new org.json.JSONArray(%1$s).length(); } catch (Throwable _t) { return 0.0; } } }).f())",
                "json %s length"));

        // 🌐 Network: JSON → есть ли ключ
        list.add(blk("sciJsonHasKey", "b", "",
                "((new Object() { public boolean f() { try { return new org.json.JSONObject(%1$s).has(%2$s); } catch (Throwable _t) { return false; } } }).f())",
                "json %s has key %s"));

        // 🌐 Network: XML → значение тега
        list.add(blk("sciXmlTagValue", "d", "",
                "((new Object() { public String f() { try { javax.xml.parsers.DocumentBuilder _b = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder(); org.w3c.dom.Document _d = _b.parse(new java.io.ByteArrayInputStream(%1$s.getBytes())); org.w3c.dom.NodeList _n = _d.getElementsByTagName(%2$s); return _n.getLength() > 0 ? _n.item(0).getTextContent() : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "xml %s tag %s value"));

        // 🌐 Network: XML → количество тегов
        list.add(blk("sciXmlTagCount", "d", "",
                "((new Object() { public double f() { try { javax.xml.parsers.DocumentBuilder _b = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder(); org.w3c.dom.Document _d = _b.parse(new java.io.ByteArrayInputStream(%1$s.getBytes())); return (double) _d.getElementsByTagName(%2$s).getLength(); } catch (Throwable _t) { return 0.0; } } }).f())",
                "xml %s tag %s count"));

        // 🌐 Network: XML → атрибут тега
        list.add(blk("sciXmlAttr2", "d", "",
                "((new Object() { public String f() { try { javax.xml.parsers.DocumentBuilder _b = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder(); org.w3c.dom.Document _d = _b.parse(new java.io.ByteArrayInputStream(%1$s.getBytes())); org.w3c.dom.NodeList _n = _d.getElementsByTagName(%2$s); if (_n.getLength() > 0) { org.w3c.dom.NamedNodeMap _a = _n.item(0).getAttributes(); if (_a != null && _a.getNamedItem(%3$s) != null) return _a.getNamedItem(%3$s).getNodeValue(); } return \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "xml %s tag %s attr %s"));

        // 🌐 Network: построить URL с параметрами
        list.add(blk("sciBuildUrlParams", "d", "",
                "((new Object() { public String f() { try { String _u = %1$s; String _p = %2$s; if (_p == null || _p.isEmpty()) return _u; String _sep = _u.contains(\"?\") ? \"&\" : \"?\"; StringBuilder _sb = new StringBuilder(_u).append(_sep); String[] _parts = _p.split(\"&\"); for (int _i = 0; _i < _parts.length; _i++) { if (_i > 0) _sb.append(\"&\"); String[] _kv = _parts[_i].split(\"=\"); if (_kv.length == 2) _sb.append(java.net.URLEncoder.encode(_kv[0], \"UTF-8\")).append(\"=\").append(java.net.URLEncoder.encode(_kv[1], \"UTF-8\")); } return _sb.toString(); } catch (Throwable _t) { return %1$s; } } }).f())",
                "build url %s with params %s"));

        // 🌐 Network: получить все параметры URL
        list.add(blk("sciUrlAllParams", "d", "",
                "((new Object() { public String f() { try { String _q = new java.net.URL(%1$s).getQuery(); if (_q == null) return \"\"; StringBuilder _sb = new StringBuilder(); for (String _p : _q.split(\"&\")) { String[] _kv = _p.split(\"=\"); if (_kv.length == 2) _sb.append(_kv[0]).append(\"=\").append(java.net.URLDecoder.decode(_kv[1], \"UTF-8\")).append(\";\"); } return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "url params of %s"));

        // 🌐 Network: проверить, является ли строка JSON
        list.add(blk("sciIsJson", "b", "",
                "((new Object() { public boolean f() { try { new org.json.JSONObject(%1$s); return true; } catch (Throwable _t) { try { new org.json.JSONArray(%1$s); return true; } catch (Throwable _t2) { return false; } } } }).f())",
                "is json %s"));

        // 🌐 Network: проверить, является ли строка XML
        list.add(blk("sciIsXml", "b", "",
                "((new Object() { public boolean f() { try { javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new java.io.ByteArrayInputStream(%1$s.getBytes())); return true; } catch (Throwable _t) { return false; } } }).f())",
                "is xml %s"));

        // 🌐 Network: получить заголовок Accept
        list.add(blk("sciHttpAccept", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestProperty(\"Accept\", %2$s); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http accept %s with %s"));

        // 🌐 Network: получить заголовок User-Agent
        list.add(blk("sciHttpUserAgent", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestProperty(\"User-Agent\", %2$s); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http user agent %s with %s"));

        // 🌐 Network: скачать JSON и получить поле
        list.add(blk("sciHttpJsonField2", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return new org.json.JSONObject(_sb.toString()).optString(%2$s); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http json %s from %s"));

        // 🌐 Network: HTTP Basic Auth (base64)
        list.add(blk("sciHttpBasicAuth2", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); String _auth = android.util.Base64.encodeToString((%2$s + \":\" + %3$s).getBytes(), android.util.Base64.NO_WRAP); _c.setRequestProperty(\"Authorization\", \"Basic \" + _auth); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http basic auth %s user %s pass %s"));

        // 🌐 Network: HTTP Bearer Token
        list.add(blk("sciHttpBearer", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestProperty(\"Authorization\", \"Bearer \" + %2$s); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http bearer %s token %s"));

        // 🌐 Network: HTTP POST с JSON
        list.add(blk("sciHttpPostJson2", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"POST\"); _c.setRequestProperty(\"Content-Type\", \"application/json\"); _c.setDoOutput(true); java.io.OutputStream _os = _c.getOutputStream(); _os.write(%2$s.getBytes(\"UTF-8\")); _os.flush(); _os.close(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http post json %s data %s"));

        // 🌐 Network: HTTP POST Form
        list.add(blk("sciHttpPostForm2", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"POST\"); _c.setRequestProperty(\"Content-Type\", \"application/x-www-form-urlencoded\"); _c.setDoOutput(true); java.io.OutputStream _os = _c.getOutputStream(); _os.write(%2$s.getBytes(\"UTF-8\")); _os.flush(); _os.close(); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http post form %s data %s"));

        // 🌐 Network: скачать изображение в Bitmap
        list.add(blk("sciDownloadImage", "d", "",
                "((new Object() { public String f() { try { java.net.URL _u = new java.net.URL(%1$s); android.graphics.Bitmap _b = android.graphics.BitmapFactory.decodeStream(_u.openStream()); return _b != null ? android.util.Base64.encodeToString(_b.toString().getBytes(), android.util.Base64.NO_WRAP) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "download image %s"));

        // 🌐 Network: скачать текст
        list.add(blk("sciDownloadText", "d", "",
                "((new Object() { public String f() { try { java.net.URL _u = new java.net.URL(%1$s); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_u.openStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l).append(\"\\n\"); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "download text %s"));

        // 🌐 Network: проверить доступность (ping)
        list.add(blk("sciPingCheck", "b", "",
                "((new Object() { public boolean f() { try { return java.net.InetAddress.getByName(%1$s).isReachable(3000); } catch (Throwable _t) { return false; } } }).f())",
                "ping check %s"));

        // 🌐 Network: получить IP по хосту
        list.add(blk("sciDnsLookup", "d", "",
                "((new Object() { public String f() { try { return java.net.InetAddress.getByName(%1$s).getHostAddress(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "dns lookup %s"));

        // 🌐 Network: проверить SSL
        list.add(blk("sciSslValid", "b", "",
                "((new Object() { public boolean f() { try { javax.net.ssl.HttpsURLConnection _c = (javax.net.ssl.HttpsURLConnection) new java.net.URL(%1$s).openConnection(); _c.connect(); return true; } catch (Throwable _t) { return false; } } }).f())",
                "ssl valid %s"));

        // 🌐 Network: получить сертификат
        list.add(blk("sciSslCert", "d", "",
                "((new Object() { public String f() { try { javax.net.ssl.HttpsURLConnection _c = (javax.net.ssl.HttpsURLConnection) new java.net.URL(%1$s).openConnection(); _c.connect(); return _c.getCipherSuite(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "ssl cert %s"));

        // 🌐 Network: HTTP с таймаутом
        list.add(blk("sciHttpGetTimeout2", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setConnectTimeout((int)(%2$s)); _c.setReadTimeout((int)(%2$s)); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http get %s timeout %d"));

        // 🌐 Network: HTTP с прокси
        list.add(blk("sciHttpProxy", "d", "",
                "((new Object() { public String f() { try { java.net.Proxy _p = new java.net.Proxy(java.net.Proxy.Type.HTTP, new java.net.InetSocketAddress(%2$s, (int)(%3$s))); java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(_p); java.io.BufferedReader _r = new java.io.BufferedReader(new java.io.InputStreamReader(_c.getInputStream())); StringBuilder _sb = new StringBuilder(); String _l; while ((_l = _r.readLine()) != null) _sb.append(_l); _r.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "http proxy %s host %s port %d"));

        // 🌐 Network: кодировать Base64 URL
        list.add(blk("sciUrlBase64", "d", "",
                "android.util.Base64.encodeToString(%1$s.getBytes(), android.util.Base64.URL_SAFE | android.util.Base64.NO_WRAP)",
                "url base64 %s"));

        // 🌐 Network: декодировать Base64 URL
        list.add(blk("sciUrlBase64Decode", "d", "",
                "new String(android.util.Base64.decode(%1$s, android.util.Base64.URL_SAFE | android.util.Base64.NO_WRAP))",
                "url base64 decode %s"));

        // 🌐 Network: разбить URL на части
        list.add(blk("sciUrlParse", "d", "",
                "((new Object() { public String f() { try { java.net.URL _u = new java.net.URL(%1$s); return \"protocol=\" + _u.getProtocol() + \";host=\" + _u.getHost() + \";port=\" + _u.getPort() + \";path=\" + _u.getPath() + \";query=\" + _u.getQuery(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "url parse %s"));

        // 🌐 Network: получить протокол URL
        list.add(blk("sciUrlProtocol", "d", "",
                "((new Object() { public String f() { try { return new java.net.URL(%1$s).getProtocol(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "url protocol of %s"));

        // 🌐 Network: получить порт URL
        list.add(blk("sciUrlPort", "d", "",
                "((new Object() { public double f() { try { return (double) new java.net.URL(%1$s).getPort(); } catch (Throwable _t) { return -1.0; } } }).f())",
                "url port of %s"));

        // 🌐 Network: получить путь URL
        list.add(blk("sciUrlPath", "d", "",
                "((new Object() { public String f() { try { return new java.net.URL(%1$s).getPath(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "url path of %s"));

        // 🌐 Network: проверить, является ли строка URL
        list.add(blk("sciIsUrl", "b", "",
                "((new Object() { public boolean f() { try { new java.net.URL(%1$s); return true; } catch (Throwable _t) { return false; } } }).f())",
                "is url %s"));

        // 🌐 Network: получить параметр из URL (с учётом кодирования)
        list.add(blk("sciUrlParam", "d", "",
                "((new Object() { public String f() { try { String _q = new java.net.URL(%1$s).getQuery(); if (_q == null) return \"\"; for (String _p : _q.split(\"&\")) { String[] _kv = _p.split(\"=\"); if (_kv.length == 2 && _kv[0].equals(%2$s)) return java.net.URLDecoder.decode(_kv[1], \"UTF-8\"); } return \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "url param %s from %s"));

        // 🌐 Network: соединить URL и путь
        list.add(blk("sciUrlJoin", "d", "",
                "((new Object() { public String f() { try { String _b = %1$s; String _p = %2$s; if (_b.endsWith(\"/\") && _p.startsWith(\"/\")) return _b + _p.substring(1); if (!_b.endsWith(\"/\") && !_p.startsWith(\"/\")) return _b + \"/\" + _p; return _b + _p; } catch (Throwable _t) { return %1$s; } } }).f())",
                "url join %s and %s"));

        // 🌐 Network: проверить HTTPS
        list.add(blk("sciIsHttps", "b", "",
                "((new Object() { public boolean f() { try { return new java.net.URL(%1$s).getProtocol().equals(\"https\"); } catch (Throwable _t) { return false; } } }).f())",
                "is https %s"));

        // 🌐 Network: установить User-Agent для всех запросов
        list.add(blk("sciSetUserAgent", " ", "",
                "System.setProperty(\"http.agent\", %1$s);",
                "set user agent %s"));

        // 🌐 Network: получить заголовок ответа
        list.add(blk("sciResponseHeader", "d", "",
                "((new Object() { public String f() { try { java.net.HttpURLConnection _c = (java.net.HttpURLConnection) new java.net.URL(%1$s).openConnection(); _c.setRequestMethod(\"HEAD\"); return _c.getHeaderField(%2$s); } catch (Throwable _t) { return \"\"; } } }).f())",
                "response header %s of %s"));

        // 🗄️ SQLite: вставить строку
        list.add(blk("sciDbInsert", " ", "",
                "((new Object() { public void f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); android.content.ContentValues _v = new android.content.ContentValues(); _v.put(%3$s, %4$s); _db.insert(%5$s, null, _v); _db.close(); } catch (Throwable _t) {} } }).f());",
                "db insert = into from %m.context"));

        // 🗄️ SQLite: обновить строку
        list.add(blk("sciDbUpdate", " ", "",
                "((new Object() { public void f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); android.content.ContentValues _v = new android.content.ContentValues(); _v.put(%3$s, %4$s); _db.update(%5$s, _v, %6$s, null); _db.close(); } catch (Throwable _t) {} } }).f());",
                "db update = in where from %m.context"));

        // 🗄️ SQLite: удалить строки
        list.add(blk("sciDbDelete", " ", "",
                "((new Object() { public void f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); _db.delete(%3$s, %4$s, null); _db.close(); } catch (Throwable _t) {} } }).f());",
                "db delete from where from %m.context"));

        // 🗄️ SQLite: проверить существование таблицы
        list.add(blk("sciDbTableExists", "b", "",
                "((new Object() { public boolean f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); android.database.Cursor _c = _db.rawQuery(\"SELECT name FROM sqlite_master WHERE type='table' AND name='\" + %3$s + \"'\", null); boolean _e = _c.getCount() > 0; _c.close(); _db.close(); return _e; } catch (Throwable _t) { return false; } } }).f())",
                "db table exists from %m.context"));

        // 🗄️ SQLite: получить все таблицы
        list.add(blk("sciDbTables", "d", "",
                "((new Object() { public String f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); android.database.Cursor _c = _db.rawQuery(\"SELECT name FROM sqlite_master WHERE type='table'\", null); StringBuilder _sb = new StringBuilder(); while (_c.moveToNext()) _sb.append(_c.getString(0)).append(\"\\n\"); _c.close(); _db.close(); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "db tables from %m.context"));

        // 🗄️ SQLite: получить версию БД
        list.add(blk("sciDbVersion", "d", "",
                "((new Object() { public double f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); double _v = _db.getVersion(); _db.close(); return _v; } catch (Throwable _t) { return 0.0; } } }).f())",
                "db version from %m.context"));

        // 🗄️ SQLite: сжать (VACUUM)
        list.add(blk("sciDbVacuum", " ", "",
                "((new Object() { public void f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); _db.execSQL(\"VACUUM\"); _db.close(); } catch (Throwable _t) {} } }).f());",
                "db vacuum from %m.context"));

        // 🗄️ SQLite: получить путь к БД
        list.add(blk("sciDbPath", "d", "",
                "%1$s.getDatabasePath(%2$s).getAbsolutePath()",
                "db path from %m.context"));

        // 🗄️ SQLite: удалить БД
        list.add(blk("sciDbDeleteFile", "b", "",
                "%1$s.deleteDatabase(%2$s)",
                "db delete from %m.context"));

        // 🗄️ SQLite: выполнить транзакцию
        list.add(blk("sciDbTransaction", " ", "",
                "((new Object() { public void f() { try { android.database.sqlite.SQLiteDatabase _db = %2$s.openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null); _db.beginTransaction(); try { _db.execSQL(%3$s); _db.setTransactionSuccessful(); } finally { _db.endTransaction(); } _db.close(); } catch (Throwable _t) {} } }).f());",
                "db transaction from %m.context"));

        // 🔍 Regex: проверить соответствие
        list.add(blk("sciRegexMatches2", "b", "",
                "%1$s.matches(%2$s)",
                "%s matches %s"));

        // 🔍 Regex: найти первое вхождение
        list.add(blk("sciRegexFind2", "d", "",
                "((new Object() { public String f() { try { java.util.regex.Matcher _m = java.util.regex.Pattern.compile(%2$s).matcher(%1$s); return _m.find() ? _m.group() : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "regex find %s in %s"));

        // 🔍 Regex: найти все вхождения
        list.add(blk("sciRegexFindAll2", "d", "",
                "((new Object() { public String f() { try { java.util.regex.Matcher _m = java.util.regex.Pattern.compile(%2$s).matcher(%1$s); StringBuilder _sb = new StringBuilder(); while (_m.find()) _sb.append(_m.group()).append(\"\\n\"); return _sb.toString(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "regex find all %s in %s"));

        // 🔍 Regex: заменить все
        list.add(blk("sciRegexReplaceAll2", "d", "",
                "%1$s.replaceAll(%2$s, %3$s)",
                "%s replace all %s with %s"));

        // 🔍 Regex: заменить первое
        list.add(blk("sciRegexReplaceFirst2", "d", "",
                "%1$s.replaceFirst(%2$s, %3$s)",
                "%s replace first %s with %s"));

        // 🔍 Regex: разделить
        list.add(blk("sciRegexSplit2", "d", "",
                "java.util.Arrays.toString(%1$s.split(%2$s))",
                "%s split by %s"));

        // 🔍 Regex: извлечь группу
        list.add(blk("sciRegexGroup2", "d", "",
                "((new Object() { public String f() { try { java.util.regex.Matcher _m = java.util.regex.Pattern.compile(%2$s).matcher(%1$s); return _m.find() ? _m.group((int)(%3$s)) : \"\"; } catch (Throwable _t) { return \"\"; } } }).f())",
                "regex group %d of %s in %s"));

        // 🔍 Regex: проверить, является ли regex валидным
        list.add(blk("sciRegexValid2", "b", "",
                "((new Object() { public boolean f() { try { java.util.regex.Pattern.compile(%1$s); return true; } catch (Throwable _t) { return false; } } }).f())",
                "regex %s is valid"));

        // 🔍 Regex: получить количество групп
        list.add(blk("sciRegexGroupCount", "d", "",
                "((new Object() { public double f() { try { java.util.regex.Matcher _m = java.util.regex.Pattern.compile(%2$s).matcher(%1$s); if (_m.find()) return (double) _m.groupCount(); return 0.0; } catch (Throwable _t) { return 0.0; } } }).f())",
                "regex %s group count in %s"));

        // 🎨 Color: смешать два цвета (50/50)
        list.add(blk("sciMixColor", "d", "",
                "(((int)(%1$s) & 0xFF000000) | (((((int)(%1$s) & 0xFF0000) + ((int)(%2$s) & 0xFF0000)) / 2) & 0xFF0000) | (((((int)(%1$s) & 0xFF00) + ((int)(%2$s) & 0xFF00)) / 2) & 0xFF00) | (((((int)(%1$s) & 0xFF) + ((int)(%2$s) & 0xFF)) / 2) & 0xFF))",
                "mix color %s and %s"));

        // 🎨 Color: прозрачность (0-255)
        list.add(blk("sciColorAlpha2", "d", "",
                "(double)(((int)(%1$s) >> 24) & 0xFF)",
                "alpha of %s"));

        // 🎨 Color: цвет из HEX
        list.add(blk("sciColorHex", "d", "",
                "android.graphics.Color.parseColor(%1$s)",
                "color from hex %s"));

        // 🎨 Color: цвет в HEX
        list.add(blk("sciColorToHex2", "d", "",
                "String.format(\"#%08X\", (int)(%1$s))",
                "color %s to hex"));

        // 🎨 Color: красный компонент
        list.add(blk("sciColorRed2", "d", "",
                "(double) android.graphics.Color.red((int)(%1$s))",
                "red of %s"));

        // 🎨 Color: зелёный компонент
        list.add(blk("sciColorGreen2", "d", "",
                "(double) android.graphics.Color.green((int)(%1$s))",
                "green of %s"));

        // 🎨 Color: синий компонент
        list.add(blk("sciColorBlue2", "d", "",
                "(double) android.graphics.Color.blue((int)(%1$s))",
                "blue of %s"));

        // 🎨 Color: прозрачный (0)
        list.add(blk("sciColorTransparent", "d", "",
                "android.graphics.Color.TRANSPARENT",
                "transparent color"));

        // 🎨 Color: белый
        list.add(blk("sciColorWhite", "d", "",
                "android.graphics.Color.WHITE",
                "white color"));

        // 🎨 Color: чёрный
        list.add(blk("sciColorBlack", "d", "",
                "android.graphics.Color.BLACK",
                "black color"));

        // 🎨 Color: красный
        list.add(blk("sciColorRedBase", "d", "",
                "android.graphics.Color.RED",
                "red color"));

        // 🎨 Color: зелёный
        list.add(blk("sciColorGreenBase", "d", "",
                "android.graphics.Color.GREEN",
                "green color"));

        // 🎨 Color: синий
        list.add(blk("sciColorBlueBase", "d", "",
                "android.graphics.Color.BLUE",
                "blue color"));

        // 🎨 Color: жёлтый
        list.add(blk("sciColorYellow", "d", "",
                "android.graphics.Color.YELLOW",
                "yellow color"));

        // 🎨 Color: серый
        list.add(blk("sciColorGray", "d", "",
                "android.graphics.Color.GRAY",
                "gray color"));

        // 🎨 Color: тёмно-серый
        list.add(blk("sciColorDarkGray", "d", "",
                "android.graphics.Color.DKGRAY",
                "dark gray color"));

        // 🎨 Color: светло-серый
        list.add(blk("sciColorLightGray", "d", "",
                "android.graphics.Color.LTGRAY",
                "light gray color"));

        // 🎨 Color: голубой
        list.add(blk("sciColorCyan", "d", "",
                "android.graphics.Color.CYAN",
                "cyan color"));

        // 🎨 Color: пурпурный
        list.add(blk("sciColorMagenta", "d", "",
                "android.graphics.Color.MAGENTA",
                "magenta color"));

        // 🎨 Color: с прозрачностью
        list.add(blk("sciColorWithAlpha2", "d", "",
                "android.graphics.Color.argb((int)(%1$s), android.graphics.Color.red((int)(%2$s)), android.graphics.Color.green((int)(%2$s)), android.graphics.Color.blue((int)(%2$s)))",
                "color %s with alpha %d"));

        // 📁 File: получить размер файла (МБ)
        list.add(blk("sciFileSizeMB2", "d", "",
                "(double)(new java.io.File(%1$s).length() / (1024.0 * 1024.0))",
                "file size MB of %s"));

        // 📁 File: получить размер файла (байты)
        list.add(blk("sciFileSizeBytes2", "d", "",
                "(double) new java.io.File(%1$s).length()",
                "file size bytes of %s"));

        // 📁 File: получить путь к родительской папке
        list.add(blk("sciFileParentPath", "d", "",
                "((new Object() { public String f() { try { return new java.io.File(%1$s).getParent(); } catch (Throwable _t) { return \"\"; } } }).f())",
                "file parent path of %s"));

        // 📁 File: получить абсолютный путь
        list.add(blk("sciFileAbsPath", "d", "",
                "new java.io.File(%1$s).getAbsolutePath()",
                "file absolute path of %s"));

        // 📁 File: проверить, является ли путь директорией
        list.add(blk("sciFileIsDir", "b", "",
                "new java.io.File(%1$s).isDirectory()",
                "file %s is directory"));

        // 📁 File: проверить, является ли путь файлом
        list.add(blk("sciFileIsFile2", "b", "",
                "new java.io.File(%1$s).isFile()",
                "file %s is file"));

        // 📁 File: проверить, является ли путь скрытым
        list.add(blk("sciFileIsHidden3", "b", "",
                "new java.io.File(%1$s).isHidden()",
                "file %s is hidden"));

        // 📁 File: создать пустой файл
        list.add(blk("sciFileCreate", "b", "",
                "((new Object() { public boolean f() { try { return new java.io.File(%1$s).createNewFile(); } catch (Throwable _t) { return false; } } }).f())",
                "create file %s"));
    }
}