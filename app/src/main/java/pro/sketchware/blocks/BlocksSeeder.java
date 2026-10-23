package pro.sketchware.blocks;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import mod.hilal.saif.activities.tools.ConfigActivity;
import pro.sketchware.utility.FileUtil;

/**
 * Засеивает дефолтные кастомные блоки (Layout, Sound, Utils) при старте приложения.
 * Merge-логика: не перезаписывает существующие блоки пользователя, добавляет только новые по name.
 */
public final class BlocksSeeder {

    private static final String PALETTE_PATH =
            Environment.getExternalStorageDirectory() + "/.sketchware/resources/block/My Block/palette.json";
    private static final String BLOCKS_PATH =
            Environment.getExternalStorageDirectory() + "/.sketchware/resources/block/My Block/block.json";

    private BlocksSeeder() {}

    public static synchronized void seedOnStartup(Context ctx) {
        if (ctx == null) return;
        try {
            File paletteFile = new File(PALETTE_PATH);
            File blocksFile = new File(BLOCKS_PATH);
            File parent = paletteFile.getParentFile();
            if (parent != null && !parent.exists()) parent.mkdirs();

            boolean useLocalized = ConfigActivity.isSettingEnabled(
                    ConfigActivity.SETTING_TRANSLATE_CUSTOM_BLOCKS);
            String lang = useLocalized ? Locale.getDefault().getLanguage() : "en";
            String dir = "default_blocks/" + lang;
            // fallback
            try {
                InputStream is = ctx.getAssets().open(dir + "/blocks.json");
                is.close();
            } catch (Throwable t) {
                dir = "default_blocks/en";
            }

            String assetsPalette = readAsset(ctx, dir + "/palettes.json");
            String assetsBlocks = readAsset(ctx, dir + "/blocks.json");
            if (assetsPalette == null || assetsBlocks == null) return;

            Gson gson = new Gson();
            List<Map<String, Object>> newPalettes = gson.fromJson(assetsPalette,
                    new TypeToken<List<Map<String, Object>>>() {}.getType());
            List<Map<String, Object>> newBlocks = gson.fromJson(assetsBlocks,
                    new TypeToken<List<Map<String, Object>>>() {}.getType());
            if (newPalettes == null || newBlocks == null) return;

            // Читаем существующие (если есть)
            List<Map<String, Object>> curPalettes = new ArrayList<>();
            List<Map<String, Object>> curBlocks = new ArrayList<>();
            if (paletteFile.exists()) {
                String s = FileUtil.readFile(paletteFile.getAbsolutePath());
                if (s != null && !s.trim().isEmpty() && !s.trim().equals("[]")) {
                    try { curPalettes = gson.fromJson(s, new TypeToken<List<Map<String, Object>>>() {}.getType()); }
                    catch (Throwable ignored) {}
                }
            }
            if (blocksFile.exists()) {
                String s = FileUtil.readFile(blocksFile.getAbsolutePath());
                if (s != null && !s.trim().isEmpty() && !s.trim().equals("[]")) {
                    try { curBlocks = gson.fromJson(s, new TypeToken<List<Map<String, Object>>>() {}.getType()); }
                    catch (Throwable ignored) {}
                }
            }

            // ==== MERGE PALETTES ====
            // Индексы в Sketchware начинаются с 9. Индекс в palette.json = позиция в массиве.
            // Собираем существующие имена палитр
            Set<String> existingPaletteNames = new HashSet<>();
            for (Map<String, Object> p : curPalettes) {
                Object n = p.get("name");
                if (n instanceof String) existingPaletteNames.add((String) n);
            }

            // Сколько палитр было до merge — это стартовый индекс для новых
            int baseIndex = 100 + curPalettes.size();
            Map<String, Integer> newPaletteIndexByName = new HashMap<>();
            for (Map<String, Object> p : newPalettes) {
                String name = (String) p.get("name");
                if (name == null) continue;
                if (!existingPaletteNames.contains(name)) {
                    curPalettes.add(p);
                    int idx = baseIndex + newPaletteIndexByName.size();
                    newPaletteIndexByName.put(name, idx);
                    existingPaletteNames.add(name);
                } else {
                    // уже существует — найдём его индекс
                    for (int i = 0; i < curPalettes.size(); i++) {
                        if (name.equals(curPalettes.get(i).get("name"))) {
                            newPaletteIndexByName.put(name, 100 + i);
                            break;
                        }
                    }
                }
            }

            // ==== MERGE BLOCKS ====
            Set<String> existingBlockNames = new HashSet<>();
            for (Map<String, Object> b : curBlocks) {
                Object n = b.get("name");
                if (n instanceof String) existingBlockNames.add((String) n);
            }

            // В assets blocks.json "palette" — это 0/1/2 (позиция в assets palette.json).
            // Преобразуем в реальный индекс (9 + позиция в assets палитр) → это newPaletteIndexByName по имени.
            // Для этого нужен маппинг: index_assets → index_real
            Map<String, Integer> assetsIndexToReal = new HashMap<>();
            for (int i = 0; i < newPalettes.size(); i++) {
                String pname = (String) newPalettes.get(i).get("name");
                if (pname != null && newPaletteIndexByName.containsKey(pname)) {
                    assetsIndexToReal.put(String.valueOf(100 + i), newPaletteIndexByName.get(pname));
                }
            }

            for (Map<String, Object> b : newBlocks) {
                String name = (String) b.get("name");
                if (name == null || existingBlockNames.contains(name)) continue;
                Object pal = b.get("palette");
                if (pal instanceof String && assetsIndexToReal.containsKey(pal)) {
                    b.put("palette", String.valueOf(assetsIndexToReal.get(pal)));
                }
                curBlocks.add(b);
                existingBlockNames.add(name);
            }

            // Запись
            FileUtil.writeFile(paletteFile.getAbsolutePath(), gson.toJson(curPalettes));
            FileUtil.writeFile(blocksFile.getAbsolutePath(), gson.toJson(curBlocks));

            // Сброс кэша BlockLoader через рефлексию (если есть)
            try {
                Class<?> c = Class.forName("mod.hey.studios.editor.manage.block.v2.BlockLoader");
                java.lang.reflect.Field f = c.getDeclaredField("blocks");
                f.setAccessible(true);
                f.set(null, null);
            } catch (Throwable ignored) {}
        } catch (Throwable ignored) {}
    }

    private static String readAsset(Context ctx, String path) {
        try (InputStream is = ctx.getAssets().open(path)) {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] buf = new byte[8192];
            int n;
            while ((n = is.read(buf)) > 0) bos.write(buf, 0, n);
            return bos.toString("UTF-8");
        } catch (Throwable t) {
            return null;
        }
    }
}
