package pro.sketchware.activities.projectgraph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Алгоритмы раскладки графа.
 */
public class GraphLayout {

    public static final float NODE_WIDTH = 220f;
    public static final float NODE_HEIGHT = 70f;

    private static final float BASE_GAP_X = 280f;
    private static final float BASE_GAP_Y = 110f;

    /** Применить раскладку к графу. */
    public static void apply(ProjectGraph graph, int layout, int levelGap) {
        if (graph == null || graph.nodes.isEmpty()) return;

        float gapX = getGapX(levelGap);
        float gapY = getGapY(levelGap);

        switch (layout) {
            case GraphSettings.LAYOUT_HORIZONTAL -> layoutHorizontal(graph, gapX, gapY);
            case GraphSettings.LAYOUT_RADIAL -> layoutRadial(graph);
            case GraphSettings.LAYOUT_GRID -> layoutGrid(graph, gapX, gapY);
            case GraphSettings.LAYOUT_FORCE -> layoutForceDirected(graph);
            case GraphSettings.LAYOUT_TREE -> layoutTree(graph, gapX, gapY);
            default -> layoutTree(graph, gapX, gapY);
        }
    }

    private static float getGapX(int levelGap) {
        switch (levelGap) {
            case GraphSettings.GAP_COMPACT: return BASE_GAP_X * 0.7f;
            case GraphSettings.GAP_SPACIOUS: return BASE_GAP_X * 1.4f;
            case GraphSettings.GAP_STANDARD:
            default: return BASE_GAP_X;
        }
    }

    private static float getGapY(int levelGap) {
        switch (levelGap) {
            case GraphSettings.GAP_COMPACT: return BASE_GAP_Y * 0.7f;
            case GraphSettings.GAP_SPACIOUS: return BASE_GAP_Y * 1.4f;
            case GraphSettings.GAP_STANDARD:
            default: return BASE_GAP_Y;
        }
    }

    /** Дерево сверху вниз (текущий). */
    private static void layoutTree(ProjectGraph graph, float gapX, float gapY) {
        Map<String, Integer> level = new HashMap<>();
        for (ProjectGraphNode n : graph.nodes) {
            level.put(n.id, getDepth(graph, n.id));
        }

        Map<Integer, List<ProjectGraphNode>> byLevel = new java.util.TreeMap<>();
        for (ProjectGraphNode n : graph.nodes) {
            int lvl = level.getOrDefault(n.id, 0);
            byLevel.computeIfAbsent(lvl, k -> new ArrayList<>()).add(n);
        }

        float y = 0;
        for (Map.Entry<Integer, List<ProjectGraphNode>> entry : byLevel.entrySet()) {
            List<ProjectGraphNode> nodes = entry.getValue();
            float totalWidth = nodes.size() * NODE_WIDTH + (nodes.size() - 1) * (gapX - NODE_WIDTH);
            float x = -totalWidth / 2f;
            for (ProjectGraphNode n : nodes) {
                n.x = x;
                n.y = y;
                x += gapX;
            }
            y += gapY;
        }
    }

    /** Дерево слева направо. */
    private static void layoutHorizontal(ProjectGraph graph, float gapX, float gapY) {
        Map<String, Integer> level = new HashMap<>();
        for (ProjectGraphNode n : graph.nodes) {
            level.put(n.id, getDepth(graph, n.id));
        }

        Map<Integer, List<ProjectGraphNode>> byLevel = new java.util.TreeMap<>();
        for (ProjectGraphNode n : graph.nodes) {
            int lvl = level.getOrDefault(n.id, 0);
            byLevel.computeIfAbsent(lvl, k -> new ArrayList<>()).add(n);
        }

        float x = 0;
        for (Map.Entry<Integer, List<ProjectGraphNode>> entry : byLevel.entrySet()) {
            List<ProjectGraphNode> nodes = entry.getValue();
            float totalHeight = nodes.size() * NODE_HEIGHT + (nodes.size() - 1) * (gapY - NODE_HEIGHT);
            float y = -totalHeight / 2f;
            for (ProjectGraphNode n : nodes) {
                n.x = x;
                n.y = y;
                y += gapY;
            }
            x += gapX;
        }
    }

