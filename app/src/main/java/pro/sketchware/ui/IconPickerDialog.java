package pro.sketchware.ui;


import mod.hey.studios.util.Helper;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
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
 * Использует setImageTintList для корректного окрашивания VectorDrawable.
 */
public class IconPickerDialog {

    public interface Callback {
        void onIconPicked(String icon);
    }

    private static final String PREFS = "icon_picker_prefs";
    private static final String KEY_MIXED = "mixed_search";
    private static final int ICONS_PER_PAGE = 30;

    private static final Map<String, List<String>> CATEGORIES = new LinkedHashMap<>();
    static {
        CATEGORIES.put(Helper.getResString(R.string.auto_ipd_cat_basic), Arrays.asList(
                "ic_mtrl_home", "ic_mtrl_settings", "ic_mtrl_star", "ic_mtrl_done",
                "ic_mtrl_help", "ic_mtrl_info", "ic_mtrl_search", "ic_mtrl_menu",
                "ic_mtrl_category", "ic_mtrl_grid", "ic_mtrl_list", "ic_mtrl_checklist",
                "ic_mtrl_bookmark", "ic_mtrl_label", "ic_mtrl_pin", "ic_mtrl_pin_fill",
                "ic_mtrl_team", "ic_mtrl_group", "ic_mtrl_component", "ic_mtrl_preview"
        ));
        CATEGORIES.put(Helper.getResString(R.string.auto_ipd_cat_actions), Arrays.asList(
                "ic_mtrl_add", "ic_mtrl_add_circle", "ic_mtrl_edit", "ic_mtrl_delete",
                "ic_mtrl_save", "ic_mtrl_save_as", "ic_mtrl_close", "ic_mtrl_check",
                "ic_mtrl_undo", "ic_mtrl_redo", "ic_mtrl_refresh", "ic_mtrl_sync",
                "ic_mtrl_download", "ic_mtrl_upload", "ic_mtrl_export", "ic_mtrl_filter",
                "ic_mtrl_sort", "ic_mtrl_clear_all", "ic_mtrl_cancel", "ic_mtrl_reset",
                "ic_mtrl_done", "ic_mtrl_swap_vertical", "ic_mtrl_more_vertical"
        ));
        CATEGORIES.put(Helper.getResString(R.string.auto_ipd_cat_arrows), Arrays.asList(
                "ic_mtrl_arrow_left", "ic_mtrl_arrow_right", "ic_mtrl_arrow_right2", "ic_mtrl_arrow_up",
                "ic_mtrl_arrow_down", "ic_mtrl_chevron_right_24", "ic_mtrl_next", "ic_mtrl_exit",
                "ic_mtrl_expand", "ic_mtrl_enlarge", "ic_mtrl_pull_down", "ic_mtrl_top",
                "ic_mtrl_swipe_up", "ic_mtrl_swipe_down", "ic_mtrl_swipe_horizontal", "ic_mtrl_swipe_vertical",
                "ic_mtrl_move_x", "ic_mtrl_move_y", "ic_mtrl_rotate", "ic_mtrl_rotate_90"
        ));
        CATEGORIES.put(Helper.getResString(R.string.auto_ipd_cat_media), Arrays.asList(
                "ic_mtrl_image", "ic_mtrl_circle_play", "ic_mtrl_circle_pause", "ic_mtrl_stop",
                "ic_mtrl_volume", "ic_mtrl_video", "ic_mtrl_music", "ic_mtrl_camera",
                "ic_mtrl_screen", "ic_mtrl_screen_play", "ic_mtrl_style", "ic_mtrl_palette",
                "ic_mtrl_pick_color", "ic_mtrl_font", "ic_mtrl_formattext", "ic_mtrl_type",
                "ic_mtrl_speech", "ic_mtrl_tts", "ic_mtrl_stt", "ic_mtrl_youtube"
        ));
        CATEGORIES.put(Helper.getResString(R.string.auto_ipd_cat_connect), Arrays.asList(
                "ic_mtrl_bluetooth", "ic_mtrl_bluetooth_connected", "ic_mtrl_wifi",
                "ic_mtrl_notifications", "ic_mtrl_email_sent", "ic_mtrl_sms_check",
                "ic_mtrl_chat", "ic_mtrl_web", "ic_mtrl_link", "ic_mtrl_link_check",
                "ic_mtrl_signin", "ic_mtrl_login", "ic_mtrl_password",
                "ic_mtrl_shield_check", "ic_mtrl_shield_lock",
                "ic_mtrl_verified_user", "ic_mtrl_fingerprint", "ic_mtrl_profile"
        ));
        CATEGORIES.put(Helper.getResString(R.string.auto_ipd_cat_time), Arrays.asList(
                "ic_mtrl_calendar", "ic_mtrl_calendar_add", "ic_mtrl_calendary_today",
                "ic_mtrl_date_changed", "ic_mtrl_time", "ic_mtrl_timer", "ic_mtrl_clock",
                "ic_mtrl_history", "ic_mtrl_sprint"
        ));
        CATEGORIES.put(Helper.getResString(R.string.auto_ipd_cat_place), Arrays.asList(
                "ic_mtrl_location", "ic_mtrl_location_changed", "ic_mtrl_map", "ic_mtrl_map_ready",
                "ic_mtrl_pin", "ic_mtrl_pin_fill", "ic_mtrl_loc_click", "ic_mtrl_devices"
        ));
        CATEGORIES.put(Helper.getResString(R.string.auto_ipd_cat_data), Arrays.asList(
                "ic_mtrl_database_added", "ic_mtrl_database_edit", "ic_mtrl_database_moved",
                "ic_mtrl_database_off", "ic_mtrl_folder", "ic_mtrl_folder_code",
                "ic_mtrl_file", "ic_mtrl_file_picked", "ic_mtrl_file_present",
                "ic_mtrl_package"
        ));
        CATEGORIES.put(Helper.getResString(R.string.auto_ipd_cat_dev), Arrays.asList(
                "ic_mtrl_code", "ic_mtrl_java", "ic_mtrl_kotlin", "ic_mtrl_terminal",
                "ic_mtrl_bug_report", "ic_mtrl_apk_document", "ic_mtrl_apk_install",
                "ic_mtrl_firebase", "ic_mtrl_firebase_auth", "ic_mtrl_firebase_cloud",
                "ic_mtrl_firebase_rtdb", "ic_mtrl_firebase_storage", "ic_mtrl_firebase_google",
                "ic_mtrl_firebase_sms", "ic_mtrl_firebase_onesignal", "ic_mtrl_firebase_dl",
                "ic_mtrl_material3", "ic_mtrl_regular_expression", "ic_mtrl_deployed_code",
                "ic_mtrl_version_control", "ic_mtrl_inject"
        ));
        CATEGORIES.put(Helper.getResString(R.string.auto_ipd_cat_ui), Arrays.asList(
                "ic_mtrl_design", "ic_mtrl_view_horizontal", "ic_mtrl_view_relative",
                "ic_mtrl_view_vertical", "ic_mtrl_orientation", "ic_mtrl_width", "ic_mtrl_height",
                "ic_mtrl_margin", "ic_mtrl_padding", "ic_mtrl_weight", "ic_mtrl_drag",
                "ic_mtrl_touch", "ic_mtrl_touch_long", "ic_mtrl_click", "ic_mtrl_button_click",
                "ic_mtrl_abc_click", "ic_mtrl_seekbar", "ic_mtrl_progress_bar", "ic_mtrl_spinner",
                "ic_mtrl_switch", "ic_mtrl_checkbox", "ic_mtrl_radio_btn", "ic_mtrl_dialog",
                "ic_mtrl_fab", "ic_mtrl_edittext", "ic_mtrl_sidebar", "ic_mtrl_viewpager",
                "ic_mtrl_scroller", "ic_mtrl_indeterminate", "ic_mtrl_progress",
                "ic_mtrl_progress_check", "ic_mtrl_prog_max", "ic_mtrl_prog_min", "ic_mtrl_tune"
        ));
        CATEGORIES.put(Helper.getResString(R.string.auto_ipd_cat_user), Arrays.asList(
                "ic_mtrl_user_create", "ic_mtrl_user_delete", "ic_mtrl_user_edit",
                "ic_mtrl_user_register_complete", "ic_mtrl_user_remove"
        ));
        CATEGORIES.put(Helper.getResString(R.string.auto_ipd_cat_misc), Arrays.asList(
                "ic_mtrl_key", "ic_mtrl_keyboard", "ic_mtrl_sensor",
                "ic_mtrl_sensors", "ic_mtrl_payment", "ic_mtrl_admob", "ic_mtrl_ad",
                "ic_mtrl_interests", "ic_mtrl_interface", "ic_mtrl_animation",
                "ic_mtrl_bulb", "ic_mtrl_numbers", "ic_mtrl_id", "ic_mtrl_warning",
                "ic_mtrl_gpp_bad", "ic_mtrl_run", "ic_mtrl_lifecycle",
                "ic_mtrl_square", "ic_mtrl_circle", "ic_mtrl_circle_small",
                "ic_mtrl_puzzle", "ic_mtrl_block", "ic_mtrl_moreblock"
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
    private int currentPage = 0;

    /** Кэш цвета иконок (colorOnSurface). */
    private int iconColor = 0;

    public IconPickerDialog(Context context, String currentIcon, Callback callback) {
        this.context = context;
        this.currentIcon = currentIcon;
        this.callback = callback;
        this.selectedCategory = CATEGORIES.keySet().iterator().next();
        this.mixedMode = prefs().getBoolean(KEY_MIXED, false);
        this.iconColor = resolveColorOnSurface();
    }

    private SharedPreferences prefs() {
        return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }

    /** Получить цвет colorOnSurface из темы. Fallback — белый. */
    private int resolveColorOnSurface() {
        try {
            android.util.TypedValue tv = new android.util.TypedValue();
            if (context.getTheme().resolveAttribute(
                    androidx.appcompat.R.attr.colorAccent, tv, true)) {
                if (tv.type >= android.util.TypedValue.TYPE_FIRST_COLOR_INT
                        && tv.type <= android.util.TypedValue.TYPE_LAST_COLOR_INT) {
                    return tv.data;
                }
                if (tv.resourceId != 0) {
                    return androidx.core.content.ContextCompat.getColor(context, tv.resourceId);
                }
            }
        } catch (Exception ignored) {}
        // Fallback — тёмно-серый (виден на светлой теме)
        return 0xFF6750A4;  // Material 3 Primary Purple
    }

    public void show() {
        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(16);
        content.setPadding(pad, pad, pad, pad);

        // Поиск
        EditText search = new EditText(context);
        search.setHint(Helper.getResString(R.string.auto_ipd_hint_search));
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

        final List<TextView> chips = new ArrayList<>();
        for (String cat : CATEGORIES.keySet()) {
            TextView chip = new TextView(context);
            chip.setText(cat);
            chip.setTag(cat);
            chip.setPadding(dp(12), dp(8), dp(12), dp(8));
            chip.setBackgroundResource(R.drawable.single_choice_background);
            chip.setOnClickListener(v -> {
                selectedCategory = cat;
                currentPage = 0;
                updateChips(chips);
                refreshIcons();
            });
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(dp(4), dp(4), dp(4), dp(4));
            chip.setLayoutParams(lp);
            chips.add(chip);
            catRow.addView(chip);
        }
        updateChips(chips);

        catScroll.addView(catRow);
        catScroll.setPadding(0, dp(8), 0, 0);
        content.addView(catScroll);

        // Сетка иконок
        RecyclerView recycler = new RecyclerView(context);
        LinearLayout.LayoutParams rvLp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, dp(420));
        rvLp.topMargin = dp(8);
        recycler.setLayoutParams(rvLp);
        recycler.setLayoutManager(new GridLayoutManager(context, 4));
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
        listAllCb.setText(Helper.getResString(R.string.auto_ipd_list_all));
        listAllCb.setChecked(listAll);
        listAllCb.setOnCheckedChangeListener((btn, checked) -> {
            listAll = checked;
            currentPage = 0;
            refreshIcons();
        });
        content.addView(listAllCb);

        // Поиск
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
                .setTitle(Helper.getResString(R.string.auto_ipd_title))
                .setView(content)
                .setPositiveButton(Helper.getResString(R.string.auto_ipd_ok), (d, w) -> {})
                .setNegativeButton(Helper.getResString(R.string.auto_ipd_cancel), null)
                .show();

        refreshIcons();
    }

    private void updateChips(List<TextView> chips) {
        for (TextView c : chips) {
            String cat = (String) c.getTag();
            c.setSelected(cat.equals(selectedCategory));
            c.setAlpha(cat.equals(selectedCategory) ? 1f : 0.6f);
        }
    }

    private boolean iconExists(String name) {
        return context.getResources().getIdentifier(name, "drawable", context.getPackageName()) != 0;
    }

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
        previewView.setImageTintList(ColorStateList.valueOf(iconColor));

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
            RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(dp(64), dp(64));
            lp.setMargins(dp(4), dp(4), dp(4), dp(4));
            iv.setLayoutParams(lp);
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv.setPadding(dp(6), dp(6), dp(6), dp(6));
            iv.setBackgroundResource(R.drawable.single_choice_background);
            // КРИТИЧНО: явный tint через ImageTintList
            iv.setImageTintList(ColorStateList.valueOf(iconColor));
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
