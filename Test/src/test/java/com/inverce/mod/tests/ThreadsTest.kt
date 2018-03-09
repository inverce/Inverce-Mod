package com.inverce.mod.tests

import com.inverce.mod.core.threadpool.DynamicScheduledExecutor
import org.junit.Assert.assertEquals
import org.junit.Test
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


}