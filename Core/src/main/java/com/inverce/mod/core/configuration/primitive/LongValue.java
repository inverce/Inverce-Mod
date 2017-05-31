package com.inverce.mod.core.configuration.primitive;

import com.inverce.mod.core.configuration.Value;
import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.functional.IPredicate;
import com.inverce.mod.core.functional.ISupplier;

public class LongValue extends Value<Long> {
    public LongValue() {
    }

    public LongValue(Long value) {
        super(value);
    }

    public LongValue(Long value, IPredicate<Long> validator) {
        super(value, validator);
    }

    public LongValue(ISupplier<Long> supplier, IConsumer<Long> setter) {
        super(supplier, setter);
    }

    public LongValue(ISupplier<Long> supplier, IConsumer<Long> setter, IPredicate<Long> validator) {
        super(supplier, setter, validator);
    }
}
