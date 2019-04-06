package com.travel.driveassistant.lib_utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Toast;

public class PopUpUtil {

    public static void showToast(@NonNull Context context, String text) {
        Toast.makeText(context, text, Toast.LENGTH_SHORT).show();
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    public static void showSnackbar(@NonNull View view,
            final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(view,
                view.getContext().getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(view.getContext().getString(actionStringId), listener).show();
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    public static void showSnackbar(@NonNull Activity activity,
                              final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(activity.findViewById(android.R.id.content),
                activity.getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(activity.getString(actionStringId), listener).show();
    }

    public static void showSnackbar(@NonNull Activity activity,
                                    final String mainTextString, final String actionString,
                                    View.OnClickListener listener) {
        Snackbar.make(activity.findViewById(android.R.id.content),
                mainTextString,
                Snackbar.LENGTH_INDEFINITE)
                .setAction(actionString, listener).show();
    }

    /**
     * Show alert dialog with simple ok and cancel button
     *
     * @param context
     * @param message
     * @param okListener
     */
    public static void showAlertDialog(@NonNull Context context, String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(context)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
}
