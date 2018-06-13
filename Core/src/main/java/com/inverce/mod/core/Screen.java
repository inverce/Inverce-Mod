package com.inverce.mod.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.inverce.mod.v2.core.Log;

import static com.inverce.mod.core.IM.context;

/**
 * The Screen utilities
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Screen {
    public static final int MATCH_PARENT = ViewGroup.LayoutParams.MATCH_PARENT;
    public static final int WRAP_CONTENT = ViewGroup.LayoutParams.WRAP_CONTENT;

    public static boolean isXLargeTablet() {
        return (IM.resources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    /**
     * Gets screen size.
     *
     * @return the screen size
     */
    @NonNull
    public static Point getScreenSize() {
        Point size = new Point();
        Display display;
        if (context() instanceof Activity) {
            display = ((Activity) context()).getWindowManager().getDefaultDisplay();
        } else {
            WindowManager wm = (WindowManager) context().getSystemService(Context.WINDOW_SERVICE);
            display = wm.getDefaultDisplay();
        }
        display.getSize(size);
        return size;
    }

    /**
     * Gets activity size.
     *
     * @return the activity size
     */
    @NonNull
    public static Point getActivitySize() {
        Point size = getScreenSize();
        size.y -= getStatusBarHeight();
        Activity activity = IM.activity();

        if (activity != null) {
            try {
                int measuredHeight = activity.findViewById(android.R.id.content).getMeasuredHeight();
                if (measuredHeight > 0 && size.y != measuredHeight) {
                    size.y = measuredHeight;
                }
            } catch (Exception ex) {
                Log.exs("Tools", "getActivitySize", ex);
            }
        }
        return size;
    }

    /**
     * Converts size in pixels into estimate in dp
     *
     * @param px the px
     * @return the int
     */
    public static int pxToDp(float px) {
        return (int) (px / IM.resources().getDisplayMetrics().density);
    }

    /**
     * Converts size in dp into estimate in pixels
     *
     * @param dp the dp
     * @return the int
     */
    public static int dpToPx(float dp) {
        return (int) (dp * IM.resources().getDisplayMetrics().density);
    }

    /**
     * Gets location on screen for specific view
     *
     * @param measurementView the measurement view
     * @return the location on screen
     */
    @NonNull
    public static Point getLocationOnScreen(@Nullable View measurementView) {
        int[] areaBegin = new int[2];
        if (measurementView == null)
            return new Point(-1, -1);
        measurementView.getLocationOnScreen(areaBegin);
        return new Point(areaBegin[0], areaBegin[1]);
    }

    /**
     * Gets view size.
     *
     * @param measurementView the measurement view
     * @return the view size
     */
    @NonNull
    public static Point getViewSize(@Nullable View measurementView) {
        if (measurementView == null)
            return new Point(-1, -1);
        return new Point(measurementView.getMeasuredWidth(), measurementView.getMeasuredHeight());
    }

    /**
     * Gets status bar height.
     *
     * @return the status bar height
     */
    public static int getStatusBarHeight() {
        int result = 0;
        int resourceId = IM.resources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = IM.resources().getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
