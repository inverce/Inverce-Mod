package com.inverce.mod.core.configuration;

class BoxedValue<T> {
    private T value;

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
