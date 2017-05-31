package com.inverce.mod.core.configuration.primitive;

import com.inverce.mod.core.configuration.Value;
import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.functional.IPredicate;
import com.inverce.mod.core.functional.ISupplier;

public class IntValue extends Value<Integer> {
    public IntValue() {
    }

    public IntValue(Integer value) {
        super(value);
    }

    public IntValue(Integer value, IPredicate<Integer> validator) {
        super(value, validator);
    }

    public IntValue(ISupplier<Integer> supplier, IConsumer<Integer> setter) {
        super(supplier, setter);
    }

    public IntValue(ISupplier<Integer> supplier, IConsumer<Integer> setter, IPredicate<Integer> validator) {
        super(supplier, setter, validator);
    }
}
