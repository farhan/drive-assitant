package com.travel.driveassistant.util;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.travel.driveassistant.R;

public class PermissionUtil {

    public static final int REQUEST_CODE_WRITE_PERMISSION = 1001;
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 1002;

    /**
     * Check that all given permissions have been granted by verifying that each entry in the
     * given array is of the value {@link PackageManager#PERMISSION_GRANTED}.
     *
     * @see Activity#onRequestPermissionsResult(int, String[], int[])
     */
    public static boolean verifyPermissions(int[] grantResults) {
        // At least one result must be checked.
        if (grantResults.length < 1) {
            return false;
        }

        // Verify that each required permission has been granted, otherwise return false.
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkPermissionsGranted(@NonNull Context context, String... permissions) {
        if (context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void requestWriteAndLocationPermissions(@NonNull Activity activity) {
        if(!checkPermissionsGranted(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                            Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    public static void requestWritePermissionIfRequired(@NonNull Activity activity) {
        if (!checkWritePermissionGranted(activity)) {
            requestWritePermission(activity);
        }
    }

    public static boolean checkWritePermissionGranted(@NonNull Context context) {
        int permissionState = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public static void startWritePermissionRequest(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                REQUEST_CODE_WRITE_PERMISSION);
    }

    public static void requestWritePermission(@NonNull final Activity activity) {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            PopUpUtil.showSnackbar(activity, R.string.write_permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startWritePermissionRequest(activity);
                        }
                    });

        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startWritePermissionRequest(activity);
        }
    }

    public static void requestLocationPermissionIfRequired(@NonNull Activity activity) {
        if (!checkLocationPermissionGranted(activity)) {
            requestLocationPermission(activity);
        }
    }

    public static boolean checkLocationPermissionGranted(@NonNull Context context) {
        int permissionState = ActivityCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    public static void startLocationPermissionRequest(@NonNull Activity activity) {
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                REQUEST_CODE_LOCATION_PERMISSION);
    }

    public static void requestLocationPermission(@NonNull final Activity activity) {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            PopUpUtil.showSnackbar(activity, R.string.location_permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            startLocationPermissionRequest(activity);
                        }
                    });

        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            startLocationPermissionRequest(activity);
        }
    }
}
