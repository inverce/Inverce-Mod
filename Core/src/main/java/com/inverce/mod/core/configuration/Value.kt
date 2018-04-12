package com.inverce.mod.core.configuration

import kotlin.reflect.KProperty

typealias ValueChanged<T> = (preference: Value<T>, value: T) -> Unit

interface ValueChangedEvent<T> {
    fun addListener(listener: T)
    fun removeListener(listener: T)
    fun clear()
}

open class ReadOnlyValue<T> protected constructor() {
    protected lateinit var getter: () -> T

    constructor(value: T) : this({ value })
    constructor(value: Value<T>) : this({ value.getValue() })
    constructor(getter: () -> T) : this() {
        this.getter = getter
    }

    open fun getValue(): T = getter()
    operator fun getValue(t: Any?, property: KProperty<*>): T = getValue()
    override fun toString(): String {
        return "Value: " + (getValue() ?: "null")
    }
}

open class Value<T> protected constructor(protected var setter: (T) -> Unit = { }, var validator: (T) -> Boolean = { true }) : ReadOnlyValue<T>() {
    val onChanged = listOf<(T) -> Unit>()

    constructor(getter: () -> T, setter: (T) -> Unit, validator: (T) -> Boolean = { true }) : this(setter, validator) {
        this.getter = getter
    }

    constructor(value: T, validator: (T) -> Boolean = { true }) : this() {
        val box = arrayListOf(value)
        setter = { box[0] = it }
        getter = { box[0] }
        this.validator = validator
    }

    open fun setValue(e: T) = when (validator(e)) {
        true -> {
            setter(e)
            onChanged.onEach { it(e) }
            true
        }
        else -> false
    }

    operator fun setValue(t: Any?, property: KProperty<*>, value: T) = setValue(value)

    fun asReadOnly(): ReadOnlyValue<T> = ReadOnlyValue(this)

}