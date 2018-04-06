package com.inverce.mod.core.configuration;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.inverce.mod.core.functional.ISupplier;

@SuppressWarnings({"WeakerAccess", "unused"})
public class ReadOnlyValue<T> {
    protected ISupplier<T> supplier;

    protected ReadOnlyValue() { }

    public ReadOnlyValue(@NonNull Value<T> value) {
        this(value::get);
    }

    public ReadOnlyValue(T value) {
        this(() -> value);
    }

    public ReadOnlyValue(ISupplier<T> supplier) {
        setGetter(supplier);
    }

    @Nullable
    public T get() {
        return supplier != null ? supplier.get() : null;
    }

    protected void setGetter(ISupplier<T> supplier) {
        this.supplier = supplier;
    }

    @NonNull
    @Override
    public String toString() {
        return "Value: " + String.valueOf(get());
    }
}
