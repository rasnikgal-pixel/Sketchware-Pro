package mod.hilal.saif.activities.tools;

import static pro.sketchware.utility.GsonUtils.getGson;

import android.content.DialogInterface;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.Toast;
import pro.sketchware.smartdrop.DebugLogger;
import pro.sketchware.smartdrop.LogViewerActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceDataStore;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreferenceCompat;

import com.besome.sketch.lib.base.BaseAppCompatActivity;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.JsonParseException;
import com.topjohnwu.superuser.Shell;

import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mod.hey.studios.util.Helper;
import mod.jbk.util.LogUtil;
import pro.sketchware.R;
import pro.sketchware.databinding.DialogCreateNewFileLayoutBinding;
import pro.sketchware.databinding.PreferenceActivityBinding;
import pro.sketchware.utility.FileUtil;
import pro.sketchware.utility.SketchwareUtil;

public class ConfigActivity extends BaseAppCompatActivity {

    public static final File SETTINGS_FILE = new File(FileUtil.getExternalStorageDir(), ".sketchware/data/settings.json");
    public static final String SETTING_ALWAYS_SHOW_BLOCKS = "always-show-blocks";
    public static final String SETTING_BACKUP_DIRECTORY = "backup-dir";
    public static final String SETTING_ROOT_AUTO_INSTALL_PROJECTS = "root-auto-install-projects";
    public static final String SETTING_ROOT_AUTO_OPEN_AFTER_INSTALLING = "root-auto-open-after-installing";
    public static final String SETTING_BACKUP_FILENAME = "backup-filename";
    public static final String SETTING_SHOW_BUILT_IN_BLOCKS = "built-in-blocks";
    public static final String SETTING_SHOW_EVERY_SINGLE_BLOCK = "show-every-single-block";
    public static final String SETTING_SHOW_HEADERS_WHEN_SEARCHING_BLOCKS = "show-headers-when-searching-blocks";
    public static final String SETTING_BLOCK_LOGIC_CHECK = "block-logic-check";
    public static final String SETTING_BLOCK_LOGIC_CHECK_BEFORE_BUILD = "block-logic-check-before-build";
    public static final String SETTING_AUTO_BACKUP_ENABLED = "auto-backup-enabled";
    public static final String SETTING_AUTO_BACKUP_KEEP_COUNT = "auto-backup-keep-count";
    public static final String SETTING_BLOCK_TEMPLATES = "block-templates";
    public static final String SETTING_USE_NEW_VERSION_CONTROL = "use-new-version-control";
    public static final String SETTING_USE_ASD_HIGHLIGHTER = "use-asd-highlighter";
    public static final String SETTING_CRITICAL_UPDATE_REMINDER = "critical-update-reminder";
    public static final String SETTING_BLOCKMANAGER_DIRECTORY_PALETTE_FILE_PATH = "palletteDir";
    public static final String SETTING_SMARTDROP_UPDATE_ALL_BLOCKS = "smartdrop-update-all-blocks";
    public static final String SETTING_SMARTDROP_FORCE_REPLACE = "smartdrop-force-replace";
    public static final String SETTING_BLOCKMANAGER_DIRECTORY_BLOCK_FILE_PATH = "blockDir";
    // SkProj export settings
    public static final String SETTING_SKPROJ_INCLUDE_COMPONENTS = "skproj-include-components";
    public static final String SETTING_SKPROJ_INCLUDE_VARIABLES = "skproj-include-variables";
    public static final String SETTING_SKPROJ_INCLUDE_RESOURCES = "skproj-include-resources";
    public static final String SETTING_SKPROJ_INCLUDE_LOCAL_LIBS = "skproj-include-local-libs";
    public static final String SETTING_SKPROJ_INCLUDE_APK = "skproj-include-apk";
    public static final String SETTING_SKPROJ_INCLUDE_CUSTOM_BLOCKS = "skproj-include-custom-blocks";
    public static final String SETTING_SKPROJ_FORMAT_ZIP = "skproj-format-zip";
    public static final String SETTING_SKPROJ_OUTPUT_DIR = "skproj-output-dir";

