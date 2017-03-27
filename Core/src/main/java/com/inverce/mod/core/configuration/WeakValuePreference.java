package com.inverce.mod.core.configuration;

import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.functional.IPredicate;

import java.lang.ref.WeakReference;

@SuppressWarnings({"unused", "WeakerAccess"})
public class WeakValuePreference<T> extends ValuePreference<T> {
    private IPredicate<T> validator;
    private IConsumer<T> setter;

    private WeakReference<T> simpleValue;

    protected WeakValuePreference() { }

    public WeakValuePreference(T value) {
        this(value, P -> true);
    }

    public WeakValuePreference(T value, IPredicate<T> validator) {
        setSetter(p -> simpleValue = new WeakReference<>(p));
        setGetter(() -> simpleValue.get());
        setValidator(validator);
        set(value);
    }

}
