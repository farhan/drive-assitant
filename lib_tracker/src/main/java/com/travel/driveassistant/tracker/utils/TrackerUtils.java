package com.travel.driveassistant.tracker.utils;

import android.location.Location;

import com.travel.driveassistant.lib_utils.MapUtil;

public class TrackerUtils {
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
