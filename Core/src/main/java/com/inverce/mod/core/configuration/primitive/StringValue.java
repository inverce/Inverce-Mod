package com.inverce.mod.core.configuration.primitive;

import com.inverce.mod.core.configuration.Value;
import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.functional.IPredicate;
import com.inverce.mod.core.functional.ISupplier;

public class StringValue extends Value<String> {
    public StringValue() {
    }

    public StringValue(String value) {
        super(value);
    }

    public StringValue(String value, IPredicate<String> validator) {
        super(value, validator);
    }

    public StringValue(ISupplier<String> supplier, IConsumer<String> setter) {
        super(supplier, setter);
    }

    public StringValue(ISupplier<String> supplier, IConsumer<String> setter, IPredicate<String> validator) {
        super(supplier, setter, validator);
    }
}
