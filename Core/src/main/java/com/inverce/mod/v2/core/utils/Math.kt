package com.inverce.mod.v2.core.utils

import android.graphics.PointF
import android.support.annotation.ColorInt
import android.util.Base64
import com.inverce.mod.v2.core.verification.nullPass
import java.lang.Math.*
import java.text.DecimalFormat
import java.util.*

object MathEx {
    fun lerp(start: Int, end: Int, t: Float): Int {
        return (start + (end - start) * t).toInt()
    }

    fun lerp(start: Float, end: Float, t: Float): Float {
        return start + (end - start) * t
    }

    fun lerp(start: FloatArray, end: FloatArray, t: Float): FloatArray {
        val array = FloatArray(start.size)
        for (i in array.indices) {
            val s = start[i]
            val e = end[i]
            array[i] = s + t * (e - s)
        }
        return array
    }

    fun lerp(start: PointF, end: PointF, t: Float): PointF {
        return PointF(lerp(start.x, end.x, t), lerp(start.y, end.y, t))
    }

    @Suppress("NOTHING_TO_INLINE") // we inline to make kotlin marge bit wise operation for us
    private inline fun lerpBytes(start: Int, end: Int, t: Float, pos: Int): Int {
        val sA = start shr pos and 0xff
        val eA = end shr pos and 0xff
        return sA + (t * (eA - sA)).toInt() and 0xFF shl pos
    }


    @ColorInt
    fun lerpColor(@ColorInt start: Int, @ColorInt end: Int, t: Float): Int {
        return lerpBytes(start, end, t, 24) or
                lerpBytes(start, end, t, 16) or
                lerpBytes(start, end, t, 8) or
                lerpBytes(start, end, t, 0)
    }


    fun normalize(start: Float, end: Float, value: Float): Float {
        return (value - start) / (end - start)
    }

    fun clamp(value: Float, min: Float, max: Float): Float {
        return min(max, max(min, value))
    }

    fun distanceSquared(x1: Float, x2: Float, y1: Float, y2: Float): Float {
        return (pow((x2 - x1).toDouble(), 2.0) + pow((y2 - y1).toDouble(), 2.0)).toFloat()
    }

    fun distance(x1: Float, x2: Float, y1: Float, y2: Float): Float {
        return sqrt(pow((x2 - x1).toDouble(), 2.0) + pow((y2 - y1).toDouble(), 2.0)).toFloat()
    }

    fun toBase64(input: String?): String? = nullPass(input, { Base64.encodeToString(it.toByteArray(), Base64.NO_WRAP) })
    fun toBase64(input: ByteArray?): String? = nullPass(input, { Base64.encodeToString(it, Base64.NO_WRAP) })
    fun fromBase64(input: String?): String? = nullPass(input, { String(Base64.decode(it, Base64.NO_WRAP)) })
    fun fromBase64Bytes(input: String?): ByteArray? = nullPass(input, { Base64.decode(it, Base64.NO_WRAP) })

    fun <E> generatePermutations(original: MutableList<E>): List<List<E>> {
        if (original.size == 0) {
            val result = ArrayList<List<E>>()
            result.add(ArrayList())
            return result
        }
        val firstElement = original.removeAt(0)
        val returnValue = ArrayList<List<E>>()
        val permutations = generatePermutations(original)
        for (smallerPermutated in permutations) {
            for (index in 0..smallerPermutated.size) {
                val temp = ArrayList(smallerPermutated)
                temp.add(index, firstElement)
                returnValue.add(temp)
            }
        }
        return returnValue
    }

    fun decimFormat(format: String, `val`: Double): String {
        return DecimalFormat(format).format(`val`)
    }
}