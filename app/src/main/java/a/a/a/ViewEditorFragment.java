package a.a.a;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;

import androidx.annotation.NonNull;

import com.besome.sketch.beans.HistoryViewBean;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ViewBean;
import com.besome.sketch.design.DesignActivity;
import com.besome.sketch.editor.LogicEditorActivity;
import com.besome.sketch.editor.PropertyActivity;
import com.besome.sketch.editor.view.DraggingListener;
import com.besome.sketch.editor.view.ViewEditor;
import com.besome.sketch.editor.view.ViewProperty;
import com.besome.sketch.editor.view.palette.PaletteWidget;

import java.util.ArrayList;

import mod.hey.studios.util.Helper;
import pro.sketchware.R;
import pro.sketchware.utility.SketchwareUtil;
import pro.sketchware.widgets.WidgetsCreatorManager;

public class ViewEditorFragment extends qA {

    public ViewEditor viewEditor;
    private ProjectFileBean projectFileBean;
    private boolean isFabEnabled = false;
    private ViewProperty viewProperty;

    // Быстрая панель свойств виджета (сверху)
    private View quickPropsPanel;
    private EditText quickPropsId;
    private EditText quickPropsText;
    private EditText quickPropsHint;
    private com.google.android.material.button.MaterialButton quickPropsWidth;
    private com.google.android.material.button.MaterialButton quickPropsHeight;
    private com.google.android.material.button.MaterialButton quickPropsMargin;
    private com.google.android.material.button.MaterialButton quickPropsPadding;
    private View quickPropsGroupSize;
    private View quickPropsGroupIndent;
    private com.google.android.material.tabs.TabLayout quickPropsTabs;
    private View quickPropsTextLayout;
    private View quickPropsHintLayout;
    private View quickPropsWidthLayout;
    private View quickPropsHeightLayout;
    private ViewBean quickPropsCurrentBean;
    private boolean quickPropsUpdating = false;
    private View quickBottomToggle;
    private ObjectAnimator showPropertyViewAnimator;
    private ObjectAnimator hidePropertyViewAnimator;
    private boolean isPropertyViewVisible;
    private boolean isDragging = false;
    private String sc_id;

    private WidgetsCreatorManager widgetsCreatorManager;

    public ViewEditorFragment() {
    }

    /**
     * Инициализация быстрой панели свойств виджета (сверху).
     * Показывает id / text / hint выбранного виджета. Полные свойства — кнопка "Ещё".
     */
    private void initQuickPropsPanel(ViewGroup viewGroup) {
        try {
            quickPropsPanel = requireActivity().findViewById(R.id.quick_props_include);
            if (quickPropsPanel == null) return;
            quickPropsId = quickPropsPanel.findViewById(R.id.quick_props_id);
            quickPropsText = quickPropsPanel.findViewById(R.id.quick_props_text);
            quickPropsHint = quickPropsPanel.findViewById(R.id.quick_props_hint);
            quickPropsTextLayout = quickPropsPanel.findViewById(R.id.quick_props_text_layout);
            quickPropsHintLayout = quickPropsPanel.findViewById(R.id.quick_props_hint_layout);
            quickPropsWidth = quickPropsPanel.findViewById(R.id.quick_props_width);
            quickPropsHeight = quickPropsPanel.findViewById(R.id.quick_props_height);

            ImageButton close = quickPropsPanel.findViewById(R.id.quick_props_close);
            if (close != null) close.setOnClickListener(v -> hideQuickPropsPanel());

            View more = quickPropsPanel.findViewById(R.id.quick_props_more);
            if (more != null) more.setOnClickListener(v -> {
                if (quickPropsCurrentBean != null) openPropertyActivity(quickPropsCurrentBean);
            });

            TextWatcher watcher = new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int st, int c, int a) {}
                @Override public void onTextChanged(CharSequence s, int st, int b, int c) {}
                @Override public void afterTextChanged(Editable e) {
                    applyQuickPropsChanges();
                }
            };
            if (quickPropsId != null) quickPropsId.addTextChangedListener(watcher);
            if (quickPropsText != null) quickPropsText.addTextChangedListener(watcher);
            if (quickPropsHint != null) quickPropsHint.addTextChangedListener(watcher);

            quickPropsTabs = quickPropsPanel.findViewById(R.id.quick_props_tabs);
            quickPropsGroupSize = quickPropsPanel.findViewById(R.id.quick_props_group_size);
            quickPropsGroupIndent = quickPropsPanel.findViewById(R.id.quick_props_group_indent);
            quickPropsMargin = quickPropsPanel.findViewById(R.id.quick_props_margin);
            quickPropsPadding = quickPropsPanel.findViewById(R.id.quick_props_padding);

            if (quickPropsTabs != null) {
                quickPropsTabs.removeAllTabs();
                quickPropsTabs.addTab(quickPropsTabs.newTab().setText(R.string.quick_props_tab_size));
                quickPropsTabs.addTab(quickPropsTabs.newTab().setText(R.string.quick_props_tab_indent));
                quickPropsTabs.addOnTabSelectedListener(new com.google.android.material.tabs.TabLayout.OnTabSelectedListener() {
                    @Override public void onTabSelected(com.google.android.material.tabs.TabLayout.Tab tab) {
                        boolean isSize = tab.getPosition() == 0;
                        if (quickPropsGroupSize != null) quickPropsGroupSize.setVisibility(isSize ? View.VISIBLE : View.GONE);
                        if (quickPropsGroupIndent != null) quickPropsGroupIndent.setVisibility(isSize ? View.GONE : View.VISIBLE);
                    }
                    @Override public void onTabUnselected(com.google.android.material.tabs.TabLayout.Tab tab) {}
                    @Override public void onTabReselected(com.google.android.material.tabs.TabLayout.Tab tab) {}
                });
            }

