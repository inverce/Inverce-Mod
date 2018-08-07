package com.inverce.mod.v2.integrations.recycler

import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inverce.mod.v2.core.onUi
import com.inverce.mod.v2.core.utils.isOnUiThread
import java.util.*

abstract class RecyclerAdapter<ITEM, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    var useDiffUtil: Boolean = false

    var data: List<ITEM> = ArrayList()
        protected set(items) = when {
            !isOnUiThread -> onUi { data = items }
            useDiffUtil -> {
                val oldItems = ArrayList(data)
                DiffUtil.calculateDiff(EasyDiffUtilCallBack(items, oldItems))
                        .dispatchUpdatesTo(this)
                field = items
            }
            else -> {
                field = items
                notifyDataSetChanged()
            }
        }

    fun getItem(position: Int): ITEM = data[position]

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, getItem(position), position)
    }

    abstract fun onBindViewHolder(holder: VH, item: ITEM, position: Int)
    override fun getItemCount() = data.size

    protected fun inflate(parent: ViewGroup, @LayoutRes res: Int): View {
        return LayoutInflater.from(parent.context)
                .inflate(res, parent, false)
    }
}

