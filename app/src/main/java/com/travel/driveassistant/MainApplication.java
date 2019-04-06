package com.travel.driveassistant;

import android.app.Application;

import com.travel.driveassistant.lib_utils.Logger;


public class MainApplication extends Application {
    public static Logger sLogger = new Logger(MainApplication.class.getSimpleName());
}
