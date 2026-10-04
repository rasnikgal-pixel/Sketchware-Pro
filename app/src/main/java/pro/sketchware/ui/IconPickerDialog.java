package pro.sketchware.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pro.sketchware.R;

/**
 * Диалог выбора иконки экрана с категориями, поиском, листанием.
 */
public class IconPickerDialog {

    public interface Callback {
        void onIconPicked(String icon);
    }

    private static final String PREFS = "icon_picker_prefs";
    private static final String KEY_MIXED = "mixed_search";
    private static final int ICONS_PER_PAGE = 20;

    /** Категории иконок. */
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
                "ic_mtrl_refresh", "ic_mtrl_share", "ic_mtrl_download", "ic_mtrl_upload",
                "ic_mtrl_filter", "ic_mtrl_sort", "ic_mtrl_more", "ic_mtrl_copy"
        ));
        CATEGORIES.put("Стрелки", Arrays.asList(
                "ic_mtrl_arrow_left", "ic_mtrl_arrow_right", "ic_mtrl_arrow_up", "ic_mtrl_arrow_down",
                "ic_mtrl_arrow_back", "ic_mtrl_arrow_forward", "ic_mtrl_chevron_left", "ic_mtrl_chevron_right",
                "ic_mtrl_expand_more", "ic_mtrl_expand_less", "ic_mtrl_close_full", "ic_mtrl_menu_open",
                "ic_mtrl_arrow_drop_down", "ic_mtrl_arrow_drop_up"
        ));
        CATEGORIES.put("Медиа", Arrays.asList(
                "ic_mtrl_image", "ic_mtrl_play", "ic_mtrl_pause", "ic_mtrl_stop",
                "ic_mtrl_volume", "ic_mtrl_music_note", "ic_mtrl_movie", "ic_mtrl_photo",
                "ic_mtrl_videocam", "ic_mtrl_mic", "ic_mtrl_camera", "ic_mtrl_headphones",
                "ic_mtrl_speaker", "ic_mtrl_equalizer", "ic_mtrl_slideshow", "ic_mtrl_library_music"
        ));
        CATEGORIES.put("Связь", Arrays.asList(
                "ic_mtrl_call", "ic_mtrl_email", "ic_mtrl_message", "ic_mtrl_chat",
                "ic_mtrl_wifi", "ic_mtrl_bluetooth", "ic_mtrl_cloud", "ic_mtrl_notifications",
                "ic_mtrl_sync", "ic_mtrl_share_link", "ic_mtrl_public", "ic_mtrl_phone_android",
                "ic_mtrl_contact_mail", "ic_mtrl_contact_phone", "ic_mtrl_alternate_email", "ic_mtrl_rss_feed"
        ));
        CATEGORIES.put("Транспорт", Arrays.asList(
                "ic_mtrl_directions_car", "ic_mtrl_flight", "ic_mtrl_train", "ic_mtrl_directions_bike",
                "ic_mtrl_directions_bus", "ic_mtrl_local_taxi", "ic_mtrl_two_wheeler", "ic_mtrl_local_shipping",
                "ic_mtrl_directions_walk", "ic_mtrl_directions_boat", "ic_mtrl_local_gas_station", "ic_mtrl_traffic",
                "ic_mtrl_map", "ic_mtrl_navigation", "ic_mtrl_location", "ic_mtrl_my_location"
        ));
        CATEGORIES.put("Еда", Arrays.asList(
                "ic_mtrl_restaurant", "ic_mtrl_local_cafe", "ic_mtrl_local_pizza", "ic_mtrl_local_bar",
                "ic_mtrl_fastfood", "ic_mtrl_bakery_dining", "ic_mtrl_icecream", "ic_mtrl_ramen_dining",
                "ic_mtrl_lunch_dining", "ic_mtrl_dinner_dining", "ic_mtrl_breakfast_dining", "ic_mtrl_brunch_dining",
                "ic_mtrl_liquor", "ic_mtrl_kitchen", "ic_mtrl_local_grocery_store", "ic_mtrl_wine_bar"
        ));
        CATEGORIES.put("Здоровье", Arrays.asList(
                "ic_mtrl_fitness_center", "ic_mtrl_favorite_border", "ic_mtrl_healing", "ic_mtrl_local_hospital",
                "ic_mtrl_medical_services", "ic_mtrl_medication", "ic_mtrl_monitor_heart", "ic_mtrl_psychology_alt",
                "ic_mtrl_spa", "ic_mtrl_self_improvement", "ic_mtrl_hiking", "ic_mtrl_directions_run",
                "ic_mtrl_sports_soccer", "ic_mtrl_sports_basketball", "ic_mtrl_sports_tennis", "ic_mtrl_pool"
        ));
        CATEGORIES.put("Работа", Arrays.asList(
                "ic_mtrl_work", "ic_mtrl_business", "ic_mtrl_school", "ic_mtrl_book",
                "ic_mtrl_menu_book", "ic_mtrl_library_books", "ic_mtrl_edit_note", "ic_mtrl_assignment",
                "ic_mtrl_description", "ic_mtrl_folder", "ic_mtrl_attach_file", "ic_mtrl_print",
                "ic_mtrl_meeting_room", "ic_mtrl_event", "ic_mtrl_schedule", "ic_mtrl_timer"
        ));
        CATEGORIES.put("Развлечения", Arrays.asList(
                "ic_mtrl_celebration", "ic_mtrl_card_giftcard", "ic_mtrl_emoji_events", "ic_mtrl_military_tech",
                "ic_mtrl_attractions", "ic_mtrl_local_activity", "ic_mtrl_nightlife", "ic_mtrl_theater_comedy",
                "ic_mtrl_sports_esports", "ic_mtrl_casino", "ic_mtrl_music_note_alt", "ic_mtrl_movie_filter",
                "ic_mtrl_videogame_asset", "ic_mtrl_palette", "ic_mtrl_brush", "ic_mtrl_color_lens"
        ));
        CATEGORIES.put("Магазин", Arrays.asList(
                "ic_mtrl_shopping_cart", "ic_mtrl_shopping_bag", "ic_mtrl_attach_money", "ic_mtrl_credit_card",
                "ic_mtrl_payments", "ic_mtrl_account_balance", "ic_mtrl_receipt", "ic_mtrl_storefront",
                "ic_mtrl_local_offer", "ic_mtrl_redeem", "ic_mtrl_sell", "ic_mtrl_currency_exchange",
                "ic_mtrl_price_check", "ic_mtrl_price_change", "ic_mtrl_discount", "ic_mtrl_savings"
        ));
        CATEGORIES.put("Разное", Arrays.asList(
                "ic_mtrl_image_default", "ic_mtrl_pets", "ic_mtrl_cake", "ic_mtrl_local_florist",
                "ic_mtrl_wb_sunny", "ic_mtrl_ac_unit", "ic_mtrl_bolt", "ic_mtrl_eco",
                "ic_mtrl_beach_access", "ic_mtrl_terrain", "ic_mtrl_water_drop", "ic_mtrl_forest",
                "ic_mtrl_stars", "ic_mtrl_auto_awesome", "ic_mtrl_diamond", "ic_mtrl_local_fire_department"
        ));
    }

    private final Context context;
    private final String currentIcon;
    private final Callback callback;

    private String selectedCategory;
    private int currentPage = 0;
    private boolean mixedMode;
    private boolean listAll;
    private String searchQuery = "";

    private ImageView previewView;
    private GridLayout gridView;
    private TextView pageInfo;
    private Button prevBtn, nextBtn;

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
        ScrollView root = new ScrollView(context);
        LinearLayout content = new LinearLayout(context);
        content.setOrientation(LinearLayout.VERTICAL);
        int pad = dp(16);
        content.setPadding(pad, pad, pad, pad);

        // ─── 1. Поиск
        EditText search = new EditText(context);
        search.setHint("Поиск...");
        search.setSingleLine(true);
        content.addView(search);

        // ─── 2. Превью
        previewView = new ImageView(context);
        LinearLayout.LayoutParams previewLp = new LinearLayout.LayoutParams(dp(64), dp(64));
        previewLp.gravity = Gravity.CENTER_HORIZONTAL;
        previewLp.topMargin = dp(12);
        previewView.setLayoutParams(previewLp);
        setPreview(currentIcon);
        content.addView(previewView);

        // ─── 3. Категории (горизонтальный скролл с чипами)
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
                renderGrid();
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

        // ─── 4. Сетка иконок
        gridView = new GridLayout(context);
        gridView.setColumnCount(5);
        gridView.setPadding(0, dp(8), 0, dp(8));
        content.addView(gridView);

        // ─── 5. Кнопки листания + инфо
        LinearLayout navRow = new LinearLayout(context);
        navRow.setOrientation(LinearLayout.HORIZONTAL);
        navRow.setGravity(Gravity.CENTER_VERTICAL);

        prevBtn = new Button(context);
        prevBtn.setText("←");
        prevBtn.setOnClickListener(v -> {
            if (currentPage > 0) {
                currentPage--;
                renderGrid();
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
            currentPage++;
            renderGrid();
        });

        navRow.addView(prevBtn);
        navRow.addView(pageInfo);
        navRow.addView(nextBtn);
        content.addView(navRow);

        // ─── 6. Чекбокс "Листать все иконки"
        CheckBox listAllCb = new CheckBox(context);
        listAllCb.setText("Листать все иконки");
        listAllCb.setChecked(listAll);
        listAllCb.setOnCheckedChangeListener((btn, checked) -> {
            listAll = checked;
            currentPage = 0;
            renderGrid();
        });
        content.addView(listAllCb);

        // ─── 7. Слушатель поиска
        search.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
            @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
            @Override public void afterTextChanged(android.text.Editable s) {
                searchQuery = s.toString().trim().toLowerCase();
                currentPage = 0;
                renderGrid();
            }
        });

        root.addView(content);

        new AlertDialog.Builder(context)
                .setTitle("Выберите иконку")
                .setView(root)
                .setPositiveButton("ОК", (d, w) -> {})
                .setNegativeButton("Отмена", null)
                .show();

        renderGrid();
    }

    /** Возвращает список иконок по текущему состоянию. */
    private List<String> getCurrentIcons() {
        // Если есть поиск
        if (!searchQuery.isEmpty()) {
            List<String> result = new ArrayList<>();
            if (mixedMode || listAll) {
                // Ищем по всем
                for (List<String> list : CATEGORIES.values()) {
                    for (String icon : list) {
                        if (icon.toLowerCase().contains(searchQuery)) result.add(icon);
                    }
                }
            } else {
                // Ищем только в текущей категории
                List<String> list = CATEGORIES.get(selectedCategory);
                if (list != null) {
                    for (String icon : list) {
                        if (icon.toLowerCase().contains(searchQuery)) result.add(icon);
                    }
                }
            }
            return result;
        }

        // Без поиска — по категории или все
        if (listAll) {
            List<String> all = new ArrayList<>();
            for (List<String> list : CATEGORIES.values()) all.addAll(list);
            return all;
        }
        List<String> list = CATEGORIES.get(selectedCategory);
        return list != null ? list : new ArrayList<>();
    }

    private void renderGrid() {
        if (gridView == null) return;
        gridView.removeAllViews();

        List<String> icons = getCurrentIcons();
        int totalPages = Math.max(1, (icons.size() + ICONS_PER_PAGE - 1) / ICONS_PER_PAGE);
        if (currentPage >= totalPages) currentPage = totalPages - 1;
        if (currentPage < 0) currentPage = 0;

        int from = currentPage * ICONS_PER_PAGE;
        int to = Math.min(from + ICONS_PER_PAGE, icons.size());

        for (int i = from; i < to; i++) {
            String name = icons.get(i);
            ImageView iv = new ImageView(context);
            GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
            lp.width = dp(48);
            lp.height = dp(48);
            lp.setMargins(dp(4), dp(4), dp(4), dp(4));
            iv.setLayoutParams(lp);
            iv.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            iv.setPadding(dp(4), dp(4), dp(4), dp(4));
            iv.setBackgroundResource(R.drawable.single_choice_background);

            int resId = context.getResources().getIdentifier(name, "drawable", context.getPackageName());
            if (resId != 0) iv.setImageResource(resId);
            else iv.setImageResource(R.drawable.ic_mtrl_image);

            iv.setOnClickListener(v -> {
                String value = "@drawable/" + name;
                setPreview(value);
                callback.onIconPicked(value);
            });

            gridView.addView(iv);
        }

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
}
