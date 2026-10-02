package com.besome.sketch.editor.logic;

import static pro.sketchware.utility.ThemeUtils.getColor;
import static pro.sketchware.utility.ThemeUtils.isDarkThemeEnabled;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;

import a.a.a.Rs;
import a.a.a.Ts;
import a.a.a.wB;
import pro.sketchware.R;
import pro.sketchware.databinding.PaletteBlockBinding;

public class PaletteBlock extends LinearLayout {

    public float f = 0.0F;
    /** Search filter for block palette (issue #1971). Empty = show all. */
    public String blockSearchQuery = "";

    private String pendingHeaderTitle = null;
    private int pendingHeaderColor = 0;
    private boolean hasPendingHeader = false;
    private int currentPaletteId = -1;
    private static final String PREFS_SCROLL = "palette_scroll_positions";

    public void setCurrentPaletteId(int paletteId) {
        this.currentPaletteId = paletteId;
    }

    /** Saves the current scroll position for the active palette. */
    public void saveScrollPosition() {
        if (currentPaletteId < 0) return;
        try {
            android.content.SharedPreferences prefs = context.getSharedPreferences(PREFS_SCROLL, Context.MODE_PRIVATE);
            prefs.edit().putInt("palette_" + currentPaletteId, binding.scroll.getScrollY()).apply();
        } catch (Throwable ignored) {}
    }

    /** Restores the scroll position for the active palette. Call after blocks have been added. */
    public void restoreScrollPosition() {
        if (currentPaletteId < 0) return;
        try {
            android.content.SharedPreferences prefs = context.getSharedPreferences(PREFS_SCROLL, Context.MODE_PRIVATE);
            int y = prefs.getInt("palette_" + currentPaletteId, 0);
            binding.scroll.post(() -> binding.scroll.scrollTo(0, y));
        } catch (Throwable ignored) {}
    }

    public void setBlockSearchQuery(String query) {
        this.blockSearchQuery = (query == null) ? "" : query.toLowerCase().trim();
    }

    private boolean matchesSearch(String... fields) {
        if (blockSearchQuery.isEmpty()) return true;
        for (String s : fields) {
            if (s != null && s.toLowerCase().contains(blockSearchQuery)) return true;
        }
        return false;
    }

    /** Adds the pending header (if any) to the UI and clears it. */
    private void flushPendingHeader() {
        if (!hasPendingHeader) return;
        hasPendingHeader = false;
        if (blockSearchQuery.isEmpty()) {
            // No active filter — add header as usual
            addHeaderToUi(pendingHeaderTitle, pendingHeaderColor);
        } else {
            // Search active — only add if the setting allows it
            try {
                boolean showHeaders = mod.hilal.saif.activities.tools.ConfigActivity
                        .isSettingEnabled(mod.hilal.saif.activities.tools.ConfigActivity.SETTING_SHOW_HEADERS_WHEN_SEARCHING_BLOCKS);
                if (showHeaders) {
                    addHeaderToUi(pendingHeaderTitle, pendingHeaderColor);
                }
            } catch (Throwable ignored) {
                // If setting not available yet, fall back to showing headers
                addHeaderToUi(pendingHeaderTitle, pendingHeaderColor);
            }
        }
        pendingHeaderTitle = null;
    }

    private void clearPendingHeader() {
        hasPendingHeader = false;
        pendingHeaderTitle = null;
    }

    /** Actual implementation of "add a section header to the UI" (extracted from a(String,int)). */
    private void addHeaderToUi(String title, int color) {
        var cardView = new MaterialCardView(context);
        var params = getLayoutParams(18.0F);
        params.topMargin = (int) (f * 16.0F);
        cardView.setLayoutParams(params);
        cardView.setCardBackgroundColor(color);
        cardView.setRadius(f * 8f);

        TextView textView = new TextView(context);
        textView.setText(title);
        textView.setTextColor(getColor(context, isDarkThemeEnabled(context) ? R.attr.colorOnSurface : R.attr.colorOnSurfaceInverse));
        textView.setTextSize(10.0F);
        textView.setGravity(Gravity.CENTER | Gravity.LEFT);
        textView.setPadding((int) (f * 12.0F), 0, (int) (f * 12.0F), 0);
        cardView.addView(textView);

        binding.blockBuilder.addView(cardView);
    }

    private PaletteBlockBinding binding;
    private Context context;

    public PaletteBlock(Context context) {
        super(context);
        initialize(context);
    }

    public PaletteBlock(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize(context);
    }

    private void initialize(Context context) {
        this.context = context;
        binding = PaletteBlockBinding.inflate(LayoutInflater.from(context), this, true);
        f = wB.a(context, 1.0F);
    }

