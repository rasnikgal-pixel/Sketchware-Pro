package pro.sketchware.activities.projectgraph;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Настройки графа проекта.
 */
public class GraphSettings {

    // Раскладки
    public static final int LAYOUT_TREE = 0;
    public static final int LAYOUT_HORIZONTAL = 1;
    public static final int LAYOUT_RADIAL = 2;
    public static final int LAYOUT_GRID = 3;
    public static final int LAYOUT_FORCE = 4;

    // Отступы
    public static final int GAP_COMPACT = 0;
    public static final int GAP_STANDARD = 1;
    public static final int GAP_SPACIOUS = 2;

    private static final String PREFS = "project_graph_prefs";

    private static final String KEY_DRAG_NODES = "drag_nodes";
    private static final String KEY_EXPORT_FULL = "export_full";
    private static final String KEY_FORCE_DIRECTED = "force_directed";
    private static final String KEY_SHOW_BLOCKS = "show_blocks";
    private static final String KEY_ACTIVITY_FILTER = "activity_filter";
    private static final String KEY_LAYOUT = "layout";
    private static final String KEY_LEVEL_GAP = "level_gap";
    private static final String KEY_SHOW_EDGES = "show_edges";

    private static final boolean DEFAULT_DRAG_NODES = true;
    private static final boolean DEFAULT_EXPORT_FULL = true;
    private static final boolean DEFAULT_FORCE_DIRECTED = false;
    private static final boolean DEFAULT_SHOW_BLOCKS = false;
    private static final int DEFAULT_LAYOUT = LAYOUT_TREE;
    private static final int DEFAULT_LEVEL_GAP = GAP_STANDARD;
    private static final boolean DEFAULT_SHOW_EDGES = true;

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

    public boolean isShowBlocks() {
        return prefs.getBoolean(KEY_SHOW_BLOCKS, DEFAULT_SHOW_BLOCKS);
    }

    public void setShowBlocks(boolean v) {
        prefs.edit().putBoolean(KEY_SHOW_BLOCKS, v).apply();
    }

    /** ID Activity для фильтра (null = показывать всё). */
    public String getActivityFilter() {
        return prefs.getString(KEY_ACTIVITY_FILTER, null);
    }

    public void setActivityFilter(String id) {
        prefs.edit().putString(KEY_ACTIVITY_FILTER, id).apply();
    }

    public int getLayout() {
        return prefs.getInt(KEY_LAYOUT, DEFAULT_LAYOUT);
    }

    public void setLayout(int v) {
        prefs.edit().putInt(KEY_LAYOUT, v).apply();
    }

    public int getLevelGap() {
        return prefs.getInt(KEY_LEVEL_GAP, DEFAULT_LEVEL_GAP);
    }

    public void setLevelGap(int v) {
        prefs.edit().putInt(KEY_LEVEL_GAP, v).apply();
    }

    public boolean isShowEdges() {
        return prefs.getBoolean(KEY_SHOW_EDGES, DEFAULT_SHOW_EDGES);
    }

    public void setShowEdges(boolean v) {
        prefs.edit().putBoolean(KEY_SHOW_EDGES, v).apply();
    }
}