            if (quickPropsMargin != null) {
                quickPropsMargin.setOnClickListener(v -> {
                    if (quickPropsCurrentBean != null) showIndentDialog(true, quickPropsCurrentBean);
                });
            }
            if (quickPropsPadding != null) {
                quickPropsPadding.setOnClickListener(v -> {
                    if (quickPropsCurrentBean != null) showIndentDialog(false, quickPropsCurrentBean);
                });
            }

            if (quickPropsWidth != null) {
                quickPropsWidth.setOnClickListener(v -> {
                    if (quickPropsCurrentBean != null) showMeasureDialog(true, quickPropsCurrentBean);
                });
            }
            if (quickPropsHeight != null) {
                quickPropsHeight.setOnClickListener(v -> {
                    if (quickPropsCurrentBean != null) showMeasureDialog(false, quickPropsCurrentBean);
                });
            }

            initBottomToggle(viewGroup);
            
            // Если тумблер включен, нижняя панель должна быть скрыта по умолчанию
            if (isQuickPropsEnabled()) {
                hideViewPropertyViewInstant();
            }
        } catch (Throwable ignored) {}
    }

    /** Инициализация кнопки-шторки нижней панели свойств. */
    private void initBottomToggle(ViewGroup viewGroup) {
        try {
            // Кнопка теперь внутри viewProperty. Ищем её там.
            quickBottomToggle = viewProperty.findViewById(R.id.bottom_panel_toggle_include);
            if (quickBottomToggle == null) return;
            quickBottomToggle.setOnClickListener(v -> {
                try {
                    a(!isPropertyViewVisible);
                } catch (Throwable ignored) {}
            });
            updateBottomTogglePosition(isPropertyViewVisible);
        } catch (Throwable ignored) {}
    }

    /**
     * Синхронизирует позицию кнопки-шторки с нижней панелью.
     * Панель скрыта → кнопка внизу (translationY=0), иконка «вверх» (^).
     * Панель открыта → кнопка над панелью (translationY=-height), иконка «вниз» (v).
     */
    private void updateBottomTogglePosition(boolean panelVisible) {
        if (quickBottomToggle == null || getContext() == null) return;
        try {
            // Кнопка внутри viewProperty — позиция автоматическая. Меняем только иконку.
            android.widget.ImageView icon = quickBottomToggle.findViewById(R.id.bottom_panel_toggle_icon);
            if (icon != null) {
                icon.setImageResource(panelVisible
                        ? R.drawable.ic_mtrl_arrow_down
                        : R.drawable.ic_mtrl_arrow_up);
                icon.setContentDescription(Helper.getResString(panelVisible
                        ? R.string.quick_props_collapse
                        : R.string.quick_props_expand));
            }
            // Кнопка всегда видима, если выбран виджет (если тумблер включен)
            if (isQuickPropsEnabled() && quickBottomToggle.getVisibility() != View.VISIBLE) {
                quickBottomToggle.setVisibility(View.VISIBLE);
            }
        } catch (Throwable ignored) {}
    }

    private void showBottomToggle() {
        if (quickBottomToggle != null) {
            quickBottomToggle.setVisibility(View.VISIBLE);
            updateBottomTogglePosition(isPropertyViewVisible);
        }
    }

    /** Мгновенно скрывает нижнюю панель без анимации. */
    private void hideViewPropertyViewInstant() {
        try {
            if (showPropertyViewAnimator != null) showPropertyViewAnimator.cancel();
            if (hidePropertyViewAnimator != null) hidePropertyViewAnimator.cancel();
            viewProperty.setTranslationY(wB.a(requireActivity(), 170f));
            isPropertyViewVisible = false;
            updateBottomTogglePosition(false);
        } catch (Throwable ignored) {}
    }

    private void hideBottomToggle() {
        if (quickBottomToggle != null) quickBottomToggle.setVisibility(View.GONE);
    }

    private boolean isQuickPropsEnabled() {
        try {
            return mod.hilal.saif.activities.tools.ConfigActivity.isSettingEnabled(
                    mod.hilal.saif.activities.tools.ConfigActivity.SETTING_QUICK_PROPS_PANEL);
        } catch (Throwable t) {
            return false;
        }
    }

    private void showQuickPropsPanel(ViewBean bean) {
        if (quickPropsPanel == null || bean == null || !isQuickPropsEnabled()) return;
        quickPropsUpdating = true;
        try {
            quickPropsCurrentBean = bean;
            quickPropsPanel.setVisibility(View.VISIBLE);
            showBottomToggle();
            // При включенном быстром режиме нижняя панель ВСЕГДА скрыта по умолчанию
            if (isPropertyViewVisible) {
                hideViewPropertyViewInstant(); // Скрываем мгновенно
            }
            if (quickPropsId != null) quickPropsId.setText(bean.id == null ? "" : bean.id);
            boolean hasText = hasTextField(bean);
            if (quickPropsTextLayout != null) quickPropsTextLayout.setVisibility(hasText ? View.VISIBLE : View.GONE);
            if (quickPropsText != null) {
                String txt = bean.text != null && bean.text.text != null ? bean.text.text : "";
                quickPropsText.setText(txt);
            }
            boolean hasHint = hasHintField(bean);
            if (quickPropsHintLayout != null) quickPropsHintLayout.setVisibility(hasHint ? View.VISIBLE : View.GONE);
            if (quickPropsHint != null) {
                String hint = bean.text != null && bean.text.hint != null ? bean.text.hint : "";
                quickPropsHint.setText(hint);
            }
            if (bean.layout != null) {
                if (quickPropsWidth != null) quickPropsWidth.setText(Helper.getResString(R.string.quick_props_width) + ": " + measureToString(bean.layout.width));
                if (quickPropsHeight != null) quickPropsHeight.setText(Helper.getResString(R.string.quick_props_height) + ": " + measureToString(bean.layout.height));
            }
        } finally {
            quickPropsUpdating = false;
        }
    }

    private void hideQuickPropsPanel() {
        if (quickPropsPanel != null) quickPropsPanel.setVisibility(View.GONE);
        hideBottomToggle();
        quickPropsCurrentBean = null;
    }

    /** Тип виджета поддерживает "text" (TextView, Button, EditText, CheckBox и т.п.)? */
    private boolean hasTextField(ViewBean bean) {
        int t = bean.type;
        return t == ViewBean.VIEW_TYPE_WIDGET_TEXTVIEW
                || t == ViewBean.VIEW_TYPE_WIDGET_BUTTON
                || t == ViewBean.VIEW_TYPE_WIDGET_EDITTEXT
                || t == ViewBean.VIEW_TYPE_WIDGET_CHECKBOX
                || t == ViewBean.VIEW_TYPE_WIDGET_SWITCH
                || t == ViewBean.VIEW_TYPE_WIDGET_FAB;
    }

    /** Тип виджета поддерживает "hint" (только EditText)? */
    private boolean hasHintField(ViewBean bean) {
        return bean.type == ViewBean.VIEW_TYPE_WIDGET_EDITTEXT;
    }

    /** Диалог выбора отступов: для Margin (снаружи) или Padding (внутри). */
    private void showIndentDialog(boolean isMargin, ViewBean bean) {
        if (getContext() == null || bean.layout == null) return;
        String title = isMargin ? "Margin" : "Padding";
        CharSequence[] items = new CharSequence[]{Helper.getResString(R.string.quick_props_set_value), Helper.getResString(R.string.quick_props_reset_zero)};
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setItems(items, (d, which) -> {
                    if (which == 0) {
                        android.widget.EditText input = new android.widget.EditText(requireContext());
                        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
                        input.setHint(Helper.getResString(R.string.quick_props_dp_hint));
                        int cur = isMargin ? bean.layout.marginLeft : bean.layout.paddingLeft;
                        if (cur > 0) input.setText(String.valueOf(cur));
                        int pad = (int) wB.a(requireActivity(), 16f);
                        input.setPadding(pad, pad, pad, pad);
                        new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                                .setTitle(title)
                                .setView(input)
                                .setPositiveButton(R.string.common_word_ok, (dd, ww) -> {
                                    try {
                                        int val = Integer.parseInt(input.getText().toString().trim());
                                        if (isMargin) {
                                            bean.layout.marginLeft = val;
                                            bean.layout.marginTop = val;
                                            bean.layout.marginRight = val;
                                            bean.layout.marginBottom = val;
                                        } else {
                                            bean.layout.paddingLeft = val;
                                            bean.layout.paddingTop = val;
                                            bean.layout.paddingRight = val;
                                            bean.layout.paddingBottom = val;
                                        }
                                        viewEditor.updateSelection(bean.id);
                                        a(bean.id);
                                        showQuickPropsPanel(bean);
                                    } catch (Throwable ignored) {}
                                })
                                .setNegativeButton(R.string.common_word_cancel, null)
                                .show();
                    } else {
                        if (isMargin) {
                            bean.layout.marginLeft = 0;
                            bean.layout.marginTop = 0;
                            bean.layout.marginRight = 0;
                            bean.layout.marginBottom = 0;
                        } else {
                            bean.layout.paddingLeft = 0;
                            bean.layout.paddingTop = 0;
                            bean.layout.paddingRight = 0;
                            bean.layout.paddingBottom = 0;
                        }
                        viewEditor.updateSelection(bean.id);
                        a(bean.id);
                        showQuickPropsPanel(bean);
                    }
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    /** Диалог выбора размера (width/height): match_parent / wrap_content / ввести число. */
    private void showMeasureDialog(boolean isWidth, ViewBean bean) {
        if (getContext() == null || bean.layout == null) return;
        int current = isWidth ? bean.layout.width : bean.layout.height;
        String title = Helper.getResString(isWidth ? R.string.quick_props_width : R.string.quick_props_height);

        CharSequence[] items = new CharSequence[]{
                "match_parent",
                "wrap_content",
                Helper.getResString(R.string.quick_props_enter_dp)
        };
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle(title)
                .setSingleChoiceItems(items, -1, (d, which) -> {
                    d.dismiss();
                    int newVal;
                    if (which == 0) newVal = -1;
                    else if (which == 1) newVal = -2;
                    else { showCustomMeasureDialog(isWidth, bean, current); return; }
                    if (isWidth) bean.layout.width = newVal;
                    else bean.layout.height = newVal;
                    viewEditor.updateSelection(bean.id);
                    a(bean.id);
                    showQuickPropsPanel(bean); // обновить панель
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    private void showCustomMeasureDialog(boolean isWidth, ViewBean bean, int current) {
        android.widget.EditText input = new android.widget.EditText(requireContext());
        input.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        input.setHint(Helper.getResString(R.string.quick_props_dp_hint));
        if (current > 0) input.setText(String.valueOf(current));
        int pad = (int) wB.a(requireActivity(), 16f);
        input.setPadding(pad, pad, pad, pad);

        new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                .setTitle(Helper.getResString(isWidth ? R.string.quick_props_width : R.string.quick_props_height))
                .setView(input)
                .setPositiveButton(R.string.common_word_ok, (d, w) -> {
                    try {
                        int val = Integer.parseInt(input.getText().toString().trim());
                        if (isWidth) bean.layout.width = val;
                        else bean.layout.height = val;
                        viewEditor.updateSelection(bean.id);
                        a(bean.id);
                        showQuickPropsPanel(bean);
                    } catch (Throwable ignored) {}
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    /** int из ViewBean.layout.width/height → строка для EditText. */
    private String measureToString(int value) {
        if (value == -1) return "match";
        if (value == -2) return "wrap";
        return String.valueOf(value);
    }

    /** строка из EditText → int для ViewBean.layout.width/height. */
    private int stringToMeasure(String str) {
        if (str == null) return -2;
        String v = str.trim().toLowerCase();
        if (v.isEmpty()) return -2;
        if (v.equals("match") || v.equals("match_parent") || v.equals("m")) return -1;
        if (v.equals("wrap") || v.equals("wrap_content") || v.equals("w")) return -2;
        try {
            return Integer.parseInt(v);
        } catch (Throwable t) {
            return -2;
        }
    }

    /** Применяет изменения из панели к выбранному ViewBean. */
    private void applyQuickPropsChanges() {
        if (quickPropsUpdating) return;
        if (quickPropsCurrentBean == null || quickPropsPanel == null) return;
        try {
            ViewBean bean = quickPropsCurrentBean;
            if (quickPropsId != null) {
                String newId = quickPropsId.getText() == null ? "" : quickPropsId.getText().toString().trim();
                if (!newId.isEmpty() && !newId.equals(bean.id)) {
                    bean.id = newId;
                }
            }
            if (hasTextField(bean) && quickPropsText != null) {
                if (bean.text == null) bean.text = new com.besome.sketch.beans.TextBean();
                bean.text.text = quickPropsText.getText() == null ? "" : quickPropsText.getText().toString();
            }
            if (hasHintField(bean) && quickPropsHint != null) {
                if (bean.text == null) bean.text = new com.besome.sketch.beans.TextBean();
                bean.text.hint = quickPropsHint.getText() == null ? "" : quickPropsHint.getText().toString();
            }
            // width/height применяются из диалога showMeasureDialog — тут не трогаем.
            // Обновить отображение в редакторе + сохранить
            viewEditor.updateSelection(bean.id);
            a(bean.id);
            invalidateOptionsMenu();
        } catch (Throwable ignored) {}
    }

    private void initialize(ViewGroup viewGroup) {
        setHasOptionsMenu(true);
        viewEditor = viewGroup.findViewById(R.id.view_editor);
        initQuickPropsPanel(viewGroup);
        viewEditor.setScreenType(getResources().getConfiguration().orientation);
        widgetsCreatorManager = new WidgetsCreatorManager(this);
        viewEditor.widgetsCreatorManager = widgetsCreatorManager;
        viewProperty = requireActivity().findViewById(R.id.view_property);
        viewProperty.setOnPropertyListener(new Iw() {
            @Override
            public void a() {
                viewEditor.setFavoriteData(Rp.h().f());
            }

            @Override
            public void a(String s, ViewBean viewBean) {
                openPropertyActivity(viewBean);
            }
        });
        viewProperty.setOnPropertyValueChangedListener(viewBean -> {
            a(viewBean.id);
            viewProperty.e();
            invalidateOptionsMenu();
        });
        viewProperty.setOnPropertyDeleted(viewBean -> {
            viewEditor.deleteWidget(viewBean);
            if (requireActivity() instanceof DesignActivity designActivity) {
                designActivity.hideViewPropertyView();
            }
            SketchwareUtil.toast(Helper.getResString(R.string.common_word_deleted));
        });
        viewProperty.setOnEventClickListener(eventBean -> toLogicEditorActivity(eventBean.targetId, eventBean.eventName, eventBean.eventName));
        viewProperty.setOnPropertyTargetChangeListener(viewEditor::updateSelection);
        viewEditor.setOnWidgetDoubleTapListener(viewBean -> {
            if (viewBean == null) return;

            // Двойной тап — открыть список событий виджета (как в Delphi)
            showWidgetEventsDialog(viewBean);
        });

        viewEditor.setOnWidgetSelectedListener(new cy() {
            @Override
            public void a() {
                n();
                viewProperty.e();
                hideQuickPropsPanel();
                // Скрываем и нижнюю панель, чтобы всё было чисто при клике по пустому месту
                if (isQuickPropsEnabled()) {
                    ViewEditorFragment.this.a(false);
                }
            }

            @Override
            public void a(String viewId) {
                n();
                viewProperty.a(viewId);
            }

            @Override
            public void a(boolean var1, String viewId) {
                if (!viewId.isEmpty()) {
                    a();
                    viewProperty.a(viewId);
                    viewProperty.e();
                    // Быстрая панель свойств сверху
                    try {
                        ViewBean qb = viewEditor.findViewBeanByTag(viewId);
                        if (qb != null) showQuickPropsPanel(qb);
                    } catch (Throwable ignored) {}
                } else {
                    hideQuickPropsPanel();
                }

                // Если включена быстрая панель сверху, нижнюю панель НЕ открываем
                if (isQuickPropsEnabled()) {
                    ViewEditorFragment.this.a(false);
                } else {
                    ViewEditorFragment.this.a(var1);
                }
            }
        });
        viewEditor.setOnDraggingListener(new DraggingListener() {
            @Override
            public boolean isAdmobEnabled() {
                return jC.c(sc_id).b().isEnabled();
            }

            @Override
            public void b() {
                isDragging = true;
                ((DesignActivity) requireActivity()).setTouchEventEnabled(false);
            }

            @Override
            public boolean isGoogleMapEnabled() {
                return jC.c(sc_id).e().isEnabled();
            }

            @Override
            public void d() {
                isDragging = false;
                ((DesignActivity) requireActivity()).setTouchEventEnabled(true);

                // Пользователь только что уронил виджет — значит, проект изменён.
                try {
                    ((DesignActivity) requireActivity()).hasUnsavedChanges = true;
                } catch (Throwable ignored) {}
            }
        });
        viewEditor.setOnHistoryChangeListener(this::invalidateOptionsMenu);
        viewEditor.setFavoriteData(Rp.h().f());
    }

    public void initialize(ProjectFileBean projectFileBean) {
        this.projectFileBean = projectFileBean;
        isFabEnabled = projectFileBean.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_FAB);
        viewEditor.initialize(sc_id, projectFileBean);
        viewEditor.h();
        viewProperty.a(sc_id, this.projectFileBean);
        e();
        i();
        invalidateOptionsMenu();
    }

    private void a(ViewBean viewBean) {
        viewEditor.removeFab();
        if (isFabEnabled) viewEditor.addFab(viewBean);
    }

    private void a(String viewId) {
        ViewBean viewBean;
        if (viewId.equals("_fab")) {
            viewBean = jC.a(sc_id).h(projectFileBean.getXmlName());
        } else {
            viewBean = jC.a(sc_id).c(projectFileBean.getXmlName(), viewId);
        }
        c(viewBean);
        viewProperty.e();
    }

    /**
     * Показывает диалог со списком всех событий виджета (как в Delphi).
     * По выбору открывает LogicEditorActivity для создания блоков события.
     */
    private void showWidgetEventsDialog(ViewBean bean) {
        if (bean == null || bean.id == null) return;
        try {
            String[] events = oq.getEventsForClass(bean.getClassInfo());
            if (events == null || events.length == 0) {
                SketchwareUtil.toast(Helper.getResString(R.string.sora_xml_editor_empty));
                return;
            }

            // Формируем читаемые названия без суффикса Listener
            String[] display = new String[events.length];
            for (int i = 0; i < events.length; i++) {
                display[i] = events[i].replace("Listener", "");
            }

            String title = (bean.id != null && !bean.id.isEmpty())
                    ? bean.id
                    : Helper.getResString(R.string.auto_view_editor_003);

            new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                    .setTitle(title)
                    .setItems(display, (d, which) -> {
                        String eventName = events[which];
                        toLogicEditorActivity(bean.id, eventName, display[which]);
                    })
                    .setNegativeButton(Helper.getResString(R.string.common_word_cancel), null)
                    .show();
        } catch (Throwable t) {
            pro.sketchware.smartdrop.DebugLogger.get(requireContext())
                    .d("ViewEditorFragment", "showWidgetEventsDialog_error", String.valueOf(t.getMessage()));
        }
    }

    private void toLogicEditorActivity(String eventId, String eventName, String eventName2) {
        Intent intent = new Intent(requireContext(), LogicEditorActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        intent.putExtra("id", eventId);
        intent.putExtra("event", eventName);
        intent.putExtra("project_file", projectFileBean);
        intent.putExtra("event_text", eventName2);
        requireContext().startActivity(intent);
    }

    public void a(ArrayList<ViewBean> viewBeans) {
        viewEditor.h();
        viewEditor.a(eC.a(viewBeans));
    }

    public void a(boolean var1) {
        startAnimation();
        if (!isPropertyViewVisible || !var1) {
            cancelAnimations();
            if (var1) {
                showPropertyViewAnimator.start();
            } else if (isPropertyViewVisible) {
                hidePropertyViewAnimator.start();
            }

            isPropertyViewVisible = var1;
            updateBottomTogglePosition(isPropertyViewVisible);
        }
    }

    public void openPropertyActivity(ViewBean viewBean) {
        Intent intent = new Intent(requireContext(), PropertyActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        intent.putExtra("sc_id", sc_id);
        intent.putExtra("bean", viewBean);
        intent.putExtra("project_file", projectFileBean);
        startActivityForResult(intent, 213);
    }

    private void b(ArrayList<ViewBean> viewBeans) {
        l();
        a(viewBeans);
    }

    private void cancelAnimations() {
        if (showPropertyViewAnimator.isRunning()) showPropertyViewAnimator.cancel();
        if (hidePropertyViewAnimator.isRunning()) hidePropertyViewAnimator.cancel();
    }

    public void c(ViewBean var1) {
        viewEditor.e(var1);
    }

    public void showHidePropertyView(boolean shouldShow) {
        viewProperty.setVisibility(shouldShow ? View.VISIBLE : View.GONE);
    }

    public ProjectFileBean d() {
        return projectFileBean;
    }

    public void e() {
        viewEditor.removeWidgetsAndLayouts();
        viewEditor.setPaletteLayoutVisible(View.VISIBLE);
        viewEditor.paletteWidget.extraTitle(Helper.getResString(R.string.auto_view_editor_fragment_001), 0);
        viewEditor.addWidgetLayout(PaletteWidget.a.a, "");
        viewEditor.addWidgetLayout(PaletteWidget.a.b, "");
        viewEditor.addWidget(PaletteWidget.b.b, "", "TextView", "TextView");
        viewEditor.addWidgetLayout(PaletteWidget.a.c, "");
        viewEditor.addWidgetLayout(PaletteWidget.a.d, "");
        viewEditor.extraWidgetLayout("", "RadioGroup");
        viewEditor.extraWidgetLayout("", "RelativeLayout");
        widgetsCreatorManager.addWidgetsByTitle(Helper.getResString(R.string.auto_view_editor_fragment_002));

        viewEditor.paletteWidget.extraTitle("AndroidX", 0);
        viewEditor.extraWidgetLayout("", "TabLayout");
        viewEditor.extraWidgetLayout("", "BottomNavigationView");
        viewEditor.extraWidgetLayout("", "CollapsingToolbarLayout");
        viewEditor.extraWidgetLayout("", "CardView");
        viewEditor.extraWidgetLayout("", "TextInputLayout");
        viewEditor.extraWidgetLayout("", "SwipeRefreshLayout");
        widgetsCreatorManager.addWidgetsByTitle("AndroidX");

        viewEditor.paletteWidget.extraTitle(Helper.getResString(R.string.auto_view_editor_fragment_003), 1);
        viewEditor.addWidget(PaletteWidget.b.c, "", "EditText", "Edit Text");
        viewEditor.extraWidget("", "AutoCompleteTextView", "AutoCompleteTextView");
        viewEditor.extraWidget("", "MultiAutoCompleteTextView", "MultiAutoCompleteTextView");
        viewEditor.addWidget(PaletteWidget.b.a, "", "Button", "Button");
        viewEditor.extraWidget("", "MaterialButton", "MaterialButton");
        viewEditor.addWidget(PaletteWidget.b.d, "", "ImageView", "default_image");
        viewEditor.extraWidget("", "CircleImageView", "default_image");
        viewEditor.addWidget(PaletteWidget.b.g, "", "CheckBox", "CheckBox");
        viewEditor.extraWidget("", "RadioButton", "RadioButton");
        viewEditor.addWidget(PaletteWidget.b.i, "", "Switch", "Switch");
        viewEditor.addWidget(PaletteWidget.b.j, "", "SeekBar", "SeekBar");
        viewEditor.addWidget(PaletteWidget.b.m, "", "ProgressBar", "ProgressBar");
        viewEditor.extraWidget("", "RatingBar", "RatingBar");
        viewEditor.extraWidget("", "SearchView", "SearchView");
        viewEditor.extraWidget("", "VideoView", "VideoView");
        viewEditor.addWidget(PaletteWidget.b.h, "", "WebView", "WebView");
        widgetsCreatorManager.addWidgetsByTitle(Helper.getResString(R.string.auto_view_editor_fragment_004));

        viewEditor.paletteWidget.extraTitle(Helper.getResString(R.string.auto_view_editor_fragment_005), 1);
        viewEditor.addWidget(PaletteWidget.b.e, "", "ListView", "ListView");
        viewEditor.extraWidget("", "GridView", "GridView");
        viewEditor.extraWidget("", "RecyclerView", "RecyclerView");
        viewEditor.addWidget(PaletteWidget.b.f, "", "Spinner", "Spinner");
        viewEditor.extraWidget("", "ViewPager", "ViewPager");
        widgetsCreatorManager.addWidgetsByTitle(Helper.getResString(R.string.auto_view_editor_fragment_006));

        viewEditor.paletteWidget.extraTitle(Helper.getResString(R.string.auto_view_editor_fragment_007), 1);
        viewEditor.extraWidget("", "WaveSideBar", "WaveSideBar");
        viewEditor.extraWidget("", "PatternLockView", "PatternLockView");
        viewEditor.extraWidget("", "CodeView", "CodeView");
        viewEditor.extraWidget("", "LottieAnimation", "LottieAnimation");
        viewEditor.extraWidget("", "OTPView", "OTPView");
        widgetsCreatorManager.addWidgetsByTitle(Helper.getResString(R.string.auto_view_editor_fragment_008));

        viewEditor.paletteWidget.extraTitle("Google", 1);
        viewEditor.addWidget(PaletteWidget.b.l, "", "AdView", "AdView");
        viewEditor.addWidget(PaletteWidget.b.n, "", "MapView", "MapView");
        viewEditor.extraWidget("", "SignInButton", "SignInButton");
        viewEditor.extraWidget("", "YoutubePlayer", "YoutubePlayer");
        widgetsCreatorManager.addWidgetsByTitle("Google");

        viewEditor.paletteWidget.extraTitle(Helper.getResString(R.string.auto_view_editor_fragment_009), 1);
        viewEditor.extraWidget("", "AnalogClock", "AnalogClock");
        viewEditor.extraWidget("", "DigitalClock", "DigitalClock");
        viewEditor.extraWidget("", "TimePicker", "TimePicker");
        viewEditor.extraWidget("", "DatePicker", "DatePicker");
        viewEditor.addWidget(PaletteWidget.b.k, "", "CalendarView", "CalendarView");
        widgetsCreatorManager.addWidgetsByTitle(Helper.getResString(R.string.auto_view_editor_fragment_010));
        widgetsCreatorManager.addExtraClasses();
        viewEditor.paletteWidget.post(() -> viewEditor.paletteWidget.applyAllCategoryStates());
    }

    private void startAnimation() {
        if (showPropertyViewAnimator == null) {
            showPropertyViewAnimator = ObjectAnimator.ofFloat(viewProperty, View.TRANSLATION_Y, 0.0F);
            showPropertyViewAnimator.setDuration(700L);
            showPropertyViewAnimator.setInterpolator(new DecelerateInterpolator());
        }

        if (hidePropertyViewAnimator == null) {
            if (getActivity() == null) return;
            hidePropertyViewAnimator = ObjectAnimator.ofFloat(viewProperty, View.TRANSLATION_Y, wB.a(requireActivity(), (float) viewProperty.getHeight()));
            hidePropertyViewAnimator.setDuration(300L);
            hidePropertyViewAnimator.setInterpolator(new DecelerateInterpolator());
        }
    }

    public boolean isPropertyViewVisible() {
        return isPropertyViewVisible;
    }

    private void onRedo() {
        if (!isDragging) {
            HistoryViewBean historyViewBean = cC.c(sc_id).h(projectFileBean.getXmlName());
            if (historyViewBean != null) {
                int actionType = historyViewBean.getActionType();
                if (actionType == HistoryViewBean.ACTION_TYPE_ADD) {
                    for (ViewBean viewBean : historyViewBean.getAddedData()) {
                        jC.a(sc_id).a(projectFileBean.getXmlName(), viewBean);
                    }
                    viewEditor.a(viewEditor.a(historyViewBean.getAddedData(), false), false);
                } else if (actionType == HistoryViewBean.ACTION_TYPE_UPDATE) {
                    ViewBean prevUpdateData = historyViewBean.getPrevUpdateData();
                    ViewBean currentUpdateData = historyViewBean.getCurrentUpdateData();
                    if (!prevUpdateData.id.equals(currentUpdateData.id)) {
                        currentUpdateData.preId = prevUpdateData.id;
                    }

                    if (currentUpdateData.id.equals("_fab")) {
                        jC.a(sc_id).h(projectFileBean.getXmlName()).copy(currentUpdateData);
                    } else {
                        jC.a(sc_id).c(projectFileBean.getXmlName(), prevUpdateData.id).copy(currentUpdateData);
                    }

                    viewEditor.a(viewEditor.e(currentUpdateData), false);
                } else if (actionType == HistoryViewBean.ACTION_TYPE_REMOVE) {
                    for (ViewBean viewBean : historyViewBean.getRemovedData()) {
                        jC.a(sc_id).a(projectFileBean, viewBean);
                    }
                    viewEditor.b(historyViewBean.getRemovedData(), false);
                    viewEditor.i();
                } else if (actionType == HistoryViewBean.ACTION_TYPE_MOVE) {
                    ViewBean movedData = historyViewBean.getMovedData();
                    ViewBean viewBean = jC.a(sc_id).c(projectFileBean.getXmlName(), movedData.id);
                    viewBean.copy(movedData);
                    viewEditor.a(viewEditor.b(viewBean, false), false);
                } else if (actionType == HistoryViewBean.ACTION_TYPE_OVERRIDE) {
                    jC.a(sc_id).c.put(projectFileBean.getXmlName(), historyViewBean.getAddedData());
                    i();
                }
            }
            invalidateOptionsMenu();
        }
    }

    public void i() {
        invalidateOptionsMenu();
        if (projectFileBean != null) {
            b(jC.a(sc_id).d(projectFileBean.getXmlName()));
            a(jC.a(sc_id).h(projectFileBean.getXmlName()));
        }
    }

    public void j() {
        viewEditor.setFavoriteData(Rp.h().f());
    }

    private void invalidateOptionsMenu() {
        if (getActivity() != null) {
            getActivity().invalidateOptionsMenu();
        }
    }

    public void l() {
        viewEditor.j();
    }

    private void onUndo() {
        if (!isDragging) {
            HistoryViewBean historyViewBean = cC.c(sc_id).i(projectFileBean.getXmlName());
            if (historyViewBean != null) {
                int actionType = historyViewBean.getActionType();
                if (actionType == HistoryViewBean.ACTION_TYPE_ADD) {
                    for (ViewBean view : historyViewBean.getAddedData()) {
                        jC.a(sc_id).a(projectFileBean, view);
                    }
                    viewEditor.b(historyViewBean.getAddedData(), false);
                    viewEditor.i();
                } else if (actionType == HistoryViewBean.ACTION_TYPE_UPDATE) {
                    ViewBean prevUpdateData = historyViewBean.getPrevUpdateData();
                    ViewBean currentUpdateData = historyViewBean.getCurrentUpdateData();
                    if (!prevUpdateData.id.equals(currentUpdateData.id)) {
                        prevUpdateData.preId = currentUpdateData.id;
                    }
                    if (currentUpdateData.id.equals("_fab")) {
                        jC.a(sc_id).h(projectFileBean.getXmlName()).copy(prevUpdateData);
                    } else {
                        jC.a(sc_id).c(projectFileBean.getXmlName(), currentUpdateData.id).copy(prevUpdateData);
                    }
                    viewEditor.a(viewEditor.e(prevUpdateData), false);
                } else if (actionType == HistoryViewBean.ACTION_TYPE_REMOVE) {
                    for (ViewBean view : historyViewBean.getRemovedData()) {
                        jC.a(sc_id).a(projectFileBean.getXmlName(), view);
                    }
                    viewEditor.a(viewEditor.a(historyViewBean.getRemovedData(), false), false);
                } else if (actionType == HistoryViewBean.ACTION_TYPE_MOVE) {
                    ViewBean movedData = historyViewBean.getMovedData();
                    ViewBean viewBean = jC.a(sc_id).c(projectFileBean.getXmlName(), movedData.id);
                    viewBean.preIndex = movedData.index;
                    viewBean.index = movedData.preIndex;
                    viewBean.parent = movedData.preParent;
                    viewBean.preParent = movedData.parent;
                    viewBean.parentType = movedData.preParentType;
                    viewBean.preParentType = movedData.parentType;
                    viewEditor.a(viewEditor.b(viewBean, false), false);
                } else if (actionType == HistoryViewBean.ACTION_TYPE_OVERRIDE) {
                    jC.a(sc_id).c.put(projectFileBean.getXmlName(), historyViewBean.getRemovedData());
                    i();
                }
            }
            invalidateOptionsMenu();
        }
    }

    public void n() {
        ArrayList<ViewBean> viewBeanArrayList = eC.a(jC.a(sc_id).d(projectFileBean.getXmlName()));
        ViewBean viewBean;
        if (projectFileBean.hasActivityOption(ProjectFileBean.OPTION_ACTIVITY_FAB)) {
            viewBean = jC.a(sc_id).h(projectFileBean.getXmlName());
        } else {
            viewBean = null;
        }
        viewProperty.addActivityViews(viewBeanArrayList, viewBean);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        invalidateOptionsMenu();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 213) {
            // Пользователь вернулся из PropertyActivity. Если сохранение было (RESULT_OK) —
            // помечаем проект как изменённый.
            if (resultCode == -1) {
                try {
                    ((DesignActivity) requireActivity()).hasUnsavedChanges = true;
                } catch (Throwable ignored) {}
                c(data.getParcelableExtra("bean"));
            }

            if (data != null && data.getBooleanExtra("is_edit_image", false)) {
                for (ViewBean viewBean : jC.a(sc_id).d(projectFileBean.getXmlName())) {
                    c(viewBean);
                }
                if (isFabEnabled) {
                    c(jC.a(sc_id).h(projectFileBean.getXmlName()));
                }
            }
            invalidateOptionsMenu();
        }
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfiguration) {
        super.onConfigurationChanged(newConfiguration);
        viewEditor.setScreenType(newConfiguration.orientation);
        viewEditor.isLayoutChanged = true;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
        super.onCreateOptionsMenu(menu, menuInflater);
        menuInflater.inflate(R.menu.design_view_menu, menu);
        menu.findItem(R.id.menu_view_redo).setEnabled(false);
        menu.findItem(R.id.menu_view_undo).setEnabled(false);
        if (projectFileBean != null) {
            menu.findItem(R.id.menu_view_redo).setEnabled(cC.c(sc_id).f(projectFileBean.getXmlName()));
            menu.findItem(R.id.menu_view_undo).setEnabled(cC.c(sc_id).g(projectFileBean.getXmlName()));
        }
    }

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup parent, Bundle bundle) {
        ViewGroup viewGroup = (ViewGroup) layoutInflater.inflate(R.layout.fr_graphic_editor, parent, false);
        initialize(viewGroup);
        if (bundle != null) {
            sc_id = bundle.getString("sc_id");
        } else {
            sc_id = requireActivity().getIntent().getStringExtra("sc_id");
        }

        return viewGroup;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menu_view_redo) {
            onRedo();
        } else if (itemId == R.id.menu_view_undo) {
            onUndo();
        }
        return true;
    }

    @Override
    public void onSaveInstanceState(Bundle newInstanceState) {
        newInstanceState.putString("sc_id", sc_id);
        super.onSaveInstanceState(newInstanceState);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (viewProperty != null) {
            viewProperty.d();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (viewEditor != null) {
            viewEditor.resetPaletteTapState();
        }
    }
}