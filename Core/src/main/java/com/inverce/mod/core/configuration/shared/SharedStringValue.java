package com.inverce.mod.core.configuration.shared;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SharedStringValue extends SharedValue<String> {
    SharedValueImpl<String> impl;

    public SharedStringValue(String key) {
        this(key, "im_shared", null);
    }

    public SharedStringValue(String key, String defaultValue) {
        this(key, "im_shared", defaultValue);
    }

    public SharedStringValue(String key, String storeFile, String defaultValue) {
        super(String.class, key, storeFile, defaultValue);
    }
}
