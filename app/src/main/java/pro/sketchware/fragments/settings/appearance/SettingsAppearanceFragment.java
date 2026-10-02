package pro.sketchware.fragments.settings.appearance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.card.MaterialCardView;

import a.a.a.qA;
import pro.sketchware.databinding.FragmentSettingsAppearanceBinding;
import pro.sketchware.utility.theme.ThemeManager;

public class SettingsAppearanceFragment extends qA {
    private FragmentSettingsAppearanceBinding binding;
    private MaterialCardView selectedThemeCard;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSettingsAppearanceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupToolbar();
        initializeThemeSettings();
        setupClickListeners();
        setupColorThemes();

        {
            View view1 = binding.content;
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

    private void setupToolbar() {
        binding.toolbar.setNavigationOnClickListener(v -> {
            if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                requireActivity().getSupportFragmentManager().popBackStack();
            } else {
                requireActivity().onBackPressed();
            }
        });

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
    }

    private void initializeThemeSettings() {
        boolean isSystemTheme = ThemeManager.isSystemTheme(requireContext());
        binding.switchSystem.setChecked(isSystemTheme);

        updateThemeCardSelection(ThemeManager.getCurrentTheme(requireContext()));

        setThemeCardsEnabled(!isSystemTheme);
    }

    private void setupClickListeners() {
        binding.themeSystem.setOnClickListener(v -> binding.switchSystem.setChecked(!binding.switchSystem.isChecked()));

        binding.switchSystem.setOnCheckedChangeListener((buttonView, isChecked) -> {
            unselectSelectedThemeCard();
            setThemeCardsEnabled(!isChecked);
            if (isChecked) {
                ThemeManager.setTheme(requireContext(), ThemeManager.THEME_SYSTEM);
                setupColorThemes(); // redraw: no color theme should be checked
                return;
            }
            int theme = ThemeManager.getSystemAppliedTheme(requireContext());
            ThemeManager.setTheme(requireContext(), theme);
            updateThemeCardSelection(theme);
            setupColorThemes(); // redraw
        });

        binding.themeLight.setOnClickListener(v -> {
            if (!binding.switchSystem.isChecked()) {
                updateThemeCardSelection(ThemeManager.THEME_LIGHT);
                ThemeManager.setTheme(requireContext(), ThemeManager.THEME_LIGHT);
                setupColorThemes(); // redraw: no color theme should be checked
            }
        });

        binding.themeDark.setOnClickListener(v -> {
            if (!binding.switchSystem.isChecked()) {
                updateThemeCardSelection(ThemeManager.THEME_DARK);
                ThemeManager.setTheme(requireContext(), ThemeManager.THEME_DARK);
                setupColorThemes(); // redraw: no color theme should be checked
            }
        });
    }

    private void updateThemeCardSelection(int theme) {
        unselectSelectedThemeCard();

        MaterialCardView newSelection = switch (theme) {
            case ThemeManager.THEME_LIGHT -> binding.themeLight;
            case ThemeManager.THEME_DARK -> binding.themeDark;
            default -> null;
        };

        if (newSelection != null && !binding.switchSystem.isChecked()) {
            newSelection.setChecked(true);
            selectedThemeCard = newSelection;
        }
    }

    private void unselectSelectedThemeCard() {
        if (selectedThemeCard != null) {
            selectedThemeCard.setChecked(false);
            selectedThemeCard = null;
        }
    }

    private void setThemeCardsEnabled(boolean enabled) {
        // Light/Dark cards stay always clickable — the user may override the System toggle.
        binding.themeLight.setEnabled(true);
        binding.themeDark.setEnabled(true);
        binding.themeLight.setClickable(true);
        binding.themeDark.setClickable(true);
        binding.themeLight.setFocusable(true);
        binding.themeDark.setFocusable(true);
        binding.themeLight.animate().alpha(1.0f).start();
        binding.themeDark.animate().alpha(1.0f).start();
    }

    private void setupColorThemes() {
        android.widget.LinearLayout container = binding.colorThemesContainer;
        if (container == null) return;
        container.removeAllViews();

        int currentTheme = ThemeManager.getCurrentTheme(requireContext());
        boolean colorThemesEnabled = !binding.switchSystem.isChecked();
        int[] ids = {
                ThemeManager.THEME_PURPLE_DARK,
                ThemeManager.THEME_BLACK,
                ThemeManager.THEME_BLUE,
                ThemeManager.THEME_GREEN,
                ThemeManager.THEME_GOLD
        };
        int[] previewColors = {
                0xFFc2b0ff, // Purple Dark primary
                0xFFbbd6ff, // Black primary
                0xFFa8c8ff, // Blue primary
                0xFF9bd4b7, // Green primary
                0xFFebc23c  // Gold primary
        };

        for (int i = 0; i < ids.length; i++) {
            int themeId = ids[i];
            int previewColor = previewColors[i];
            container.addView(createThemeCard(themeId, previewColor, themeId == currentTheme, colorThemesEnabled));
        }
    }

    private android.view.View createThemeCard(int themeId, int previewColor, boolean selected, boolean enabled) {
        android.view.View card = getLayoutInflater().inflate(
                pro.sketchware.R.layout.item_color_theme, binding.colorThemesContainer, false);

        android.view.View preview = card.findViewById(pro.sketchware.R.id.color_preview);
        android.widget.TextView nameView = card.findViewById(pro.sketchware.R.id.theme_name);
        com.google.android.material.radiobutton.MaterialRadioButton radio =
                card.findViewById(pro.sketchware.R.id.theme_radio);

        nameView.setText(ThemeManager.getThemeName(themeId));
        radio.setChecked(selected);
        card.setEnabled(enabled);
        card.setClickable(enabled);
        card.setFocusable(enabled);
        card.setAlpha(enabled ? 1.0f : 0.5f);
        radio.setEnabled(enabled);

        // Set preview circle color
        android.graphics.drawable.GradientDrawable bg = new android.graphics.drawable.GradientDrawable();
        bg.setShape(android.graphics.drawable.GradientDrawable.OVAL);
        bg.setColor(previewColor);
        bg.setStroke((int) (getResources().getDisplayMetrics().density * 1), 0x33FFFFFF);
        preview.setBackground(bg);

        card.setOnClickListener(v -> {
            int current = ThemeManager.getCurrentTheme(requireContext());
            if (current == themeId) return; // already selected
            // sync: color theme selected -> uncheck System toggle and reset Light/Dark cards
            binding.switchSystem.setChecked(false);
            unselectSelectedThemeCard();
            setThemeCardsEnabled(true);
            ThemeManager.setTheme(requireContext(), themeId);
            setupColorThemes(); // redraw all color theme cards with new selection
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Тема сохранена")
                    .setMessage("Чтобы применить тему, приложение нужно перезапустить.\n\nПерезапустить сейчас?")
                    .setPositiveButton("Перезапустить", (d, w) -> {
                        android.content.Context ctx = requireContext().getApplicationContext();
                        android.content.Intent intent = ctx.getPackageManager()
                                .getLaunchIntentForPackage(ctx.getPackageName());
                        if (intent != null) {
                            intent.addFlags(android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP
                                    | android.content.Intent.FLAG_ACTIVITY_NEW_TASK
                                    | android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            ctx.startActivity(intent);
                        }
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    })
                    .setNegativeButton("Позже", null)
                    .show();
        });

        return card;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}