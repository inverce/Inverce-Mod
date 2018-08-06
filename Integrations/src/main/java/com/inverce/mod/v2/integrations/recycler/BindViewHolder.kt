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
import com.inverce.mod.v2.core.functional.IConsumer
import com.inverce.mod.v2.core.utils.visible

@Suppress("unused", "UNCHECKED_CAST")
open class BindViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    protected var childrenInflated: Boolean = false
    val children: SparseArray<View> by lazy {
        childrenInflated = true
        searchForViews(itemView)
    }

    operator fun <V : View> get(@IdRes res: Int): V? {
        return children.get(res) as? V
    }

    fun has(@IdRes res: Int): Boolean {
        return get<View>(res) != null
    }

    fun <V : View> ifHas(@IdRes res: Int, consumer: IConsumer<V>): Boolean {
        val v = get<V>(res)
        if (v != null) {
            consumer(v)
            return true
        }
        return false
    }

    fun bindText(@IdRes id: Int, map: String) = apply {
        get<TextView>(id)?.text = map
    }

    fun bindText(@IdRes id: Int, @StringRes res: Int) = apply {
        get<TextView>(id)?.setText(res)
    }

    fun bindImage(@IdRes id: Int, @DrawableRes res: Int) = apply {
        get<ImageView>(id)?.setImageResource(res)
    }

    fun bindBackground(@IdRes id: Int, @DrawableRes res: Int) = apply {
        get<View>(id)?.setBackgroundResource(res)
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
        get<View>(id).visible(visible)
    }

    fun bindOnClickListener(@IdRes id: Int, onClickListener: View.OnClickListener) = apply {
        get<View>(id)?.setOnClickListener(onClickListener)
    }

    fun bindOnClickListener(@IdRes id: Int, onClickListener: (View) -> Unit) = apply {
        get<View>(id)?.setOnClickListener(onClickListener)
    }

    protected fun searchForViews(view: View, children: SparseArray<View> = SparseArray()): SparseArray<View> {
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


