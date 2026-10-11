package pro.sketchware.activities.projectgraph;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Настройки графа проекта.
 */
public class GraphSettings {

    private static final String PREFS = "project_graph_prefs";

    private static final String KEY_DRAG_NODES = "drag_nodes";
    private static final String KEY_EXPORT_FULL = "export_full";
    private static final String KEY_FORCE_DIRECTED = "force_directed";
    private static final String KEY_SHOW_EDGE_LABELS = "show_edge_labels";

    // Значения по умолчанию
    private static final boolean DEFAULT_DRAG_NODES = true;
    private static final boolean DEFAULT_EXPORT_FULL = true;
    private static final boolean DEFAULT_FORCE_DIRECTED = false; // дерево по уровням
    private static final boolean DEFAULT_SHOW_EDGE_LABELS = false;

    private final SharedPreferences prefs;

    public GraphSettings(Context context) {
        prefs = context.getApplicationContext()
                .getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public boolean isDragNodesEnabled() {
        return prefs.getBoolean(KEY_DRAG_NODES, DEFAULT_DRAG_NODES);
    }

    public void setDragNodesEnabled(boolean v) {
        prefs.edit().putBoolean(KEY_DRAG_NODES, v).apply();
    }

    public boolean isExportFullTree() {
        return prefs.getBoolean(KEY_EXPORT_FULL, DEFAULT_EXPORT_FULL);
    }

    public void setExportFullTree(boolean v) {
        prefs.edit().putBoolean(KEY_EXPORT_FULL, v).apply();
    }

    public boolean isForceDirected() {
        return prefs.getBoolean(KEY_FORCE_DIRECTED, DEFAULT_FORCE_DIRECTED);
    }

    public void setForceDirected(boolean v) {
        prefs.edit().putBoolean(KEY_FORCE_DIRECTED, v).apply();
    }

    public boolean isShowEdgeLabels() {
        return prefs.getBoolean(KEY_SHOW_EDGE_LABELS, DEFAULT_SHOW_EDGE_LABELS);
    }

    public void setShowEdgeLabels(boolean v) {
        prefs.edit().putBoolean(KEY_SHOW_EDGE_LABELS, v).apply();
    }
}
