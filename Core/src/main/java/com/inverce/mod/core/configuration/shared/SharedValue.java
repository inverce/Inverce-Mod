package com.inverce.mod.core.configuration.shared;

import android.support.annotation.NonNull;

import com.inverce.mod.core.configuration.Value;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SharedValue<T> extends Value<T> {
    protected SharedValueImpl<T> impl;

    public SharedValue(@NonNull Class<T> clazz, String key) {
        this(clazz, key, "im_shared", null);
    }

    public SharedValue(@NonNull Class<T> clazz, String key, T defaultValue) {
        this(clazz, key, "im_shared", defaultValue);
    }

    public SharedValue(@NonNull Class<T> clazz, String key, String storeFile) {
        this(clazz, key, storeFile, null);
    }

    public SharedValue(@NonNull Class<T> clazz, String key, String storeFile, T defaultValue) {
        this.impl = implementationForClass(clazz.getName());
        this.impl.with(key, storeFile, defaultValue);
        setSetter(this.impl::setValue);
        setGetter(this.impl::getValue);
        setValidator(p -> p != null);
    }

    @NonNull
    private SharedValueImpl<T> implementationForClass(@NonNull String clazz) {
        SharedValueImpl<?> impl;
        switch (clazz) {
            case "java.lang.Boolean":
                impl = new SharedValueImpl.Bool(); break;
            case "java.lang.Float":
                impl = new SharedValueImpl.Floating(); break;
            case "java.lang.Integer":
                impl = new SharedValueImpl.Int(); break;
            case "java.lang.Long":
                impl = new SharedValueImpl.LongInt(); break;
            case "java.lang.String":
                impl = new SharedValueImpl.Text(); break;
            default: throw new UnsupportedOperationException("This type is unsupported");
        }
        //noinspection unchecked // in this context its safe cast
        return (SharedValueImpl<T>) impl;
    }

    public String getKey() {
        return impl.getKey();
    }

}
