package com.inverce.mod.v2.events

@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.FUNCTION, AnnotationTarget.PROPERTY_GETTER, AnnotationTarget.PROPERTY_SETTER)
annotation class EventInfo(val thread: ThreadPolicy = ThreadPolicy.CallingThread)

enum class ThreadPolicy {
    BgThread, UiThread, CallingThread
}