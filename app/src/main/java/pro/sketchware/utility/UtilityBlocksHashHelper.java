package pro.sketchware.utility;

import java.security.MessageDigest;

/**
 * Хелпер для хеширования строк (MD5, SHA-256).
 * Используется блоками md5/sha256 из {@code UtilityBlocks}.
 * Класс регистрируется в проекте пользователя во время сборки.
 */
public final class UtilityBlocksHashHelper {

    private UtilityBlocksHashHelper() {}

    /** @return MD5-хеш строки в hex-формате (lowercase). */
    public static String md5(String input) {
        return hash(input, "MD5");
    }

    /** @return SHA-256-хеш строки в hex-формате (lowercase). */
    public static String sha256(String input) {
        return hash(input, "SHA-256");
    }

    private static String hash(String input, String algorithm) {
        if (input == null) return "";
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] digest = md.digest(input.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder(digest.length * 2);
            for (byte b : digest) {
                sb.append(String.format("%02x", b & 0xff));
            }
            return sb.toString();
        } catch (Throwable t) {
            return "";
        }
    }
}
