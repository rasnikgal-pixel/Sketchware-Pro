package pro.sketchware.templates;

import android.content.Context;

import com.besome.sketch.beans.BlockBean;
import com.besome.sketch.beans.LayoutBean;
import com.besome.sketch.beans.TextBean;
import com.besome.sketch.beans.ViewBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Преобразует ScreenTemplate (JSON-модель) в реальные ViewBean (для Sketchware).
 */
public class TemplateParser {

    /** Конвертирует один RawView в ViewBean. */
    public static ViewBean toViewBean(ScreenTemplate.RawView raw) {
        ViewBean v = new ViewBean();
        v.id = raw.id;
        v.name = raw.name != null ? raw.name : raw.id;
        v.type = raw.type;
        v.parent = raw.parent != null ? raw.parent : "root";
        v.index = raw.index;
        v.preParent = null;
        v.preParentType = 0;
        v.preIndex = -1;
        v.preId = null;

        // Layout
        LayoutBean layout = v.layout;
        layout.width = raw.width;
        layout.height = raw.height;
        layout.marginLeft = raw.marginLeft;
        layout.marginTop = raw.marginTop;
        layout.marginRight = raw.marginRight;
        layout.marginBottom = raw.marginBottom;
        layout.layoutGravity = raw.layoutGravity;

        // Text
        TextBean text = v.text;
        if (raw.text != null) text.text = raw.text;
        text.textSize = raw.textSize;
        text.textType = raw.textStyle;
        if (raw.hint != null) text.hint = raw.hint;

        return v;
    }

    /** Конвертирует список RawView. */
    public static ArrayList<ViewBean> toViewBeans(List<ScreenTemplate.RawView> rawList) {
        ArrayList<ViewBean> result = new ArrayList<>();
        if (rawList == null) return result;
        for (ScreenTemplate.RawView r : rawList) {
            result.add(toViewBean(r));
        }
        return result;
    }

    /**
     * Получить список ViewBean для шаблона по его id.
     * Возвращает null, если шаблон не найден.
     */
    public static ArrayList<ViewBean> getViewsForTemplate(Context ctx, String templateId) {
        ScreenTemplates st = ScreenTemplates.get(ctx);
        ScreenTemplate t = st.getById(templateId);
        if (t == null) return null;
        return toViewBeans(t.views);
    }

    /**
     * Получить список BlockBean для шаблона по его id.
     * Пока возвращает пустой список (реализуем позже).
     */
    public static ArrayList<BlockBean> getBlocksForTemplate(Context ctx, String templateId) {
        return new ArrayList<>();
    }

    /** Префикс, который используется для идентификации наших шаблонов в presetName. */
    public static final String PREFIX = "template:";

    public static boolean isOurTemplate(String presetName) {
        return presetName != null && presetName.startsWith(PREFIX);
    }

    public static String extractTemplateId(String presetName) {
        if (!isOurTemplate(presetName)) return null;
        return presetName.substring(PREFIX.length());
    }
}
