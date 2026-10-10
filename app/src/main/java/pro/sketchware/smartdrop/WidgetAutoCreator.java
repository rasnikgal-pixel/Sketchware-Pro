package pro.sketchware.smartdrop;


import mod.hey.studios.util.Helper;
import pro.sketchware.R;
import android.app.Activity;

import com.besome.sketch.beans.BlockBean;
import com.besome.sketch.beans.LayoutBean;
import com.besome.sketch.beans.ViewBean;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import pro.sketchware.utility.SketchwareUtil;

/**
 * Автоматически предлагает создать виджет, если блок его требует,
 * а на экране нет. Этап A: пока только WebView.
 */
public final class WidgetAutoCreator {

    private WidgetAutoCreator() {}

    /** Возвращает тип ViewBean, нужный блоку, или -1. */
    public static int getRequiredViewType(String opCode) {
        if (opCode == null) return -1;
        switch (opCode) {
            case "webViewLoadUrl":
            case "webViewGoBack":
            case "webViewGoForward":
            case "webViewGetUrl":
            case "webViewCanGoBack":
            case "webViewCanGoForward":
            case "webViewStopLoading":
            case "webViewClearCache":
            case "webViewClearHistory":
            case "webViewZoomIn":
            case "webViewZoomOut":
            case "webViewSetCacheMode":
                return ViewBean.VIEW_TYPE_WIDGET_WEBVIEW;
            default:
                return -1;
        }
    }

    /** Префикс имени виджета. */
    public static String getWidgetPrefix(int viewType) {
        switch (viewType) {
            case ViewBean.VIEW_TYPE_WIDGET_WEBVIEW:   return "webView";
            case ViewBean.VIEW_TYPE_WIDGET_TEXTVIEW:  return "textView";
            case ViewBean.VIEW_TYPE_WIDGET_EDITTEXT:  return "editText";
            case ViewBean.VIEW_TYPE_WIDGET_IMAGEVIEW: return "imageView";
            case ViewBean.VIEW_TYPE_WIDGET_LISTVIEW:  return "listView";
            case ViewBean.VIEW_TYPE_WIDGET_SPINNER:   return "spinner";
            case ViewBean.VIEW_TYPE_WIDGET_SEEKBAR:   return "seekBar";
            case ViewBean.VIEW_TYPE_WIDGET_CHECKBOX:  return "checkBox";
            default: return "view";
        }
    }

    /** Человеко-читаемое имя типа. */
    public static String getWidgetTypeName(int viewType) {
        try {
            String n = ViewBean.getViewTypeName(viewType);
            if (n != null && !n.isEmpty()) return n;
        } catch (Throwable ignored) {}
        return Helper.getResString(R.string.auto_widget_auto_creator_001);
    }

    /** XML-тег для типа (WebView, TextView, ...). */
    public static String getConvertName(int viewType) {
        try {
            String n = ViewBean.getViewTypeName(viewType);
            if (n != null && !n.isEmpty()) return n;
        } catch (Throwable ignored) {}
        return "View";
    }

    /** Проверка, есть ли виджет нужного типа. */
    public static boolean hasWidgetOfType(String scId, String xmlName, int viewType) {
        try {
            ArrayList<ViewBean> views = a.a.a.jC.a(scId).d(xmlName);
            if (views == null) return false;
            for (ViewBean v : views) {
                if (v != null && v.type == viewType) return true;
            }
        } catch (Throwable ignored) {}
        return false;
    }

    /** Свободное имя вида webView1, webView2... */
    public static String generateWidgetName(String scId, String xmlName, int viewType) {
        String prefix = getWidgetPrefix(viewType);
        try {
            a.a.a.eC ec = a.a.a.jC.a(scId);
            for (int i = 1; i <= 1000; i++) {
                String name = prefix + i;
                if (ec.c(xmlName, name) == null) return name;
            }
        } catch (Throwable ignored) {}
        return prefix + "1";
    }

    /** Определяет тип root-layout экрана. */
    public static int getRootViewType(String scId, String xmlName) {
        try {
            ArrayList<ViewBean> views = a.a.a.jC.a(scId).d(xmlName);
            if (views != null) {
                for (ViewBean v : views) {
                    if (v != null && "root".equals(v.id)) return v.type;
                }
            }
        } catch (Throwable ignored) {}
        return ViewBean.VIEW_TYPE_LAYOUT_LINEAR;
    }

    /** Создаёт ViewBean нужного типа и добавляет в экран. */
    public static boolean createWidget(String scId, String xmlName, int viewType, String name) {
        try {
            a.a.a.eC ec = a.a.a.jC.a(scId);
            ViewBean vb = new ViewBean();
            vb.id = name;
            vb.name = name;
            vb.type = viewType;
            vb.convert = getConvertName(viewType);
            vb.parent = "root";
            vb.parentType = getRootViewType(scId, xmlName);
            vb.preParent = "root";
            vb.preParentType = vb.parentType;
            vb.index = -1;
            vb.preIndex = -1;
            LayoutBean lb = vb.layout;
            if (viewType == ViewBean.VIEW_TYPE_WIDGET_WEBVIEW) {
                lb.width = -1;
                lb.height = -1;
                lb.paddingLeft = 0;
                lb.paddingTop = 0;
                lb.paddingRight = 0;
                lb.paddingBottom = 0;
            } else {
                lb.width = -2;
                lb.height = -2;
            }
            ec.a(xmlName, vb);
            ec.k();
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /**
     * Проверка и предложение создать виджет.
     * Вызывать после SmartDropHelper.handleDrop.
     */
    public static void checkAndSuggest(Activity activity,
                                       String scId,
                                       String xmlName,
                                       BlockBean blockBean,
                                       Runnable onCreated) {
        if (activity == null || blockBean == null || blockBean.opCode == null) return;
        if (scId == null || xmlName == null) return;

        int viewType = getRequiredViewType(blockBean.opCode);
        if (viewType < 0) return;

        try {
            if (hasWidgetOfType(scId, xmlName, viewType)) return;

            String typeName = getWidgetTypeName(viewType);
            String suggestedName = generateWidgetName(scId, xmlName, viewType);

            new MaterialAlertDialogBuilder(activity)
                    .setTitle("Создать " + typeName + "?")
                    .setMessage("Для этого блока нужен " + typeName + ".\n\n"
                            + "На экране нет " + typeName + ". Создать его "
                            + "автоматически с именем \"" + suggestedName + "\"?")
                    .setPositiveButton(Helper.getResString(R.string.auto_widget_auto_creator_002), (d, w) -> {
                        boolean ok = createWidget(scId, xmlName, viewType, suggestedName);
                        if (ok) {
                            SketchwareUtil.toast("Создан " + suggestedName);
                            if (onCreated != null) onCreated.run();
                        } else {
                            SketchwareUtil.toastError("Не удалось создать " + typeName);
                        }
                    })
                    .setNegativeButton(Helper.getResString(R.string.auto_widget_auto_creator_003), null)
                    .show();
        } catch (Throwable ignored) {}
    }
}
