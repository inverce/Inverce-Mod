package com.inverce.mod.tests

import android.app.Activity
import android.os.Bundle
import com.inverce.mod.v2.core.Log
import com.inverce.mod.v2.core.utils.isOnUiThread
import com.inverce.mod.v2.events.Event
import com.inverce.mod.v2.events.Listener

//fun ds() {
//    onBg(delay = 200, command = {
//    }) then onUi({
//
//    }) then onBg({
//
//    })
//}
//
//infix fun ScheduledFuture<*>.then(that: ScheduledFuture<*>): ScheduledFuture<*> {
//    onBg
//
//    return onBg().schedule({
//        get()
//        that.get()
//    }, 0, TimeUnit.MILLISECONDS)
//}
//
//fun onBg(command: () -> Unit, delay: Long = 0, unit: TimeUnit = TimeUnit.MILLISECONDS): ScheduledFuture<*> = IM.onBg().schedule(command, delay, unit)
//
//fun onUi(command: () -> Unit, delay: Long = 0, unit: TimeUnit = TimeUnit.MILLISECONDS): ScheduledFuture<*> = IM.onBg().schedule(command, delay, unit)

interface Aa : Listener {
    fun d()
}

interface Bb : Listener

class MainActivity : Activity(), Aa, Bb {
    override fun d() {
        Log.w("ds dd")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Event.Bus.register<Aa>(this)
        Event.Bus.post<Aa>().d()
        if (isOnUiThread) {
            Log.w("ds")
        }
    }
}