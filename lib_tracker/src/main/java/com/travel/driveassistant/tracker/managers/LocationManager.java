package com.travel.driveassistant.tracker.managers;

import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.travel.driveassistant.lib_utils.FileLogger;
import com.travel.driveassistant.lib_utils.Logger;
import com.travel.driveassistant.tracker.events.LocationUpdateEvent;
import com.travel.driveassistant.tracker.services.CommonIntentService;

import org.greenrobot.eventbus.EventBus;

public class LocationManager {
    private static Logger logger = new Logger(LocationManager.class.getSimpleName());

    private final long GPS_TIME_INTERVAL = 1000 * 2;
    private final long GPS_FASTEST_TIME_INTERVAL = 1000;
    private final int GPS_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private Context context;

    public LocationManager(@NonNull Context applicationContext) {
        this.context = applicationContext;
    }

    private LocationRequest getLocationRequest() {
        if (locationRequest == null) {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(GPS_TIME_INTERVAL);
            locationRequest.setFastestInterval(GPS_FASTEST_TIME_INTERVAL);
            locationRequest.setPriority(GPS_PRIORITY);
        }
        return locationRequest;
    }

    private LocationCallback getLocationCallback() {
        if (locationCallback == null) {
            locationCallback = new LocationCallback() {
                public void onLocationResult(LocationResult locationResult) {
                    onLocationUpdate(locationResult);
                }
            };
        }
        return locationCallback;
    }

    private FusedLocationProviderClient getFusedLocationClient(@NonNull Context applicationContext) {
        if (fusedLocationClient == null) {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext);
        }
        return fusedLocationClient;
    }

    @SuppressLint("MissingPermission")
    public void startLocationUpdates(@NonNull Context applicationContext) {

        getFusedLocationClient(applicationContext).requestLocationUpdates(getLocationRequest(),
                getLocationCallback(),
                applicationContext.getMainLooper());

//        getFusedLocationClient(applicationContext).requestLocationUpdates(getLocationRequest(),
//                getPendingIntent(applicationContext));

        logger.debug("startLocationUpdates");
        FileLogger.writeCommonLog("LocationManager.startLocationUpdates");
    }

    public void stopLocationUpdates() {
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
            fusedLocationClient = null;
        }
        logger.debug("stopLocationUpdates");
        FileLogger.writeCommonLog("LocationManager.stopLocationUpdates");
    }

    private PendingIntent getPendingIntent(@NonNull Context applicationContext) {
        Intent intent = new Intent(applicationContext, CommonIntentService.class);
        intent.setAction(CommonIntentService.ACTION_LOCATION_UPDATES);
        return PendingIntent.getBroadcast(applicationContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    public static void onLocationUpdate(LocationResult locationResult) {
        final Location location = locationResult.getLastLocation();
        if (location != null) {
            FileLogger.write(location);
            logger.debug("Got Location Update : "+location.toString());
            EventBus.getDefault().post(new LocationUpdateEvent(location));
        }
    }
}
