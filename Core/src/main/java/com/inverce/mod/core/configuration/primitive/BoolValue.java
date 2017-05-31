package com.inverce.mod.core.configuration.primitive;

import com.inverce.mod.core.configuration.Value;
import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.functional.IPredicate;
import com.inverce.mod.core.functional.ISupplier;

public class BoolValue extends Value<Boolean> {
    public BoolValue() {
    }

    public BoolValue(Boolean value) {
        super(value);
    }

    public BoolValue(Boolean value, IPredicate<Boolean> validator) {
        super(value, validator);
    }

    public BoolValue(ISupplier<Boolean> supplier, IConsumer<Boolean> setter) {
        super(supplier, setter);
    }

    public BoolValue(ISupplier<Boolean> supplier, IConsumer<Boolean> setter, IPredicate<Boolean> validator) {
        super(supplier, setter, validator);
    }
}
