package com.inverce.mod.core.utilities;

public class Util {
    public static <T> T nonNull(Object ... params) {
        for (Object t: params)
            if (t != null) return (T) t;
        return null;
    }

}
