package com.inverce.mod.v2.core.threadpool

import android.app.Application
import android.arch.lifecycle.Lifecycle.Event
import android.arch.lifecycle.Lifecycle.Event.ON_PAUSE
import android.arch.lifecycle.LifecycleOwner
import java.io.Closeable
import java.util.concurrent.Future

class Disposer {
    val taskItems = HashMap<Any, IDisposeItem<Any>>()
    val taskEvents = HashMap<Event, Any>()

    @Synchronized
    fun add(task: Closeable?, time: Event = ON_PAUSE) = add(task, time, DisposerFactory.disposeCloseable)

    @Synchronized
    fun <T> add(task: Future<T>?, time: Event = ON_PAUSE) = add(task, time, DisposerFactory.disposeFuture)

    @Synchronized
    @Suppress("UNCHECKED_CAST")
    fun <T : Any> add(task: T?, time: Event = ON_PAUSE, dispose: IDisposeItem<T>) {
        if (task == null) return
        taskItems[task] = dispose as IDisposeItem<Any>

    }

    /**
     * Disposes specified task, if this task is in disposer removes it from set disposer
     */
    @Synchronized
    fun dispose(task: Any) {
        taskItems.remove(task)?.invoke(task)
    }

    /**
     * Removes specified task from disposer, does not dispose the task
     */
    @Synchronized
    fun remove(task: Any) {
        taskItems.remove(task)
    }

    /**
     * Disposes all stored task
     */
    @Synchronized
    fun disposeAll() {
        taskItems.forEach {
            it.value(it.key)
        }
        taskEvents.clear()
        taskItems.clear()
    }


}

typealias IDisposeItem<T> = (T) -> Unit

object DisposerFactory {
    internal val disposeCloseable: IDisposeItem<Closeable> = { it.close() }
    internal val disposeFuture: IDisposeItem<Future<*>> = { it.cancel(true) }

    private val store = HashMap<Any, Disposer>()

    fun get(owner: LifecycleOwner) = store.getOrPut(owner, ::Disposer)
    fun get(owner: Application) = store.getOrPut(owner, ::Disposer)
    fun get(owner: String) = store.getOrPut(owner, ::Disposer)
    fun get() = store.getOrPut(this, ::Disposer)
}