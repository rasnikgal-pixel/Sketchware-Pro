package pro.sketchware.skproj;

import android.content.Context;

import com.besome.sketch.beans.BlockBean;
import com.besome.sketch.beans.ProjectFileBean;
import com.besome.sketch.beans.ViewBean;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import a.a.a.jC;
import a.a.a.lC;
import a.a.a.yB;
import mod.hilal.saif.activities.tools.ConfigActivity;

/**
 * Экспорт проекта Sketchware в переносимую папку формата .skproj.
 *
 * <p>Структура:
 * <pre>
 * MyProject.skproj/
 *   manifest.json    — метаданные формата
 *   project.json     — данные проекта
 *   screens/         — экраны (ViewBeans)
 *   logic/           — события (BlockBeans)
 *   components/      — компоненты (Dialog, Timer, ...) по экранам
 *   more_blocks/     — MoreBlocks по экранам
 * </pre>
 */
public class SkProjExporter {

    private static final String FORMAT = "skproj";
    private static final int FORMAT_VERSION = 1;

    private final Context context;
    private final String scId;
    public String error = "";
    private File outPath;
    public SkProjExporter(Context context, String scId) {
        this.context = context;
        this.scId = scId;
    }

    /** @return созданная папка .skproj или zip, либо null при ошибке. */
    public File export() {
        try {
            HashMap<String, Object> metadata = lC.b(scId);
            if (metadata == null) {
                error = "Не удалось прочитать метаданные проекта";
                return null;
            }

            String projectName = yB.c(metadata, "my_ws_name");
            if (projectName == null || projectName.isEmpty()) projectName = "project_";
            projectName = projectName.replace(File.separator, "_");

            File baseDir = new File(android.os.Environment.getExternalStorageDirectory(),
                    ConfigActivity.getSkprojOutputDir());
            if (!baseDir.exists() && !baseDir.mkdirs()) {
                error = "Не удалось создать папку экспорта: " + baseDir;
                return null;
            }

            // Временная папка (или сразу целевая, если папка)
            String ts = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.US).format(new Date());
            File workDir = new File(baseDir, projectName + "_" + ts + ".skproj");
            if (!workDir.mkdirs()) {
                error = "Не удалось создать рабочую папку: " + workDir;
                return null;
            }

            // Собираем содержимое
            exportManifest(workDir, metadata, projectName);
            exportProject(workDir, metadata);
            exportScreens(workDir);
            exportLogic(workDir);
            exportComponents(workDir);
            exportMoreBlocks(workDir);

            // Если нужен zip — упаковываем
            if (ConfigActivity.isSkprojFormatZip()) {
                File zipFile = new File(baseDir, projectName + "_" + ts + ".skproj.zip");
                zipFolder(workDir, zipFile);
                deleteRecursive(workDir);
                outPath = zipFile;
            } else {
                outPath = workDir;
            }
            return outPath;
        } catch (Throwable t) {
            error = String.valueOf(t);
            return null;
        }
    }

    public File getOutFile() { return outPath; }

    /** Создаёт manifest.json с метаданными формата. */
    private void exportManifest(File dir, HashMap<String, Object> metadata, String projectName) throws Exception {
        JSONObject manifest = new JSONObject();
        manifest.put("format", FORMAT);
        manifest.put("version", FORMAT_VERSION);
        manifest.put("app", "Sketchware Pro RU");
        manifest.put("createdAt", new SimpleDateFormat("yyyy-MM-dd\u0027T\u0027HH:mm:ssXXX", Locale.US).format(new Date()));
        manifest.put("projectScId", scId);
        manifest.put("projectName", projectName);

        writeFile(new File(dir, "manifest.json"), manifest.toString(2));
    }

    /** Создаёт project.json с данными проекта. */
    private void exportProject(File dir, HashMap<String, Object> metadata) throws Exception {
        JSONObject project = new JSONObject();
        try {
            project.put("scId", yB.c(metadata, "sc_id"));
            project.put("projectName", yB.c(metadata, "my_ws_name"));
            project.put("appName", yB.c(metadata, "my_app_name"));
            project.put("packageName", yB.c(metadata, "my_sc_pkg_name"));
            project.put("versionCode", yB.c(metadata, "sc_ver_code"));
            project.put("versionName", yB.c(metadata, "sc_ver_name"));
        } catch (Throwable t) {
            error = String.valueOf(t);
        }

        // Все остальные поля метаданных (на всякий случай)
        JSONObject extras = new JSONObject();
        for (Map.Entry<String, Object> e : metadata.entrySet()) {
            try {
                Object v = e.getValue();
                if (v == null) continue;
                if (v instanceof String || v instanceof Number || v instanceof Boolean) {
                    extras.put(e.getKey(), v);
                }
            } catch (Throwable ignored) {}
        }
        project.put("metadata", extras);

        // Список экранов
        JSONArray screens = new JSONArray();
        ArrayList<ProjectFileBean> files = jC.b(scId).b();
        if (files != null) {
            for (ProjectFileBean pfb : files) {
                if (pfb == null) continue;
                JSONObject s = new JSONObject();
                s.put("fileName", pfb.fileName);
                s.put("fileType", pfb.fileType);
                s.put("xmlName", pfb.getXmlName());
                s.put("javaName", pfb.getJavaName());
                s.put("orientation", pfb.orientation);
                s.put("options", pfb.options);
                screens.put(s);
            }
        }
        project.put("screens", screens);

        writeFile(new File(dir, "project.json"), project.toString(2));
    }

    /** Сохраняет все экраны в screens/<xmlName>.json. */
    private void exportScreens(File dir) throws Exception {
        File screensDir = new File(dir, "screens");
        screensDir.mkdirs();

        ArrayList<ProjectFileBean> files = jC.b(scId).b();
        if (files == null) return;

        for (ProjectFileBean pfb : files) {
            if (pfb == null) continue;
            String xmlName = pfb.getXmlName();
            if (xmlName == null || xmlName.isEmpty()) continue;

            ArrayList<ViewBean> views = jC.a(scId).d(xmlName);
            JSONArray arr = new JSONArray();
            if (views != null) {
                for (ViewBean v : views) {
                    if (v == null) continue;
                    try { arr.put(viewBeanToJson(v)); } catch (Throwable ignored) {}
                }
            }

            JSONObject root = new JSONObject();
            root.put("xmlName", xmlName);
            root.put("views", arr);

            writeFile(new File(screensDir, xmlName + ".json"), root.toString(2));
        }
    }

    /** ViewBean → JSON. */
    private JSONObject viewBeanToJson(ViewBean v) throws Exception {
        JSONObject o = new JSONObject();
        o.put("id", v.id);
        o.put("name", v.name);
        o.put("type", v.type);
        o.put("parent", v.parent);
        o.put("index", v.index);
        o.put("parentType", v.parentType);
        o.put("convert", v.convert);
        return o;
    }

    /** Сохраняет все события в logic/<javaName>__<eventName>.json. */
    private void exportLogic(File dir) throws Exception {
        File logicDir = new File(dir, "logic");
        logicDir.mkdirs();

        ArrayList<ProjectFileBean> files = jC.b(scId).b();
        if (files == null) return;

        for (ProjectFileBean pfb : files) {
            if (pfb == null) continue;
            String javaName = pfb.getJavaName();
            if (javaName == null || javaName.isEmpty()) continue;

            HashMap<String, ArrayList<BlockBean>> events;
            try { events = jC.a(scId).b(javaName); } catch (Throwable t) { continue; }
            if (events == null || events.isEmpty()) continue;

            for (Map.Entry<String, ArrayList<BlockBean>> e : events.entrySet()) {
                String eventName = e.getKey();
                ArrayList<BlockBean> blocks = e.getValue();
                if (blocks == null) blocks = new ArrayList<>();

                JSONArray arr = new JSONArray();
                for (BlockBean b : blocks) {
                    if (b == null) continue;
                    try { arr.put(blockBeanToJson(b)); } catch (Throwable ignored) {}
                }

                JSONObject root = new JSONObject();
                root.put("javaName", javaName);
                root.put("eventName", eventName);
                root.put("blocks", arr);

                String fname = javaName + "__" + eventName + ".json";
                writeFile(new File(logicDir, fname), root.toString(2));
            }
        }
    }

    /** BlockBean → JSON. */
    private JSONObject blockBeanToJson(BlockBean b) throws Exception {
        JSONObject o = new JSONObject();
        o.put("id", b.id);
        o.put("opCode", b.opCode);
        o.put("spec", b.spec);
        o.put("type", b.type);
        o.put("nextBlock", b.nextBlock);
        o.put("subStack1", b.subStack1);
        o.put("subStack2", b.subStack2);
        if (b.parameters != null) {
            JSONArray p = new JSONArray();
            for (String s : b.parameters) p.put(s);
            o.put("parameters", p);
        }
        return o;
    }

    /** Сохраняет компоненты экранов (Dialog, Timer, ...) в components/<javaName>.json. */
    private void exportComponents(File dir) throws Exception {
        File compDir = new File(dir, "components");
        compDir.mkdirs();

        ArrayList<ProjectFileBean> files = jC.b(scId).b();
        if (files == null) return;

        for (ProjectFileBean pfb : files) {
            if (pfb == null) continue;
            String javaName = pfb.getJavaName();
            if (javaName == null || javaName.isEmpty()) continue;

            ArrayList<com.besome.sketch.beans.ComponentBean> comps;
            try { comps = jC.a(scId).e(javaName); } catch (Throwable t) { continue; }
            if (comps == null || comps.isEmpty()) continue;

            JSONArray arr = new JSONArray();
            for (com.besome.sketch.beans.ComponentBean c : comps) {
                if (c == null) continue;
                try { arr.put(componentBeanToJson(c)); } catch (Throwable ignored) {}
            }

            JSONObject root = new JSONObject();
            root.put("javaName", javaName);
            root.put("components", arr);

            writeFile(new File(compDir, javaName + ".json"), root.toString(2));
        }
    }

    /** ComponentBean → JSON. */
    private JSONObject componentBeanToJson(com.besome.sketch.beans.ComponentBean c) throws Exception {
        JSONObject o = new JSONObject();
        o.put("type", c.type);
        o.put("componentId", c.componentId);
        o.put("param1", c.param1);
        o.put("param2", c.param2);
        o.put("param3", c.param3);
        return o;
    }

    /** Сохраняет MoreBlocks в more_blocks/<javaName>.json. */
    private void exportMoreBlocks(File dir) throws Exception {
        File mbDir = new File(dir, "more_blocks");
        mbDir.mkdirs();

        ArrayList<ProjectFileBean> files = jC.b(scId).b();
        if (files == null) return;

        for (ProjectFileBean pfb : files) {
            if (pfb == null) continue;
            String javaName = pfb.getJavaName();
            if (javaName == null || javaName.isEmpty()) continue;

            ArrayList<android.util.Pair<String, String>> mb;
            try { mb = jC.a(scId).i(javaName); } catch (Throwable t) { continue; }
            if (mb == null || mb.isEmpty()) continue;

            JSONArray arr = new JSONArray();
            for (android.util.Pair<String, String> p : mb) {
                if (p == null) continue;
                JSONObject o = new JSONObject();
                o.put("first", p.first);
                o.put("second", p.second);
                arr.put(o);
            }

            JSONObject root = new JSONObject();
            root.put("javaName", javaName);
            root.put("moreBlocks", arr);

            writeFile(new File(mbDir, javaName + ".json"), root.toString(2));
        }
    }

    /** Записывает содержимое в файл (UTF-8). */
    private void writeFile(File f, String content) throws Exception {
        try (FileOutputStream fos = new FileOutputStream(f)) {
            fos.write(content.getBytes("UTF-8"));
        }
    }

    /** Упаковывает папку в zip (максимальное сжатие). */
    private void zipFolder(File srcDir, File zipFile) throws Exception {
        try (ZipOutputStream zos = new ZipOutputStream(new java.io.FileOutputStream(zipFile))) {
            zos.setLevel(9);  // максимальное сжатие
            addFolderToZip(srcDir, srcDir, zos);
            zos.finish();
        }
    }

    private void addFolderToZip(File root, File dir, ZipOutputStream zos) throws Exception {
        File[] files = dir.listFiles();
        if (files == null) return;
        byte[] buf = new byte[8192];
        for (File f : files) {
            if (f.isDirectory()) {
                addFolderToZip(root, f, zos);
            } else {
                String rel = root.toURI().relativize(f.toURI()).getPath();
                zos.putNextEntry(new ZipEntry(rel));
                try (java.io.FileInputStream fis = new java.io.FileInputStream(f)) {
                    int n;
                    while ((n = fis.read(buf)) > 0) zos.write(buf, 0, n);
                }
                zos.closeEntry();
            }
        }
    }

    /** Рекурсивное удаление папки/файла. */
    private void deleteRecursive(File f) {
        if (f == null || !f.exists()) return;
        if (f.isDirectory()) {
            File[] files = f.listFiles();
            if (files != null) for (File c : files) deleteRecursive(c);
        }
        f.delete();
    }
}
