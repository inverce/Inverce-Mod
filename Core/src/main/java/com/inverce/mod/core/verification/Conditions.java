package com.inverce.mod.core.verification;

import android.database.Cursor;

import java.lang.ref.WeakReference;
import java.util.Collection;

@SuppressWarnings("unused")
public class Conditions {
    private Conditions() { }

    public static boolean nonNull(Object value) {
        return value != null;
    }

    public static boolean nonNull(Object ... value) {
        for (Object k: value) {
            if (k == null) {
                return false;
            }
        }
        return true;
    }

    @SafeVarargs
    public static <T> T firstNonNull(T value, T ... values) {
        if (value != null) return value;
        for (T k: values) {
            if (k != null) {
                return k;
            }
        }
        return null;
    }

    public static boolean notNullOrEmpty(String value) {
        return value != null && value.length() > 0;
    }

    public static boolean notNullOrEmpty(WeakReference<?> value) {
        return value != null && value.get() != null;
    }

    public static <T> boolean notNullOrEmpty(T [] value) {
        return value != null && value.length > 0;
    }

    public static boolean notNullOrEmpty(Collection<?> value) {
        return value != null && value.size() > 0;
    }

    public static boolean notNullOrEmpty(Cursor value) {
        return value != null && value.getCount() > 0;
    }


}
