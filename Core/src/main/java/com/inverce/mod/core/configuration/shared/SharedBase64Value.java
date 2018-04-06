package com.inverce.mod.core.configuration.shared;

import android.support.annotation.Nullable;

import static com.inverce.mod.core.MathEx.fromBase64;
import static com.inverce.mod.core.MathEx.toBase64;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SharedBase64Value extends SharedValue<String> {

    public SharedBase64Value(String key) {
        this(key, "im_shared", null);
    }

    public SharedBase64Value(String key, String defaultValue) {
        this(key, "im_shared", defaultValue);
    }

    public SharedBase64Value(String key, String storeFile, String defaultValue) {
        super(String.class, key, storeFile, defaultValue);
    }

    @Nullable
    @Override
    public String get() {
        return fromBase64(super.get());
    }

    @Override
    public boolean set(String e) {
        return super.set(toBase64(e));
    }
}
