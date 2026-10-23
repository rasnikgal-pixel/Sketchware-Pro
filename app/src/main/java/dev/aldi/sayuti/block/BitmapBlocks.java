package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Блоки для работы с Bitmap: загрузка, ресайз, поворот, кроп, сохранение.
 * Все блоки inline — не требуют хелперов.
 */
public final class BitmapBlocks {

    private BitmapBlocks() {}

    private static final String COLOR_FUNC = "#4a6cd4";
    private static final String PALETTE_FUNC = "7";
    private static final String TYPE_BITMAP = "Bitmap";

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
        list.add(blk("bitmapFromFile", "v", TYPE_BITMAP,
                "android.graphics.BitmapFactory.decodeFile(%1$s)",
                "Bitmap from file %s"));

        list.add(blk("bitmapResize", "v", TYPE_BITMAP,
                "android.graphics.Bitmap.createScaledBitmap(%1$s, (int)(%2$s), (int)(%3$s), true)",
                "Bitmap %m.bitmap resize to %d x %d"));

        list.add(blk("bitmapGetWidth", "d", "", "((java.util.function.Supplier<Double>) () -> { try { android.graphics.Bitmap _b = %1$s; return _b != null ? (double) _b.getWidth() : 0.0; } catch (Throwable _t) { return 0.0; } }).get()", "Bitmap %m.bitmap width"));

        list.add(blk("bitmapGetHeight", "d", "", "((java.util.function.Supplier<Double>) () -> { try { android.graphics.Bitmap _b = %1$s; return _b != null ? (double) _b.getHeight() : 0.0; } catch (Throwable _t) { return 0.0; } }).get()", "Bitmap %m.bitmap height"));

        list.add(blk("bitmapSaveToFile", " ", "", "try { android.graphics.Bitmap _b = %1$s; if (_b != null) { java.io.FileOutputStream _fos = new java.io.FileOutputStream(%2$s); _b.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, _fos); _fos.flush(); _fos.close(); } } catch (Throwable _t) {}", "save Bitmap %m.bitmap to file %s"));

        list.add(blk("bitmapRotate", "v", TYPE_BITMAP,
                "android.graphics.Bitmap.createBitmap(%1$s, 0, 0, %1$s.getWidth(), %1$s.getHeight(), (() -> { android.graphics.Matrix _m = new android.graphics.Matrix(); _m.postRotate((float)(%2$s)); return _m; }).get(), true)",
                "Bitmap %m.bitmap rotate %d degrees"));

        list.add(blk("bitmapCrop", "v", TYPE_BITMAP,
                "android.graphics.Bitmap.createBitmap(%1$s, (int)(%2$s), (int)(%3$s), (int)(%4$s), (int)(%5$s))",
                "Bitmap %m.bitmap crop x %d y %d width %d height %d"));

        list.add(blk("bitmapToGray", "v", TYPE_BITMAP,
                "(() -> { android.graphics.Bitmap _bmp = android.graphics.Bitmap.createBitmap(%1$s.getWidth(), %1$s.getHeight(), android.graphics.Bitmap.Config.ARGB_8888); android.graphics.Canvas _c = new android.graphics.Canvas(_bmp); android.graphics.ColorMatrix _cm = new android.graphics.ColorMatrix(); _cm.setSaturation(0); android.graphics.Paint _p = new android.graphics.Paint(); _p.setColorFilter(new android.graphics.ColorMatrixColorFilter(_cm)); _c.drawBitmap(%1$s, 0, 0, _p); return _bmp; }).get()",
                "Bitmap %m.bitmap to grayscale"));
    }
}
