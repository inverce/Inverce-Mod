package com.inverce.mod.v2.core.threadpool

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

open class NamedThreadPool(name: String) : ThreadFactory {
    protected val group: ThreadGroup = System.getSecurityManager()?.threadGroup
            ?: Thread.currentThread().threadGroup
    protected val threadNumber = AtomicInteger(1)
    protected val namePrefix = "$name-${poolNumber.getAndIncrement()}-thread-"


    override fun newThread(r: Runnable) = Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0).apply {
        if (isDaemon)
            isDaemon = false
        if (priority != Thread.NORM_PRIORITY)
            priority = Thread.NORM_PRIORITY
    }


    protected companion object {
        val poolNumber = AtomicInteger(1)
    }
}
