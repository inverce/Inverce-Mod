@file:JvmName("CollectionsEx")

package com.inverce.mod.v2.core.collection

fun <T> (List<T>?).sameAs(B: List<T>?): Boolean {
    if (this == null || B == null) return this === B
    if (this.size != B.size) return false

    for (i in this.indices) {
        if (this[i] != B[i]) {
            return false
        }
    }
    return true
}

fun <T : Y, Y> List<T>.migrateType() : List<T> = this.map { it }