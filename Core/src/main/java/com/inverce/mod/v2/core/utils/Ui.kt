@file:Suppress("unused")

package com.inverce.mod.v2.core.utils


import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.graphics.drawable.StateListDrawable
import android.os.Build
import android.os.Looper
import android.support.annotation.ColorRes
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.util.StateSet
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import com.inverce.mod.core.IM
import com.inverce.mod.v2.core.Log
import java.io.Closeable
import java.text.Normalizer

object Ui {
    private val PADDING_NPE = "To set padding you must provide VIEW"

    /**
     * Is on ui thread.
     *
     * @return the boolean
     */
    @JvmStatic val isUiThread
        get() = Looper.myLooper() == Looper.getMainLooper()

    /**
     * Make selector state for specified colors
     *
     * @param drawable    the drawable
     * @param pressedRes  the pressed res
     * @param disabledRes the disabled res
     * @return the state list drawable
     */
    @JvmStatic fun makeSelector(drawable: Drawable, @ColorRes pressedRes: Int, @ColorRes disabledRes: Int): StateListDrawable {
        val state = StateListDrawable()
        val pressed = LayerDrawable(arrayOf(drawable, ColorDrawable(ActivityCompat.getColor(IM.context(), pressedRes))))
        val disabled = LayerDrawable(arrayOf(drawable, ColorDrawable(ActivityCompat.getColor(IM.context(), disabledRes))))
        state.addState(intArrayOf(android.R.attr.state_pressed), pressed)
        state.addState(intArrayOf(android.R.attr.state_focused), disabled)
        state.addState(StateSet.WILD_CARD, drawable)
        return state
    }

    /**
     * Changes visibility of view in safe and simple manner.
     *
     * @param view    the view
     * @param visible the visibility
     * @param gone    whatever use GONE or INVISIBLE when visible is false
     * @return whatever view will be visible
     */
    @JvmStatic @JvmOverloads
    fun View?.visible(visible: Boolean, gone: Boolean = true): Boolean {
        when {
            this == null -> return false
            visible -> visibility = View.VISIBLE
            gone -> visibility = View.GONE
            else -> visibility = View.INVISIBLE
        }
        return visible
    }

    @JvmStatic
    fun View.getRelativePosition(parent: View?): Point {
        val position = Point(this.left, this.top)
        var actView: ViewParent? = this.parent ?: return position
        do {
            if (actView is View) {
                position.x += (actView as View).left
                position.y += (actView as View).top
            }
            actView = actView!!.parent
        } while (actView is View && actView !== parent)
        return position
    }

    @JvmStatic
    fun View.getPositionOnScreen(): Point {
        val pos = IntArray(2)
        this.getLocationOnScreen(pos)
        return Point(pos[0], pos[1])
    }

    @JvmStatic
    fun Fragment.createScreenShoot(): Bitmap? {
        return this.view?.createScreenShoot()
    }

    @JvmStatic
    fun View?.createScreenShoot(): Bitmap? = this?.let {
        try {
            isDrawingCacheEnabled = true
            val bitmap = Bitmap.createBitmap(it.drawingCache)
            isDrawingCacheEnabled = false
            bitmap
        } catch (e: Throwable) {
            Log.exs(e)
            null
        }
    }

    /**
     * Run on next layout.
     *
     * @param rootView the root view
     * @param run      the run
     */
    @JvmStatic
    fun runOnNextLayout(rootView: View, run: Runnable) {
        rootView.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rootView.viewTreeObserver.removeOnGlobalLayoutListener(this)
                } else {
                    @Suppress("DEPRECATION")
                    rootView.viewTreeObserver.removeGlobalOnLayoutListener(this)
                }
                run.run()
            }
        })
    }

    /**
     * Hide soft input.
     *
     * @param view the view
     */
    @JvmStatic
    fun hideSoftInput(view: View) {
        try {
            val imm = IM.context().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        } catch (ignored: Exception) {
            /* safely ignore, as ex in here means we could not hide keyboard */
        }
    }

    @JvmStatic @JvmOverloads
    fun showSoftInput(view: View, useImplicit: Boolean = false) {
        try {
            val imm = IM.context().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            when {
                useImplicit -> imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
                else -> imm.toggleSoftInput(0, 0)
            }
        } catch (ignored: Exception) {
            /* safely ignore, as ex in here means we could not hide keyboard */
        }
    }

    @JvmStatic
    fun deAccent(name: String): String {
        return Normalizer.normalize(name, Normalizer.Form.NFD)
                .replace("ł".toRegex(), "l")
                .replace("Ł".toRegex(), "L")
                .replace("[^\\p{ASCII}]".toRegex(), "")
    }

    @Suppress("UNCHECKED_CAST")
    open class Layout<P: ViewGroup.LayoutParams>(val params: P, val view: View? = null) : Closeable {
        constructor(view: View) : this(view.layoutParams as P, view)

        @JvmOverloads
        fun setLayoutSize(width: Int = 0, height: Int = 0) {
            params.width = width
            params.height = height
        }

        @JvmOverloads
        fun setPadding(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0) {
            view?.setPadding(left, top, right, bottom)
            view?.setPadding() = 33
        }
        override fun close() {
            view?.layoutParams = params
        }

        companion object {
            fun on(view: View): Layout<ViewGroup.LayoutParams> = Layout(view)
            fun on(params: ViewGroup.LayoutParams) = Layout(params)
        }
    }


//    class Margin : Layout2 {
//        internal var params: ViewGroup.MarginLayoutParams
//
//        internal constructor(view: View) : super(view) {
//            this.params = view.layoutParams as ViewGroup.MarginLayoutParams
//        }
//
//        internal constructor(params: ViewGroup.MarginLayoutParams) : super(params) {
//            this.params = params
//        }
//
//        fun top(margin: Int, usePixels: Boolean): Margin {
//            params.topMargin = if (usePixels) margin else Screen.dpToPx(margin.toFloat())
//            return this
//        }
//
//        fun left(margin: Int, usePixels: Boolean): Margin {
//            params.leftMargin = if (usePixels) margin else Screen.dpToPx(margin.toFloat())
//            return this
//        }
//
//        fun right(margin: Int, usePixels: Boolean): Margin {
//            params.rightMargin = if (usePixels) margin else Screen.dpToPx(margin.toFloat())
//            return this
//        }
//
//        fun bottom(margin: Int, usePixels: Boolean): Margin {
//            params.bottomMargin = if (usePixels) margin else Screen.dpToPx(margin.toFloat())
//            return this
//        }
//
//        override fun done() {
//            if (view != null) {
//                view!!.layoutParams = params
//            }
//        }
//
//        companion object {
//
//            fun on(view: View): Margin {
//                return Margin(view)
//            }
//
//            fun on(params: ViewGroup.MarginLayoutParams): Margin {
//                return Margin(params)
//            }
//        }
//    }
}