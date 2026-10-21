package com.besome.sketch.editor.logic;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.util.AttributeSet;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;

import a.a.a.Vs;
import mod.hey.studios.util.Helper;
import pro.sketchware.R;
import pro.sketchware.databinding.PalettesSearchDialogBinding;

public class PaletteSelector extends RecyclerView {

    private final Context context;

    private final String[] MainCategoriesNames = {
            Helper.getResString(R.string.auto_palette_selector_001),
            Helper.getResString(R.string.auto_palette_selector_002),
            Helper.getResString(R.string.palette_var),
            Helper.getResString(R.string.palette_list),
            Helper.getResString(R.string.palette_control),
            Helper.getResString(R.string.palette_operator),
            Helper.getResString(R.string.palette_math),
            Helper.getResString(R.string.palette_file),
            Helper.getResString(R.string.palette_view_func),
            Helper.getResString(R.string.palette_component_func),
            Helper.getResString(R.string.xml_strings),
            Helper.getResString(R.string.palette_moreblock),
            Helper.getResString(R.string.block_category_app_control),
            Helper.getResString(R.string.block_category_currency),
            Helper.getResString(R.string.block_category_science)
    };

    private final int[] MainCategoriesColors = {
            mod.jbk.util.BlockTemplatesManager.TEMPLATES_PALETTE_COLOR,
            0xffd4af37,
            0xffee7d16, 0xffcc5b22, 0xffe1a92a,
            0xff5cb722, 0xff23b9a9, 0xffa1887f,
            0xff4a6cd4, 0xff2ca5e2, 0xff7c83db,
            0xff8a55d7, 0xffee7d16, 0xff2e7d32, 0xff6a1b9a
    };

    private final int[] MainCategoriesIds = {
            mod.jbk.util.BlockTemplatesManager.TEMPLATES_PALETTE_ID,
            mod.jbk.util.FavoriteBlocksManager.FAVORITE_PALETTE_ID,
            0, 1, 2, 3, 4, 5, 6, 7, -1, 8, 9, 10, 11
    };

    // ===== New logical palettes (200+) — used when SETTING_USE_NEW_PALETTES is enabled =====
    private final String[] NewMainCategoriesNames = {
            Helper.getResString(R.string.auto_palette_selector_001),
            Helper.getResString(R.string.auto_palette_selector_002),
            Helper.getResString(R.string.palette_new_electricity),
            Helper.getResString(R.string.palette_new_mechanics),
            Helper.getResString(R.string.palette_new_chemistry),
            Helper.getResString(R.string.palette_new_accounting),
            Helper.getResString(R.string.palette_new_text),
            Helper.getResString(R.string.palette_new_lists),
            Helper.getResString(R.string.palette_new_files),
            Helper.getResString(R.string.palette_new_network),
            Helper.getResString(R.string.palette_new_sqlite),
            Helper.getResString(R.string.palette_new_color),
            Helper.getResString(R.string.palette_new_ui),
            Helper.getResString(R.string.palette_new_animations),
            Helper.getResString(R.string.palette_new_datetime),
            Helper.getResString(R.string.palette_new_device),
            Helper.getResString(R.string.palette_new_sensors),
            Helper.getResString(R.string.palette_new_location),
            Helper.getResString(R.string.palette_new_bluetooth),
            Helper.getResString(R.string.palette_new_sound),
            Helper.getResString(R.string.palette_new_vibration),
            Helper.getResString(R.string.palette_new_flashlight),
            Helper.getResString(R.string.palette_new_camera),
            Helper.getResString(R.string.palette_new_video),
            Helper.getResString(R.string.palette_new_images),
            Helper.getResString(R.string.palette_new_archives),
            Helper.getResString(R.string.palette_new_crypto),
            Helper.getResString(R.string.palette_new_random),
            Helper.getResString(R.string.palette_new_contacts),
            Helper.getResString(R.string.palette_new_validation),
            Helper.getResString(R.string.palette_new_notifications),
            Helper.getResString(R.string.palette_new_intents),
            Helper.getResString(R.string.palette_new_regex),
            Helper.getResString(R.string.palette_new_xml_json),
            Helper.getResString(R.string.palette_new_math),
            Helper.getResString(R.string.palette_new_currencies),
    };

    private final int[] NewMainCategoriesColors = {
            mod.jbk.util.BlockTemplatesManager.TEMPLATES_PALETTE_COLOR,
            0xffd4af37,
            0xff1565c0, 0xff1976d2, 0xff2e7d32, 0xfff9a825, 0xff6a1b9a, 0xff00838f,
            0xff5d4037, 0xff0277bd, 0xff37474f, 0xffad1457, 0xff4527a0, 0xffc62828,
            0xffef6c00, 0xff00695c, 0xff558b2f, 0xff283593, 0xff00838f, 0xff6a1b9a,
            0xff4e342e, 0xfff57f17, 0xff37474f, 0xffb71c1c, 0xffad1457, 0xff5d4037,
            0xff1b5e20, 0xff6a1b9a, 0xff00897b, 0xff455a64, 0xffef6c00, 0xff2ca5e2,
            0xff7b1fa2, 0xff455a64, 0xff4a6cd4, 0xff2e7d32
    };

    private final int[] NewMainCategoriesIds = {
            mod.jbk.util.BlockTemplatesManager.TEMPLATES_PALETTE_ID,
            mod.jbk.util.FavoriteBlocksManager.FAVORITE_PALETTE_ID,
            200, 201, 202, 203, 204, 205, 206, 207, 208, 209, 210, 211,
            212, 213, 214, 215, 216, 217, 218, 219, 220, 221, 222, 223,
            224, 225, 226, 227, 228, 229, 230, 231, 232, 233
    };

