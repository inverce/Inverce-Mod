package com.inverce.mod.tests

import com.inverce.mod.v2.core.configuration.LazyWeakValue
import com.inverce.mod.v2.core.configuration.Value
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class EventsTest {

    @Test
    fun weak_value_returns_value() {
        val d = LazyWeakValue({ 31L })
        val v = d.value
        assertEquals(v, 31L)
    }

    @Test
    fun value_returns_correctValue() {
        val value = Value("test")
        assertEquals(value.value, "test")
    }

    @Test
    fun valueChanged_called_once() {
        val value = Value("test")
        var changedCalled = 0
        value.onChanged += { p, v ->
            changedCalled++
        }
        value.value = "value 2"
        assertTrue(changedCalled == 1)
    }


}