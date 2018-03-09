package com.inverce.mod.tests

import com.inverce.mod.core.configuration.Value
import com.inverce.mod.core.configuration.ValueChanged
import com.inverce.mod.core.configuration.extended.LazyWeakValue
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class EventsTest {

    @Test
    fun weakvalue_returns_value() {
        val d = LazyWeakValue<Long> { 31L }
        val v = d.get()
        assertEquals(v as Long, 31L)
    }

    @Test
    fun value_returns_correctValue() {
        val value = Value("test")
        assertEquals(value.get()!!, "test")
    }

    @Test
    fun valueChanged_called_once() {
        val value = Value("test")
        var changedCalled = 0
        value.changeValueEvent().addListener(ValueChanged<String> { p, v ->
            changedCalled ++
        })
        value.set("value 2")
        assertTrue(changedCalled == 1)
    }
}