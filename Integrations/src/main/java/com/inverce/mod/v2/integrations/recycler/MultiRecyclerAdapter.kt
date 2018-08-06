package com.inverce.mod.v2.integrations.recycler

import android.support.annotation.LayoutRes
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.ViewGroup
import java.util.*

open class MultiRecyclerAdapter<ITEM : Any> : RecyclerAdapter<ITEM, RecyclerView.ViewHolder>() {
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

    inline fun <reified T> type(crossinline check: MultiPredicate<T> = { true }): MultiPredicate<ITEM> = { it is T && check(it as T) }

    infix fun <VH : RecyclerView.ViewHolder> MultiViewToVH<VH>.inflate(@LayoutRes layout: Int): MultiCreateVH<VH> = { parent: ViewGroup, inflater: LayoutInflater ->
        this(inflater.inflate(layout, parent, false))
    }

    @JvmOverloads
    fun <I : ITEM, VH : RecyclerView.ViewHolder> register(
            bind: MultiBind<I, VH>,
            vh: MultiCreateVH<VH>,
            check: MultiPredicate<ITEM> = { true }
    ) = MultiInfo(check, bind, vh).apply {
        registeredType = types.size
        types.add(this)
        typesSparse.put(registeredType, this)
    }.registeredType

    @JvmOverloads
    fun <I, VH> register(bind: MultiBind<I, VH>, create: MultiViewToVH<VH>, @LayoutRes res: Int, check: MultiPredicate<ITEM> = { true }): Int
            where I : ITEM, VH : RecyclerView.ViewHolder = register(bind, create inflate res, check)

    fun <I, VH> register(create: MultiViewToVH<VH>, @LayoutRes res: Int, check: MultiPredicate<ITEM> = { true }): Int
            where I : ITEM, VH : RecyclerView.ViewHolder, VH : MultiBinder<I> = register({ vh, item: I, pos ->
        vh.onBindViewHolder(item, pos)
    }, create inflate res, check)

    fun <I> register(binder: DataBinder<I>, @LayoutRes res: Int, check: MultiPredicate<ITEM> = { true }): Int
            where I : ITEM = register(binder::invoke, ::BindViewHolder inflate res, check)

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