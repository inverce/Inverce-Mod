package com.inverce.mod.core.configuration;

import com.inverce.mod.core.configuration.function.IConsumer;
import com.inverce.mod.core.configuration.function.IPredicate;
import com.inverce.mod.core.configuration.function.ISupplier;

public class ConfigValue<T> {
    T value;
    boolean allowChange;

    IPredicate<T> validator = new IPredicate.TRUE<>();
    ISupplier<T> supplier;
    IConsumer<T> setter;

    public T get() {
        return supplier.supply();
    }

    public void set(T e) {
        if (validator.test(e)) {
            setter.consume(e);
        }
    }


}
