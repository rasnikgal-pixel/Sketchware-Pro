package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

public final class NotificationExtBlocks {

    private NotificationExtBlocks() {}

    private static final String COLOR_FUNC = "#4a6cd4";
    private static final String PALETTE_FUNC = "7";
    private static final String COLOR_OP = "#5cb722";
    private static final String PALETTE_OP = "3";

    private static HashMap<String, Object> blk(String name, String type, String typeName,
                                                 String code, String color, String palette,
                                                 String spec) {
        HashMap<String, Object> m = new HashMap<>();
        m.put("name", name);
        m.put("type", type);
        m.put("typeName", typeName);
        m.put("code", code);
        m.put("color", color);
        m.put("palette", palette);
        m.put("spec", spec);
        return m;
    }

    public static void addBlocks(ArrayList<HashMap<String, Object>> list) {
        list.add(blk("notificationChannelCreate", " ", "",
                "try { if (android.os.Build.VERSION.SDK_INT >= 26) { android.app.NotificationManager _nm = (android.app.NotificationManager) getApplicationContext().getSystemService(android.content.Context.NOTIFICATION_SERVICE); if (_nm != null) _nm.createNotificationChannel(new android.app.NotificationChannel(%1$s, %2$s, android.app.NotificationManager.IMPORTANCE_DEFAULT)); } } catch (Throwable _t) {}",
                COLOR_OP, PALETTE_OP, "create notification channel id %s name %s"));

        list.add(blk("areNotificationsEnabled", "b", "",
                "((java.util.function.Supplier<Boolean>) () -> { try { if (android.os.Build.VERSION.SDK_INT < 24) return true; android.app.NotificationManager _nm = (android.app.NotificationManager) getApplicationContext().getSystemService(android.content.Context.NOTIFICATION_SERVICE); return _nm != null && _nm.areNotificationsEnabled(); } catch (Throwable _t) { return false; } }).get()",
                COLOR_FUNC, PALETTE_FUNC, "notifications are enabled"));

        list.add(blk("notificationShowWithChannel", " ", "",
                "try { android.app.NotificationManager _nm = (android.app.NotificationManager) getApplicationContext().getSystemService(android.content.Context.NOTIFICATION_SERVICE); if (_nm != null) _nm.notify((int)(System.currentTimeMillis() % 2000000000), new androidx.core.app.NotificationCompat.Builder(getApplicationContext(), %1$s).setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle(%2$s).setContentText(%3$s).setAutoCancel(true).build()); } catch (Throwable _t) {}",
                COLOR_OP, PALETTE_OP, "show notification in channel %s title %s text %s"));

        list.add(blk("openNotificationSettings", " ", "",
                "try { startActivity(new android.content.Intent(android.provider.Settings.ACTION_APP_NOTIFICATION_SETTINGS).putExtra(android.provider.Settings.EXTRA_APP_PACKAGE, getPackageName())); } catch (Throwable _t) {}",
                COLOR_OP, PALETTE_OP, "open notification settings"));
    }
}
