package com.travel.driveassistant.lib_utils;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;

import com.google.android.gms.location.DetectedActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import driveassitant.travel.com.lib_utils.BuildConfig;

import static com.travel.driveassistant.lib_utils.FileLogger.LOG_TYPE.GPS;

public class FileLogger {
    private static Logger logger = new Logger(FileLogger.class.getName());

    public enum LOG_TYPE {
        GPS, AR
    }

    private static String getFileName(LOG_TYPE type) {
        String date = new SimpleDateFormat("yyyy.MM.dd").format(new Date());
        return date + "-" + type.name() + "-" + Build.MODEL + ".csv";
    }

    private static void writeOnFile(@NonNull Context context, String message, LOG_TYPE type) {
        if (context == null ||
                !BuildConfig.DEBUG ||
                !PermissionUtil.checkWritePermissionGranted(context)) {
            return;
        }
        try {
            final Date date = new Date();
            message = String.valueOf(date.getTime()) + "," + new SimpleDateFormat("HH:mm:ss").format(date)
                    + "," + message ;

            final File root = android.os.Environment.getExternalStorageDirectory();
            final File dir = new File(root.getAbsolutePath() + "/DriveAssistant");
            dir.mkdirs();
            final String fileName = getFileName(type);
            final File file = new File(dir, fileName);

            if (!file.exists()) {
                file.createNewFile();
                switch(type) {
                    case GPS:
                        message = "TIMESTAMP,TIME,LAT,LONG,SPEED,ACCURACY,BEARING/COURSE \r\n"
                                + message;
                        break;
                    case AR:
                        message = "TIMESTAMP,TIME,IN_VEHICLE,ON_BICYCLE,ON_FOOT,STILL,UNKNOWN,TILTING,GARBAGE,WALKING,RUNNING \r\n"
                                + message;
                        break;
                    default:
                        message = "TIMESTAMP,TIME,EVENT\r\n"
                                + message;
                        break;
                }
            }
            logger.debug("LOG:" + message);
            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(message + "\r\n");
            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            logger.debug("Exception occurred in writing log in a file.\n"+e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getCommaSeparatedRow(String[] elements) {
        final StringBuilder csr = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == null)
                elements[i] = "";
            csr.append(elements[i]).append(elements.length - 1 == i ? "" : ",");
        }
        return csr.toString();
    }

    private static String getCommaSeparatedRow(int[] elements) {
        final StringBuilder csr = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            csr.append(elements[i]).append(elements.length - 1 == i ? "" : ",");
        }
        return csr.toString();
    }

    public static void write(@NonNull Context context, @NonNull Location location) {
        if (!BuildConfig.DEBUG) {
            return;
        }
        String[] arr = new String[5];
        arr[0] = String.valueOf(location.getLatitude());
        arr[1] = String.valueOf(location.getLongitude());
        arr[2] = String.valueOf(location.getSpeed());
        arr[3] = String.valueOf(location.getAccuracy());
        arr[4] = String.valueOf(location.getBearing());
        writeOnFile(context, getCommaSeparatedRow(arr), GPS);
    }

    public static void write(@NonNull Context context, @NonNull ArrayList<DetectedActivity> detectedActivities) {
        if (!BuildConfig.DEBUG) {
            return;
        }

        int[] activitiesConfidences = new int[9];
        for (DetectedActivity detectedActivity : detectedActivities) {
            activitiesConfidences[detectedActivity.getType()] = detectedActivity.getConfidence();
        }

        writeOnFile(context, getCommaSeparatedRow(activitiesConfidences), LOG_TYPE.AR);
    }
}
