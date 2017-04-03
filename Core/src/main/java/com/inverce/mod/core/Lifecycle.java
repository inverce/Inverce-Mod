package com.inverce.mod.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import com.inverce.mod.core.interfaces.ActivityState;
import com.inverce.mod.core.interfaces.ActivityStateListener;
import com.inverce.mod.core.internal.IMInternal;
import com.inverce.mod.events.Event;

public class Lifecycle {
    private static ActivityState currentActivityState = ActivityState.NotCreated;
    private static ActivityStateListener listener;
    private static boolean postEvents;

    static void initialize() {
        IM.application().registerActivityLifecycleCallbacks(new StatesAdapterImpl());
    }

    public static void setPostEvents(boolean postEvents) {
        Lifecycle.postEvents = postEvents;
    }

    public static void setListener(ActivityStateListener listener) {
        Lifecycle.listener = listener;
    }

    public static ActivityState getActivityState() {
        return currentActivityState;
    }

    synchronized static void onActivityState(ActivityState state, Activity activity, Bundle extra) {
        currentActivityState = state;

        if (state.ordinal() < 4) {
            IMInternal.get().setActivity(activity);
        }

        if (listener != null) {
            listener.activityStateChanged(state, activity, extra);
        }

        if (postEvents) {
            Event.Bus.post(ActivityStateListener.class)
                    .activityStateChanged(state, activity, extra);
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
