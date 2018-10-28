package com.inverce.mod.v2.core.configuration

import kotlin.reflect.KProperty

typealias ValueChanged<T> = (preference: Value<T>, value: T) -> Unit

open class ReadOnlyValue<T> protected constructor() {
    constructor(value: T) : this({ value })
    constructor(getter: () -> T) : this() {
        this.getter = getter
    }

    protected lateinit var getter: () -> T

    open val value: T
        get() = getter()

    operator fun getValue(t: Any?, property: KProperty<*>): T = value
    override fun toString(): String = "Value: $value"
}

open class Value<T> protected constructor(protected var setter: (T) -> Unit = { }, var validator: (T) -> Boolean = { true }) : ReadOnlyValue<T>() {
    constructor(getter: () -> T, setter: (T) -> Unit, validator: (T) -> Boolean = { true }) : this(setter, validator) {
        this.getter = getter
    }

    constructor(value: T, validator: (T) -> Boolean = { true }) : this() {
        val box = arrayListOf(value)
        setter = { box[0] = it }
        getter = { box[0] }
        this.validator = validator
    }

    override var value: T
        get() = getter()
        set(v) {
            if (validator(v)) {
                setter(v)
                onChanged.onEach { it(this, v) }
            }
        }

    val onChanged = mutableListOf<ValueChanged<T>>()


    operator fun setValue(t: Any?, property: KProperty<*>, value: T) {
        this.value = value
    }

    fun asReadOnly(): ReadOnlyValue<T> = ReadOnlyValue({ value })

}