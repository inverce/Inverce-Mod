package com.inverce.utils.tools;

@SuppressWarnings("unused")
public class MathEx {

    public static int compare(int lhs, int rhs) {
        return lhs < rhs ? -1 : (lhs == rhs ? 0 : 1);
    }

    public static float clamp(float lover, float upper, float value) {
        return value < lover ? lover : value > upper ? upper : value;
    }

    public static int clamp(int lover, int upper, int value) {
        return value < lover ? lover : value > upper ? upper : value;
    }

    public static boolean within(int val, int min, int max) {
        return val >= min && val <= max;
    }

    public static float vectorLength(float[] data) {
        float pow = 0;
        for (float f : data) {
            pow += f * f;
        }
        return (float) Math.sqrt(pow);
    }
}
