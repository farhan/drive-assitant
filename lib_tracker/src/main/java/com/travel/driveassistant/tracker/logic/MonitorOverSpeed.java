package com.travel.driveassistant.tracker.logic;

import android.location.Location;
import android.support.annotation.NonNull;

public class MonitorOverSpeed {
    public static int getSpeedLimitOfLocation(@NonNull Location location) {
        return 80;
    }
}
