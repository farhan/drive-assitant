package com.travel.driveassistant;

import android.app.Application;

import com.travel.driveassistant.utils.Logger;

public class MainApplication extends Application {
    public static Logger sLogger = new Logger(MainApplication.class.getSimpleName());
}
