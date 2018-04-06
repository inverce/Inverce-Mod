package com.inverce.mod.tests

import com.inverce.mod.core.threadpool.DynamicScheduledExecutor
import org.junit.Assert.assertEquals
import org.junit.Test
import java.util.concurrent.Callable
import java.util.concurrent.TimeUnit


class ThreadsTest {

    @Test
    fun bgthread_executes_in_thread() {
        val v = Array<Boolean>(1, { false });
        val t = DynamicScheduledExecutor()
        t.schedule({
            v[0] = true
        }, 1, TimeUnit.SECONDS).get()
        Thread.sleep(100)
        assertEquals(v[0], true)
    }

    @Test
    fun callable_retrurns_value() {
        val t = DynamicScheduledExecutor()
        t.setKeepAliveTime(1, TimeUnit.SECONDS);
        val r = t.schedule(Callable<Boolean> { true }, 1, TimeUnit.SECONDS).get()
        Thread.sleep(100)
        assertEquals(r, true)
    }

    @Test
    fun fixed_rate_sheduler_ticks_properly() {
        val v = Array<Int>(1, { 0 });
        val t = DynamicScheduledExecutor()
        val f = t.scheduleAtFixedRate({
            v[0]++
        }, 0, 100, TimeUnit.MILLISECONDS)
        Thread.sleep(1000)
        f.cancel(false)
        assertEquals(v[0] > 8, true)
    }

    @Test
    fun fixed_rate_delay_sheduler_ticks_properly() {
        val v = Array<Int>(1, { 0 });
        val t = DynamicScheduledExecutor()
        val f = t.scheduleWithFixedDelay({
            v[0]++
        }, 0, 100, TimeUnit.MILLISECONDS)
        Thread.sleep(1000)
        f.cancel(false)
        assertEquals(v[0] > 8, true)
    }
}