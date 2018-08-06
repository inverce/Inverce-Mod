package com.inverce.mod.v2.integrations.recycler

import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.IdRes
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.inverce.mod.v2.core.functional.IMapper
import com.inverce.mod.v2.core.utils.visible
import java.util.*

@Suppress("unused")
open class DataBinder<T> : MultiBind<T, BindViewHolder> {
    public var loadImage: (String, ImageView) -> Unit = { _, _ -> throw IllegalStateException("Image processor not specified") }

    protected var tasks: MutableList<MultiBind<T, BindViewHolder>> = ArrayList()

    fun bind(bind: MultiBind<T, BindViewHolder>) = apply {
        tasks.add(bind)
    }

    inline fun <V : View> bind(crossinline view: IMapper<BindViewHolder, V>, crossinline binder: MultiBind<T, V>) = bind { holder, item, position ->
        binder(view(holder), item, position)
    }

    inline fun <V : View> bind(@IdRes res: Int, crossinline binder: MultiBind<T, V>) = bind { holder, item, position ->
        val v: V = holder[res] ?: throw IllegalStateException("Resource $res not found")
        binder(v, item, position)
    }

    fun bindText(@IdRes id: Int, map: IMapper<T, String>) = bind<TextView>(id) { view, item, _ ->
        view.text = map(item)
    }

    fun bindTextRes(@IdRes id: Int, map: IMapper<T, Int>) = bind<TextView>(id) { view, item, _ ->
        view.setText(map(item))
    }

    fun bindImageRes(@IdRes id: Int, map: IMapper<T, Int>) = bind<ImageView>(id) { view, item, _ ->
        view.setImageResource(map(item))
    }

    fun bindImage(@IdRes id: Int, map: IMapper<T, String>) = bind<ImageView>(id) { view, item, _ ->
        loadImage(map(item), view)
    }

    fun bindBackgroundRes(@IdRes id: Int, map: IMapper<T, Int>) = bind<View>(id) { view, item, _ ->
        view.setBackgroundResource(map(item))
    }

    fun bindBackground(@IdRes id: Int, map: IMapper<T, Drawable>) = bind<View>(id) { view, item, _ ->
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.background = map(item)
        } else {
            @Suppress("DEPRECATION")
            view.setBackgroundDrawable(map(item))
        }
    }

    fun bindVisibility(@IdRes id: Int, map: IMapper<T, Boolean>, gone: Boolean = true) = bind<View>(id) { view, item, _ ->
        view.visible(map(item), gone)
    }

    fun bindOnClickListener(@IdRes id: Int, map: IMapper<T, View.OnClickListener>) = bind<View>(id) { view, item, _ ->
        view.setOnClickListener(map(item))
    }

    @Synchronized
    override fun invoke(holder: BindViewHolder, item: T, position: Int) {
        for (bind in tasks) {
            bind(holder, item, position)
        }
    }
}