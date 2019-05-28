package com.travel.driveassistant.tracker.managers;

import android.content.Context;

import com.travel.driveassistant.lib_utils.managers.PrefManager;

public class TrackerPrefManager extends PrefManager {
    public static final String PREF_NAME = "pref_tracker";

    /**
     * Contains preference key constants.
     */
    public static final class Key {
        public static final String TRACKER_ENABLED = "TRACKER_ENABLED";
    }

    public TrackerPrefManager(Context applicationContext) {
        super(applicationContext, PREF_NAME);
    }

    public boolean isTrackerEnabled() {
        return getBoolean(Key.TRACKER_ENABLED, false);
    }

    public void setTrackerEnabled(boolean enabled) {
        super.put(Key.TRACKER_ENABLED, enabled);
    }

    public static void nukeSharedPreferences(Context applicationContext) {
        applicationContext.getSharedPreferences(
                    PREF_NAME, Context.MODE_PRIVATE).edit().clear().apply();
    }
}
