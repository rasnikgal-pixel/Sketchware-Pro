package mod.jbk.util;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import pro.sketchware.utility.FileUtil;

/**
 * Manages block templates (ready-made block chains) stored in
 * /sdcard/.sketchware/block_templates.json
 *
 * Each template:
 *   id          — unique string (used as opCode suffix: "template_" + id)
 *   name        — display name in the palette
 *   description — optional description
 *   blocks      — array of blocks to insert, each:
 *                  type        — "s", "d", "b", " ", "c", ...
 *                  opCode      — the real opCode of the block
 *                  parameters  — array of String parameters (may be empty)
 *                  typeName    — optional type name for 4-arg blocks
 */
public final class BlockTemplatesManager {

    public static final int TEMPLATES_PALETTE_ID = 200;
    public static final int TEMPLATES_PALETTE_COLOR = 0xff7e57c2; // deep purple
    public static final String TEMPLATE_OPCODE_PREFIX = "template_";

    private static final String FILE_PATH = FileUtil.getExternalStorageDir()
            + "/.sketchware/block_templates.json";

    private BlockTemplatesManager() {}

    /** Returns all templates (seeds defaults on first run). */
    public static List<Map<String, Object>> getAll() {
        if (!FileUtil.isExistFile(FILE_PATH)) {
            seedDefaults();
        }
        try {
            String json = FileUtil.readFile(FILE_PATH);
            if (json == null || json.trim().isEmpty()) return new ArrayList<>();

            // JSON structure: {"templates":[...]}
            Map<String, Object> root = new Gson().fromJson(json,
                    new TypeToken<Map<String, Object>>() {}.getType());
            if (root == null) return new ArrayList<>();

            Object templatesObj = root.get("templates");
            if (!(templatesObj instanceof List)) return new ArrayList<>();

            List<Map<String, Object>> result = new ArrayList<>();
            for (Object item : (List<?>) templatesObj) {
                if (item instanceof Map) {
                    @SuppressWarnings("unchecked")
                    Map<String, Object> m = (Map<String, Object>) item;
                    result.add(m);
                }
            }
            return result;
        } catch (Throwable t) {
            return new ArrayList<>();
        }
    }

    /** Finds a template by id. Returns null if not found. */
    public static Map<String, Object> getById(String id) {
        if (id == null) return null;
        for (Map<String, Object> t : getAll()) {
            Object tid = t.get("id");
            if (id.equals(tid)) return t;
        }
        return null;
    }

    /** Extracts the template id from an opCode like "template_dialog_exit" → "dialog_exit". */
    public static String extractIdFromOpCode(String opCode) {
        if (opCode == null || !opCode.startsWith(TEMPLATE_OPCODE_PREFIX)) return null;
        return opCode.substring(TEMPLATE_OPCODE_PREFIX.length());
    }

    private static void seedDefaults() {
        // Read the default template JSON from assets/block_templates.json
        // This avoids messy escaping inside a Java string.
        try {
            android.content.Context ctx = getAppContext();
            if (ctx != null) {
                java.io.InputStream is = ctx.getAssets().open("block_templates.json");
                java.io.ByteArrayOutputStream baos = new java.io.ByteArrayOutputStream();
                byte[] buf = new byte[8192];
                int n;
                while ((n = is.read(buf)) > 0) {
                    baos.write(buf, 0, n);
                }
                is.close();
                String json = baos.toString("UTF-8");
                FileUtil.writeFile(FILE_PATH, json);
                return;
            }
        } catch (Throwable ignored) {}

        // Fallback: empty template list
        try {
            FileUtil.writeFile(FILE_PATH, "{\"templates\":[]}");
        } catch (Throwable ignored) {}
    }

    /** Returns an application Context if one is available, otherwise null. */
    private static android.content.Context cachedContext;
    public static void setContext(android.content.Context c) { cachedContext = c; }
    private static android.content.Context getAppContext() { return cachedContext; }