    /** Радиальная — корень в центре, потомки по кругам. */
    private static void layoutRadial(ProjectGraph graph) {
        // Группируем по уровню
        Map<Integer, List<ProjectGraphNode>> byLevel = new java.util.TreeMap<>();
        for (ProjectGraphNode n : graph.nodes) {
            int lvl = getDepth(graph, n.id);
            byLevel.computeIfAbsent(lvl, k -> new ArrayList<>()).add(n);
        }

        for (Map.Entry<Integer, List<ProjectGraphNode>> entry : byLevel.entrySet()) {
            int lvl = entry.getKey();
            List<ProjectGraphNode> nodes = entry.getValue();

            if (lvl == 0) {
                // Корень в центре
                for (ProjectGraphNode n : nodes) {
                    n.x = -NODE_WIDTH / 2f;
                    n.y = -NODE_HEIGHT / 2f;
                }
                continue;
            }

            // Радиус растёт с уровнем
            float radius = lvl * 300f;
            float angleStep = (float) (2 * Math.PI / nodes.size());
            for (int i = 0; i < nodes.size(); i++) {
                float angle = i * angleStep;
                ProjectGraphNode n = nodes.get(i);
                n.x = (float) (Math.cos(angle) * radius) - NODE_WIDTH / 2f;
                n.y = (float) (Math.sin(angle) * radius) - NODE_HEIGHT / 2f;
            }
        }
    }

    /** Сетка — просто решётка. */
    private static void layoutGrid(ProjectGraph graph, float gapX, float gapY) {
        int columns = (int) Math.ceil(Math.sqrt(graph.nodes.size()));
        if (columns < 1) columns = 1;

        for (int i = 0; i < graph.nodes.size(); i++) {
            ProjectGraphNode n = graph.nodes.get(i);
            int col = i % columns;
            int row = i / columns;
            n.x = col * gapX;
            n.y = row * gapY;
        }
    }

    /**
     * Force-directed (силовой алгоритм).
     * Простая реализация Fruchterman-Reingold.
     */
    private static void layoutForceDirected(ProjectGraph graph) {
        int count = graph.nodes.size();
        if (count == 0) return;
        if (count == 1) {
            graph.nodes.get(0).x = 0;
            graph.nodes.get(0).y = 0;
            return;
        }

        // Инициализация случайными позициями (фиксированный seed для стабильности)
        Random rnd = new Random(42L);
        for (ProjectGraphNode n : graph.nodes) {
            n.x = (rnd.nextFloat() - 0.5f) * 1000f;
            n.y = (rnd.nextFloat() - 0.5f) * 1000f;
        }

        float area = count * 50000f;
        float k = (float) Math.sqrt(area / count);
        int iterations = 200;

        Map<String, ProjectGraphNode> idMap = new HashMap<>();
        for (ProjectGraphNode n : graph.nodes) idMap.put(n.id, n);

        for (int iter = 0; iter < iterations; iter++) {
            float temperature = k * (1f - (float) iter / iterations) * 0.5f;
            if (temperature < 1f) temperature = 1f;

            // Отталкивание
            Map<String, float[]> disp = new HashMap<>();
            for (ProjectGraphNode n : graph.nodes) {
                disp.put(n.id, new float[]{0, 0});
            }

            for (int i = 0; i < count; i++) {
                ProjectGraphNode a = graph.nodes.get(i);
                for (int j = i + 1; j < count; j++) {
                    ProjectGraphNode b = graph.nodes.get(j);
                    float dx = a.x - b.x;
                    float dy = a.y - b.y;
                    float dist = (float) Math.max(1.0, Math.sqrt(dx * dx + dy * dy));
                    float force = (k * k) / dist;
                    float fx = (dx / dist) * force;
                    float fy = (dy / dist) * force;

                    float[] da = disp.get(a.id);
                    float[] db = disp.get(b.id);
                    da[0] += fx; da[1] += fy;
                    db[0] -= fx; db[1] -= fy;
                }
            }

            // Притяжение по рёбрам
            for (ProjectGraph.Edge e : graph.edges) {
                ProjectGraphNode a = idMap.get(e.fromId);
                ProjectGraphNode b = idMap.get(e.toId);
                if (a == null || b == null) continue;
                float dx = a.x - b.x;
                float dy = a.y - b.y;
                float dist = (float) Math.max(1.0, Math.sqrt(dx * dx + dy * dy));
                float force = (dist * dist) / k;
                float fx = (dx / dist) * force;
                float fy = (dy / dist) * force;

                float[] da = disp.get(a.id);
                float[] db = disp.get(b.id);
                da[0] -= fx; da[1] -= fy;
                db[0] += fx; db[1] += fy;
            }

            // Применяем
            for (ProjectGraphNode n : graph.nodes) {
                float[] d = disp.get(n.id);
                float dLen = (float) Math.max(1.0, Math.sqrt(d[0] * d[0] + d[1] * d[1]));
                n.x += (d[0] / dLen) * Math.min(dLen, temperature);
                n.y += (d[1] / dLen) * Math.min(dLen, temperature);
            }
        }
    }

    private static int getDepth(ProjectGraph graph, String nodeId) {
        int depth = 0;
        ProjectGraphNode current = graph.findNode(nodeId);
        while (current != null && current.parentId != null) {
            depth++;
            current = graph.findNode(current.parentId);
        }
        return depth;
    }
}
