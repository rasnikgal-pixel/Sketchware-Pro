package pro.sketchware.smartdrop;

import android.content.Context;
import android.util.Log;

import com.besome.sketch.beans.ComponentBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import a.a.a.jC;

/**
 * SmartDrop — автоматическое создание/подключение компонентов при drop блока.
 *
 * Читает таблицу opCode -> componentTypeName из assets/smartdrop/component_map.json.
 * Один статический экземпляр, ленивая инициализация.
 */
public class SmartDropHelper {

    private static final String TAG = "SmartDropHelper";
    private static final String ASSET_PATH = "smartdrop/component_map.json";

    private static SmartDropHelper instance;

    /** opCode -> человекочитаемое имя типа ("Dialog", "Vibrator", ...) */
    private final Map<String, String> opCodeToTypeName = new HashMap<>();

    /** Кэш: typeName -> int-константа ComponentBean.COMPONENT_TYPE_* */
    private final Map<String, Integer> typeNameToInt = new HashMap<>();

    private SmartDropHelper(Context context) {
        loadMap(context);
        buildTypeCache();
    }

    public static synchronized SmartDropHelper get(Context context) {
        if (instance == null) {
            instance = new SmartDropHelper(context.getApplicationContext());
        }
        return instance;
    }

    private void loadMap(Context context) {
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(context.getAssets().open(ASSET_PATH)))) {
            Type type = new TypeToken<Map<String, String>>() {}.getType();
            Map<String, String> loaded = new Gson().fromJson(reader, type);
            if (loaded != null) {
                opCodeToTypeName.putAll(loaded);
            }
            Log.i(TAG, "Loaded " + opCodeToTypeName.size() + " opCode mappings");
        } catch (Exception e) {
            Log.e(TAG, "Failed to load component_map.json", e);
        }
    }

    /**
     * Строим обратный кэш typeName ("Dialog") -> int (COMPONENT_TYPE_DIALOG).
     * Используем ComponentBean.getComponentTypeByTypeName().
     */
    private void buildTypeCache() {
        Set<String> names = new HashSet<>(opCodeToTypeName.values());
        for (String name : names) {
            try {
                int t = ComponentBean.getComponentTypeByTypeName(name);
                if (t > 0) {
                    typeNameToInt.put(name, t);
                } else {
                    Log.w(TAG, "Unknown component type name: " + name);
                }
            } catch (Exception e) {
                Log.w(TAG, "Failed to resolve type for " + name, e);
            }
        }
    }

    /**
     * @return int-тип компонента (COMPONENT_TYPE_*), либо null если блок не требует компонента.
     */
    public Integer getRequiredComponent(String opCode) {
        if (opCode == null) return null;
        String typeName = opCodeToTypeName.get(opCode);
        if (typeName == null) return null;
        return typeNameToInt.get(typeName);
    }

    /**
     * @return человекочитаемое имя типа ("Dialog") или null.
     */
    public String getRequiredComponentName(String opCode) {
        return opCodeToTypeName.get(opCode);
    }

    /**
     * Найти все компоненты указанного типа у текущего экрана.
     */
    public ArrayList<ComponentBean> findComponents(String scId, String javaName, int componentType) {
        try {
            ArrayList<ComponentBean> result = jC.a(scId).c(javaName, componentType);
            return result != null ? result : new ArrayList<>();
        } catch (Exception e) {
            Log.e(TAG, "findComponents failed", e);
            return new ArrayList<>();
        }
    }

    /**
     * Сгенерировать уникальное имя для нового компонента по префиксу.
     * Пример: если уже есть Dialog1, Dialog2 — вернёт Dialog3.
     */
    public String generateComponentName(List<ComponentBean> existing, String prefix) {
        Set<String> used = new HashSet<>();
        if (existing != null) {
            for (ComponentBean c : existing) {
                if (c != null && c.componentId != null) {
                    used.add(c.componentId);
                }
            }
        }
        int n = 1;
        while (used.contains(prefix + n)) {
            n++;
        }
        return prefix + n;
    }

    /**
     * Создать компонент. Возвращает true при успехе.
     */
    public boolean createComponent(String scId, String javaName, int componentType, String name) {
        try {
            jC.a(scId).a(javaName, componentType, name);
            jC.a(scId).k();
            return true;
        } catch (Exception e) {
            Log.e(TAG, "createComponent failed", e);
            return false;
        }
    }
}
