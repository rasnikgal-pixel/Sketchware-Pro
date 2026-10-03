package mod.jbk.util;

import com.besome.sketch.beans.BlockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Checks a list of blocks in a single event for suspicious patterns.
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
                issues.add(new Issue(e.getKey(), e.getKey() + " × " + ids.size(), duplicatesToDelete));
            }
        }
        return issues;
    }

    private static boolean isDuplicateSensitive(String opCode) {
        for (String s : DUPLICATE_SENSITIVE) {
            if (s.equals(opCode)) return true;
        }
        return false;
    }
}
