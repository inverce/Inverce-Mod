package com.inverce.mod.v2.core.collection

import java.util.HashMap
import kotlin.collections.ArrayList
import kotlin.collections.component1
import kotlin.collections.component2

class Entry<K, V>(
        var key: K,
        var value: V,
        var left: Entry<K, V>? = null,
        var right: Entry<K, V>? = null
)

class LruCache<K, V>(val maxSize: Int = 100) : MutableMap<K, V> {
    var hashmap: MutableMap<K, Entry<K, V>> = HashMap()
    internal var start: Entry<K, V>? = null
    internal var end: Entry<K, V>? = null

    override val size get() = hashmap.size
    override fun isEmpty() = hashmap.isEmpty()
    override val keys get() = hashmap.keys
    override val values get() = ArrayList(hashmap.values.map { it.value })

    override fun containsKey(key: K) = hashmap.containsKey(key)
    override fun containsValue(value: V) = hashmap.entries.indexOfFirst { it.value == value } != -1

    override fun get(key: K): V? {
        return if (hashmap.containsKey(key)) {
            hashmap[key]?.apply {
                removeNode(this)
                addAtTop(this)
            }?.value
        } else {
            null
        }
    }

    override fun putAll(from: Map<out K, V>) {
        for ((k, v) in from) {
            put(k, v)
        }
    }

    override fun clear() {
        hashmap.clear()
        start = null
        end = null
    }

    override fun put(key: K, value: V): V? {
        // Key Already Exist, just update the value and move it to top
        return if (hashmap.containsKey(key)) {
            hashmap[key]?.let {
                val oldValue = it.value
                it.value = value
                removeNode(it)
                addAtTop(it)
                oldValue
            }
        } else {
            val node = Entry(key, value)
            // We have reached maxium size so need to make room for new element.
            if (hashmap.size >= maxSize) {
                end?.apply {
                    hashmap.remove(this.key)
                    removeNode(this)
                }
            }
            addAtTop(node)
            hashmap.put(key, node)?.value
        }
    }


    override val entries: MutableSet<MutableMap.MutableEntry<K, V>> get() {
        val map = hashmap.mapValues { it.value.value }
        return map.toMutableMap().entries
    }

    override fun remove(key: K): V? {
        return if (hashmap.containsKey(key)) {
            hashmap[key]?.let {
                removeNode(it)
                hashmap.remove(key)
                it.value
            }
        } else {
            null
        }
    }


    protected fun addAtTop(node: Entry<K, V>) {
        node.right = start
        node.left = null
        if (start != null)
            start?.left = node
        start = node
        if (end == null)
            end = start
    }

    protected fun removeNode(node: Entry<K, V>) {
        if (node.left != null) {
            node.left?.right = node.right
        } else {
            start = node.right
        }

        if (node.right != null) {
            node.right?.left = node.left
        } else {
            end = node.left
        }
    }
}