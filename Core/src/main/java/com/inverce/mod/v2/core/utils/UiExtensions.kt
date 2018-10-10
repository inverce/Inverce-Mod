@file:JvmName("Ui")

package com.inverce.mod.v2.core.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Point
import android.os.Build
import android.os.Looper
import android.support.v4.app.Fragment
import android.view.View
import android.view.ViewParent
import android.view.ViewTreeObserver
import android.view.inputmethod.InputMethodManager
import com.inverce.mod.v2.core.IMInitializer
import com.inverce.mod.v2.core.Log
import com.inverce.mod.v2.core.internal.IMInternal
import com.inverce.mod.v2.core.resources
import java.text.Normalizer

/**
 * Enables usage of IM utilities in debug mode for Specified view,
 * does nothing in running application
 *
 * @param view the view
 */
fun View.enableInEditModeForView() {
    if (this.isInEditMode) {
        IMInternal.isInEdit = true
        IMInitializer.initialize(this.context)
    }
}

/**
 * Is on ui thread.
 *
 * @return the boolean
 */
val isOnUiThread
    get() = Looper.myLooper() == Looper.getMainLooper()

/**
 * Hide soft input.
 *
 * @param view the view
 */
fun View?.hideSoftInput() {
    try {
        this?.let {
            val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(this.windowToken, 0)
        }
    } catch (ignored: Exception) {
        /* safely ignore, as ex in here means we could not hide keyboard */
    }
}

@JvmOverloads
fun View.showSoftInput(useImplicit: Boolean = false) {
    try {
        val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        when {
            useImplicit -> imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
            else -> imm.toggleSoftInput(0, 0)
        }
    } catch (ignored: Exception) {
        /* safely ignore, as ex in here means we could not hide keyboard */
    }
}

/**
 * Changes visibility of view in safe and simple manner.
 *
 * @param view    the view
 * @param visible the visibility
 * @param gone    whatever use GONE or INVISIBLE when visible is false
 * @return whatever view will be visible
 */
@JvmOverloads
fun View?.visible(visible: Boolean, gone: Boolean = true): Boolean {
    this?.visibility = when {
        this == null -> return false
        visible -> View.VISIBLE
        gone -> View.GONE
        else -> View.INVISIBLE
    }
    return visible
}

/**
 * Run on next layout.
 *
 * @param rootView the root view
 * @param run      the run
 */
fun View.runOnNextLayout(run: Runnable) {
    val treeObserver = this.viewTreeObserver
    treeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
        override fun onGlobalLayout() {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                treeObserver.removeOnGlobalLayoutListener(this)
            } else {
                @Suppress("DEPRECATION")
                treeObserver.removeGlobalOnLayoutListener(this)
            }
            run.run()
        }
    })
}

fun Fragment.createScreenShoot(): Bitmap? {
    return this.view?.createScreenShoot()
}

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

fun View.getRelativePosition(parent: View?): Point {
    val position = Point(this.left, this.top)
    var actView: ViewParent? = this.parent ?: return position
    do {
        if (actView is View) {
            position.x += (actView as View).left
            position.y += (actView as View).top
        }
        actView = actView?.parent
    } while (actView is View && actView !== parent)
    return position
}

fun View.getPositionOnScreen(): Point {
    val pos = IntArray(2)
    this.getLocationOnScreen(pos)
    return Point(pos[0], pos[1])
}

fun String.deAccent(): String {
    return Normalizer.normalize(this, Normalizer.Form.NFD)
            .replace("ł".toRegex(), "l")
            .replace("Ł".toRegex(), "L")
            .replace("[^\\p{ASCII}]".toRegex(), "")
}

fun View?.isVisible(): Boolean {
    return this?.visibility == View.VISIBLE
}

/**
 * Converts size in pixels into estimate in dp
 *
 * @param px the px
 * @return the int
 */
fun pxToDp(px: Int): Float {
    return (px.toFloat() / resources.displayMetrics.density)
}

/**
 * Converts size in dp into estimate in pixels
 *
 * @param dp the dp
 * @return the int
 */
fun dpToPx(dp: Float): Int {
    return (dp * resources.displayMetrics.density).toInt()
}

fun Int.toDp() = pxToDp(this)
fun Float.toPx() = dpToPx(this)

/**
 * Gets location on screen for specific view
 *
 * @return the location on screen
 */
fun View?.getLocationOnScreen(): Point {
    val areaBegin = IntArray(2)
    if (this == null)
        return Point(-1, -1)
    this.getLocationOnScreen(areaBegin)
    return Point(areaBegin[0], areaBegin[1])
}

/**
 * Gets view size.
 *
 * @return the view size
 */
fun View?.getViewSize(): Point {
    return if (this == null) Point(-1, -1) else Point(this.measuredWidth, this.measuredHeight)
}