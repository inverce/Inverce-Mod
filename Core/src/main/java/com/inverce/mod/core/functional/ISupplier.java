package com.inverce.mod.core.functional;

@FunctionalInterface
public interface ISupplier<T> {
    T get();
}
