package com.besome.sketch.help;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.besome.sketch.lib.ui.PropertyOneLineItem;
import com.besome.sketch.lib.ui.PropertyTwoLineItem;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import a.a.a.GB;
import a.a.a.bB;
import a.a.a.mB;
import a.a.a.wB;
import mod.hey.studios.util.Helper;
import pro.sketchware.BuildConfig;
import pro.sketchware.R;
import pro.sketchware.databinding.ProgramInfoBinding;
import pro.sketchware.updater.UpdateChecker;
import pro.sketchware.updater.UpdateDialog;
import pro.sketchware.updater.UpdateInfo;

public class ProgramInfoActivity extends BaseAppCompatActivity {

    private static final int ITEM_CHANGELOG = 2;
    private static final int ITEM_4PDA = 3;
    private static final int ITEM_DOCS_LOG = 4;
    private static final int ITEM_GITHUB = 7;
    private static final int ITEM_SYSTEM_INFORMATION = 1;
    private static final int ITEM_OPEN_SOURCE_LICENSES = 15;

    private ProgramInfoBinding binding;

    private void addTwoLineItem(int key, int name, int description) {
        addTwoLineItem(key, Helper.getResString(name), Helper.getResString(description));
    }

    private void addTwoLineItem(int key, int name, int description, boolean hideDivider) {
        addTwoLineItem(key, Helper.getResString(name), Helper.getResString(description), hideDivider);
    }

    private void addTwoLineItem(int key, String name, String description) {
        addTwoLineItem(key, name, description, false);
    }

    private void addTwoLineItem(int key, String name, String description, boolean hideDivider) {
        PropertyTwoLineItem item = new PropertyTwoLineItem(this);
        item.setKey(key);
        item.setName(name);
        item.setDesc(description);
        item.setHideDivider(hideDivider);
        binding.content.addView(item);
        item.setOnClickListener(this::handleItem);
    }

    private void addSingleLineItem(int key, int name) {
        addSingleLineItem(key, Helper.getResString(name));
    }

    private void addSingleLineItem(int key, int name, boolean hideDivider) {
        addSingleLineItem(key, Helper.getResString(name), hideDivider);
    }

    private void addSingleLineItem(int key, String name) {
        addSingleLineItem(key, name, false);
    }

    private void addSingleLineItem(int key, String name, boolean hideDivider) {
        PropertyOneLineItem item = new PropertyOneLineItem(this);
        item.setKey(key);
        item.setName(name);
        item.setHideDivider(hideDivider);
        binding.content.addView(item);
        if (key == ITEM_SYSTEM_INFORMATION || key == ITEM_OPEN_SOURCE_LICENSES) {
            item.setOnClickListener(this::handleItem);
        }
    }

    private void resetDialog(View view) {
        MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
        dialog.setTitle(Helper.getResString(R.string.program_information_reset_system_title));
        dialog.setIcon(R.drawable.rollback_96);
        View rootView = wB.a(this, R.layout.all_init_popup);
        RadioGroup radioGroup = rootView.findViewById(R.id.rg_type);
        ((RadioButton) rootView.findViewById(R.id.rb_all)).setText(Helper.getResString(R.string.program_information_reset_system_title_all_settings_data));
        ((RadioButton) rootView.findViewById(R.id.rb_only_config)).setText(Helper.getResString(R.string.program_information_reset_system_title_all_settings));
        dialog.setView(rootView);
        dialog.setPositiveButton(Helper.getResString(R.string.common_word_yes), (v, which) -> {
            if (!mB.a()) {
                int buttonId = radioGroup.getCheckedRadioButtonId();
                boolean resetOnlySettings = buttonId != R.id.rb_all;
                v.dismiss();
                setResult(RESULT_OK, getIntent().putExtra("onlyConfig", resetOnlySettings));
                finish();
            }
        });
        dialog.setNegativeButton(Helper.getResString(R.string.common_word_cancel), null);
        dialog.show();
    }

