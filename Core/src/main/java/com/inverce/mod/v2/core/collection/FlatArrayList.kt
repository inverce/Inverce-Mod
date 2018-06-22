package com.inverce.mod.v2.core.collection

import java.util.*
import kotlin.collections.ArrayList

/**
 * List that present data in multiple lists as its own
 *
 * optimize -
 *  when true inner lists will be flattened when store element are changed or user calls refresh, thus operating on sum of sub list, and having access time of (o(1)), this mode may present incorrect data if sublist are changed
 *  when false inner lists will be accessed when needed, access time of this mode is o(1 * M) (where M is number of sublist), this mode will show always proper elements however changes to sublist should be synchronized with flat list access
 */
class FlatArrayList<E> @JvmOverloads constructor(val optimize: Boolean = false) : kotlin.collections.AbstractList<E>(), List<E>, RandomAccess, java.io.Serializable {
    val store = FlatArrayListStore<E>()
    protected var cache: List<E> = ArrayList()
    protected var requiresRefresh = false
    protected var flatError = false

    override val size: Int
        get() = chooseAction({ cache.size }, { calculateSize() })

    override fun get(index: Int) = chooseAction({ cache[index] }, { getFlatElement(index) })

    @Synchronized
    private fun getFlatElement(index: Int): E {
        val size = size
        if (index < 0 || index >= size) {
            throw IndexOutOfBoundsException(String.format(Locale.getDefault(), "Accessing %d when size is %d", index, size))
        }

        var current = 0
        var next: Int
        var box: List<E>
        for (i in store.indices) {
            box = store[i]
            next = current + box.size
            if (index >= current && index < next) {
                return box[index - current]
            } else {
                current = next
            }
        }

        if (flatError) {
            flatError = false
            throw IllegalStateException("Reached size and not found index.")
        } else {
            flatError = true
            val el = getFlatElement(index)
            flatError = false
            return el
        }
    }

    @Synchronized
    private fun calculateSize(): Int {
        var size = 0
        for (el in store) {
            size += el.size
        }
        return size
    }

    @Synchronized
    fun refresh() {
        val newCache = ArrayList<E>()
        for (el in store) {
            newCache.addAll(el)
        }
        this.cache = newCache
    }

    protected inline fun <T> chooseAction(optimized: () -> T, optimizedDisabled: () -> T): T = when {
        optimize -> {
            if (requiresRefresh) {
                requiresRefresh = false
                refresh()
            }
            optimized()
        }
        else -> optimizedDisabled()
    }

    inner class FlatArrayListStore<T> : kotlin.collections.AbstractMutableList<List<T>>() {
        val inner = ArrayList<List<T>>()
        override val size: Int
            get() = inner.size

        override fun add(index: Int, element: List<T>) {
            inner.add(index, element)
            requiresRefresh = true
        }

        override fun get(index: Int) = inner[index]

        override fun removeAt(index: Int) = inner.removeAt(index).apply {
            requiresRefresh = true
        }

        override fun set(index: Int, element: List<T>) = inner.set(index, element).apply {
            requiresRefresh = true
        }
    }

}
