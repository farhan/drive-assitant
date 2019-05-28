//package com.travel.driveassistant.managers;
//
//import android.content.Context;
//import android.support.annotation.NonNull;
//
//import com.travel.driveassistant.tracker.events.LocationRequestEvent;
//
//import org.greenrobot.eventbus.EventBus;
//
//public class DataInputManager {
//    public static void startTakingLocationUpdates(@NonNull final Context applicationContext) {
//        EventBus.getDefault().post(new LocationRequestEvent(true));
//    }
//
//    public static void stopTakingLocationUpdates(@NonNull final Context applicationContext) {
//        EventBus.getDefault().post(new LocationRequestEvent(false));
//    }
//
//    public static void startTakingUserActivityUpdates(@NonNull final Context applicationContext) {
//
//    }
//
//    public static void stopTakingUserActivityUpdates(@NonNull final Context applicationContext) {
//
//    }
//}
