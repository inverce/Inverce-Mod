package com.inverce.mod.core.configuration.primitive;

import com.inverce.mod.core.configuration.Value;
import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.functional.IPredicate;
import com.inverce.mod.core.functional.ISupplier;

public class FloatValue extends Value<Float> {
    public FloatValue() {
    }

    public FloatValue(Float value) {
        super(value);
    }

    public FloatValue(Float value, IPredicate<Float> validator) {
        super(value, validator);
    }

    public FloatValue(ISupplier<Float> supplier, IConsumer<Float> setter) {
        super(supplier, setter);
    }

    public FloatValue(ISupplier<Float> supplier, IConsumer<Float> setter, IPredicate<Float> validator) {
        super(supplier, setter, validator);
    }
}