    public static String getBackupPath() {
        return DataStore.getInstance().getString(SETTING_BACKUP_DIRECTORY, "/.sketchware/backups/");
    }

    public static String getStringSettingValueOrSetAndGet(String settingKey, String toReturnAndSetIfNotFound) {
        var dataStore = DataStore.getInstance();
        Map<String, Object> settings = dataStore.getSettings();

        Object value = settings.get(settingKey);
        if (value instanceof String s) {
            return s;
        } else {
            dataStore.putString(settingKey, toReturnAndSetIfNotFound);
            dataStore.persist();

            return toReturnAndSetIfNotFound;
        }
    }

    public static String getBackupFileName() {
        return DataStore.getInstance().getString(SETTING_BACKUP_FILENAME, "$projectName v$versionName ($pkgName, $versionCode) $time(yyyy-MM-dd'T'HHmmss)");
    }

    public static boolean isSettingEnabled(String keyName) {
        return DataStore.getInstance().getBoolean(keyName, false);
    }

    // === SkProj export helpers ===

    public static boolean isSkprojIncludeComponents() {
        return DataStore.getInstance().getBoolean(SETTING_SKPROJ_INCLUDE_COMPONENTS, true);
    }

    public static boolean isSkprojIncludeVariables() {
        return DataStore.getInstance().getBoolean(SETTING_SKPROJ_INCLUDE_VARIABLES, true);
    }

    public static boolean isSkprojIncludeResources() {
        return DataStore.getInstance().getBoolean(SETTING_SKPROJ_INCLUDE_RESOURCES, true);
    }

    public static boolean isSkprojIncludeLocalLibs() {
        return DataStore.getInstance().getBoolean(SETTING_SKPROJ_INCLUDE_LOCAL_LIBS, false);
    }

    public static boolean isSkprojIncludeApk() {
        return DataStore.getInstance().getBoolean(SETTING_SKPROJ_INCLUDE_APK, false);
    }

    public static boolean isSkprojIncludeCustomBlocks() {
        return DataStore.getInstance().getBoolean(SETTING_SKPROJ_INCLUDE_CUSTOM_BLOCKS, true);
    }

    public static boolean isSkprojFormatZip() {
        return DataStore.getInstance().getBoolean(SETTING_SKPROJ_FORMAT_ZIP, true);
    }

    public static String getSkprojOutputDir() {
        return DataStore.getInstance().getString(SETTING_SKPROJ_OUTPUT_DIR, "/.sketchware/backups/exports/");
    }

    /** @return true, если автобэкап после успешной сборки включён */
    public static boolean isAutoBackupEnabled() {
        return DataStore.getInstance().getBoolean(SETTING_AUTO_BACKUP_ENABLED, true);
    }

    /** @return сколько последних бэкапов хранить (0 — без ограничений) */
    public static int getAutoBackupKeepCount() {
        try {
            return Integer.parseInt(DataStore.getInstance().getString(SETTING_AUTO_BACKUP_KEEP_COUNT, "5"));
        } catch (Throwable ignored) {
            return 5;
        }
    }

    /** @return true если SmartDrop должен обновлять все блоки того же типа */
    public static boolean isSmartDropUpdateAllEnabled() {
        return DataStore.getInstance().getBoolean(SETTING_SMARTDROP_UPDATE_ALL_BLOCKS, true);
    }

    /** @return true если SmartDrop заменяет параметры без подтверждения */
    public static boolean isSmartDropForceReplaceEnabled() {
        return DataStore.getInstance().getBoolean(SETTING_SMARTDROP_FORCE_REPLACE, true);
    }

    public static void setSetting(String key, Object value) {
        var dataStore = DataStore.getInstance();
        if (value instanceof String s) {
            dataStore.putString(key, s);
        } else if (value instanceof Boolean b) {
            dataStore.putBoolean(key, b);
        } else {
            throw new IllegalArgumentException("Unhandled data type " + value.getClass());
        }
        dataStore.persist();
    }

