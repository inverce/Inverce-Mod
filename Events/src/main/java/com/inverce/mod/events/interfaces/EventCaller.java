package com.inverce.mod.events.interfaces;

import android.support.annotation.NonNull;

public interface EventCaller<T> {
    @NonNull
    T post();
}
