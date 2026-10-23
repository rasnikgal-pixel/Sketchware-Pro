package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Дополнительные блоки WebView (аналоги DroidScript: Execute, LoadHtml, GetUrl и т.д.)
 * Палитра 103 — «WebView Ext».
 */
public final class WebViewExtBlocks {

    private WebViewExtBlocks() {}

    private static final String COLOR_FUNC = "#4A6CD4";
    private static final String PALETTE_FUNC = "103";

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
        list.add(blk("wvEvaluateJS", " ", "",
                "%1$s.evaluateJavascript(%2$s, null);",
                "%m.webview evaluate JS %s"));

        list.add(blk("wvLoadDataWithBase", " ", "",
                "%1$s.loadDataWithBaseURL(null, %2$s, \"text/html\", \"UTF-8\", null);",
                "%m.webview load HTML data %s"));

        list.add(blk("wvSetUserAgent", " ", "",
                "%1$s.getSettings().setUserAgentString(%2$s);",
                "%m.webview set User-Agent %s"));

        list.add(blk("wvSetCacheMode", " ", "",
                "%1$s.getSettings().setCacheMode(%2$s);",
                "%m.webview set CacheMode %s"));

        list.add(blk("wvClearCache", " ", "",
                "%1$s.clearCache(true);",
                "%m.webview clear cache"));

        list.add(blk("wvClearHistory", " ", "",
                "%1$s.clearHistory();",
                "%m.webview clear history"));

        list.add(blk("wvCanGoBack", "b", "",
                "%1$s.canGoBack()",
                "%m.webview canGoBack"));

        list.add(blk("wvCanGoForward", "b", "",
                "%1$s.canGoForward()",
                "%m.webview canGoForward"));

        list.add(blk("wvGetUrl", "s", "",
                "%1$s.getUrl()",
                "%m.webview get current URL"));

        list.add(blk("wvGetTitle", "s", "",
                "%1$s.getTitle()",
                "%m.webview get page title"));

        list.add(blk("wvSetJavaScriptEnabled", " ", "",
                "%1$s.getSettings().setJavaScriptEnabled(%2$s);",
                "%m.webview set JavaScript enabled %b"));

        list.add(blk("wvSetDomStorageEnabled", " ", "",
                "%1$s.getSettings().setDomStorageEnabled(%2$s);",
                "%m.webview set DOM storage enabled %b"));
    }
}
