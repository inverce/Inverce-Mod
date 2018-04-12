package com.inverce.mod.core.configuration

import com.inverce.mod.core.MathEx.fromBase64
import com.inverce.mod.core.MathEx.toBase64

open class SharedBoolValue(initial: Boolean, name: String) : SharedValue<Boolean>(Boolean::class.java, initial, name)

open class SharedFloatValue(initial: Float, name: String) : SharedValue<Float>(Float::class.java, initial, name)

open class SharedDoubleValue(initial: Double, name: String) : SharedValue<Double>(Double::class.java, initial, name)

open class SharedIntValue(initial: Int, name: String) : SharedValue<Int>(Int::class.java, initial, name)

open class SharedLongValue(initial: Long, name: String) : SharedValue<Long>(Long::class.java, initial, name)

open class SharedStringValue(initial: String, name: String) : SharedValue<String>(String::class.java, initial, name)

open class SharedBase64Value(initial: String, name: String) : SharedStringValue(initial, name) {
    override fun getValue(): String {
        return fromBase64(super.getValue()) ?: initial
    }
    override fun setValue(e: String): Boolean {
        return super.setValue(toBase64(e) ?: initial)
    }
}

class SharedBoolAutoToggle(initial: Boolean, protected val toggled: Boolean = false, name: String) : SharedBoolValue(initial, name) {
    override fun getValue(): Boolean {
        val get = super.getValue()
        if (get != toggled) {
            setValue(toggled)
        }
        return get
    }
}