package com.inverce.mod.executor.internal;

import java.lang.ref.WeakReference;
import java.util.concurrent.Callable;

public class IMWeakCallable<T> implements Callable<T> {
    WeakReference<Callable<T>> inner;

    public IMWeakCallable(Callable<T> inner) {
        this.inner = new WeakReference<>(inner);
    }

    @Override
    public T call() throws Exception {
        if (inner.get() != null) {
            return inner.get().call();
        }

        return null;
    }
}