    @NonNull
    private static HashMap<String, Object> readSettings() {
        HashMap<String, Object> settings;

        if (SETTINGS_FILE.exists()) {
            Exception toLog;

            try {
                settings = getGson().fromJson(FileUtil.readFile(SETTINGS_FILE.getAbsolutePath()), Helper.TYPE_MAP);

                if (settings != null) {
                    // Миграция: добавляем недостающие ключи со значениями по умолчанию
                    migrateMissingDefaults(settings);
                    return settings;
                }

                toLog = new NullPointerException("settings == null");
                // fall-through to shared error handler
            } catch (JsonParseException e) {
                toLog = e;
                // fall-through to shared error handler
            }

            SketchwareUtil.toastError("Не удалось прочитать настройки приложения! Восстановлены значения по умолчанию.");
            LogUtil.e("ConfigActivity", "Failed to parse App Settings.", toLog);
        }
        settings = new HashMap<>();
        restoreDefaultSettings(settings);

        return settings;
    }

    private static void restoreDefaultSettings(HashMap<String, Object> settings) {
        settings.clear();

        List<String> keys = Arrays.asList(SETTING_ALWAYS_SHOW_BLOCKS,
                SETTING_BACKUP_DIRECTORY,
                SETTING_ROOT_AUTO_INSTALL_PROJECTS,
                SETTING_ROOT_AUTO_OPEN_AFTER_INSTALLING,
                SETTING_SHOW_BUILT_IN_BLOCKS,
                SETTING_SHOW_EVERY_SINGLE_BLOCK,
                SETTING_USE_NEW_VERSION_CONTROL,
                SETTING_USE_ASD_HIGHLIGHTER,
                SETTING_SHOW_HEADERS_WHEN_SEARCHING_BLOCKS,
                SETTING_BLOCK_LOGIC_CHECK,
                SETTING_BLOCK_LOGIC_CHECK_BEFORE_BUILD,
                SETTING_AUTO_BACKUP_ENABLED,
                SETTING_AUTO_BACKUP_KEEP_COUNT,
                SETTING_SKPROJ_INCLUDE_COMPONENTS,
                SETTING_SKPROJ_INCLUDE_VARIABLES,
                SETTING_SKPROJ_INCLUDE_RESOURCES,
                SETTING_SKPROJ_INCLUDE_LOCAL_LIBS,
                SETTING_SKPROJ_INCLUDE_APK,
                SETTING_SKPROJ_INCLUDE_CUSTOM_BLOCKS,
                SETTING_SKPROJ_FORMAT_ZIP,
                SETTING_SKPROJ_OUTPUT_DIR,
                SETTING_BLOCK_TEMPLATES,
                SETTING_BLOCKMANAGER_DIRECTORY_PALETTE_FILE_PATH,
                SETTING_BLOCKMANAGER_DIRECTORY_BLOCK_FILE_PATH);

        for (String key : keys) {
            settings.put(key, getDefaultValue(key));
        }
        FileUtil.writeFile(SETTINGS_FILE.getAbsolutePath(), getGson().toJson(settings));
    }

