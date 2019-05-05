package com.travel.driveassistant.tracker.events;

import android.location.Location;
import android.support.annotation.NonNull;

import com.travel.driveassistant.lib_speed_data.OverSpeedResult;

public class OverSpeedEvent {
    @NonNull
    final public Location userLocation;
    @NonNull
    final public OverSpeedResult overSpeedResult;

    public OverSpeedEvent(@NonNull Location userLocation, @NonNull OverSpeedResult overSpeedResult) {
        this.userLocation = userLocation;
        this.overSpeedResult =  overSpeedResult;
    }
}
