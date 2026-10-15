package pro.sketchware.control.logic;

import com.besome.sketch.beans.BlockBean;

import java.util.ArrayList;
import java.util.Map.Entry;

import a.a.a.Jx;
import a.a.a.jC;
import a.a.a.jq;
import java.util.Set;

public class PermissionManager {

    private final String javaName;
    private final String sc_id;
    public boolean hasPermission = false;

    public PermissionManager(String id, String javaName) {
        this.javaName = javaName;
        sc_id = id;
    }

    private ArrayList<String> addedPermissions() {
        ArrayList<String> permList = new ArrayList<>();
        for (Entry<String, ArrayList<BlockBean>> blocks : jC.a(sc_id).b(javaName).entrySet()) {
            for (BlockBean block : blocks.getValue()) {
                if (block.opCode.equals("addPermission")) {
                    String firstParam = block.parameters.get(0);
                    if (!firstParam.trim().isEmpty()) {
                        if (firstParam.startsWith("Manifest")) {
                            permList.add(firstParam);
                        } else {
                            permList.add("Manifest.permission." + firstParam);
                        }
                    }
                }
            }
        }
        return permList;
    }

    /**
     * Собирает extra-permissions (SMS, Contacts, NFC и т.д.) из блоков проекта.
     * Они не помещаются в int q и не входят в стандартный набор Sketchware.
     */
    private ArrayList<String> addedExtraPermissions() {
        ArrayList<String> permList = new ArrayList<>();
        for (Entry<String, ArrayList<BlockBean>> blocks : jC.a(sc_id).b(javaName).entrySet()) {
            for (BlockBean block : blocks.getValue()) {
                String perm = opCodeToExtraPermission(block.opCode);
                if (perm != null && !perm.isEmpty()) {
                    permList.add("Manifest.permission." + perm);
                }
            }
        }
        return permList;
    }

    /**
     * Маппинг opCode → extra-permission (без префикса).
     * Возвращает null, если блок не требует extra-разрешения.
     */
    private static String opCodeToExtraPermission(String opCode) {
        if (opCode == null) return null;
        switch (opCode) {
            case "smsSend": return "SEND_SMS";
            case "smsGetCount":
            case "smsGetBodyByIndex": return "READ_SMS";
            case "contactsGetCount":
            case "contactsReadAllNames":
            case "contactsReadByName": return "READ_CONTACTS";
            case "contactsAdd": return "WRITE_CONTACTS";
            case "callPhoneDirect": return "CALL_PHONE";
            case "nfcIsAvailable":
            case "nfcIsEnabled": return "NFC";
            case "biometricIsAvailable":
            case "biometricAuthenticate": return "USE_BIOMETRIC";
            case "wallpaperSetFromFile": return "SET_WALLPAPER";
            case "alarmSet":
            case "alarmCancel": return "SET_ALARM";
            case "wakelockAcquire":
            case "wakelockRelease": return "WAKE_LOCK";
            case "accountsGetCount":
            case "accountsGetNames": return "GET_ACCOUNTS";
            case "getOperatorName":
            case "getNetworkCountryIso":
            case "isCallActive": return "READ_PHONE_STATE";
            case "isAppInstalled":
            case "getAppVersionName":
            case "getInstalledAppsCount":
            case "getPackageVersionName": return "QUERY_ALL_PACKAGES";
            case "wifiIsEnabled": return "ACCESS_WIFI_STATE";

            // ─── Camera / File picker (API 23+) ───
            case "takePicture": return "CAMERA";
            case "pickImage":
            case "pickFile": return "READ_EXTERNAL_STORAGE";

            // ─── Notifications (API 33+) ───
            case "notificationShow":
            case "notificationShowWithChannel":
            case "notificationChannelCreate":
            case "notificationCancel":
            case "notificationCancelAll": return "POST_NOTIFICATIONS";

            // ─── Vibrate ───
            case "vibrateShort":
            case "vibratePattern": return "VIBRATE";

            default: return null;
        }
    }

    private String formatPermission(boolean isAppCompat, String permission) {
        if (isAppCompat) {
            return "ContextCompat.checkSelfPermission(this, " + permission + ") == PackageManager.PERMISSION_DENIED";
        } else {
            return "checkSelfPermission(" + permission + ") == PackageManager.PERMISSION_DENIED";
        }
    }

    private void addReqPermission(boolean isAppCompat, ArrayList<String> checkPerm, ArrayList<String> reqPerm) {
        for (String permission : addedPermissions()) {
            checkPerm.add(formatPermission(isAppCompat, permission));
            reqPerm.add(permission);
        }
    }

    private void removePermission(boolean isAppCompat, ArrayList<String> checkPerm, ArrayList<String> reqPerm) {
        for (Entry<String, ArrayList<BlockBean>> blocks : jC.a(sc_id).b(javaName).entrySet()) {
            for (BlockBean block : blocks.getValue()) {
                if (block.opCode.equals("removePermission") && !block.parameters.get(0).trim().isEmpty()) {
                    String permission = block.parameters.get(0).startsWith("Manifest") ? block.parameters.get(0) : ("Manifest.permission." + block.parameters.get(0));
                    checkPerm.remove(formatPermission(isAppCompat, permission));
                    reqPerm.remove(permission);
                }
            }
        }
    }

    public boolean hasNewPermission() {
        return !addedPermissions().isEmpty();
    }

