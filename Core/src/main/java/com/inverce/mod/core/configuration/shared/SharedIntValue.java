package com.inverce.mod.core.configuration.shared;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SharedIntValue extends SharedValue<Integer> {
    SharedValueImpl<Integer> impl;

    public SharedIntValue(String key) {
        this(key, "im_shared", null);
    }

    public SharedIntValue(String key, Integer defaultValue) {
        this(key, "im_shared", defaultValue);
    }

    public SharedIntValue(String key, String storeFile) {
        this(key, storeFile, null);
    }

    public SharedIntValue(String key, String storeFile, Integer defaultValue) {
        super(Integer.class, key, storeFile, defaultValue);
    }
}
