package pro.sketchware.activities.projectgraph;

import java.util.Objects;

/**
 * Узел графа проекта.
 */
public class ProjectGraphNode {

    public enum Type {
        PROJECT,       // корень
        ACTIVITY,      // экран
        EVENT,         // событие виджета (onClick и т.д.)
        BLOCK,         // блок (пазл)
        FUNCTION,      // More Block
        VARIABLE,      // переменная
        COMPONENT      // компонент (Firebase, Camera и т.д.)
    }

    public final String id;
    public final String label;
    public final Type type;
    public final String parentId;

    // Дополнительные поля (для разных типов узлов)
    // EVENT: extra1 = javaName, extra2 = eventKey
    // ACTIVITY: extra1 = javaName
    public String extra1 = null;
    public String extra2 = null;

    // Координаты на холсте
    public float x;
    public float y;

    public ProjectGraphNode(String id, String label, Type type, String parentId) {
        this.id = id;
        this.label = label;
        this.type = type;
        this.parentId = parentId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProjectGraphNode)) return false;
        return Objects.equals(id, ((ProjectGraphNode) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
