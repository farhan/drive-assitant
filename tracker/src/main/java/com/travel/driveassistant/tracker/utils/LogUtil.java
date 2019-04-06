package com.travel.driveassistant.tracker.utils;

import android.util.Log;

public class LogUtil {
    public static final String TAG = "Tracker";

    public static void log(String log) {
        Log.i(TAG, log);
    }

    public static void log(String tag, String log) {
        Log.i(tag, log);
    }
}
