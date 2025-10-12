package com.myspps.pdfscanner.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

public class Permissions {

    public static final int STORAGE_PERMISSION_CODE = 100;
    public static final int CAMERA_PERMISSION_CODE = 101;

    // 🧾 Request storage/media permissions for Activity
    public static void requestMediaPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String[] permissions = {
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
            };
            if (!hasAllPermissions(activity, permissions)) {
                ActivityCompat.requestPermissions(activity, permissions, STORAGE_PERMISSION_CODE);
            }
        } else {
            requestReadStoragePermission(activity);
        }
    }

    // 🧾 Request storage/media permissions for Fragment
    public static void requestMediaPermission(Fragment fragment) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String[] permissions = {
                    Manifest.permission.READ_MEDIA_IMAGES,
                    Manifest.permission.READ_MEDIA_VIDEO,
                    Manifest.permission.READ_MEDIA_AUDIO
            };
            if (!hasAllPermissions(fragment.getActivity(), permissions)) {
                fragment.requestPermissions(permissions, STORAGE_PERMISSION_CODE);
            }
        } else {
            requestReadStoragePermission(fragment);
        }
    }

    // 📂 Legacy storage permission (Activity)
    public static void requestReadStoragePermission(Activity activity) {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (!hasAllPermissions(activity, permissions)) {
            ActivityCompat.requestPermissions(activity, permissions, STORAGE_PERMISSION_CODE);
        }
    }

    // 📂 Legacy storage permission (Fragment)
    public static void requestReadStoragePermission(Fragment fragment) {
        String[] permissions = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };
        if (!hasAllPermissions(fragment.getActivity(), permissions)) {
            fragment.requestPermissions(permissions, STORAGE_PERMISSION_CODE);
        }
    }

    // 📷 Camera permission request (Activity)
    public static void requestCameraPermission(Activity activity) {
        if (!hasCameraPermission(activity)) {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        }
    }

    // 📷 Camera permission request (Fragment)
    public static void requestCameraPermission(Fragment fragment) {
        if (!hasCameraPermission(fragment.getActivity())) {
            fragment.requestPermissions(new String[]{Manifest.permission.CAMERA},
                    CAMERA_PERMISSION_CODE);
        }
    }

    // ✅ Check storage permission (Activity)
    public static boolean hasStoragePermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_MEDIA_IMAGES)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        }
    }

    // ✅ Check camera permission
    public static boolean hasCameraPermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED;
    }

    // ✅ Helper: check multiple permissions
    private static boolean hasAllPermissions(Activity activity, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
