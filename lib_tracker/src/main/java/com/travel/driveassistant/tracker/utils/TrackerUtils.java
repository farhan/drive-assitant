package com.travel.driveassistant.tracker.utils;

import android.content.Context;
import android.location.Location;

import com.travel.driveassistant.lib_utils.MapUtil;
import com.travel.driveassistant.tracker.managers.ActivityRecognitionManager;
import com.travel.driveassistant.tracker.managers.ActivityTransitionManager;

public class TrackerUtils {
    public static void startTakingUserActivityUpdates(final Context applicationContext) {
        ActivityTransitionManager.startTransitionUpdates(applicationContext);
        ActivityRecognitionManager.startActivityUpdates(applicationContext);
    }

    public static void stopTakingUserActivityUpdates(final Context applicationContext) {
        ActivityTransitionManager.stopTransitionUpdates(applicationContext);
        ActivityRecognitionManager.stopActivityUpdates(applicationContext);
    }

    public static boolean isValuableSpeedDiff(float userSpeed, Location lastUserLocation, Location newUserLocation) {
        return userSpeed > 45f && speedDiffExceeds1Kmph(lastUserLocation, newUserLocation);
    }

    private static boolean speedDiffExceeds1Kmph(Location lastUserLocation, Location newUserLocation) {
        if (lastUserLocation == null) {
            return true;
        }
        return Math.abs(MapUtil.getNormalizedSpeed(lastUserLocation) -
                MapUtil.getNormalizedSpeed(newUserLocation)) > 1;
    }
}
