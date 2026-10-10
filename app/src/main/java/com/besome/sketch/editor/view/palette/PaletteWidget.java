package com.besome.sketch.editor.view.palette;

import static pro.sketchware.utility.SketchwareUtil.dpToPx;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.besome.sketch.lib.ui.CustomScrollView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.color.MaterialColors;

import java.util.HashMap;

import a.a.a.wB;
import dev.aldi.sayuti.editor.view.palette.IconBadgeView;
import dev.aldi.sayuti.editor.view.palette.IconBottomNavigationView;
import dev.aldi.sayuti.editor.view.palette.IconCardView;
import dev.aldi.sayuti.editor.view.palette.IconCircleImageView;
import dev.aldi.sayuti.editor.view.palette.IconCodeView;
import dev.aldi.sayuti.editor.view.palette.IconCollapsingToolbar;
import dev.aldi.sayuti.editor.view.palette.IconGoogleSignInButton;
import dev.aldi.sayuti.editor.view.palette.IconLottieAnimation;
import dev.aldi.sayuti.editor.view.palette.IconMaterialButton;
import dev.aldi.sayuti.editor.view.palette.IconOTPView;
import dev.aldi.sayuti.editor.view.palette.IconPatternLockView;
import dev.aldi.sayuti.editor.view.palette.IconRadioGroup;
import dev.aldi.sayuti.editor.view.palette.IconRecyclerView;
import dev.aldi.sayuti.editor.view.palette.IconSwipeRefreshLayout;
import dev.aldi.sayuti.editor.view.palette.IconTabLayout;
import dev.aldi.sayuti.editor.view.palette.IconTextInputLayout;
import dev.aldi.sayuti.editor.view.palette.IconViewPager;
import dev.aldi.sayuti.editor.view.palette.IconWaveSideBar;
import dev.aldi.sayuti.editor.view.palette.IconYoutubePlayer;
import mod.agus.jcoderz.editor.view.palette.IconAnalogClock;
import mod.agus.jcoderz.editor.view.palette.IconAutoCompleteTextView;
import mod.agus.jcoderz.editor.view.palette.IconDatePicker;
import mod.agus.jcoderz.editor.view.palette.IconDigitalClock;
import mod.agus.jcoderz.editor.view.palette.IconGridView;
import mod.agus.jcoderz.editor.view.palette.IconMultiAutoCompleteTextView;
import mod.agus.jcoderz.editor.view.palette.IconRadioButton;
import mod.agus.jcoderz.editor.view.palette.IconRatingBar;
import mod.agus.jcoderz.editor.view.palette.IconSearchView;
import mod.agus.jcoderz.editor.view.palette.IconTimePicker;
import mod.agus.jcoderz.editor.view.palette.IconVideoView;
import mod.hey.studios.util.Helper;
import pro.sketchware.R;
import pro.sketchware.widgets.IconCustomWidget;

public class PaletteWidget extends LinearLayout {

    public MaterialCardView cardView;
    private LinearLayout layoutContainer;
    private LinearLayout widgetsContainer;
    private TextView titleLayouts;
    private TextView titleWidgets;
    private CustomScrollView scrollView;

    // Карта: имя категории → контейнер виджетов этой категории.
    private final java.util.Map<String, LinearLayout> categoryWidgetsContainers = new java.util.HashMap<>();
    // Карта: имя категории → подконтейнер (заголовок + виджеты).
    private final java.util.Map<String, LinearLayout> categoryContainers = new java.util.HashMap<>();

    // Текущая категория (к которой добавлять виджеты). null = до первого extraTitle.
    private String currentCategoryName = null;
    private LinearLayout currentWidgetsContainer = null;

    public PaletteWidget(Context context) {
        super(context);
        initialize(context);
    }

