package com.inverce.mod.core.configuration

import com.inverce.mod.core.utilities.SubBuilder
import java.util.*

class Preset protected constructor() {
    protected var records: ArrayList<Record<*>> = ArrayList()

    fun apply(): Preset {
        for (record in records) {
            record.apply()
        }
        return this
    }

    protected inner class Record<T>(internal var preference: Value<T>, internal var value: () -> T) {

        internal fun apply() {
            preference.setValue(value())
        }
    }

    inner class Builder internal constructor() : SubBuilder<Preset>(this@Preset) {

        fun <T, Y : Value<T>> add(preference: Y, value: T): Builder {
            return addSupplier(preference, { value })
        }

        fun <T, Y : Value<T>> addSupplier(preference: Y, value: () -> T): Builder {
            this@Preset.records.add(Record(preference, value))
            return this
        }
    }

    companion object {
        fun create(): Preset.Builder {
            // create inner class for specific instance of parent,
            // inner classes have reference to parent (unless static ^^)
            return Preset().Builder()
        }
    }
}