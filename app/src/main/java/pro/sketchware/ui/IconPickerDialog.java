package pro.sketchware.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pro.sketchware.R;

/**
 * Диалог выбора иконки экрана с категориями, поиском, листанием.
 * Несуществующие иконки автоматически скрываются.
 */
public class IconPickerDialog {

    public interface Callback {
        void onIconPicked(String icon);
    }

    private static final String PREFS = "icon_picker_prefs";
    private static final String KEY_MIXED = "mixed_search";

    private static final Map<String, List<String>> CATEGORIES = new LinkedHashMap<>();
    static {
        CATEGORIES.put("Общие", Arrays.asList(
                "ic_mtrl_home", "ic_mtrl_settings", "ic_mtrl_star", "ic_mtrl_favorite",
                "ic_mtrl_person", "ic_mtrl_people", "ic_mtrl_search", "ic_mtrl_menu",
                "ic_mtrl_dashboard", "ic_mtrl_list", "ic_mtrl_build", "ic_mtrl_extension",
                "ic_mtrl_lightbulb", "ic_mtrl_psychology", "ic_mtrl_code", "ic_mtrl_security"
        ));
        CATEGORIES.put("Действия", Arrays.asList(
                "ic_mtrl_add", "ic_mtrl_edit", "ic_mtrl_delete", "ic_mtrl_save",
                "ic_mtrl_close", "ic_mtrl_check", "ic_mtrl_undo", "ic_mtrl_redo",
                "ic_mtrl_refresh", "ic_mtrl_share", "ic_mtrl_download", "ic_mtrl_upload"
        ));
        CATEGORIES.put("Стрелки", Arrays.asList(
                "ic_mtrl_arrow_left", "ic_mtrl_arrow_right", "ic_mtrl_arrow_up", "ic_mtrl_arrow_down",
                "ic_mtrl_arrow_drop_down", "ic_mtrl_arrow_drop_up", "ic_mtrl_menu"
        ));
        CATEGORIES.put("Медиа", Arrays.asList(
                "ic_mtrl_image", "ic_mtrl_play", "ic_mtrl_pause", "ic_mtrl_stop",
                "ic_mtrl_volume", "ic_mtrl_video", "ic_mtrl_camera"
        ));
        CATEGORIES.put("Связь", Arrays.asList(
                "ic_mtrl_email", "ic_mtrl_message", "ic_mtrl_wifi", "ic_mtrl_bluetooth",
                "ic_mtrl_cloud", "ic_mtrl_notifications", "ic_mtrl_sync", "ic_mtrl_rss_feed"
        ));
        CATEGORIES.put("Разное", Arrays.asList(
                "ic_mtrl_calendar", "ic_mtrl_time", "ic_mtrl_timer", "ic_mtrl_location",
                "ic_mtrl_lock", "ic_mtrl_vpn", "ic_mtrl_bug_report", "ic_mtrl_apk_document"
        ));
    }

    private final Context context;
    private final String currentIcon;
    private final Callback callback;

    private String selectedCategory;
    private boolean mixedMode;
    private boolean listAll;
    private String searchQuery = "";

    private ImageView previewView;
    private IconAdapter adapter;
    private TextView pageInfo;
    private Button prevBtn, nextBtn;

    private final List<String> filteredIcons = new ArrayList<>();
    private static final int ICONS_PER_PAGE = 30;
    private int currentPage = 0;

    public IconPickerDialog(Context context, String currentIcon, Callback callback) {
        this.context = context;
        this.currentIcon = currentIcon;
        this.callback = callback;
        this.selectedCategory = CATEGORIES.keySet().iterator().next();
        this.mixedMode = prefs().getBoolean(KEY_MIXED, false);
    }

    private SharedPreferences prefs() {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    public void show() {
        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(16);
        content.setPadding(pad, pad, pad, pad);

        // Поиск
        EditText search = new EditText(context);
        search.setHint("Поиск...");
        search.setSingleLine(true);
        content.addView(search);

        // Превью
        previewView = new ImageView(context);
        LinearLayout.LayoutParams previewLp = new LinearLayout.LayoutParams(dp(64), dp(64));
        previewLp.gravity = Gravity.CENTER_HORIZONTAL;
        previewLp.topMargin = dp(12);
        previewView.setLayoutParams(previewLp);
        setPreview(currentIcon);
        content.addView(previewView);

        // Категории
        android.widget.HorizontalScrollView catScroll = new android.widget.HorizontalScrollView(context);
        LinearLayout catRow = new LinearLayout(context);
        catRow.setOrientation(LinearLayout.HORIZONTAL);

        for (String cat : CATEGORIES.keySet()) {
            TextView chip = new TextView(context);
            chip.setText(cat);
            chip.setPadding(dp(12), dp(8), dp(12), dp(8));
            chip.setBackgroundResource(R.drawable.single_choice_background);
            chip.setOnClickListener(v -> {
                selectedCategory = cat;
                currentPage = 0;
                refreshIcons();
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(dp(4), dp(4), dp(4), dp(4));
            chip.setLayoutParams(lp);
            catRow.addView(chip);
        }
        catScroll.addView(catRow);
        catScroll.setPadding(0, dp(8), 0, 0);
        content.addView(catScroll);

        // Сетка иконок — RecyclerView
        RecyclerView recycler = new RecyclerView(context);
        LinearLayout.LayoutParams rvLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(280));
        rvLp.topMargin = dp(8);
        recycler.setLayoutParams(rvLp);
        recycler.setLayoutManager(new GridLayoutManager(context, 5));
        adapter = new IconAdapter();
        recycler.setAdapter(adapter);
        content.addView(recycler);

        // Кнопки листания
        LinearLayout navRow = new LinearLayout(context);
        navRow.setOrientation(LinearLayout.HORIZONTAL);
        navRow.setGravity(Gravity.CENTER_VERTICAL);

        prevBtn = new Button(context);
        prevBtn.setText("←");
        prevBtn.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                renderPage();
            }
        });

