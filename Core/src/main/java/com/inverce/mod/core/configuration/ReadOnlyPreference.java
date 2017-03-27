package com.inverce.mod.core.configuration;

import com.inverce.mod.core.functional.ISupplier;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ReadOnlyPreference<T> {
    private ISupplier<T> supplier;

    protected ReadOnlyPreference() { }

    public ReadOnlyPreference(ValuePreference<T> value) {
        this(value::get);
    }

    public ReadOnlyPreference(T value) {
        this(() -> value);
    }

    public ReadOnlyPreference(ISupplier<T> supplier) {
        setGetter(supplier);
    }

    public T get() {
        return supplier.get();
    }

    protected void setGetter(ISupplier<T> supplier) {
        this.supplier = supplier;
    }
}
