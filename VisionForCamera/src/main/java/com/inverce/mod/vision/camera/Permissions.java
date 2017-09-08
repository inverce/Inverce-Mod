package com.inverce.mod.vision.camera;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;

public class Permissions {

    public final static String[] CAMERA_PERMISSIONS = {
            Manifest.permission.CAMERA
    };

    public static boolean hasPermissions(String[] permissions, Activity activity) {
        if (!isApi(23)) { // if api is less than 23 we dont need to check permissions
            return true;
        }

        if (activity == null) {
            return false;
        }

        try {
            for (String permission : permissions) {
                if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        } catch (Exception ex) {
            return false;
        }

        return true;
    }

    public static boolean isApi(int level) {
        return android.os.Build.VERSION.SDK_INT >= level;
    }
}

