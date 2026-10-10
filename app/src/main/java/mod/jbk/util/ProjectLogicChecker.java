package mod.jbk.util;


import mod.hey.studios.util.Helper;
import pro.sketchware.R;
import android.content.Context;

import com.besome.sketch.beans.BlockBean;
import com.besome.sketch.beans.ComponentBean;

import java.util.ArrayList;
import java.util.List;

import pro.sketchware.smartdrop.SmartDropHelper;

/**
 * Project-level logic checker.
 * Extends BlockLogicChecker with rules that require access to the project data
 * (existing components, variables, lists).
 *
 * <p>All methods are wrapped in try/catch so any failure is silent — the logic
 * editor must never crash because of a check.
 */
public final class ProjectLogicChecker {

    private ProjectLogicChecker() {}

    /**
     * Runs all checks: base rules + project-aware rules.
     * Never throws — returns whatever was collected.
     */
    public static List<BlockLogicChecker.Issue> check(List<BlockBean> blocks,
                                                       Context context,
                                                       String scId,
                                                       String javaName) {
        List<BlockLogicChecker.Issue> issues = new ArrayList<>();
        if (blocks == null || blocks.isEmpty()) return issues;

        // 1. Base rules (already implemented in BlockLogicChecker)
        try {
            List<BlockLogicChecker.Issue> base = BlockLogicChecker.check(blocks);
            if (base != null) issues.addAll(base);
        } catch (Throwable t) {
            // silent: base check failure must not affect the editor
        }

        // 2. Project-aware rules
        try {
            checkMissingComponents(blocks, context, scId, javaName, issues);
        } catch (Throwable t) {
            // silent
        }

        return issues;
    }

    /**
     * Rule: a block references a component (dialog / webview / timer / etc.)
     * that does not exist in the project.
     */
    private static void checkMissingComponents(List<BlockBean> blocks,
                                                 Context context,
                                                 String scId,
                                                 String javaName,
                                                 List<BlockLogicChecker.Issue> issues) {
        if (context == null || scId == null || javaName == null) return;
        ArrayList<BlockBean> arr = new ArrayList<>(blocks);
        ArrayList<ComponentBean> missing =
                SmartDropHelper.get(context).getUnresolvedComponents(arr, scId, javaName);
        if (missing == null || missing.isEmpty()) return;
        for (ComponentBean cb : missing) {
            if (cb == null) continue;
            String compId = cb.componentId == null ? "?" : cb.componentId;
            String typeName = cb.type > 0 ? String.valueOf(cb.type) : "?";
            issues.add(new BlockLogicChecker.Issue(
                    null,
                    Helper.getResString(R.string.auto_project_logic_checker_component_missing, compId, typeName),
                    new ArrayList<>(),
                    BlockLogicChecker.Severity.CRITICAL,
                    null,
                    null,
                    Helper.getResString(R.string.auto_project_logic_checker_001)));
        }
    }
}
