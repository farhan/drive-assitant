/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.travel.driveassistant.tracker.services;

import android.app.IntentService;
import android.content.Intent;

import com.google.android.gms.location.ActivityRecognitionResult;
import com.google.android.gms.location.ActivityTransitionEvent;
import com.google.android.gms.location.ActivityTransitionResult;
import com.google.android.gms.location.DetectedActivity;
import com.travel.driveassistant.lib_utils.FileLogger;
import com.travel.driveassistant.lib_utils.Logger;
import com.travel.driveassistant.tracker.events.ActivityTransitionUpdateEvent;
import com.travel.driveassistant.tracker.events.UserActivityUpdateEvent;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 *  IntentService for handling incoming intents that are generated as a result of requesting
 *  activity updates using
 *  {@link com.google.android.gms.location.ActivityRecognitionApi#requestActivityUpdates}.
 */
public class CommonIntentService extends IntentService {
    private static final Logger logger = new Logger(CommonIntentService.class.getSimpleName());

    public static final String ACTION_ACTIVITY_RECOGNITION_UPDATES = "ACTION_ACTIVITY_RECOGNITION_UPDATES";
    public static final String ACTION_ACTIVITY_TRANSITION_UPDATES = "ACTION_ACTIVITY_TRANSITION_UPDATES";

    /**
     * This constructor is required, and calls the super IntentService(String)
     * constructor with the name for a worker thread.
     */
    public CommonIntentService() {
        // Use the TAG to name the worker thread.
        super("CommonIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * Handles incoming intents.
     * @param intent The Intent is provided (inside a PendingIntent) when requestActivityUpdates()
     *               is called.
     */
    @SuppressWarnings("unchecked")
    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null || intent.getAction() == null) {
            return;
        }

        switch (intent.getAction()) {
            case ACTION_ACTIVITY_RECOGNITION_UPDATES:
                handleActivityRecognitionUpdates(intent);
                break;
            case ACTION_ACTIVITY_TRANSITION_UPDATES:
                handleActivityTransitionUpdates(intent);
                break;
        }
    }

    public void handleActivityRecognitionUpdates(Intent intent) {
        if (!ActivityRecognitionResult.hasResult(intent)) {
            return;
        }
        final ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

        // Get the list of the probable activities associated with the current state of the
        // device. Each activity is associated with a confidence level, which is an int between
        // 0 and 100.
        ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

        logger.debug("Got user activity update : " + detectedActivities.toString());
        EventBus.getDefault().post(new UserActivityUpdateEvent(detectedActivities));

        FileLogger.write(detectedActivities);

//        // Log each activity.
//        Log.i(TAG, "activities detected");
//        for (DetectedActivity da: detectedActivities) {
//            Log.i(TAG, Utils.getActivityString(
//                            getApplicationContext(),
//                            da.getType()) + " " + da.getConfidence() + "%"
//            );
//        }
    }

    public void handleActivityTransitionUpdates(Intent intent) {
        if (!ActivityTransitionResult.hasResult(intent)) {
            return;
        }
        final ActivityTransitionResult result = ActivityTransitionResult.extractResult(intent);

        final List<ActivityTransitionEvent> transitionEvents = result.getTransitionEvents();
        for (ActivityTransitionEvent event : transitionEvents) {

            FileLogger.write(event);

            // chronological sequence of events....
            EventBus.getDefault().post(new ActivityTransitionUpdateEvent(event));

//            // write log
//            String activity = CommonUtils.getActivityTypeName(event.getActivityType());
//            String transition = String.valueOf(CommonUtils.getTransitionTypeName(event.getTransitionType()));
//            FileLogger.writeDetailLog("Got Activity transition update: "+activity+"_"+transition);
        }
    }
}
