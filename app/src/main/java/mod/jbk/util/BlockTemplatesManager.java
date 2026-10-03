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
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"templates\": [\n");
        sb.append("    {\n");
        sb.append("      \"id\": \"dialog_exit\",\n");
        sb.append("      \"name\": \"📦 Диалог выхода из приложения\",\n");
        sb.append("      \"description\": \"Готовый диалог подтверждения выхода с OK и Cancel\",\n");
        sb.append("      \"blocks\": [\n");
        sb.append("        {\"type\":\" \",\"opCode\":\"dialogSetTitle\",\"spec\":\"%m.dialog setTitle %s\",\"parameters\":[\"\\\"Выход\\\"\"]},\n");
        sb.append("        {\"type\":\" \",\"opCode\":\"dialogSetMessage\",\"spec\":\"%m.dialog setMessage %s\",\"parameters\":[\"\\\"Вы уверены, что хотите выйти?\\\"\"]},\n");
        sb.append("        {\"type\":\" \",\"opCode\":\"dialogOkButton\",\"spec\":\"%m.dialog OK Button %s Clicked\",\"parameters\":[\"\\\"Выйти\\\"\",\"\"]},\n");
        sb.append("        {\"type\":\" \",\"opCode\":\"dialogCancelButton\",\"spec\":\"%m.dialog Cancel Button %s Clicked\",\"parameters\":[\"\\\"Остаться\\\"\",\"\"]},\n");
        sb.append("        {\"type\":\" \",\"opCode\":\"dialogShow\",\"spec\":\"%m.dialog show\",\"parameters\":[\"DlgExt\"]}\n");
        sb.append("      ]\n");
        sb.append("    }\n");
        sb.append("  ]\n");
        sb.append("}\n");
        try {
            FileUtil.writeFile(FILE_PATH, sb.toString());
        } catch (Throwable ignored) {}
    }

    /** Converts a template's "blocks" array to ArrayList<BlockBean> with chained nextBlock. */
    public static java.util.ArrayList<com.besome.sketch.beans.BlockBean> toBlockBeans(
            java.util.Map<String, Object> template) {
        java.util.ArrayList<com.besome.sketch.beans.BlockBean> result = new java.util.ArrayList<>();
        if (template == null) return result;

        Object blocksObj = template.get("blocks");
        if (!(blocksObj instanceof java.util.List)) return result;
        java.util.List<?> blockDefs = (java.util.List<?>) blocksObj;

        // Use a small, unique base for block IDs — large millis values may
        // overflow nextBlock (int) or clash with existing IDs.
        int startId = (int) (System.currentTimeMillis() % 100000) * 100;
        for (int i = 0; i < blockDefs.size(); i++) {
            Object item = blockDefs.get(i);
            if (!(item instanceof java.util.Map)) continue;
            java.util.Map<?, ?> bdef = (java.util.Map<?, ?>) item;

            String type = String.valueOf(bdef.get("type"));
            String opCode = String.valueOf(bdef.get("opCode"));

            com.besome.sketch.beans.BlockBean bean = new com.besome.sketch.beans.BlockBean();
            bean.id = String.valueOf(startId + i);
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
            if (i < blockDefs.size() - 1) {
                bean.nextBlock = startId + i + 1;
            } else {
                bean.nextBlock = -1;
            }
            bean.subStack1 = -1;
            bean.subStack2 = -1;
            result.add(bean);
        }
        return result;
    }
}
