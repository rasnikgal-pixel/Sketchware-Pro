package pro.sketchware.activities.projectgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import a.a.a.jC;
import com.besome.sketch.beans.BlockBean;
import com.besome.sketch.beans.EventBean;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ViewBean;

/**
 * Собирает данные проекта для построения графа.
 * Использует публичные API jC/hC/eC/kC/iC.
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
     * Все события Activity.
     */
    public List<EventBean> collectEvents(String javaName) {
        List<EventBean> result = new ArrayList<>();
        try {
            List<EventBean> list = jC.a(scId).b(javaName);
            if (list != null) result.addAll(list);
        } catch (Throwable ignored) {
        }
        return result;
    }

    /**
     * Блоки конкретного события.
     */
    public List<BlockBean> collectBlocks(String javaName, String eventKey) {
        List<BlockBean> result = new ArrayList<>();
        try {
            List<BlockBean> list = jC.a(scId).a(javaName, eventKey);
            if (list != null) result.addAll(list);
        } catch (Throwable ignored) {
        }
        return result;
    }

    /**
     * Виджеты Activity (если API доступен).
     */
    public List<ViewBean> collectWidgets(String javaName) {
        List<ViewBean> result = new ArrayList<>();
        try {
            // Пробуем через hC.c(javaName)
            List<ViewBean> list = jC.b(scId).c(javaName);
            if (list != null) result.addAll(list);
        } catch (Throwable ignored) {
        }
        return result;
    }

    public String getScId() {
        return scId;
    }
}
