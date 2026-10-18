package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Блоки управления приложением: закрыть, свернуть, развернуть.
 * Не трогают ядро — используют стандартные Android API.
 */
public final class AppControlBlocks {

    private AppControlBlocks() {}

    private static final String COLOR_FUNC = "#4a6cd4";
    private static final String PALETTE_FUNC = "9";

    private static HashMap<String, Object> blk(String name, String type, String typeName,
                                               String code, String spec) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("type", type);
        m.put("typeName", typeName);
        m.put("code", code);
        m.put("color", COLOR_FUNC);
        m.put("palette", PALETTE_FUNC);
        m.put("spec", spec);
        return m;
    }

    public static void addBlocks(ArrayList<HashMap<String, Object>> list) {
        // Закрыть приложение (завершить все Activity + убить процесс)
        list.add(blk("appControlClose", " ", "",
                "try { finishAffinity(); System.exit(0); } catch (Throwable _t) {}",
                "close app"));

        // Свернуть приложение (как кнопка Home)
        list.add(blk("appControlMinimize", " ", "",
                "try { moveTaskToBack(true); } catch (Throwable _t) {}",
                "minimize app (Home button)"));

        // Развернуть приложение (вывести из фона на передний план)
        // Работает, только если Activity уже в стеке задач.
        list.add(blk("appControlRestore", " ", "",
                "try { android.content.Intent _i = new android.content.Intent(getApplicationContext(), getClass()); _i.addFlags(android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | android.content.Intent.FLAG_ACTIVITY_NEW_TASK); startActivity(_i); } catch (Throwable _t) {}",
                "restore app from background"));
    }
}
