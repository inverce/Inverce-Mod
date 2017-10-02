package com.inverce.mod.core.configuration.shared;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SharedBoolValue extends SharedValue<Boolean> {

    public SharedBoolValue(String key) {
        this(key, "im_shared", null);
    }

    public SharedBoolValue(String key, Boolean defaultValue) {
        this(key, "im_shared", defaultValue);
    }

    public SharedBoolValue(String key, String storeFile) {
        this(key, storeFile, null);
    }

    public SharedBoolValue(String key, String storeFile, Boolean defaultValue) {
        super(Boolean.class, key, storeFile, defaultValue);
    }
}
