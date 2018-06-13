package com.inverce.mod.v2.integrations.recycler

import android.support.annotation.DrawableRes
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityCompat
import android.support.v7.util.DiffUtil
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.inverce.mod.core.IM
import com.inverce.mod.core.Ui
import com.inverce.mod.core.Ui.hideSoftInput
import com.inverce.mod.core.functional.IFunction
import com.inverce.mod.integrations.support.annotations.IBinder
import com.inverce.mod.integrations.support.recycler.BindViewHolder
import com.inverce.mod.integrations.support.recycler.DataBinder
import java.lang.ref.WeakReference
import java.util.*

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

class HideSoftKeyboardOnScrollListener(rootView: View) : RecyclerView.OnScrollListener() {
    internal val rootView: WeakReference<View> = WeakReference(rootView)
    internal var isScrolling: Boolean = false

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        isScrolling = newState == SCROLL_STATE_DRAGGING
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (isScrolling) {
            rootView.get()?.let {
                hideSoftInput(it.findFocus())
            }
        }
    }
}

class VoidItemAnimator : RecyclerView.ItemAnimator() {
    override fun animateDisappearance(viewHolder: RecyclerView.ViewHolder, preLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo, postLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo?) = false
    override fun animateAppearance(viewHolder: RecyclerView.ViewHolder, preLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo?, postLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo) = false
    override fun animatePersistence(viewHolder: RecyclerView.ViewHolder, preLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo, postLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo) = false
    override fun animateChange(oldHolder: RecyclerView.ViewHolder, newHolder: RecyclerView.ViewHolder, preLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo, postLayoutInfo: RecyclerView.ItemAnimator.ItemHolderInfo) = false
    override fun isRunning() = false
    override fun runPendingAnimations() = Unit
    override fun endAnimation(item: RecyclerView.ViewHolder) = Unit
    override fun endAnimations() = Unit
}

class SimpleDividerDecorator @JvmOverloads constructor(@DrawableRes resource: Int, orientation: Int = DividerItemDecoration.VERTICAL) : DividerItemDecoration(IM.context(), orientation) {
    init {
        setDrawable(ActivityCompat.getDrawable(IM.context(), resource)!!)
    }
}

abstract class RecyclerAdapter<ITEM, VH : RecyclerView.ViewHolder> : RecyclerView.Adapter<VH>() {
    var data: List<ITEM> = ArrayList()
        protected set

    fun getItem(position: Int): ITEM = data[position]

    override fun onBindViewHolder(holder: VH, position: Int) {
        onBindViewHolder(holder, getItem(position), position)
    }

    abstract fun onBindViewHolder(holder: VH, item: ITEM, position: Int)
    override fun getItemCount() = data.size

    fun setData(items: List<ITEM>, useDiffUtil: Boolean = false): Unit = when {
        !Ui.isUiThread() -> IM.onUi().execute { setData(items, useDiffUtil) } // TODO make to IM.V2
        useDiffUtil -> {
            val oldItems = ArrayList(data)
            DiffUtil.calculateDiff(EasyDiffUtilCallBack(items, oldItems))
                    .dispatchUpdatesTo(this)
            data = items
        }
        else -> {
            data = items
            notifyDataSetChanged()
        }
    }

    protected fun inflate(parent: ViewGroup, @LayoutRes res: Int): View {
        return LayoutInflater.from(parent.context)
                .inflate(res, parent, false)
    }
}

typealias MultiPredicate<T> = (T) -> Boolean
typealias MultiBind<I, VH> = (VH, I, position: Int) -> Boolean
typealias MultiCreateVH<VH> = (parent: ViewGroup, inflater: LayoutInflater) -> VH
typealias MultiCreateVH2<VH> = (View) -> VH


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
