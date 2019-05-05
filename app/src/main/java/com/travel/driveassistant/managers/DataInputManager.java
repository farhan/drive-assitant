package com.travel.driveassistant.managers;

import android.content.Context;
import android.support.annotation.NonNull;

import com.travel.driveassistant.lib_utils.Logger;
import com.travel.driveassistant.tracker.events.LocationRequestEvent;
import com.travel.driveassistant.tracker.managers.ActivityRecognitionManager;
import com.travel.driveassistant.tracker.managers.ActivityTransitionManager;

import org.greenrobot.eventbus.EventBus;

public class DataInputManager {
    private Logger logger = new Logger(DataInputManager.class.getSimpleName());
//    private Context applicationContext;
//
//    public DataInputManager(@NonNull final Context applicationContext) {
//        this.applicationContext = applicationContext;
//    }

    public static void startTakingLocationUpdates(@NonNull final Context applicationContext) {
//        LocationService.start(applicationContext, null);
        EventBus.getDefault().post(new LocationRequestEvent(true));
    }

    public static void stopTakingLocationUpdates(@NonNull final Context applicationContext) {
//        LocationService.stop(applicationContext);
        EventBus.getDefault().post(new LocationRequestEvent(false));
    }

    public static void startTakingUserActivityUpdates(@NonNull final Context applicationContext) {
        ActivityRecognitionManager.startActivityUpdates(applicationContext);
        ActivityTransitionManager.startTransitionUpdates(applicationContext);
    }

    public static void stopTakingUserActivityUpdates(@NonNull final Context applicationContext) {
        ActivityRecognitionManager.stopActivityUpdates(applicationContext);
        ActivityTransitionManager.stopTransitionUpdates(applicationContext);
    }
}
