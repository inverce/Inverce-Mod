package com.inverce.mod.events;

import com.inverce.mod.core.Log;
import com.inverce.mod.core.collections.WeakArrayList;
import com.inverce.mod.events.annotation.EventInfo;
import com.inverce.mod.events.annotation.Listener;
import com.inverce.mod.events.interfaces.EventCaller;
import com.inverce.mod.events.interfaces.MultiEvent;
import com.inverce.mod.events.interfaces.SingleEvent;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static java.lang.reflect.Proxy.newProxyInstance;

@SuppressWarnings("unused")
public class Event<T extends Listener> implements SingleEvent<T>, MultiEvent<T>, EventCaller<T>, InvocationHandler {
    private Class<T> service;

    // weak referenced used to clean_up listeners even if user forgets to, or didn't had time
    private final List<T> list;

    private final T proxyCaller;
    private boolean needCleanUp;
    private static Executor uiExecutor, bgExecutor;

    static {
        uiExecutor = new DefaultUiExecutor();
        bgExecutor = new ThreadPoolExecutor(0, Integer.MAX_VALUE,
                3L, TimeUnit.SECONDS,
                new SynchronousQueue<>());
    }

    public Event(Class<T> clazz) {
        this(clazz, true);
    }

    @SuppressWarnings("unchecked")
    public Event(Class<T> clazz, boolean useWeakReferences) {
        this.service = clazz;
        if (useWeakReferences) {
            list = new WeakArrayList<>();
        } else {
            list = new ArrayList<>(1);
        }

        // NOTE: we use method.invoke on proxy class, this has small overhead on method invoke (not execution),
        // around .057 ms instead of .042 on 2.33 ghz processor (around 30 %)
        proxyCaller = (T) newProxyInstance(service.getClassLoader(), new Class<?>[]{service}, this);
    }

    private void cleanUp(T listener) {
        int cleared = 0;
        if (listener != null) {
            cleared += list.remove(listener) ? 1 : 0;
        }

        if (list instanceof WeakArrayList) {
            cleared += ((WeakArrayList) list).clearEmptyReferences();
        }

        if (needCleanUp) {
            Log.e(service.getSimpleName(), "Cleaned up references " + cleared);
        }
        needCleanUp = false;
    }

    void addListenerInternal(Object listener) {
        if (service.isInstance(listener)) {
            //noinspection unchecked // i just checked u know ^^
            addListener((T) listener);
        }
    }

    void removeListenerInternal(Object listener) {
        if (service.isInstance(listener)) {
            //noinspection unchecked // i just checked u know ^^
            removeListener((T) listener);
        }
    }

    /**
     * Set single listener.
     *
     * @param listener listener
     */
    public void setListener(T listener) {
        synchronized (list) {
            list.clear();
            if (listener != null) {
                list.add(listener);
            }
        }
    }

    public void addListener(T listener) {
        if (listener != null) {
            synchronized (list) {
                if (!list.contains(listener)) {
                    list.add(listener);
                }
            }
        }
    }

    public void removeListener(T listener) {
        synchronized (list) {
            cleanUp(listener);
        }
    }

    public void clear() {
        synchronized (list) {
            list.clear();
        }
    }

    public T post() {
        synchronized (list) {
            return proxyCaller;
        }
    }

    public int getCount() {
        synchronized (list) {
            return list.size();
        }
    }

    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        EventInfo onThread = method.getAnnotation(EventInfo.class);

//        AsyncResult<T> result = null;
//        if (method.getReturnType() == AsyncResult.class) {
//            result = new AsyncResult<>(new ArrayList<T>());
//        }

        if (onThread != null) {
            switch (onThread.thread()) {
                case BgThread:
                    bgExecutor.execute(createInvokerRunnable(null, method, args));
                    return null;
                case UiThread:
                    uiExecutor.execute(createInvokerRunnable(null, method, args));
                    return null;
            }
        }

