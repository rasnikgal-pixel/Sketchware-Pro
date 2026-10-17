package mod.jbk.util;

import android.content.Context;

import com.besome.sketch.beans.ProjectFileBean;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

        return issues;
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
