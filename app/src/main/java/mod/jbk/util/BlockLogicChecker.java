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

    /** Severity level of a detected issue. */
    public enum Severity {
        /** Critical: project likely will not compile or will hang. */
        CRITICAL,
        /** Warning: may work incorrectly, review recommended. */
        WARNING
    }

    /** A single problem found by the checker. */
    public static class Issue {
        public final String opCode;
        public final String message;
        public final List<String> duplicateBlockIds;
        /** Severity of the issue. Defaults to WARNING for backward compatibility. */
        public final Severity severity;
        /** Id of the offending block (may be null if not applicable). */
        public final String blockId;
        /** Name of the event where the issue was found (may be null). */
        public final String eventName;
        /** Human-readable location, e.g. Russian text. */
        public final String humanLocation;

        /** Backward-compatible constructor: severity defaults to WARNING. */
        public Issue(String opCode, String message, List<String> duplicateBlockIds) {
            this(opCode, message, duplicateBlockIds, Severity.WARNING, null, null, null);
        }

        /** Full constructor. */
        public Issue(String opCode, String message, List<String> duplicateBlockIds,
                     Severity severity, String blockId, String eventName, String humanLocation) {
            this.opCode = opCode;
            this.message = message;
            this.duplicateBlockIds = duplicateBlockIds == null ? new ArrayList<>() : duplicateBlockIds;
            this.severity = severity == null ? Severity.WARNING : severity;
            this.blockId = blockId;
            this.eventName = eventName;
            this.humanLocation = humanLocation;
        }

        /** Emoji prefix for UI: red for critical, yellow for warning. */
        public String emoji() {
            return severity == Severity.CRITICAL ? "\uD83D\uDD34" : "\uD83D\uDFE1";
        }

        /** Human-readable line for dialogs and journals. */
        public String toDisplayString() {
            StringBuilder sb = new StringBuilder();
            sb.append(emoji()).append(" ").append(message);
            if (humanLocation != null && !humanLocation.isEmpty()) {
                sb.append(" (").append(humanLocation).append(")");
            }
            return sb.toString();
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
                issues.add(new Issue(e.getKey(),
                        "Дубликат: " + e.getKey() + " × " + ids.size(),
                        duplicatesToDelete,
                        Severity.WARNING,
                        ids.isEmpty() ? null : ids.get(0),
                        null,
                        "Дубликат блока"));
            }
        }

        // 2. Presence / order checks
        checkPresenceAndOrder(blocks, issues);

        // 3. Structural checks on individual blocks
        checkEmptyConditions(blocks, issues);
        checkTrivialConditions(blocks, issues);
        checkUnreachableBlocks(blocks, issues);

        return issues;
    }

    /**
     * Rule: blocks that are not connected to any event root.
     * We build a graph via nextBlock / subStack1 / subStack2, collect all ids
     * reachable from any root (block nobody points to), and flag the rest.
     */
    private static void checkUnreachableBlocks(List<BlockBean> blocks, List<Issue> issues) {
        if (blocks == null || blocks.size() < 2) return;
        Map<String, BlockBean> byId = new HashMap<>();
        for (BlockBean b : blocks) {
            if (b != null && b.id != null) byId.put(b.id, b);
        }
        if (byId.isEmpty()) return;
        Set<String> pointedTo = new HashSet<>();
        for (BlockBean b : blocks) {
            if (b == null) continue;
            if (b.nextBlock > 0) {
                BlockBean n = byId.get(String.valueOf(b.nextBlock));
                if (n == null) {
                    for (BlockBean cand : blocks) {
                        if (cand != null && cand.id != null && cand.id.equals(String.valueOf(b.nextBlock))) {
                            pointedTo.add(cand.id);
                            break;
                        }
                    }
                } else {
                    pointedTo.add(n.id);
                }
            }
            if (b.subStack1 > 0) pointedTo.add(String.valueOf(b.subStack1));
            if (b.subStack2 > 0) pointedTo.add(String.valueOf(b.subStack2));
        }
        java.util.Deque<String> stack = new java.util.ArrayDeque<>();
        Set<String> visited = new HashSet<>();
        for (String id : byId.keySet()) {
            if (!pointedTo.contains(id)) stack.push(id);
        }
        if (stack.isEmpty()) {
            for (String id : byId.keySet()) stack.push(id);
        }
        while (!stack.isEmpty()) {
            String id = stack.pop();
            if (!visited.add(id)) continue;
            BlockBean b = byId.get(id);
            if (b == null) continue;
            if (b.nextBlock > 0) stack.push(String.valueOf(b.nextBlock));
            if (b.subStack1 > 0) stack.push(String.valueOf(b.subStack1));
            if (b.subStack2 > 0) stack.push(String.valueOf(b.subStack2));
            if (b.parameters != null) {
                for (String p : b.parameters) {
                    if (p == null) continue;
                    String t = p.trim();
                    if (t.startsWith("@") && t.length() > 1) stack.push(t.substring(1));
                }
            }
        }
        for (BlockBean b : blocks) {
            if (b == null || b.id == null) continue;
            if (visited.contains(b.id)) continue;
            issues.add(new Issue(
                    b.opCode,
                    "Блок " + b.opCode + " не подключён к событию (висит отдельно)",
                    new ArrayList<>(),
                    Severity.WARNING,
                    b.id,
                    null,
                    "Недостижимый блок"));
        }
    }

    /**
     * Rule: if / ifElse / while with a trivial constant condition (true/false).
     * Works but is almost always a mistake — code becomes dead or always-on.
     */
    private static void checkTrivialConditions(List<BlockBean> blocks, List<Issue> issues) {
        for (BlockBean b : blocks) {
            if (b == null || b.opCode == null) continue;
            String op = b.opCode;
            boolean isConditional = "if".equals(op) || "ifElse".equals(op) || "while".equals(op);
            if (!isConditional) continue;
            if (b.parameters == null || b.parameters.isEmpty()) continue;
            String condition = b.parameters.get(0);
            if (condition == null) continue;
            String c = condition.trim();
            if ("true".equals(c) || "false".equals(c)) {
                issues.add(new Issue(
                        op,
                        "Тривиальное условие " + c + " в блоке " + op + " — всегда " + c,
                        new ArrayList<>(),
                        Severity.WARNING,
                        b.id,
                        null,
                        "Условие " + c + " в " + op));
            }
        }
    }

    /**
     * Rule: if / ifElse / while with empty or missing condition parameter.
     * This is the leading cause of Sketchware hanging when dragging such a block.
     */
    private static void checkEmptyConditions(List<BlockBean> blocks, List<Issue> issues) {
        for (BlockBean b : blocks) {
            if (b == null || b.opCode == null) continue;
            String op = b.opCode;
            boolean isConditional = "if".equals(op) || "ifElse".equals(op) || "while".equals(op);
            if (!isConditional) continue;
            String condition = null;
            if (b.parameters != null && !b.parameters.isEmpty()) {
                condition = b.parameters.get(0);
            }
            if (condition == null || condition.trim().isEmpty()) {
                issues.add(new Issue(
                        op,
                        "Пустое условие в блоке " + op + " — может привести к зависанию",
                        new ArrayList<>(),
                        Severity.CRITICAL,
                        b.id,
                        null,
                        "Пустое условие в " + op));
            }
        }
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
                    new ArrayList<>(),
                    Severity.WARNING,
                    null,
                    null,
                    "dialogSetTitle вне dialogShow"));
        }
        if (hasSetMessage && !hasShow) {
            issues.add(new Issue("dialogSetMessage",
                    "dialogSetMessage без dialogShow — диалог не будет показан",
                    new ArrayList<>(),
                    Severity.WARNING,
                    null,
                    null,
                    "dialogSetMessage вне dialogShow"));
        }

        // Rule: dialogDismiss without dialogShow
        if (hasDismiss && !hasShow) {
            issues.add(new Issue("dialogDismiss",
                    "dialogDismiss без dialogShow — диалог не показан, dismiss бесполезен",
                    new ArrayList<>(),
                    Severity.WARNING,
                    null,
                    null,
                    "dialogDismiss без показа"));
        }

        // Rule: dialogDismiss placed before dialogShow
        if (hasShow && hasDismiss && firstDismissIndex >= 0 && firstShowIndex >= 0
                && firstDismissIndex < firstShowIndex) {
            issues.add(new Issue("dialogDismiss",
                    "dialogDismiss стоит до dialogShow — dismiss сработает раньше показа",
                    dismissIdsBeforeShow,
                    Severity.WARNING,
                    dismissIdsBeforeShow.isEmpty() ? null : dismissIdsBeforeShow.get(0),
                    null,
                    "Порядок блоков диалога"));
        }
    }

    private static boolean isDuplicateSensitive(String opCode) {
        for (String s : DUPLICATE_SENSITIVE) {
            if (s.equals(opCode)) return true;
        }
        return false;
    }
}
