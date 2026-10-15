package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Блоки для работы с Intent: открытие приложений, настройки, share,
 * выбор файлов, установка APK, звонок через диалер.
 */
public final class IntentBlocks {

    private IntentBlocks() {}

    private static final String COLOR_OP = "#5cb722";
    private static final String PALETTE_OP = "3";

    private static HashMap<String, Object> blk(String name, String type, String typeName,
                                                 String code, String spec) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("type", type);
        m.put("typeName", typeName);
        m.put("code", code);
        m.put("color", COLOR_OP);
        m.put("palette", PALETTE_OP);
        m.put("spec", spec);
        return m;
    }

    public static void addBlocks(ArrayList<HashMap<String, Object>> list) {
        addOpenBlocks(list);
        addShareBlocks(list);
        addPickBlocks(list);
    }

    private static void addOpenBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("openApp", " ", "",
                "startActivity(getPackageManager().getLaunchIntentForPackage(%1$s));",
                "open app with package %s"));

        list.add(blk("openSettings", " ", "",
                "startActivity(new android.content.Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS, android.net.Uri.parse(\"package:\" + getPackageName())));",
                "open app settings"));

        list.add(blk("openWifiSettings", " ", "",
                "startActivity(new android.content.Intent(android.provider.Settings.ACTION_WIFI_SETTINGS));",
                "open Wi-Fi settings"));

        list.add(blk("openLocationSettings", " ", "",
                "startActivity(new android.content.Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));",
                "open location settings"));

        list.add(blk("openBluetoothSettings", " ", "",
                "startActivity(new android.content.Intent(android.provider.Settings.ACTION_BLUETOOTH_SETTINGS));",
                "open Bluetooth settings"));
    }

    private static void addShareBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("shareFile", " ", "",
                "android.content.Intent _sh = new android.content.Intent(android.content.Intent.ACTION_SEND);\\n"
                + "_sh.setType(\"*/*\");\\n"
                + "_sh.putExtra(android.content.Intent.EXTRA_STREAM, android.net.Uri.fromFile(new java.io.File(%1$s)));\\n"
                + "startActivity(android.content.Intent.createChooser(_sh, %2$s));",
                "share file %s with chooser %s"));

        list.add(blk("sendSms", " ", "",
                "startActivity(new android.content.Intent(android.content.Intent.ACTION_VIEW, android.net.Uri.parse(\"sms:\" + %1$s + \"?body=\" + android.net.Uri.encode(%2$s))));",
                "send SMS to %s with text %s"));
    }

    private static void addPickBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("pickImage", " ", "",
                "startActivityForResult(new android.content.Intent(android.content.Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI), %1$d);",
                "pick image with request code %d"));

        list.add(blk("pickFile", " ", "",
                "startActivityForResult(new android.content.Intent(android.content.Intent.ACTION_GET_CONTENT).setType(\"*/*\"), %1$d);",
                "pick any file with request code %d"));

        list.add(blk("takePicture", " ", "",
                "startActivityForResult(new android.content.Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE), %1$d);",
                "take picture with request code %d"));

        list.add(blk("installApk", " ", "",
                "android.content.Intent _inst = new android.content.Intent(android.content.Intent.ACTION_VIEW);\\n"
                + "_inst.setDataAndType(android.net.Uri.fromFile(new java.io.File(%1$s)), \"application/vnd.android.package-archive\");\\n"
                + "_inst.addFlags(android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION);\\n"
                + "startActivity(_inst);",
                "install APK from file %s"));

        list.add(blk("uninstallApp", " ", "",
                "startActivity(new android.content.Intent(android.content.Intent.ACTION_DELETE, android.net.Uri.parse(\"package:\" + %1$s)));",
                "uninstall app %s"));
    }
}
