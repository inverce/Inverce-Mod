package com.inverce.mod.core.configuration;

@SuppressWarnings({"unused", "WeakerAccess"})
public class SharedPreference<T> extends ValuePreference<T> {
    SharedPreferenceImpl<T> impl;

    public SharedPreference(Class<T> clazz, String key, String storeFile) {
        this(clazz, key, storeFile, null);
    }

    public SharedPreference(Class<T> clazz, String key, String storeFile, T defaultValue) {
        this.impl = implementationForClass(clazz.getName());
        this.impl.with(key, storeFile, defaultValue);
        setSetter(this.impl::setValue);
        setGetter(this.impl::getValue);
        setValidator(p -> p != null);
    }

    private SharedPreferenceImpl<T> implementationForClass(String clazz) {
        SharedPreferenceImpl<?> impl;
        switch (clazz) {
            case "java.lang.Boolean":
                impl = new SharedPreferenceImpl.Bool(); break;
            case "java.lang.Float":
                impl = new SharedPreferenceImpl.Floating(); break;
            case "java.lang.Integer":
                impl = new SharedPreferenceImpl.Int(); break;
            case "java.lang.Long":
                impl = new SharedPreferenceImpl.LongInt(); break;
            case "java.lang.String":
                impl = new SharedPreferenceImpl.Text(); break;
            default: throw new UnsupportedOperationException("This type is unsupported");
        }
        //noinspection unchecked // in this context its safe cast
        return (SharedPreferenceImpl<T>) impl;
    }

    public String getKey() {
        return impl.getKey();
    }

}
