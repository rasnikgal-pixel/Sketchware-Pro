package pro.sketchware.templates;


import mod.hey.studios.util.Helper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import com.besome.sketch.lib.base.BaseAppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

import pro.sketchware.R;

/**
 * Галерея шаблонов экранов.
 * Возвращает выбранный id шаблона через setResult(RESULT_OK, intent.putExtra("template_id", id)).
 */
public class TemplateGalleryActivity extends BaseAppCompatActivity {

    public static final String EXTRA_TEMPLATE_ID = "template_id";

    private ScreenTemplates templates;
    private RecyclerView recyclerView;
    private ChipGroup categoryGroup;
    private TemplateAdapter adapter;
    private String selectedCategory = null; // null = Helper.getResString(R.string.auto_template_gallery_activity_001)
    private String searchQuery = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_template_gallery);

        templates = ScreenTemplates.get(this);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recycler);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        adapter = new TemplateAdapter();
        recyclerView.setAdapter(adapter);

        categoryGroup = findViewById(R.id.category_group);
        buildCategories();

        // Поиск
        TextInputEditText inputSearch = findViewById(R.id.input_search);
        inputSearch.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(android.text.Editable s) {
                searchQuery = s.toString().trim().toLowerCase();
                // При активном поиске — сбрасываем категорию на "Все"
                if (!searchQuery.isEmpty() && selectedCategory != null) {
                    selectedCategory = null;
                    // Снимаем выделение с чипов
                    for (int i = 0; i < categoryGroup.getChildCount(); i++) {
                        android.view.View child = categoryGroup.getChildAt(i);
                        if (child instanceof Chip) {
                            ((Chip) child).setChecked(i == 0);
                        }
                    }
                }
                refreshList();
            }
        });

        refreshList();
    }

    private void buildCategories() {
        // Показываем только один chip "Все" — в UI доступны лишь 5 шаблонов,
        // остальные скрыты в ScreenTemplates.BUNDLED_IDS.
        addChip(getString(R.string.templates_category_all), null, true);
        // Категории намеренно не добавляем: 5 шаблонов помещаются в один список.
    }

    private void addChip(String name, String categoryId, boolean selected) {
        Chip chip = new Chip(this);
        chip.setText(name);
        chip.setCheckable(true);
        chip.setChecked(selected);
        chip.setTag(categoryId);
        chip.setOnClickListener(v -> {
            selectedCategory = categoryId;
            refreshList();
        });
        categoryGroup.addView(chip);
    }

    private void refreshList() {
        List<ScreenTemplate> list = (selectedCategory == null)
                ? templates.getAll()
                : templates.getByCategory(selectedCategory);

        // Фильтр по поисковому запросу
        if (!searchQuery.isEmpty()) {
            List<ScreenTemplate> filtered = new ArrayList<>();
            for (ScreenTemplate t : list) {
                if (matchesSearch(t, searchQuery)) {
                    filtered.add(t);
                }
            }
            list = filtered;
        }

        adapter.setData(list);
    }

    private boolean matchesSearch(ScreenTemplate t, String query) {
        if (t.name != null && t.name.toLowerCase().contains(query)) return true;
        if (t.description != null && t.description.toLowerCase().contains(query)) return true;
        if (t.id != null && t.id.toLowerCase().contains(query)) return true;
        return false;
    }

    private void onTemplateSelected(ScreenTemplate t) {
        // Формируем ProjectFileBean с префиксом "template:"
        com.besome.sketch.beans.ProjectFileBean pfb = new com.besome.sketch.beans.ProjectFileBean(
                com.besome.sketch.beans.ProjectFileBean.PROJECT_FILE_TYPE_ACTIVITY,
                null,
                "template:" + t.id);
        setResult(RESULT_OK, new android.content.Intent().putExtra("preset_data", pfb));
        finish();
    }

    private class TemplateAdapter extends RecyclerView.Adapter<TemplateAdapter.VH> {

        private final List<ScreenTemplate> data = new ArrayList<>();

        void setData(List<ScreenTemplate> newData) {
            data.clear();
            data.addAll(newData);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_template_card, parent, false);
            return new VH(v);
        }

        @Override
        public void onBindViewHolder(@NonNull VH h, int position) {
            ScreenTemplate t = data.get(position);
            h.name.setText(t.name);
            ScreenTemplates.Category cat = templates.getCategory(t.category);
            h.category.setText(cat != null ? cat.name : "");
            h.description.setText(t.description != null ? t.description : "");

            // Иконка предпросмотра — из drawable
            int resId = getResources().getIdentifier(
                    t.previewIcon != null ? t.previewIcon : "ic_mtrl_image",
                    "drawable", getPackageName());
            if (resId != 0) {
                h.icon.setImageResource(resId);
            } else {
                h.icon.setImageResource(R.drawable.ic_mtrl_image);
            }

            h.itemView.setOnClickListener(v -> onTemplateSelected(t));
        }

        @Override
        public int getItemCount() {
            return data.size();
        }

        class VH extends RecyclerView.ViewHolder {
            final ImageView icon;
            final TextView name;
            final TextView category;
            final TextView description;

            VH(View item) {
                super(item);
                icon = item.findViewById(R.id.icon);
                name = item.findViewById(R.id.name);
                category = item.findViewById(R.id.category);
                description = item.findViewById(R.id.description);
            }
        }
    }
}
