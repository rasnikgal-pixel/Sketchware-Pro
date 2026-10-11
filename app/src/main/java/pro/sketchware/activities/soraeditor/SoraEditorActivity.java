package pro.sketchware.activities.soraeditor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.viewpager2.widget.ViewPager2;

import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.tabs.TabLayoutMediator;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pro.sketchware.R;
import pro.sketchware.databinding.ActivitySoraEditorBinding;

/**
 * Редактор SORA с вкладками по файлам проекта.
 */
public class SoraEditorActivity extends BaseAppCompatActivity {

    /** Максимум открытых вкладок. */
    private static final int MAX_TABS = 10;

    private ActivitySoraEditorBinding binding;
    private SoraEditorAdapter adapter;
    private TabLayoutMediator tabMediator;
    private String scId;
    private SoraFileCollector collector;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        enableEdgeToEdgeNoContrast();
        super.onCreate(savedInstanceState);

        binding = ActivitySoraEditorBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        scId = getIntent().getStringExtra("sc_id");
        if (scId == null) {
            finish();
            return;
        }

        collector = new SoraFileCollector(scId);
        adapter = new SoraEditorAdapter(this);
        adapter.setOnModifiedListener((path, modified) -> updateTabTitles());

        binding.viewPager.setAdapter(adapter);
        binding.viewPager.setOffscreenPageLimit(1);

        tabMediator = new TabLayoutMediator(binding.tabLayout, binding.viewPager,
                (tab, position) -> {
                    String path = adapter.getFilePath(position);
                    tab.setText(collector.getShortName(path));
                });
        tabMediator.attach();

