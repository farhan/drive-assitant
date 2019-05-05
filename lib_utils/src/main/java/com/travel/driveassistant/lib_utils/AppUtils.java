package com.travel.driveassistant.lib_utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;

public class AppUtils {
    public static Drawable getAppIconByPackageName(String ApkTempPackageName, Context context) {
        Drawable drawable;
        try {
            drawable = context.getPackageManager().getApplicationIcon(ApkTempPackageName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            drawable = ContextCompat.getDrawable(context, android.R.drawable.ic_menu_mylocation);
        }
        return drawable;
    }

    public static String getAppNameByPackageName(String ApkPackageName, Context context) {
        String Name = "";
        ApplicationInfo applicationInfo;
        PackageManager packageManager = context.getPackageManager();
        try {
            applicationInfo = packageManager.getApplicationInfo(ApkPackageName, 0);
            if (applicationInfo != null) {
                Name = (String) packageManager.getApplicationLabel(applicationInfo);
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return Name;
    }
}
