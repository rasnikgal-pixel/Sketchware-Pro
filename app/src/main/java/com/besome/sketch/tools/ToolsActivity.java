package com.besome.sketch.tools;

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
            getSupportActionBar().setTitle("Инструменты");
        }

        toolsList = findViewById(R.id.tools_list);
        toolsList.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ToolAdapter();
        adapter.setItems(buildTools());
        toolsList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.add(Menu.NONE, 101, Menu.NONE, "Справка")
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
                    "category-interfeys-prilozheniya");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /** Список инструментов. */
    private List<ToolItem> buildTools() {
        List<ToolItem> items = new ArrayList<>();
        items.add(new ToolItem(
                "\uD83D\uDCE6 Экспорт проекта (.skproj)",
                "Сохранить проект в переносимую папку",
                R.drawable.ic_mtrl_save,
                this::onExportSkproj
        ));
        items.add(new ToolItem(
                "\uD83D\uDCBE Резервные копии",
                "Просмотр и управление .swb бэкапами",
                R.drawable.ic_mtrl_history,
                this::onBackups
        ));
        items.add(new ToolItem(
                "\uD83D\uDCE5 Импорт проекта (.skproj)",
                "Загрузить проект из файла",
                R.drawable.ic_mtrl_download,
                this::onImportSkproj
        ));
        return items;
    }

    private void onExportSkproj() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Экспорт .skproj")
                .setMessage("Инструмент в разработке.\n\nФункция выбора проекта появится в следующей версии.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void onBackups() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Резервные копии")
                .setMessage("Управление .swb бэкапами.\n\nПоявится в следующей версии.")
                .setPositiveButton("OK", null)
                .show();
    }

    private void onImportSkproj() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Импорт .skproj")
                .setMessage("Инструмент в разработке.\n\nПоявится в следующей версии.")
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
