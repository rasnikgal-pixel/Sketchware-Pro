package com.besome.sketch.help;

import com.besome.sketch.beans.ViewBean;

/**
 * Сопоставляет типы виджетов (ViewBean.type) с якорями справки.
 * Используется в PropertyActivity для кнопки «?».
 */
public final class WidgetHelpMapper {

    private WidgetHelpMapper() {}

    /**
     * @param type — значение ViewBean.type
     * @return якорь справки, или null если для данного типа якоря нет.
     */
    public static String getAnchor(int type) {
        switch (type) {
            case ViewBean.VIEW_TYPE_LAYOUT_LINEAR:       return "block-linear-v-vertikalnyy";
            case ViewBean.VIEW_TYPE_LAYOUT_RELATIVE:     return "block-relativelayout-otnositelnyy-maket";
            case ViewBean.VIEW_TYPE_LAYOUT_HSCROLLVIEW:  return "block-scroll-h-gorizontalnaya-prokrutka";
            case ViewBean.VIEW_TYPE_LAYOUT_VSCROLLVIEW:  return "block-scroll-v-vertikalnaya-prokrutka";
            case ViewBean.VIEW_TYPE_WIDGET_BUTTON:       return "block-button-knopka";
            case ViewBean.VIEW_TYPE_WIDGET_TEXTVIEW:     return "block-textview-tekst";
            case ViewBean.VIEW_TYPE_WIDGET_EDITTEXT:     return "block-edittext-pole-vvoda";
            case ViewBean.VIEW_TYPE_WIDGET_IMAGEVIEW:    return "block-imageview-izobrazhenie";
            case ViewBean.VIEW_TYPE_WIDGET_WEBVIEW:      return "block-webview-mini-brauzer";
            case ViewBean.VIEW_TYPE_WIDGET_PROGRESSBAR:  return "block-progressbar-indikator-zagruzki";
            case ViewBean.VIEW_TYPE_WIDGET_LISTVIEW:     return "block-listview-vertikalnyy-spisok";
            case ViewBean.VIEW_TYPE_WIDGET_SPINNER:      return "block-spinner-vypadayuschiy-spisok";
            case ViewBean.VIEW_TYPE_WIDGET_CHECKBOX:     return "block-checkbox-galochka";
            case ViewBean.VIEW_TYPE_WIDGET_SWITCH:       return "block-switch-tumbler";
            case ViewBean.VIEW_TYPE_WIDGET_SEEKBAR:      return "block-seekbar-polzunok";
            case ViewBean.VIEW_TYPE_WIDGET_CALENDARVIEW: return "block-calendarview-kalendar";
            case ViewBean.VIEW_TYPE_WIDGET_ADVIEW:       return "block-adview-reklamnyy-banner";
            case ViewBean.VIEW_TYPE_WIDGET_MAPVIEW:      return "block-mapview-karta-google";
            // FAB (16) пока без якоря
            default: return null;
        }
    }
}
