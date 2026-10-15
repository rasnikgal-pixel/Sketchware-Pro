# Multilingual localization of Sketchware Pro (RasNikGal)

[![Latest Release](https://img.shields.io/github/v/release/rasnikgal-pixel/Sketchware-Pro?label=Release&style=flat-square)](https://github.com/rasnikgal-pixel/Sketchware-Pro/releases/latest)
[![4PDA](https://img.shields.io/badge/4PDA-Discussion-blue?style=flat-square)](https://4pda.to/forum/index.php?showtopic=1126677)
[![Android CI](https://github.com/rasnikgal-pixel/Sketchware-Pro/actions/workflows/android.yml/badge.svg)](https://github.com/rasnikgal-pixel/Sketchware-Pro/actions/workflows/android.yml)

🌍 **5 languages + system:** 🇷🇺 Russian · 🇬🇧 English · 🇨🇳 中文 · 🇮🇳 हिन्दी · 🇺🇦 Ukrainian

📖 **Documentation in other languages:** [🇷🇺 RU](README.md) · [🇬🇧 EN](README-en.md) · [🇨🇳 中文](README-zh.md) · [🇮🇳 हिन्दी](README-hi.md) · [🇺🇦 UK](README-uk.md)

📜 **Full version history:** [CHANGELOG.md](CHANGELOG.md)

This repository is a **fork** of the **Sketchware Pro** project, with **full multilingual localization** added: **Russian, English, Chinese, Hindi, and Ukrainian**. The original project was created to revive the visual Android app development environment after the original Sketchware stopped being updated.

**Switching language in the app:** side drawer → **Interface language** → select a language.

**Smart fallback:** if the system language is not supported, the app automatically falls back to English.

**Download APK:** [Releases page](https://github.com/rasnikgal-pixel/Sketchware-Pro/releases/latest)

---

## 📥 Installation

### First installation

1. Download the APK from the [Releases page](https://github.com/rasnikgal-pixel/Sketchware-Pro/releases/latest) — file **`app-release.apk`**.
2. Open the file, allow installation from unknown sources (Android will prompt you).
3. Wait for the installation to complete.

### Updating from a previous version

Installs **over** the previous version — project data and settings are preserved. No need to uninstall the old version.

If Android refuses to install over (for example, due to a different signature), **back up your projects** via Settings → Export settings or by copying `/storage/emulated/0/.sketchware/data/`, then uninstall the old version and install the new one.

### Update check inside the app

The app can check for new versions itself — see the next section.

---

## 📌 About the project

**Sketchware Pro** is a powerful tool for creating Android apps right on your smartphone using a visual block-based builder. It lets you build complete apps without writing code manually, though you can use Java and Kotlin if you want.

**The goal of this fork** is to make the development environment **accessible to users around the world** — in their native language.

### What is translated?

Translation covers **all interface levels**:

- **Main screens:** Designer, Logic editor, Code editor.
- **Managers:** Blocks, components, events, libraries, and resources.
- **All dialogs:** creation, editing, settings.
- **System messages:** compilation errors, notifications (Toast, Snackbar), warnings.
- **Block categories:** Variables, Lists, Control, Operators, Math, Files, View, Components.

### Supported languages

| Language | Code | Folder | Status |
|----------|------|--------|--------|
| 🇷🇺 Russian | ru | values/ | Complete |
| 🇬🇧 English | en | values-en/ | Complete |
| 🇨🇳 Chinese | zh | values-zh/ | Complete |
| 🇮🇳 Hindi | hi | values-hi/ | Complete |
| 🇺🇦 Ukrainian | uk | values-uk/ | Complete |

### Translation coverage

| Component | Count |
|-----------|-------|
| Strings in strings.xml | ~2500 |
| Hardcoded strings in Java | ~1300 (moved to strings.xml) |
| UI messages (toast, snackbar) | ~250 |
| Layout files | 117 |
| Menu files | 19 |
| **Total replacements** | **~3000+** |

---

## 🆕 What's new in 7.5.0

**Major update with three new developer tools.**

### 📝 SORA Editor (text editor)

- **Tabs for project files** — Manifest, layouts, Java.
- **Java/Kotlin syntax highlighting** (TextMate).
- **Menu**: Save, Undo, Redo, Open, Close, Find/Replace, Format, Preview, Wrap, Theme, Copy path, Settings.
- **Settings**: font size (8-32), word wrap.
- **Short tab names** + double-tap to copy full path.
- **Theme follows** the app theme.

### 📊 Project Graph

- **5 layouts**: tree, horizontal, radial, grid, force-directed.
- **Nodes**: Project, Activity, Event, Block, Variable, Component, Function.
- **Export PNG** (full tree / visible area).
- **Drag nodes** with long press.
- **Search nodes** with highlighting.
- **Filter by Activity** — "show only this".
- **Tap on Event → block editor**.
- Settings: layout, gap, show edges/blocks/variables/components/functions.

### ⚡ Double-tap on widget in designer

- Opens **list of all events** for the widget (like Delphi).
- By selection — **block editor** for this event.

### 💻 My Code — custom Java classes

- Folder `user/` in the project — **not overwritten** during build.
- **4 templates**: empty class, utility, custom View, interface.
- **Create / delete / rename**.
- **Compiled** with the project.
- From blocks — via `addSourceDirectly`.

### 🛠 All features in "Tools" section

- Project Graph, SORA Text Editor, Debug Log, **My Code** — all with project picker.
- **"My Code"** item also in SORA menu.

### 🛡 Update protection

- **HEAD request** to downloadUrl before showing update.
- If APK missing — auto-check silently ignores, manual — shows "Update is being prepared".

### 🚀 CI

- **Release tag** — automatically from `build.gradle`.
- **Release name** — in English.
- **update.json** — updated automatically after build.

---

## 🆕 What's new in 7.4.0

**🌍 MULTILINGUAL — 5 LANGUAGES + SYSTEM!**

The app now supports:

- 🇷🇺 **Russian**
- 🇬🇧 **English**
- 🇨🇳 **中文** (Chinese)
- 🇮🇳 **हिन्दी** (Hindi)
- 🇺🇦 **Ukrainian**
- 🌐 **System** (follows device language)

**Switching:** side drawer → **Interface language**.

**Smart fallback:** if the system language is not supported (e.g., German or Arabic), the app automatically falls back to **English**.

**📦 Full interface localization:**
- ~1300 strings moved from Java to strings.xml
- 117 strings from layout files
- 19 strings from menus
- 443 missing strings added
- 100% coverage of all our strings in RU/EN/ZH/HI/UK

**🔧 Refactoring and fixes:**
- Code editor menus via `MENU_ID` (CodeEditorLayout, SrcCodeEditor)
- All `(CharSequence)` ambiguities resolved
- Fixed NPE in `ViewPane` (#2077, #2034)
- Escaped apostrophes and `%` in all strings
- Help language follows app language (RU/UK → Russian, EN/ZH/HI → English)

**🛠 For developers:**
- `add_string.py` script — add strings to all 5 languages with one command

---

## 🆕 What's new in 7.1.0

**📦 Block collections** — new **"Collections"** category at the top of the block palette. 10 ready-made templates (dialogs, progress, finish, Toast) that can be dropped as a **single block** and expand into a chain with nested blocks.

**🤖 SmartDrop** — when you drag a block that requires a component, the app offers to create or connect the needed component. No more manual adding of dialogs, timers, vibrators, etc.

**🖼 Screen templates** — 200 ready-made layouts in 20 categories. Gallery with search, preview and history. Quick start of a new screen in one tap.

**🎨 5 colored themes** — Purple Dark (per reference), AMOLED, Blue, Green, Gold. Full Material3 schemes.

**🔍 Editor improvements:**
- Block search across all palettes
- Favorite blocks with ☆ icon
- Smart search: `dialog show` finds `dialogShow`
- Palette scroll position saved (per-project)
- Block Logic Checker — 8 logic check rules

**💾 Settings export/import** — save all app settings to JSON via FilePicker.

**📜 Debug log (RU/EN)** — built-in log viewer with share, copy, clear.

**🔄 Update system** — the app checks for new versions and offers to download.

More details — [FIXES.md](FIXES.md).

---

## 🔄 Update check

The app downloads the `update.json` file from the repository and compares `versionCode` with the installed version.

### How it works

- **On launch**, the app checks `update.json`.
- If a newer version is found, a dialog opens with **"Later"** and **"Download"** buttons.
- **"Later"** remembers the version — it won't prompt again (except when `required: true`).
- **"Download"** opens the release page in a browser.

### Sources

1. **Primary:** `raw.githubusercontent.com/rasnikgal-pixel/Sketchware-Pro/main/update.json`
2. **Fallback:** `cdn.jsdelivr.net/gh/rasnikgal-pixel/Sketchware-Pro@main/update.json`

If the primary source is unavailable, the app automatically switches to the fallback.

### Settings

In **Settings → Update check** you can:

- Enable/disable automatic checking (enabled by default).
- Choose the frequency: _On launch_ (default), _Once a day_, _Every 3/6/9 days_.
- Tap **"Check now"** — instant check.

### `update.json` format

Main fields:

- `versionCode` — integer, compared with the installed version.
- `versionName` — displayed in the dialog.
- `downloadUrl` — direct link to the APK in GitHub Release.
- `changelog` — text with `\n` line breaks.
- `required` — if `true`, the user cannot postpone.
- `releaseDate` — release date (UTC).
- `minVersion` — if the installed version is lower, the update becomes mandatory.
- `channel` — `stable` or `beta` (future use).
- `apkSize` — APK size in bytes.
- `sha256` — APK SHA-256 hash (lowercase hex).

Fields `versionCode`, `versionName`, `releaseDate`, `apkSize`, `sha256`, `downloadUrl` are **updated automatically** in CI after release. `changelog` is written manually.

---

## 🔢 Versioning scheme

Format: `versionCode` — integer, monotonically increasing; `versionName` — semantic `MAJOR.MINOR.PATCH`.

| Change | versionCode | versionName | Example |
|--------|-------------|-------------|---------|
| **Patch** (small fix) | +1 | `X.Y.Z+1` | 150 → 151, `7.1.0` → `7.1.1` |
| **Minor** (new feature) | +10 | `X.Y+1.0` | 150 → 160, `7.1.0` → `7.2.0` |
| **Major** (large) | +100 | `X+1.0.0` | 150 → 250, `7.1.0` → `8.0.0` |

### Releasing a new version

1. Update `versionCode` and `versionName` in `app/build.gradle`.
2. Update `versionCode` / `versionName` / `changelog` in `update.json` manually.
3. Update `tag_name` in `.github/workflows/android.yml`.
4. Update `RELEASE_TAG` in the `updateUpdateJson` job in the same workflow.
5. Commit and push to `main` — CI builds the APK, updates the Release, and recalculates `sha256` / `apkSize` / `releaseDate` in `update.json`.

---

## 📂 Project structure

Key files of this fork (added to the original):

- `update.json` — update metadata (read by UpdateChecker)
- `FIXES.md` — detailed changelog of fork features
- `README.md` — Russian version (main)
- `README-en.md` — English version (this file)
- `README-zh.md` — Chinese version
- `README-hi.md` — Hindi version
- `README-uk.md` — Ukrainian version
- `.github/workflows/android.yml` — CI: build, Telegram, Release, update.json
- `.github/workflows/update_update_json.py` — auto-update script for update.json
- `add_string.py` — helper script for adding strings to all 5 languages

### i18n helpers (`app/src/main/java/pro/sketchware/i18n/`)

- `LocaleHelper.java` — locale storage and application
  - `getEffectiveLanguage(Context)` — final language (with fallback to English)
  - `getHelpLanguage(Context)` — help language (RU/UK → "ru", others → "en")
  - `isSupported(String)` — check if language is supported
  - `apply(Context)` / `wrap(Context)` — apply locale

### Updater package (`app/src/main/java/pro/sketchware/updater/`)

- `UpdateInfo.java` — update.json model
- `UpdateChecker.java` — download, version comparison, fallback to jsDelivr
- `UpdateDialog.java` — "Update available" dialog

### Other

- `app/src/main/java/pro/sketchware/smartdrop/DebugLogger.java` — debug log (RU/EN)
- `app/src/main/res/values/strings_log.xml` — log strings (RU)
- `app/src/main/res/values-en/strings_log.xml` — log strings (EN)

---

## 🛠 Building from source

If you want to build the app yourself, you'll need **Gradle** and **Android SDK**.

### Requirements

- **Java Development Kit (JDK) 17** or higher.
- **Android SDK** with platform `android-36` (or the one required in `build.gradle`).
- **Gradle** (recommended: use the wrapper `./gradlew`).

### Build steps

1. **Clone the repository:**

        git clone https://github.com/rasnikgal-pixel/Sketchware-Pro.git
        cd Sketchware-Pro

2. **Set up `local.properties`** (if building outside Android Studio): Create a `local.properties` file in the project root and specify the path to Android SDK:

        sdk.dir=/path/to/your/Android/SDK

3. **Build APK:**

        ./gradlew assembleMinApi26Debug

The resulting APK will be at `app/build/outputs/apk/minApi26/debug/`.

> **Note:** Building requires `google-services.json` and `SKETCHUB_API_KEY`. They are not provided in the official repository. For a test build, you can create a dummy `google-services.json` (as described in the community instructions).

---

## 🤝 Contributing

### Report a bug / suggest an idea

1. Create an **Issue** in this repository. If it's a bug — attach a log from **Settings → Debug log**.
2. Or write to the **4PDA** thread: [Discussion of localization](https://4pda.to/forum/index.php?showtopic=1126677).

### Pull Request

PRs are welcome. To make changes easy to review:

1. **One feature / fix — one commit** with a meaningful message.
2. **Don't break existing functionality.** Before submitting a PR, make sure CI is green.
3. **Logging** — only via `DebugLogger`, not `android.util.Log`. Strings — in `strings_log.xml` (RU + EN).
4. **UI strings** — via `strings.xml` (all 5 languages) or `add_string.py`, no hardcoding in Java or layouts.
5. **Version bump** — if the PR adds features, follow the "Versioning scheme" section.

### Rules for working with the fork

- Default branch: **`main`**. Work directly or via feature branches.
- **One version bump = one separate commit** (versionCode + versionName + update.json + release tag).
- **Don't touch** bot auto-commits (`chore(updater): auto-update update.json`).
- If the change affects the core of Sketchware Pro (not localization) — better propose it in the main repository.

---

## 📄 License and disclaimer

This is an **unofficial modification** of Sketchware Pro. The project is not affiliated with the original developers.

- **License:** The project is distributed under the same license as the original Sketchware Pro.
- **Liability:** The app is provided "as is". The author is not responsible for any damage caused by using this software.
- **Publication:** Publishing this modified app on Google Play or other stores is **prohibited** under the terms of the original project.

---

## 🙏 Credits

- **The Sketchware Pro team** for creating and maintaining a powerful open-source development environment.
- **The 4PDA community** for feedback and testing of the localization.

**Localization author:** RasNikGal
