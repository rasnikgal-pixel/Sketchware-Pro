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
        String json = """{
  "templates": [
    {
      "id": "dialog_ok",
      "name": "📦 Диалог OK",
      "description": "Простой диалог с одной кнопкой OK. Требует компонент Dialog с именем DlgExt.",
      "blocks": [
        {"type":" ","opCode":"dialogSetTitle","spec":"%m.dialog setTitle %s","parameters":["DlgExt","\\\"Заголовок\\\""]},
        {"type":" ","opCode":"dialogSetMessage","spec":"%m.dialog setMessage %s","parameters":["DlgExt","\\\"Сообщение\\\""]},
        {"type":"c","opCode":"dialogOkButton","spec":"%m.dialog OK Button %s Clicked","parameters":["DlgExt","\\\"OK\\\""]},
        {"type":" ","opCode":"dialogShow","spec":"%m.dialog show","parameters":["DlgExt"]}
      ]
    },
    {
      "id": "dialog_confirm",
      "name": "📦 Диалог подтверждения",
      "description": "Диалог с кнопками OK и Cancel. Требует DlgExt.",
      "blocks": [
        {"type":" ","opCode":"dialogSetTitle","spec":"%m.dialog setTitle %s","parameters":["DlgExt","\\\"Подтверждение\\\""]},
        {"type":" ","opCode":"dialogSetMessage","spec":"%m.dialog setMessage %s","parameters":["DlgExt","\\\"Вы уверены?\\\""]},
        {"type":"c","opCode":"dialogOkButton","spec":"%m.dialog OK Button %s Clicked","parameters":["DlgExt","\\\"Да\\\""]},
        {"type":"c","opCode":"dialogCancelButton","spec":"%m.dialog Cancel Button %s Clicked","parameters":["DlgExt","\\\"Отмена\\\""]},
        {"type":" ","opCode":"dialogShow","spec":"%m.dialog show","parameters":["DlgExt"]}
      ]
    },
    {
      "id": "dialog_3buttons",
      "name": "📦 Диалог с 3 кнопками",
      "description": "Диалог с OK, Cancel и Neutral. Требует DlgExt.",
      "blocks": [
        {"type":" ","opCode":"dialogSetTitle","spec":"%m.dialog setTitle %s","parameters":["DlgExt","\\\"Выбор\\\""]},
        {"type":" ","opCode":"dialogSetMessage","spec":"%m.dialog setMessage %s","parameters":["DlgExt","\\\"Выберите действие\\\""]},
        {"type":"c","opCode":"dialogOkButton","spec":"%m.dialog OK Button %s Clicked","parameters":["DlgExt","\\\"Да\\\""]},
        {"type":"c","opCode":"dialogCancelButton","spec":"%m.dialog Cancel Button %s Clicked","parameters":["DlgExt","\\\"Отмена\\\""]},
        {"type":"c","opCode":"dialogNeutralButton","spec":"%m.dialog Neutral Button %s Clicked","parameters":["DlgExt","\\\"Позже\\\""]},
        {"type":" ","opCode":"dialogShow","spec":"%m.dialog show","parameters":["DlgExt"]}
      ]
    },
    {
      "id": "dialog_dismiss",
      "name": "📦 Диалог с dismiss",
      "description": "Простой диалог с закрытием. Требует DlgExt.",
      "blocks": [
        {"type":" ","opCode":"dialogSetTitle","spec":"%m.dialog setTitle %s","parameters":["DlgExt","\\\"Внимание\\\""]},
        {"type":" ","opCode":"dialogSetMessage","spec":"%m.dialog setMessage %s","parameters":["DlgExt","\\\"Сообщение\\\""]},
        {"type":" ","opCode":"dialogDismiss","spec":"%m.dialog dismiss","parameters":["DlgExt"]},
        {"type":" ","opCode":"dialogShow","spec":"%m.dialog show","parameters":["DlgExt"]}
      ]
    },
    {
      "id": "dialog_exit_app",
      "name": "📦 Диалог выхода из приложения",
      "description": "Подтверждение выхода. Да — завершить приложение, Остаться — закрыть диалог. Требует DlgExt.",
      "blocks": [
        {"type":" ","opCode":"dialogSetTitle","spec":"%m.dialog setTitle %s","parameters":["DlgExt","\\\"Выход\\\""]},
        {"type":" ","opCode":"dialogSetMessage","spec":"%m.dialog setMessage %s","parameters":["DlgExt","\\\"Вы уверены, что хотите выйти?\\\""]},
        {"type":"c","opCode":"dialogOkButton","spec":"%m.dialog OK Button %s Clicked","parameters":["DlgExt","\\\"Да, выйти\\\""],"subStack1":{"type":"f","opCode":"finishActivity","spec":"Finish Activity","parameters":[]}},
        {"type":"c","opCode":"dialogCancelButton","spec":"%m.dialog Cancel Button %s Clicked","parameters":["DlgExt","\\\"Остаться\\\""],"subStack1":{"type":" ","opCode":"dialogDismiss","spec":"%m.dialog dismiss","parameters":["DlgExt"]}},
        {"type":" ","opCode":"dialogShow","spec":"%m.dialog show","parameters":["DlgExt"]}
      ]
    },
    {
      "id": "progressdialog_simple",
      "name": "📦 Прогресс-диалог",
      "description": "Create + setTitle + setMessage + show. Требует компонент ProgressDialog с именем PDlg.",
      "blocks": [
        {"type":" ","opCode":"progressdialogCreate","spec":"%m.progressdialog Create in %m.activity","parameters":["PDlg","MainActivity.this"]},
        {"type":" ","opCode":"progressdialogSetTitle","spec":"%m.progressdialog setTitle %s","parameters":["PDlg","\\\"Загрузка\\\""]},
        {"type":" ","opCode":"progressdialogSetMessage","spec":"%m.progressdialog setMessage %s","parameters":["PDlg","\\\"Пожалуйста, подождите...\\\""]},
        {"type":" ","opCode":"progressdialogShow","spec":"%m.progressdialog show","parameters":["PDlg"]}
      ]
    },
    {
      "id": "progressdialog_dismiss",
      "name": "📦 Закрыть прогресс-диалог",
      "description": "Dismiss прогресс-диалога. Требует PDlg.",
      "blocks": [
        {"type":" ","opCode":"progressdialogDismiss","spec":"%m.progressdialog dismiss","parameters":["PDlg"]}
      ]
    },
    {
      "id": "finish_activity",
      "name": "📦 Завершить Activity",
      "description": "Завершает текущую Activity (finish).",
      "blocks": [
        {"type":"f","opCode":"finishActivity","spec":"Finish Activity","parameters":[]}
      ]
    },
    {
      "id": "finish_affinity",
      "name": "📦 Завершить приложение",
      "description": "Полное завершение приложения (finishAffinity).",
      "blocks": [
        {"type":"f","opCode":"finishAffinity","spec":"Finish Affinity","parameters":[]}
      ]
    },
    {
      "id": "custom_toast",
      "name": "📦 Всплывающее сообщение (Toast)",
      "description": "Показывает Toast-сообщение.",
      "blocks": [
        {"type":" ","opCode":"customToast","spec":"CustomToast %s textColor %m.color textSize %d bgColor %m.color cornerRadius %d gravity %m.gravity_t","parameters":["\\\"Сообщение\\\"","\\\"#FFFFFF\\\"","14","\\\"#000000\\\"","8","BOTTOM"]}
      ]
    }
  ]
}""";
        try {
            FileUtil.writeFile(FILE_PATH, json);
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
