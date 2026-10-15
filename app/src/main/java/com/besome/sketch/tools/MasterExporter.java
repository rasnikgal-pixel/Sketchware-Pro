package com.besome.sketch.tools;

import android.content.Context;
import java.io.InputStream;
import java.io.OutputStream;

/** Копирует встроенный мастер-файл локализации в выбранный файл. */
public final class MasterExporter {

    private static final String ASSET_PATH = "i18n/strings_master.xml";

    private MasterExporter() {}

    /** Копирует assets-файл в out. Возвращает количество скопированных байт. */
    public static long copy(Context ctx, OutputStream out) throws Exception {
        InputStream in = ctx.getAssets().open(ASSET_PATH);
        java.io.ByteArrayOutputStream bufOut = new java.io.ByteArrayOutputStream();
        byte[] buf = new byte[8192];
        int n;
        while ((n = in.read(buf)) > 0) {
            bufOut.write(buf, 0, n);
        }
        in.close();
        byte[] bytes = bufOut.toByteArray();
        out.write(bytes);
        out.flush();
        String text = new String(bytes, "UTF-8");
        int count = 0;
        int idx = 0;
        while ((idx = text.indexOf("<string ", idx)) >= 0) { count++; idx += 8; }
        return count;
    }
}
