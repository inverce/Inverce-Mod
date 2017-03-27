package com.inverce.mod.core.configuration;


import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.functional.IPredicate;
import com.inverce.mod.core.functional.ISupplier;

public class BooleanPreference extends ValuePreference<Boolean> {
    public BooleanPreference() {
    }

    public BooleanPreference(Boolean value) {
        super(value);
    }

    public BooleanPreference(Boolean value, IPredicate<Boolean> validator) {
        super(value, validator);
    }

    public BooleanPreference(ISupplier<Boolean> supplier, IConsumer<Boolean> setter) {
        super(supplier, setter);
    }

    public BooleanPreference(ISupplier<Boolean> supplier, IConsumer<Boolean> setter, IPredicate<Boolean> validator) {
        super(supplier, setter, validator);
    }
}
