package pro.sketchware.activities.projectgraph;

import java.util.ArrayList;
import java.util.List;

/**
 * Модель графа: узлы + рёбра.
 */
public class ProjectGraph {

    public static class Edge {
        public final String fromId;
        public final String toId;
        public final String label;

        public Edge(String fromId, String toId, String label) {
            this.fromId = fromId;
            this.toId = toId;
            this.label = label;
        }
    }

    public final List<ProjectGraphNode> nodes = new ArrayList<>();
    public final List<Edge> edges = new ArrayList<>();

    public void addNode(ProjectGraphNode node) {
        if (node != null) nodes.add(node);
    }

    public void addEdge(String fromId, String toId, String label) {
        edges.add(new Edge(fromId, toId, label));
    }

    public ProjectGraphNode findNode(String id) {
        for (ProjectGraphNode n : nodes) {
            if (n.id.equals(id)) return n;
        }
        return null;
    }

    public List<ProjectGraphNode> getNodesByType(ProjectGraphNode.Type type) {
        List<ProjectGraphNode> result = new ArrayList<>();
        for (ProjectGraphNode n : nodes) {
            if (n.type == type) result.add(n);
        }
        return result;
    }
}
