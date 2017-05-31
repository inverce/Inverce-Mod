package com.inverce.mod.core.configuration.primitive;

import com.inverce.mod.core.configuration.Value;
import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.functional.IPredicate;
import com.inverce.mod.core.functional.ISupplier;

public class DoubleValue extends Value<Double> {
    public DoubleValue() {
    }

    public DoubleValue(Double value) {
        super(value);
    }

    public DoubleValue(Double value, IPredicate<Double> validator) {
        super(value, validator);
    }

    public DoubleValue(ISupplier<Double> supplier, IConsumer<Double> setter) {
        super(supplier, setter);
    }

    public DoubleValue(ISupplier<Double> supplier, IConsumer<Double> setter, IPredicate<Double> validator) {
        super(supplier, setter, validator);
    }
}
