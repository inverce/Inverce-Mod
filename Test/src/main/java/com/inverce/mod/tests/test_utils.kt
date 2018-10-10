package com.inverce.mod.tests

import android.app.Activity
import com.inverce.mod.core.IM
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

fun ds() {
    onBg(delay = 200, command = {
    }) then onUi({

    }) then onBg({

    })

}

infix fun ScheduledFuture<*>.then(that: ScheduledFuture<*>): ScheduledFuture<*> {
    return IM.onBg().schedule({
        get()
        that.get()
    }, 0, TimeUnit.MILLISECONDS)
}

fun onBg(command: () -> Unit, delay: Long = 0, unit: TimeUnit = TimeUnit.MILLISECONDS): ScheduledFuture<*>
        = IM.onBg().schedule(command, delay, unit)

fun onUi(command: () -> Unit, delay: Long = 0, unit: TimeUnit = TimeUnit.MILLISECONDS): ScheduledFuture<*>
        = IM.onBg().schedule(command, delay, unit)

class MainActivity : Activity {

}