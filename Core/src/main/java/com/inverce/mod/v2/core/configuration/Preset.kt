package com.inverce.mod.v2.core.configuration

import java.util.*

class Preset protected constructor() {
    private var records: ArrayList<Record<*>> = ArrayList()

    fun apply(): Preset {
        for (record in records) {
            record.apply()
        }
        return this
    }

    private inner class Record<T>(internal var preference: Value<T>, internal var value: () -> T) {
        internal fun apply() {
            preference.value = value()
        }
    }

    fun <T, Y : Value<T>> add(preference: Y, value: T) = apply {
        return addSupplier(preference, { value })
    }

    fun <T, Y : Value<T>> addSupplier(preference: Y, value: () -> T) = apply {
        this@Preset.records.add(Record(preference, value))
        return this
    }
}