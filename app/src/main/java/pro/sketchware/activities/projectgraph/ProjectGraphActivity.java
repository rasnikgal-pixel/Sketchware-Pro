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
        }
        return super.onOptionsItemSelected(item);
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
