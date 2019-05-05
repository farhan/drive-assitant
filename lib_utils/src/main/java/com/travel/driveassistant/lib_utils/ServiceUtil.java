package com.travel.driveassistant.lib_utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;

public class ServiceUtil {
    public static void startServiceAsForeground(@NonNull Service service) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForeground(service);
        } else {
            service.startForeground(1, new Notification());
        }
    }

    public static void stopServiceAsForeground(@NonNull Service service) {
        service.stopForeground(true);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private static void startForeground(@NonNull Service service) {
        final String NOTIFICATION_CHANNEL_ID = service.getApplicationContext().getPackageName();
        final String channelName = "Location Service";
        NotificationChannel chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
        chan.setLightColor(Color.BLUE);
        chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        final NotificationManager manager = (NotificationManager) service.getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        manager.createNotificationChannel(chan);


        final NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(service, NOTIFICATION_CHANNEL_ID);
        final Notification notification = notificationBuilder.setOngoing(true)
                // TODO: set app icon here
                .setSmallIcon(android.R.drawable.ic_menu_mylocation)
                .setContentTitle("App '" +AppUtils.getAppNameByPackageName(service.getPackageName(), service) + "' is running in background.")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        service.startForeground(2, notification);
    }
}