    public String writePermission(boolean isAppCompat, int var1) {
        ArrayList<String> checkPerm = new ArrayList<>();
        ArrayList<String> addPerm = new ArrayList<>();
        StringBuilder permissionCode = new StringBuilder();

        addReqPermission(isAppCompat, checkPerm, addPerm);

        // Extra permissions (SMS, Contacts, NFC и т.д.) — те, что не входят в int q.
        for (String extraPerm : addedExtraPermissions()) {
            String checkStr = isAppCompat
                    ? "ContextCompat.checkSelfPermission(this, " + extraPerm + ") == PackageManager.PERMISSION_DENIED"
                    : "checkSelfPermission(" + extraPerm + ") == PackageManager.PERMISSION_DENIED";
            checkPerm.add(checkStr);
            addPerm.add(extraPerm);
        }

        if (isAppCompat) {
            if ((var1 & jq.PERMISSION_CALL_PHONE) == jq.PERMISSION_CALL_PHONE) {
                checkPerm.add("ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED");
                addPerm.add("Manifest.permission.CALL_PHONE");
            }
            if ((var1 & jq.PERMISSION_CAMERA) == jq.PERMISSION_CAMERA) {
                checkPerm.add("ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED");
                addPerm.add("Manifest.permission.CAMERA");
            }
            if ((var1 & jq.PERMISSION_READ_EXTERNAL_STORAGE) == jq.PERMISSION_READ_EXTERNAL_STORAGE) {
                checkPerm.add("ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED");
                addPerm.add("Manifest.permission.READ_EXTERNAL_STORAGE");
            }
            if ((var1 & jq.PERMISSION_WRITE_EXTERNAL_STORAGE) == jq.PERMISSION_WRITE_EXTERNAL_STORAGE) {
                checkPerm.add("ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED");
                addPerm.add("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            }
            if ((var1 & jq.PERMISSION_RECORD_AUDIO) == jq.PERMISSION_RECORD_AUDIO) {
                checkPerm.add("ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED");
                addPerm.add("Manifest.permission.RECORD_AUDIO");
            }
            if ((var1 & jq.PERMISSION_ACCESS_FINE_LOCATION) == jq.PERMISSION_ACCESS_FINE_LOCATION) {
                checkPerm.add("ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED");
                addPerm.add("Manifest.permission.ACCESS_FINE_LOCATION");
            }
            removePermission(true, checkPerm, addPerm);

            if (!checkPerm.isEmpty() && !addPerm.isEmpty()) {
                permissionCode.append("if (");

                for (int i = 0; i < checkPerm.size(); i++) {
                    if (i != 0) permissionCode.append("\r\n|| ");
                    permissionCode.append(checkPerm.get(i));
                }

                permissionCode.append(") {" + Jx.EOL + "ActivityCompat.requestPermissions(this, new String[] {");

                for (int i = 0; i < addPerm.size(); i++) {
                    if (i != 0) permissionCode.append(", ");
                    permissionCode.append(addPerm.get(i));
                }

                permissionCode.append("}, 1000);" + Jx.EOL +
                        "} else {" + Jx.EOL +
                        "initializeLogic();" + Jx.EOL +
                        "}" + Jx.EOL);
            }

        } else {
            if ((var1 & jq.PERMISSION_CALL_PHONE) == jq.PERMISSION_CALL_PHONE) {
                checkPerm.add("checkSelfPermission(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_DENIED");
                addPerm.add("Manifest.permission.CALL_PHONE");
            }
            if ((var1 & jq.PERMISSION_CAMERA) == jq.PERMISSION_CAMERA) {
                checkPerm.add("checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED");
                addPerm.add("Manifest.permission.CAMERA");
            }
            if ((var1 & jq.PERMISSION_READ_EXTERNAL_STORAGE) == jq.PERMISSION_READ_EXTERNAL_STORAGE) {
                checkPerm.add("checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED");
                addPerm.add("Manifest.permission.READ_EXTERNAL_STORAGE");
            }
            if ((var1 & jq.PERMISSION_WRITE_EXTERNAL_STORAGE) == jq.PERMISSION_WRITE_EXTERNAL_STORAGE) {
                checkPerm.add("checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED");
                addPerm.add("Manifest.permission.WRITE_EXTERNAL_STORAGE");
            }
            if ((var1 & jq.PERMISSION_RECORD_AUDIO) == jq.PERMISSION_RECORD_AUDIO) {
                checkPerm.add("checkSelfPermission(Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_DENIED");
                addPerm.add("Manifest.permission.RECORD_AUDIO");
            }
            if ((var1 & jq.PERMISSION_ACCESS_FINE_LOCATION) == jq.PERMISSION_ACCESS_FINE_LOCATION) {
                checkPerm.add("checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED");
                addPerm.add("Manifest.permission.ACCESS_FINE_LOCATION");
            }
            removePermission(false, checkPerm, addPerm);

            if (!checkPerm.isEmpty() && !addPerm.isEmpty()) {
                permissionCode.append("if (Build.VERSION.SDK_INT >= 23) {" + Jx.EOL + "if (");

                for (int i = 0; i < checkPerm.size(); i++) {
                    if (i != 0) permissionCode.append(Jx.EOL + "||");
                    permissionCode.append(checkPerm.get(i));
                }

                permissionCode.append(") {" + Jx.EOL + "requestPermissions(new String[] {");

                for (int i = 0; i < addPerm.size(); i++) {
                    if (i != 0) permissionCode.append(", ");
                    permissionCode.append(addPerm.get(i));
                }

                permissionCode.append("}, 1000);" + Jx.EOL +
                        "} else {" + Jx.EOL +
                        "initializeLogic();" + Jx.EOL +
                        "}" + Jx.EOL +
                        "} else {" + Jx.EOL +
                        "initializeLogic();" + Jx.EOL +
                        "}" + Jx.EOL);
            }
        }

        hasPermission = !checkPerm.isEmpty() || !addPerm.isEmpty();

        if (permissionCode.toString().trim().isEmpty()) {
            return "initializeLogic();" + Jx.EOL;
        } else {
            return Jx.EOL + permissionCode;
        }
    }
}
