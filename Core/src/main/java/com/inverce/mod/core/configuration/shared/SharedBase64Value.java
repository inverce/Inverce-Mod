package com.inverce.mod.core.configuration.shared;

import android.util.Base64;

public class SharedBase64Value extends SharedStringValue {
    public SharedBase64Value(String key) {
        super(key);
    }

    public SharedBase64Value(String key, String defaultValue) {
        super(key, defaultValue);
    }

    public SharedBase64Value(String key, String storeFile, String defaultValue) {
        super(key, storeFile, defaultValue);
    }

    @Override
    public String get() {
        return new String(Base64.decode(super.get(), Base64.NO_WRAP));
    }

    @Override
    public boolean set(String e) {
        return super.set(Base64.encodeToString(e.getBytes(), Base64.NO_WRAP));
    }
}
