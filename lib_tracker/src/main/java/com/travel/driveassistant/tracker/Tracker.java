package com.travel.driveassistant.tracker;

import android.content.Context;

import com.travel.driveassistant.tracker.managers.TrackerPrefManager;
import com.travel.driveassistant.tracker.services.BackgroundService;
import com.travel.driveassistant.tracker.utils.TrackerUtils;

public class Tracker {
    public static void startTracker(Context applicationContext) {
        new TrackerPrefManager(applicationContext).setTrackerEnabled(true);
        // Start tracker
        BackgroundService.start(applicationContext, null);
        TrackerUtils.startTakingUserActivityUpdates(applicationContext);
    }

    public static void stopTracker(Context applicationContext) {
        new TrackerPrefManager(applicationContext).setTrackerEnabled(false);
        // Stop tracker
        TrackerUtils.stopTakingUserActivityUpdates(applicationContext);
    }
}
