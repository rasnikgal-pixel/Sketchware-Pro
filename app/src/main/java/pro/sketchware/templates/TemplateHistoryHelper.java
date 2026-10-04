package pro.sketchware.templates;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * История последних использованных шаблонов.
 */
public class TemplateHistoryHelper {

    private static final String PREFS = "template_history_prefs";
    private static final String KEY_HISTORY = "history";
    private static final String SEPARATOR = ",";
    private static final int MAX_SIZE = 10;

    public static List<String> getHistory(Context ctx) {
        String raw = prefs(ctx).getString(KEY_HISTORY, "");
        if (raw == null || raw.isEmpty()) return new ArrayList<>();
        return new ArrayList<>(Arrays.asList(raw.split(SEPARATOR)));
    }

    public static void addToHistory(Context ctx, String templateId) {
        if (templateId == null || templateId.isEmpty()) return;
        List<String> history = getHistory(ctx);
        history.remove(templateId);  // убираем если уже есть
        history.add(0, templateId);  // добавляем в начало
        if (history.size() > MAX_SIZE) {
            history = history.subList(0, MAX_SIZE);
        }
        save(ctx, history);
    }

    public static void clear(Context ctx) {
        prefs(ctx).edit().remove(KEY_HISTORY).apply();
    }

    private static void save(Context ctx, List<String> list) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            if (i > 0) sb.append(SEPARATOR);
            sb.append(list.get(i));
        }
        prefs(ctx).edit().putString(KEY_HISTORY, sb.toString()).apply();
    }

    private static SharedPreferences prefs(Context ctx) {
        return ctx.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE);
    }
}