        pageInfo = new TextView(context);
        pageInfo.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams infoLp = new LinearLayout.LayoutParams(0,
                ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        pageInfo.setLayoutParams(infoLp);

        nextBtn = new Button(context);
        nextBtn.setText("→");
        nextBtn.setOnClickListener(v -> {
            int totalPages = getTotalPages();
            if (currentPage < totalPages - 1) {
                currentPage++;
                renderPage();
            }
        });

        navRow.addView(prevBtn);
        navRow.addView(pageInfo);
        navRow.addView(nextBtn);
        content.addView(navRow);

        // Чекбокс "Листать все иконки"
        CheckBox listAllCb = new CheckBox(context);
        listAllCb.setText("Листать все иконки");
        listAllCb.setChecked(listAll);
        listAllCb.setOnCheckedChangeListener((btn, checked) -> {
            listAll = checked;
            currentPage = 0;
            refreshIcons();
        });
        content.addView(listAllCb);

        // Поиск-слушатель
        search.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
            @Override public void afterTextChanged(android.text.Editable s) {
                searchQuery = s.toString().trim().toLowerCase();
                currentPage = 0;
                refreshIcons();
            }
        });

        new AlertDialog.Builder(context)
                .setTitle("Выберите иконку")
                .setView(content)
                .setPositiveButton("ОК", (d, w) -> {})
                .setNegativeButton("Отмена", null)
                .show();

        refreshIcons();
    }

    private boolean iconExists(String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName()) != 0;
    }

    /** Обновляет список иконок согласно фильтрам. */
    private void refreshIcons() {
        filteredIcons.clear();

        List<String> source;
        if (listAll || mixedMode) {
            source = new ArrayList<>();
            for (List<String> list : CATEGORIES.values()) source.addAll(list);
        } else {
            source = CATEGORIES.get(selectedCategory);
        }
        if (source == null) source = new ArrayList<>();

        for (String icon : source) {
            if (!iconExists(icon)) continue;
            if (!searchQuery.isEmpty() && !icon.toLowerCase().contains(searchQuery)) continue;
            filteredIcons.add(icon);
        }

        renderPage();
    }

    private int getTotalPages() {
        return Math.max(1, (filteredIcons.size() + ICONS_PER_PAGE - 1) / ICONS_PER_PAGE);
    }

    private void renderPage() {
        int totalPages = getTotalPages();
        if (currentPage >= totalPages) currentPage = totalPages - 1;
        if (currentPage < 0) currentPage = 0;

        int from = currentPage * ICONS_PER_PAGE;
        int to = Math.min(from + ICONS_PER_PAGE, filteredIcons.size());

        List<String> pageIcons = new ArrayList<>(filteredIcons.subList(from, to));
        adapter.setIcons(pageIcons);

        pageInfo.setText((currentPage + 1) + " / " + totalPages);
        prevBtn.setEnabled(currentPage > 0);
        nextBtn.setEnabled(currentPage < totalPages - 1);
    }

    private void setPreview(String icon) {
        if (previewView == null) return;
        if (icon == null || icon.isEmpty()) {
            previewView.setImageResource(R.drawable.ic_mtrl_image);
            return;
        }
        if (icon.startsWith("@drawable/")) {
            String name = icon.substring("@drawable/".length());
            int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            previewView.setImageResource(resId != 0 ? resId : R.drawable.ic_mtrl_image);
        } else {
            previewView.setImageResource(R.drawable.ic_mtrl_image);
        }
    }

    private int dp(int v) {
        return Math.round(v * context.getResources().getDisplayMetrics().density);
    }

    /** Адаптер сетки иконок. */
    private class IconAdapter extends RecyclerView.Adapter<IconAdapter.VH> {
        private final List<String> icons = new ArrayList<>();

        void setIcons(List<String> newIcons) {
            icons.clear();
            icons.addAll(newIcons);
            notifyDataSetChanged();
        }

        @Override
        public VH onCreateViewHolder(ViewGroup parent, int viewType) {
            ImageView iv = new ImageView(context);
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(dp(56), dp(56));
            lp.setMargins(dp(4), dp(4), dp(4), dp(4));
            iv.setLayoutParams(lp);
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv.setPadding(dp(8), dp(8), dp(8), dp(8));
            iv.setBackgroundResource(R.drawable.single_choice_background);
            return new VH(iv);
        }

        @Override
        public void onBindViewHolder(VH h, int position) {
            String name = icons.get(position);
            int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            h.iv.setImageResource(resId != 0 ? resId : R.drawable.ic_mtrl_image);
            h.iv.setOnClickListener(v -> {
                String value = "@drawable/" + name;
                setPreview(value);
                callback.onIconPicked(value);
            });
        }

        @Override
        public int getItemCount() {
            return icons.size();
        }

        class VH extends RecyclerView.ViewHolder {
            final ImageView iv;
            VH(ImageView v) { super(v); iv = v; }
        }
    }
}
