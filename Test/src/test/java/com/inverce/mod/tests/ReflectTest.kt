package com.inverce.mod.tests

import android.graphics.Point
import com.inverce.mod.core.reflection.TypeToken
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test


class ReflectTest {

    @Test
    fun reflect_returns_correctClass() {
        val test = object: TypeToken<Point>() { }
        assertTrue(test.type == Point::class.java)
    }

    @Test
    fun reflect_returns_correctTypedClass() {
        val test = TypeToken.getParameterized(List::class.java, Integer::class.java)
        assertEquals(test.toString(), "java.util.List<java.lang.Integer>")
    }
}