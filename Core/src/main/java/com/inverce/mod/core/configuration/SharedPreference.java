package com.inverce.mod.core.configuration;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.IntDef;

import com.inverce.mod.core.IM;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

import static com.inverce.mod.core.configuration.SharedPreference.Type.BOOLEAN;
import static com.inverce.mod.core.configuration.SharedPreference.Type.FLOAT;
import static com.inverce.mod.core.configuration.SharedPreference.Type.INTEGER;
import static com.inverce.mod.core.configuration.SharedPreference.Type.LONG;
import static com.inverce.mod.core.configuration.SharedPreference.Type.STRING;
import static com.inverce.mod.core.configuration.SharedPreference.Type.STRING_SET;


@SuppressWarnings({"unused", "WeakerAccess"})
public class SharedPreference<T> extends ValuePreference<T> {
    private String key;
    private String storeFile;
    private @Type int preferenceType;
    private T defaultValue = null;

    public SharedPreference(Class<T> clazz, String key, String storeFile, T defaultValue) {
        this(clazz, key, storeFile);
        this.defaultValue = defaultValue;
    }

    public SharedPreference(Class<T> clazz, String key, String storeFile) {
        setSetter(this::internalSetValue);
        setGetter(this::internalGetValue);
        setValidator(p -> true);

        this.key = key;
        this.storeFile = storeFile;

        switch (clazz.getName()) {
            case "java.lang.Boolean":   preferenceType = BOOLEAN; break;
            case "java.lang.Float":     preferenceType = FLOAT; break;
            case "java.lang.Integer":   preferenceType = INTEGER; break;
            case "java.lang.Long":      preferenceType = LONG; break;
            case "java.util.Set":       preferenceType = STRING_SET; break;
            case "java.lang.String":    preferenceType = STRING; break;
            default: throw new UnsupportedOperationException("This type is unsupported");
        }
    }

    public String getKey() {
        return key;
    }

    @Type
    public int getPreferenceType() {
        return preferenceType;
    }

    private SharedPreferences store() {
        return IM.context().getSharedPreferences(storeFile, Context.MODE_PRIVATE);
    }

    @SuppressWarnings("unchecked") // user is the one to check correctness
    protected T internalGetValue() {
        Object value = null;
        switch (preferenceType) {
            case BOOLEAN:      value = store().getBoolean(key, (Boolean) getDefaultValue()); break;
            case FLOAT:        value = store().getFloat(key, (Float) getDefaultValue()); break;
            case INTEGER:      value = store().getInt(key, (Integer) getDefaultValue()); break;
            case LONG:         value = store().getLong(key, (Long) getDefaultValue()); break;
            case STRING_SET:   value = store().getStringSet(key, (Set<String>) getDefaultValue()); break;
            case STRING:       value = store().getString(key, (String) getDefaultValue()); break;
        }
        return (T) value;
    }

    protected void internalSetValue(T value) {
        switch (preferenceType) {
            case BOOLEAN:
                store().edit()
                        .putBoolean(key, (Boolean) value)
                        .apply();
                break;
            case FLOAT:
                store().edit()
                        .putFloat(key, (Float) value)
                        .apply();
                break;
            case INTEGER:
                store().edit()
                        .putInt(key, (Integer) value)
                        .apply();
                break;
            case LONG:
                store().edit()
                        .putLong(key, (Long) value)
                        .apply();
                break;
            case STRING_SET:
                store().edit()
                        .putStringSet(key, (Set<String>) value)
                        .apply();
                break;
            case STRING:
                store().edit()
                        .putString(key, (String) value)
                        .apply();
                break;
        }
    }

    @SuppressWarnings("unchecked")
    protected T getDefaultValue() {
        switch (preferenceType) {
            case BOOLEAN:
                return defaultValue != null ? defaultValue : (T) (Object) false;
            case Type.FLOAT:
                return defaultValue != null ? defaultValue : (T) (Object) 0f;
            case Type.INTEGER:
                return defaultValue != null ? defaultValue : (T) (Object) 0;
            case Type.LONG:
                return defaultValue != null ? defaultValue : (T) (Object) 0L;
            default: return defaultValue;
        }
    }

    @IntDef({BOOLEAN, INTEGER, LONG, FLOAT, STRING, STRING_SET})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Type {
        int BOOLEAN = 0;
        int INTEGER = 1;
        int LONG = 2;
        int FLOAT = 3;
        int STRING = 4;
        int STRING_SET = 5;
    }
}
