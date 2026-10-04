package pro.sketchware.templates;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Загрузчик шаблонов экранов из assets/screen_templates/templates.json.
 * Singleton, ленивая инициализация.
 */
public class ScreenTemplates {

    private static final String TAG = "ScreenTemplates";
    private static final String ASSET_PATH = "screen_templates/templates.json";

    private static ScreenTemplates instance;

    private final List<Category> categories = new ArrayList<>();
    private final List<ScreenTemplate> templates = new ArrayList<>();
    private final LinkedHashMap<String, Category> categoryById = new LinkedHashMap<>();

    /** Кэш "первый шаблон каждой категории" — только их показываем в UI. */
    private final java.util.Set<String> bundledIds = new java.util.HashSet<>();

    private void computeBundledIds() {
        bundledIds.clear();
        java.util.Set<String> seenCategories = new java.util.HashSet<>();
        for (ScreenTemplate t : templates) {
            if (t.category == null || t.id == null) continue;
            if (seenCategories.add(t.category)) {
                bundledIds.add(t.id);
            }
        }
    }

    private ScreenTemplates(Context context) {
        load(context);
    }

    public static synchronized ScreenTemplates get(Context context) {
        if (instance == null) {
            instance = new ScreenTemplates(context.getApplicationContext());
        }
        return instance;
    }

    private void load(Context context) {
        try (BufferedReader r = new BufferedReader(
                new InputStreamReader(context.getAssets().open(ASSET_PATH)))) {
            Root root = new Gson().fromJson(r, Root.class);
            if (root != null) {
                if (root.categories != null) {
                    categories.addAll(root.categories);
                    for (Category c : root.categories) {
                        categoryById.put(c.id, c);
                    }
                }
                if (root.templates != null) {
                    templates.addAll(root.templates);
                }
            }
            Log.i(TAG, "Loaded " + categories.size() + " categories, " + templates.size() + " templates");
            computeBundledIds();
        } catch (Exception e) {
            Log.e(TAG, "Failed to load " + ASSET_PATH, e);
        }
    }

    public List<Category> getCategories() {
        return new ArrayList<>(categories);
    }

    public Category getCategory(String id) {
        return categoryById.get(id);
    }

    public List<ScreenTemplate> getAll() {
        List<ScreenTemplate> result = new ArrayList<>();
        for (ScreenTemplate t : templates) {
            if (t.id != null && bundledIds.contains(t.id)) result.add(t);
        }
        return result;
    }

    public List<ScreenTemplate> getByCategory(String categoryId) {
        List<ScreenTemplate> result = new ArrayList<>();
        for (ScreenTemplate t : templates) {
            if (t.id == null || !bundledIds.contains(t.id)) continue;
            if (categoryId == null || categoryId.equals(t.category)) {
                result.add(t);
            }
        }
        return result;
    }

    public ScreenTemplate getById(String id) {
        for (ScreenTemplate t : templates) {
            if (t.id != null && t.id.equals(id)) return t;
        }
        return null;
    }

    /** Категория шаблонов. */
    public static class Category {
        public String id;
        @SerializedName("name")
        public String name;
        public String icon;
    }

    /** Корневой объект JSON. */
    private static class Root {
        public int version;
        public ArrayList<Category> categories;
        public ArrayList<ScreenTemplate> templates;
    }
}
