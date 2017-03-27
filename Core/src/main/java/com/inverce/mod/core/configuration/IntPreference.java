package com.inverce.mod.core.configuration;


import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.functional.IPredicate;
import com.inverce.mod.core.functional.ISupplier;

public class IntPreference extends ValuePreference<Integer> {
    public IntPreference() {
    }

    public IntPreference(Integer value) {
        super(value);
    }

    public IntPreference(Integer value, IPredicate<Integer> validator) {
        super(value, validator);
    }

    public IntPreference(ISupplier<Integer> supplier, IConsumer<Integer> setter) {
        super(supplier, setter);
    }

    public IntPreference(ISupplier<Integer> supplier, IConsumer<Integer> setter, IPredicate<Integer> validator) {
        super(supplier, setter, validator);
    }
}
