package pro.sketchware.settings;


import mod.hey.studios.util.Helper;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.materialswitch.MaterialSwitch;

import pro.sketchware.R;

/**
 * Экран «Настройки дизайнера».
 *
 * Все опции Дизайнера в одном месте: поведение вкладок, размер иконок,
 * ширина палитры, поведение виджетов, быстрые действия, поиск.
 */
public class DesignerSettingsActivity extends BaseAppCompatActivity {

    private LinearLayout container;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Однократная миграция дефолтов (аккордеон=true, стартовое=first_expanded).
        DesignerSettingsStore.migrateIfNeeded(this);

        setContentView(R.layout.activity_designer_settings);

        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        container = findViewById(R.id.settings_container);

        buildTabsSection();
        buildAppearanceSection();
        buildWidgetBehaviorSection();
        buildPaletteGesturesSection();
        buildQuickActionsSection();
    }

    // ─────────────────────────────────────────────────────────────
    // Секция 1: Поведение вкладок
    // ─────────────────────────────────────────────────────────────
    private void buildTabsSection() {
        addHeader(Helper.getResString(R.string.auto_designer_settings_activity_001));

        addSwitch(Helper.getResString(R.string.auto_designer_settings_activity_002),
                Helper.getResString(R.string.auto_designer_settings_activity_003),
                DesignerSettingsStore.isAccordion(this),
                v -> DesignerSettingsStore.setAccordion(this, v));

        addSwitch(Helper.getResString(R.string.auto_designer_settings_activity_004),
                Helper.getResString(R.string.auto_designer_settings_activity_005),
                DesignerSettingsStore.isAnimation(this),
                v -> DesignerSettingsStore.setAnimation(this, v));

        addSwitch(Helper.getResString(R.string.auto_designer_settings_activity_006),
                Helper.getResString(R.string.auto_designer_settings_activity_007),
                DesignerSettingsStore.isShowCount(this),
                v -> DesignerSettingsStore.setShowCount(this, v));

        addChoice(Helper.getResString(R.string.auto_designer_settings_activity_008),
                Helper.getResString(R.string.auto_designer_settings_activity_009),
                DesignerSettingsStore.getTabsStartState(this),
                new String[]{"expanded", "collapsed", "first_expanded"},
                new String[]{Helper.getResString(R.string.auto_designer_settings_activity_010), Helper.getResString(R.string.auto_designer_settings_activity_011), Helper.getResString(R.string.auto_designer_settings_activity_012)},
                v -> DesignerSettingsStore.setTabsStartState(this, v));
    }

    private void buildAppearanceSection() {
        addHeader(Helper.getResString(R.string.auto_designer_settings_activity_013));

        addChoice(Helper.getResString(R.string.auto_designer_settings_activity_014),
                Helper.getResString(R.string.auto_designer_settings_activity_015),
                DesignerSettingsStore.getIconSize(this),
                new String[]{"compact", "normal", "large"},
                new String[]{Helper.getResString(R.string.auto_designer_settings_activity_016), Helper.getResString(R.string.auto_designer_settings_activity_017), Helper.getResString(R.string.auto_designer_settings_activity_018)},
                v -> DesignerSettingsStore.setIconSize(this, v));

        addChoice(Helper.getResString(R.string.auto_designer_settings_activity_019),
                Helper.getResString(R.string.auto_designer_settings_activity_020),
                DesignerSettingsStore.getTextSize(this),
                new String[]{"small", "normal", "large"},
                new String[]{Helper.getResString(R.string.auto_designer_settings_activity_021), Helper.getResString(R.string.auto_designer_settings_activity_022), Helper.getResString(R.string.auto_designer_settings_activity_023)},
                v -> DesignerSettingsStore.setTextSize(this, v));

        addChoice(Helper.getResString(R.string.auto_designer_settings_activity_024),
                Helper.getResString(R.string.auto_designer_settings_activity_025),
                DesignerSettingsStore.getPaletteWidth(this),
                new String[]{"narrow", "normal", "wide"},
                new String[]{Helper.getResString(R.string.auto_designer_settings_activity_026), Helper.getResString(R.string.auto_designer_settings_activity_027), Helper.getResString(R.string.auto_designer_settings_activity_028)},
                v -> DesignerSettingsStore.setPaletteWidth(this, v));

        addSwitch(Helper.getResString(R.string.auto_designer_settings_activity_029),
                Helper.getResString(R.string.auto_designer_settings_activity_030),
                DesignerSettingsStore.isShowLabels(this),
                v -> DesignerSettingsStore.setShowLabels(this, v));
    }

    private void buildWidgetBehaviorSection() {
        addHeader(Helper.getResString(R.string.auto_designer_settings_activity_031));

        addChoice(Helper.getResString(R.string.auto_designer_settings_activity_032),
                Helper.getResString(R.string.auto_designer_settings_activity_033),
                DesignerSettingsStore.getDoubleTapAction(this),
                new String[]{"properties", "none"},
                new String[]{Helper.getResString(R.string.auto_designer_settings_activity_034), Helper.getResString(R.string.auto_designer_settings_activity_035)},
                v -> DesignerSettingsStore.setDoubleTapAction(this, v));

        addChoice(Helper.getResString(R.string.auto_designer_settings_activity_036),
                Helper.getResString(R.string.auto_designer_settings_activity_037),
                DesignerSettingsStore.getLongTapAction(this),
                new String[]{"properties", "none"},
                new String[]{Helper.getResString(R.string.auto_designer_settings_activity_038), Helper.getResString(R.string.auto_designer_settings_activity_039)},
                v -> DesignerSettingsStore.setLongTapAction(this, v));

        addSwitch(Helper.getResString(R.string.auto_designer_settings_activity_040),
                Helper.getResString(R.string.auto_designer_settings_activity_041),
                DesignerSettingsStore.isTooltips(this),
                v -> DesignerSettingsStore.setTooltips(this, v));
    }

    private void buildPaletteGesturesSection() {
        addHeader(Helper.getResString(R.string.auto_designer_settings_activity_042));

        String[] ids = {
                "nothing", "drag", "add_center", "add_corner",
                "tooltip", "designer_settings", "help", "info"
        };
        String[] labels = {
                Helper.getResString(R.string.auto_designer_settings_activity_043),
                Helper.getResString(R.string.auto_designer_settings_activity_044),
                Helper.getResString(R.string.auto_designer_settings_activity_045),
                Helper.getResString(R.string.auto_designer_settings_activity_046),
                Helper.getResString(R.string.auto_designer_settings_activity_047),
                Helper.getResString(R.string.auto_designer_settings_activity_048),
                Helper.getResString(R.string.auto_designer_settings_activity_049),
                Helper.getResString(R.string.auto_designer_settings_activity_050)
        };

        addChoice(Helper.getResString(R.string.auto_designer_settings_activity_051),
                Helper.getResString(R.string.auto_designer_settings_activity_052),
                DesignerSettingsStore.getPaletteSingleTapAction(this),
                ids, labels,
                v -> DesignerSettingsStore.setPaletteSingleTapAction(this, v));

        addChoice(Helper.getResString(R.string.auto_designer_settings_activity_053),
                Helper.getResString(R.string.auto_designer_settings_activity_054),
                DesignerSettingsStore.getPaletteDoubleTapAction(this),
                ids, labels,
                v -> DesignerSettingsStore.setPaletteDoubleTapAction(this, v));

        addChoice(Helper.getResString(R.string.auto_designer_settings_activity_055),
                Helper.getResString(R.string.auto_designer_settings_activity_056),
                DesignerSettingsStore.getPaletteLongTapAction(this),
                ids, labels,
                v -> DesignerSettingsStore.setPaletteLongTapAction(this, v));
    }

    private void buildQuickActionsSection() {
        addHeader(Helper.getResString(R.string.auto_designer_settings_activity_057));

        addSwitch(Helper.getResString(R.string.auto_designer_settings_activity_058),
                Helper.getResString(R.string.auto_designer_settings_activity_059),
                DesignerSettingsStore.isShowCollapseAll(this),
                v -> DesignerSettingsStore.setShowCollapseAll(this, v));

        addSwitch(Helper.getResString(R.string.auto_designer_settings_activity_060),
                Helper.getResString(R.string.auto_designer_settings_activity_061),
                DesignerSettingsStore.isShowSearch(this),
                v -> DesignerSettingsStore.setShowSearch(this, v));

        addSwitch(Helper.getResString(R.string.auto_designer_settings_activity_062),
                Helper.getResString(R.string.auto_designer_settings_activity_063),
                DesignerSettingsStore.isUseLegacySaveDialog(this),
                v -> DesignerSettingsStore.setUseLegacySaveDialog(this, v));
    }

    // ─────────────────────────────────────────────────────────────
    // Вспомогательные методы для построения UI
    // ─────────────────────────────────────────────────────────────

    private void addHeader(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextSize(13);
        tv.setPadding(dp(16), dp(16), dp(16), dp(6));
        tv.setAllCaps(false);
        tv.setTextColor(com.google.android.material.color.MaterialColors.getColor(
                tv, pro.sketchware.R.attr.colorPrimary));
        container.addView(tv);
    }

    private void addPlaceholder() {
        TextView tv = new TextView(this);
        tv.setText(Helper.getResString(R.string.auto_designer_settings_activity_064));
        tv.setTextSize(12);
        tv.setPadding(dp(16), dp(8), dp(16), dp(8));
        tv.setAlpha(0.5f);
        container.addView(tv);
    }

    private interface OnBoolChanged {
        void onChanged(boolean value);
    }

    private interface OnStringChanged {
        void onChanged(String value);
    }

    private void addSwitch(String title, String subtitle, boolean initial, OnBoolChanged listener) {
        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setPadding(dp(16), dp(10), dp(16), dp(10));
        row.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout textBox = new LinearLayout(this);
        textBox.setOrientation(LinearLayout.VERTICAL);
        textBox.setLayoutParams(new LinearLayout.LayoutParams(
                0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f));

        TextView tvTitle = new TextView(this);
        tvTitle.setText(title);
        tvTitle.setTextSize(15);
        textBox.addView(tvTitle);

        if (subtitle != null) {
            TextView tvSub = new TextView(this);
            tvSub.setText(subtitle);
            tvSub.setTextSize(12);
            tvSub.setAlpha(0.7f);
            tvSub.setPadding(0, dp(2), 0, 0);
            textBox.addView(tvSub);
        }

        row.addView(textBox);

        MaterialSwitch sw = new MaterialSwitch(this);
        sw.setChecked(initial);
        sw.setOnCheckedChangeListener((v, checked) -> listener.onChanged(checked));
        row.addView(sw);

        container.addView(row);
    }

    private void addChoice(String title, String subtitle, String currentKey,
                           String[] keys, String[] labels, OnStringChanged listener) {

        LinearLayout row = new LinearLayout(this);
        row.setOrientation(LinearLayout.VERTICAL);
        row.setPadding(dp(16), dp(10), dp(16), dp(10));
        row.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));

        TextView tvTitle = new TextView(this);
        tvTitle.setText(title);
        tvTitle.setTextSize(15);
        row.addView(tvTitle);

        if (subtitle != null) {
            TextView tvSub = new TextView(this);
            tvSub.setText(subtitle);
            tvSub.setTextSize(12);
            tvSub.setAlpha(0.7f);
            tvSub.setPadding(0, dp(2), 0, dp(4));
            row.addView(tvSub);
        }

        TextView tvValue = new TextView(this);
        tvValue.setTextSize(14);
        String currentLabel = labels[0];
        for (int i = 0; i < keys.length; i++) {
            if (keys[i].equals(currentKey)) { currentLabel = labels[i]; break; }
        }
        tvValue.setText(currentLabel);
        tvValue.setPadding(0, dp(4), 0, 0);
        row.addView(tvValue);

        row.setOnClickListener(v -> {
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(this)
                    .setTitle(title)
                    .setSingleChoiceItems(labels, indexOf(keys, currentKey), (dialog, which) -> {
                        listener.onChanged(keys[which]);
                        tvValue.setText(labels[which]);
                        dialog.dismiss();
                    })
                    .setNegativeButton(Helper.getResString(R.string.auto_designer_settings_activity_065), null)
                    .show();
        });

        container.addView(row);
    }

    private int indexOf(String[] arr, String val) {
        for (int i = 0; i < arr.length; i++) if (arr[i].equals(val)) return i;
        return 0;
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }
}
