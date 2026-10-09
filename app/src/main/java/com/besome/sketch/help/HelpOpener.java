package com.besome.sketch.help;

import android.app.Activity;
import android.content.Context;

/**
 * Единая точка открытия справки во всём приложении.
 *
 * Применение:
 *   HelpOpener.open(this, "block-dizayner-view");
 *
 * Если anchor не найден в anchors.json — откроется главная справки.
 */
public final class HelpOpener {

    private HelpOpener() {}

    /** Открывает справку по anchor. */
    public static void open(Context context, String anchor) {
        if (context == null) return;
        HelpActivity.openAnchor(context, anchor);
    }

    /** Открывает главную справки. */
    public static void openMain(Context context) {
        if (context == null) return;
        HelpActivity.openPage(context, null);
    }
}
