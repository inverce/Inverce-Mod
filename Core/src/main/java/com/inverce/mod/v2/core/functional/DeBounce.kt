package com.inverce.mod.v2.core.functional

import com.inverce.mod.v2.core.functional.BounceMethod.*
import com.inverce.mod.v2.core.functional.BounceThread.*
import com.inverce.mod.v2.core.onBg
import com.inverce.mod.v2.core.onUi
import java.util.concurrent.ScheduledFuture
import kotlin.jvm.internal.Intrinsics
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class DeBounce<T>(
        val delayMs: Long = 500L,
        val bounceMethod: BounceMethod = BLOCK,
        val thread: BounceThread = CALLING,
        val equalityCheck: (T, T) -> Boolean = Intrinsics::areEqual,
        val collector: (T) -> Unit) : ReadOnlyProperty<Any, (T) -> Unit> {

    protected val now get() = System.currentTimeMillis()
    protected var lastTs = 0L
    protected var feature: ScheduledFuture<*>? = null
    protected var current: T? = null

    private val timedMethods = arrayOf(FIRST, LAST)

    init {
        if (thread == CALLING && bounceMethod in timedMethods)
            throw IllegalStateException("Calling thread not supported for selected bounce method")
    }


    override fun getValue(thisRef: Any, property: KProperty<*>): (T) -> Unit = ::post

    fun post(value: T) {
        val element = current
        when {
            bounceMethod == BLOCK && (lastTs + delayMs) > now -> postNowInternal(value = value)
            bounceMethod == BLOCK -> { /* do nothing */ }
            bounceMethod == CHANGE && element != null && equalityCheck(element, value) -> postNowInternal(value = value)
            bounceMethod == CHANGE -> { /* do nothing */ }
            bounceMethod in timedMethods && feature == null -> {
                current = value
                feature = onBg(delayMs) { postNow(value); feature = null }
            }
            bounceMethod == LAST -> current = value
            bounceMethod == FIRST -> { /* do nothing */ }
        }
    }

    protected fun postNowInternal(value: T) {
        when (thread) {
            UI -> onUi { collector(value) }
            BG -> onBg { collector(value) }
            CALLING -> { collector(value) }
        }
        lastTs = now
    }

    /**
     * This will omit any bounce method and call collector method in correct thread
     */
    fun postNow(value: T) = postNowInternal(value = value)

    fun clearState() {
        current = null
        feature?.let {
            if (!it.isDone && !it.isCancelled) {
                it.cancel(false)
            }
        }
        feature = null
    }
}

enum class BounceMethod {
    /**
     * Collect will be called when value changes
     */
    CHANGE,
    /**
     * Collect will be called only if it was not called for at least delayMs
     */
    BLOCK,
    /**
     * Collect will be called after delay, with first element received
     */
    FIRST,
    /**
     * Collect will be called after delay, with last element received
     */
    LAST,
}

enum class BounceThread {
    UI, BG, CALLING
}