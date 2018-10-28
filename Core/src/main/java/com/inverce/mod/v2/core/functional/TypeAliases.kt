package com.inverce.mod.v2.core.functional

typealias KSupplier<T> = () -> T
typealias KPredicate<T> = (T) -> Boolean
typealias KFunction<T, R> = (T) -> R
typealias KMapper<T, R> = (T) -> R
typealias KConsumer<T> = (T) -> Unit
typealias KBiFunction<T, U, R> = (T, U) -> R
typealias KBiConsumer<T, U> = (T, U) -> Unit

@FunctionalInterface
interface ISupplierEx<T> {
    @Throws(Exception::class)
    fun get(): T
}

@FunctionalInterface
interface IAggregator<T> : IBiFunction<T, T, T>

@FunctionalInterface
interface IBiConsumer<T, U> {
    fun accept(t: T, u: U)
}

@FunctionalInterface
interface IMapper<T, R> {
    fun map(v: T) : R
}


@FunctionalInterface
interface IBiFunction<T, U, R> {
    fun apply(value1: T, value2: U): R
}

@FunctionalInterface
interface IConsumer<T> {
    fun accept(element: T)
}

@FunctionalInterface
interface IFunction<T, R> {
    fun apply(t: T): R
}

@FunctionalInterface
interface IPredicate<T> {
    fun test(`in`: T): Boolean
}

@FunctionalInterface
interface ISupplier<T> {
    fun get(): T
}