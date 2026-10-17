package mod.jbk.util;

import android.content.Context;

import com.besome.sketch.beans.ProjectFileBean;

import java.io.File;
import java.util.List;

import a.a.a.jC;
import a.a.a.wq;
import pro.sketchware.activities.usercode.UserCodeManager;
import pro.sketchware.utility.FileUtil;

/**
 * Собирает общую статистику и «здоровье» проекта для отчёта.
 * Возвращает готовый текстовый отчёт.
 */
public final class ProjectHealthChecker {

    private ProjectHealthChecker() {}

    public static String buildReport(Context ctx, String scId, String projectName) {
        StringBuilder r = new StringBuilder();
        if (scId == null) return "Нет проекта";

        r.append("=== Диагностика проекта ===\n");
        if (projectName != null) r.append("Проект: ").append(projectName).append("\n");
        r.append("sc_id: ").append(scId).append("\n\n");

        // Экраны
        int activities = 0, layouts = 0, totalBlocks = 0, totalComponents = 0;
        int emptyActivities = 0, duplicateJavaNames = 0;
        try {
            List<ProjectFileBean> actList = jC.b(scId).b();
            if (actList != null) {
                activities = actList.size();
                java.util.HashSet<String> seen = new java.util.HashSet<>();
                for (ProjectFileBean pfb : actList) {
                    if (pfb == null) continue;
                    String jn = pfb.getJavaName();
                    if (jn == null || jn.isEmpty()) {
                        emptyActivities++;
                    } else if (!seen.add(jn)) {
                        duplicateJavaNames++;
                    }
                }
            }
        } catch (Throwable ignored) {}

        try {
            List<ProjectFileBean> others = jC.b(scId).c();
            if (others != null) layouts = others.size();
        } catch (Throwable ignored) {}

        // Блоки и компоненты по всем экранам
        try {
            List<ProjectFileBean> actList = jC.b(scId).b();
            if (actList != null) {
                for (ProjectFileBean pfb : actList) {
                    if (pfb == null) continue;
                    String jn = pfb.getJavaName();
                    if (jn == null || jn.isEmpty()) continue;
                    try {
                        java.util.HashMap<String, java.util.ArrayList<com.besome.sketch.beans.BlockBean>> evs =
                                jC.a(scId).b(jn);
                        if (evs != null) {
                            for (java.util.ArrayList<com.besome.sketch.beans.BlockBean> list : evs.values()) {
                                if (list != null) totalBlocks += list.size();
                            }
                        }
                    } catch (Throwable ignored) {}
                    try {
                        java.util.ArrayList<com.besome.sketch.beans.ComponentBean> comps =
                                jC.a(scId).e(jn);
                        if (comps != null) totalComponents += comps.size();
                    } catch (Throwable ignored) {}
                }
            }
        } catch (Throwable ignored) {}

        r.append("Экраны:\n");
        r.append("  Activity:       ").append(activities).append("\n");
        r.append("  Layout (другие): ").append(layouts).append("\n");
        r.append("  Блоков всего:   ").append(totalBlocks).append("\n");
        r.append("  Компонентов:    ").append(totalComponents).append("\n\n");

        // Проблемы
        int problems = 0;
        r.append("Проблемы:\n");
        if (emptyActivities > 0) {
            r.append("  🔴 Экранов без javaName: ").append(emptyActivities).append("\n");
            problems++;
        }
        if (duplicateJavaNames > 0) {
            r.append("  🔴 Дубликатов javaName: ").append(duplicateJavaNames).append("\n");
            problems++;
        }

        // Иконка
        try {
            String iconPath = wq.e() + File.separator + scId + File.separator + "icon.png";
            if (!FileUtil.isExistFile(iconPath)) {
                r.append("  🟡 Иконка приложения не задана\n");
                problems++;
            }
        } catch (Throwable ignored) {}

        // «Мой код»
        int userClasses = 0, badUserClasses = 0;
        try {
            UserCodeManager mgr = new UserCodeManager(scId);
            List<File> files = mgr.listFiles();
            if (files != null) {
                for (File f : files) {
                    if (f == null) continue;
                    userClasses++;
                    String name = f.getName();
                    if (name == null) continue;
                    String base = name.endsWith(".java") ? name.substring(0, name.length() - 5) : name;
                    if (base.isEmpty() || !UserCodeManager.isValidClassName(base)) badUserClasses++;
                }
            }
        } catch (Throwable ignored) {}
        r.append("  Классов в «Мой код»: ").append(userClasses);
        if (badUserClasses > 0) r.append(" (🔴 невалидных: ").append(badUserClasses).append(")");
        r.append("\n");

        if (problems == 0) {
            r.append("  ✅ Проблем не найдено\n");
        }
        r.append("\n");

        // Размер
        try {
            long bytes = dirSize(new File(wq.e() + File.separator + scId));
            r.append("Размер проекта: ").append(humanSize(bytes)).append("\n");
        } catch (Throwable ignored) {}

        return r.toString();
    }

    private static long dirSize(File dir) {
        if (dir == null || !dir.exists()) return 0;
        if (dir.isFile()) return dir.length();
        long total = 0;
        File[] kids = dir.listFiles();
        if (kids == null) return 0;
        for (File f : kids) total += dirSize(f);
        return total;
    }

    private static String humanSize(long b) {
        if (b < 1024) return b + " B";
        if (b < 1024 * 1024) return (b / 1024) + " KB";
        return String.format(java.util.Locale.US, "%.2f MB", b / (1024.0 * 1024.0));
    }
}
