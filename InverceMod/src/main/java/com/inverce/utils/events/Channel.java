package com.inverce.utils.events;

import com.inverce.utils.events.annotation.Listener;

import java.util.HashMap;

@SuppressWarnings("unchecked")
public class Channel extends HashMap<Class<? extends Listener>, Event<? extends Listener>> {
    /**
     * Allows user to register new listener for specified event
     *
     * @param clazz    - event class that will be used as listener
     * @param listener - listener instance
     * @param <T>      event type (not used as type is implicitly specified while defining clazz
     */
    public <T extends Listener> void register(Class<T> clazz, T listener) {
        event(clazz).addListener(listener);
    }

    /**
     * Allows user to register new listener for specified event, removing old ones
     *
     * @param clazz    - event class that will be used as listener
     * @param listener - listener instance
     * @param <T>      event type (not used as type is implicitly specified while defining clazz
     */
    public <T extends Listener> void registerSingle(Class<T> clazz, T listener) {
        event(clazz).setListener(listener);
    }

    /**
     * Allows user to unregister listener for specified event
     *
     * @param clazz    - event class that will be used as listener
     * @param listener - listener instance to remove
     * @param <T>      event type (not used as type is implicitly specified while defining clazz
     */
    public <T extends Listener> void unregister(Class<T> clazz, T listener) {
        event(clazz).removeListener(listener);
    }

    /**
     * Allows user to call functions on all underlying listeners for specified event, if function returns value only value received from first listener will be returned
     *
     * @param clazz - event class that will be used as listener
     * @param <T>   event type (not used as type is implicitly specified while defining clazz
     */
    public <T extends Listener> T post(Class<T> clazz) {
        return event(clazz).post();
    }

    /**
     * Gets underlying event instance
     *
     * @param clazz - event class that will be used as listener
     * @param <T>   event type (not used as type is implicitly specified while defining clazz
     */

    public <T extends Listener> Event<T> event(Class<T> clazz) {
        Event<T> event = (Event<T>) super.get(clazz);
        if (event != null) {
            return event;
        }
        put(clazz, event = new Event<>(clazz));
        return event;
    }
}
