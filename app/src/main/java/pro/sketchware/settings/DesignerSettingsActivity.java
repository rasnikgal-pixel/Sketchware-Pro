package pro.sketchware.settings;

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
        buildQuickActionsSection();
    }

    // ─────────────────────────────────────────────────────────────
    // Секция 1: Поведение вкладок
    // ─────────────────────────────────────────────────────────────
    private void buildTabsSection() {
        addHeader("Поведение вкладок");

        addSwitch("Аккордеон",
                "Открытие одной категории сворачивает все остальные",
                DesignerSettingsStore.isAccordion(this),
                v -> DesignerSettingsStore.setAccordion(this, v));

        addSwitch("Анимация",
                "Плавное сворачивание / разворачивание категорий",
                DesignerSettingsStore.isAnimation(this),
                v -> DesignerSettingsStore.setAnimation(this, v));

        addSwitch("Показывать счётчик виджетов",
                "В заголовке категории показывать количество виджетов (например, «Виджеты (12)»)",
                DesignerSettingsStore.isShowCount(this),
                v -> DesignerSettingsStore.setShowCount(this, v));

        addChoice("Стартовое состояние категорий",
                "Какие категории показывать развёрнутыми при входе в Дизайнер",
                DesignerSettingsStore.getTabsStartState(this),
                new String[]{"expanded", "collapsed", "first_expanded"},
                new String[]{"Все развёрнуты", "Все свёрнуты", "Первая развёрнута"},
                v -> DesignerSettingsStore.setTabsStartState(this, v));
    }

    private void buildAppearanceSection() {
        addHeader("Внешний вид палитры");

        addChoice("Размер иконок",
                "Размер иконок виджетов в палитре",
                DesignerSettingsStore.getIconSize(this),
                new String[]{"compact", "normal", "large"},
                new String[]{"Компактный", "Обычный", "Крупный"},
                v -> DesignerSettingsStore.setIconSize(this, v));

        addChoice("Размер текста",
                "Размер текста палитры",
                DesignerSettingsStore.getTextSize(this),
                new String[]{"small", "normal", "large"},
                new String[]{"Мелкий", "Обычный", "Крупный"},
                v -> DesignerSettingsStore.setTextSize(this, v));

        addChoice("Ширина палитры",
                "Ширина боковой палитры виджетов",
                DesignerSettingsStore.getPaletteWidth(this),
                new String[]{"narrow", "normal", "wide"},
                new String[]{"Узкая", "Обычная", "Широкая"},
                v -> DesignerSettingsStore.setPaletteWidth(this, v));

        addSwitch("Показывать подписи",
                "Подписи под иконками виджетов",
                DesignerSettingsStore.isShowLabels(this),
                v -> DesignerSettingsStore.setShowLabels(this, v));
    }

    private void buildWidgetBehaviorSection() {
        addHeader("Поведение виджетов");

        addChoice("Двойной тап по виджету",
                "Что делать при двойном тапе на виджете",
                DesignerSettingsStore.getDoubleTapAction(this),
                new String[]{"properties", "none"},
                new String[]{"Открыть свойства", "Ничего"},
                v -> DesignerSettingsStore.setDoubleTapAction(this, v));

        addChoice("Долгий тап по виджету",
                "Что делать при долгом тапе на виджете",
                DesignerSettingsStore.getLongTapAction(this),
                new String[]{"properties", "none"},
                new String[]{"Открыть свойства", "Ничего"},
                v -> DesignerSettingsStore.setLongTapAction(this, v));

        addSwitch("Показывать тултипы",
                "Всплывающие описания при долгом тапе",
                DesignerSettingsStore.isTooltips(this),
                v -> DesignerSettingsStore.setTooltips(this, v));
    }

    private void buildQuickActionsSection() {
        addHeader("Быстрые действия");

        addSwitch("Кнопка «Свернуть всё»",
                "Показывать кнопку сворачивания всех категорий в шапке палитры",
                DesignerSettingsStore.isShowCollapseAll(this),
                v -> DesignerSettingsStore.setShowCollapseAll(this, v));

        addSwitch("Строка поиска",
                "Поиск виджетов по названию над палитрой",
                DesignerSettingsStore.isShowSearch(this),
                v -> DesignerSettingsStore.setShowSearch(this, v));

        addSwitch("Старый диалог сохранения",
                "Показывать диалог сохранения ВСЕГДА при выходе из проекта. Если выключено — только при наличии изменений.",
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
        tv.setText("(в разработке)");
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
                    .setNegativeButton("Отмена", null)
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
