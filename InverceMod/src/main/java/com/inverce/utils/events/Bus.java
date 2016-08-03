package com.inverce.utils.events;

import com.inverce.utils.events.annotation.Listener;

/**
 * Event bus is "simple" class used to manage registering and calling application global events <BR/>
 * It must be noted that this class saves every registered listener in weak reference instance, thus listeners might be removed without explicit warning or unregister call <BR/>
 * Bus has small overhead over managing events globally and MANUALLY, its not recommended to use Bus as way to replace standard listeners <BR/>
 * Overhead is around 20-30% in function call per listener (not function total execution time, but time it takes to start method), thus is strongly advised not to use Bus in events that are repeated multiple times per second (onDraw | mouseMove.)
 */
@SuppressWarnings("unchecked")
public class Bus {
    static Channel defaultChannel;
    static ChannelGroup channels;

    /**
     * Allows user to register new listener for specified event
     *
     * @param clazz    - event class that will be used as listener
     * @param listener - listener instance
     * @param <T>      event type (not used as type is implicitly specified while defining clazz
     */
    public static <T extends Listener> void register(Class<T> clazz, T listener) {
        event(clazz).addListener(listener);
    }

    /**
     * Allows user to register new listener for specified event, removing old ones
     *
     * @param clazz    - event class that will be used as listener
     * @param listener - listener instance
     * @param <T>      event type (not used as type is implicitly specified while defining clazz
     */
    public static <T extends Listener> void registerSingle(Class<T> clazz, T listener) {
        event(clazz).setListener(listener);
    }

    /**
     * Allows user to unregister listener for specified event
     *
     * @param clazz    - event class that will be used as listener
     * @param listener - listener instance to remove
     * @param <T>      event type (not used as type is implicitly specified while defining clazz
     */
    public static <T extends Listener> void unregister(Class<T> clazz, T listener) {
        event(clazz).removeListener(listener);
    }

    /**
     * Allows user to call functions on all underlying listeners for specified event, if function returns value only value received from first listener will be returned
     *
     * @param clazz - event class that will be used as listener
     * @param <T>   event type (not used as type is implicitly specified while defining clazz
     */
    public static <T extends Listener> T post(Class<T> clazz) {
        return event(clazz).post();
    }

    /**
     * Gets underlying event instance
     *
     * @param clazz - event class that will be used as listener
     * @param <T>   event type (not used as type is implicitly specified while defining clazz
     */
    public synchronized static <T extends Listener> Event<T> event(Class<T> clazz) {
        if (defaultChannel == null) {
            defaultChannel = new Channel();
        }
        return defaultChannel.event(clazz);
    }

    public static Channel channel(int channelId) {
        if (channels == null) {
            channels = new ChannelGroup();
        }

        return channels.on(channelId);
    }
}
