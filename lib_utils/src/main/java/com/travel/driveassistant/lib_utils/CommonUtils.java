package com.travel.driveassistant.lib_utils;

import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.DetectedActivity;

public class CommonUtils {

    public static boolean isUserStateChanged(int lastActivityType, int lastTransitionType,
                                      int newActivityType, int newTransitionType) {
        return lastActivityType != newActivityType || lastTransitionType != newTransitionType;
    }

    public static String getTransitionTypeName(int transitionType) {
        switch (transitionType) {
            case ActivityTransition.ACTIVITY_TRANSITION_ENTER:
                return "ENTER";
            case ActivityTransition.ACTIVITY_TRANSITION_EXIT:
                return "EXIT";
            default:
                return "INVALID_VALUE";
        }
    }

    public static String getActivityTypeName(int activityType) {
        switch (activityType) {
            case DetectedActivity.IN_VEHICLE:
                return "IN_VEHICLE";
            case DetectedActivity.ON_BICYCLE:
                return "ON_BICYCLE";
            case DetectedActivity.ON_FOOT:
                return "ON_FOOT";
            case DetectedActivity.STILL:
                return "STILL";
            case DetectedActivity.UNKNOWN:
                return "UNKNOWN";
            case DetectedActivity.TILTING:
                return "TILTING";
            case DetectedActivity.WALKING:
                return "WALKING";
            case DetectedActivity.RUNNING:
                return "RUNNING";
            default:
                return "INVALID_VALUE";
        }
    }
}
