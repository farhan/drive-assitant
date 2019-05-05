package com.travel.driveassistant.tracker.managers;

import android.content.Context;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;
import com.travel.driveassistant.lib_speed_data.OverSpeedResult;
import com.travel.driveassistant.lib_speed_data.OverSpeedUtil;
import com.travel.driveassistant.lib_utils.MapUtil;
import com.travel.driveassistant.lib_utils.managers.TTSManager;
import com.travel.driveassistant.tracker.events.LocationUpdateEvent;
import com.travel.driveassistant.tracker.events.LocationUpdateOverSpeedEvent;
import com.travel.driveassistant.tracker.events.OverSpeedEvent;
import com.travel.driveassistant.tracker.utils.TrackerUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class BusinessLogicManager {
    private TTSManager ttsManager;
    private Location lastUserLocation = null;

    public BusinessLogicManager(@NonNull Context applicationContext) {
        EventBus.getDefault().register(this);
        ttsManager = new TTSManager(applicationContext);
    }

    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        ttsManager.onDestroy();
        lastUserLocation = null;
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
                // Fire over speeding event
                EventBus.getDefault().post(new OverSpeedEvent(userLocation, result));
                ttsManager.speak("Please avoid over speeding");
                //TODO: Disable taking locations for some time
            }
        }
        EventBus.getDefault().post(new LocationUpdateOverSpeedEvent(userLocation, result));
    }
}
