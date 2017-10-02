package com.inverce.mod.core.configuration.shared;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SharedLongValue extends SharedValue<Long> {

    public SharedLongValue(String key) {
        this(key, "im_shared", null);
    }

    public SharedLongValue(String key, Long defaultValue) {
        this(key, "im_shared", defaultValue);
    }

    public SharedLongValue(String key, String storeFile) {
        this(key, storeFile, null);
    }

    public SharedLongValue(String key, String storeFile, Long defaultValue) {
        super(Long.class, key, storeFile, defaultValue);
    }
}
