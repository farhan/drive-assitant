package com.travel.driveassistant.tracker.events;

import android.support.annotation.NonNull;

import com.google.android.gms.location.DetectedActivity;

import java.util.ArrayList;

public class UserActivityUpdateEvent {
    final public ArrayList<DetectedActivity> detectedActivities;

    public UserActivityUpdateEvent(@NonNull ArrayList<DetectedActivity> detectedActivities) {
        this.detectedActivities =  detectedActivities;
    }
}
