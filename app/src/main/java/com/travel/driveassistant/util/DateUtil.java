package com.travel.driveassistant.util;

public class DateUtil {
    public static boolean underNSecs(long oldTimeStamp, int seconds) {
        return (System.currentTimeMillis()-oldTimeStamp) < seconds*1000;
    }
}