    public Ts a(String var1, String var2, String var3) {
        if (!matchesSearch(var1, var3)) {
            Rs stub = new Rs(context, -1, var1, var2, var3);
            stub.setVisibility(GONE);
            return stub;
        }
        flushPendingHeader();
        View view = new View(context);
        view.setLayoutParams(getLayoutParams(8.0F));
        binding.blockBuilder.addView(view);
        Rs blockView = new Rs(context, -1, var1, var2, var3);
        blockView.setContentDescription(generateContentDescription(var3));
        blockView.setBlockType(1);
        binding.blockBuilder.addView(blockView);
        return blockView;
    }

    public Ts a(String var1, String var2, String var3, String var4) {
        if (!matchesSearch(var1, var3, var4)) {
            Rs stub = new Rs(context, -1, var1, var2, var3, var4);
            stub.setVisibility(GONE);
            return stub;
        }
        flushPendingHeader();
        View view = new View(context);
        view.setLayoutParams(getLayoutParams(8.0F));
        binding.blockBuilder.addView(view);
        Rs blockView = new Rs(context, -1, var1, var2, var3, var4);
        blockView.setContentDescription(generateContentDescription(var4));
        blockView.setBlockType(1);
        binding.blockBuilder.addView(blockView);
        return blockView;
    }

    public TextView a(String title) {
        var textView = new TextView(context);
        textView.setText(title);
        textView.setTextSize(10.0F);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setGravity(Gravity.CENTER);
        textView.setPadding((int) (f * 8.0F), 0, (int) (f * 8.0F), 0);

        var cardView = new MaterialCardView(context);
        var params = getLayoutParams(30.0F);
        params.setMargins(0, 0, (int) (f * 4), (int) (f * 6));
        cardView.setLayoutParams(params);
        cardView.setCardBackgroundColor(getColor(context, isDarkThemeEnabled(context) ? R.attr.colorSurfaceContainerHigh : R.attr.colorSurfaceContainerHighest));
        cardView.addView(textView);

        binding.actionsContainer.addView(cardView);
        return textView;
    }

    public void a() {
        saveScrollPosition();
        binding.blockBuilder.removeAllViews();
        binding.actionsContainer.removeAllViews();
        clearPendingHeader();
    }

    public void a(String title, int color) {
        if (blockSearchQuery != null && !blockSearchQuery.isEmpty()) {
            // Search active — defer header. It will be added only if a matching block follows.
            pendingHeaderTitle = title;
            pendingHeaderColor = color;
            hasPendingHeader = true;
            return;
        }
        // No active search — add header immediately
        addHeaderToUi(title, color);
    }

    public void addDeprecatedBlock(String message, String type, String opCode) {
        if (message != null && !message.isEmpty()) {
            a(message, getColor(context, isDarkThemeEnabled(context) ? R.attr.colorSurfaceContainerHigh : R.attr.colorSurfaceInverse));
        }
        Ts blockView = a("", type, opCode);
        blockView.e = 0xFFBDBDBD;
        blockView.setTag(opCode);
    }

    private String generateContentDescription(String name) {
        if (name == null || name.isEmpty()) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        result.append(name.charAt(0));
        for (int i = 1; i < name.length(); i++) {
            char currentChar = name.charAt(i);
            if (Character.isUpperCase(currentChar)) {
                // Check if previous char is not already a space (for acronyms like "HTTPExample")
                // and if the current char is not part of an acronym (e.g. the TTP in HTTP)
                // For simplicity here, just add a space before any uppercase unless it's followed by lowercase.
                if (i + 1 < name.length() && Character.isLowerCase(name.charAt(i + 1)) || Character.isLowerCase(name.charAt(i - 1))) {
                    result.append(' ');
                }
            }
            result.append(currentChar);
        }
        return result.toString();
    }

    private LinearLayout.LayoutParams getLayoutParams(float heightMultiplier) {
        return new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (f * heightMultiplier));
    }

    public void setDragEnabled(boolean dragEnabled) {
        if (dragEnabled) {
            binding.scroll.b();
            binding.scrollHorizontal.b();
        } else {
            binding.scroll.a();
            binding.scrollHorizontal.a();
        }
    }

    public void setMinWidth(int minWidth) {
        binding.scroll.setMinimumWidth(minWidth - (int) (f * 5.0F));
        binding.scrollHorizontal.setMinimumWidth(minWidth - (int) (f * 5.0F));
        getLayoutParams().width = minWidth;
    }

    public void setUseScroll(boolean useScroll) {
        binding.scroll.setUseScroll(useScroll);
        binding.scrollHorizontal.setUseScroll(useScroll);
    }
}
