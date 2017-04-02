package com.inverce.mod.core.configuration;


import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.functional.IPredicate;
import com.inverce.mod.core.functional.ISupplier;

public class BoolPreference extends ValuePreference<Boolean> {
    public BoolPreference() {
    }

    public BoolPreference(Boolean value) {
        super(value);
    }

    public BoolPreference(Boolean value, IPredicate<Boolean> validator) {
        super(value, validator);
    }

    public BoolPreference(ISupplier<Boolean> supplier, IConsumer<Boolean> setter) {
        super(supplier, setter);
    }

    public BoolPreference(ISupplier<Boolean> supplier, IConsumer<Boolean> setter, IPredicate<Boolean> validator) {
        super(supplier, setter, validator);
    }
}
