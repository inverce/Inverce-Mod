package com.inverce.mod.core.functional;

@FunctionalInterface
public interface ISupplierEx<T> {
    T get() throws Exception;
}