    /**
     * Миграция настроек: добавляет недостающие ключи со значениями по умолчанию.
     * Нужно при добавлении новых настроек — у старых пользователей файл уже есть,
     * но нового ключа в нём нет. Сохраняем файл, только если что-то добавили.
     */
    private static void migrateMissingDefaults(HashMap<String, Object> settings) {
        List<String> keys = Arrays.asList(
                SETTING_ALWAYS_SHOW_BLOCKS,
                SETTING_BACKUP_DIRECTORY,
                SETTING_ROOT_AUTO_INSTALL_PROJECTS,
                SETTING_ROOT_AUTO_OPEN_AFTER_INSTALLING,
                SETTING_SHOW_BUILT_IN_BLOCKS,
                SETTING_SHOW_EVERY_SINGLE_BLOCK,
                SETTING_USE_NEW_VERSION_CONTROL,
                SETTING_USE_ASD_HIGHLIGHTER,
                SETTING_SHOW_HEADERS_WHEN_SEARCHING_BLOCKS,
                SETTING_BLOCK_LOGIC_CHECK,
                SETTING_BLOCK_LOGIC_CHECK_BEFORE_BUILD,
                SETTING_BLOCK_TEMPLATES,
                SETTING_BLOCKMANAGER_DIRECTORY_PALETTE_FILE_PATH,
                SETTING_BLOCKMANAGER_DIRECTORY_BLOCK_FILE_PATH);

        boolean changed = false;
        for (String key : keys) {
            if (!settings.containsKey(key)) {
                settings.put(key, getDefaultValue(key));
                changed = true;
            }
        }
        if (changed) {
            try {
                FileUtil.writeFile(SETTINGS_FILE.getAbsolutePath(), getGson().toJson(settings));
            } catch (Throwable ignored) {}
        }
    }

    public static Object getDefaultValue(String key) {
        return switch (key) {
            case SETTING_SHOW_HEADERS_WHEN_SEARCHING_BLOCKS -> true;
            case SETTING_BLOCK_LOGIC_CHECK -> true;
            case SETTING_BLOCK_LOGIC_CHECK_BEFORE_BUILD -> true;
            case SETTING_AUTO_BACKUP_ENABLED -> true;
            case SETTING_AUTO_BACKUP_KEEP_COUNT -> "5";
            case SETTING_SKPROJ_INCLUDE_COMPONENTS -> true;
            case SETTING_SKPROJ_INCLUDE_VARIABLES -> true;
            case SETTING_SKPROJ_INCLUDE_RESOURCES -> true;
            case SETTING_SKPROJ_INCLUDE_LOCAL_LIBS -> false;
            case SETTING_SKPROJ_INCLUDE_APK -> false;
            case SETTING_SKPROJ_INCLUDE_CUSTOM_BLOCKS -> true;
            case SETTING_SKPROJ_FORMAT_ZIP -> true;
            case SETTING_SKPROJ_OUTPUT_DIR -> "/.sketchware/backups/exports/";
            case SETTING_BLOCK_TEMPLATES -> true;
            case SETTING_ALWAYS_SHOW_BLOCKS,
                 SETTING_ROOT_AUTO_INSTALL_PROJECTS, SETTING_SHOW_BUILT_IN_BLOCKS,
                 SETTING_SHOW_EVERY_SINGLE_BLOCK, SETTING_USE_NEW_VERSION_CONTROL,
                 SETTING_USE_ASD_HIGHLIGHTER -> false;
            case SETTING_BACKUP_DIRECTORY -> "/.sketchware/backups/";
            case SETTING_ROOT_AUTO_OPEN_AFTER_INSTALLING -> true;
            case SETTING_BLOCKMANAGER_DIRECTORY_PALETTE_FILE_PATH ->
                    "/.sketchware/resources/block/My Block/palette.json";
            case SETTING_BLOCKMANAGER_DIRECTORY_BLOCK_FILE_PATH ->
                    "/.sketchware/resources/block/My Block/block.json";
            default -> throw new IllegalArgumentException("Unknown key '" + key + "'!");
        };
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        enableEdgeToEdgeNoContrast();
        super.onCreate(savedInstanceState);
        var binding = PreferenceActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.topAppBar.setTitle("Настройки приложения");
        binding.topAppBar.setNavigationOnClickListener(Helper.getBackPressedClickListener(this));
        var fragment = new PreferenceFragment();
        fragment.setSnackbarView(binding.getRoot());
        getSupportFragmentManager().beginTransaction()
                .replace(binding.fragmentContainer.getId(), fragment)
                .commit();

        {
            View view1 = binding.appBarLayout;
            int left = view1.getPaddingLeft();
            int top = view1.getPaddingTop();
            int right = view1.getPaddingRight();
            int bottom = view1.getPaddingBottom();

            ViewCompat.setOnApplyWindowInsetsListener(view1, (v, i) -> {
                Insets insets = i.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
                v.setPadding(left + insets.left, top + insets.top, right + insets.right, bottom);
                return i;
            });
        }

        {
            View view1 = binding.fragmentContainer;
            int left = view1.getPaddingLeft();
            int top = view1.getPaddingTop();
            int right = view1.getPaddingRight();
            int bottom = view1.getPaddingBottom();

            ViewCompat.setOnApplyWindowInsetsListener(view1, (v, i) -> {
                Insets insets = i.getInsets(WindowInsetsCompat.Type.systemBars() | WindowInsetsCompat.Type.displayCutout());
                v.setPadding(left + insets.left, top, right + insets.right, bottom + insets.bottom);
                return i;
            });
        }
    }

