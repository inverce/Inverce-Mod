package com.inverce.utils.events;

import com.inverce.utils.events.annotations.CallOnThread;
import com.inverce.utils.events.interfaces.EventCaller;
import com.inverce.utils.events.interfaces.EventMulti;
import com.inverce.utils.events.interfaces.EventSingleton;
import com.inverce.utils.events.interfaces.Listener;
import com.inverce.utils.logging.Log;
import com.inverce.utils.threads.VAExecutor;
import com.inverce.utils.tools.Ui;

import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.ArrayList;

import static java.lang.reflect.Proxy.newProxyInstance;

@SuppressWarnings("unused")
public class Event <T extends Listener> implements EventSingleton<T>, EventMulti<T>, EventCaller<T> {
    private Class<T> service;

    // weak referenced used to clean_up listeners even if user forgets to, or didn't had time
    private final ArrayList<WeakReference<T>> list, listToClean;

    private final T proxyCaller;
    private boolean needCleanUp;
    private final boolean parseAnnotation;

    public Event(Class<T> clazz) {
        this(clazz, false);
    }

    @SuppressWarnings("unchecked")
    public Event(Class<T> clazz, boolean parseAnnotation) {
        this.parseAnnotation = parseAnnotation;
        this.service = clazz;
        list = new ArrayList<>(1);
        listToClean = new ArrayList<>(1);
        // NOTE: we use method.invoke on proxy class, this has small overhead on method invoke (not execution),  around .057 ms instead of .042 on 2.33 ghz processor (around 30 %)
        proxyCaller = (T) newProxyInstance(service.getClassLoader(), new Class<?>[]{service}, createHandler());
    }

    private void cleanUp(T listener) {
        for (WeakReference<T> w: list) {
            if (w.get() == null || w.get() == listener) {
                listToClean.add(w);
            }
        }
        list.removeAll(listToClean);
        if (needCleanUp) {
            Log.e(service.getSimpleName(), "Cleaned up references " + listToClean.size());
        }
        listToClean.clear();
        needCleanUp = false;
    }

    /**
     * Set single listener.
     * @param listener listener
     */
    public void setListener(T listener) {
        synchronized (list) {
            list.clear();
            if (listener != null) {
                list.add(new WeakReference<>(listener));
            }
        }
    }

    public void addListener(T listener) {
        if (listener != null) {
            synchronized (list) {
                list.add(new WeakReference<>(listener));
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

    public T getCaller() {
        synchronized (list) {
            return proxyCaller;
        }
    }

    public int getCount() {
        synchronized (list) {
            return list.size();
        }
    }

    public InvocationHandler createHandler() {
        return new InvocationHandler() {
            public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                if (parseAnnotation) {
                    CallOnThread onThread = method.getAnnotation(CallOnThread.class);
                    if (onThread != null) {
                        switch (onThread.value()) {
                            case BgThread:
                                VAExecutor.get().execute(new Runnable() {
                                    public void run() {
                                        try {
                                            invokeInternal(proxy, method, args);
                                        } catch (Throwable throwable) {
                                            throwable.printStackTrace();
                                        }
                                    }
                                });
                                return null;
                            case UiThread:
                                Ui.runOnUI(new Runnable() {
                                    public void run() {
                                        try {
                                            invokeInternal(proxy, method, args);
                                        } catch (Throwable throwable) {
                                            throwable.printStackTrace();
                                        }
                                    }
                                });
                                return null;
                        }
                    }
                }

                return invokeInternal(proxy, method, args);
            }
        };
    }

    private Object invokeInternal(final Object proxy, final Method method, final Object[] args) throws Throwable {
        synchronized (list) {
            Object[] returns = new Object[list.size()];
            for (int i = 0; i < list.size(); i++) {
                T listener = list.get(i).get();
                if (listener != null) {
                    returns[i] = method.invoke(listener, args);
                } else {
                    needCleanUp = true;
                }
            }

            if (needCleanUp) {
                cleanUp(null);
            }
            return list.size() > 0 ? returns[0] : null;
        }
    }
}