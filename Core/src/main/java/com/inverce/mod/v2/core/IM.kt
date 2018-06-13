@file:JvmName("IM")

package com.inverce.mod.v2.core


import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.support.v4.app.FragmentActivity
import android.view.LayoutInflater
import com.inverce.mod.v2.core.configuration.SharedBoolAutoToggle
import com.inverce.mod.v2.core.threadpool.DefaultHandlerThread
import com.inverce.mod.v2.core.threadpool.DynamicScheduledExecutor
import com.inverce.mod.v2.core.threadpool.UIScheduler
import java.util.*

private val isFirstRunImp = SharedBoolAutoToggle(true, false, "im.first_run")

@get:JvmName("isFirstRun")
val isFirstRun by lazy { isFirstRunImp.getValue() }

@get:JvmName("sessionUUID")
val sessionUUID by lazy { UUID.randomUUID().toString() }

@get:JvmName("onUi")
val onUi by lazy { UIScheduler() }

fun onUi(command: Runnable) = onUi.execute(command)
fun onUi(command: () -> Unit) = onUi.execute(command)

@get:JvmName("onBg")
val onBg by lazy { DynamicScheduledExecutor() }

fun onBg(execute: Runnable) = onBg.execute(execute)
fun onBg(execute: () -> Unit) = onBg.execute(execute)

@get:JvmName("onLooper")
val onLooper by lazy {
    DefaultHandlerThread().apply {
        start()
    }
}

fun onLooper(execute: Runnable) = onLooper.execute(execute)
fun onLooper(execute: () -> Unit) = onLooper.execute(execute)

@get:JvmName("context")
val context: Context
    get() = activity!!

@get:JvmName("application")
val application: Application
    get() = context.applicationContext as Application

@get:JvmName("activity")
val activity: Activity?
    get() = null

@get:JvmName("activitySupport")
val activitySupport: FragmentActivity?
    get() = when (activity) {
        is FragmentActivity -> activity as FragmentActivity
        else -> null
    }

@get:JvmName("resources")
val resources: Resources
    get() = context.resources

@get:JvmName("inflater")
val inflater: LayoutInflater
    get() = LayoutInflater.from(context)

