package com.travel.driveassistant.tracker.managers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.travel.driveassistant.lib_utils.FileLogger;
import com.travel.driveassistant.lib_utils.Logger;
import com.travel.driveassistant.tracker.services.CommonIntentService;

public class ActivityRecognitionManager {
    private static final Logger logger = new Logger(ActivityRecognitionManager.class.getSimpleName());

    /**
     * The desired time between activity detections. Larger values result in fewer activity
     * detections while improving battery life. A value of 0 results in activity detections at the
     * fastest possible rate.
     */
    static final long DETECTION_INTERVAL_IN_MILLISECONDS = 3 * 1000; // 5 seconds

    public static void startActivityUpdates(@NonNull final Context context) {
        final ActivityRecognitionClient client = new ActivityRecognitionClient(context);
        Task<Void> task = client.requestActivityUpdates(
                DETECTION_INTERVAL_IN_MILLISECONDS,
                getActivityDetectionPendingIntent(context));

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                logger.debug("Activity recognition updates enabled successfully.");
                FileLogger.writeDetailLog("Activity recognition updates enabled successfully.");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                logger.debug("Failed to enable activity recognition updates.");
                FileLogger.writeDetailLog("Failed to enable activity recognition updates.");
            }
        });
    }

    public static void stopActivityUpdates(final Context context) {
        final ActivityRecognitionClient client = new ActivityRecognitionClient(context);
        final Task<Void> task = client.removeActivityUpdates(getActivityDetectionPendingIntent(context));
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                logger.debug("Activity recognition updates disabled successfully.");
                FileLogger.writeDetailLog("Activity recognition updates disabled successfully.");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                logger.debug("Failed to disable activity recognition updates.");
                FileLogger.writeDetailLog("Failed to disable activity recognition updates.");
            }
        });
    }

    private static PendingIntent getActivityDetectionPendingIntent(Context context) {
        Intent intent = new Intent(context, CommonIntentService.class);
        intent.setAction(CommonIntentService.ACTION_ACTIVITY_RECOGNITION_UPDATES);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // requestActivityUpdates() and removeActivityUpdates().
        return PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
