package com.inverce.mod.core;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.inverce.mod.core.interfaces.ActivityStateListener;
import com.inverce.mod.core.interfaces.LifecycleState;
import com.inverce.mod.core.internal.IMInternal;

/**
 * The Lifecycle utilities.
 */
public class Lifecycle {
    @NonNull
    private static LifecycleState currentLifecycleState = LifecycleState.NotCreated;
    private static ActivityStateListener listener;
    private static int activityHash;

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public static void initialize() {
        if (IMInternal.get().isInEdit()) return;
        IM.application().registerActivityLifecycleCallbacks(new StatesAdapterImpl());
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
    @NonNull
    public static LifecycleState getActivityState() {
        return currentLifecycleState;
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    synchronized static void onActivityState(@NonNull LifecycleState state, @NonNull Activity activity, Bundle extra) {
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
    }

    private static class StatesAdapterImpl implements Application.ActivityLifecycleCallbacks {
        public void onActivityCreated(@NonNull Activity activity, Bundle savedInstanceState) {
            onActivityState(LifecycleState.Created, activity, savedInstanceState);
        }

        public void onActivityStarted(@NonNull Activity activity) {
            onActivityState(LifecycleState.Started, activity, null);
        }

        public void onActivityResumed(@NonNull Activity activity) {
            onActivityState(LifecycleState.Resumed, activity, null);
        }

        public void onActivityPaused(@NonNull Activity activity) {
            onActivityState(LifecycleState.Paused, activity, null);
        }

        public void onActivityStopped(@NonNull Activity activity) {
            onActivityState(LifecycleState.Stopped, activity, null);
        }

        public void onActivitySaveInstanceState(@NonNull Activity activity, Bundle outState) {
            onActivityState(LifecycleState.SaveInstanceState, activity, outState);
        }

        public void onActivityDestroyed(@NonNull Activity activity) {
            onActivityState(LifecycleState.Destroyed, activity, null);
        }
    }
}
