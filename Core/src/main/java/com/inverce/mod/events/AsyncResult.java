package com.inverce.mod.events;

import android.support.annotation.NonNull;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

class AsyncResult<T> extends AbstractList<T> {
    protected List<T> store;

    AsyncResult(List<T> list) {
        store = list;
    }

    @Override
    public int size() {
        return store.size();
    }

    @Override
    public T get(int index) {
        return store.get(index);
    }

    public static <Y> AsyncResult<Y> yield() {
        return new AsyncResult<>(new ArrayList<>(0));
    }
    public static <Y> AsyncResult<Y> yield(Y el) {
        return new AsyncResult<>(Collections.singletonList(el));
    }
    @SafeVarargs
    public static <Y> AsyncResult<Y> yield(Y ... el) {
        return new AsyncResult<>(Arrays.asList(el));
    }
    public static <Y> AsyncResult<Y> yield(@NonNull List<Y> elements) {
        return new AsyncResult<>(elements);
    }
}
