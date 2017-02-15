package com.inverce.mod.core.configuration;

public class Preset<T> {
    public static <Y> Preset<Y> create(String name) {
        return new Preset<>();
    }
}
