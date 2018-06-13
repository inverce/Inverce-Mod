@file:Suppress("unused")

package com.inverce.mod.v2.core.configuration

import java.lang.ref.WeakReference

open class AutoToggleValue<T>(initial: T, protected val toggled: T) : Value<T>(initial) {
    init {
        val oldGetter = getter
        getter = {
            val value = oldGetter()
            setValue(toggled)
            value
        }
    }
}

@Deprecated("Why not use existing by lazy { } ?")
open class LazyValue<T>(initValue: () -> T) : ReadOnlyValue<T>() {
    protected val lazyValue by lazy(initValue)
    init {
        getter = { lazyValue }
    }
}

open class LazyWeakValue<T>(initValue: () -> T, validator: (T) -> Boolean = { true }) : Value<T>() {
    protected var weakValue = WeakReference<T>(null)

    init {
        this.validator = validator
        setter = { weakValue = WeakReference(it) }
        getter = {
            var value = weakValue.get()
            if (value == null) {
                value = initValue()
                weakValue = WeakReference(value)
                value
            } else {
                value
            }
        }
    }

}