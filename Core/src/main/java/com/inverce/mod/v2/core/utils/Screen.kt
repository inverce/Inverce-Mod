package com.inverce.mod.v2.core.utils

import android.content.Context
import android.content.res.Configuration
import android.graphics.Point
import android.view.*
import com.inverce.mod.v2.core.Log
import com.inverce.mod.v2.core.activity
import com.inverce.mod.v2.core.context
import com.inverce.mod.v2.core.resources

/**
 * The Screen utilities
 */
object Screen {

    @JvmStatic
    @get:JvmName("isXLargeTablet")
    val isXLargeTablet: Boolean
        get() =
            resources.configuration.screenLayout and Configuration.SCREENLAYOUT_SIZE_MASK >= Configuration.SCREENLAYOUT_SIZE_XLARGE

    /**
     * Gets screen size.
     *
     * @return the screen size
     */
    @JvmStatic
    @get:JvmName("screenSize")
    val screenSize: Point
        get() {
            val size = Point()
            val display = (activity?.windowManager ?: context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay
            display.getSize(size)
            return size
        }

    /**
     * Gets activity size.
     *
     * @return the activity size
     */
    @JvmStatic
    @get:JvmName("activitySize")
    val activitySize: Point
        get() {
            val size = screenSize
            size.y -= statusBarHeight
            val activity = activity

            if (activity != null) {
                try {
                    val measuredHeight = activity.findViewById<View>(android.R.id.content).measuredHeight
                    if (measuredHeight > 0 && size.y != measuredHeight) {
                        size.y = measuredHeight
                    }
                } catch (ex: Exception) {
                    Log.ex(ex,"Tools", "getActivitySize")
                }
            }
            return size
        }


    /**
     * Gets status bar height.
     *
     * @return the status bar height
     */
    @JvmStatic
    @get:JvmName("statusBarHeight")
    val statusBarHeight: Int
        get() {
            var result = 0
            val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
            if (resourceId > 0) {
                result = resources.getDimensionPixelSize(resourceId)
            }
            return result
        }

    @JvmStatic
    @get:JvmName("navigationBarHeight")
    val navigationBarHeight: Int
        get() {
            val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
            return if (resourceId > 0) {
                context.resources.getDimensionPixelSize(resourceId)
            } else 0
        }

    @JvmStatic
    @get:JvmName("navigationBarVisible")
    val navigationBarVisible: Boolean
        get() {
            val hasMenuKey = ViewConfiguration.get(context).hasPermanentMenuKey()
            val hasBackKey = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK)
            return !hasMenuKey && !hasBackKey
        }
}
