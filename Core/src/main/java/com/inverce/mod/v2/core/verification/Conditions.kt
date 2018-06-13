@file:JvmName("Conditions")

package com.inverce.mod.v2.core.verification

import android.database.Cursor
import android.view.View
import java.lang.ref.WeakReference

@SafeVarargs
fun <T> firstNonNull(value: T?, vararg values: T) = when (value) {
    null -> value
    else -> values.firstOrNull { it != null }
}

fun noneNull(vararg objects: Any?) = objects.none { it == null }

fun notNullOrEmpty(value: String?) = value?.isNotEmpty()
fun notNullOrEmpty(value: WeakReference<*>?) = value?.get() != null
fun <T> notNullOrEmpty(value: Array<T>?) = value?.isNotEmpty()
fun notNullOrEmpty(value: Collection<*>?) = value?.isNotEmpty()
fun notNullOrEmpty(value: Cursor?) = value?.count ?: 0 > 0

fun nullOrEmpty(value: String?) = value?.isEmpty() ?: true
fun nullOrEmpty(value: WeakReference<*>?) = value?.get() == null ?: true
fun <T> nullOrEmpty(value: Array<T>?) = value?.isEmpty() ?: true
fun nullOrEmpty(value: Collection<*>?) = value?.isEmpty() ?: true
fun nullOrEmpty(value: Map<*, *>?) = value?.isEmpty() ?: true
fun nullOrEmpty(value: Cursor?) = value?.count

fun View?.isVisible(): Boolean {
    return this?.visibility == View.VISIBLE
}

inline fun <T, Y> nullPass(value: T?, pass: (T) -> Y?): Y? = when (value) {
    null -> null
    else -> pass(value)
}