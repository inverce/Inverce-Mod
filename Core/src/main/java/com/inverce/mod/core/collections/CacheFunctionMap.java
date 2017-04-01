package com.inverce.mod.core.collections;

import android.support.annotation.NonNull;

import com.inverce.mod.core.functional.IFunction;
import com.inverce.mod.core.reflection.TypeToken;

import java.util.HashMap;

public class CacheFunctionMap<Key, Value> extends HashMap<Key, Value> {
    private IFunction<Key, Value> generator;
    private Class<Key> keyClass;

    public CacheFunctionMap(@NonNull IFunction<Key, Value> generator) {
        //noinspection unchecked
        this.keyClass = (Class<Key>) new TypeToken<Key>() {}.getRawType();
        this.generator = generator;
    }

    public CacheFunctionMap(Class<Key> keyClass, IFunction<Key, Value> generator) {
        this.generator = generator;
        this.keyClass = keyClass;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Value get(Object key) {
        Value value = super.get(key);

        if (value == null && keyClass.isInstance(key)) {
            value = generator.apply((Key)key);
            put((Key) key, value);
        }

        return value;
    }
}
