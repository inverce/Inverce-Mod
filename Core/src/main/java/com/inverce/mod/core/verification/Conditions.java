package com.inverce.mod.core.verification;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import java.lang.ref.WeakReference;
import java.util.Collection;
import java.util.Map;

@SuppressWarnings("unused")
public class Conditions {
    private Conditions() { }

    @SafeVarargs
    public static <T> T firstNonNull(@Nullable T value, @NonNull T ... values) {
        if (value != null) return value;
        for (T k: values) {
            if (k != null) {
                return k;
            }
        }
        return null;
    }

    public static boolean notNullOrEmpty(@Nullable String value) {
        return value != null && value.length() > 0;
    }

    public static boolean notNullOrEmpty(@Nullable WeakReference<?> value) {
        return value != null && value.get() != null;
    }

    public static <T> boolean notNullOrEmpty(@Nullable T [] value) {
        return value != null && value.length > 0;
    }

    public static boolean notNullOrEmpty(@Nullable Collection<?> value) {
        return value != null && value.size() > 0;
    }

    public static boolean notNullOrEmpty(@Nullable Cursor value) {
        return value != null && value.getCount() > 0;
    }

    public static boolean nullOrEmpty(@Nullable String value) {
        return value == null || value.length() == 0;
    }

    public static boolean nullOrEmpty(@Nullable WeakReference<?> value) {
        return value == null || value.get() == null;
    }

    public static <T> boolean nullOrEmpty(@Nullable T [] value) {
        return value == null || value.length == 0;
    }

    public static boolean nullOrEmpty(@Nullable Collection<?> value) {
        return value == null || value.size() == 0;
    }

    public static boolean nullOrEmpty(@Nullable Map<?, ?> value) {
        return value == null || value.size() == 0;
    }

    public static boolean nullOrEmpty(@Nullable Cursor value) {
        return value == null || value.getCount() == 0;
    }

    public static boolean isVisible(@Nullable View view) {
        return view != null && view.getVisibility() == View.VISIBLE;
    }
}
