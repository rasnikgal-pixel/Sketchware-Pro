package pro.sketchware.activities.projectgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import a.a.a.jC;
import com.besome.sketch.beans.BlockBean;
import com.besome.sketch.beans.ProjectFileBean;

/**
 * Собирает данные проекта для построения графа.
 * Использует только проверенные публичные API jC.
 */
public class ProjectAnalyzer {

    private final String scId;

    public ProjectAnalyzer(String scId) {
        this.scId = scId;
    }

    /**
     * Возвращает список Activity/View (ProjectFileBean).
     */
    public List<ProjectFileBean> collectActivities() {
        List<ProjectFileBean> result = new ArrayList<>();
        try {
            List<ProjectFileBean> list = jC.b(scId).b();
            if (list != null) result.addAll(list);
        } catch (Throwable ignored) {
        }
        return result;
    }

    /**
     * Возвращает карту событий Activity:
     * ключ = eventKey (например, "button1_onClick"),
     * значение = список блоков события.
     */
    public Map<String, List<BlockBean>> collectEvents(String javaName) {
        Map<String, List<BlockBean>> result = new LinkedHashMap<>();
        try {
            HashMap<String, ArrayList<BlockBean>> map = jC.a(scId).b(javaName);
            if (map != null) {
                for (Map.Entry<String, ArrayList<BlockBean>> entry : map.entrySet()) {
                    List<BlockBean> blocks = entry.getValue();
                    if (blocks != null) {
                        result.put(entry.getKey(), new ArrayList<>(blocks));
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return result;
    }

    /**
     * Вспомогательный метод: разбивает eventKey "button1_onClick" на
     * widgetId и eventName.
     */
    public static String[] splitEventKey(String eventKey) {
        if (eventKey == null) return new String[]{"", ""};
        int idx = eventKey.indexOf('_');
        if (idx < 0) return new String[]{eventKey, ""};
        return new String[]{eventKey.substring(0, idx), eventKey.substring(idx + 1)};
    }

    public String getScId() {
        return scId;
    }
}
