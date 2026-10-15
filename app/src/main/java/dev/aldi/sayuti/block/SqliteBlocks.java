package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

public final class SqliteBlocks {

    private SqliteBlocks() {}

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
        list.add(blk("sqliteOpen", "v", "android.database.sqlite.SQLiteDatabase",
                "openOrCreateDatabase(%1$s, android.content.Context.MODE_PRIVATE, null)",
                "open SQLite DB %s"));

        list.add(blk("sqliteExec", " ", "",
                "%1$s.execSQL(%2$s);",
                "SQLite %m.db exec SQL %s"));

        list.add(blk("sqliteQuery", "s", "",
                "((java.util.function.Supplier<String>) () -> { StringBuilder _sb = new StringBuilder(); android.database.Cursor _c = null; try { _c = %1$s.rawQuery(%2$s, null); if (_c != null) { while (_c.moveToNext()) { for (int _i = 0; _i < _c.getColumnCount(); _i++) { _sb.append(_c.getString(_i)).append(\"|\"); } _sb.append(\"\\n\"); } } } catch (Throwable _t) {} finally { if (_c != null) _c.close(); } return _sb.toString(); }).get()",
                "SQLite %m.db query %s"));

        list.add(blk("sqliteGetCount", "d", "",
                "((java.util.function.Supplier<Double>) () -> { double _n = 0; android.database.Cursor _c = null; try { _c = %1$s.rawQuery(%2$s, null); if (_c != null) _n = (double) _c.getCount(); } catch (Throwable _t) {} finally { if (_c != null) _c.close(); } return _n; }).get()",
                "SQLite %m.db count rows in %s"));

        list.add(blk("sqliteClose", " ", "",
                "%1$s.close();",
                "SQLite %m.db close"));
    }
}
