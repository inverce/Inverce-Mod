package com.inverce.mod.v2.core.functional

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.support.annotation.RestrictTo
import com.inverce.mod.v2.core.application
import com.inverce.mod.v2.core.internal.IMInternal
import java.lang.ref.WeakReference

/**
 * The Lifecycle utilities.
 */
object Lifecycle {
    /**
     * Gets activity state.
     *
     * @return the activity state
     */
    var activityState = LifecycleState.NotCreated
        private set

    private var listener: ActivityStateListener? = null
    private var activityHash: Int = 0

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    fun initialize() {
        if (IMInternal.isInEdit) return
        application.registerActivityLifecycleCallbacks(StatesAdapterImpl())
    }

    /**
     * Sets ActivityState listener.
     *
     * @param listener the listener
     */
    fun setListener(listener: ActivityStateListener) {
        Lifecycle.listener = listener
    }

    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    @Synchronized
    internal fun onActivityState(state: LifecycleState, activity: Activity, extra: Bundle?) {
        if (state.ordinal > 3 && activity.hashCode() != activityHash) {
            // don't post activity state change for previous activity
            return
        }

        activityState = state

        if (state.ordinal < 4) {
            activityHash = activity.hashCode()
            IMInternal.activity = WeakReference(activity)
        }


        listener?.activityStateChanged(state, activity, extra)
    }


}

class StatesAdapterImpl : Application.ActivityLifecycleCallbacks {
    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle) {
        Lifecycle.onActivityState(LifecycleState.Created, activity, savedInstanceState)
    }

    override fun onActivityStarted(activity: Activity) {
        Lifecycle.onActivityState(LifecycleState.Started, activity, null)
    }

    override fun onActivityResumed(activity: Activity) {
        Lifecycle.onActivityState(LifecycleState.Resumed, activity, null)
    }

    override fun onActivityPaused(activity: Activity) {
        Lifecycle.onActivityState(LifecycleState.Paused, activity, null)
    }

    override fun onActivityStopped(activity: Activity) {
        Lifecycle.onActivityState(LifecycleState.Stopped, activity, null)
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {
        Lifecycle.onActivityState(LifecycleState.SaveInstanceState, activity, outState)
    }

    override fun onActivityDestroyed(activity: Activity) {
        Lifecycle.onActivityState(LifecycleState.Destroyed, activity, null)
    }
}

enum class LifecycleState {
    NotCreated, Created, Started, Resumed, Paused, Stopped, SaveInstanceState, Destroyed
}

interface ActivityStateListener {
    fun activityStateChanged(state: LifecycleState, activity: Activity, extra: Bundle?)
}