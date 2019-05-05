package com.travel.driveassistant.tracker.managers;

import android.annotation.SuppressLint;
import android.content.Context;
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

import org.greenrobot.eventbus.EventBus;

public class LocationManager {
    private Logger logger = new Logger(getClass().getSimpleName());

    private final long GPS_TIME_INTERVAL = 1000 * 2;
    private final long GPS_FASTEST_TIME_INTERVAL = 1000;
    private final int GPS_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    private Context context;
    private BusinessLogicManager businessLogicManager;

    public LocationManager(@NonNull Context applicationContext, @NonNull BusinessLogicManager businessLogicManager) {
        this.context = applicationContext;
        this.businessLogicManager = businessLogicManager;
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
                    final Location location = locationResult.getLastLocation();
                    if (location != null) {
                        FileLogger.write(context, location);
                        logger.debug("Got Location Update : "+location.toString());
                        EventBus.getDefault().post(new LocationUpdateEvent(location));
                    }
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
                null /* Looper */);
        logger.debug("startLocationUpdates");
    }

    public void stopLocationUpdates() {
        if (fusedLocationClient != null) {
            fusedLocationClient.removeLocationUpdates(locationCallback);
            fusedLocationClient = null;
        }
        logger.debug("stopLocationUpdates");
    }
}
