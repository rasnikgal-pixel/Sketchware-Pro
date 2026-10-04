package pro.sketchware.templates;

import java.util.ArrayList;

/**
 * Модель шаблона экрана.
 * Загружается из assets/screen_templates/templates.json.
 */
public class ScreenTemplate {

    public String id;
    public String category;
    public String name;
    public String description;
    public String previewIcon;
    public String defaultAppIcon;

    public ArrayList<RawView> views = new ArrayList<>();
    public ArrayList<RawBlock> blocks = new ArrayList<>();

    public ScreenTemplate() {}

    @Override
    public String toString() {
        return "ScreenTemplate{" + id + ", " + name + "}";
    }

    /** Описание View — маппится на com.besome.sketch.beans.ViewBean */
    public static class RawView {
        public String id;
        public String parent = "root";
        public int type;
        public String name;
        public int index;
        public String text;
        public int textSize = 14;
        public int textStyle = 0;
        public String hint;
        public int width = -2;
        public int height = -2;
        public int marginLeft = 0;
        public int marginTop = 0;
        public int marginRight = 0;
        public int marginBottom = 0;
        public int layoutGravity = 0;
    }

    /** Описание Block — маппится на com.besome.sketch.beans.BlockBean */
    public static class RawBlock {
        public String type = " ";
        public String opCode;
        public String spec;
        public ArrayList<String> parameters = new ArrayList<>();
    }
}
