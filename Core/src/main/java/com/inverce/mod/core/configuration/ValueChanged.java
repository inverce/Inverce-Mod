package com.inverce.mod.core.configuration;

@FunctionalInterface
public interface ValueChanged<T> {
    void valueChanged(Value<T> preference, T value);
}
