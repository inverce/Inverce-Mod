package com.inverce.mod.core.collections;

import android.support.annotation.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Created on 09/01/2018.
 */
public class MapMaker<K, V, T extends Map<K, V>> {
    T container;

    public MapMaker(T container) {
        this.container = container;
    }

    @NonNull
    public static <K, V, T extends Map<K, V>> MapMaker<K, V, T> New(@NonNull T container, K key, V value) {
        return new MapMaker<>(container).put(key, value);
    }

    @NonNull
    public static <K, V> MapMaker<K, V, HashMap<K, V>> New(K key, V value) {
        return New(new HashMap<K, V>(), key, value);
    }

    @SafeVarargs
    public static <K> WithKeys<K> keys(K... keys) {
        return new WithKeys<>(keys);
    }

    @NonNull
    public MapMaker<K, V, T> put(K key, V value) {
        container.put(key, value);
        return this;
    }

    public T build() {
        return container;
    }

    public static class WithKeys<K> {
        private final K[] keys;

        WithKeys(K[] keys) {
            this.keys = keys;
        }

        @SafeVarargs
        public final <V> MapMaker<K, V, HashMap<K, V>> vals(@NonNull V... vals) {
            if (keys.length != vals.length) {
                throw new IllegalArgumentException("Expected: " + keys.length + " values, received: " + vals.length);
            }
            HashMap<K, V> map = new HashMap<>();
            for (int i = 0; i < keys.length; i++) {
                map.put(keys[i], vals[i]);
            }
            return new MapMaker<>(map);
        }
    }
}
