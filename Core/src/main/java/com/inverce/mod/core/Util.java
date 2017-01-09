package com.inverce.mod.core;

public class Util {
    public static <T> T nonNull(Object ... params) {
        for (Object t: params)
            if (t != null) return (T) t;
        return null;
    }

}
