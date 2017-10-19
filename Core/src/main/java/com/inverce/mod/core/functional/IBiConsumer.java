package com.inverce.mod.core.functional;

@FunctionalInterface
public interface IBiConsumer<T, U> {
    void accept(T t, U u);
}