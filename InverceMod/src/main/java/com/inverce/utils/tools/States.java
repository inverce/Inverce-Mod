package com.inverce.utils.tools;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

import com.inverce.utils.tools.impl.ActivityState;

import java.lang.ref.WeakReference;

public class States {
    private static Context context;
    private static ActivityState currentActivityState = ActivityState.NotCreated;

    public static void init(Context context) {
        States.context = context.getApplicationContext();
        ((Application) context).registerActivityLifecycleCallbacks(new StatesAdapterImpl());
    }

    public static ActivityState activityState() {
        return currentActivityState;
    }

    synchronized static void onActivityState(ActivityState state, Activity activity, Bundle extra) {
        currentActivityState = state;
        Ui.mActivity = new WeakReference<>(activity);
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
