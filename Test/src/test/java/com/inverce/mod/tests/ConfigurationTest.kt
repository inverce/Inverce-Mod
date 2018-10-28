package com.inverce.mod.tests

import com.inverce.mod.v2.core.configuration.LazyWeakValue
import com.inverce.mod.v2.core.configuration.Value
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class ConfigurationTest {
    @Test // seriously if this ever fails we are f!!#$
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

    @Test // seriously if this ever fails we are f!!#$
    fun value_delegate_changes_value() {
        assertEquals(4, (2 + 2).toLong())
        val vad = Value(2)
        var d by vad
        d = 2
        vad.value = 32
        assertEquals(d, 32)
    }

    @Test
    fun weakvalue_returns_value() {
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
    fun primitivevalue_returns_correctValue() {
        val bValue = Value(true)
        assertEquals(bValue.value, true)

        val dValue = Value(23.3)
        assertEquals(dValue.value, 23.3, 0.1)

        val fValue = Value(23.3f)
        assertEquals(fValue.value, 23.3f)

        val iValue = Value(23)
        assertEquals(iValue.value, 23)

        val lValue = Value(23L)
        assertEquals(lValue.value, 23L)

        val sValue = Value("true")
        assertEquals(sValue.value, "true")
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