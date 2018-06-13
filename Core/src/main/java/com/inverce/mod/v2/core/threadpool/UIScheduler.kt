package com.inverce.mod.v2.core.threadpool

import android.os.Handler
import android.os.Looper
import java.util.concurrent.*

open class UIScheduler : Executor {
    protected companion object {
        @set:Synchronized
        @get:Synchronized
        private var count = 0
    }

    protected val uiHandler: Handler = Handler(Looper.getMainLooper())
    protected val scheduler: ScheduledThreadPoolExecutor by lazy {
        ScheduledThreadPoolExecutor(1, NamedThreadPool("UI-Scheduler-${count++}")).apply {
            setKeepAliveTime(2, TimeUnit.SECONDS)
            allowCoreThreadTimeOut(true)
        }
    }

    fun schedule(command: Runnable, delay: Long, unit: TimeUnit): ScheduledFuture<*> {
        return scheduler.schedule(wrap(command), delay, unit)
    }

    fun <V> schedule(callable: Callable<V>, delay: Long, unit: TimeUnit): ScheduledFuture<V> {
        return scheduler.schedule(callable, delay, unit)
    }

    fun scheduleAtFixedRate(command: Runnable, initialDelay: Long, period: Long, unit: TimeUnit): ScheduledFuture<*> {
        return scheduler.scheduleAtFixedRate(wrap(command), initialDelay, period, unit)
    }

    fun scheduleWithFixedDelay(command: Runnable, initialDelay: Long, delay: Long, unit: TimeUnit): ScheduledFuture<*> {
        return scheduler.scheduleWithFixedDelay(wrap(command), initialDelay, delay, unit)
    }

    private fun wrap(runnable: Runnable): Runnable {
        return Runnable { execute(runnable) }
    }

    override fun execute(command: Runnable) {
        uiHandler.post(command)
    }
}