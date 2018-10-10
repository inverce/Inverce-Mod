package com.inverce.mod.v2.core.configuration

import com.inverce.mod.v2.core.utils.MathEx.fromBase64
import com.inverce.mod.v2.core.utils.MathEx.toBase64

open class SharedBoolValue(initial: Boolean, name: String) : SharedValue<Boolean>(Boolean::class.java, initial, name)

open class SharedFloatValue(initial: Float, name: String) : SharedValue<Float>(Float::class.java, initial, name)

open class SharedDoubleValue(initial: Double, name: String) : SharedValue<Double>(Double::class.java, initial, name)

open class SharedIntValue(initial: Int, name: String) : SharedValue<Int>(Int::class.java, initial, name)

open class SharedLongValue(initial: Long, name: String) : SharedValue<Long>(Long::class.java, initial, name)

open class SharedStringValue(initial: String, name: String) : SharedValue<String>(String::class.java, initial, name)

open class SharedBase64Value(initial: String, name: String) : SharedStringValue(initial, name) {
    override var value: String
        get() = fromBase64(super.value) ?: initial
        set(value) {
            super.value = toBase64(value) ?: initial
        }
}

class SharedBoolAutoToggle(initial: Boolean, protected val toggled: Boolean = false, name: String) : SharedBoolValue(initial, name) {
    override var value: Boolean
        get() {
            val get = super.value
            if (get != toggled) {
                super.value = toggled
            }
            return get
        }
        set(value) {
            super.value = value
        }
}
