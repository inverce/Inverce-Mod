package com.inverce.mod.tests

import android.graphics.Point
import com.inverce.mod.core.reflection.Reflection
import com.inverce.mod.core.reflection.TypeToken
import org.junit.Assert.*
import org.junit.Test

interface A
interface B : A
interface C
interface D : B, C
class TestReflect : D

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

    @Test
    fun reflect_returns_correct_parent_interfaces() {
        val c = Reflection.getImplementedInterfaces(TestReflect::class.java)

        assertArrayEquals(c.toTypedArray(), arrayOf(
                A::class.java, B::class.java, C::class.java, D::class.java
        ))
    }
}