package com.inverce.mod.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.RestrictTo;

import com.inverce.mod.core.interfaces.LifecycleState;
import com.inverce.mod.core.interfaces.ActivityStateListener;
import com.inverce.mod.core.internal.IMInternal;
import com.inverce.mod.events.Event;

/**
 * The Lifecycle utilities.
 */
public class Lifecycle {
    private static LifecycleState currentLifecycleState = LifecycleState.NotCreated;
    private static ActivityStateListener listener;
    private static boolean postEvents;
    private static int activityHash;

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    static void initialize() {
        if (IMInternal.get().isInEdit()) return;
        IM.application().registerActivityLifecycleCallbacks(new StatesAdapterImpl());
    }

    /**
     * Sets whatever this class should post ActivityStateListener event, whenever activity state changes.
     *
     * @param postEvents the post events
     */
    public static void setShouldPostEvents(boolean postEvents) {
        Lifecycle.postEvents = postEvents;
    }

    /**
     * Sets ActivityState listener.
     *
     * @param listener the listener
     */
    public static void setListener(ActivityStateListener listener) {
        Lifecycle.listener = listener;
    }

    /**
     * Gets activity state.
     *
     * @return the activity state
     */
    public static LifecycleState getActivityState() {
        return currentLifecycleState;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    synchronized static void onActivityState(LifecycleState state, Activity activity, Bundle extra) {
        if (state.ordinal() > 3 && activity.hashCode() != activityHash) {
            // don't post activity state change for previous activity
            return;
        }

        currentLifecycleState = state;

        if (state.ordinal() < 4) {
            activityHash = activity.hashCode();
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
            onActivityState(LifecycleState.Created, activity, savedInstanceState);
        }

        public void onActivityStarted(Activity activity) {
            onActivityState(LifecycleState.Started, activity, null);
        }

        public void onActivityResumed(Activity activity) {
            onActivityState(LifecycleState.Resumed, activity, null);
        }

        public void onActivityPaused(Activity activity) {
            onActivityState(LifecycleState.Paused, activity, null);
        }

        public void onActivityStopped(Activity activity) {
            onActivityState(LifecycleState.Stopped, activity, null);
        }

        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
            onActivityState(LifecycleState.SaveInstanceState, activity, outState);
        }

        public void onActivityDestroyed(Activity activity) {
            onActivityState(LifecycleState.Destroyed, activity, null);
        }
    }
}
