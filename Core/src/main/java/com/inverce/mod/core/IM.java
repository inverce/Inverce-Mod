package com.inverce.mod.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;

import com.inverce.mod.core.internal.IMInternal;
import com.inverce.mod.core.threadpool.DynamicScheduledExecutor;
import com.inverce.mod.core.threadpool.UIScheduler;

/**
 * Provides easy access to context and executors.
 * Some extra goodies are also included
 */
@SuppressWarnings("WeakerAccess")
@SuppressLint("StaticFieldLeak")
public class IM {
    private static IMInternal internal = IMInternal.get();

    /**
     * Provides current context, this will be either activity or application based on whats available.
     *
     * @return the context
     */
    public static Context context() {
        if (internal.getActivity() != null) {
            return internal.getActivity();
        }
        return internal.getContext();
    }

    /**
     * Provides current application context
     *
     * @return the application context
     */
    public static Application application() {
        return (Application) context().getApplicationContext();
    }

    /**
     * Last activity, null if no activity, was present or last activity has been collected by GC.
     *
     * @return the activity
     */
    public static Activity activity() {
        return internal.getActivity();
    }

    /**
     * Last activity, null if no activity was present, last activity has been collected by GC, or activity is not support.
     *
     * @return the activity
     */
    public static FragmentActivity activitySupport() {
        Activity activity = activity();
        return activity instanceof FragmentActivity ? (FragmentActivity) activity : null;
    }

    /**
     * Resources for current context. Shortcut for IM.context)().getResources()
     *
     * @return the resources
     */
    public static Resources resources() {
        return context().getResources();
    }

    /**
     * Provides default layout inflater. Shortcut for LayoutInflater.from(IM.context());
     *
     * @return the layout inflater
     */
    public static LayoutInflater inflater() {
        return LayoutInflater.from(context());
    }

    /**
     * Shared background thread pool executor. With no maximum thread count and keep alive time set to 1 sek.
     *
     * @return the dynamic scheduled executor
     */
    public static DynamicScheduledExecutor onBg() {
        return internal.getBgExecutor();
    }

    /**
     * Shared Ui (MainThread) executor. Backed by android thread handler, and simple pool scheduler.
     *
     * @return the ui scheduler
     */
    public static UIScheduler onUi() {
        return internal.getUiExecutor();
    }

    /**
     * Enables usage of IM utilities in debug mode for Specified view,
     * does nothing in running application
     *
     * @param view the view
     */
    public static void enableInEditModeForView(@NonNull View view) {
        if (view.isInEditMode()) {
            IMInternal.get().setInEdit(true);
            IMInitializer.initialize(view.getContext());
        }
    }
}
