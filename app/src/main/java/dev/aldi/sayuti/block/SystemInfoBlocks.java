package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

public final class SystemInfoBlocks {

    private SystemInfoBlocks() {}

    private static final String COLOR_FUNC = "#4a6cd4";
    private static final String PALETTE_FUNC = "7";

    private static HashMap<String, Object> blk(String name, String type, String typeName,
                                                 String code, String spec) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("type", type);
        m.put("typeName", typeName);
        m.put("code", code);
        m.put("color", COLOR_FUNC);
        m.put("palette", PALETTE_FUNC);
        m.put("spec", spec);
        return m;
    }

    public static void addBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("getSdkVersion", "d", "",
                "(double) android.os.Build.VERSION.SDK_INT",
                "Android SDK version"));

        list.add(blk("getAppVersionName", "s", "",
                "((java.util.function.Supplier<String>) () -> { try { return getPackageManager().getPackageInfo(getPackageName(), 0).versionName; } catch (Throwable _t) { return \"\"; } }).get()",
                "app version name"));

        list.add(blk("getAppVersionCode", "d", "",
                "((java.util.function.Supplier<Double>) () -> { try { return (double) getPackageManager().getPackageInfo(getPackageName(), 0).versionCode; } catch (Throwable _t) { return 0.0; } }).get()",
                "app version code"));

        list.add(blk("getAppPackageName", "s", "",
                "getPackageName()",
                "app package name"));

        list.add(blk("getAppName", "s", "",
                "((java.util.function.Supplier<String>) () -> { try { return getApplicationInfo().loadLabel(getPackageManager()).toString(); } catch (Throwable _t) { return \"\"; } }).get()",
                "app name"));

        list.add(blk("getTimezone", "s", "",
                "java.util.TimeZone.getDefault().getID()",
                "device timezone"));

        list.add(blk("getLocale", "s", "",
                "java.util.Locale.getDefault().toString()",
                "device locale"));

        list.add(blk("isTablet", "b", "",
                "(getResources().getConfiguration().screenLayout & android.content.res.Configuration.SCREENLAYOUT_SIZE_MASK) >= android.content.res.Configuration.SCREENLAYOUT_SIZE_LARGE",
                "device is a tablet"));
    }
}
