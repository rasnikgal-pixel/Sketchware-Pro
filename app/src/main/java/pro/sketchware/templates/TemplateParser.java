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
        // Не устанавливаем preParent/preId — оставляем дефолты ViewBean
        // чтобы избежать NPE в ViewPane.updateItemView

        // Layout
        LayoutBean layout = v.layout;
        layout.width = raw.width;
        layout.height = raw.height;
        layout.marginLeft = raw.marginLeft;
        layout.marginTop = raw.marginTop;
        layout.marginRight = raw.marginRight;
        layout.marginBottom = raw.marginBottom;
        // ВАЖНО: для View внутри LinearLayout нужно layout.gravity, а не layoutGravity
        // layout.layoutGravity применяется только в RelativeLayout → вызывает ClassCastException
        layout.gravity = raw.layoutGravity;

        // Text — присваиваем из raw, если есть
        TextBean text = v.text;
        if (raw.text != null) text.text = raw.text;
        text.textSize = raw.textSize;
        text.textType = raw.textStyle;
        if (raw.hint != null) text.hint = raw.hint;

        // Гарантируем не-null критичные поля (ПОСЛЕ присваивания!)
        if (v.image.resName == null) {
            v.image.resName = "default_image";
            v.image.scaleType = "CENTER";
        }
        if (v.inject == null) v.inject = "";
        if (v.convert == null) v.convert = "";
        if (v.customView == null) v.customView = "";
        if (v.indeterminate == null) v.indeterminate = "false";
        if (v.adSize == null) v.adSize = "";
        if (v.adUnitId == null) v.adUnitId = "";
        if (v.text.text == null) v.text.text = "";
        if (v.text.hint == null) v.text.hint = "";
        if (v.text.textFont == null) v.text.textFont = "default_font";
        if (v.text.resTextColor == null) v.text.resTextColor = "";

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
