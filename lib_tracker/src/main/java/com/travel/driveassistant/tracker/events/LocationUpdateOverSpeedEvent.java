package com.travel.driveassistant.tracker.events;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.travel.driveassistant.lib_speed_data.OverSpeedResult;

public class LocationUpdateOverSpeedEvent {
    @NonNull
    final public Location userLocation;
    @Nullable
    final public OverSpeedResult overSpeedResult;

    public LocationUpdateOverSpeedEvent(@NonNull Location userLocation, @NonNull OverSpeedResult overSpeedResult) {
        this.userLocation = userLocation;
        this.overSpeedResult =  overSpeedResult;
    }
}