    public PaletteWidget(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    public void addCustomWidgets(View view) {
        pickTarget(layoutContainer).addView(view);
    }

    public View customWidget(HashMap<String, Object> map) {
        String title = map.get("title").toString();
        String name = map.get("name").toString();
        if (map.get("Class").toString().equals("Layouts")) {
            LinearLayout iconBase;
            Context context = getContext();
            iconBase = new IconCustomWidget(map, context);
            layoutContainer.addView(iconBase);
            return iconBase;
        } else {
            IconBase iconBase;
            Context context = getContext();
            iconBase = new IconCustomWidget(map, context);
            iconBase.setText(title);
            iconBase.setName(name);
            if (map.get("Class").toString().equals("AndroidX")) {
                pickTarget(layoutContainer).addView(iconBase);
            } else {
                pickTarget(widgetsContainer).addView(iconBase);
            }
            return iconBase;
        }
    }

    public View a(PaletteWidget.a layoutType, String tag) {
        LinearLayout layout = switch (layoutType) {
            case a -> new IconLinearHorizontal(getContext());
            case b -> new IconLinearVertical(getContext());
            case c -> new IconScrollViewHorizontal(getContext());
            case d -> new IconScrollViewVertical(getContext());
        };

        if (tag != null && !tag.isEmpty()) {
            layout.setTag(tag);
        }

        pickTarget(layoutContainer).addView(layout);
        return layout;
    }

    public View a(PaletteWidget.b widgetType, String tag, String text, String resourceName) {
        IconBase iconBase;
        switch (widgetType) {
            case a -> iconBase = new IconButton(getContext());
            case c -> iconBase = new IconEditText(getContext());
            case b -> iconBase = new IconTextView(getContext());
            case d -> {
                iconBase = new IconImageView(getContext());
                ((IconImageView) iconBase).setResourceName(resourceName);
            }
            case e -> iconBase = new IconListView(getContext());
            case f -> iconBase = new IconSpinner(getContext());
            case g -> iconBase = new IconCheckBox(getContext());
            case h -> iconBase = new IconWebView(getContext());
            case i -> iconBase = new IconSwitch(getContext());
            case j -> iconBase = new IconSeekBar(getContext());
            case k -> iconBase = new IconCalendarView(getContext());
            case l -> iconBase = new IconAdView(getContext());
            case m -> iconBase = new IconProgressBar(getContext());
            case n -> iconBase = new IconMapView(getContext());
            default -> iconBase = new IconBase(getContext());
        }

        if (tag != null && !tag.isEmpty()) {
            iconBase.setTag(tag);
        }

        iconBase.setText(text);
        iconBase.setName(resourceName);
        pickTarget(widgetsContainer).addView(iconBase);
        return iconBase;
    }

    public void removeWidgetLayouts() {
        layoutContainer.removeAllViews();
        // Очищаем карты от категорий, которые жили в layoutContainer.
        java.util.List<String> toRemove = new java.util.ArrayList<>();
        for (java.util.Map.Entry<String, LinearLayout> e : categoryContainers.entrySet()) {
            if (e.getValue().getParent() == null) {
                toRemove.add(e.getKey());
            }
        }
        for (String k : toRemove) {
            categoryContainers.remove(k);
            categoryWidgetsContainers.remove(k);
        }
    }

    private void initialize(Context context) {
        wB.a(context, this, R.layout.palette_widget);
        layoutContainer = findViewById(R.id.layout);
        widgetsContainer = findViewById(R.id.widget);
        titleLayouts = findViewById(R.id.tv_layout);
        titleWidgets = findViewById(R.id.tv_widget);
        titleLayouts.setText(Helper.getResString(R.string.view_panel_title_layouts));
        titleWidgets.setText(Helper.getResString(R.string.view_panel_title_widgets));
        // Скрываем встроенные заголовки — теперь их роль выполняют категории ("Макеты" и Helper.getResString(R.string.auto_palette_widget_001)).
        titleLayouts.setVisibility(View.GONE);
        titleWidgets.setVisibility(View.GONE);
        scrollView = findViewById(R.id.scv);
        cardView = findViewById(R.id.cardView);
    }

    public void removeWidgets() {
        widgetsContainer.removeAllViews();
        // Очищаем карты от категорий, которые жили в widgetsContainer.
        java.util.List<String> toRemove = new java.util.ArrayList<>();
        for (java.util.Map.Entry<String, LinearLayout> e : categoryContainers.entrySet()) {
            if (e.getValue().getParent() == null) {
                toRemove.add(e.getKey());
            }
        }
        for (String k : toRemove) {
            categoryContainers.remove(k);
            categoryWidgetsContainers.remove(k);
        }
    }

    public void extraTitle(String title, int targetType) {
        LinearLayout target = targetType == 0 ? layoutContainer : widgetsContainer;

        // Если у категории уже есть подконтейнер — переиспользуем (защита от дублей).
        LinearLayout category = categoryContainers.get(title);
        LinearLayout widgetsBox;
        TextView titleView;

        if (category == null) {
            category = new LinearLayout(getContext());
            category.setOrientation(LinearLayout.VERTICAL);
            category.setLayoutParams(new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));

            titleView = new TextView(getContext());
            LayoutParams lp = new LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            lp.setMargins(dpToPx(4), dpToPx(4), dpToPx(4), dpToPx(4));
            titleView.setLayoutParams(lp);
            titleView.setText(title);
            titleView.setTextSize(12);
            titleView.setTextColor(MaterialColors.getColor(titleView, R.attr.colorPrimary));
            titleView.setClickable(true);
            titleView.setFocusable(true);
            titleView.setLongClickable(true);
            category.addView(titleView);

            widgetsBox = new LinearLayout(getContext());
            widgetsBox.setOrientation(LinearLayout.VERTICAL);
            widgetsBox.setLayoutParams(new LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT));
            category.addView(widgetsBox);

            // Клик по заголовку:
            //  - одиночный тап → toggle свёрнутости (с задержкой 300 мс)
            //  - двойной тап  → открыть диалог настроек категории
            //  - долгий тап   → тоже диалог (как fallback, на случай если пользователь не тапает дважды)
            final String catName = title;
            final long[] lastTapTime = {0L};
            final android.os.Handler tapHandler =
                    new android.os.Handler(android.os.Looper.getMainLooper());
            final Runnable singleTapAction = () -> toggleCategory(catName);

            titleView.setOnClickListener(v -> {
                long now = System.currentTimeMillis();
                if (now - lastTapTime[0] < 300L) {
                    // Двойной тап — отменяем одиночное действие и открываем диалог
                    tapHandler.removeCallbacks(singleTapAction);
                    lastTapTime[0] = 0L;
                    showCategorySettingsDialog(catName);
                } else {
                    // Возможно, одиночный — ждём 300 мс
                    lastTapTime[0] = now;
                    tapHandler.postDelayed(singleTapAction, 300L);
                }
            });

            // Долгий тап — тоже диалог (сохраняем для совместимости)
            titleView.setOnLongClickListener(v -> {
                tapHandler.removeCallbacks(singleTapAction);
                showCategorySettingsDialog(catName);
                return true;
            });

            target.addView(category);

            categoryContainers.put(title, category);
            categoryWidgetsContainers.put(title, widgetsBox);
        } else {
            widgetsBox = categoryWidgetsContainers.get(title);
            titleView = (TextView) category.getChildAt(0);
        }

