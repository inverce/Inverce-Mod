package com.inverce.mod.v2.integrations.recycler

import android.support.v7.util.DiffUtil
import java.util.ArrayList

class EasyDiffUtilCallBack<T>(newList: List<T>?, oldList: List<T>?) : DiffUtil.Callback() {
    private val oldList: List<T> = oldList ?: ArrayList()
    private val newList: List<T> = newList ?: ArrayList()

    override fun getOldListSize(): Int = oldList.size
    override fun getNewListSize(): Int = newList.size
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}