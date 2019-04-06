package com.travel.driveassistant.events;

import android.support.annotation.NonNull;

public class PermissionGrantedEvent {
    public String permission;

    public PermissionGrantedEvent(@NonNull String permission) {
        this.permission =  permission;
    }
}
