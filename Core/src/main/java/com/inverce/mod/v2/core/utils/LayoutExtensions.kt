@file:Suppress("unused")
@file:JvmName("Layout")

package com.inverce.mod.v2.core.utils


import android.graphics.Point
import android.view.View
import android.view.ViewGroup

@get:JvmName("toSimpleParams")
val View.layoutParamsIM
    get() = SimpleLayoutParams(this.layoutParams)

val View.paddingIM
    get() = SimplePadding(this)


fun <P : ViewGroup.LayoutParams> P.asSimple() = SimpleLayoutParams(this)


@Suppress("UNCHECKED_CAST")
open class SimpleLayoutParams<P : ViewGroup.LayoutParams>(private val layoutParams: P) {
    fun asLayoutParams(): ViewGroup.LayoutParams = layoutParams
    fun asMarginParams() = layoutParams as ViewGroup.MarginLayoutParams

    var width: Int
        get() = asLayoutParams().width
        set(v) {
            asLayoutParams().width = v
        }

    var height: Int
        get() = asLayoutParams().height
        set(v) {
            asLayoutParams().height = v
        }

    var size: Point
        get() = Point(width, height)
        set(v) {
            width = v.x
            height = v.y
        }

    val margin = SimpleMargin()


    open inner class SimpleMargin {
        var top: Int
            get() = asMarginParams().topMargin
            set(v) {
                asMarginParams().topMargin = v
            }

        var bottom: Int
            get() = asMarginParams().bottomMargin
            set(v) {
                asMarginParams().bottomMargin = v
            }

        var left: Int
            get() = asMarginParams().leftMargin
            set(v) {
                asMarginParams().leftMargin = v
            }

        var right: Int
            get() = asMarginParams().rightMargin
            set(v) {
                asMarginParams().rightMargin = v
            }

        var all: Int
            get() = Math.min(Math.min(top, bottom), Math.min(left, right))
            set(v) {
                top = v
                bottom = v
                left = v
                right = v
            }

        var vertical: Int
            get() = Math.min(top, bottom)
            set(v) {
                top = v
                bottom = v
            }

        var horizontal: Int
            get() = Math.min(left, right)
            set(v) {
                left = v
                right = v
            }
    }
}

open class SimplePadding(val view: View) {
    var top: Int
        get() = view.paddingTop
        set(v) {
            view.setPadding(left, v, right, bottom)
        }

    var bottom: Int
        get() = view.paddingBottom
        set(v) {
            view.setPadding(left, top, right, v)
        }

    var left: Int
        get() = view.paddingLeft
        set(v) {
            view.setPadding(v, top, right, bottom)
        }

    var right: Int
        get() = view.paddingRight
        set(v) {
            view.setPadding(left, top, v, bottom)
        }

    var all: Int
        get() = Math.min(Math.min(top, bottom), Math.min(left, right))
        set(v) {
            top = v
            bottom = v
            left = v
            right = v
        }

    var vertical: Int
        get() = Math.min(top, bottom)
        set(v) {
            top = v
            bottom = v
        }

    var horizontal: Int
        get() = Math.min(left, right)
        set(v) {
            left = v
            right = v
        }
}



