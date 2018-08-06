package com.inverce.mod.v2.events

import com.inverce.mod.v2.core.Logger
import com.inverce.mod.v2.core.collection.WeakArrayList
import com.inverce.mod.v2.core.onBg
import com.inverce.mod.v2.core.onUi
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.*

open class Event<T : Listener>(protected var service: Class<T>, useWeakReferences: Boolean) : SingleEvent<T>, MultiEvent<T>, EventCaller<T>, InvocationHandler {
    protected val Log = Logger("IM.Event")

    // weak referenced used to clean_up listeners even if user forgets to, or didn't had time
    protected val list: MutableList<T?> = when {
        useWeakReferences -> WeakArrayList()
        else -> ArrayList(1)
    }

    protected val proxyCaller: T
    protected var needCleanUp: Boolean = false

    val count: Int
        get() = synchronized(list) {
            return list.size
        }


    constructor(clazz: Class<T>) : this(clazz, true) {}

    init {
        // we use method.invoke on proxy class, this has small overhead on method invoke (not execution),
        // around .057 ms instead of .042 on 2.33 ghz processor (around 30 %)
        proxyCaller = Proxy.newProxyInstance(service.classLoader, arrayOf<Class<*>>(service), this) as T
    }

    private fun cleanUp(listener: T?) {
        var cleared = 0
        if (listener != null) {
            cleared += if (list.remove(listener)) 1 else 0
        }

        if (list is WeakArrayList<*>) {
            cleared += (list as WeakArrayList<*>).clearEmptyReferences()
        }

        if (needCleanUp) {
            Log.e(tag = service.simpleName, message = "Cleaned up references $cleared")
        }
        needCleanUp = false
    }

    internal fun addListenerInternal(listener: Any) {
        if (service.isInstance(listener)) {

            addListener(listener as T)
        }
    }

    internal fun removeListenerInternal(listener: Any) {
        if (service.isInstance(listener)) {

            removeListener(listener as T)
        }
    }

    /**
     * Set single listener.
     *
     * @param listener listener
     */
    override fun setListener(listener: T?) {
        synchronized(list) {
            list.clear()
            if (listener != null) {
                list.add(listener)
            }
        }
    }

    override fun addListener(listener: T) {
        synchronized(list) {
            if (!list.contains(listener)) {
                list.add(listener)
            }
        }
    }

    override fun removeListener(listener: T) = synchronized(list) {
        cleanUp(listener)
    }

    override fun clear() = synchronized(list) {
        list.clear()
    }

    override fun post(): T {
        synchronized(list) {
            return proxyCaller
        }
    }

    @Throws(Throwable::class)
    override fun invoke(proxy: Any, method: Method, args: Array<Any>): Any? {
        val info = method.getAnnotation(EventInfo::class.java)
        val thread = info?.thread ?: ThreadPolicy.CallingThread
        return when (thread) {
            ThreadPolicy.BgThread -> onBg(createInvokerRunnable(method, args))
            ThreadPolicy.UiThread -> onUi(createInvokerRunnable(method, args))
            ThreadPolicy.CallingThread -> invokeInternal(method, args)
        }
    }

    private fun createInvokerRunnable(method: Method, args: Array<Any>): () -> Unit = {
        try {
            invokeInternal(method, args)
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        }
    }

    @Throws(Throwable::class)
    internal fun invokeInternal(method: Method, args: Array<Any>): Any? {
        synchronized(list) {
            val returns = arrayOfNulls<Any?>(list.size)
            for (i in list.indices) {
                val listener = list[i]
                if (listener != null) {
                    returns[i] = method.invoke(listener, *args)
                } else {
                    needCleanUp = true
                }
            }

            if (needCleanUp) {
                cleanUp(null)
            }
            return if (list.size > 0) returns[0] else null
        }
    }

    /**
     * Event bus is "simple" class used to manage registering and calling application global events
     * It must be noted that this class saves every registered listener in weak reference instance, thus listeners might be removed without explicit warning or unregister call
     * Bus has small overhead over managing events globally and MANUALLY, its not recommended to use Bus as way to replace standard listeners
     * Overhead is around 20-30% in function call per listener (not function total execution time, but time it takes to start method), thus is strongly advised not to use Bus in events that are repeated multiple times per second (onDraw | mouseMove.)
     */
    object Bus : Channel(), IChannelGroup by ChannelGroup()
}