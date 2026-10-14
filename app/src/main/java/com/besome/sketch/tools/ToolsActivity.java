package com.besome.sketch.tools;


import mod.hey.studios.util.Helper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.List;

import pro.sketchware.R;


/**
 * Экран «Инструменты» — полезные утилиты для проектов.
 * Пока: экспорт .skproj, резервные копии, импорт.
 */
public class ToolsActivity extends BaseAppCompatActivity {

    private RecyclerView toolsList;
    private ToolAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tools);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(Helper.getResString(R.string.auto_tools_activity_001));
        }

        toolsList = findViewById(R.id.tools_list);
        toolsList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ToolAdapter();
        adapter.setItems(buildTools());
        toolsList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 101, Menu.NONE, Helper.getResString(R.string.auto_tools_activity_002))
                .setIcon(R.drawable.ic_mtrl_help)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if (item.getItemId() == 101) {
            com.besome.sketch.help.HelpOpener.open(
                    this,
                    "block-interfeys-prilozheniya");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Список инструментов. */
    private List<ToolItem> buildTools() {
        List<ToolItem> items = new ArrayList<>();
        items.add(new ToolItem(
                Helper.getResString(R.string.auto_tools_activity_003),
                Helper.getResString(R.string.auto_tools_activity_004),
                R.drawable.ic_mtrl_save,
                this::onExportSkproj
        ));
        items.add(new ToolItem(
                Helper.getResString(R.string.auto_tools_activity_005),
                Helper.getResString(R.string.auto_tools_activity_006),
                R.drawable.ic_mtrl_history,
                this::onBackups
        ));
        items.add(new ToolItem(
                Helper.getResString(R.string.auto_tools_activity_007),
                Helper.getResString(R.string.auto_tools_activity_008),
                R.drawable.ic_mtrl_download,
                this::onImportSkproj
        ));
        items.add(new ToolItem(
                Helper.getResString(R.string.tools_activity_project_graph_title),
                Helper.getResString(R.string.tools_activity_project_graph_subtitle),
                R.drawable.ic_mtrl_deployed_code,
                this::onProjectGraph
        ));
        items.add(new ToolItem(
                Helper.getResString(R.string.tools_activity_sora_title),
                Helper.getResString(R.string.tools_activity_sora_subtitle),
                R.drawable.ic_mtrl_code,
                this::onSoraEditor
        ));
        items.add(new ToolItem(
                Helper.getResString(R.string.tools_activity_logcat_title),
                Helper.getResString(R.string.tools_activity_logcat_subtitle),
                R.drawable.ic_mtrl_article,
                this::onLogcat
        ));
        items.add(new ToolItem(
                Helper.getResString(R.string.tools_activity_user_code_title),
                Helper.getResString(R.string.tools_activity_user_code_subtitle),
                R.drawable.ic_mtrl_java,
                this::onUserCode
        ));
        return items;
    }

    /** Открывает экран "Мой код". */
    private void onUserCode() {
        showProjectPickerDialog((scId, projectName) -> {
            android.content.Intent intent = new android.content.Intent(this,
                    pro.sketchware.activities.usercode.UserCodeActivity.class);
            intent.putExtra("sc_id", scId);
            startActivity(intent);
        });
    }

    /** Открывает журнал отладки. */
    private void onLogcat() {
        android.content.Intent intent = new android.content.Intent(this,
                mod.khaled.logcat.LogReaderActivity.class);
        intent.setFlags(android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    /** Открывает граф проекта с выбором проекта. */
    private void onProjectGraph() {
        showProjectPickerDialog((scId, projectName) -> {
            android.content.Intent intent = new android.content.Intent(this,
                    pro.sketchware.activities.projectgraph.ProjectGraphActivity.class);
            intent.putExtra("sc_id", scId);
            intent.putExtra("project_name", projectName);
            startActivity(intent);
        });
    }

    /** Открывает SORA-редактор с выбором проекта. */
    private void onSoraEditor() {
        showProjectPickerDialog((scId, projectName) -> {
            android.content.Intent intent = new android.content.Intent(this,
                    pro.sketchware.activities.soraeditor.SoraEditorActivity.class);
            intent.putExtra("sc_id", scId);
            startActivity(intent);
        });
    }

    private interface OnProjectPicked {
        void onPicked(String scId, String projectName);
    }

    /** Диалог выбора проекта. */
    private void showProjectPickerDialog(OnProjectPicked callback) {
        java.util.ArrayList<java.util.HashMap<String, Object>> projects = a.a.a.lC.a();
        if (projects == null || projects.isEmpty()) {
            mod.hey.studios.util.Helper.getResString(R.string.sora_xml_editor_empty);
            android.widget.Toast.makeText(this,
                    mod.hey.studios.util.Helper.getResString(R.string.sora_xml_editor_empty),
                    android.widget.Toast.LENGTH_SHORT).show();
            return;
        }

        projects.sort((p1, p2) -> {
            String n1 = String.valueOf(p1.get("my_ws_name"));
            String n2 = String.valueOf(p2.get("my_ws_name"));
            return n1.compareToIgnoreCase(n2);
        });

        String[] names = new String[projects.size()];
        final String[] ids = new String[projects.size()];
        for (int i = 0; i < projects.size(); i++) {
            java.util.HashMap<String, Object> p = projects.get(i);
            names[i] = String.valueOf(p.get("my_ws_name"));
            ids[i] = String.valueOf(p.get("sc_id"));
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.sora_xml_editor_pick_project)
                .setItems(names, (d, which) -> callback.onPicked(ids[which], names[which]))
                .setNegativeButton(mod.hey.studios.util.Helper.getResString(R.string.common_word_cancel), null)
                .show();
    }

    /** Экспорт проекта в .swb через BackupFactory. */
    private void onExportSkproj() {
        showProjectPickerDialog((scId, projectName) -> showExportOptionsDialog(scId, projectName));
    }

    /** Диалог с чекбоксами: local libs + custom blocks. */
    private void showExportOptionsDialog(final String scId, final String projectName) {
        android.widget.LinearLayout box = new android.widget.LinearLayout(this);
        box.setOrientation(android.widget.LinearLayout.VERTICAL);
        int pad = dp(16);
        box.setPadding(pad, pad, pad, 0);

        final android.widget.CheckBox cbLibs = new android.widget.CheckBox(this);
        cbLibs.setText(R.string.tools_export_option_libs);
        cbLibs.setChecked(true);
        box.addView(cbLibs);

        final android.widget.CheckBox cbBlocks = new android.widget.CheckBox(this);
        cbBlocks.setText(R.string.tools_export_option_blocks);
        cbBlocks.setChecked(true);
        box.addView(cbBlocks);

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.tools_export_title)
                .setView(box)
                .setPositiveButton(R.string.tools_export_start, (d, w) ->
                        doExport(scId, projectName, cbLibs.isChecked(), cbBlocks.isChecked()))
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    /** Фоновый экспорт и копирование в Sketchware/Exports. */
    private void doExport(final String scId, final String projectName, final boolean libs, final boolean blocks) {
        final androidx.appcompat.app.AlertDialog progress = new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                .setTitle(R.string.tools_export_title)
                .setMessage(R.string.tools_export_in_progress)
                .setCancelable(false)
                .create();
        progress.show();

        new Thread(() -> {
            String errorMsg = null;
            String outPath = null;
            try {
                mod.hey.studios.project.backup.BackupFactory bf =
                        new mod.hey.studios.project.backup.BackupFactory(scId);
                bf.setBackupLocalLibs(libs);
                bf.setBackupCustomBlocks(blocks);
                bf.backup(getApplicationContext(), projectName);

                java.io.File out = bf.getOutFile();
                if (out == null || !out.exists()) {
                    errorMsg = bf.getError();
                    if (errorMsg == null || errorMsg.isEmpty()) errorMsg = "backup() returned null";
                } else {
                    // Копируем в Sketchware/Exports
                    java.io.File exportsDir = new java.io.File(
                            android.os.Environment.getExternalStorageDirectory(),
                            "Sketchware/Exports");
                    if (!exportsDir.exists() && !exportsDir.mkdirs()) {
                        throw new java.io.IOException("Cannot create " + exportsDir.getAbsolutePath());
                    }
                    String safe = projectName.replaceAll("[^a-zA-Z0-9_\u0400-\u04FF\u4E00-\u9FFF-]", "_");
                    String filename = safe + "_" + System.currentTimeMillis() + "." + mod.hey.studios.project.backup.BackupFactory.EXTENSION;
                    java.io.File dest = new java.io.File(exportsDir, filename);
                    pro.sketchware.utility.FileUtil.copyFile(out.getAbsolutePath(), dest.getAbsolutePath());
                    outPath = dest.getAbsolutePath();
                }
            } catch (Throwable t) {
                errorMsg = String.valueOf(t.getMessage());
                pro.sketchware.smartdrop.DebugLogger.get(this)
                        .d("ToolsExport", "error", String.valueOf(t.getMessage()));
            }

            final String err = errorMsg;
            final String path = outPath;
            runOnUiThread(() -> {
                try { progress.dismiss(); } catch (Throwable ignored) {}
                if (path != null) {
                    android.widget.Toast.makeText(this,
                            getString(R.string.tools_export_success, path),
                            android.widget.Toast.LENGTH_LONG).show();
                } else {
                    android.widget.Toast.makeText(this,
                            getString(R.string.tools_export_error, err == null ? "unknown" : err),
                            android.widget.Toast.LENGTH_LONG).show();
                }
            });
        }, "tools-export-thread").start();
    }

    private void onBackups() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(Helper.getResString(R.string.auto_tools_activity_011))
                .setMessage(Helper.getResString(R.string.auto_tools_activity_012))
                .setPositiveButton("OK", null)
                .show();
    }

    private void onImportSkproj() {
        new MaterialAlertDialogBuilder(this)
                .setTitle(Helper.getResString(R.string.auto_tools_activity_013))
                .setMessage(Helper.getResString(R.string.auto_tools_activity_014))
                .setPositiveButton("OK", null)
                .show();
    }

    /** Модель пункта. */
    private static class ToolItem {
        final String title;
        final String subtitle;
        final int iconRes;
        final Runnable onClick;

        ToolItem(String title, String subtitle, int iconRes, Runnable onClick) {
            this.title = title;
            this.subtitle = subtitle;
            this.iconRes = iconRes;
            this.onClick = onClick;
        }
    }

    /** Адаптер списка инструментов. */
    private class ToolAdapter extends RecyclerView.Adapter<ToolAdapter.VH> {
        private List<ToolItem> items = new ArrayList<>();

        void setItems(List<ToolItem> items) {
            this.items = items;
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LinearLayout row = new LinearLayout(ToolsActivity.this);
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setPadding(dp(16), dp(16), dp(16), dp(16));
            row.setLayoutParams(new RecyclerView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            row.setClickable(true);
            row.setFocusable(true);

            ImageView icon = new ImageView(ToolsActivity.this);
            LinearLayout.LayoutParams iconLp = new LinearLayout.LayoutParams(dp(24), dp(24));
            icon.setLayoutParams(iconLp);

            LinearLayout texts = new LinearLayout(ToolsActivity.this);
            texts.setOrientation(LinearLayout.VERTICAL);
            LinearLayout.LayoutParams textsLp = new LinearLayout.LayoutParams(
                    0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
            textsLp.setMarginStart(dp(16));
            texts.setLayoutParams(textsLp);

            TextView title = new TextView(ToolsActivity.this);
            title.setTextSize(16);
            title.setTextColor(resolveThemeColor(com.google.android.material.R.attr.colorOnSurface));
            TextView subtitle = new TextView(ToolsActivity.this);
            subtitle.setTextSize(13);
            subtitle.setTextColor(resolveThemeColor(com.google.android.material.R.attr.colorOnSurfaceVariant));
            subtitle.setPadding(0, dp(4), 0, 0);

            texts.addView(title);
            texts.addView(subtitle);
            row.addView(icon);
            row.addView(texts);

            return new VH(row, icon, title, subtitle);
        }

        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            ToolItem item = items.get(position);
            holder.icon.setImageResource(item.iconRes);
            holder.title.setText(item.title);
            holder.subtitle.setText(item.subtitle);
            holder.itemView.setOnClickListener(v -> item.onClick.run());
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        class VH extends RecyclerView.ViewHolder {
            final ImageView icon;
            final TextView title;
            final TextView subtitle;

            VH(@NonNull View itemView, ImageView icon, TextView title, TextView subtitle) {
                super(itemView);
                this.icon = icon;
                this.title = title;
                this.subtitle = subtitle;
            }
        }
    }

    /** Возвращает цвет из атрибута текущей темы. */
    private int resolveThemeColor(int attr) {
        android.util.TypedValue tv = new android.util.TypedValue();
        if (getTheme().resolveAttribute(attr, tv, true)) {
            if (tv.type >= android.util.TypedValue.TYPE_FIRST_COLOR_INT
                    && tv.type <= android.util.TypedValue.TYPE_LAST_COLOR_INT) {
                return tv.data;
            }
        }
        return 0xFF000000;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
