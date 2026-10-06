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

    /** Отдельный файл для пользовательских сборок. */
    private static final String CUSTOM_FILE_PATH = FileUtil.getExternalStorageDir()
            + "/.sketchware/custom_block_templates.json";

    /** Префикс id для пользовательских сборок. */
    public static final String CUSTOM_ID_PREFIX = "custom_";

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

    /** Finds a template by id (ищет и в builtin, и в custom). Returns null if not found. */
    public static Map<String, Object> getById(String id) {
        if (id == null) return null;
        for (Map<String, Object> t : getAll()) {
            Object tid = t.get("id");
            if (id.equals(tid)) return t;
        }
        for (Map<String, Object> t : getCustom()) {
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
        java.util.List<?> blockDefs;
        if (blocksObj instanceof java.util.List) {
            blockDefs = (java.util.List<?>) blocksObj;
        } else if (blocksObj instanceof java.util.Map) {
            // Пользовательский шаблон: blocks — одиночный объект с вложенными nextBlock/subStack.
            // Оборачиваем в одноэлементный список — дальше collectBlocksRecursive рекурсивно обойдёт всё.
            java.util.List<Object> one = new java.util.ArrayList<>();
            one.add(blocksObj);
            blockDefs = one;
        } else {
            return result;
        }

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
        boolean hasExplicitNext = false;
        for (Object item : blockDefs) {
            if (item instanceof java.util.Map) {
                Object nb = ((java.util.Map<?, ?>) item).get("nextBlock");
                if (nb instanceof java.util.Map) {
                    hasExplicitNext = true;
                    break;
                }
            }
        }
        if (!hasExplicitNext) {
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
        Object nextB = bdef.get("nextBlock");
        if (nextB instanceof java.util.Map) {
            collectBlocksRecursive(nextB, idMap, out, nextId, pendingLinks);
        }
        // nextBlock в JSON-описании обычно не используется: связи "по цепочке" 
        // задаются автоматически через subStack-структуру, но можно указать явно.
    }


    /** Возвращает только встроенные сборки. */
    public static List<Map<String, Object>> getBuiltin() {
        return getAll();
    }

    /** Возвращает только пользовательские сборки. */
    public static List<Map<String, Object>> getCustom() {
        try {
            if (!FileUtil.isExistFile(CUSTOM_FILE_PATH)) {
                return new ArrayList<>();
            }
            String json = FileUtil.readFile(CUSTOM_FILE_PATH);
            if (json == null || json.trim().isEmpty()) return new ArrayList<>();
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

    /** Объединённый список: встроенные + пользовательские. */
    public static List<Map<String, Object>> getAllCombined() {
        List<Map<String, Object>> result = new ArrayList<>();
        result.addAll(getBuiltin());
        result.addAll(getCustom());
        return result;
    }

    /** Проверяет, что id принадлежит пользовательской сборке. */
    public static boolean isCustom(String id) {
        if (id == null) return false;
        if (!id.startsWith(CUSTOM_ID_PREFIX)) return false;
        for (Map<String, Object> t : getCustom()) {
            Object tid = t.get("id");
            if (id.equals(tid)) return true;
        }
        return false;
    }

    /** Генерирует уникальный id для пользовательской сборки. */
    public static String generateId(String name) {
        String base = CUSTOM_ID_PREFIX + System.currentTimeMillis();
        int suffix = 0;
        String candidate = base;
        while (getById(candidate) != null) {
            suffix++;
            candidate = base + "_" + suffix;
        }
        return candidate;
    }

    /** Сохраняет или обновляет пользовательскую сборку. */
    public static boolean saveCustomTemplate(String id, String name, String description, Object blocksTree) {
        if (id == null || name == null) return false;
        if (!id.startsWith(CUSTOM_ID_PREFIX)) {
            id = CUSTOM_ID_PREFIX + id;
        }
        List<Map<String, Object>> custom = getCustom();
        for (int i = custom.size() - 1; i >= 0; i--) {
            Object tid = custom.get(i).get("id");
            if (id.equals(tid)) {
                custom.remove(i);
            }
        }
        Map<String, Object> entry = new LinkedHashMap<>();
        entry.put("id", id);
        entry.put("name", name);
        entry.put("description", description == null ? "" : description);
        entry.put("isCustom", true);
        entry.put("blocks", blocksTree);
        custom.add(entry);
        return writeCustomFile(custom);
    }

    /** Удаляет пользовательскую сборку. */
    public static boolean deleteCustomTemplate(String id) {
        if (id == null) return false;
        List<Map<String, Object>> custom = getCustom();
        boolean removed = false;
        for (int i = custom.size() - 1; i >= 0; i--) {
            Object tid = custom.get(i).get("id");
            if (id.equals(tid)) {
                custom.remove(i);
                removed = true;
            }
        }
        if (!removed) return false;
        return writeCustomFile(custom);
    }

    /** Обновляет имя и/или описание пользовательской сборки. */
    public static boolean updateCustomTemplate(String id, String newName, String newDescription) {
        if (id == null) return false;
        List<Map<String, Object>> custom = getCustom();
        boolean found = false;
        for (Map<String, Object> t : custom) {
            Object tid = t.get("id");
            if (id.equals(tid)) {
                if (newName != null && !newName.trim().isEmpty()) {
                    t.put("name", newName);
                }
                if (newDescription != null) {
                    t.put("description", newDescription);
                }
                found = true;
                break;
            }
        }
        if (!found) return false;
        return writeCustomFile(custom);
    }

    /** Сырой JSON пользовательских сборок (для экспорта). */
    public static String getCustomRawJson() {
        try {
            if (!FileUtil.isExistFile(CUSTOM_FILE_PATH)) {
                return emptyTemplatesJson();
            }
            String json = FileUtil.readFile(CUSTOM_FILE_PATH);
            if (json == null || json.trim().isEmpty()) {
                return emptyTemplatesJson();
            }
            return json;
        } catch (Throwable t) {
            return emptyTemplatesJson();
        }
    }

    private static String emptyTemplatesJson() {
        Map<String, Object> root = new LinkedHashMap<>();
        root.put("templates", new ArrayList<>());
        return new Gson().toJson(root);
    }

    /** Записывает сырой JSON (для импорта). */
    public static boolean setCustomRawJson(String json) {
        if (json == null) return false;
        try {
            FileUtil.writeFile(CUSTOM_FILE_PATH, json);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /** Удаляет все пользовательские сборки. */
    public static boolean deleteAllCustom() {
        try {
            FileUtil.writeFile(CUSTOM_FILE_PATH, "{\"templates\":[]}");
            return true;
        } catch (Throwable t) {
            return false;
        }
    }

    /** Внутренний метод: пишет список в файл. */
    private static boolean writeCustomFile(List<Map<String, Object>> custom) {
        try {
            Map<String, Object> root = new LinkedHashMap<>();
            root.put("templates", custom);
            String json = new Gson().toJson(root);
            FileUtil.writeFile(CUSTOM_FILE_PATH, json);
            return true;
        } catch (Throwable t) {
            return false;
        }
    }


    // ============ Импорт / слияние / замена ============

    /** Результат импорта: какие сборки добавлены/заменены/пропущены. */
    public static final class ImportResult {
        public final java.util.List<String> addedNames = new ArrayList<>();
        public final java.util.List<String> replacedNames = new ArrayList<>();
        public final java.util.List<String> loadedNames = new ArrayList<>();
        public int skippedCount = 0;
        public int removedCount = 0;
        public int existingCount = 0;
    }

    /** Разбирает входящий JSON, возвращает список валидных шаблонов. */
    @SuppressWarnings("unchecked")
    private static List<Map<String, Object>> parseIncomingTemplates(String json) {
        List<Map<String, Object>> result = new ArrayList<>();
        if (json == null || json.trim().isEmpty()) return result;
        try {
            Map<String, Object> root = new Gson().fromJson(json, Map.class);
            if (root == null) return result;
            Object templatesObj = root.get("templates");
            if (templatesObj instanceof List) {
                for (Object item : (List<?>) templatesObj) {
                    if (item instanceof Map) {
                        Map<String, Object> t = (Map<String, Object>) item;
                        Object id = t.get("id");
                        Object blocks = t.get("blocks");
                        if (id == null || blocks == null) continue;
                        result.add(t);
                    }
                }
            } else if (templatesObj instanceof Map) {
                Map<String, Object> t = (Map<String, Object>) templatesObj;
                Object id = t.get("id");
                Object blocks = t.get("blocks");
                if (id != null && blocks != null) result.add(t);
            }
        } catch (Throwable ignored) {
        }
        return result;
    }

    /** Предпросмотр слияния: что добавится, что заменится, что пропустится. */
    public static ImportResult previewImport(String json) {
        ImportResult r = new ImportResult();
        List<Map<String, Object>> current = getCustom();
        r.existingCount = current.size();
        List<Map<String, Object>> incoming = parseIncomingTemplates(json);
        for (Map<String, Object> t : incoming) {
            String id = String.valueOf(t.get("id"));
            String name = t.get("name") == null ? id : String.valueOf(t.get("name"));
            boolean exists = false;
            for (Map<String, Object> c : current) {
                if (id.equals(String.valueOf(c.get("id")))) { exists = true; break; }
            }
            if (exists) r.replacedNames.add(name);
            else r.addedNames.add(name);
        }
        return r;
    }

    /** Слияние: новые добавляются, совпадающие по id — заменяются. */
    public static ImportResult mergeCustomRawJson(String json) {
        ImportResult r = new ImportResult();
        List<Map<String, Object>> current = getCustom();
        r.existingCount = current.size();
        List<Map<String, Object>> incoming = parseIncomingTemplates(json);
        int totalInFile = 0;
        try {
            Map<String, Object> root = new Gson().fromJson(json, Map.class);
            if (root != null) {
                Object to = root.get("templates");
                if (to instanceof List) totalInFile = ((List<?>) to).size();
                else if (to instanceof Map) totalInFile = 1;
            }
        } catch (Throwable ignored) {}
        r.skippedCount = Math.max(0, totalInFile - incoming.size());

        for (Map<String, Object> t : incoming) {
            String id = String.valueOf(t.get("id"));
            String name = t.get("name") == null ? id : String.valueOf(t.get("name"));
            int found = -1;
            for (int i = 0; i < current.size(); i++) {
                if (id.equals(String.valueOf(current.get(i).get("id")))) { found = i; break; }
            }
            if (found >= 0) {
                current.set(found, t);
                r.replacedNames.add(name);
            } else {
                current.add(t);
                r.addedNames.add(name);
            }
        }
        writeCustomFile(current);
        return r;
    }

    /** Замена: все текущие удаляются, пишутся только из файла. */
    public static ImportResult replaceCustomRawJson(String json) {
        ImportResult r = new ImportResult();
        r.removedCount = getCustom().size();
        List<Map<String, Object>> incoming = parseIncomingTemplates(json);
        int totalInFile = 0;
        try {
            Map<String, Object> root = new Gson().fromJson(json, Map.class);
            if (root != null) {
                Object to = root.get("templates");
                if (to instanceof List) totalInFile = ((List<?>) to).size();
                else if (to instanceof Map) totalInFile = 1;
            }
        } catch (Throwable ignored) {}
        r.skippedCount = Math.max(0, totalInFile - incoming.size());

        for (Map<String, Object> t : incoming) {
            String name = t.get("name") == null ? String.valueOf(t.get("id")) : String.valueOf(t.get("name"));
            r.loadedNames.add(name);
        }
        writeCustomFile(incoming);
        return r;
    }



    /** Ищет пользовательскую сборку по имени (без учёта регистра).
     *  Если excludeId != null — пропускает сборку с этим id (для переименования). */
    public static Map<String, Object> findByName(String name, String excludeId) {
        if (name == null) return null;
        String needle = name.trim();
        if (needle.isEmpty()) return null;
        for (Map<String, Object> t : getAllUserTemplates()) {
            Object tid = t.get("id");
            if (excludeId != null && excludeId.equals(tid)) continue;
            Object tname = t.get("name");
            if (tname instanceof String && ((String) tname).trim().equalsIgnoreCase(needle)) {
                return t;
            }
        }
        return null;
    }


}
