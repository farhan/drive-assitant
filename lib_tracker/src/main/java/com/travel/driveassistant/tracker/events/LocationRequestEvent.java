package com.travel.driveassistant.tracker.events;

public class LocationRequestEvent {
    public final boolean isStartRequesting;

    public LocationRequestEvent(boolean isStartRequesting) {
        this.isStartRequesting =  isStartRequesting;
    }
}
