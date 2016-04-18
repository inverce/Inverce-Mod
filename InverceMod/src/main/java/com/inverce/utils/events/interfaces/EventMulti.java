package com.inverce.utils.events.interfaces;

public interface EventMulti<T> {
    void addListener(T listener);
    void removeListener(T listener);
    void clear();
}
