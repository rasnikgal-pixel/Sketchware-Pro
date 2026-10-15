package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Сетевые блоки: HTTP-запросы, работа с URL, проверка соединения.
 * Разрешения: INTERNET, ACCESS_NETWORK_STATE (автоматически через yq.java).
 */
public final class NetworkBlocks {

    private NetworkBlocks() {}

    private static final String COLOR_OP = "#5cb722";
    private static final String COLOR_FUNC = "#4a6cd4";
    private static final String PALETTE_OP = "3";
    private static final String PALETTE_FUNC = "7";

    private static HashMap<String, Object> blk(String name, String type, String typeName,
                                                 String code, String color, String palette,
                                                 String spec) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("type", type);
        m.put("typeName", typeName);
        m.put("code", code);
        m.put("color", color);
        m.put("palette", palette);
        m.put("spec", spec);
        return m;
    }

    public static void addBlocks(ArrayList<HashMap<String, Object>> list) {
        addUrlBlocks(list);
        addNetworkStateBlocks(list);
    }

    // ─────────────────────────────────────────────────────────────
    // URL
    // ─────────────────────────────────────────────────────────────
    private static void addUrlBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("parseUrlHost", "s", "",
                "android.net.Uri.parse(%1$s).getHost()",
                COLOR_FUNC, PALETTE_FUNC,
                "host of URL %s"));
    }

    // ─────────────────────────────────────────────────────────────
    // Состояние сети
    // ─────────────────────────────────────────────────────────────
    private static void addNetworkStateBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("isNetworkAvailable", "b", "",
                "(((android.net.ConnectivityManager) getApplicationContext().getSystemService(android.content.Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null && ((android.net.ConnectivityManager) getApplicationContext().getSystemService(android.content.Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo().isConnected())",
                COLOR_FUNC, PALETTE_FUNC,
                "network is available"));

        list.add(blk("isWifiConnected", "b", "",
                "(((android.net.ConnectivityManager) getApplicationContext().getSystemService(android.content.Context.CONNECTIVITY_SERVICE)).getNetworkInfo(android.net.ConnectivityManager.TYPE_WIFI) != null && ((android.net.ConnectivityManager) getApplicationContext().getSystemService(android.content.Context.CONNECTIVITY_SERVICE)).getNetworkInfo(android.net.ConnectivityManager.TYPE_WIFI).isConnected())",
                COLOR_FUNC, PALETTE_FUNC,
                "Wi-Fi is connected"));

        list.add(blk("isMobileConnected", "b", "",
                "(((android.net.ConnectivityManager) getApplicationContext().getSystemService(android.content.Context.CONNECTIVITY_SERVICE)).getNetworkInfo(android.net.ConnectivityManager.TYPE_MOBILE) != null && ((android.net.ConnectivityManager) getApplicationContext().getSystemService(android.content.Context.CONNECTIVITY_SERVICE)).getNetworkInfo(android.net.ConnectivityManager.TYPE_MOBILE).isConnected())",
                COLOR_FUNC, PALETTE_FUNC,
                "mobile network is connected"));
    }
}
