package com.inverce.mod.v2.integrations.recycler

import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.v7.widget.RecyclerView
import android.util.SparseArray
import android.view.View
import android.view.View.NO_ID
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.inverce.mod.core.Ui
import com.inverce.mod.core.functional.IConsumer

class BindViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected var children: SparseArray<View>
    protected var childrenInflated: Boolean = false

    init {
        children = SparseArray()
    }

    fun inflateChildren(): SparseArray<View> {
        if (childrenInflated) return children
        childrenInflated = true
        return children = searchForViews(itemView, children)
    }

    fun getChildren(): SparseArray<View> {
        return if (childrenInflated) {
            inflateChildren()
        } else children
    }

    operator fun <V : View> get(@IdRes res: Int): V {
        var v: V? = children.get(res) as V
        if (v == null && !childrenInflated && children.indexOfKey(res) < 0) {
            children.put(res, v = itemView.findViewById<View>(res) as V)
            return v
        }
        return v
    }

    fun has(@IdRes res: Int): Boolean {
        return get<View>(res) != null
    }

    fun <V : View> ifHas(@IdRes res: Int, consumer: IConsumer<V>): Boolean {
        val v = get<V>(res)
        if (v != null) {
            consumer.accept(v)
            return true
        }
        return false
    }

    fun bindText(@IdRes id: Int, map: String) = apply {
        val v = get<TextView>(id)
        if (v != null) v.text = map
    }

    fun bindText(@IdRes id: Int, @StringRes res: Int) = apply {
        val v = get<TextView>(id)
        v?.setText(res)
    }

    fun bindImage(@IdRes id: Int, @DrawableRes res: Int) = apply {
        val v = get<ImageView>(id)
        v?.setImageResource(res)
    }

    fun bindBackground(@IdRes id: Int, @DrawableRes res: Int)= apply {
        val v = get<View>(id)
        v?.setBackgroundResource(res)
    }

    fun bindBackground(@IdRes id: Int, res: Drawable) = apply {
        val v = get<View>(id)
        if (v != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                v.background = res
            } else {
                v.setBackgroundDrawable(res)
            }
        }
        return this
    }

    fun bindVisibility(@IdRes id: Int, visible: Boolean) = apply {
        Ui.visible(get(id), visible)
    }

    fun bindOnClickListener(@IdRes id: Int, onClickListener: View.OnClickListener): BindViewHolder {
        val v = get<View>(id)
        v?.setOnClickListener(onClickListener)
        return this
    }

    companion object {

        protected fun searchForViews(view: View, children: SparseArray<View>): SparseArray<View> {
            if (view is ViewGroup) {
                for (i in 0 until view.childCount) {
                    searchForViews(view.getChildAt(i), children)
                }
            }
            if (view.id != NO_ID) {
                children.put(view.id, view)
            }
            return children
        }
    }
}