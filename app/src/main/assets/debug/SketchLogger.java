package <?package_name?>;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import <?class_name_package?>.<?class_name?>;

/**
 * This class provides a mechanism to read and broadcast logcat messages to a designated receiver.
 * It utilizes a dedicated thread to continuously read logcat output and send it via a broadcast intent.
 *
 * Usage:
 * - Call `SketchLogger.startLogging()` to begin logging.
 * - Call `SketchLogger.stopLogging()` to stop logging.
 * - Use `SketchLogger.broadcastLog(String)` to manually send a debug log message.
 */
public class SketchLogger {
    private static volatile boolean isRunning = false;
    private static volatile Thread loggerThread = null;

    private static void runLogcat() {
        try {
            Process process = Runtime.getRuntime().exec("logcat");
            try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                String logTxt = bufferedReader.readLine();
                while (isRunning && logTxt != null) {
                    try { broadcastLog(logTxt); } catch (Throwable _t) { }
                    logTxt = bufferedReader.readLine();
                }
            }
        } catch (Throwable _t) {
            try { broadcastLog("Logcat error: " + _t.getMessage()); } catch (Throwable _t2) { }
        }
    }

    public static synchronized void startLogging() {
        if (isRunning) {
            try { broadcastLog("Logger already running"); } catch (Throwable _t) { }
            return;
        }
        isRunning = true;
        try {
            loggerThread = new Thread(new Runnable() {
                @Override public void run() { runLogcat(); }
            });
            loggerThread.setDaemon(true);
            loggerThread.start();
        } catch (Throwable _t) {
            isRunning = false;
        }
    }

    public static synchronized void stopLogging() {
        if (isRunning) {
            isRunning = false;
            broadcastLog("Stopping logger by user request.");
        } else {
            broadcastLog("Logger not running");
        }
    }

    public static void broadcastLog(String log) {
        Context context = <?class_name?>.getContext();

        Intent intent = new Intent();
        intent.setAction("pro.sketchware.ACTION_NEW_DEBUG_LOG");
        intent.putExtra("log", log);
        intent.putExtra("packageName", context.getPackageName());
        context.sendBroadcast(intent);
    }
}
