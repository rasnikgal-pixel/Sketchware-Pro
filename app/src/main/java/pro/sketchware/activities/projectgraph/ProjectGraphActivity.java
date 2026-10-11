package pro.sketchware.activities.projectgraph;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;

import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.EnumSet;
import java.util.Set;

import pro.sketchware.R;
import pro.sketchware.databinding.ActivityProjectGraphBinding;

/**
 * Activity для отображения графа проекта.
 */
public class ProjectGraphActivity extends BaseAppCompatActivity {

    private ActivityProjectGraphBinding binding;
    private String scId;
    private String projectName;
    private ProjectGraph graph;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        enableEdgeToEdgeNoContrast();
        super.onCreate(savedInstanceState);

        binding = ActivityProjectGraphBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        scId = getIntent().getStringExtra("sc_id");
        projectName = getIntent().getStringExtra("project_name");
        if (scId == null) {
            finish();
            return;
        }

        binding.graphView.setOnNodeTapListener(this::showNodeDetails);

        // Строим граф в фоне
        binding.loading.setVisibility(View.VISIBLE);
        new Thread(() -> {
            ProjectGraph g = null;
            try {
                ProjectAnalyzer analyzer = new ProjectAnalyzer(scId);
                ProjectGraphBuilder builder = new ProjectGraphBuilder(analyzer);
                g = builder.build(projectName);
            } catch (Throwable t) {
                pro.sketchware.smartdrop.DebugLogger.get(getApplicationContext())
                        .d("ProjectGraph", "build_error", String.valueOf(t.getMessage()));
            }
            final ProjectGraph result = g;
            runOnUiThread(() -> {
                binding.loading.setVisibility(View.GONE);
                if (result == null || result.nodes.size() <= 1) {
                    binding.emptyText.setVisibility(View.VISIBLE);
                    return;
                }
                graph = result;
                binding.graphView.setGraph(graph);
            });
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_project_graph, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_reset_zoom) {
            binding.graphView.resetZoom();
            return true;
        } else if (id == R.id.action_filter) {
            showFilterDialog();
            return true;
        } else if (id == R.id.action_export_png) {
            exportPng();
            return true;
        } else if (id == R.id.action_settings) {
            showGraphSettingsDialog();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Диалог настроек графа. */
    private void showGraphSettingsDialog() {
        GraphSettings settings = new GraphSettings(this);
        android.view.View view = getLayoutInflater().inflate(
                R.layout.dialog_graph_settings, null);

        com.google.android.material.materialswitch.MaterialSwitch swDrag =
                view.findViewById(R.id.switch_drag_nodes);
        com.google.android.material.materialswitch.MaterialSwitch swExport =
                view.findViewById(R.id.switch_export_full);
        com.google.android.material.materialswitch.MaterialSwitch swForce =
                view.findViewById(R.id.switch_force_directed);
        com.google.android.material.materialswitch.MaterialSwitch swEdge =
                view.findViewById(R.id.switch_edge_labels);

        swDrag.setChecked(settings.isDragNodesEnabled());
        swExport.setChecked(settings.isExportFullTree());
        swForce.setChecked(settings.isForceDirected());
        swEdge.setChecked(settings.isShowEdgeLabels());

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.graph_settings_title)
                .setView(view)
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    settings.setDragNodesEnabled(swDrag.isChecked());
                    settings.setExportFullTree(swExport.isChecked());
                    settings.setForceDirected(swForce.isChecked());
                    settings.setShowEdgeLabels(swEdge.isChecked());
                    // Применить drag в GraphView
                    binding.graphView.setDragEnabled(swDrag.isChecked());
                    // Перестроить граф, если изменилась раскладка
                    if (graph != null) {
                        binding.graphView.setGraph(graph);
                    }
                    android.widget.Toast.makeText(this,
                            getString(R.string.graph_settings_applied),
                            android.widget.Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    /** Экспорт графа в PNG. */
    private void exportPng() {
        if (graph == null || graph.nodes.isEmpty()) {
            android.widget.Toast.makeText(this,
                    getString(R.string.graph_export_empty),
                    android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            GraphSettings settings = new GraphSettings(this);
            android.graphics.Bitmap bmp = binding.graphView.renderToBitmap(
                    settings.isExportFullTree());

            // Путь сохранения
            java.io.File dir = new java.io.File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "Sketchware/Graphs");
            if (!dir.exists() && !dir.mkdirs()) {
                throw new java.io.IOException("Cannot create directory");
            }
            String safeName = (projectName == null || projectName.isEmpty())
                    ? "project" : projectName.replaceAll("[^a-zA-Z0-9_]", "_");
            String fileName = safeName + "_" + System.currentTimeMillis() + ".png";
            java.io.File outFile = new java.io.File(dir, fileName);

            try (java.io.FileOutputStream fos = new java.io.FileOutputStream(outFile)) {
                bmp.compress(android.graphics.Bitmap.CompressFormat.PNG, 100, fos);
            }

            android.widget.Toast.makeText(this,
                    getString(R.string.graph_export_success) + "\n" + outFile.getAbsolutePath(),
                    android.widget.Toast.LENGTH_LONG).show();
        } catch (Throwable t) {
            pro.sketchware.smartdrop.DebugLogger.get(this)
                    .d("ProjectGraph", "export_error", String.valueOf(t.getMessage()));
            android.widget.Toast.makeText(this,
                    getString(R.string.graph_export_error) + ": " + t.getMessage(),
                    android.widget.Toast.LENGTH_LONG).show();
        }
    }

    private void showFilterDialog() {
        final ProjectGraphNode.Type[] types = {
                ProjectGraphNode.Type.PROJECT,
                ProjectGraphNode.Type.ACTIVITY,
                ProjectGraphNode.Type.EVENT,
                ProjectGraphNode.Type.BLOCK,
                ProjectGraphNode.Type.FUNCTION,
                ProjectGraphNode.Type.VARIABLE,
                ProjectGraphNode.Type.COMPONENT
        };
        final String[] labels = new String[types.length];
        final boolean[] checked = new boolean[types.length];
        Set<ProjectGraphNode.Type> visible = binding.graphView.getVisibleTypes();
        for (int i = 0; i < types.length; i++) {
            labels[i] = getTypeLabel(types[i]);
            checked[i] = visible.contains(types[i]);
        }
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.project_graph_filter)
                .setMultiChoiceItems(labels, checked, (d, which, isChecked) -> {
                    checked[which] = isChecked;
                })
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    Set<ProjectGraphNode.Type> newVisible = EnumSet.noneOf(ProjectGraphNode.Type.class);
                    for (int i = 0; i < types.length; i++) {
                        if (checked[i]) newVisible.add(types[i]);
                    }
                    binding.graphView.setVisibleTypes(newVisible);
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    private String getTypeLabel(ProjectGraphNode.Type type) {
        switch (type) {
            case PROJECT: return getString(R.string.project_graph_node_project);
            case ACTIVITY: return getString(R.string.project_graph_node_activity);
            case EVENT: return getString(R.string.project_graph_node_event);
            case BLOCK: return getString(R.string.project_graph_node_block);
            case FUNCTION: return "Functions";
            case VARIABLE: return "Variables";
            case COMPONENT: return "Components";
        }
        return type.name();
    }

    private void showNodeDetails(ProjectGraphNode node) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(node.label)
                .setMessage("Тип: " + node.type.name() + "\nID: " + node.id
                        + (node.parentId != null ? "\nРодитель: " + node.parentId : ""))
                .setPositiveButton(android.R.string.ok, null)
                .show();
    }
}