        return invokeInternal(null, method, args);
    }

    private Runnable createInvokerRunnable(final AsyncResult<T> proxy, final Method method, final Object[] args) {
        //noinspection Convert2Lambda
        return new Runnable() {
            public void run() {
                try {
                    invokeInternal(proxy, method, args);
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }
        };
    }

    Object invokeInternal(AsyncResult<T> result, final Method method, final Object[] args) throws Throwable {
        synchronized (list) {
            Object[] returns = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
                T listener = list.get(i);
                if (listener != null) {
                    returns[i] = method.invoke(listener, args);
                } else {
                    needCleanUp = true;
                }
            }

            if (needCleanUp) {
                cleanUp(null);
            }

//            if (result != null) {
//                for (Object r: returns) {
//                    //noinspection unchecked
//                    result.store.addAll((AsyncResult<T>) r);
//                }
//                return result;
//            }

            return list.size() > 0 ? returns[0] : null;
        }
    }

    /**
     * Event bus is "simple" class used to manage registering and calling application global events <BR/>
     * It must be noted that this class saves every registered listener in weak reference instance, thus listeners might be removed without explicit warning or unregister call <BR/>
     * Bus has small overhead over managing events globally and MANUALLY, its not recommended to use Bus as way to replace standard listeners <BR/>
     * Overhead is around 20-30% in function call per listener (not function total execution time, but time it takes to start method), thus is strongly advised not to use Bus in events that are repeated multiple times per second (onDraw | mouseMove.)
     */
    @SuppressWarnings("unchecked")
    public static class Bus {
        static Channel defaultChannel;
        static ChannelGroup channels;
        static long tsLastRegister = 0, tsLastNotify = 0, tsLastCount = 0;
        static long hashLastRegister = 0, tsTimeNotify = 2000;

        private static void handleRegisterHint(Object listener) {
            if (Log.isLoggable(Log.INFO) && listener != null) {
                int hash = listener.hashCode();
                if (hashLastRegister != hash) {
                    tsLastRegister = 0;
                    hashLastRegister = hash;
                    tsLastCount = 0;
                    return;
                } else {
                    tsLastCount++;
                }

                long ts = System.currentTimeMillis();
                if ((ts - tsLastRegister) < 15 && (ts - tsLastNotify) > tsTimeNotify && tsLastCount > 2) {
                    Log.i("Consider using Bus.registerAll for registering multiple event on same object.");
                    tsLastNotify = ts;
                }

                tsLastRegister = ts;
                hashLastRegister = hash;
            }
        }

        /**
         * Allows user to register all listener for specified object
         *
         * @param listener - listener instance
         */
        public static <T extends Listener> void registerAll(T listener) {
            channel().registerAll(listener);
        }

        /**
         * Allows user to register all listener for specified object
         *
         * @param listener - listener instance
         */
        public static <T extends Listener> void unregisterAll(T listener) {
            channel().unregisterAll(listener);
        }

        /**
         * Allows user to register new listener for specified event
         *
         * @param clazz    - event class that will be used as listener
         * @param listener - listener instance
         * @param <T>      event type (not used as type is implicitly specified while defining clazz
         */
        public static <T extends Listener> void register(Class<T> clazz, T listener) {
            handleRegisterHint(listener);
            channel().event(clazz).addListener(listener);
        }

        /**
         * Allows user to register new listener for specified event, removing old ones
         *
         * @param clazz    - event class that will be used as listener
         * @param listener - listener instance
         * @param <T>      event type (not used as type is implicitly specified while defining clazz
         */
        public static <T extends Listener> void registerSingle(Class<T> clazz, T listener) {
            channel().event(clazz).setListener(listener);
        }

        /**
         * Allows user to unregister listener for specified event
         *
         * @param clazz    - event class that will be used as listener
         * @param listener - listener instance to remove
         * @param <T>      event type (not used as type is implicitly specified while defining clazz
         */
        public static <T extends Listener> void unregister(Class<T> clazz, T listener) {
            channel().event(clazz).removeListener(listener);
        }

        /**
         * Allows user to call functions on all underlying listeners for specified event, if function returns value only value received from first listener will be returned
         *
         * @param clazz - event class that will be used as listener
         * @param <T>   event type (not used as type is implicitly specified while defining clazz
         */
        public static <T extends Listener> T post(Class<T> clazz) {
            return channel().event(clazz).post();
        }

        /**
         * Gets underlying event instance
         *
         * @param clazz - event class that will be used as listener
         * @param <T>   event type (not used as type is implicitly specified while defining clazz
         */
        public synchronized static <T extends Listener> Event<T> event(Class<T> clazz) {
            return channel().event(clazz);
        }

        public static Channel channel(int channelId) {
            if (channels == null) {
                channels = new ChannelGroup(true);
            }

            return channels.on(channelId);
        }

        private static Channel channel() {
            if (defaultChannel == null) {
                defaultChannel = new Channel(true);
            }
            return defaultChannel;
        }
    }
}