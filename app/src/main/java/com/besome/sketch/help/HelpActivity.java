package com.besome.sketch.help;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import com.besome.sketch.lib.base.BaseAppCompatActivity;

import pro.sketchware.R;

/**
 * Активность «Справка» — открывает HelpFragment.
 *
 * Может быть запущена:
 * - напрямую (без параметров) — откроет главную справки;
 * - с extra "page" — откроет конкретную страницу (?page=...);
 * - с extra "anchor" — найдёт страницу по anchor в anchors.json.
 */
public class HelpActivity extends BaseAppCompatActivity {

    private static final String EXTRA_PAGE = "page";
    private static final String EXTRA_ANCHOR = "anchor";

    /** Запускает HelpActivity с конкретной страницей. */
    public static void openPage(Context context, String page) {
        Intent i = new Intent(context, HelpActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (page != null) i.putExtra(EXTRA_PAGE, page);
        context.startActivity(i);
    }

    /** Запускает HelpActivity по anchor (страница ищется в anchors.json). */
    public static void openAnchor(Context context, String anchor) {
        Intent i = new Intent(context, HelpActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (anchor != null) i.putExtra(EXTRA_ANCHOR, anchor);
        context.startActivity(i);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(getString(R.string.auto_help_activity_001));
        }

        if (savedInstanceState == null) {
            String page = null;
            String anchor = null;
            Intent intent = getIntent();
            if (intent != null) {
                page = intent.getStringExtra(EXTRA_PAGE);
                anchor = intent.getStringExtra(EXTRA_ANCHOR);
            }
            HelpFragment fragment;
            if (page != null && !page.isEmpty()) {
                fragment = HelpFragment.newInstance(page, null);
            } else if (anchor != null && !anchor.isEmpty()) {
                fragment = HelpFragment.newInstanceByAnchor(anchor);
            } else {
                fragment = HelpFragment.newInstance(null, null);
            }
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.help_container, fragment)
                    .commit();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
