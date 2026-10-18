package mod.jbk.util;

import android.content.Context;

import com.besome.sketch.beans.ProjectFileBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mod.agus.jcoderz.beans.ViewBeans;
import com.besome.sketch.editor.manage.library.material3.Material3LibraryManager;
import pro.sketchware.activities.usercode.UserCodeManager;
import pro.sketchware.utility.FileUtil;
import a.a.a.wq;

/**
 * Pre-flight проверки проекта перед сборкой APK.
 * Возвращает список Issue в том же формате, что и ProjectLogicChecker,
 * чтобы диалог в DesignActivity показывал всё в одном месте.
 *
 * Проверки (v1):
 *  - иконка приложения отсутствует            (WARNING)
 *  - экран с пустым javaName                  (CRITICAL)
 *  - два экрана с одинаковым javaName         (CRITICAL)
 *  - невалидное имя класса в «Мой код»        (CRITICAL)
 */
public final class ProjectPreflightChecker {

    private ProjectPreflightChecker() {}

    public static List<BlockLogicChecker.Issue> check(Context context, String scId,
                                                      List<ProjectFileBean> activities,
                                                      List<ProjectFileBean> others) {
        List<BlockLogicChecker.Issue> issues = new ArrayList<>();
        if (context == null || scId == null) return issues;

        checkIcon(context, scId, issues);
        checkActivities(context, activities, issues);
        checkUserCodeClasses(context, scId, issues);
        checkAppCompat(context, scId, activities, issues);
        checkMaterial3(context, scId, issues);

        return issues;
    }

    /**
     * Проверяет, что если styles.xml содержит Theme.Material3, то Material3 включён.
     * Иначе сборка падает с "resource style/Theme.Material3... not found".
     */
    private static void checkMaterial3(Context ctx, String scId,
                                       List<BlockLogicChecker.Issue> issues) {
        try {
            Material3LibraryManager mgr = new Material3LibraryManager(scId);
            if (mgr.isMaterial3Enabled()) return;

            String stylesPath = a.a.a.wq.b(scId) + "/files/resource/values/styles.xml";
            if (!pro.sketchware.utility.FileUtil.isExistFile(stylesPath)) return;

            String content = pro.sketchware.utility.FileUtil.readFile(stylesPath);
            if (content != null && content.contains("Theme.Material3")) {
                issues.add(new BlockLogicChecker.Issue(
                        null,
                        mod.hey.studios.util.Helper.getResString(
                                pro.sketchware.R.string.preflight_material3_stale, stylesPath),
                        new ArrayList<>(),
                        BlockLogicChecker.Severity.CRITICAL,
                        null,
                        null,
                        "styles.xml contains Theme.Material3, but Material3 is disabled"));
            }
        } catch (Throwable ignored) {}
    }

    /** Включает AppCompat и сохраняет в файл. */
    public static void enableAppCompat(String scId) {
        try {
            a.a.a.jC.c(scId).c().useYn = "Y";
            a.a.a.jC.c(scId).l();  // save
        } catch (Throwable ignored) {}
    }

    /** Включает AppCompat + Material3 и сохраняет в файл. */
    public static void enableMaterial3(String scId) {
        try {
            com.besome.sketch.beans.ProjectLibraryBean bean = a.a.a.jC.c(scId).c();
            bean.useYn = "Y";
            if (bean.configurations == null) bean.configurations = new java.util.HashMap<>();
            bean.configurations.put("material3", true);
            a.a.a.jC.c(scId).l();  // save
        } catch (Throwable ignored) {}
    }

