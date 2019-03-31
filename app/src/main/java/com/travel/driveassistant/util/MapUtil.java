package com.travel.driveassistant.util;

import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class MapUtil {
    // method definition
    public static BitmapDescriptor getMarkerIcon(String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }

    /**
     *
     * @param location
     * @return normalized speed in KPH
     */
    public static int getNormalizedSpeed(@NonNull Location location) {
        return Math.round(location.getSpeed() * AppConstants.MPS_TO_KPH + 3);
    }

    /**
     *
     * @param location1
     * @param location2
     * @return true if there is valuable difference in locations based on speed and distance between
     * them.
     */
    public static boolean isValuableDiff(@NonNull Location location1, @NonNull Location location2) {
        return
            Math.abs(getNormalizedSpeed(location1) - getNormalizedSpeed(location2)) > 10;
    }
}
