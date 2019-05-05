package com.travel.driveassistant.tracker.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.telephony.CellLocation;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import com.travel.driveassistant.lib_utils.Logger;
import com.travel.driveassistant.lib_utils.ServiceUtil;
import com.travel.driveassistant.tracker.events.LocationRequestEvent;
import com.travel.driveassistant.tracker.managers.LocationManager;
import com.travel.driveassistant.tracker.managers.ActivityRecognitionManager;
import com.travel.driveassistant.tracker.managers.BusinessLogicManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

public class BackgroundService extends Service {
    private static Logger logger = new Logger(BackgroundService.class.getName());

    private final long CELL_LOCATION_DELAY_MILLIS = 10 * 1000;

    private boolean isServiceRunning = false;
    private String lastCellLocationId;
    private PhoneStateListener phoneStateListener;
    private TelephonyManager telephonyManager;
    private LocationManager locationManager;
    private BusinessLogicManager businessLogicManager;
    final private Handler handler = new Handler();

    public static void start(@NonNull Context context, String action) {
        final Intent intent = new Intent(context, BackgroundService.class);
        intent.setAction(action);
        context.startService(intent);
    }

    public static void stop(Context context) {
        context.stopService(new Intent(context, BackgroundService.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        logger.debug("BackgroundService.onCreate");

        isServiceRunning = true;

        EventBus.getDefault().register(this);

        ServiceUtil.startServiceAsForeground(this);

        businessLogicManager = new BusinessLogicManager(getApplicationContext());
        locationManager = new LocationManager(getApplicationContext(), businessLogicManager);

        phoneStateListener = new PhoneStateListener() {
            @Override
            public void onCellLocationChanged(final CellLocation cellLocation) {
                super.onCellLocationChanged(cellLocation);

                logger.debug("BackgroundService.onCellLocationChanged");

                if (telephonyManager == null) {
                    return;
                }

                if (cellLocation == null) {
                    return;
                }

                if (isCellLocationChanged(cellLocation)) {
                    lastCellLocationId = cellLocation.toString();
                    logger.debug("User cell location changed to " + lastCellLocationId);
                    ActivityRecognitionManager.startActivityUpdates(getApplicationContext());
                }

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        CellLocation.requestLocationUpdate();
//                        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CELL_LOCATION);
                    }
                }, CELL_LOCATION_DELAY_MILLIS);
            }
        };
    }

    private boolean isCellLocationChanged(@NonNull CellLocation cellLocation) {
        if (lastCellLocationId == null) {
            return true;
        }
        return !cellLocation.toString().equals(lastCellLocationId);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        logger.debug("BackgroundService.onStartCommand");

        telephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_CELL_LOCATION);

        return START_STICKY;
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        logger.debug("BackgroundService.onLowMemory");
    }

    @Override
    public void onTrimMemory(int level) {
        if (level == TRIM_MEMORY_RUNNING_CRITICAL) {
            logger.debug("BackgroundService.onTrimMemory, level=TRIM_MEMORY_RUNNING_CRITICAL");
        }
    }

    @Subscribe
    public void onEvent(LocationRequestEvent event) {
        if (!this.isServiceRunning) {
            return;
        }
        if (event.isStartRequesting) {
            locationManager.startLocationUpdates(getApplicationContext());
        } else {
            locationManager.stopLocationUpdates();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        logger.debug("BackgroundService.onDestroy");

        isServiceRunning = false;

        try {
            EventBus.getDefault().unregister(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (telephonyManager != null) {
                telephonyManager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            if (businessLogicManager != null) {
                businessLogicManager.onDestroy();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        ServiceUtil.stopServiceAsForeground(this);
    }
}