    /**
     * Проверяет, что для Material-виджетов (TabLayout, BottomNav, FAB, CardView,
     * MaterialButton) включена библиотека AppCompat.
     */
    private static void checkAppCompat(Context ctx, String scId,
                                       List<ProjectFileBean> activities,
                                       List<BlockLogicChecker.Issue> issues) {
        try {
            // Если AppCompat уже включён — всё ок
            com.besome.sketch.beans.ProjectLibraryBean appCompat =
                    a.a.a.jC.c(scId).c();
            if (appCompat != null && appCompat.isEnabled()) return;

            // Иначе — ищем Material-виджеты на экранах
            java.util.LinkedHashSet<String> found = new java.util.LinkedHashSet<>();
            for (ProjectFileBean pfb : activities) {
                if (pfb == null) continue;
                String javaName = pfb.getJavaName();
                if (javaName == null || javaName.isEmpty()) continue;
                java.util.ArrayList<com.besome.sketch.beans.ViewBean> views;
                try {
                    views = a.a.a.jC.a(scId).d(javaName);
                } catch (Throwable t) {
                    continue;
                }
                if (views == null) continue;
                StringBuilder debugInfo = new StringBuilder();
                debugInfo.append("views.size()=").append(views.size()).append("; ");
                for (com.besome.sketch.beans.ViewBean v : views) {
                    if (v == null) continue;
                    int t = v.type;
                    debugInfo.append("type=").append(t).append(",");
                    String widgetName = null;
                    if (t == ViewBeans.VIEW_TYPE_LAYOUT_TABLAYOUT) widgetName = "TabLayout";
                    else if (t == ViewBeans.VIEW_TYPE_LAYOUT_BOTTOMNAVIGATIONVIEW) widgetName = "BottomNavigationView";
                    else if (t == ViewBeans.VIEW_TYPE_LAYOUT_CARDVIEW) widgetName = "CardView";
                    else if (t == ViewBeans.VIEW_TYPE_WIDGET_MATERIALBUTTON) widgetName = "MaterialButton";
                    else if (t == com.besome.sketch.beans.ViewBean.VIEW_TYPE_WIDGET_FAB) widgetName = "FloatingActionButton";
                    if (widgetName != null) found.add(widgetName);
                }
                if (ctx instanceof android.app.Activity) {
                    new com.google.android.material.dialog.MaterialAlertDialogBuilder((android.app.Activity) ctx)
                            .setTitle("PREFLIGHT DEBUG")
                            .setMessage("AppCompat enabled=" + appCompat.isEnabled() + "\n" + debugInfo.toString())
                            .setPositiveButton("OK", null)
                            .show();
                }
            }

            if (!found.isEmpty()) {
                String list = android.text.TextUtils.join(", ", found);
                issues.add(new BlockLogicChecker.Issue(
                        null,
                        mod.hey.studios.util.Helper.getResString(
                                pro.sketchware.R.string.preflight_appcompat_missing, list),
                        new ArrayList<>(),
                        BlockLogicChecker.Severity.CRITICAL,
                        null,
                        null,
                        "AppCompat library is required for Material widgets"));
            }
        } catch (Throwable ignored) {}
    }

    /** Иконка приложения — /sdcard/.sketchware/data/{scId}/icon.png */
    private static void checkIcon(Context ctx, String scId,
                                  List<BlockLogicChecker.Issue> issues) {
        try {
            String path = wq.e() + File.separator + scId + File.separator + "icon.png";
            if (!FileUtil.isExistFile(path)) {
                issues.add(new BlockLogicChecker.Issue(
                        null,
                        "Иконка приложения не задана (icon.png отсутствует)",
                        new ArrayList<>(),
                        BlockLogicChecker.Severity.WARNING,
                        null,
                        null,
                        "Приложение соберётся, но в лаунчере будет стандартная иконка."));
            }
        } catch (Throwable ignored) {}
    }

    /** Пустые / дубликаты javaName среди Activity-экранов */
    private static void checkActivities(Context ctx, List<ProjectFileBean> activities,
                                        List<BlockLogicChecker.Issue> issues) {
        if (activities == null || activities.isEmpty()) return;

        Set<String> seen = new HashSet<>();
        for (ProjectFileBean pfb : activities) {
            if (pfb == null) continue;
            String javaName;
            try {
                javaName = pfb.getJavaName();
            } catch (Throwable t) {
                continue;
            }
            String screenLabel = "";
            try {
                screenLabel = pfb.getXmlName();
            } catch (Throwable ignored) {}

            if (javaName == null || javaName.isEmpty()) {
                issues.add(new BlockLogicChecker.Issue(
                        null,
                        "Экран \"" + screenLabel + "\" без имени Java-класса",
                        new ArrayList<>(),
                        BlockLogicChecker.Severity.CRITICAL,
                        screenLabel,
                        null,
                        "aapt упадёт на этапе мерджа манифеста."));
                continue;
            }
            if (!seen.add(javaName)) {
                issues.add(new BlockLogicChecker.Issue(
                        null,
                        "Дубликат имени класса: " + javaName,
                        new ArrayList<>(),
                        BlockLogicChecker.Severity.CRITICAL,
                        screenLabel,
                        null,
                        "Два экрана ссылаются на один javaName — сборка упадёт."));
            }
        }
    }

    /** Невалидные имена классов в «Мой код» */
    private static void checkUserCodeClasses(Context ctx, String scId,
                                             List<BlockLogicChecker.Issue> issues) {
        try {
            UserCodeManager mgr = new UserCodeManager(scId);
            List<File> files = mgr.listFiles();
            if (files == null || files.isEmpty()) return;
            for (File f : files) {
                if (f == null) continue;
                String name = f.getName();
                if (name == null || name.isEmpty()) continue;
                String base = name.endsWith(".java")
                        ? name.substring(0, name.length() - 5) : name;
                if (base.isEmpty()) continue;
                if (!UserCodeManager.isValidClassName(base)) {
                    issues.add(new BlockLogicChecker.Issue(
                            null,
                            "Невалидное имя класса: " + name,
                            new ArrayList<>(),
                            BlockLogicChecker.Severity.CRITICAL,
                            null,
                            null,
                            "Имя должно начинаться с буквы, без пробелов и спецсимволов."));
                }
            }
        } catch (Throwable ignored) {}
    }
}
