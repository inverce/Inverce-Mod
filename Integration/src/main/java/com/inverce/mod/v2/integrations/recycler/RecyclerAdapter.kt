package com.inverce.mod.v2.integrations.recycler

import android.support.annotation.LayoutRes
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inverce.mod.v2.core.onUi
import com.inverce.mod.v2.core.utils.isOnUiThread
import com.inverce.mod.v2.integrations.recycler.SelectionMode.Multi
import com.inverce.mod.v2.integrations.recycler.SelectionMode.Single
import java.util.*
import kotlin.collections.HashSet

abstract class RecyclerAdapter<ITEM, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    var useDiffUtil: Boolean = false

    var selection = HashSet<ITEM>()
    var selectionMode = Multi
    var selectionAllowForeign = false

    open var data: List<ITEM> = ArrayList()
        set(items) = when {
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

    fun setData(data: List<ITEM>, useDiffUtil: Boolean) {
        if (!isOnUiThread) {
            onUi { setData(data, useDiffUtil) }
            return
        }
        val oldUseDiff = this.useDiffUtil
        this.useDiffUtil = useDiffUtil
        this.data = data
        this.useDiffUtil = oldUseDiff
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

    fun select(item: ITEM) {
        if (selectionMode == Single) {
            selection.clear()
        }

        if (selectionAllowForeign || data.contains(item)) {
            selection.add(item)
        } else {
            throw IllegalStateException("Cannot select item not in data set with selectionAllowForeign = false")
        }
    }

    fun select(list: List<ITEM>) {
        if (selectionMode == Single) {
            throw IllegalStateException("Cannot select multiple items with selectionMode = Single ")
        }

        if (data.containsAll(list)) {
            selection.addAll(list)
        } else {
            throw IllegalStateException("Cannot select item not in data set with selectionAllowForeign = false")
        }
    }

    fun clearSelection() = selection.clear()
}

enum class SelectionMode {
    Single, Multi
}