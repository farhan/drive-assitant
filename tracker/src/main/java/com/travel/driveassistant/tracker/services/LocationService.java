package com.travel.driveassistant.tracker.services;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.travel.driveassistant.tracker.events.LocationUpdateEvent;
import com.travel.driveassistant.tracker.utils.LogUtil;

import org.greenrobot.eventbus.EventBus;

public class LocationService extends Service {
    private final String LOG_TAG = LocationService.class.getSimpleName();

    private final long GPS_TIME_INTERVAL = 1000 * 2;
    private final long GPS_FASTEST_TIME_INTERVAL = 1000;
    private final int GPS_PRIORITY = LocationRequest.PRIORITY_HIGH_ACCURACY;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    public static void start(@NonNull Context context, String action) {
        final Intent intent = new Intent(context, LocationService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    public static void stop(Context context) {
        context.stopService(new Intent(context, LocationService.class));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            startForeground();
        else
            startForeground(1, new Notification());
        locationCallback = new LocationCallback() {
            public void onLocationResult(LocationResult locationResult) {
                final Location location = locationResult.getLastLocation();
                if (location != null) {
                    LogUtil.log("Got Location Update : "+location.toString());
                    EventBus.getDefault().post(new LocationUpdateEvent(location));
                }
            }
        };
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void startForeground() {
        final String NOTIFICATION_CHANNEL_ID = getApplicationContext().getPackageName();
        final String channelName = "Location Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        final NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);


        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        final Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(getApplicationInfo().icon)
                .setContentTitle(getApplicationInfo().name + " is running in background.")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(2, notification);
    }

    protected LocationRequest getLocationRequest() {
        if (locationRequest == null) {
            locationRequest = new LocationRequest();
            locationRequest.setInterval(GPS_TIME_INTERVAL);
            locationRequest.setFastestInterval(GPS_FASTEST_TIME_INTERVAL);
            locationRequest.setPriority(GPS_PRIORITY);
        }
        return locationRequest;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        startLocationUpdates();
        // Tells the system to not try to recreate the service after it has been killed.
        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
        stopForeground(true);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(getLocationRequest(),
                locationCallback,
                null /* Looper */);
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }
}
