package pro.sketchware.smartdrop;

import android.content.Context;

/**
 * Обёртка над DebugLogger для логирования действий пользователя.
 * Все методы безопасны: не бросают исключений, работают асинхронно.
 */
public final class UserActionLogger {

    private static final String TAG_LIFECYCLE = "Lifecycle";
    private static final String TAG_ACTION = "Action";
    private static final String TAG_BLOCK = "Block";
    private static final String TAG_COMPONENT = "Component";
    private static final String TAG_BUILD = "Build";
    private static final String TAG_UI = "UI";

    private UserActionLogger() {}

    // ─── Lifecycle ───────────────────────────────────────────────

    public static void screenOpen(Context ctx, String screenName) {
        DebugLogger.get(ctx).i(TAG_LIFECYCLE, "log_screen_open", screenName);
    }

    public static void screenClose(Context ctx, String screenName) {
        DebugLogger.get(ctx).i(TAG_LIFECYCLE, "log_screen_close", screenName);
    }

    public static void screenResume(Context ctx, String screenName) {
        DebugLogger.get(ctx).d(TAG_LIFECYCLE, "log_screen_resume", screenName);
    }

    public static void screenPause(Context ctx, String screenName) {
        DebugLogger.get(ctx).d(TAG_LIFECYCLE, "log_screen_pause", screenName);
    }

    // ─── Общие действия ─────────────────────────────────────────

    public static void action(Context ctx, String actionName) {
        DebugLogger.get(ctx).i(TAG_ACTION, "log_action", actionName);
    }

    public static void actionWithParam(Context ctx, String actionName, String param) {
        DebugLogger.get(ctx).i(TAG_ACTION, "log_action_param",
                actionName + ": " + param);
    }

    public static void click(Context ctx, String viewName) {
        DebugLogger.get(ctx).d(TAG_UI, "log_click", viewName);
    }

    // ─── Блоки ──────────────────────────────────────────────────

    public static void blockDrop(Context ctx, String opCode) {
        DebugLogger.get(ctx).d(TAG_BLOCK, "log_block_drop", opCode);
    }

    public static void blockDelete(Context ctx, String opCode) {
        DebugLogger.get(ctx).d(TAG_BLOCK, "log_block_delete", opCode);
    }

    public static void blockConnect(Context ctx, String parent, String child) {
        DebugLogger.get(ctx).d(TAG_BLOCK, "log_block_connect", parent + " -> " + child);
    }

    // ─── Компоненты ─────────────────────────────────────────────

    public static void componentCreate(Context ctx, String type, String name) {
        DebugLogger.get(ctx).i(TAG_COMPONENT, "log_component_create", type + " " + name);
    }

    public static void componentDelete(Context ctx, String name) {
        DebugLogger.get(ctx).i(TAG_COMPONENT, "log_component_delete", name);
    }

    // ─── Сборка ─────────────────────────────────────────────────

    public static void buildStart(Context ctx, String projectId) {
        DebugLogger.get(ctx).i(TAG_BUILD, "log_build_start", projectId);
    }

    public static void buildSuccess(Context ctx, String projectId) {
        DebugLogger.get(ctx).i(TAG_BUILD, "log_build_success", projectId);
    }

    public static void buildError(Context ctx, String projectId, Throwable error) {
        DebugLogger.get(ctx).e(TAG_BUILD, "log_build_error_" + projectId, error);
    }
}
