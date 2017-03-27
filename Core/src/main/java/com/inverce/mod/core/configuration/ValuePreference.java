package com.inverce.mod.core.configuration;

import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.functional.IPredicate;
import com.inverce.mod.core.functional.ISupplier;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ValuePreference<T> extends ReadOnlyPreference<T> {
    private IPredicate<T> validator;
    private IConsumer<T> setter;

    private T simpleValue;

    protected ValuePreference() { }

    public ValuePreference(T value) {
        this(value, P -> true);
    }

    public ValuePreference(T value, IPredicate<T> validator) {
        setSetter(p -> simpleValue = p);
        setGetter(() -> simpleValue);
        setValidator(validator);
        set(value);
    }

    public ValuePreference(ISupplier<T> supplier, IConsumer<T> setter) {
        this(supplier, setter, p -> true);
    }

    public ValuePreference(ISupplier<T> supplier, IConsumer<T> setter, IPredicate<T> validator) {
        super(supplier);
        this.validator = validator;
        this.setter = setter;
    }

    public boolean set(T e) {
        if (validator.test(e)) {
            setter.consume(e);
            return true;
        }
        return false;
    }

    protected void setValidator(IPredicate<T> validator) {
        this.validator = validator;
    }

    protected void setSetter(IConsumer<T> setter) {
        this.setter = setter;
    }

    @Override
    public String toString() {
        return String.valueOf(get());
    }

    public ReadOnlyPreference<T> asReadOnly() {
        return new ReadOnlyPreference<>(this);
    }
}
