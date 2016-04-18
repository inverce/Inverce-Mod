package com.inverce.utils.events.interfaces;

public interface EventSingleton<T> {
    void setListener(T listener);
}
