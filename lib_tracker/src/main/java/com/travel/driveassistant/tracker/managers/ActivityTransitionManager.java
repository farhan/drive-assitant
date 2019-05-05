package com.travel.driveassistant.tracker.managers;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;

import com.google.android.gms.location.ActivityRecognition;
import com.google.android.gms.location.ActivityTransition;
import com.google.android.gms.location.ActivityTransitionRequest;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.travel.driveassistant.lib_utils.Logger;
import com.travel.driveassistant.tracker.services.CommonIntentService;

import java.util.ArrayList;
import java.util.List;

public class ActivityTransitionManager {
    private static final Logger logger = new Logger(ActivityTransitionManager.class.getSimpleName());

    public static void startTransitionUpdates(@NonNull Context context) {
        List<ActivityTransition> transitions = new ArrayList<>();

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.IN_VEHICLE)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());
        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.IN_VEHICLE)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.ON_FOOT)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());
        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.ON_FOOT)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.STILL)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_ENTER)
                        .build());
        transitions.add(
                new ActivityTransition.Builder()
                        .setActivityType(DetectedActivity.STILL)
                        .setActivityTransition(ActivityTransition.ACTIVITY_TRANSITION_EXIT)
                        .build());

        ActivityTransitionRequest request = new ActivityTransitionRequest(transitions);

        // myPendingIntent is the instance of PendingIntent where the app receives callbacks.
        Task<Void> task = ActivityRecognition.getClient(context).requestActivityTransitionUpdates(request, getActivityTransitionPendingIntent(context));

        task.addOnSuccessListener(
                new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void result) {
                        logger.debug("Activity transition updates enabled successfully.");
                    }
                }
        );

        task.addOnFailureListener(
                new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        logger.debug("Failed to enable activity transition updates.");
                    }
                }
        );
    }

    public static void stopTransitionUpdates(Context context) {
        final PendingIntent pendingIntent = getActivityTransitionPendingIntent(context);
        final Task<Void> task = ActivityRecognition.getClient(context)
                .removeActivityTransitionUpdates(pendingIntent);

        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                pendingIntent.cancel();
                logger.debug("Activity recognition updates disabled successfully.");
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                logger.debug("Failed to disable activity recognition updates.");
            }
        });
    }

    private static PendingIntent getActivityTransitionPendingIntent(Context context) {
        Intent intent = new Intent(context, CommonIntentService.class);
        intent.setAction(CommonIntentService.ACTION_ACTIVITY_TRANSITION_UPDATES);
        // We use FLAG_UPDATE_CURRENT so that we get the same pending intent back when calling
        // requestActivityUpdates() and removeActivityUpdates().
        return PendingIntent.getService(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}
