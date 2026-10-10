package mod.agus.jcoderz.editor.manage.library.locallibrary;

import android.os.Environment;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import mod.hey.studios.util.Helper;
import mod.jbk.util.LogUtil;
import pro.sketchware.utility.FilePathUtil;
import pro.sketchware.utility.FileUtil;
import pro.sketchware.utility.SketchwareUtil;

public class ManageLocalLibrary {

    private final String projectId;
    public ArrayList<HashMap<String, Object>> list;

    public ManageLocalLibrary(String sc_id) {
        projectId = sc_id;
        String localLibraryConfigPath = new FilePathUtil().getPathLocalLibrary(projectId);
        if (FileUtil.isExistFile(localLibraryConfigPath)) {
            try {
                list = new Gson().fromJson(FileUtil.readFile(localLibraryConfigPath), Helper.TYPE_MAP_LIST);

                if (list == null) {
                    LogUtil.w(getClass().getSimpleName(), "Read null from file " + localLibraryConfigPath + ", deleting invalid configuration.");
                    if (!new File(localLibraryConfigPath).delete()) {
                        LogUtil.e(getClass().getSimpleName(), "Couldn't delete file " + localLibraryConfigPath);
                    }

                    // fall-through to shared error handler
                } else {
                    return;
                }
            } catch (JsonParseException e) {
                // fall-through to shared error handler
            }

            SketchwareUtil.toastError(Helper.getResString(R.string.auto_manage_local_library_001));
        }
        list = new ArrayList<>();
    }

    public ArrayList<String> getAssets() {
        ArrayList<String> assets = new ArrayList<>();

        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            HashMap<String, Object> localLibrary = list.get(i);

            if (localLibrary.containsKey("assetsPath")) {
                Object assetsPath = localLibrary.get("assetsPath");

                if (assetsPath instanceof String) {
                    assets.add((String) assetsPath);
                } else {
                    SketchwareUtil.toastError("Недопустимый путь к assets у включённой локальной библиотеки #" + i, Toast.LENGTH_LONG);
                }
            }
        }

