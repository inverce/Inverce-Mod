package com.inverce.mod.v2.integrations.recycler

import android.support.annotation.DrawableRes
import android.support.v4.app.ActivityCompat
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.RecyclerView.ViewHolder
import android.view.View
import com.inverce.mod.core.IM
import com.inverce.mod.core.Ui
import java.lang.ref.WeakReference

open class DividerDecorator @JvmOverloads constructor(@DrawableRes resource: Int, orientation: Int = VERTICAL) : DividerItemDecoration(IM.context(), orientation) {
    init {
        setDrawable(ActivityCompat.getDrawable(IM.context(), resource)!!)
    }
}

open class VoidItemAnimator : RecyclerView.ItemAnimator() {
    override fun animateDisappearance(viewHolder: ViewHolder, preLayoutInfo: ItemHolderInfo, postLayoutInfo: ItemHolderInfo?) = false
    override fun animateAppearance(viewHolder: ViewHolder, preLayoutInfo: ItemHolderInfo?, postLayoutInfo: ItemHolderInfo) = false
    override fun animatePersistence(viewHolder: ViewHolder, preLayoutInfo: ItemHolderInfo, postLayoutInfo: ItemHolderInfo) = false
    override fun animateChange(oldHolder: ViewHolder, newHolder: ViewHolder, preLayoutInfo: ItemHolderInfo, postLayoutInfo: ItemHolderInfo) = false
    override fun isRunning() = false
    override fun runPendingAnimations() = Unit
    override fun endAnimation(item: ViewHolder) = Unit
    override fun endAnimations() = Unit
}

open class HideSoftKeyboardOnScrollListener(rootView: View) : RecyclerView.OnScrollListener() {
    internal val rootView: WeakReference<View> = WeakReference(rootView)
    internal var isScrolling: Boolean = false

    override fun onScrollStateChanged(recyclerView: RecyclerView?, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        isScrolling = newState == RecyclerView.SCROLL_STATE_DRAGGING
    }

    override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
        super.onScrolled(recyclerView, dx, dy)
        if (isScrolling) {
            rootView.get()?.let {
                Ui.hideSoftInput(it.findFocus())
            }
        }
    }
}