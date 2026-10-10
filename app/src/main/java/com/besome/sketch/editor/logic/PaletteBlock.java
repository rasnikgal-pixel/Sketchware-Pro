package com.besome.sketch.editor.logic;


import mod.hey.studios.util.Helper;
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
    private String scId = "";
    private static final String PREFS_SCROLL = "palette_scroll_positions_per_project";

    public void setScId(String scId) {
        this.scId = scId == null ? "" : scId;
    }

    private String keyPrefix() {
        return (scId.isEmpty() ? "global" : scId) + "_";
    }

    public void setCurrentPaletteId(int paletteId) {
        this.currentPaletteId = paletteId;
    }

    /** Saves the current scroll position for the active palette. */
    public void saveScrollPosition() {
        if (currentPaletteId < 0) return;
        try {
            android.content.SharedPreferences prefs = context.getSharedPreferences(PREFS_SCROLL, Context.MODE_PRIVATE);
            prefs.edit().putInt(keyPrefix() + "palette_" + currentPaletteId, binding.scroll.getScrollY()).apply();
        } catch (Throwable ignored) {}
    }

    /** Restores the scroll position for the active palette. Call after blocks have been added. */
    public void restoreScrollPosition() {
        if (currentPaletteId < 0) return;
        try {
            android.content.SharedPreferences prefs = context.getSharedPreferences(PREFS_SCROLL, Context.MODE_PRIVATE);
            int y = prefs.getInt(keyPrefix() + "palette_" + currentPaletteId, 0);
            binding.scroll.post(() -> binding.scroll.scrollTo(0, y));
        } catch (Throwable ignored) {}
    }

    public void setBlockSearchQuery(String query) {
        this.blockSearchQuery = (query == null) ? "" : query.toLowerCase().trim();
    }

    private boolean matchesSearch(String... fields) {
        if (blockSearchQuery.isEmpty()) return true;
        String normalizedQuery = normalizeForSearch(blockSearchQuery);
        for (String s : fields) {
            if (s == null) continue;
            String lower = s.toLowerCase();
            // 1. Прямое вхождение подстроки
            if (lower.contains(blockSearchQuery)) return true;
            // 2. Нормализованное сравнение (без пробелов и разделителей):
            //    'dialog show' -> 'dialogShow', 'math pi' -> 'mathPi'
            if (!normalizedQuery.isEmpty() && normalizeForSearch(lower).contains(normalizedQuery)) return true;
        }
        return false;
    }

    /** Removes whitespace, dashes, underscores and any non-alphanumeric chars. */
    
    /** Wraps the block view in a horizontal row with a star icon on the left. */
    private View wrapWithStar(View blockView, String type, String name, String typeName) {
        LinearLayout row = new LinearLayout(context);
        row.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams rowParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        row.setLayoutParams(rowParams);
        row.setGravity(android.view.Gravity.CENTER_VERTICAL);

        android.widget.ImageView star = new android.widget.ImageView(context);
        int starSize = (int) (f * 22.0F);
        LinearLayout.LayoutParams starParams = new LinearLayout.LayoutParams(starSize, starSize);
        star.setLayoutParams(starParams);
        star.setImageResource(
                mod.jbk.util.FavoriteBlocksManager.contains(name)
                        ? android.R.drawable.btn_star_big_on
                        : android.R.drawable.btn_star_big_off);
        star.setScaleType(android.widget.ImageView.ScaleType.CENTER_INSIDE);
        star.setAlpha(0.7f);
        star.setOnClickListener(v -> {
            boolean inFav = mod.jbk.util.FavoriteBlocksManager.contains(name);
            boolean newState = !inFav;
            if (newState) {
                mod.jbk.util.FavoriteBlocksManager.add(name, type, typeName);
            } else {
                mod.jbk.util.FavoriteBlocksManager.remove(name);
            }
            star.setImageResource(newState
                    ? android.R.drawable.btn_star_big_on
                    : android.R.drawable.btn_star_big_off);
            android.widget.Toast.makeText(context,
                    (CharSequence) ((CharSequence) (newState ? Helper.getResString(R.string.auto_palette_block_001) : Helper.getResString(R.string.auto_palette_block_002))),
                    android.widget.Toast.LENGTH_SHORT).show();
        });

        LinearLayout.LayoutParams blockParams = new LinearLayout.LayoutParams(
                0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
        blockView.setLayoutParams(blockParams);

        row.addView(star);
        row.addView(blockView);
        return row;
    }

    private static String normalizeForSearch(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder(s.length());
        for (int i = 0; i < s.length(); i++) {
            char c = Character.toLowerCase(s.charAt(i));
            if ((c >= 'a' && c <= 'z') || (c >= '0' && c <= '9') || (c >= 'а' && c <= 'я') || c == 'ё') {
                sb.append(c);
            }
        }
        return sb.toString();
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
        binding.blockBuilder.addView(wrapWithStar(blockView, var2, var3, null));
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
        binding.blockBuilder.addView(wrapWithStar(blockView, var2, var4, var3));
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

    /** Adds a template (Us) view directly to the palette. */
    public void addTemplateView(android.view.View templateView) {
        binding.blockBuilder.addView(templateView);
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