        return assets;
    }

    public ArrayList<String> getDexLocalLibrary() {
        ArrayList<String> dexes = new ArrayList<>();

        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            HashMap<String, Object> localLibrary = list.get(i);
            Object dexPath = localLibrary.get("dexPath");

            if (dexPath instanceof String) {
                dexes.add((String) dexPath);
            } else {
                SketchwareUtil.toastError("Недопустимый путь к DEX у включённой локальной библиотеки #" + i, Toast.LENGTH_LONG);
            }
        }

        // Issue #1929: include transitive dependency DEX files
        try {
            File libsRoot = new File(pro.sketchware.utility.FileUtil.getExternalStorageDir()
                    + "/.sketchware/libs/local_libs/");
            if (libsRoot.isDirectory()) {
                File[] subdirs = libsRoot.listFiles(File::isDirectory);
                if (subdirs != null) {
                    java.util.Set<String> alreadyAdded = new java.util.HashSet<>(dexes);
                    for (File dir : subdirs) {
                        File classesDex = new File(dir, "classes.dex");
                        if (classesDex.isFile()) {
                            String abs = classesDex.getAbsolutePath();
                            if (alreadyAdded.add(abs)) {
                                dexes.add(abs);
                            }
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return dexes;
    }

    public ArrayList<String> getExtraDexes() {
        ArrayList<String> extraDexes = new ArrayList<>();

        for (String localLibraryDexPath : getDexLocalLibrary()) {
            File dexPath = new File(localLibraryDexPath);
            if (dexPath.getParentFile() != null) {
                File[] dexPathFiles = dexPath.getParentFile().listFiles();

                if (dexPathFiles != null) {
                    for (File dexPathFile : dexPathFiles) {
                        String dexPathFilename = dexPathFile.getName();
                        if (!dexPathFilename.equals("classes.dex")
                                && dexPathFilename.startsWith("classes")
                                && dexPathFilename.endsWith(".dex")) {
                            extraDexes.add(dexPathFile.getAbsolutePath());
                        }
                    }
                }
            }
        }

        return extraDexes;
    }


    public ArrayList<String> getManifestPaths() {
        ArrayList<String> manifests = new ArrayList<>();
        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            HashMap<String, Object> localLibrary = list.get(i);
            Object manifestPath = localLibrary.get("manifestPath");
            if (manifestPath instanceof String && FileUtil.isExistFile((String) manifestPath)) {
                manifests.add((String) manifestPath);
            }
        }
        return manifests;
    }

    public ArrayList<String> getGenLocalLibrary() {
        ArrayList<String> genPaths = new ArrayList<>();

        for (String packageName : getPackageNames()) {
            if (!packageName.isEmpty()) {
                File projectGenFolder = new File(Environment.getExternalStorageDirectory(),
                        ".sketchware/mysc/".concat(projectId).concat("/gen"));
                String rJavaPath = packageName.replace(".", File.separator)
                        .concat(File.separator).concat("R.java");
                genPaths.add(new File(projectGenFolder, rJavaPath).getAbsolutePath());
            }
        }

        return genPaths;
    }

    public ArrayList<String> getImportLocalLibrary() {
        ArrayList<String> imports = new ArrayList<>();

        for (String packageName : getPackageNames()) {
            if (!packageName.isEmpty()) {
                imports.add(packageName.concat(".*"));
            }
        }

        return imports;
    }

    public ArrayList<File> getLocalLibraryJars() {
        ArrayList<File> jars = new ArrayList<>();

        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            HashMap<String, Object> localLibrary = list.get(i);
            Object jarPath = localLibrary.get("jarPath");

            if (jarPath instanceof String) {
                jars.add(new File((String) jarPath));
            } else {
                SketchwareUtil.toastError("Недопустимый путь к JAR у включённой локальной библиотеки #" + i + "->" + localLibrary.get("name"), Toast.LENGTH_LONG);
            }
        }

        return jars;
    }

    public String getJarLocalLibrary() {
        StringBuilder classpath = new StringBuilder();

        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            HashMap<String, Object> localLibrary = list.get(i);
            Object jarPath = localLibrary.get("jarPath");

            if (jarPath instanceof String) {
                classpath.append(":");
                classpath.append((String) jarPath);
            } else {
                SketchwareUtil.toastError("Недопустимый путь к JAR у включённой локальной библиотеки #" + i + "->" + localLibrary.get("name"), Toast.LENGTH_LONG);
            }
        }

        // Issue #1929: add transitive dependency jars from local_libs/
        try {
            File libsRoot = new File(pro.sketchware.utility.FileUtil.getExternalStorageDir()
                    + "/.sketchware/libs/local_libs/");
            if (libsRoot.isDirectory()) {
                File[] subdirs = libsRoot.listFiles(File::isDirectory);
                if (subdirs != null) {
                    java.util.Set<String> alreadyAdded = new java.util.HashSet<>();
                    for (String part : classpath.toString().split(":")) {
                        if (!part.isEmpty()) alreadyAdded.add(new File(part).getAbsolutePath());
                    }
                    for (File dir : subdirs) {
                        File classesJar = new File(dir, "classes.jar");
                        if (classesJar.isFile()) {
                            String abs = classesJar.getAbsolutePath();
                            if (alreadyAdded.add(abs)) {
                                classpath.append(":").append(abs);
                            }
                        }
                    }
                }
            }
        } catch (Throwable ignored) {
        }
        return classpath.toString();
    }

    public ArrayList<String> getNativeLibs() {
        ArrayList<String> nativeLibraryDirectories = new ArrayList<>();

        for (String localLibraryDexPath : getDexLocalLibrary()) {
            File localLibraryDexFile = new File(localLibraryDexPath);
            File jniFolder = new File(localLibraryDexFile.getParentFile(), "jni");
            if (jniFolder.isDirectory()) {
                nativeLibraryDirectories.add(jniFolder.getAbsolutePath());
            }
        }

        return nativeLibraryDirectories;
    }

    public ArrayList<String> getPackageNames() {
        ArrayList<String> packageNames = new ArrayList<>();

        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            HashMap<String, Object> localLibrary = list.get(i);
            if (localLibrary.containsKey("packageName")) {
                Object packageName = localLibrary.get("packageName");

                if (packageName instanceof String) {
                    packageNames.add((String) packageName);
                } else {
                    SketchwareUtil.toastError("Недопустимое имя пакета включённой локальной библиотеки #" + i, Toast.LENGTH_LONG);
                }
            }
        }

        return packageNames;
    }

    public String getPackageNameLocalLibrary() {
        StringBuilder packageNames = new StringBuilder();

        for (String packageName : getPackageNames()) {
            if (!packageName.isEmpty()) {
                packageNames.append(packageName);
                packageNames.append(":");
            }
        }

        return packageNames.toString();
    }

    public ArrayList<String> getPgRules() {
        ArrayList<String> proguardRules = new ArrayList<>();

        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            HashMap<String, Object> localLibrary = list.get(i);
            if (localLibrary.containsKey("pgRulesPath")) {
                Object proguardRulesPath = localLibrary.get("pgRulesPath");

                if (proguardRulesPath instanceof String) {
                    proguardRules.add((String) proguardRulesPath);
                } else {
                    SketchwareUtil.toastError("Недопустимый путь к ProGuard у включённой локальной библиотеки #" + i, Toast.LENGTH_LONG);
                }
            }
        }

        return proguardRules;
    }

    public ArrayList<String> getResLocalLibrary() {
        ArrayList<String> localLibraryRes = new ArrayList<>();

        for (int i = 0, listSize = list.size(); i < listSize; i++) {
            HashMap<String, Object> localLibrary = list.get(i);
            if (localLibrary.containsKey("resPath")) {
                Object resPath = localLibrary.get("resPath");

                if (resPath instanceof String) {
                    localLibraryRes.add((String) resPath);
                } else {
                    SketchwareUtil.toastError("Недопустимый путь к каталогу res/ у включённой локальной библиотеки #" + i, Toast.LENGTH_LONG);
                }
            }
        }

        return localLibraryRes;
    }
}