    private void showChangelogDialog() {
        // Сначала пробуем загрузить changelog с GitHub (update.json).
        // Если сеть недоступна — fallback на локальный assets/changelog.txt.
        Toast.makeText(this, Helper.getResString(R.string.auto_program_info_activity_001), Toast.LENGTH_SHORT).show();

        new Thread(() -> {
            String remoteText = null;
            try {
                java.net.URL u = new java.net.URL(
                    "https://raw.githubusercontent.com/rasnikgal-pixel/Sketchware-Pro/main/update.json"
                );
                java.net.HttpURLConnection c = (java.net.HttpURLConnection) u.openConnection();
                c.setConnectTimeout(5000);
                c.setReadTimeout(7000);
                c.setRequestMethod("GET");
                c.connect();
                if (c.getResponseCode() == 200) {
                    StringBuilder sb = new StringBuilder();
                    try (BufferedReader r = new BufferedReader(
                            new InputStreamReader(c.getInputStream(), StandardCharsets.UTF_8))) {
                        String line;
                        while ((line = r.readLine()) != null) sb.append(line);
                    }
                    org.json.JSONObject obj = new org.json.JSONObject(sb.toString());
                    if (obj.has("changelog")) {
                        remoteText = obj.getString("changelog");
                    }
                }
                c.disconnect();
            } catch (Throwable ignored) { /* fallback ниже */ }

            final String text;
            if (remoteText != null && !remoteText.isEmpty()) {
                text = remoteText;
            } else {
                text = readLocalChangelog();
            }

            runOnUiThread(() -> {
                MaterialAlertDialogBuilder dialog = new MaterialAlertDialogBuilder(this);
                dialog.setTitle(Helper.getResString(R.string.auto_program_info_activity_002));
                dialog.setMessage(text);
                dialog.setPositiveButton(Helper.getResString(R.string.common_word_ok), null);
                dialog.show();
            });
        }).start();
    }

    private String readLocalChangelog() {
        try {
            InputStream is = getAssets().open("changelog.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            return Helper.getResString(R.string.auto_program_info_activity_003);
        }
    }

    private void checkUpdatesNow() {
        Toast.makeText(this, Helper.getResString(R.string.auto_program_info_activity_004), Toast.LENGTH_SHORT).show();
        new UpdateChecker().checkIfNeeded(
                this,
                BuildConfig.VERSION_CODE,
                true,
                new UpdateChecker.Callback() {
                    @Override
                    public void onUpdateAvailable(UpdateInfo info) {
                        runOnUiThread(() -> UpdateDialog.show(ProgramInfoActivity.this, info));
                    }

                    @Override
                    public void onUpToDate() {
                        runOnUiThread(() -> Toast.makeText(
                                ProgramInfoActivity.this,
                                Helper.getResString(R.string.auto_program_info_activity_005),
                                Toast.LENGTH_SHORT).show());
                    }

                    @Override
                    public void onError(String message) {
                        runOnUiThread(() -> Toast.makeText(
                                ProgramInfoActivity.this,
                                Helper.getResString(R.string.auto_program_info_activity_006),
                                Toast.LENGTH_SHORT).show());
                    }
                }
        );
    }

    private void handleItem(View v) {
        if (!mB.a()) {
            int key;
            if (v instanceof PropertyOneLineItem) {
                key = ((PropertyOneLineItem) v).getKey();
                switch (key) {
                    case ITEM_SYSTEM_INFORMATION -> toSystemInfoActivity();
                    case ITEM_OPEN_SOURCE_LICENSES -> {
                        if (!GB.h(getApplicationContext())) {
                            bB.a(getApplicationContext(), Helper.getResString(R.string.common_message_check_network), bB.TOAST_NORMAL).show();
                        } else {
                            toLicenseActivity();
                        }
                    }
                }
            }

            if (v instanceof PropertyTwoLineItem) {
                key = ((PropertyTwoLineItem) v).getKey();
                switch (key) {
                    case ITEM_CHANGELOG -> showChangelogDialog();
                    case ITEM_4PDA -> openUrl(Helper.getResString(R.string.link_russian_4pda));
                    case ITEM_DOCS_LOG -> openUrl(Helper.getResString(R.string.link_russian_help));
                    case ITEM_GITHUB -> openUrl(Helper.getResString(R.string.link_github_url));
                }
            }
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ProgramInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.toolbar.setNavigationOnClickListener(Helper.getBackPressedClickListener(this));
        binding.appVersion.setText(GB.e(getApplicationContext()));
        binding.btnReset.setOnClickListener(this::resetDialog);
        binding.btnUpgrade.setOnClickListener(v -> checkUpdatesNow());

        addTwoLineItem(ITEM_CHANGELOG, Helper.getResString(R.string.auto_program_info_activity_007), Helper.getResString(R.string.auto_program_info_activity_008));
        addTwoLineItem(ITEM_4PDA, "Sketchware Pro 4PDA", Helper.getResString(R.string.link_russian_4pda));
        addTwoLineItem(ITEM_DOCS_LOG, Helper.getResString(R.string.auto_program_info_activity_009), Helper.getResString(R.string.link_russian_help));
        addTwoLineItem(ITEM_GITHUB, Helper.getResString(R.string.auto_program_info_activity_010), Helper.getResString(R.string.link_github_url));
        addSingleLineItem(ITEM_SYSTEM_INFORMATION, R.string.program_information_title_system_information);
        addSingleLineItem(ITEM_OPEN_SOURCE_LICENSES, R.string.program_information_title_open_source_license, true);
    }

    private void toLicenseActivity() {
        Intent intent = new Intent(getApplicationContext(), LicenseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void toSystemInfoActivity() {
        Intent intent = new Intent(getApplicationContext(), SystemInfoActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    private void openUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