    public static class PreferenceFragment extends PreferenceFragmentCompat {

        private void exportAppSettings() {
            dev.pranav.filepicker.FilePickerOptions options = new dev.pranav.filepicker.FilePickerOptions();
            options.setSelectionMode(dev.pranav.filepicker.SelectionMode.BOTH);
            options.setMultipleSelection(false);
            options.setTitle("Выберите папку для сохранения");
            options.setInitialDirectory(FileUtil.getExternalStorageDir());

            dev.pranav.filepicker.FilePickerCallback callback = new dev.pranav.filepicker.FilePickerCallback() {
                @Override
                public void onFilesSelected(@org.jetbrains.annotations.NotNull java.util.List<? extends java.io.File> files) {
                    if (files.isEmpty()) return;
                    java.io.File selectedDir = files.get(0);
                    if (!selectedDir.isDirectory()) {
                        selectedDir = selectedDir.getParentFile();
                    }
                    if (selectedDir == null) return;
                    String ts = new java.text.SimpleDateFormat("yyyy-MM-dd_HHmmss", java.util.Locale.US).format(new java.util.Date());
                    java.io.File dest = new java.io.File(selectedDir, "settings_backup_" + ts + ".json");
                    try {
                        pro.sketchware.utility.FileUtil.copyFile(SETTINGS_FILE.getAbsolutePath(), dest.getAbsolutePath());
                        android.widget.Toast.makeText(requireContext(), "Настройки сохранены в " + dest.getAbsolutePath(), android.widget.Toast.LENGTH_LONG).show();
                    } catch (Throwable t) {
                        android.widget.Toast.makeText(requireContext(), "Ошибка экспорта: " + t.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                    }
                }
            };
            new dev.pranav.filepicker.FilePickerDialogFragment(options, callback).show(getChildFragmentManager(), "settings_export");
        }

        private void importAppSettings() {
            dev.pranav.filepicker.FilePickerOptions options = new dev.pranav.filepicker.FilePickerOptions();
            options.setSelectionMode(dev.pranav.filepicker.SelectionMode.BOTH);
            options.setMultipleSelection(false);
            options.setTitle("Выберите файл настроек");
            options.setInitialDirectory(FileUtil.getExternalStorageDir());

            dev.pranav.filepicker.FilePickerCallback callback = new dev.pranav.filepicker.FilePickerCallback() {
                @Override
                public void onFilesSelected(@org.jetbrains.annotations.NotNull java.util.List<? extends java.io.File> files) {
                    if (files.isEmpty()) return;
                    java.io.File source = files.get(0);
                    if (source.isDirectory()) {
                        android.widget.Toast.makeText(requireContext(), "Нужно выбрать файл, а не папку", android.widget.Toast.LENGTH_LONG).show();
                        return;
                    }
                    try {
                        String json = pro.sketchware.utility.FileUtil.readFile(source.getAbsolutePath());
                        if (json == null || json.trim().isEmpty() || !json.trim().startsWith("{")) {
                            android.widget.Toast.makeText(requireContext(), "Файл не похож на настройки", android.widget.Toast.LENGTH_LONG).show();
                            return;
                        }
                        pro.sketchware.utility.FileUtil.writeFile(SETTINGS_FILE.getAbsolutePath(), json);
                        new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                                .setTitle("Настройки импортированы")
                                .setMessage("Чтобы применить все настройки, приложение нужно перезапустить.\n\nПерезапустить сейчас?")
                                .setPositiveButton("Перезапустить", (d, w) -> {
                                    android.os.Process.killProcess(android.os.Process.myPid());
                                    System.exit(0);
                                })
                                .setNegativeButton("Позже", null)
                                .show();
                    } catch (Throwable t) {
                        android.widget.Toast.makeText(requireContext(), "Ошибка импорта: " + t.getMessage(), android.widget.Toast.LENGTH_LONG).show();
                    }
                }
            };
            new dev.pranav.filepicker.FilePickerDialogFragment(options, callback).show(getChildFragmentManager(), "settings_import");
        }

        private View snackbarView;
        private DataStore dataStore;

        @Override
        public void onCreatePreferences(@Nullable Bundle savedInstanceState, @Nullable String rootKey) {
            dataStore = DataStore.getInstance();
            getPreferenceManager().setPreferenceDataStore(dataStore);
            setPreferencesFromResource(R.xml.preferences_config_activity, rootKey);
            Preference helpPref = findPreference("open-help");
            if (helpPref != null) {
                helpPref.setOnPreferenceClickListener(preference -> {
                    com.besome.sketch.help.HelpOpener.open(
                            ConfigActivity.this,
                            "block-nastroyki-prilozheniya-vnutrennie-parametry");
                    return true;
                });
            }

            Preference backupDir = findPreference("backup-dir");
            assert backupDir != null;
            backupDir.setOnPreferenceClickListener(preference -> {
                DialogCreateNewFileLayoutBinding binding = DialogCreateNewFileLayoutBinding.inflate(getLayoutInflater());
                binding.inputText.setText(getBackupPath());
                binding.chipGroupTypes.setVisibility(View.GONE);
                AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                        .setView(binding.getRoot())
                        .setTitle("Директория резервных копий")
                        .setMessage("Директория внутри /Internal storage/, например .sketchware/backups")
                        .setNegativeButton(R.string.common_word_cancel, null)
                        .setPositiveButton(R.string.common_word_save, null)
                        .create();

                dialog.setOnShowListener(dialogInterface -> {
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(
                            Helper.getDialogDismissListener(dialogInterface));
                    Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    positiveButton.setOnClickListener(view -> {
                        getDataStore().putString(SETTING_BACKUP_DIRECTORY, Helper.getText(binding.inputText));
                        dialog.dismiss();
                    });

                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    binding.inputText.requestFocus();
                });
                dialog.show();
                return true;
            });

            Preference exportPref = findPreference("export-app-settings");
            if (exportPref != null) {
                exportPref.setOnPreferenceClickListener(preference -> {
                    exportAppSettings();
                    return true;
                });
            }
            Preference importPref = findPreference("import-app-settings");
            if (importPref != null) {
                importPref.setOnPreferenceClickListener(preference -> {
                    importAppSettings();
                    return true;
                });
            }

            SwitchPreferenceCompat installWithRoot = findPreference("root-auto-install-projects");
            assert installWithRoot != null;
            installWithRoot.setOnPreferenceClickListener(preference -> {
                if (installWithRoot.isChecked()) {
                    Shell.getShell(shell -> {
                        if (!shell.isRoot()) {
                            Snackbar.make(snackbarView, "Не удалось получить root-доступ", BaseTransientBottomBar.LENGTH_SHORT).show();
                            installWithRoot.setChecked(false);
                        }
                    });
                }
                return true;
            });

            SwitchPreferenceCompat debugLogEnabled = findPreference("debug-log-enabled");
            if (debugLogEnabled != null) {
                DebugLogger logger = DebugLogger.get(requireContext());
                debugLogEnabled.setChecked(logger.isEnabled());
                debugLogEnabled.setOnPreferenceChangeListener((pref, newValue) -> {
                    DebugLogger.get(requireContext()).setEnabled((Boolean) newValue);
                    return true;
                });
            }
            SwitchPreferenceCompat debugLogVerbose = findPreference("debug-log-verbose");
            if (debugLogVerbose != null) {
                DebugLogger verboseLogger = DebugLogger.get(requireContext());
                debugLogVerbose.setChecked(verboseLogger.isVerbose());
                debugLogVerbose.setOnPreferenceChangeListener((pref, newValue) -> {
                    DebugLogger.get(requireContext()).setVerbose((Boolean) newValue);
                    return true;
                });
            }
            Preference debugLogOpen = findPreference("debug-log-open");
            if (debugLogOpen != null) {
                debugLogOpen.setOnPreferenceClickListener(pref -> {
                    startActivity(new Intent(requireContext(), LogViewerActivity.class));
                    return true;
                });
            }

            // Настройки обновлений
            SwitchPreferenceCompat updatesEnabled = findPreference("updates-check-enabled");
            if (updatesEnabled != null) {
                updatesEnabled.setChecked(pro.sketchware.updater.UpdateChecker.isEnabled(requireContext()));
                updatesEnabled.setOnPreferenceChangeListener((pref, v) -> {
                    pro.sketchware.updater.UpdateChecker.setEnabled(requireContext(), (Boolean) v);
                    return true;
                });
            }

            androidx.preference.ListPreference updatesPeriod = findPreference("updates-check-period");
            if (updatesPeriod != null) {
                updatesPeriod.setValue(pro.sketchware.updater.UpdateChecker.getPeriod(requireContext()));
                updatesPeriod.setOnPreferenceChangeListener((pref, v) -> {
                    pro.sketchware.updater.UpdateChecker.setPeriod(requireContext(), (String) v);
                    return true;
                });
            }

            Preference updatesCheckNow = findPreference("updates-check-now");
            if (updatesCheckNow != null) {
                updatesCheckNow.setOnPreferenceClickListener(pref -> {
                    android.widget.Toast.makeText(requireContext(), "Проверка обновлений...", android.widget.Toast.LENGTH_SHORT).show();
                    new pro.sketchware.updater.UpdateChecker().checkIfNeeded(
                            requireContext(),
                            pro.sketchware.BuildConfig.VERSION_CODE,
                            true,
                            new pro.sketchware.updater.UpdateChecker.Callback() {
                                @Override public void onUpdateAvailable(pro.sketchware.updater.UpdateInfo info) {
                                    if (getActivity() != null)
                                        pro.sketchware.updater.UpdateDialog.show(getActivity(), info);
                                }
                                @Override public void onUpToDate() {
                                    android.widget.Toast.makeText(requireContext(), "Обновлений нет", android.widget.Toast.LENGTH_SHORT).show();
                                }
                                @Override public void onError(String message) {
                                    android.widget.Toast.makeText(requireContext(), "Ошибка: " + message, android.widget.Toast.LENGTH_SHORT).show();
                                }
                            }
                    );
                    return true;
                });
            }

            // Иконки и разделы
            SwitchPreferenceCompat iconsShow = findPreference("icons-show-enabled");
            if (iconsShow != null) {
                SharedPreferences iconPrefs = requireContext().getSharedPreferences("icon_picker_prefs", android.content.Context.MODE_PRIVATE);
                iconsShow.setChecked(iconPrefs.getBoolean("icons_show_enabled", true));
                iconsShow.setOnPreferenceChangeListener((pref, v) -> {
                    iconPrefs.edit().putBoolean("icons_show_enabled", (Boolean) v).apply();
                    return true;
                });
            }
            SwitchPreferenceCompat iconsMixed = findPreference("icons-mixed-search");
            if (iconsMixed != null) {
                SharedPreferences iconPrefs = requireContext().getSharedPreferences("icon_picker_prefs", android.content.Context.MODE_PRIVATE);
                iconsMixed.setChecked(iconPrefs.getBoolean("mixed_search", false));
                iconsMixed.setOnPreferenceChangeListener((pref, v) -> {
                    iconPrefs.edit().putBoolean("mixed_search", (Boolean) v).apply();
                    return true;
                });
            }

            Preference backupFilename = findPreference("backup-filename");
            assert backupFilename != null;
            backupFilename.setOnPreferenceClickListener(preference -> {
                DialogCreateNewFileLayoutBinding binding = DialogCreateNewFileLayoutBinding.inflate(getLayoutInflater());
                binding.chipGroupTypes.setVisibility(View.GONE);
                binding.inputText.setText(getBackupFileName());

                AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                        .setView(binding.getRoot())
                        .setTitle("Формат имени файла резервной копии")
                        .setMessage("Это определяет, как именуются файлы резервных копий SWB.\n" +
                                "Available variables:\n" +
                                " - $projectName - Project name\n" +
                                " - $versionCode - App version code\n" +
                                " - $versionName - App version name\n" +
                                " - $pkgName - App package name\n" +
                                " - $timeInMs - Time during backup in milliseconds\n" +
                                "\n" +
                                "Additionally, you can format your own time like this using Java's date formatter syntax:\n" +
                                "$time(yyyy-MM-dd'T'HHmmss)\n")
                        .setNegativeButton(R.string.common_word_cancel, null)
                        .setPositiveButton(R.string.common_word_save, null)
                        .setNeutralButton(R.string.common_word_reset, (dialogInterface, which) -> {
                            getDataStore().putString(SETTING_BACKUP_FILENAME, null);
                            Snackbar.make(snackbarView, "Сброс к значениям по умолчанию завершён.", BaseTransientBottomBar.LENGTH_SHORT).show();
                        })
                        .create();

                dialog.setOnShowListener(dialogInterface -> {
                    dialog.getButton(DialogInterface.BUTTON_NEGATIVE).setOnClickListener(
                            Helper.getDialogDismissListener(dialog));
                    Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
                    positiveButton.setOnClickListener(view -> {
                        getDataStore().putString(SETTING_BACKUP_FILENAME, Helper.getText(binding.inputText));
                        dialog.dismiss();
                    });
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                    binding.inputText.requestFocus();
                });
                dialog.show();
                return true;
            });
        }

        public DataStore getDataStore() {
            return dataStore;
        }

        public void setSnackbarView(View snackbarView) {
            this.snackbarView = snackbarView;
        }
    }

