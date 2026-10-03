package mod.jbk.util;

import com.besome.sketch.beans.BlockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Checks a list of blocks in a single event for suspicious patterns.
 *
 * Current checks:
 *  1. Duplicates of sensitive opCodes (dialogSetTitle, dialogShow, ...).
 *  2. dialogDismiss without any dialogShow in the same event.
 *  3. dialogDismiss placed before the first dialogShow.
 *  4. dialogSetTitle / dialogSetMessage without dialogShow.
 */
public final class BlockLogicChecker {

    private static final String[] DUPLICATE_SENSITIVE = {
            "dialogSetTitle",
            "dialogSetMessage",
            "dialogShow",
            "dialogDismiss",
            "progressdialogShow",
            "progressdialogDismiss",
            "progressdialogCreate"
    };

    private BlockLogicChecker() {}

    /** A single problem found by the checker. */
    public static class Issue {
        public final String opCode;
        public final String message;
        public final List<String> duplicateBlockIds;

        public Issue(String opCode, String message, List<String> duplicateBlockIds) {
            this.opCode = opCode;
            this.message = message;
            this.duplicateBlockIds = duplicateBlockIds;
        }
    }

    public static List<Issue> check(List<BlockBean> blocks) {
        List<Issue> issues = new ArrayList<>();
        if (blocks == null || blocks.isEmpty()) return issues;

        // 1. Duplicate checks
        Map<String, List<String>> idGroups = new HashMap<>();
        for (BlockBean b : blocks) {
            if (b == null || b.opCode == null || b.id == null) continue;
            if (!isDuplicateSensitive(b.opCode)) continue;
            List<String> ids = idGroups.computeIfAbsent(b.opCode, k -> new ArrayList<>());
            ids.add(b.id);
        }
        for (Map.Entry<String, List<String>> e : idGroups.entrySet()) {
            List<String> ids = e.getValue();
            if (ids.size() > 1) {
                List<String> duplicatesToDelete = new ArrayList<>(ids.subList(1, ids.size()));
                issues.add(new Issue(e.getKey(), "Дубликат: " + e.getKey() + " × " + ids.size(), duplicatesToDelete));
            }
        }

        // 2. Presence / order checks
        checkPresenceAndOrder(blocks, issues);

        return issues;
    }

    private static void checkPresenceAndOrder(List<BlockBean> blocks, List<Issue> issues) {
        boolean hasShow = false;
        boolean hasDismiss = false;
        boolean hasSetTitle = false;
        boolean hasSetMessage = false;
        int firstShowIndex = -1;
        int firstDismissIndex = -1;
        List<String> dismissIdsBeforeShow = new ArrayList<>();

        for (int i = 0; i < blocks.size(); i++) {
            BlockBean b = blocks.get(i);
            if (b == null || b.opCode == null) continue;
            switch (b.opCode) {
                case "dialogShow":
                    if (!hasShow) {
                        hasShow = true;
                        firstShowIndex = i;
                    }
                    break;
                case "dialogDismiss":
                    if (!hasDismiss) {
                        hasDismiss = true;
                        firstDismissIndex = i;
                    }
                    if (!hasShow && b.id != null) {
                        dismissIdsBeforeShow.add(b.id);
                    }
                    break;
                case "dialogSetTitle":
                    hasSetTitle = true;
                    break;
                case "dialogSetMessage":
                    hasSetMessage = true;
                    break;
            }
        }

        // Rule: dialogSetTitle / dialogSetMessage without dialogShow
        if (hasSetTitle && !hasShow) {
            issues.add(new Issue("dialogSetTitle",
                    "dialogSetTitle без dialogShow — диалог не будет показан",
                    new ArrayList<>()));
        }
        if (hasSetMessage && !hasShow) {
            issues.add(new Issue("dialogSetMessage",
                    "dialogSetMessage без dialogShow — диалог не будет показан",
                    new ArrayList<>()));
        }

        // Rule: dialogDismiss without dialogShow
        if (hasDismiss && !hasShow) {
            issues.add(new Issue("dialogDismiss",
                    "dialogDismiss без dialogShow — диалог не показан, dismiss бесполезен",
                    new ArrayList<>()));
        }

        // Rule: dialogDismiss placed before dialogShow
        if (hasShow && hasDismiss && firstDismissIndex >= 0 && firstShowIndex >= 0
                && firstDismissIndex < firstShowIndex) {
            issues.add(new Issue("dialogDismiss",
                    "dialogDismiss стоит до dialogShow — dismiss сработает раньше показа",
                    dismissIdsBeforeShow));
        }
    }

    private static boolean isDuplicateSensitive(String opCode) {
        for (String s : DUPLICATE_SENSITIVE) {
            if (s.equals(opCode)) return true;
        }
        return false;
    }
}
