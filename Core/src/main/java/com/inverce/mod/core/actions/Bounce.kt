package com.inverce.mod.core.actions

import com.inverce.mod.core.IM
import com.inverce.mod.v2.core.Log
import com.inverce.mod.v2.core.functional.Aggregator
import com.inverce.mod.v2.core.functional.IConsumer
import java.util.*
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

class CollectBounce<T> {
    protected var feature: ScheduledFuture<*>? = null
    protected var report: IConsumer<List<T>>
    protected var delay: Long = 500
    protected var events: MutableList<T>? = null
    protected var emitOnDelay: Boolean = false

    init {
        events = Collections.synchronizedList(ArrayList())
    }

    fun setConsumer(report: IConsumer<List<T>>): CollectBounce<T> {
        this.report = report
        return this
    }

    fun setDelay(delay: Long): CollectBounce<T> {
        this.delay = delay
        return this
    }

    fun emitAfterDelay(): CollectBounce<T> {
        this.emitOnDelay = true
        return this
    }

    fun emitWhenNoNewEvent(): CollectBounce<T> {
        this.emitOnDelay = false
        return this
    }

    private fun defaultEquals(a: T?, b: T): Boolean {
        return a === b || a != null && a == b
    }

    @Synchronized
    fun post(element: T) {
        events!!.add(element)

        if (!emitOnDelay) {
            cancel()
        }

        if (!emitOnDelay || feature == null) {
            scheduleTask()
        }
    }

    @Synchronized
    private fun internalReport() {
        if (events != null) {
            report.accept(events)
            events!!.clear()
            Log.w("reporting event")
            feature = null
        }
    }

    fun cancel() {
        if (feature != null) {
            feature!!.cancel(false)
            feature = null
        }
    }

    private fun scheduleTask() {
        cancel()
        feature = IM.onBg().schedule({ this.internalReport() }, delay, TimeUnit.MILLISECONDS)
    }

    companion object {

        fun <T> aggregateUseNew(): Aggregator<T> {
            return { A, B -> B }
        }

        fun <T> aggregateUseOld(): Aggregator<T> {
            return { A, B -> B }
        }
    }
}
