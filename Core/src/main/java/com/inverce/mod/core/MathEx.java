package com.inverce.mod.core;

import android.graphics.PointF;
import android.support.annotation.ColorInt;
import android.util.Base64;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.max;
import static java.lang.Math.min;
import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

public final class MathEx {
    public static int lerp(int start, int end, float t) {
        return (int) (start + (end - start) * t);
    }

    public static float lerp(float start, float end, float t) {
        return start + (end - start) * t;
    }

    public static float[] lerp(float[] start, float[] end, float t) {
        float[] array = new float[start.length];
        for (int i = 0; i < array.length; i++) {
            float s = start[i];
            float e = end[i];
            array[i] = s + (t * (e - s));
        }
        return array;
    }

    public static PointF lerp(PointF start, PointF end, float t) {
        return new PointF(
                start.x + (t * (end.x - start.x)),
                start.y + (t * (end.y - start.y)));
    }

    @ColorInt
    public static int lerpColor(@ColorInt int start, @ColorInt int end, float t) {
        int sA = (start >> 24) & 0xff,  sR = (start >> 16) & 0xff;
        int eA = (end >> 24) & 0xff,    eR = (end >> 16) & 0xff;
        int sG = (start >> 8) & 0xff,   sB = start & 0xff;
        int eG = (end >> 8) & 0xff,     eB = end & 0xff;

        return (sA + (int)(t * (eA - sA))) << 24 |
                (sR + (int)(t * (eR - sR))) << 16 |
                (sG + (int)(t * (eG - sG))) << 8 |
                (sB + (int)(t * (eB - sB)));
    }


    public static float normalize(float start, float end, float value) {
        return (value - start) / (end - start);
    }

    public static float clamp(float value, float min, float max) {
        return min(max, max(min, value));
    }

    public static float distanceSquared(float x1, float x2, float y1, float y2) {
        return (float) (pow(x2 - x1, 2) + pow(y2 - y1, 2));
    }

    public static float distance(float x1, float x2, float y1, float y2) {
        return (float) sqrt(pow(x2 - x1, 2)  + pow(y2 - y1, 2));
    }

    public static String toBase64(String input) {
        return input == null ? null : Base64.encodeToString(input.getBytes(), Base64.NO_WRAP);
    }

    public static String toBase64(byte[] input) {
        return input == null ? null : Base64.encodeToString(input, Base64.NO_WRAP);
    }

    public static String fromBase64(String input) {
        return input == null ? null : new String(Base64.decode(input, Base64.NO_WRAP));
    }

    public static byte[] fromBase64Bytes(String input) {
        return input == null ? null : Base64.decode(input, Base64.NO_WRAP);
    }

    public static <E> List<List<E>> generatePermutations(List<E> original) {
        if (original.size() == 0) {
            List<List<E>> result = new ArrayList<>();
            result.add(new ArrayList<E>());
            return result;
        }
        E firstElement = original.remove(0);
        List<List<E>> returnValue = new ArrayList<>();
        List<List<E>> permutations = generatePermutations(original);
        for (List<E> smallerPermutated : permutations) {
            for (int index=0; index <= smallerPermutated.size(); index++) {
                List<E> temp = new ArrayList<>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }

    public static String decimFormat(String format, double val) {
        return new DecimalFormat(format).format(val);
    }
}
