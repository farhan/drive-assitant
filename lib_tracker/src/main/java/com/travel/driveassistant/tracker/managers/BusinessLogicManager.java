package com.travel.driveassistant.tracker.managers;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.maps.model.LatLng;
import com.travel.driveassistant.lib_speed_data.OverSpeedResult;
import com.travel.driveassistant.lib_speed_data.OverSpeedUtil;
import com.travel.driveassistant.lib_utils.CommonUtils;
import com.travel.driveassistant.lib_utils.FileLogger;
import com.travel.driveassistant.lib_utils.MapUtil;
import com.travel.driveassistant.lib_utils.managers.TTSManager;
import com.travel.driveassistant.tracker.events.ActivityTransitionUpdateEvent;
import com.travel.driveassistant.tracker.events.LocationRequestEvent;
import com.travel.driveassistant.tracker.events.LocationUpdateEvent;
import com.travel.driveassistant.tracker.events.LocationUpdateOverSpeedEvent;
import com.travel.driveassistant.tracker.events.OverSpeedEvent;
import com.travel.driveassistant.tracker.utils.TrackerUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class BusinessLogicManager {
    private ActivityTransitionEvent userLastTransitionEvent = null;
    private Context context;
    private TTSManager ttsManager;
    private Location lastUserLocation = null;

    public BusinessLogicManager(@NonNull Context applicationContext) {
        context = applicationContext;
        EventBus.getDefault().register(this);
        ttsManager = new TTSManager(applicationContext);
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        ttsManager.onDestroy();
        lastUserLocation = null;
    }

    @Subscribe
    public void onMessageEvent(ActivityTransitionUpdateEvent event) {
        final ActivityTransitionEvent transitionEvent = event.transitionEvent;
        if (!CommonUtils.isUserStateChanged(userLastTransitionEvent, transitionEvent)) {
            return;
        }
        switch (transitionEvent.getActivityType()) {
            case DetectedActivity.IN_VEHICLE:
                switch (transitionEvent.getTransitionType()) {
                    case ActivityTransition.ACTIVITY_TRANSITION_ENTER:
                        onStartDriving();
                        break;
                    case ActivityTransition.ACTIVITY_TRANSITION_EXIT:
                        onEndDriving();
                        break;
                }
                break;
        }
        userLastTransitionEvent = transitionEvent;
    }

    public void onStartDriving() {
        FileLogger.writeCommonLog("START_DRIVING");
        ttsManager.speak("Drive safely!");
        EventBus.getDefault().post(new LocationRequestEvent(true));
    }

    public void onEndDriving() {
        EventBus.getDefault().post(new LocationRequestEvent(false));
        if (CommonUtils.isLastStateWasVehicleEnter(userLastTransitionEvent)) {
            FileLogger.writeCommonLog("END_DRIVING");
            ttsManager.speak("Nice drive champ!");
        } else {
            FileLogger.writeCommonLog("END_DRIVING but last event was not IN_VEHICLE_ENTER");
        }
    }

    @Subscribe
    public void onMessageEvent(LocationUpdateEvent event) {
        final Location userLocation = event.location;
        final float userSpeed = MapUtil.getNormalizedSpeed(userLocation);

        OverSpeedResult result = null;
        if (TrackerUtils.isValuableSpeedDiff(userSpeed, lastUserLocation, userLocation)) {
            lastUserLocation = userLocation;

            final LatLng userLatLng = new LatLng(userLocation.getLatitude(), userLocation.getLongitude());

            // Monitor over userSpeed
            result = OverSpeedUtil.checkOverSpeed(userLatLng, userSpeed);
            if (result.isOverSpeed) {
                FileLogger.writeCommonLog("OVER_SPEED DETECTED!!!, speed: " + result.userSpeedNormalized);
                ttsManager.speak("Please avoid over speeding");

                // Fire over speeding transitionEvent
                EventBus.getDefault().post(new OverSpeedEvent(userLocation, result));

                //TODO: Disable taking locations for some time
            }
        }
        EventBus.getDefault().post(new LocationUpdateOverSpeedEvent(userLocation, result));
    }
}
