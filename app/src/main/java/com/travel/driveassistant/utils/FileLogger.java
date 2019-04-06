package com.travel.driveassistant.utils;

import android.content.Context;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;

import com.travel.driveassistant.BuildConfig;
import com.travel.driveassistant.MainApplication;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.travel.driveassistant.utils.FileLogger.LOG_TYPE.*;

public class FileLogger {
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
                        message = "TIMESTAMP,TIME,IN_VEHICLE,ON_BICYCLE,ON_FOOT,STILL,UNKNOWN,TILTING, INFO \r\n"
                                + message;
                        break;
                    default:
                        message = "TIMESTAMP,TIME,EVENT\r\n"
                                + message;
                        break;
                }
            }
            MainApplication.sLogger.debug("LOG:" + message);
            FileOutputStream fOut = new FileOutputStream(file, true);
            OutputStreamWriter myOutWriter = new OutputStreamWriter(fOut);
            myOutWriter.append(message + "\r\n");
            myOutWriter.close();
            fOut.close();
        } catch (IOException e) {
            MainApplication.sLogger.debug("Exception occurred in writing log in a file.\n"+e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getCommaSaperatedRow(String[] elements) {
        final StringBuilder csr = new StringBuilder();
        for (int i = 0; i < elements.length; i++) {
            if (elements[i] == null)
                elements[i] = "";
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
        writeOnFile(context, getCommaSaperatedRow(arr), GPS);
    }
}