        currentCategoryName = title;
        currentWidgetsContainer = widgetsBox;

        // Применяем сохранённое состояние.
        applyCategoryState(title);
    }

    /** Применить сохранённое состояние категории. */
    public void applyCategoryState(String name) {
        LinearLayout category = categoryContainers.get(name);
        LinearLayout widgetsBox = categoryWidgetsContainers.get(name);
        if (category == null || widgetsBox == null) return;

        int state = pro.sketchware.palette.WidgetTabsSettings.getState(getContext(), name);
        TextView titleView = (TextView) category.getChildAt(0);

        // Проверяем — включена ли анимация
        boolean animate = pro.sketchware.settings.DesignerSettingsStore.isAnimation(getContext());

        switch (state) {
            case pro.sketchware.palette.WidgetTabsSettings.STATE_HIDDEN:
                if (animate) {
                    category.animate().alpha(0f).setDuration(150).withEndAction(() -> {
                        category.setVisibility(View.GONE);
                        category.setAlpha(1f);
                    }).start();
                } else {
                    category.setVisibility(View.GONE);
                }
                break;
            case pro.sketchware.palette.WidgetTabsSettings.STATE_COLLAPSED:
                category.setVisibility(View.VISIBLE);
                widgetsBox.setVisibility(View.GONE);
                titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, android.R.drawable.arrow_down_float, 0);
                break;
            case pro.sketchware.palette.WidgetTabsSettings.STATE_EXPANDED:
            default:
                category.setVisibility(View.VISIBLE);
                widgetsBox.setVisibility(View.VISIBLE);
                titleView.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                break;
        }

        updateTitleWithCount(name);
    }

    /** Обновить заголовок категории — добавить счётчик виджетов, если включено. */
    private void updateTitleWithCount(String name) {
        LinearLayout category = categoryContainers.get(name);
        LinearLayout widgetsBox = categoryWidgetsContainers.get(name);
        if (category == null || widgetsBox == null) return;

        TextView titleView = (TextView) category.getChildAt(0);
        String base = titleView.getText().toString();
        // Убираем старый счётчик, если был
        base = base.replaceAll(" \\(\\d+\\)$", "");

        if (pro.sketchware.settings.DesignerSettingsStore.isShowCount(getContext())) {
            int count = widgetsBox.getChildCount();
            titleView.setText(base + " (" + count + ")");
        } else {
            titleView.setText(base);
        }
    }

    /** Развернуть/свернуть категорию (по клику на заголовок). */
    public void toggleCategory(String name) {
        LinearLayout category = categoryContainers.get(name);
        LinearLayout widgetsBox = categoryWidgetsContainers.get(name);
        if (category == null || widgetsBox == null) return;

        int current = pro.sketchware.palette.WidgetTabsSettings.getState(getContext(), name);
        int next;

        if (current == pro.sketchware.palette.WidgetTabsSettings.STATE_COLLAPSED) {
            next = pro.sketchware.palette.WidgetTabsSettings.STATE_EXPANDED;
        } else if (current == pro.sketchware.palette.WidgetTabsSettings.STATE_EXPANDED) {
            // Если развёрнута — сворачиваем. Если была скрыта — не трогаем.
            next = pro.sketchware.palette.WidgetTabsSettings.STATE_COLLAPSED;
        } else {
            // Скрыта — не переключаем по клику, только через долгий тап.
            return;
        }

        pro.sketchware.palette.WidgetTabsSettings.setState(getContext(), name, next);
        applyCategoryState(name);

        // Аккордеон: если разворачиваем эту категорию — сворачиваем остальные.
        if (next == pro.sketchware.palette.WidgetTabsSettings.STATE_EXPANDED
                && pro.sketchware.settings.DesignerSettingsStore.isAccordion(getContext())) {
            for (String otherName : categoryContainers.keySet()) {
                if (otherName.equals(name)) continue;
                int otherState = pro.sketchware.palette.WidgetTabsSettings.getState(getContext(), otherName);
                if (otherState == pro.sketchware.palette.WidgetTabsSettings.STATE_EXPANDED) {
                    pro.sketchware.palette.WidgetTabsSettings.setState(getContext(), otherName,
                            pro.sketchware.palette.WidgetTabsSettings.STATE_COLLAPSED);
                    applyCategoryState(otherName);
                }
            }
        }
    }

    /** Диалог настройки категории (3 опции). */
    public void showCategorySettingsDialog(String name) {
        android.content.Context ctx = getContext();
        final String[] options = {
                Helper.getResString(R.string.auto_palette_widget_002),
                Helper.getResString(R.string.auto_palette_widget_003),
                Helper.getResString(R.string.auto_palette_widget_004)
        };
        int cur = pro.sketchware.palette.WidgetTabsSettings.getState(ctx, name);
        int checked = (cur == pro.sketchware.palette.WidgetTabsSettings.STATE_COLLAPSED) ? 1
                    : (cur == pro.sketchware.palette.WidgetTabsSettings.STATE_HIDDEN) ? 2
                    : 0;

        new com.google.android.material.dialog.MaterialAlertDialogBuilder(ctx)
                .setTitle("Категория: " + name)
                .setSingleChoiceItems(options, checked, (dialog, which) -> {
                    int newState = (which == 0) ? pro.sketchware.palette.WidgetTabsSettings.STATE_EXPANDED
                                : (which == 1) ? pro.sketchware.palette.WidgetTabsSettings.STATE_COLLAPSED
                                : pro.sketchware.palette.WidgetTabsSettings.STATE_HIDDEN;
                    pro.sketchware.palette.WidgetTabsSettings.setState(ctx, name, newState);
                    applyCategoryState(name);
                    dialog.dismiss();
                })
                .setNegativeButton(Helper.getResString(R.string.common_word_cancel), null)
                .show();
    }

    /** Фильтр по имени виджета. Пустая строка — показать всё. */
    public void filterByQuery(String query) {
        try {
            String q = (query == null ? "" : query.trim().toLowerCase());
            // Обходим оба контейнера и фильтруем
            filterContainer(layoutContainer, q);
            filterContainer(widgetsContainer, q);
        } catch (Throwable ignored) {}
    }

    /** Рекурсивно фильтрует контейнер — скрывает виджеты, не подходящие под запрос. */
    private void filterContainer(android.view.ViewGroup parent, String query) {
        if (parent == null) return;
        for (int i = 0; i < parent.getChildCount(); i++) {
            android.view.View child = parent.getChildAt(i);
            if (child instanceof IconBase) {
                // Виджет — фильтруем по имени
                IconBase icon = (IconBase) child;
                String name = icon.getWidgetName();
                boolean match = query.isEmpty() ||
                        (name != null && name.toLowerCase().contains(query));
                child.setVisibility(match ? android.view.View.VISIBLE : android.view.View.GONE);
            } else if (child instanceof android.view.ViewGroup) {
                // Контейнер — рекурсивно фильтруем
                filterContainer((android.view.ViewGroup) child, query);

                // Проверяем — есть ли в подконтейнере хоть один видимый IconBase
                boolean anyVisible = hasVisibleIcon((android.view.ViewGroup) child);
                child.setVisibility(anyVisible ? android.view.View.VISIBLE : android.view.View.GONE);
            }
        }
    }

    /** Есть ли в контейнере (рекурсивно) хоть один видимый IconBase. */
    private boolean hasVisibleIcon(android.view.ViewGroup parent) {
        if (parent == null) return false;
        for (int i = 0; i < parent.getChildCount(); i++) {
            android.view.View child = parent.getChildAt(i);
            if (child instanceof IconBase) {
                if (child.getVisibility() == android.view.View.VISIBLE) return true;
            } else if (child instanceof android.view.ViewGroup) {
                if (hasVisibleIcon((android.view.ViewGroup) child)) return true;
            }
        }
        return false;
    }

    /** Применить состояния ко всем категориям. */
    public void applyAllCategoryStates() {
        String startState = pro.sketchware.settings.DesignerSettingsStore
                .getTabsStartState(getContext());

        boolean firstDone = false;
        for (String name : categoryContainers.keySet()) {
            // Если категория ещё НЕ настраивалась пользователем — применяем стартовое состояние.
            if (!pro.sketchware.palette.WidgetTabsSettings.isExplicit(getContext(), name)) {
                if ("collapsed".equals(startState)) {
                    pro.sketchware.palette.WidgetTabsSettings.setStateQuiet(getContext(), name,
                            pro.sketchware.palette.WidgetTabsSettings.STATE_COLLAPSED);
                } else if ("first_expanded".equals(startState)) {
                    if (!firstDone) {
                        pro.sketchware.palette.WidgetTabsSettings.setStateQuiet(getContext(), name,
                                pro.sketchware.palette.WidgetTabsSettings.STATE_EXPANDED);
                        firstDone = true;
                    } else {
                        pro.sketchware.palette.WidgetTabsSettings.setStateQuiet(getContext(), name,
                                pro.sketchware.palette.WidgetTabsSettings.STATE_COLLAPSED);
                    }
                }
                // "expanded" — оставляем по умолчанию (уже expanded)
            }
            applyCategoryState(name);
        }
    }

    /** Текущий подконтейнер для добавления виджетов (или null). */
    private LinearLayout getCurrentWidgetsTarget() {
        return currentWidgetsContainer;
    }

    /** Целевой контейнер: подконтейнер текущей категории или fallback. */
    private LinearLayout pickTarget(LinearLayout fallback) {
        LinearLayout t = getCurrentWidgetsTarget();
        return (t != null) ? t : fallback;
    }

    /** Полный сброс состояния: контейнеры, карты, текущая категория. */
    public void reset() {
        layoutContainer.removeAllViews();
        widgetsContainer.removeAllViews();
        categoryContainers.clear();
        categoryWidgetsContainers.clear();
        currentCategoryName = null;
        currentWidgetsContainer = null;
    }

    public View extraWidget(String tag, String title, String name) {
        IconBase iconBase;
        Context context = getContext();
        iconBase = switch (title) {
            case "DatePicker" -> new IconDatePicker(context);
            case "RatingBar" -> new IconRatingBar(context);
            case "SearchView" -> new IconSearchView(context);
            case "DigitalClock" -> new IconDigitalClock(context);
            case "RadioButton" -> new IconRadioButton(context);
            case "GridView" -> new IconGridView(context);
            case "AutoCompleteTextView" -> new IconAutoCompleteTextView(context);
            case "MultiAutoCompleteTextView" -> new IconMultiAutoCompleteTextView(context);
            case "VideoView" -> new IconVideoView(context);
            case "TimePicker" -> new IconTimePicker(context);
            case "AnalogClock" -> new IconAnalogClock(context);
            case "ViewPager" -> new IconViewPager(context);
            case "BadgeView" -> new IconBadgeView(context);
            case "PatternLockView" -> new IconPatternLockView(context);
            case "WaveSideBar" -> new IconWaveSideBar(context);
            case "SignInButton" -> new IconGoogleSignInButton(context);
            case "MaterialButton" -> new IconMaterialButton(context);
            case "CircleImageView" -> new IconCircleImageView(context);
            case "LottieAnimation" -> new IconLottieAnimation(context);
            case "YoutubePlayer" -> new IconYoutubePlayer(context);
            case "OTPView" -> new IconOTPView(context);
            case "CodeView" -> new IconCodeView(context);
            case "RecyclerView" -> new IconRecyclerView(context);
            default -> new IconBase(context);
        };
        if (tag != null && !tag.isEmpty()) {
            iconBase.setTag(tag);
        }

        iconBase.setText(title);
        iconBase.setName(name);
        pickTarget(widgetsContainer).addView(iconBase);
        return iconBase;
    }

    public View extraWidgetLayout(String tag, String name) {
        IconBase iconBase;
        Context context = getContext();
        iconBase = switch (name) {
            case "TabLayout" -> new IconTabLayout(context);
            case "BottomNavigationView" -> new IconBottomNavigationView(context);
            case "CollapsingToolbarLayout" -> new IconCollapsingToolbar(context);
            case "SwipeRefreshLayout" -> new IconSwipeRefreshLayout(context);
            case "RadioGroup" -> new IconRadioGroup(context);
            case "CardView" -> new IconCardView(context);
            case "TextInputLayout" -> new IconTextInputLayout(context);
            case "RelativeLayout" -> new IconRelativeLayout(context);
            default -> new IconBase(context);
        };
        if (tag != null && !tag.isEmpty()) {
            iconBase.setTag(tag);
        }

        pickTarget(layoutContainer).addView(iconBase);
        return iconBase;
    }

    public void setLayoutVisible(int visibility) {
        layoutContainer.setVisibility(visibility);
        // titleLayouts всегда скрыт — его роль выполняет категория "Макеты".
    }

    public void setScrollEnabled(boolean scrollEnabled) {
        if (scrollEnabled) {
            scrollView.b();
        } else {
            scrollView.a();
        }
    }

    public void setWidgetVisible(int visibility) {
        widgetsContainer.setVisibility(visibility);
        // titleWidgets всегда скрыт — его роль выполняет категория "Виджеты".
    }

    public enum a {
        a, //eLinearHorizontal
        b, //eLinearVertical
        c, //eScrollHorizontal
        d //eScrollVertical
    }

    public enum b {
        a, //eButton
        b, //eTextView
        c, //eEditText
        d, //eImageView
        e, //eListView
        f, //eSpinner
        g, //eCheckBox
        h, //eWebView
        i, //eSwitch
        j, //eSeekBar
        k, //eCalenderView
        l, //eAddView
        m, //eProgressBar
        n, //eMapView
        o //eRadioButton
    }
}