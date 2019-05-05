package com.travel.driveassistant.lib_utils;

import android.graphics.Color;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class MapUtil {

//    public static final String OVER_SPEED_COLOR = "#FF4500";

    // method definition
    public static BitmapDescriptor getMarkerIcon(@NonNull String color) {
        float[] hsv = new float[3];
        Color.colorToHSV(Color.parseColor(color), hsv);
        return BitmapDescriptorFactory.defaultMarker(hsv[0]);
    }
//    public static String getColorForSpeed(float speed) {
//        if (speed > 70 ) {
//            return "#ffd0c1";
////            return OVER_SPEED_COLOR;
//        }
//        if (speed > 60) {
//            return "#ffd0c1";
////            return "#e4ffc1";
//        }
//        if (speed > 50) {
//            return "#e4ffc1";
//        }
//        return "#fbffc1";
//    }


    /**
     *
     * @param location
     * @return normalized speed in KPH
     */
    public static float getNormalizedSpeed(@NonNull Location location) {
        return location.getSpeed() * Constants.MPS_TO_KPH + 3;
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
