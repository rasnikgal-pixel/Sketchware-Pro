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
                GraphSettings settings = new GraphSettings(ProjectGraphActivity.this);
                binding.graphView.setShowEdges(settings.isShowEdges());
                binding.graphView.setDragEnabled(settings.isDragNodesEnabled());
                binding.graphView.setShowBlocks(settings.isShowBlocks());
                binding.graphView.setShowVariables(settings.isShowVariables());
                binding.graphView.setShowComponents(settings.isShowComponents());
                binding.graphView.setShowFunctions(settings.isShowFunctions());
                binding.graphView.setActivityFilter(settings.getActivityFilter());
                binding.graphView.setGraph(graph);
                binding.graphView.applyLayout(settings.getLayout(), settings.getLevelGap());
            });
        }).start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_project_graph, menu);

        MenuItem searchItem = menu.findItem(R.id.action_search);
        if (searchItem != null) {
            androidx.appcompat.widget.SearchView searchView =
                    (androidx.appcompat.widget.SearchView) searchItem.getActionView();
            if (searchView != null) {
                searchView.setQueryHint(getString(R.string.graph_search_hint));
                searchView.setOnQueryTextListener(
                        new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        applySearch(query);
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        applySearch(newText);
                        return true;
                    }
                });
            }
        }
        return true;
    }

    /** Применить поиск: подсветка + автоцентрирование. */
    private void applySearch(String query) {
        if (binding == null || binding.graphView == null) return;
        binding.graphView.setSearchQuery(query);
        if (query != null && !query.isEmpty() && graph != null) {
            String lower = query.toLowerCase();
            for (ProjectGraphNode n : graph.nodes) {
                if (n.label.toLowerCase().contains(lower)
                        || n.id.toLowerCase().contains(lower)) {
                    binding.graphView.scrollToNode(n.id);
                    break;
                }
            }
        }
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
        } else if (id == R.id.action_export_json) {
            exportJson();
            return true;
        } else if (id == R.id.action_export_svg) {
            exportSvg();
            return true;
        } else if (id == R.id.action_settings) {
            showGraphSettingsDialog();
            return true;
        } else if (id == R.id.action_reset_filter) {
            GraphSettings settings = new GraphSettings(this);
            settings.setActivityFilter(null);
            binding.graphView.setActivityFilter(null);
            android.widget.Toast.makeText(this,
                    getString(R.string.graph_filter_reset),
                    android.widget.Toast.LENGTH_SHORT).show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Диалог настроек графа. */
    private void showGraphSettingsDialog() {
        GraphSettings settings = new GraphSettings(this);
        android.view.View view = getLayoutInflater().inflate(
                R.layout.dialog_graph_settings, null);

        android.widget.RadioGroup radioLayout = view.findViewById(R.id.radio_layout);
        android.widget.SeekBar seekGap = view.findViewById(R.id.seek_level_gap);
        com.google.android.material.materialswitch.MaterialSwitch swEdges =
                view.findViewById(R.id.switch_show_edges);
        com.google.android.material.materialswitch.MaterialSwitch swDrag =
                view.findViewById(R.id.switch_drag_nodes);
        com.google.android.material.materialswitch.MaterialSwitch swExport =
                view.findViewById(R.id.switch_export_full);
        com.google.android.material.materialswitch.MaterialSwitch swShowBlocks =
                view.findViewById(R.id.switch_show_blocks);
        com.google.android.material.materialswitch.MaterialSwitch swShowVariables =
                view.findViewById(R.id.switch_show_variables);
        com.google.android.material.materialswitch.MaterialSwitch swShowComponents =
                view.findViewById(R.id.switch_show_components);
        com.google.android.material.materialswitch.MaterialSwitch swShowFunctions =
                view.findViewById(R.id.switch_show_functions);

        // Раскладка — выбрать текущую
        int layout = settings.getLayout();
        switch (layout) {
            case GraphSettings.LAYOUT_HORIZONTAL -> radioLayout.check(R.id.radio_layout_horizontal);
            case GraphSettings.LAYOUT_RADIAL -> radioLayout.check(R.id.radio_layout_radial);
            case GraphSettings.LAYOUT_GRID -> radioLayout.check(R.id.radio_layout_grid);
            case GraphSettings.LAYOUT_FORCE -> radioLayout.check(R.id.radio_layout_force);
            default -> radioLayout.check(R.id.radio_layout_tree);
        }

        seekGap.setMax(2);
        seekGap.setProgress(settings.getLevelGap());
        swEdges.setChecked(settings.isShowEdges());
        swDrag.setChecked(settings.isDragNodesEnabled());
        swExport.setChecked(settings.isExportFullTree());
        swShowBlocks.setChecked(settings.isShowBlocks());
        swShowVariables.setChecked(settings.isShowVariables());
        swShowComponents.setChecked(settings.isShowComponents());
        swShowFunctions.setChecked(settings.isShowFunctions());

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.graph_settings_title)
                .setView(view)
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    // Определить раскладку
                    int newLayout;
                    int checkedId = radioLayout.getCheckedRadioButtonId();
                    if (checkedId == R.id.radio_layout_horizontal) {
                        newLayout = GraphSettings.LAYOUT_HORIZONTAL;
                    } else if (checkedId == R.id.radio_layout_radial) {
                        newLayout = GraphSettings.LAYOUT_RADIAL;
                    } else if (checkedId == R.id.radio_layout_grid) {
                        newLayout = GraphSettings.LAYOUT_GRID;
                    } else if (checkedId == R.id.radio_layout_force) {
                        newLayout = GraphSettings.LAYOUT_FORCE;
                    } else {
                        newLayout = GraphSettings.LAYOUT_TREE;
                    }
                    int newGap = seekGap.getProgress();

                    // Сохранить
                    final int layoutToApply = newLayout;
                    final int gapToApply = newGap;
                    Runnable applyAll = () -> {
                        settings.setLayout(layoutToApply);
                        settings.setLevelGap(gapToApply);
                        settings.setShowEdges(swEdges.isChecked());
                        settings.setDragNodesEnabled(swDrag.isChecked());
                        settings.setExportFullTree(swExport.isChecked());
                        settings.setShowBlocks(swShowBlocks.isChecked());
                        settings.setShowVariables(swShowVariables.isChecked());
                        settings.setShowComponents(swShowComponents.isChecked());
                        settings.setShowFunctions(swShowFunctions.isChecked());

                        // Применить
                        binding.graphView.setDragEnabled(swDrag.isChecked());
                        binding.graphView.setShowEdges(swEdges.isChecked());
                        binding.graphView.setShowBlocks(swShowBlocks.isChecked());
                        binding.graphView.setShowVariables(swShowVariables.isChecked());
                        binding.graphView.setShowComponents(swShowComponents.isChecked());
                        binding.graphView.setShowFunctions(swShowFunctions.isChecked());
                        binding.graphView.applyLayout(layoutToApply, gapToApply);

                        android.widget.Toast.makeText(this,
                                getString(R.string.graph_settings_applied),
                                android.widget.Toast.LENGTH_SHORT).show();
                    };

                    if (layoutToApply == GraphSettings.LAYOUT_FORCE
                            && graph != null
                            && graph.nodes.size() > 200) {
                        final int nodeCount = graph.nodes.size();
                        new MaterialAlertDialogBuilder(this)
                                .setTitle(R.string.graph_force_warning_title)
                                .setMessage(getString(R.string.graph_force_warning_msg, nodeCount))
                                .setPositiveButton(R.string.common_word_continue,
                                        (d2, w2) -> applyAll.run())
                                .setNegativeButton(R.string.common_word_cancel, null)
                                .show();
                    } else {
                        applyAll.run();
                    }
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

    /** Экспорт графа в JSON. */
    private void exportJson() {
        if (graph == null || graph.nodes.isEmpty()) {
            android.widget.Toast.makeText(this,
                    getString(R.string.graph_export_empty),
                    android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            org.json.JSONObject root = new org.json.JSONObject();
            root.put("project", projectName == null ? "" : projectName);
            root.put("exportedAt", System.currentTimeMillis());

            org.json.JSONArray nodesArr = new org.json.JSONArray();
            for (ProjectGraphNode n : graph.nodes) {
                org.json.JSONObject o = new org.json.JSONObject();
                o.put("id", n.id);
                o.put("label", n.label);
                o.put("type", n.type.name());
                o.put("parentId", n.parentId == null ? org.json.JSONObject.NULL : n.parentId);
                if (n.extra1 != null) o.put("extra1", n.extra1);
                if (n.extra2 != null) o.put("extra2", n.extra2);
                o.put("x", n.x);
                o.put("y", n.y);
                nodesArr.put(o);
            }
            root.put("nodes", nodesArr);

            org.json.JSONArray edgesArr = new org.json.JSONArray();
            for (ProjectGraph.Edge e : graph.edges) {
                org.json.JSONObject o = new org.json.JSONObject();
                o.put("from", e.fromId);
                o.put("to", e.toId);
                if (e.label != null) o.put("label", e.label);
                edgesArr.put(o);
            }
            root.put("edges", edgesArr);

            java.io.File dir = new java.io.File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "Sketchware/Graphs");
            if (!dir.exists() && !dir.mkdirs()) {
                throw new java.io.IOException("Cannot create directory");
            }
            String safeName = (projectName == null || projectName.isEmpty())
                    ? "project" : projectName.replaceAll("[^a-zA-Z0-9_]", "_");
            String fileName = safeName + "_" + System.currentTimeMillis() + ".json";
            java.io.File outFile = new java.io.File(dir, fileName);

            try (java.io.FileWriter fw = new java.io.FileWriter(outFile)) {
                fw.write(root.toString(2));
            }

            android.widget.Toast.makeText(this,
                    getString(R.string.graph_export_success) + "\n" + outFile.getAbsolutePath(),
                    android.widget.Toast.LENGTH_LONG).show();
        } catch (Throwable t) {
            pro.sketchware.smartdrop.DebugLogger.get(this)
                    .d("ProjectGraph", "export_json_error", String.valueOf(t.getMessage()));
            android.widget.Toast.makeText(this,
                    getString(R.string.graph_export_error) + ": " + t.getMessage(),
                    android.widget.Toast.LENGTH_LONG).show();
        }
    }

    /** Экспорт графа в SVG. */
    private void exportSvg() {
        if (graph == null || graph.nodes.isEmpty()) {
            android.widget.Toast.makeText(this,
                    getString(R.string.graph_export_empty),
                    android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            final float r = 14f;
            final float labelGap = 34f;
            final float pad = 40f;
            final float legendH = 130f;

            float minX = Float.MAX_VALUE, minY = Float.MAX_VALUE;
            float maxX = -Float.MAX_VALUE, maxY = -Float.MAX_VALUE;
            for (ProjectGraphNode n : graph.nodes) {
                if (n.x < minX) minX = n.x;
                if (n.y < minY) minY = n.y;
                if (n.x > maxX) maxX = n.x;
                if (n.y > maxY) maxY = n.y;
            }
            float offX = -minX + pad;
            float offY = -minY + pad;
            float w = (maxX - minX) + pad * 2;
            float h = (maxY - minY) + pad * 2 + labelGap + legendH;

            java.util.Map<String, ProjectGraphNode> byId = new java.util.HashMap<>();
            for (ProjectGraphNode n : graph.nodes) byId.put(n.id, n);

            StringBuilder sb = new StringBuilder();
            sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n");
            sb.append("<svg xmlns=\"http://www.w3.org/2000/svg\" ")
              .append("width=\"").append(w).append("\" ")
              .append("height=\"").append(h).append("\" ")
              .append("viewBox=\"0 0 ").append(w).append(" ").append(h).append("\">\n");
            sb.append("<rect width=\"100%\" height=\"100%\" fill=\"#FAFAFA\"/>\n");

            // Рёбра
            sb.append("<g stroke=\"#9E9E9E\" stroke-width=\"1.2\" fill=\"none\">\n");
            for (ProjectGraph.Edge e : graph.edges) {
                ProjectGraphNode a = byId.get(e.fromId);
                ProjectGraphNode b = byId.get(e.toId);
                if (a == null || b == null) continue;
                sb.append("<line x1=\"").append(a.x + offX).append("\" ")
                  .append("y1=\"").append(a.y + offY).append("\" ")
                  .append("x2=\"").append(b.x + offX).append("\" ")
                  .append("y2=\"").append(b.y + offY).append("\"/>\n");
            }
            sb.append("</g>\n");

            // Узлы
            for (ProjectGraphNode n : graph.nodes) {
                String color = svgColorFor(n.type);
                float cx = n.x + offX;
                float cy = n.y + offY;
                sb.append("<circle cx=\"").append(cx).append("\" ")
                  .append("cy=\"").append(cy).append("\" r=\"").append(r).append("\" ")
                  .append("fill=\"").append(color).append("\" ")
                  .append("stroke=\"#FFFFFF\" stroke-width=\"2\"/>\n");
                String label = n.label == null ? "" : n.label;
                if (label.length() > 40) label = label.substring(0, 37) + "...";
                label = label.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;");
                sb.append("<text x=\"").append(cx).append("\" ")
                  .append("y=\"").append(cy + labelGap).append("\" ")
                  .append("font-family=\"sans-serif\" font-size=\"11\" ")
                  .append("text-anchor=\"middle\" fill=\"#212121\">")
                  .append(label).append("</text>\n");
            }

            // Легенда
            float lx = 20f;
            float ly = h - legendH + 10f;
            sb.append("<g font-family=\"sans-serif\" font-size=\"12\" fill=\"#212121\">\n");
            sb.append("<text x=\"").append(lx).append("\" y=\"").append(ly)
              .append("\" font-weight=\"bold\">Типы узлов:</text>\n");
            ProjectGraphNode.Type[] types = {
                    ProjectGraphNode.Type.PROJECT,
                    ProjectGraphNode.Type.ACTIVITY,
                    ProjectGraphNode.Type.EVENT,
                    ProjectGraphNode.Type.BLOCK,
                    ProjectGraphNode.Type.VARIABLE,
                    ProjectGraphNode.Type.COMPONENT,
                    ProjectGraphNode.Type.FUNCTION
            };
            String[] names = {"PROJECT", "ACTIVITY", "EVENT", "BLOCK", "VARIABLE", "COMPONENT", "FUNCTION"};
            for (int i = 0; i < types.length; i++) {
                float y = ly + 18f * (i + 1);
                sb.append("<circle cx=\"").append(lx + 6f).append("\" ")
                  .append("cy=\"").append(y - 4f).append("\" r=\"6\" ")
                  .append("fill=\"").append(svgColorFor(types[i])).append("\"/>\n");
                sb.append("<text x=\"").append(lx + 20f).append("\" y=\"").append(y)
                  .append("\">").append(names[i]).append("</text>\n");
            }
            sb.append("</g>\n");

            sb.append("</svg>\n");

            java.io.File dir = new java.io.File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "Sketchware/Graphs");
            if (!dir.exists() && !dir.mkdirs()) {
                throw new java.io.IOException("Cannot create directory");
            }
            String safeName = (projectName == null || projectName.isEmpty())
                    ? "project" : projectName.replaceAll("[^a-zA-Z0-9_]", "_");
            String fileName = safeName + "_" + System.currentTimeMillis() + ".svg";
            java.io.File outFile = new java.io.File(dir, fileName);

            try (java.io.FileWriter fw = new java.io.FileWriter(outFile)) {
                fw.write(sb.toString());
            }

            android.widget.Toast.makeText(this,
                    getString(R.string.graph_export_success) + "\n" + outFile.getAbsolutePath(),
                    android.widget.Toast.LENGTH_LONG).show();
        } catch (Throwable t) {
            pro.sketchware.smartdrop.DebugLogger.get(this)
                    .d("ProjectGraph", "export_svg_error", String.valueOf(t.getMessage()));
            android.widget.Toast.makeText(this,
                    getString(R.string.graph_export_error) + ": " + t.getMessage(),
                    android.widget.Toast.LENGTH_LONG).show();
        }
    }

    /** Цвет узла для SVG. */
    private String svgColorFor(ProjectGraphNode.Type t) {
        switch (t) {
            case PROJECT:   return "#9C27B0";
            case ACTIVITY:  return "#2196F3";
            case EVENT:     return "#FF9800";
            case BLOCK:     return "#4CAF50";
            case VARIABLE:  return "#03A9F4";
            case COMPONENT: return "#795548";
            case FUNCTION:  return "#E91E63";
            default:        return "#607D8B";
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
        String msg = "Тип: " + node.type.name() + "\nID: " + node.id
                + (node.parentId != null ? "\nРодитель: " + node.parentId : "");

        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(this)
                .setTitle(node.label)
                .setMessage(msg)
                .setNegativeButton(R.string.common_word_cancel, null);

        // Опция "Открыть в редакторе" для EVENT
        if (node.type == ProjectGraphNode.Type.EVENT
                && node.extra1 != null && node.extra2 != null) {
            builder.setPositiveButton(R.string.graph_open_in_editor, (d, w) -> {
                openEventInLogicEditor(node.extra1, node.extra2);
            });
        } else {
            builder.setPositiveButton(android.R.string.ok, null);
        }

        // Опция фильтра для Activity и Event
        if (node.type == ProjectGraphNode.Type.ACTIVITY) {
            builder.setNeutralButton(R.string.graph_filter_only_this, (d, w) -> {
                GraphSettings settings = new GraphSettings(this);
                settings.setActivityFilter(node.id);
                binding.graphView.setActivityFilter(node.id);
            });
        } else if (node.type == ProjectGraphNode.Type.EVENT
                || node.type == ProjectGraphNode.Type.BLOCK) {
            builder.setNeutralButton(R.string.graph_filter_only_this, (d, w) -> {
                String actId = findActivityAncestorId(node);
                if (actId != null) {
                    GraphSettings settings = new GraphSettings(this);
                    settings.setActivityFilter(actId);
                    binding.graphView.setActivityFilter(actId);
                }
            });
        }

        builder.show();
    }

    /** Открыть событие в LogicEditor. */
    private void openEventInLogicEditor(String javaName, String eventKey) {
        try {
            // eventKey = "button1_onClick" → id = "button1", eventName = "onClick"
            int idx = eventKey.indexOf('_');
            if (idx < 0) return;
            String widgetId = eventKey.substring(0, idx);
            String eventName = eventKey.substring(idx + 1);

            // Получить ProjectFileBean
            com.besome.sketch.beans.ProjectFileBean pfb = null;
            try {
                pfb = a.a.a.jC.b(scId).a(javaName);
            } catch (Throwable ignored) {
            }
            if (pfb == null) {
                android.widget.Toast.makeText(this,
                        getString(R.string.graph_search_not_found),
                        android.widget.Toast.LENGTH_SHORT).show();
                return;
            }

            android.content.Intent intent = new android.content.Intent(this,
                    com.besome.sketch.editor.LogicEditorActivity.class);
            intent.setFlags(android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP);
            intent.putExtra("sc_id", scId);
            intent.putExtra("id", widgetId);
            intent.putExtra("event", eventName);
            intent.putExtra("project_file", pfb);
            intent.putExtra("event_text", eventName);
            startActivity(intent);
        } catch (Throwable t) {
            pro.sketchware.smartdrop.DebugLogger.get(this)
                    .d("ProjectGraph", "open_editor_error", String.valueOf(t.getMessage()));
            android.widget.Toast.makeText(this,
                    getString(R.string.graph_export_error) + ": " + t.getMessage(),
                    android.widget.Toast.LENGTH_LONG).show();
        }
    }

    private String findActivityAncestorId(ProjectGraphNode node) {
        if (graph == null || node == null) return null;
        ProjectGraphNode current = node;
        while (current != null && current.parentId != null) {
            current = graph.findNode(current.parentId);
            if (current != null && current.type == ProjectGraphNode.Type.ACTIVITY) {
                return current.id;
            }
        }
        return null;
    }
}
