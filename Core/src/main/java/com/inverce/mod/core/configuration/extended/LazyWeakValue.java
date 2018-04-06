package com.inverce.mod.core.configuration.extended;

import android.support.annotation.NonNull;

import com.inverce.mod.core.functional.ISupplier;

public class LazyWeakValue<T> extends WeakValue<T> {
    ISupplier<T> initValue;

    public LazyWeakValue(@NonNull ISupplier<T> initValue) {
        super(null);
        this.initValue = initValue;
    }

    public boolean isInitialized() {
        return super.get() != null;
    }

    @Override
    public T get() {
        T v = super.get();
        if (v == null) {
            set(v = initValue.get());
        }
        return v;
    }
}
