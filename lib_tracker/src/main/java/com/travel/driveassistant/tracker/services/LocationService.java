//package com.travel.driveassistant.tracker.services;
//
//import android.annotation.SuppressLint;
//import android.app.Service;
//import android.content.Context;
//import android.content.Intent;
//import android.location.Location;
//import android.os.IBinder;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//
//import com.google.android.gms.location.FusedLocationProviderClient;
//import com.google.android.gms.location.LocationCallback;
//import com.google.android.gms.location.LocationRequest;
//import com.google.android.gms.location.LocationResult;
//import com.google.android.gms.location.LocationServices;
//import com.travel.driveassistant.lib_utils.Logger;
//import com.travel.driveassistant.tracker.events.LocationUpdateEvent;
//import com.travel.driveassistant.lib_utils.ServiceUtil;
//
//import org.greenrobot.eventbus.EventBus;
//
//public class LocationService extends Service {
//    private Logger logger = new Logger(LocationService.class.getSimpleName());
//
//    private final long GPS_TIME_INTERVAL = 1000 * 2;
//    private final long GPS_FASTEST_TIME_INTERVAL = 1000;
//    private final int GPS_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;
//
//    private FusedLocationProviderClient fusedLocationClient;
//    private LocationRequest locationRequest;
//    private LocationCallback locationCallback;
//
//    public static void start(@NonNull Context context, String action) {
//        final Intent intent = new Intent(context, LocationService.class);
//        intent.setAction(action);
//        context.startService(intent);
//    }
//
//    public static void stop(Context context) {
//        context.stopService(new Intent(context, LocationService.class));
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
////        ServiceUtil.startServiceAsForeground(this);
//        locationCallback = new LocationCallback() {
//            public void onLocationResult(LocationResult locationResult) {
//                final Location location = locationResult.getLastLocation();
//                if (location != null) {
//                    logger.debug("Got Location Update : " + location.toString());
//                    EventBus.getDefault().post(new LocationUpdateEvent(location));
//                }
//            }
//        };
//    }
//
//    protected LocationRequest getLocationRequest() {
//        if (locationRequest == null) {
//            locationRequest = new LocationRequest();
//            locationRequest.setInterval(GPS_TIME_INTERVAL);
//            locationRequest.setFastestInterval(GPS_FASTEST_TIME_INTERVAL);
//            locationRequest.setPriority(GPS_PRIORITY);
//        }
//        return locationRequest;
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
//        startLocationUpdates();
//        // Tells the system to not try to recreate the service after it has been killed.
//        return START_NOT_STICKY;
//    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        stopLocationUpdates();
//        ServiceUtil.stopServiceAsForeground(this);
//    }
//
//    @Nullable
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @SuppressLint("MissingPermission")
//    private void startLocationUpdates() {
//        fusedLocationClient.requestLocationUpdates(getLocationRequest(),
//                locationCallback,
//                null /* Looper */);
//    }
//
//    private void stopLocationUpdates() {
//        fusedLocationClient.removeLocationUpdates(locationCallback);
//    }
//}
