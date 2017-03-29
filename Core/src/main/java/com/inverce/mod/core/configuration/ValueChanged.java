package com.inverce.mod.core.configuration;

@FunctionalInterface
public interface ValueChanged<T> {
    void valueChanged(ValuePreference<T> preference, T value);
}
