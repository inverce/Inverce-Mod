package com.inverce.mod.v2.events

import java.util.*

open class Channel(private val useWeekEvents: Boolean = true) {
    protected val events = HashMap<Class<out Listener>, Event<out Listener>>()

    /**
     * Allows user to register new listener for specified event
     *
     * @param clazz    - event class that will be used as listener
     * @param listener - listener instance
     * @param <T>      event type (not used as type is implicitly specified while defining clazz
    </T> */
    fun <T : Listener> register(clazz: Class<T>, listener: T) {
        event(clazz).addListener(listener)
    }

    /**
     * Allows user to register new listener for specified event, removing old ones
     *
     * @param clazz    - event class that will be used as listener
     * @param listener - listener instance
     * @param <T>      event type (not used as type is implicitly specified while defining clazz
    </T> */
    fun <T : Listener> registerSingle(clazz: Class<T>, listener: T) {
        event(clazz).setListener(listener)
    }

    /**
     * Allows user to unregister listener for specified event
     *
     * @param clazz    - event class that will be used as listener
     * @param listener - listener instance to remove
     * @param <T>      event type (not used as type is implicitly specified while defining clazz
    </T> */
    fun <T : Listener> unregister(clazz: Class<T>, listener: T) {
        event(clazz).removeListener(listener)
    }

    /**
     * Allows user to call functions on all underlying listeners for specified event, if function returns value only value received from first listener will be returned
     *
     * @param clazz - event class that will be used as listener
     * @param <T>   event type (not used as type is implicitly specified while defining clazz
    </T> */
    fun <T : Listener> post(clazz: Class<T>): T {
        return event(clazz).post()
    }

    /**
     * Gets underlying event instance
     *
     * @param clazz - event class that will be used as listener
     * @param <T>   event type (not used as type is implicitly specified while defining clazz
    </T> */

    fun <T : Listener> event(clazz: Class<T>): Event<T> {
        return events[clazz] as? Event<T>? ?: Event(clazz, useWeekEvents).apply {
            events[clazz] = this
        }
    }

    private fun eventInternal(clazz: Class<*>): Event<*> {
        return Event(clazz as Class<Listener>, useWeekEvents).apply {
            events[clazz] = this
        }
    }
}