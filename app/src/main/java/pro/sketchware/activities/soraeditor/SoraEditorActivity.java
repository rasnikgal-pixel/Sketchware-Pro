package pro.sketchware.activities.soraeditor;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
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

        // Свайп между вкладками: только если включён перенос строк
        applySwipeSetting();

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
        pro.sketchware.smartdrop.DebugLogger.get(this)
                .d("SoraEditor", "files_found", "count=" + all.size());
        if (all.isEmpty()) return;

        // Логируем первые 5 файлов для диагностики
        for (int i = 0; i < Math.min(5, all.size()); i++) {
            pro.sketchware.smartdrop.DebugLogger.get(this)
                    .d("SoraEditor", "file_" + i, all.get(i));
        }

        // Открыть: AndroidManifest, первый layout, первый java
        List<String> initial = new ArrayList<>();
        for (String path : all) {
            String lower = path.toLowerCase();
            if (lower.endsWith("/androidmanifest.xml") || lower.endsWith("\\androidmanifest.xml")) {
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
        pro.sketchware.smartdrop.DebugLogger.get(this)
                .d("SoraEditor", "initial_tabs", "count=" + initial.size());
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
            SoraEditorFragment f = findFragmentAt(i);
            boolean modified = f != null && f.isModified();
            String name = shortName(path);
            tab.setText(modified ? name + " *" : name);
            // Двойной тап по вкладке → копировать путь
            if (tab.view != null) {
                tab.view.setOnTouchListener(new android.view.View.OnTouchListener() {
                    private long lastTap = 0L;
                    @Override
                    public boolean onTouch(android.view.View v, android.view.MotionEvent event) {
                        if (event.getAction() == android.view.MotionEvent.ACTION_DOWN) {
                            long now = System.currentTimeMillis();
                            if (now - lastTap < 350L) {
                                copyToClipboard(path);
                                return true;
                            }
                            lastTap = now;
                        }
                        return false;
                    }
                });
            }
        }
    }

    /** Короткое имя файла для вкладки (последний сегмент пути). */
    private String shortName(String path) {
        if (path == null) return "";
        int idx = path.lastIndexOf('/');
        return (idx >= 0) ? path.substring(idx + 1) : path;
    }

    /** Копирует путь текущего файла в буфер обмена. */
    private void copyCurrentPathToClipboard() {
        int idx = binding.viewPager.getCurrentItem();
        String path = adapter.getFilePath(idx);
        if (path == null) return;
        copyToClipboard(path);
    }

    private void copyToClipboard(String text) {
        try {
            android.content.ClipboardManager cm = (android.content.ClipboardManager)
                    getSystemService(android.content.Context.CLIPBOARD_SERVICE);
            if (cm != null) {
                cm.setPrimaryClip(android.content.ClipData.newPlainText("SORA path", text));
                android.widget.Toast.makeText(this,
                        getString(R.string.sora_xml_editor_copy_path) + "\n" + text,
                        android.widget.Toast.LENGTH_SHORT).show();
            }
        } catch (Throwable ignored) {
        }
    }

    /** Применить настройку свайпа между вкладками. */
    private void applySwipeSetting() {
        try {
            SoraEditorSettings settings = new SoraEditorSettings(this);
            // Свайп работает ТОЛЬКО при включённом word wrap
            binding.viewPager.setUserInputEnabled(settings.isWordWrapEnabled());
        } catch (Throwable ignored) {
        }
    }

    /** Диалог настроек редактора. */
    private void showSettingsDialog() {
        SoraEditorSettings settings = new SoraEditorSettings(this);
        android.view.View view = getLayoutInflater().inflate(
                R.layout.dialog_sora_settings, null);
        com.google.android.material.materialswitch.MaterialSwitch swWrap =
                view.findViewById(R.id.switch_word_wrap);
        android.widget.SeekBar seekBar = view.findViewById(R.id.seek_font_size);
        android.widget.TextView tvSize = view.findViewById(R.id.tv_font_size_value);

        // Скрыть переключатель XML-подсветки (отключена)
        android.view.View swXml = view.findViewById(R.id.switch_xml_highlight);
        if (swXml != null) swXml.setVisibility(android.view.View.GONE);

        swWrap.setChecked(settings.isWordWrapEnabled());
        seekBar.setMax(24); // 8..32
        seekBar.setProgress(settings.getFontSize() - 8);
        tvSize.setText(String.valueOf(settings.getFontSize()));

        seekBar.setOnSeekBarChangeListener(new android.widget.SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(android.widget.SeekBar sb, int progress, boolean fromUser) {
                tvSize.setText(String.valueOf(progress + 8));
            }
            @Override public void onStartTrackingTouch(android.widget.SeekBar sb) {}
            @Override public void onStopTrackingTouch(android.widget.SeekBar sb) {}
        });

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.sora_xml_editor_settings_title)
                .setView(view)
                .setPositiveButton(android.R.string.ok, (d, w) -> {
                    settings.setWordWrapEnabled(swWrap.isChecked());
                    settings.setFontSize(seekBar.getProgress() + 8);
                    // Применить к текущему фрагменту
                    SoraEditorFragment f = findFragmentAt(binding.viewPager.getCurrentItem());
                    if (f != null && f.getView() != null) {
                        android.view.View ed = f.getView().findViewById(R.id.editor);
                        if (ed instanceof io.github.rosemoe.sora.widget.CodeEditor) {
                            io.github.rosemoe.sora.widget.CodeEditor ce =
                                    (io.github.rosemoe.sora.widget.CodeEditor) ed;
                            try { ce.setTextSize(settings.getFontSize()); } catch (Throwable ignored) {}
                            try { ce.setWordwrap(settings.isWordWrapEnabled()); } catch (Throwable ignored) {}
                        }
                    }
                    // Обновить свайп
                    applySwipeSetting();
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
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
        } else if (id == R.id.action_project_search) {
            showProjectSearchDialog();
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
        } else if (id == R.id.action_copy_path) {
            copyCurrentPathToClipboard();
            return true;
        } else if (id == R.id.action_settings) {
            showSettingsDialog();
            return true;
        } else if (id == R.id.action_user_code) {
            android.content.Intent intent = new android.content.Intent(this,
                    pro.sketchware.activities.usercode.UserCodeActivity.class);
            intent.putExtra("sc_id", scId);
            startActivity(intent);
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private SoraEditorFragment currentFragment() {
        return findFragmentAt(binding.viewPager.getCurrentItem());
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
        SoraEditorFragment f = findFragmentAt(index);
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

    /** Диалог ввода текста для поиска по проекту. */
    private void showProjectSearchDialog() {
        final android.widget.EditText input = new android.widget.EditText(this);
        input.setHint(R.string.sora_project_search_hint);
        input.setSingleLine(true);
        int pad = (int) (16 * getResources().getDisplayMetrics().density);
        android.widget.FrameLayout wrap = new android.widget.FrameLayout(this);
        wrap.setPadding(pad, pad / 2, pad, 0);
        wrap.addView(input, new android.widget.FrameLayout.LayoutParams(
                android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

        new MaterialAlertDialogBuilder(this)
                .setTitle(R.string.sora_project_search_title)
                .setView(wrap)
                .setPositiveButton(R.string.common_word_search, (d, w) -> {
                    String q = input.getText().toString();
                    if (!q.isEmpty()) {
                        runProjectSearch(q);
                    }
                })
                .setNegativeButton(R.string.common_word_cancel, null)
                .show();
    }

    /** Фоновый поиск по всем файлам проекта. */
    private void runProjectSearch(String query) {
        final String needle = query.toLowerCase(java.util.Locale.ROOT);
        new Thread(() -> {
            java.util.List<String> files = collector.collect();
            final java.util.List<String[]> hits = new java.util.ArrayList<>();
            final int MAX_HITS = 500;
            for (String path : files) {
                if (hits.size() >= MAX_HITS) break;
                try {
                    java.io.BufferedReader br = new java.io.BufferedReader(
                            new java.io.InputStreamReader(
                                    new java.io.FileInputStream(path), "UTF-8"));
                    String line;
                    int lineNo = 0;
                    while ((line = br.readLine()) != null) {
                        lineNo++;
                        if (line.toLowerCase(java.util.Locale.ROOT).contains(needle)) {
                            hits.add(new String[]{path, String.valueOf(lineNo), line.trim()});
                            if (hits.size() >= MAX_HITS) break;
                        }
                    }
                    br.close();
                } catch (Throwable ignore) {
                }
            }
            runOnUiThread(() -> showSearchResults(hits));
        }, "sora-project-search").start();
    }

    /** Показывает список результатов поиска. */
    private void showSearchResults(java.util.List<String[]> hits) {
        if (hits.isEmpty()) {
            android.widget.Toast.makeText(this,
                    getString(R.string.sora_project_search_empty),
                    android.widget.Toast.LENGTH_SHORT).show();
            return;
        }
        final String[] items = new String[hits.size()];
        for (int i = 0; i < hits.size(); i++) {
            String[] h = hits.get(i);
            String rel = collector.getShortName(h[0]);
            String preview = h[2].length() > 80 ? h[2].substring(0, 77) + "..." : h[2];
            items[i] = rel + ":" + h[1] + "  " + preview;
        }
        new MaterialAlertDialogBuilder(this)
                .setTitle(getString(R.string.sora_project_search_results, hits.size()))
                .setItems(items, (d, which) -> {
                    String[] h = hits.get(which);
                    openFileAtLine(h[0], Integer.parseInt(h[1]));
                })
                .setNegativeButton(R.string.common_word_close, null)
                .show();
    }

    /** Открывает файл во вкладке и переходит на строку. */
    private void openFileAtLine(String path, int line) {
        if (path == null) return;
        int existing = adapter.getIndexForPath(path);
        if (existing >= 0) {
            binding.viewPager.setCurrentItem(existing, false);
        } else {
            if (adapter.getItemCount() >= MAX_TABS) {
                android.widget.Toast.makeText(this,
                        getString(R.string.sora_xml_editor_toast_max_tabs, MAX_TABS),
                        android.widget.Toast.LENGTH_SHORT).show();
                return;
            }
            adapter.addFile(path);
            binding.viewPager.setCurrentItem(adapter.getItemCount() - 1, false);
        }
        binding.viewPager.postDelayed(() -> {
            SoraEditorFragment f = currentFragment();
            if (f != null) f.goToLine(line - 1);
        }, 200);
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


    /**
     * Находит фрагмент для указанной позиции.
     * ViewPager2 использует FragmentStateAdapter с itemId = путь.hashCode().
     */
    private SoraEditorFragment findFragmentAt(int position) {
        if (position < 0 || position >= adapter.getItemCount()) return null;
        String path = adapter.getFilePath(position);
        if (path == null) return null;
        String tag = "f" + path.hashCode();
        Fragment f = getSupportFragmentManager().findFragmentByTag(tag);
        return (f instanceof SoraEditorFragment) ? (SoraEditorFragment) f : null;
    }

    private void confirmExit() {
        List<String> modified = new ArrayList<>();
        for (int i = 0; i < adapter.getItemCount(); i++) {
            SoraEditorFragment f = findFragmentAt(i);
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
    public void onDestroy() {
        if (tabMediator != null) tabMediator.detach();
        super.onDestroy();
    }
}
