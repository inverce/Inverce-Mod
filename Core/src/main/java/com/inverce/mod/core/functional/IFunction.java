package com.inverce.mod.core.functional;

@FunctionalInterface
public interface IFunction<T, R> {
    R apply(T t);
}
