package com.inverce.mod.core.configuration.extended;

import com.inverce.mod.core.configuration.primitive.BoolValue;

public class BoolValueAutoToggle extends BoolValue {
    boolean toggled;

    public BoolValueAutoToggle(boolean initialValue, boolean toggledValue) {
        super(initialValue);
        toggled = toggledValue;
    }

    @Override
    public Boolean get() {
        boolean get = super.get();
        if (get != toggled) {
            set(toggled);
        }
        return get;
    }
}