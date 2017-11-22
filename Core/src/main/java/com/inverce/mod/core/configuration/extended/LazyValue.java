package com.inverce.mod.core.configuration.extended;

import com.inverce.mod.core.configuration.Value;
import com.inverce.mod.core.functional.ISupplier;

public class LazyValue<T> extends Value<T> {
    protected boolean isInitialized;
    protected T value;

    public LazyValue(ISupplier<T> initValue) {
        setValidator(p -> true);
        setSetter(p -> value = p);
        setGetter(() -> {
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
