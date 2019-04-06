package com.travel.driveassistant.managers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.travel.driveassistant.tracker.services.LocationService;
import com.travel.driveassistant.utils.Logger;

public class DataInputManager {
    private Logger logger = new Logger(DataInputManager.class.getSimpleName());
//    private Context applicationContext;
//
//    public DataInputManager(@NonNull final Context applicationContext) {
//        this.applicationContext = applicationContext;
//    }

    public static void startTakingLocationUpdates(@NonNull final Context applicationContext) {
        LocationService.start(applicationContext, null);
    }

    public static void stopTakingLocationUpdates(@NonNull final Context applicationContext) {
        LocationService.stop(applicationContext);
    }
}