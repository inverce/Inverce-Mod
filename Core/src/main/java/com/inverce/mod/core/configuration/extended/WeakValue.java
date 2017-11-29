package com.inverce.mod.core.configuration.extended;

import com.inverce.mod.core.configuration.Value;
import com.inverce.mod.core.functional.IPredicate;

import java.lang.ref.WeakReference;

@SuppressWarnings({"unused", "WeakerAccess"})
public class WeakValue<T> extends Value<T> {
    protected WeakReference<T> simpleValue;

    protected WeakValue() { }

    public WeakValue(T value) {
        this(value, P -> true);
    }

    public WeakValue(T value, IPredicate<T> validator) {
        setSetter(p -> simpleValue = new WeakReference<>(p));
        setGetter(() -> simpleValue.get());
        setValidator(validator);
        set(value);
    }
}
