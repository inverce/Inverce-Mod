package com.inverce.mod.core.configuration.extended;

public class BoxedValue<T> {
    protected T value;

    public BoxedValue(T value) {
        this.value = value;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }
}
