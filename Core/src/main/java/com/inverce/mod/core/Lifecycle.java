package com.inverce.mod.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.inverce.mod.core.interfaces.ActivityState;

import java.lang.ref.WeakReference;

public class Lifecycle {
    private static ActivityState currentActivityState = ActivityState.NotCreated;

    public static void init(Application context) {
        context.registerActivityLifecycleCallbacks(new StatesAdapterImpl());
    }

    public static ActivityState activityState() {
        return currentActivityState;
    }

    synchronized static void onActivityState(ActivityState state, Activity activity, Bundle extra) {
        currentActivityState = state;

        if (state.ordinal() < 4) {
            Ui.mActivity = new WeakReference<>(activity);
        }
    }

    private static class StatesAdapterImpl implements Application.ActivityLifecycleCallbacks {
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            onActivityState(ActivityState.Created, activity, savedInstanceState);
        }

        public void onActivityStarted(Activity activity) {
            onActivityState(ActivityState.Started, activity, null);
        }

        public void onActivityResumed(Activity activity) {
            onActivityState(ActivityState.Resumed, activity, null);
        }

        public void onActivityPaused(Activity activity) {
            onActivityState(ActivityState.Paused, activity, null);
        }

        public void onActivityStopped(Activity activity) {
            onActivityState(ActivityState.Stopped, activity, null);
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            onActivityState(ActivityState.SaveInstanceState, activity, outState);
        }

        public void onActivityDestroyed(Activity activity) {
            onActivityState(ActivityState.Destroyed, activity, null);
        }
    }
}
