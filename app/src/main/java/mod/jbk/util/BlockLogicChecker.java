package mod.jbk.util;

import com.besome.sketch.beans.BlockBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Checks a list of blocks in a single event for suspicious patterns.
 *
 * Currently detects duplicate blocks that are usually meant to be unique.
 * Extend {@link #DUPLICATE_SENSITIVE} to add more blocks.
 */
public final class BlockLogicChecker {

    /** opCodes for which having more than one occurrence in an event is suspicious. */
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

    /** Returns a list of human-readable issues found in the block list. */
    public static List<String> check(List<BlockBean> blocks) {
        List<String> problems = new ArrayList<>();
        if (blocks == null || blocks.isEmpty()) return problems;

        Map<String, Integer> counts = new HashMap<>();
        for (BlockBean b : blocks) {
            if (b == null || b.opCode == null) continue;
            if (isDuplicateSensitive(b.opCode)) {
                Integer cur = counts.get(b.opCode);
                counts.put(b.opCode, cur == null ? 1 : cur + 1);
            }
        }

        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            if (e.getValue() > 1) {
                problems.add(e.getKey() + " × " + e.getValue());
            }
        }
        return problems;
    }

    private static boolean isDuplicateSensitive(String opCode) {
        for (String s : DUPLICATE_SENSITIVE) {
            if (s.equals(opCode)) return true;
        }
        return false;
    }
}
