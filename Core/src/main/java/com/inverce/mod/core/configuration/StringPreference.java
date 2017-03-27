package com.inverce.mod.core.configuration;


import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.functional.IPredicate;
import com.inverce.mod.core.functional.ISupplier;

public class StringPreference extends ValuePreference<String> {
    public StringPreference() {
    }

    public StringPreference(String value) {
        super(value);
    }

    public StringPreference(String value, IPredicate<String> validator) {
        super(value, validator);
    }

    public StringPreference(ISupplier<String> supplier, IConsumer<String> setter) {
        super(supplier, setter);
    }

    public StringPreference(ISupplier<String> supplier, IConsumer<String> setter, IPredicate<String> validator) {
        super(supplier, setter, validator);
    }
}
