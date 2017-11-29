package com.inverce.mod.core.configuration.shared;

public class SharedBoolAutoToggle extends SharedBoolValue {
    protected boolean initialized;

    public SharedBoolAutoToggle(String key, boolean initialValue, boolean toggledValue) {
        super(key, initialValue);
        initialized = toggledValue;
    }

    public SharedBoolAutoToggle(String key, String store, boolean initialValue, boolean toggledValue) {
        super(key, store, initialValue);
        initialized = toggledValue;
    }

    @Override
    public Boolean get() {
        boolean get = super.get();
        if (get != initialized) {
            set(initialized);
        }
        return get;
    }
}
