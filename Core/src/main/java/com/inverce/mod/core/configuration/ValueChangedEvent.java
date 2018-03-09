package com.inverce.mod.core.configuration;

import android.support.annotation.NonNull;

/**
 * Created on 23/02/2018.
 */
public interface ValueChangedEvent<T> {
    void addListener(@NonNull T listener);

    void removeListener(T listener);

    void clear();
}
