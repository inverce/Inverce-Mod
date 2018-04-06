package com.inverce.mod.core.configuration.extended;

import android.support.annotation.NonNull;

import com.inverce.mod.core.configuration.Value;
import com.inverce.mod.core.functional.ISupplier;

public class LazyValue<T> extends Value<T> {
    protected boolean isInitialized;

    public LazyValue(@NonNull ISupplier<T> initValue) {
        super(null);
        setGetter(() -> {
            T value = LazyValue.super.get();
            if (!isInitialized) {
                isInitialized = true;
                set(initValue.get());
            }
            return value;
        });
    }

    public boolean isInitialized() {
        return isInitialized;
    }
}
