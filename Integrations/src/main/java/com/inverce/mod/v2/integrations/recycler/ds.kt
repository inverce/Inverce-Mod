package com.inverce.mod.v2.integrations.recycler

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inverce.mod.core.Ui.hideSoftInput
import com.inverce.mod.core.functional.IFunction
import com.inverce.mod.integrations.support.annotations.IBinder
import com.inverce.mod.integrations.support.recycler.BindViewHolder
import com.inverce.mod.integrations.support.recycler.DataBinder
import java.lang.ref.WeakReference
import java.util.*


open class MultiRecyclerAdapter<ITEM> : RecyclerAdapter<ITEM, RecyclerView.ViewHolder>() {
    protected var types: MutableList<MultiInfo<*, *>> = ArrayList()
    protected var typesSparse: SparseArray<MultiInfo<*, *>> = SparseArray()

    fun resetRegistrations() {
        types = ArrayList()
        typesSparse = SparseArray()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ITEM, position: Int) {
        val type = getItemViewType(position)
        val info = typesSparse.get(type)
        info.onBindViewHolder(holder, item, position)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val info = typesSparse.get(viewType)
        val inflater = LayoutInflater.from(parent.context)
        return info.createViewHolder(parent, inflater)
    }

    override fun getItemViewType(position: Int): Int {
        val item = getItem(position)
        for (type in types) {
            if (type.checkType(item)) {
                return type.registeredType
            }
        }
        return -1
    }

    fun <I : ITEM, VH : RecyclerView.ViewHolder> register(checkType: MultiPredicate<ITEM>, binder: MultiBind<I, VH>, createViewHolder: MultiCreateVH<VH>): Int {
        val info = MultiInfo(checkType, binder, createViewHolder)
        types.add(info)
        info.registeredType = types.size
        typesSparse.put(info.registeredType, info)
        return info.registeredType
    }

    fun <I : ITEM, VH : RecyclerView.ViewHolder> register(checkType: MultiPredicate<ITEM>, binder: MultiBind<I, VH>, createViewHolder: MultiCreateVH2<VH>, @LayoutRes layout: Int): Int {
        return register(checkType, binder, { parent, inflater -> createViewHolder(inflater.inflate(layout, parent, false)) })
    }

    fun <I : ITEM, VH> register(checkType: MultiPredicate<ITEM>, createViewHolder: MultiCreateVH<VH>): Int where VH : RecyclerView.ViewHolder, VH : IBinder<I> {
        return register(checkType, { vh, item: I, position -> vh.onBindViewHolder(item, position) }, createViewHolder)
    }

    fun <I : ITEM, VH> register(checkType: MultiPredicate<ITEM>, createViewHolder: IFunction<View, VH>, @LayoutRes layout: Int): Int where VH : RecyclerView.ViewHolder, VH : IBinder<I> {
        return register(checkType, MultiBind<I, VH> { vh, item, position -> vh.onBindViewHolder(item, position) }, ICreateVH { parent, inflater -> createViewHolder.apply(inflater.inflate(layout, parent, false)) })
    }

    fun <I : ITEM> register(checkType: MultiPredicate<ITEM>, binder: DataBinder<I>, @LayoutRes layout: Int): Int {
        return register(checkType, binder, { BindViewHolder(it) }, layout)
    }

    protected inner class MultiInfo<I : ITEM, VH : RecyclerView.ViewHolder>(
            var checkType: MultiPredicate<ITEM>,
            var binder: MultiBind<I, VH>,
            var createViewHolder: MultiCreateVH<VH>) {
        var registeredType: Int = 0
        fun onBindViewHolder(holder: RecyclerView.ViewHolder, item: ITEM, position: Int) {
            binder(holder as VH, item as I, position)
        }
    }

}
