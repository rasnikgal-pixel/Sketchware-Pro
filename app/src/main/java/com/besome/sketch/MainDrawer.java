package com.besome.sketch;

import static com.google.android.material.theme.overlay.MaterialThemeOverlay.wrap;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.core.view.WindowInsetsCompat;

import com.besome.sketch.help.ProgramInfoActivity;
import com.besome.sketch.tools.NewKeyStoreActivity;
import com.besome.sketch.tools.ToolsActivity;
import com.besome.sketch.help.HelpActivity;
import com.google.android.material.navigation.NavigationView;

import a.a.a.mB;
import dev.chrisbanes.insetter.Insetter;
import dev.chrisbanes.insetter.Side;
import mod.hilal.saif.activities.tools.AppSettings;
import pro.sketchware.R;
import pro.sketchware.utility.UI;

public class MainDrawer extends NavigationView {
    private static final int DEF_STYLE_RES = R.style.Widget_SketchwarePro_NavigationView_Main;

    public MainDrawer(@NonNull Context context) {
        this(context, null);
    }

    public MainDrawer(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, R.attr.navigationViewStyle);
    }

    public MainDrawer(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(wrap(context, attrs, defStyleAttr, DEF_STYLE_RES), attrs, defStyleAttr);
        context = getContext();

        var layoutDirection = context.getResources().getConfiguration().getLayoutDirection();
        Insetter.builder()
                .margin(WindowInsetsCompat.Type.displayCutout() | WindowInsetsCompat.Type.navigationBars(),
                        Side.create(layoutDirection == LAYOUT_DIRECTION_LTR,
                                false, layoutDirection == LAYOUT_DIRECTION_RTL, false))
                .applyToView(this);

        ViewGroup headerView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.main_drawer_header, null);
        headerView.findViewById(R.id.status_bar_overlapper).setMinimumHeight(UI.getStatusBarHeight(context));

        addHeaderView(headerView);
        inflateMenu(R.menu.main_drawer_menu);
        setNavigationItemSelectedListener(item -> {
            initializeSocialLinks(item.getItemId());
            initializeDrawerItems(item.getItemId());

            // Return false to prevent selection
            return false;
        });
    }

    private void initializeSocialLinks(@IdRes int id) {
        if (!mB.a()) {
            if (id == R.id.social_russian_help) {
                Activity activity = unwrap(getContext());
                Intent intent = new Intent(activity, HelpActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                activity.startActivity(intent);
            }
        }
    }

    private void initializeDrawerItems(@IdRes int id) {
        Activity activity = unwrap(getContext());
        if (id == R.id.program_info) {
            Intent intent = new Intent(activity, ProgramInfoActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivityForResult(intent, 105);
        } else if (id == R.id.app_settings) {
            Intent intent = new Intent(activity, AppSettings.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
        } else if (id == R.id.create_release_keystore) {
            Intent intent = new Intent(activity, NewKeyStoreActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
        } else if (id == R.id.item_language) {
            showLanguageDialog(activity);
        } else if (id == R.id.item_tools) {
            Intent intent = new Intent(activity, ToolsActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            activity.startActivity(intent);
        } else if (id == R.id.sora_editor) {
            showProjectPickerDialog(activity);
        }
    }

    /**
     * Показывает диалог выбора проекта и открывает SoraEditorActivity.
     */
    private void showProjectPickerDialog(Activity activity) {
        java.util.ArrayList<java.util.HashMap<String, Object>> projects = a.a.a.lC.a();
        if (projects == null || projects.isEmpty()) {
            pro.sketchware.utility.SketchwareUtil.toast(
                    mod.hey.studios.util.Helper.getResString(R.string.sora_xml_editor_empty));
            return;
        }

        // Сортируем по имени проекта
        projects.sort((p1, p2) -> {
            String n1 = String.valueOf(p1.get("my_ws_name"));
            String n2 = String.valueOf(p2.get("my_ws_name"));
            return n1.compareToIgnoreCase(n2);
        });

        String[] names = new String[projects.size()];
        final String[] ids = new String[projects.size()];
        for (int i = 0; i < projects.size(); i++) {
            java.util.HashMap<String, Object> p = projects.get(i);
            names[i] = String.valueOf(p.get("my_ws_name"));
            ids[i] = String.valueOf(p.get("sc_id"));
        }

        new com.google.android.material.dialog.MaterialAlertDialogBuilder(activity)
                .setTitle(R.string.sora_xml_editor_pick_project)
                .setItems(names, (d, which) -> {
                    Intent intent = new Intent(activity,
                            pro.sketchware.activities.soraeditor.SoraEditorActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent.putExtra("sc_id", ids[which]);
                    activity.startActivity(intent);
                })
                .setNegativeButton(mod.hey.studios.util.Helper.getResString(R.string.common_word_cancel), null)
                .show();
    }

    private void showLanguageDialog(Activity activity) {
        String[] labels = {
                activity.getString(R.string.settings_language_system),
                activity.getString(R.string.settings_language_ru),
                activity.getString(R.string.settings_language_en),
                activity.getString(R.string.settings_language_zh),
                activity.getString(R.string.settings_language_hi),
                activity.getString(R.string.settings_language_uk)
        };
        String[] values = {
                pro.sketchware.i18n.LocaleHelper.LANG_SYSTEM,
                pro.sketchware.i18n.LocaleHelper.LANG_RU,
                pro.sketchware.i18n.LocaleHelper.LANG_EN,
                pro.sketchware.i18n.LocaleHelper.LANG_ZH,
                pro.sketchware.i18n.LocaleHelper.LANG_HI,
                pro.sketchware.i18n.LocaleHelper.LANG_UK
        };
        String current = pro.sketchware.i18n.LocaleHelper.getLanguage(activity);
        int checked = 0;
        for (int i = 0; i < values.length; i++) {
            if (values[i].equals(current)) { checked = i; break; }
        }
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(activity)
                .setTitle(R.string.settings_language_title)
                .setSingleChoiceItems(labels, checked, (dialog, which) -> {
                    pro.sketchware.i18n.LocaleHelper.setLanguage(activity, values[which]);
                    pro.sketchware.i18n.LocaleHelper.apply(activity);
                    dialog.dismiss();
                    android.widget.Toast.makeText(activity,
                            R.string.settings_language_restart_toast,
                            android.widget.Toast.LENGTH_SHORT).show();
                    activity.recreate();
                })
                .setNegativeButton(android.R.string.cancel, null)
                .show();
    }

    private void openUrl(String url) {
        Activity activity = unwrap(getContext());
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        activity.startActivity(intent);
    }

    private Activity unwrap(Context context) {
        while (!(context instanceof Activity) && context instanceof ContextWrapper) {
            context = ((ContextWrapper) context).getBaseContext();
        }

        return (Activity) context;
    }
}
