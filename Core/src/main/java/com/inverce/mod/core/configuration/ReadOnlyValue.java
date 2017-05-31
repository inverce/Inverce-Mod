package com.inverce.mod.core.configuration;

import com.inverce.mod.core.functional.ISupplier;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ReadOnlyValue<T> {
    private ISupplier<T> supplier;

    protected ReadOnlyValue() { }

    public ReadOnlyValue(Value<T> value) {
        this(value::get);
    }

    public ReadOnlyValue(T value) {
        this(() -> value);
    }

    public ReadOnlyValue(ISupplier<T> supplier) {
        setGetter(supplier);
    }

    public T get() {
        return supplier.get();
    }

    protected void setGetter(ISupplier<T> supplier) {
        this.supplier = supplier;
    }
}
