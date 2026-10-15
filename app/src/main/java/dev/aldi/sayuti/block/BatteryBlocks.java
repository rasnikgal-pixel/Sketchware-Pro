package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

public final class BatteryBlocks {

    private BatteryBlocks() {}

    private static final String COLOR_FUNC = "#4a6cd4";
    private static final String PALETTE_FUNC = "7";

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
        list.add(blk("batteryTemperature", "d", "",
                "((double) getApplicationContext().registerReceiver(null, new android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED)).getIntExtra(android.os.BatteryManager.EXTRA_TEMPERATURE, 0)) / 10.0",
                "battery temperature in degrees Celsius"));

        list.add(blk("batteryVoltage", "d", "",
                "(double) getApplicationContext().registerReceiver(null, new android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED)).getIntExtra(android.os.BatteryManager.EXTRA_VOLTAGE, 0)",
                "battery voltage in mV"));

        list.add(blk("batteryHealth", "s", "",
                "String.valueOf(getApplicationContext().registerReceiver(null, new android.content.IntentFilter(android.content.Intent.ACTION_BATTERY_CHANGED)).getIntExtra(android.os.BatteryManager.EXTRA_HEALTH, -1))",
                "battery health status code"));
    }
}