    private String searchValue = "";
    private PaletteSelectorAdapter paletteAdapter;
    private List<paletteSelectorRecord> allPalettes;

    public PaletteSelector(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    private void initialize(Vs onBlockCategorySelectListener) {
        paletteAdapter = new PaletteSelectorAdapter(this, onBlockCategorySelectListener);
        setAdapter(paletteAdapter);

        Executors.newSingleThreadExecutor().execute(() ->
                new Handler(Looper.getMainLooper()).post(this::initializePalettes)
        );
    }

    private void initializePalettes() {
        allPalettes = new ArrayList<>();

        boolean hideTemplates = !mod.hilal.saif.activities.tools.ConfigActivity.isSettingEnabled(
                mod.hilal.saif.activities.tools.ConfigActivity.SETTING_BLOCK_TEMPLATES);

        boolean useNew = mod.hilal.saif.activities.tools.ConfigActivity.isSettingEnabled(
                mod.hilal.saif.activities.tools.ConfigActivity.SETTING_USE_NEW_PALETTES);
        String[] paletteNames = useNew ? NewMainCategoriesNames : MainCategoriesNames;
        int[] paletteColors = useNew ? NewMainCategoriesColors : MainCategoriesColors;
        int[] paletteIds = useNew ? NewMainCategoriesIds : MainCategoriesIds;

        int paletteCount = Math.min(paletteNames.length, Math.min(paletteIds.length, paletteColors.length));
        for (int i = 0; i < paletteCount; i++) {
            if (hideTemplates
                    && paletteIds[i] == mod.jbk.util.BlockTemplatesManager.TEMPLATES_PALETTE_ID) {
                continue;
            }

            allPalettes.add(new paletteSelectorRecord(paletteIds[i], paletteNames[i], paletteColors[i]));
        }

        new mod.agus.jcoderz.editor.manage.block.palette.PaletteSelector()
                .getPaletteSelector()
                .forEach(this::addDynamicPalette);

        paletteAdapter.setPalettes(allPalettes);

        if (paletteAdapter.getItemCount() > 0) {
            paletteAdapter.selectPosition(0);
        }
    }

    public void scrollToTop() {
        post(() -> scrollToPosition(0));
    }

    public void performClickPalette(int tag) {
        paletteAdapter.selectPaletteById(tag);
        if (!mod.hilal.saif.activities.tools.ConfigActivity.isSettingEnabled(
                mod.hilal.saif.activities.tools.ConfigActivity.SETTING_REMEMBER_PALETTE_POSITION)) {
            postDelayed(() -> scrollToPosition(0), 100);
        }
    }

    private void addDynamicPalette(HashMap<String, Object> paletteMap) {
        String text = Objects.requireNonNull(paletteMap.get("text")).toString();
        if (matchesSearch(text)) {
            int index = Integer.parseInt(Objects.requireNonNull(paletteMap.get("index")).toString());
            int color = Integer.parseInt(Objects.requireNonNull(paletteMap.get("color")).toString());
            allPalettes.add(new paletteSelectorRecord(index, text, color));
        }
    }

    public boolean matchesSearch(String title) {
        return searchValue.isEmpty() || title.toLowerCase().contains(searchValue.toLowerCase());
    }

    public void showSearchDialog() {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(context);
        PalettesSearchDialogBinding binding = PalettesSearchDialogBinding.inflate(((Activity) context).getLayoutInflater());

        dialog.setTitle(Helper.getResString(R.string.search_in_palettes_dialog_title));
        dialog.setPositiveButton(Helper.getResString(R.string.search), (v1, which) -> {
            if (binding.textInputLayoutSearch.getError() == null) {
                startSearch(v1, Helper.getText(binding.edittextSearchValue).trim());
            }
            v1.dismiss();
        });
        dialog.setNegativeButton(Helper.getResString(R.string.cancel), (v1, which) -> v1.dismiss());
        if (!searchValue.isEmpty()) {
            dialog.setNeutralButton(Helper.getResString(R.string.restore), (v1, which) -> startSearch(v1, ""));
        }
        dialog.setView(binding.getRoot());
        binding.edittextSearchValue.setText(searchValue);
        binding.edittextSearchValue.addTextChangedListener(new SimpleTextWatcher(s -> validateSearch(s.toString(), binding.textInputLayoutSearch)));
        dialog.show();
    }

    private boolean canSearch(String query) {
        String trimmedQuery = query.trim().toLowerCase();
        return allPalettes.stream().anyMatch(palette -> {
            String title = palette.text().toLowerCase();
            return title.contains(trimmedQuery);
        });
    }

    private void validateSearch(String query, TextInputLayout layout) {
        layout.setError(canSearch(query) ? null : Helper.getResString(R.string.search_error_message));
    }

    private void startSearch(DialogInterface dialog, String query) {
        searchValue = query;
        Executors.newSingleThreadExecutor().execute(() ->
                new Handler(Looper.getMainLooper()).post(() -> {
                    initializePalettes();
                    dialog.dismiss();
                })
        );
    }

    public void setOnBlockCategorySelectListener(Vs listener) {
        initialize(listener);
    }

    public record paletteSelectorRecord(int index, String text, int color) {
    }

    public record SimpleTextWatcher(
            java.util.function.Consumer<CharSequence> onTextChanged) implements android.text.TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void afterTextChanged(Editable s) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            onTextChanged.accept(s);
        }
    }
}
