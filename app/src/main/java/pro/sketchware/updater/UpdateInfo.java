package pro.sketchware.updater;

/**
 * Модель файла update.json.
 */
public class UpdateInfo {

    public int versionCode;
    public String versionName;
    public String downloadUrl;
    public String changelog;
    public boolean required;
    public String releaseDate;
    public int minVersion;

    // ─── Опциональные поля (могут отсутствовать в update.json) ───
    /** Канал: "stable" или "beta". null = stable. */
    public String channel;
    /** Размер APK в байтах (0 = неизвестно). */
    public long apkSize;
    /** SHA-256 APK (hex, lowercase). null = не проверять. */
    public String sha256;

    public UpdateInfo() {}

    /** @return true, если это обновление (новая версия больше текущей). */
    public boolean isNewerThan(int currentVersionCode) {
        return versionCode > currentVersionCode;
    }

    /** @return true, если текущая версия ниже минимально поддерживаемой. */
    public boolean isBelowMin(int currentVersionCode) {
        return minVersion > 0 && currentVersionCode < minVersion;
    }

    @Override
    public String toString() {
        return "UpdateInfo{" + versionName + " (" + versionCode + ")}";
    }
}
