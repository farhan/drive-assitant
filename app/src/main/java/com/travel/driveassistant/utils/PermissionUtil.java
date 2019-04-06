package com.travel.driveassistant.utils;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.view.View;

import com.travel.driveassistant.BuildConfig;
import com.travel.driveassistant.events.PermissionGrantedEvent;
import com.travel.driveassistant.lib_utils.PopUpUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class PermissionUtil {

    public static final int REQUEST_CODE_CORE_PERMISSION = 1002;
    public static final int REQUEST_CODE_WRITE_PERMISSION = 1001;
    public static final int REQUEST_CODE_LOCATION_PERMISSION = 1002;

    public static String[] CORE_APP_PERMISSIONS = BuildConfig.DEBUG ?
            // Debug app permissions
            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION} :
            // Release app permissions
            new String[]{Manifest.permission.ACCESS_FINE_LOCATION};

    public static boolean checkPermissionsGranted(@NonNull Context context, @NonNull String... permissions) {
        if (context == null) {
            return false;
        }
        for (String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param context
     * @param permissions
     * @return list of permissions which are not granted by user yet
     */
    private static String[] filterDeniedPermission(@NonNull Context context, @NonNull String... permissions) {
        final ArrayList<String> unGrantedPermissions = new ArrayList();
        for (final String permission : permissions) {
            if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                unGrantedPermissions.add(permission);
            }
        }
        return unGrantedPermissions.size() > 0 ? unGrantedPermissions.toArray(new String[0]) : null;
    }

    public static void requestAppCorePermissions(@NonNull Activity activity) {
        if (activity == null) {
            return;
        }
        final String[] filterDeniedPermission = filterDeniedPermission(activity, CORE_APP_PERMISSIONS);
        if (filterDeniedPermission != null) {
            ActivityCompat.requestPermissions(activity, filterDeniedPermission, REQUEST_CODE_CORE_PERMISSION);
        }
    }

    @TargetApi(Build.VERSION_CODES.M)
    public static void onRequestCorePermissionsResult(final Activity activity, int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CORE_PERMISSION:
                if (permissions != null && permissions.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        final String permission = permissions[i];
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            // Permission granted, yuphee!
                            EventBus.getDefault().post(new PermissionGrantedEvent(permission));
                        } else {
                            if (activity.shouldShowRequestPermissionRationale(permission)) {
                                PopUpUtil.showAlertDialog(activity,
                                        "You need to allow access to the '" + permission + "' permission to access the core functionality of this app.",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                requestAppCorePermissions(activity);
                                            }
                                        });
                                return;
                            } else {
                                PopUpUtil.showSnackbar(activity,
                                        "'" + permission + "' Permission is mandatory to run the app.",
                                        "Settings", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                // Build intent that displays the App settings screen.
                                                Intent intent = new Intent();
                                                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", BuildConfig.APPLICATION_ID, null);
                                                intent.setData(uri);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                activity.startActivity(intent);
                                            }
                                        });
                            }
                        }
                    }
                }
                break;
        }

    }

//    public static boolean checkWritePermissionGranted(@NonNull Context context) {
//        int permissionState = ActivityCompat.checkSelfPermission(context,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        return permissionState == PackageManager.PERMISSION_GRANTED;
//    }
//
//    public static void requestWritePermissionIfRequired(@NonNull Activity activity) {
//        if (!checkWritePermissionGranted(activity)) {
//            requestWritePermission(activity);
//        }
//    }
//
//    public static void startWritePermissionRequest(@NonNull Activity activity) {
//        ActivityCompat.requestPermissions(activity,
//                new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
//                REQUEST_CODE_WRITE_PERMISSION);
//    }
//
//    public static void requestWritePermission(@NonNull final Activity activity) {
//        boolean shouldProvideRationale =
//                ActivityCompat.shouldShowRequestPermissionRationale(activity,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE);
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            PopUpUtil.showSnackbar(activity, R.string.write_permission_rationale, android.R.string.ok,
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // Request permission
//                            startWritePermissionRequest(activity);
//                        }
//                    });
//
//        } else {
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            startWritePermissionRequest(activity);
//        }
//    }
//
//    public static void requestLocationPermissionIfRequired(@NonNull Activity activity) {
//        if (!checkLocationPermissionGranted(activity)) {
//            requestLocationPermission(activity);
//        }
//    }
//
//    public static boolean checkLocationPermissionGranted(@NonNull Context context) {
//        int permissionState = ActivityCompat.checkSelfPermission(context,
//                Manifest.permission.ACCESS_FINE_LOCATION);
//        return permissionState == PackageManager.PERMISSION_GRANTED;
//    }
//
//    public static void startLocationPermissionRequest(@NonNull Activity activity) {
//        ActivityCompat.requestPermissions(activity,
//                new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                REQUEST_CODE_LOCATION_PERMISSION);
//    }
//
//    public static void requestLocationPermission(@NonNull final Activity activity) {
//        boolean shouldProvideRationale =
//                ActivityCompat.shouldShowRequestPermissionRationale(activity,
//                        Manifest.permission.ACCESS_FINE_LOCATION);
//
//        // Provide an additional rationale to the user. This would happen if the user denied the
//        // request previously, but didn't check the "Don't ask again" checkbox.
//        if (shouldProvideRationale) {
//            PopUpUtil.showSnackbar(activity, R.string.location_permission_rationale, android.R.string.ok,
//                    new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            // Request permission
//                            startLocationPermissionRequest(activity);
//                        }
//                    });
//
//        } else {
//            // Request permission. It's possible this can be auto answered if device policy
//            // sets the permission in a given state or the user denied the permission
//            // previously and checked "Never ask again".
//            startLocationPermissionRequest(activity);
//        }
//    }
}
