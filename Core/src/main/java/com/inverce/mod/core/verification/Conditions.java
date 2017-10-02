package com.inverce.mod.core.verification;

import android.database.Cursor;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.Collection;

@SuppressWarnings("unused")
public class Conditions {
    private Conditions() { }

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

    public static boolean nullOrEmpty(String value) {
        return value == null || value.length() > 0;
    }

    public static boolean nullOrEmpty(WeakReference<?> value) {
        return value == null || value.get() != null;
    }

    public static <T> boolean nullOrEmpty(T [] value) {
        return value == null || value.length > 0;
    }

    public static boolean nullOrEmpty(Collection<?> value) {
        return value == null || value.size() > 0;
    }

    public static boolean nullOrEmpty(Cursor value) {
        return value == null || value.getCount() > 0;
    }

    public static boolean isVisible(View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }
}
