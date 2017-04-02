package com.inverce.mod.core.functional;

@FunctionalInterface
public interface IPredicate<T> {
    boolean test(T in);
}
