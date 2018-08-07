package com.inverce.mod.vision.camera

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat

object Permissions {
    val CAMERA_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)

    fun hasPermissions(permissions: Array<String>, activity: Activity): Boolean {
        when {
            !isApi(23) -> return true
            else -> {
                try {
                    for (permission in permissions) {
                        if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                            return false
                        }
                    }
                } catch (ex: Exception) {
                    return false
                }
                return true
            }
        }
    }


    fun isApi(level: Int) = android.os.Build.VERSION.SDK_INT >= level
}