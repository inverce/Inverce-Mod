package com.inverce.mod.events.interfaces;

public interface MultiEvent<T> {
    void addListener(T listener);
    void removeListener(T listener);
    void clear();
}
