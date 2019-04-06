package com.travel.driveassistant.tracker.events;

import android.location.Location;
import android.support.annotation.NonNull;

public class LocationUpdateEvent {
    public Location location;

    public LocationUpdateEvent(@NonNull Location location) {
        this.location =  location;
    }
}
