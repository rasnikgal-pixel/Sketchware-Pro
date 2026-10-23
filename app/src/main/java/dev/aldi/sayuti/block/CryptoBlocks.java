package dev.aldi.sayuti.block;

import java.util.ArrayList;
import java.util.HashMap;

public final class CryptoBlocks {

    private CryptoBlocks() {}

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
        list.add(blk("sha1", "s", "", "((java.util.function.Supplier<String>) () -> { try { return new java.math.BigInteger(1, java.security.MessageDigest.getInstance(\"SHA-1\").digest(%1$s.getBytes(\"UTF-8\"))).toString(16); } catch (Throwable _t) { return \"\"; } }).get()", "SHA-1 hash of %s"));

        list.add(blk("sha512", "s", "", "((java.util.function.Supplier<String>) () -> { try { return new java.math.BigInteger(1, java.security.MessageDigest.getInstance(\"SHA-512\").digest(%1$s.getBytes(\"UTF-8\"))).toString(16); } catch (Throwable _t) { return \"\"; } }).get()", "SHA-512 hash of %s"));

        list.add(blk("randomUuid", "s", "",
                "java.util.UUID.randomUUID().toString()",
                "random UUID"));

        list.add(blk("randomInt", "d", "", "(double) (new java.util.Random().nextInt((int)(%2$s) - (int)(%1$s) + 1) + (int)(%1$s))", "random int from %d to %d %s %s"));

        list.add(blk("randomString", "s", "", "((java.util.function.Supplier<String>) () -> { String _c = \"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789\"; StringBuilder _sb = new StringBuilder(); java.util.Random _r = new java.util.Random(); for (int _i = 0; _i < (int)(%1$s); _i++) { _sb.append(_c.charAt(_r.nextInt(_c.length()))); } return _sb.toString(); }).get()", "random string of length %d %s"));
    }
}
