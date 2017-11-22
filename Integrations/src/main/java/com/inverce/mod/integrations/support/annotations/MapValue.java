package com.inverce.mod.integrations.support.annotations;

import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.StringRes;

public interface MapValue<T, Y> {
    Y get(T item);

    interface ToStringRes<T> {
        @StringRes
        int get(T item);
    }

    interface ToDrawableRes<T> {
        @DrawableRes
        int get(T item);
    }

    interface ToDrawable<T> {
        Drawable get(T item);
    }
}
