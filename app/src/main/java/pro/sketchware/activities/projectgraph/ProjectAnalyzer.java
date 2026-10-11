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


    /** Обёртка для информации о переменной. */
    public static class VariableInfo {
        public final String name;
        public final int type; // 0=boolean, 1=number, 2=string, 3=map, 5=custom
        public VariableInfo(String name, int type) {
            this.name = name;
            this.type = type;
        }
        public String getTypeName() {
            switch (type) {
                case 0: return "Boolean";
                case 1: return "Number";
                case 2: return "String";
                case 3: return "Map";
                case 5: return "Custom";
                default: return "Variable";
            }
        }
    }

    /** Обёртка для информации о компоненте. */
    public static class ComponentInfo {
        public final String name;
        public final int type;
        public final String typeName;
        public ComponentInfo(String name, int type, String typeName) {
            this.name = name;
            this.type = type;
            this.typeName = typeName;
        }
    }

    /** Обёртка для информации о функции (More Block). */
    public static class FunctionInfo {
        public final String name;
        public final String spec;
        public FunctionInfo(String name, String spec) {
            this.name = name;
            this.spec = spec;
        }
    }

    /**
     * Все переменные Activity по типам.
     */
    public List<VariableInfo> collectVariables(String javaName) {
        List<VariableInfo> result = new ArrayList<>();
        int[] types = {0, 1, 2, 3, 5};
        for (int type : types) {
            try {
                ArrayList<String> list = jC.a(scId).e(javaName, type);
                if (list == null) continue;
                for (String name : list) {
                    if (name != null && !name.isEmpty()) {
                        result.add(new VariableInfo(name, type));
                    }
                }
            } catch (Throwable ignored) {
            }
        }
        return result;
    }

    /**
     * Все компоненты Activity.
     */
    public List<ComponentInfo> collectComponents(String javaName) {
        List<ComponentInfo> result = new ArrayList<>();
        try {
            ArrayList<com.besome.sketch.beans.ComponentBean> list =
                    jC.a(scId).e(javaName);
            if (list != null) {
                for (com.besome.sketch.beans.ComponentBean c : list) {
                    if (c == null) continue;
                    String typeName = com.besome.sketch.beans.ComponentBean.getComponentTypeName(c.type);
                    result.add(new ComponentInfo(c.componentId, c.type, typeName));
                }
            }
        } catch (Throwable ignored) {
        }
        return result;
    }

    /**
     * Все функции (More Blocks) Activity.
     */
    public List<FunctionInfo> collectFunctions(String javaName) {
        List<FunctionInfo> result = new ArrayList<>();
        try {
            ArrayList<android.util.Pair<String, String>> list =
                    jC.a(scId).i(javaName);
            if (list != null) {
                for (android.util.Pair<String, String> p : list) {
                    if (p == null) continue;
                    result.add(new FunctionInfo(p.first, p.second));
                }
            }
        } catch (Throwable ignored) {
        }
        return result;
    }
}