    /** Converts a template's "blocks" array to ArrayList<BlockBean> with chained nextBlock. */
    public static java.util.ArrayList<com.besome.sketch.beans.BlockBean> toBlockBeans(
            java.util.Map<String, Object> template) {
        java.util.ArrayList<com.besome.sketch.beans.BlockBean> result = new java.util.ArrayList<>();
        if (template == null) return result;

        Object blocksObj = template.get("blocks");
        if (!(blocksObj instanceof java.util.List)) return result;
        java.util.List<?> blockDefs = (java.util.List<?>) blocksObj;

        // Собираем все блоки рекурсивно (с subStack1/subStack2) в один плоский список,
        // но с правильными связями nextBlock/subStack1/subStack2 между ними.
        int startId = (int) (System.currentTimeMillis() % 100000) * 100;
        final int[] nextId = { startId };
        java.util.Map<Object, Integer> idMap = new java.util.HashMap<>();
        java.util.List<com.besome.sketch.beans.BlockBean> allBeans = new java.util.ArrayList<>();

        // Первый проход — создать BlockBean-и и запомнить их ID по исходному объекту JSON
        // Второй проход — установить связи.
        java.util.List<Object[]> pendingLinks = new java.util.ArrayList<>();

        for (Object item : blockDefs) {
            collectBlocksRecursive(item, idMap, allBeans, nextId, pendingLinks);
        }

        // Второй проход: установить связи
        for (Object[] pair : pendingLinks) {
            com.besome.sketch.beans.BlockBean bean = (com.besome.sketch.beans.BlockBean) pair[0];
            Object def = pair[1];
            java.util.Map<?, ?> bdef = (java.util.Map<?, ?>) def;

            Object sub1 = bdef.get("subStack1");
            if (sub1 instanceof java.util.Map) {
                Integer id = idMap.get(sub1);
                if (id != null) bean.subStack1 = id;
            }
            Object sub2 = bdef.get("subStack2");
            if (sub2 instanceof java.util.Map) {
                Integer id = idMap.get(sub2);
                if (id != null) bean.subStack2 = id;
            }
            Object next = bdef.get("nextBlock");
            if (next instanceof java.util.Map) {
                Integer id = idMap.get(next);
                if (id != null) bean.nextBlock = id;
            }
        }

        // Automatic chaining: link top-level sibling blocks via nextBlock
        // (do not touch subStack1/subStack2 — they were set above).
        {
            Integer prevId = null;
            for (Object item : blockDefs) {
                if (!(item instanceof java.util.Map)) continue;
                Integer id = idMap.get(item);
                if (id == null) continue;
                if (prevId != null) {
                    for (com.besome.sketch.beans.BlockBean b : allBeans) {
                        try {
                            if (b.id != null && Integer.parseInt(b.id) == prevId) {
                                b.nextBlock = id;
                                break;
                            }
                        } catch (Throwable ignored) {}
                    }
                }
                prevId = id;
            }
        }


        result.addAll(allBeans);
        return result;
    }

    private static void collectBlocksRecursive(
            Object def,
            java.util.Map<Object, Integer> idMap,
            java.util.List<com.besome.sketch.beans.BlockBean> out,
            int[] nextId,
            java.util.List<Object[]> pendingLinks) {
        if (!(def instanceof java.util.Map)) return;
        java.util.Map<?, ?> bdef = (java.util.Map<?, ?>) def;

        String type = String.valueOf(bdef.get("type"));
        String opCode = String.valueOf(bdef.get("opCode"));

        com.besome.sketch.beans.BlockBean bean = new com.besome.sketch.beans.BlockBean();
        int myId = nextId[0]++;
        bean.id = String.valueOf(myId);
        bean.opCode = opCode == null ? "" : opCode;
        bean.type = type == null ? " " : type;
        bean.typeName = "";
        bean.spec = " ";
        bean.color = 0;
        bean.parameters = new java.util.ArrayList<>();
        Object paramsObj = bdef.get("parameters");
        if (paramsObj instanceof java.util.List) {
            for (Object p : (java.util.List<?>) paramsObj) {
                bean.parameters.add(String.valueOf(p));
            }
        }
        Object specObj = bdef.get("spec");
        if (specObj instanceof String && !((String) specObj).isEmpty()) {
            bean.spec = (String) specObj;
        }

        idMap.put(def, myId);
        out.add(bean);
        pendingLinks.add(new Object[] { bean, def });

        // Рекурсивно обходим subStack1/subStack2
        Object sub1 = bdef.get("subStack1");
        if (sub1 instanceof java.util.Map) {
            collectBlocksRecursive(sub1, idMap, out, nextId, pendingLinks);
        }
        Object sub2 = bdef.get("subStack2");
        if (sub2 instanceof java.util.Map) {
            collectBlocksRecursive(sub2, idMap, out, nextId, pendingLinks);
        }
        // nextBlock в JSON-описании обычно не используется: связи "по цепочке" 
        // задаются автоматически через subStack-структуру, но можно указать явно.
    }
}
