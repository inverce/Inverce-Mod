@file:JvmName("Conditions")

package com.inverce.mod.v2.core.verification

import android.database.Cursor
import java.lang.ref.WeakReference

@SafeVarargs
fun <T> firstNonNull(value: T?, vararg values: T) = when (value) {
    null -> value
    else -> values.firstOrNull { it != null }
}

fun noneNull(vararg objects: Any?) = objects.none { it == null }

fun String?.isNotNullOrEmpty() = this?.isNotEmpty() ?: false
fun WeakReference<*>?.isNotNullOrEmpty() = this?.get() != null
fun <T> Array<T>?.isNotNullOrEmpty() = this?.isNotEmpty() ?: false
fun Collection<*>?.isNotNullOrEmpty() = this?.isNotEmpty() ?: false
fun Cursor?.isNotNullOrEmpty() = this?.count ?: 0 > 0

fun String?.isNullOrEmpty() = this?.isEmpty() ?: true
fun WeakReference<*>?.isNullOrEmpty() = this?.get() == null ?: true
fun <T> Array<T>?.isNullOrEmpty() = this?.isEmpty() ?: true
fun Collection<*>?.isNullOrEmpty() = this?.isEmpty() ?: true
fun Map<*, *>?.isNullOrEmpty() = this?.isEmpty() ?: true
fun Cursor?.isNullOrEmpty() = this?.count

internal inline fun <T, Y> nullPass(value: T?, pass: (T) -> Y?): Y? = when (value) {
    null -> null
    else -> pass(value)
}

fun <T> T?.nullWhen(filter: T) = nullWhen { this == filter }
inline fun <T> T?.nullWhen(filter: T.() -> Boolean) = when {
    this == null -> null
    filter(this) -> null
    else -> this
}