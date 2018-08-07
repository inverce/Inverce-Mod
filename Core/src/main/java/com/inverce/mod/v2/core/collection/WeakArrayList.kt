package com.inverce.mod.v2.core.collection

import java.lang.ref.WeakReference
import java.util.*

/**
 * Implementation of arrayList with values stored in weak references
 * @param <T>
</T> */
class WeakArrayList<T> : kotlin.collections.AbstractMutableList<T?>() {

    override val size: Int
        get() = impl.size

    override fun add(index: Int, element: T?) {
        impl.add(index, WeakReference(element))
    }

    override fun removeAt(index: Int): T? {
        val old = impl.removeAt(index)
        return old?.get()
    }

    override fun set(index: Int, element: T?): T? {
        val old = impl.set(index, WeakReference(element))
        return old?.get()
    }

    protected val impl = ArrayList<WeakReference<T?>>(1)

    override fun get(index: Int): T? {
        return impl[index]?.get()
    }

    fun clearEmptyReferences(): Int {
        var cleared = 0
        val it = iterator()
        while (it.hasNext()) {
            if (it.next() == null) {
                it.remove()
                cleared++
            }
        }
        return cleared
    }
}