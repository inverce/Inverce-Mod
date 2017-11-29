package com.inverce.mod.core.configuration.extended;

import com.inverce.mod.core.functional.ISupplier;

public class LazyWeakValue<T> extends WeakValue<T> {

    public LazyWeakValue(ISupplier<T> initValue) {
        super(null);
        setGetter(() -> {
            T value = LazyWeakValue.super.get();
            if (value == null) {
                set(initValue.get());
            }
            return value;
        });
    }

    public boolean isInitialized() {
        return LazyWeakValue.super.get() != null;
    }
}