    /**
     * An in-memory caching store for settings listed in {@link ConfigActivity}.
     * Persists to {@link #SETTINGS_FILE}.
     *
     * @see #persist()
     */
    public static class DataStore extends PreferenceDataStore {
        private static DataStore INSTANCE;
        private final Map<String, Object> settings;

        private DataStore() {
            settings = readSettings();
        }

        public static DataStore getInstance() {
            return INSTANCE == null ? (INSTANCE = new DataStore()) : INSTANCE;
        }

        private Map<String, Object> getSettings() {
            return settings;
        }

        /**
         * Blocking method that writes its data to {@link #SETTINGS_FILE}. Should be called manually,
         * since there's no automatic persist. Meaning, every write, unless they are in batches.
         */
        public void persist() {
            FileUtil.writeFile(SETTINGS_FILE.getAbsolutePath(), getGson().toJson(settings));
        }

        @Override
        public void putString(String key, @Nullable String value) {
            if (value == null) {
                settings.remove(key);
            } else {
                settings.put(key, value);
            }
            persist();
        }

        @Nullable
        @Override
        public String getString(String key, @Nullable String defValue) {
            var value = settings.get(key);
            if (value instanceof String s) {
                return s;
            }
            return defValue;
        }

        @Override
        public void putBoolean(String key, boolean value) {
            settings.put(key, value);
            persist();
        }

        @Override
        public boolean getBoolean(String key, boolean defValue) {
            var value = settings.get(key);
            if (value instanceof Boolean b) {
                return b;
            }
            return defValue;
        }
    }
}
