package com.myspps.pdfscanner.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;

public class Permissions {
    public static final int CAMERA_PERMISSION_REQUEST_CODE = 1;
    public static final int READ_PERMISSION_REQUEST_CODE = 2;
    public static final int STORAGE_PERMISSION_REQUEST_CODE = 2;

    public static void requestReadStoragePermission(final Context context) {
        Activity activity = (Activity) context;
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, "android.permission.READ_EXTERNAL_STORAGE")) {
            new AlertDialog.Builder(context).setTitle((CharSequence) "Permission needed").setMessage((CharSequence) "This permission is needed to get images").setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 2);
                }
            }).setNegativeButton((CharSequence) "cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 2);
        }
    }

    public static void writeStoragePermission(final Context context) {
        Activity activity = (Activity) context;
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, "android.permission.WRITE_EXTERNAL_STORAGE")) {
            new AlertDialog.Builder(context).setTitle((CharSequence) "Permission needed").setMessage((CharSequence) "This permission is needed to store files").setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
                }
            }).setNegativeButton((CharSequence) "cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).create().show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.WRITE_EXTERNAL_STORAGE"}, 2);
        }
    }

    public static void requestCameraPermission(final Context context) {
        Activity activity = (Activity) context;
        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, "android.permission.CAMERA")) {
            new AlertDialog.Builder(context).setTitle((CharSequence) "Permission needed").setMessage((CharSequence) "This permission is needed to capture images").setPositiveButton((CharSequence) "OK", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{"android.permission.CAMERA"}, 1);
                }
            }).setNegativeButton((CharSequence) "Cancel", (DialogInterface.OnClickListener) new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.CAMERA"}, 1);
        }
    }
}
