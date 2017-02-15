package com.inverce.mod.core.configuration;

public class Config<T> {
    public static <Y, T extends Config<Y>> T getConfig(Class<T> cfgClass) {
        return (T) new Config<T>();
    }

    public <A> Config<T> set(ConfigValue<A> config, A value) {
        return this;
    }

    public T proxy() {
        return null;
    }
}
