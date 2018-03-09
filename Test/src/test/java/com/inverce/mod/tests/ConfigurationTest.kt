package com.inverce.mod.tests

import com.inverce.mod.core.configuration.Value
import com.inverce.mod.core.configuration.ValueChanged
import com.inverce.mod.core.configuration.extended.LazyWeakValue
import com.inverce.mod.core.configuration.primitive.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class ConfigurationTest {
    @Test // seriously if this ever fails we are f!!#$
    fun addition_isCorrect() {
        assertEquals(4, (2 + 2).toLong())
    }

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
    fun primitivevalue_returns_correctValue() {
        val bValue = BoolValue(true)
        assertEquals(bValue.get()!!, true)

        val dValue = DoubleValue(23.3)
        assertEquals(dValue.get()!!, 23.3, 0.1)

        val fValue = FloatValue(23.3f)
        assertEquals(fValue.get()!!, 23.3f)

        val iValue = IntValue(23)
        assertEquals(iValue.get()!!, 23)

        val lValue = LongValue(23L)
        assertEquals(lValue.get()!!, 23L)

        val sValue = StringValue("true")
        assertEquals(sValue.get()!!, "true")
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