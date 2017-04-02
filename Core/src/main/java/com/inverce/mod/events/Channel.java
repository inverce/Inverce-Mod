package com.inverce.mod.events;

import com.inverce.mod.core.collections.CacheFunctionMap;
import com.inverce.mod.core.reflection.Reflection;
import com.inverce.mod.events.annotation.Listener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"unchecked", "unused"})
public class Channel extends HashMap<Class<? extends Listener>, Event<? extends Listener>> {
    private static CacheFunctionMap<Class<?>, Set<Class<?>>> listenersInClass;

    static {
        listenersInClass = new CacheFunctionMap<>(Channel::getListenersInClassImpl);
    }

    public static Set<Class<?>> getListenersInClassImpl(Class<?> clazz) {
        Set<Class<?>> allInterfaces = Reflection.getImplementedInterfaces(clazz);
        Set<Class<?>> listeners = new HashSet<>();
        for (Class<?> check : allInterfaces) {
            if (Listener.class.isAssignableFrom(check) && check != Listener.class) {
                listeners.add(check);
            }
        }
        return listeners;
    }

    private final boolean useWeekEvents;

    public Channel() {
        this(true);
    }

    public Channel(boolean useWeekEvents) {
        this.useWeekEvents = useWeekEvents;
    }

    /**
     * Allows user to register all listener for specified object
     *
     * @param listener - listener instance
     */
    public <T extends Listener> void registerAll(T listener) {
        for (Class<?> clazz : listenersInClass.get(listener.getClass())) {
            eventInternal(clazz).addListenerInternal(listener);
        }
    }

    /**
     * Allows user to register all listener for specified object
     *
     * @param listener - listener instance
     */
    public <T extends Listener> void unregisterAll(T listener) {
        for (Class<?> clazz : listenersInClass.get(listener.getClass())) {
            eventInternal(clazz).removeListenerInternal(listener);
        }
    }

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
        put(clazz, event = new Event<>(clazz, useWeekEvents));
        return event;
    }

    private Event<?> eventInternal(Class<?> clazz) {
        Event<Listener> event = new Event<>((Class<Listener>) clazz, useWeekEvents);
        put((Class<? extends Listener>) clazz, event);
        return event;
    }
}
