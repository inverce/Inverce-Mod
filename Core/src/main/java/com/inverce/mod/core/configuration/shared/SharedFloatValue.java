package com.inverce.mod.core.configuration.shared;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SharedFloatValue extends SharedValue<Float> {
    SharedValueImpl<Float> impl;

    public SharedFloatValue(String key) {
        this(key, "im_shared", null);
    }

    public SharedFloatValue(String key, Float defaultValue) {
        this(key, "im_shared", defaultValue);
    }

    public SharedFloatValue(String key, String storeFile) {
        this(key, storeFile, null);
    }

    public SharedFloatValue(String key, String storeFile, Float defaultValue) {
        super(Float.class, key, storeFile, defaultValue);
    }
}
