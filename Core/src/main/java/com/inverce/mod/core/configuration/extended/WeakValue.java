package com.inverce.mod.core.configuration.extended;

import com.inverce.mod.core.configuration.Value;
import com.inverce.mod.core.functional.IPredicate;

import java.lang.ref.WeakReference;

@SuppressWarnings({"unused", "WeakerAccess"})
public class WeakValue<T> extends Value<T> {
    protected WeakReference<T> weakValue;

    protected WeakValue() {
        this(null);
    }

    public WeakValue(T value) {
        this(value, P -> true);
    }

    public WeakValue(T value, IPredicate<T> validator) {
        setSetter(p -> weakValue = new WeakReference<>(p));
        setGetter(() -> weakValue.get());
        setValidator(validator);
        set(value);
    }
}
