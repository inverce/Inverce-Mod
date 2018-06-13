package com.inverce.mod.v2.core.functional

import android.graphics.Point
import android.graphics.PointF

typealias ISupplier<T> = () -> T
typealias IsEqual<T> = (T, T) -> Boolean
typealias IPredicate<T> = (T) -> Boolean
typealias IFunction<T, R> = (T) -> R
typealias IConsumer<T> = (T) -> Unit
typealias IBiFunction<T, U, R> = (T, U) -> R
typealias IBiConsumer<T, U> = (T, U) -> Unit
interface Aggregator<T> : IBiFunction<T, T, T>

class D {
    lateinit var ds : IBiFunction<Int, Point, PointF>
}