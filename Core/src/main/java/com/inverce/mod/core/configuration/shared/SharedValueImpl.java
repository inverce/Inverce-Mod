package com.inverce.mod.core.configuration.shared;

import android.content.Context;
import android.content.SharedPreferences;

import com.inverce.mod.core.IM;

@SuppressWarnings("WeakerAccess")
abstract class SharedValueImpl<T> {
    protected String key;
    protected String storeFile;
    protected T defaultValue;

    public void with(String key, String storeFile, Object defaultValue) {
        this.key = key;
        this.storeFile = storeFile;
        //noinspection unchecked
        this.defaultValue = (T) defaultValue;
    }

    String getKey() {
        return key;
    }

    protected T defaultOr(T value) {
        return defaultValue != null ? defaultValue : value;
    }

    public abstract T getValue();
    public abstract void setValue(T value);

    protected SharedPreferences store() {
        return IM.context().getSharedPreferences(storeFile, Context.MODE_PRIVATE);
    }

    static class Bool extends SharedValueImpl<Boolean> {
        @Override
        public Boolean getValue() {
            return store().getBoolean(key, defaultOr(false));
        }
        @Override
        public void setValue(Boolean value) {
            store().edit().putBoolean(key, value).apply();
        }
    }

    static class Int extends SharedValueImpl<Integer> {
        @Override
        public Integer getValue() {
            return store().getInt(key, defaultOr(0));
        }
        @Override
        public void setValue(Integer value) {
            store().edit().putInt(key, value).apply();
        }
    }

    static class LongInt extends SharedValueImpl<Long> {
        @Override
        public Long getValue() {
            return store().getLong(key, defaultOr(0L));
        }

        @Override
        public void setValue(Long value) {
            store().edit().putLong(key, value).apply();
        }
    }

    static class Floating extends SharedValueImpl<Float> {
        @Override
        public Float getValue() {
            return store().getFloat(key, defaultOr(0f));
        }

        @Override
        public void setValue(Float value) {
            store().edit().putFloat(key, value).apply();
        }
    }

    static class Text extends SharedValueImpl<String> {
        @Override
        public String getValue() {
            return store().getString(key, defaultOr(""));
        }

        @Override
        public void setValue(String value) {
            store().edit().putString(key, value).apply();
        }
    }
}
