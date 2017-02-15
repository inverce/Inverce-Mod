package com.inverce.mod.core.configuration.function;

public interface ISupplier<T> {
    T supply();

    public class SINGLE<T> implements ISupplier<T> {
        T value;
        public SINGLE(T value) {
            this.value = value;
        }

        @Override
        public T supply() {
            return value;
        }
    }
}
