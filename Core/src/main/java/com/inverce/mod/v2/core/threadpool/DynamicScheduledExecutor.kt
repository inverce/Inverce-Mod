package com.inverce.mod.v2.core.threadpool

import java.util.concurrent.*

class DynamicScheduledExecutor : ThreadPoolExecutor(0, Integer.MAX_VALUE, 1, TimeUnit.SECONDS, SynchronousQueue()), ScheduledExecutorService {
    protected companion object {
        @set:Synchronized
        @get:Synchronized
        private var count = 0
    }
    protected val scheduler: ScheduledThreadPoolExecutor by lazy {
        ScheduledThreadPoolExecutor(1, NamedThreadPool("Dynamic-Scheduler-${count++}")).apply {
            setKeepAliveTime(1100, TimeUnit.MILLISECONDS)
            allowCoreThreadTimeOut(true)
        }
    }

    init {
        threadFactory = NamedThreadPool("Dynamic")
    }

    override fun setKeepAliveTime(time: Long, unit: TimeUnit) {
        super.setKeepAliveTime(time, unit)
        scheduler.setKeepAliveTime(time, unit)
    }

    override fun schedule(command: Runnable, delay: Long, unit: TimeUnit): ScheduledFuture<*> {
        return scheduler.schedule(wrap(command), delay, unit)
    }

    override fun <V> schedule(callable: Callable<V>, delay: Long, unit: TimeUnit): ScheduledFuture<V> {
        return scheduler.schedule(callable, delay, unit)
    }

    override fun scheduleAtFixedRate(command: Runnable, initialDelay: Long, period: Long, unit: TimeUnit): ScheduledFuture<*> {
        return scheduler.scheduleAtFixedRate(wrap(command), initialDelay, period, unit)
    }

    override fun scheduleWithFixedDelay(command: Runnable, initialDelay: Long, delay: Long, unit: TimeUnit): ScheduledFuture<*> {
        return scheduler.scheduleWithFixedDelay(wrap(command), initialDelay, delay, unit)
    }

    private fun wrap(runnable: Runnable): Runnable {
        return Runnable { execute(runnable) }
    }
}