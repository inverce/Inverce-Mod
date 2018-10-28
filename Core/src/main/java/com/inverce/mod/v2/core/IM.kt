@file:JvmName("IM")

package com.inverce.mod.v2.core


import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import android.view.View
import com.inverce.mod.v2.core.configuration.SharedBoolAutoToggle
import com.inverce.mod.v2.core.internal.IMInternal
import com.inverce.mod.v2.core.threadpool.DefaultHandlerThread
import com.inverce.mod.v2.core.threadpool.DynamicScheduledExecutor
import com.inverce.mod.v2.core.threadpool.UIScheduler
import com.inverce.mod.v2.core.utils.enableInEditModeForView
import java.util.*
import java.util.concurrent.TimeUnit

object IMEx {
    private val isFirstRunImp = SharedBoolAutoToggle(true, false, "im.first_run")

    @get:JvmName("isFirstRun")
    val isFirstRun by lazy { isFirstRunImp.value }

    @get:JvmName("sessionUUID")
    val sessionUUID by lazy { UUID.randomUUID().toString() }

    @get:JvmName("context")
    val context: Context
        get() = IMInternal.activity.get() ?: IMInternal.context ?: throw IllegalStateException("Inverce's mod not initialized")

    @get:JvmName("activity")
    val activity: Activity?
        get() = IMInternal.activity.get()
}

@JvmSynthetic
fun enableInEditModeForView(view: View) {
    view.enableInEditModeForView()
}

@get:JvmName("onUi")
val onUi by lazy { UIScheduler() }

@get:JvmName("onBg")
val onBg by lazy { DynamicScheduledExecutor() }

fun onUi(exec: () -> Unit) = onUi.execute(exec)
fun onBg(exec: () -> Unit) = onBg.execute(exec)

fun onUi(delay: Long, unit: TimeUnit = TimeUnit.MILLISECONDS, exec: () -> Unit) = onUi.schedule(exec, delay, unit)
fun onBg(delay: Long, unit: TimeUnit = TimeUnit.MILLISECONDS, exec: () -> Unit) = onBg.schedule(exec, delay, unit)

@get:JvmName("onLooper")
val onLooper by lazy {
    DefaultHandlerThread().apply {
        start()
    }
}

fun onLooper(execute: () -> Unit) = onLooper.execute(execute)

@get:JvmName("context")
val context: Context
    get() = IMInternal.activity.get() ?: IMInternal.context ?: throw IllegalStateException("Inverce's mod not initialized")

@get:JvmName("application")
val application: Application
    get() = context.applicationContext as Application

@get:JvmName("activity")
val activity: Activity? // CHECK IF WORKS FINE
    get() = IMInternal.activity.get()

@get:JvmName("activitySupport")
val activitySupport: FragmentActivity?
    get() = activity as? FragmentActivity

@get:JvmName("resources")
val resources: Resources
    get() = context.resources

@get:JvmName("inflater") // FIXME (AKA CHECK IF WORKS FINE
val inflater: LayoutInflater
    get() = LayoutInflater.from(context)


/**
 * Utility class to initialize Inverce's Mod, for cases when automatic initialization does not apply.
 */
object IMInitializer {
    /**
     * Initialize Inverce's Mod
     *
     * @param context the context
     */
    @JvmStatic
    fun initialize(context: Context) {
        IMInternal.context = context
        Log.w("ds", "ds")
    }
}