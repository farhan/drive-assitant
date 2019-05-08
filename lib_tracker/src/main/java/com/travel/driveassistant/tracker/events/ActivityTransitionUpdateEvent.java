package com.travel.driveassistant.tracker.events;

import com.google.android.gms.location.ActivityTransitionEvent;

public class ActivityTransitionUpdateEvent {
    public final ActivityTransitionEvent transitionEvent;

    public ActivityTransitionUpdateEvent(ActivityTransitionEvent event) {
        this.transitionEvent =  event;
    }
}
