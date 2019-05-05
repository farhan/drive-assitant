package com.travel.driveassistant.lib_speed_data;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;

public class OverSpeedResult {

    public LatLng userLatLong;
    // User speed in kmph
    public float userSpeedNormalized;

    public boolean isOverSpeed = false;
    public int speedLimitAtLocation = -1;

    @Nullable
    public String addressOfUser = null;

    public OverSpeedResult(@NonNull LatLng userLatLong, final float userSpeedNormalized) {
        this.userLatLong = userLatLong;
        this.userSpeedNormalized = userSpeedNormalized;
    }
}
