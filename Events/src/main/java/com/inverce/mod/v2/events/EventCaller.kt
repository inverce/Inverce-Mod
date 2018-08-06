package com.inverce.mod.v2.events

interface EventCaller<T> {
    fun post(): T
}

interface SingleEvent<T> {
    fun setListener(listener: T?)
}

interface MultiEvent<T> {
    fun addListener(listener: T)
    fun removeListener(listener: T)
    fun clear()
}