        // Обработка кнопки "назад"
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                confirmExit();
            }
        });

        // Открыть базовый набор файлов
        openInitialFiles();
        updateEmptyState();
    }

    private void openInitialFiles() {
        List<String> all = collector.collect();
        if (all.isEmpty()) return;

        // Открыть: AndroidManifest, первый layout, первый java
        List<String> initial = new ArrayList<>();
        for (String path : all) {
            String lower = path.toLowerCase();
            if (lower.endsWith("/androidmanifest.xml")) {
                initial.add(path);
                break;
            }
        }
        String firstLayout = null;
        String firstJava = null;
        for (String path : all) {
            String lower = path.toLowerCase();
            if (firstLayout == null && lower.endsWith(".xml") && lower.contains("/layout/")) {
                firstLayout = path;
            }
            if (firstJava == null && lower.endsWith(".java")) {
                firstJava = path;
            }
        }
        if (firstLayout != null) initial.add(firstLayout);
        if (firstJava != null) initial.add(firstJava);

        if (initial.isEmpty()) {
            initial.add(all.get(0));
        }
        adapter.setFiles(initial);
        updateTabTitles();
    }

    private void updateTabTitles() {
        if (binding == null || adapter == null) return;
        for (int i = 0; i < binding.tabLayout.getTabCount(); i++) {
            var tab = binding.tabLayout.getTabAt(i);
            if (tab == null) continue;
            String path = adapter.getFilePath(i);
            if (path == null) continue;
            SoraEditorFragment f = adapter.getFragmentAt(i);
            boolean modified = f != null && f.isModified();
            String name = collector.getShortName(path);
            tab.setText(modified ? name + " *" : name);
        }
    }

    private void updateEmptyState() {
        boolean empty = adapter.getItemCount() == 0;
        binding.emptyState.setVisibility(empty ? View.VISIBLE : View.GONE);
        binding.viewPager.setVisibility(empty ? View.GONE : View.VISIBLE);
        binding.tabLayout.setVisibility(empty ? View.GONE : View.VISIBLE);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sora_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveCurrent();
            return true;
        } else if (id == R.id.action_undo) {
            SoraEditorFragment f = currentFragment();
            if (f != null) f.undo();
            return true;
        } else if (id == R.id.action_redo) {
            SoraEditorFragment f = currentFragment();
            if (f != null) f.redo();
            return true;
        } else if (id == R.id.action_open_file) {
            showOpenFileDialog();
            return true;
        } else if (id == R.id.action_close_tab) {
            closeCurrentTab();
            return true;
        } else if (id == R.id.action_word_wrap) {
            SoraEditorFragment f = currentFragment();
            if (f != null && f.getView() != null) {
                var ed = f.getView().findViewById(R.id.editor);
                if (ed instanceof io.github.rosemoe.sora.widget.CodeEditor) {
                    var ce = (io.github.rosemoe.sora.widget.CodeEditor) ed;
                    ce.setWordwrap(!ce.isWordwrap());
                }
            }
            return true;
        } else if (id == R.id.action_theme) {
            showThemeDialog();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private SoraEditorFragment currentFragment() {
        return adapter.getFragmentAt(binding.viewPager.getCurrentItem());
    }

    private void saveCurrent() {
        SoraEditorFragment f = currentFragment();
        if (f == null) return;
        if (f.save()) {
            updateTabTitles();
        }
    }

    private void closeCurrentTab() {
        int index = binding.viewPager.getCurrentItem();
        SoraEditorFragment f = adapter.getFragmentAt(index);
        if (f != null && f.isModified()) {
            new MaterialAlertDialogBuilder(this)
                    .setTitle(R.string.sora_xml_editor_unsaved)
                    .setMessage(R.string.sora_xml_editor_confirm_close)
                    .setPositiveButton(R.string.sora_xml_editor_action_close_tab,
                            (d, w) -> doCloseTab(index))
                    .setNegativeButton(R.string.common_word_cancel, null)
                    .show();
        } else {
            doCloseTab(index);
        }
    }

    private void doCloseTab(int index) {
        adapter.removeFileAt(index);
        updateTabTitles();
        updateEmptyState();
    }

    private void showOpenFileDialog() {
        final List<String> all = collector.collect();
        if (all.isEmpty()) return;

        // Убираем уже открытые
        List<String> already = adapter.getFilePaths();
        List<String> available = new ArrayList<>();
        for (String p : all) {
            if (!already.contains(p)) available.add(p);
        }

        if (available.isEmpty()) {
            new MaterialAlertDialogBuilder(this)
                    .setMessage(R.string.sora_xml_editor_empty)
                    .setPositiveButton(android.R.string.ok, null)
                    .show();
            return;
        }

        String[] items = new String[available.size()];
        for (int i = 0; i < available.size(); i++) {
            items[i] = collector.getShortName(available.get(i));
        }

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.sora_xml_editor_file_picker_title)
                .setItems(items, (d, which) -> {
                    if (adapter.getItemCount() >= MAX_TABS) {
                        new MaterialAlertDialogBuilder(this)
                                .setMessage(R.string.sora_xml_editor_tab_limit)
                                .setPositiveButton(android.R.string.ok, null)
                                .show();
                        return;
                    }
                    adapter.addFile(available.get(which));
                    updateTabTitles();
                    updateEmptyState();
                    binding.viewPager.setCurrentItem(adapter.getItemCount() - 1);
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    private void showThemeDialog() {
        SoraEditorFragment f = currentFragment();
        if (f == null || f.getView() == null) return;
        var ed = f.getView().findViewById(R.id.editor);
        if (!(ed instanceof io.github.rosemoe.sora.widget.CodeEditor)) return;
        var ce = (io.github.rosemoe.sora.widget.CodeEditor) ed;
        String[] themes = new String[]{"Default", "GitHub", "Eclipse", "Darcula", "VS2019", "NotepadXX"};
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.sora_xml_editor_action_theme)
                .setItems(themes, (d, which) -> {
                    mod.hey.studios.code.SrcCodeEditor.selectTheme(ce, which);
                })
                .show();
    }

    private void confirmExit() {
        List<String> modified = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            SoraEditorFragment f = adapter.getFragmentAt(i);
            if (f != null && f.isModified()) {
                modified.add(f.getFilePath());
            }
        }
        if (modified.isEmpty()) {
            finish();
            return;
        }
        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.sora_xml_editor_unsaved)
                .setMessage(R.string.sora_xml_editor_confirm_close)
                .setPositiveButton(R.string.sora_xml_editor_action_close_tab,
                        (d, w) -> finish())
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    @Override
    protected void onDestroy() {
        if (tabMediator != null) tabMediator.detach();
        super.onDestroy();
    }
}
