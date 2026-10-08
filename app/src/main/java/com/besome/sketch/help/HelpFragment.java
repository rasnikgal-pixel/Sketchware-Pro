package com.besome.sketch.help;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import pro.sketchware.R;

/**
 * Фрагмент «Справка» — WebView с онлайн-документацией.
 *
 * URL: https://rasnikgal-pixel.github.io/sketchware-help/?page=<page>
 *
 * Anchor → page берётся из assets/anchors.json.
 */
public class HelpFragment extends Fragment {

    private static final String BASE_URL = "https://rasnikgal-pixel.github.io/sketchware-help/";
    private static final String DEFAULT_PAGE = "page_1.html";
    private static final String ARG_PAGE = "page";
    private static final String ARG_ANCHOR = "anchor";

    private WebView webView;

    /** Создаёт фрагмент, открывающий конкретную страницу. */
    public static HelpFragment newInstance(String page, String anchor) {
        HelpFragment f = new HelpFragment();
        Bundle b = new Bundle();
        if (page != null) b.putString(ARG_PAGE, page);
        if (anchor != null) b.putString(ARG_ANCHOR, anchor);
        f.setArguments(b);
        return f;
    }

    /** Создаёт фрагмент, открывающий страницу по anchor (ищет в anchors.json). */
    public static HelpFragment newInstanceByAnchor(String anchor) {
        String page = lookupPage(anchor);
        return newInstance(page, null);
    }

    /** Ищет страницу по anchor в assets/anchors.json. */
    public static String lookupPage(String anchor) {
        if (anchor == null || anchor.isEmpty()) return DEFAULT_PAGE;
        try {
            android.content.Context ctx = pro.sketchware.SketchApplication.getContext();
            java.io.InputStream is = ctx.getAssets().open("anchors.json");
            java.io.BufferedReader br = new java.io.BufferedReader(
                    new java.io.InputStreamReader(is, "UTF-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) sb.append(line);
            br.close();
            org.json.JSONObject obj = new org.json.JSONObject(sb.toString());
            if (obj.has(anchor)) return obj.getString(anchor);
        } catch (Throwable ignored) {}
        return DEFAULT_PAGE;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_help, container, false);
        webView = root.findViewById(R.id.help_webview);
        View fab = root.findViewById(R.id.help_fab);
        if (fab != null) {
            fab.setOnClickListener(v -> {
                if (webView != null) {
                    webView.loadUrl(BASE_URL + "?page=" + DEFAULT_PAGE);
                }
            });
        }
        setupWebView();
        return root;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setupWebView() {
        if (webView == null) return;

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);

        webView.setWebViewClient(new WebViewClient());

        String page = DEFAULT_PAGE;
        String anchor = null;
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey(ARG_PAGE)) {
                page = args.getString(ARG_PAGE);
            } else if (args.containsKey(ARG_ANCHOR)) {
                page = lookupPage(args.getString(ARG_ANCHOR));
            }
        }
        if (page == null || page.isEmpty()) page = DEFAULT_PAGE;

        webView.loadUrl(BASE_URL + "?page=" + page);
    }

    @Override
    public void onDestroyView() {
        if (webView != null) {
            webView.destroy();
            webView = null;
        }
        super.onDestroyView();
    }
}
