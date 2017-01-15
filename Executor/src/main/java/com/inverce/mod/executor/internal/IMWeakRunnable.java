package com.inverce.mod.executor.internal;

import java.lang.ref.WeakReference;

public class IMWeakRunnable implements Runnable {
    WeakReference<Runnable> inner;

    public IMWeakRunnable(Runnable inner) {
        this.inner = new WeakReference<>(inner);
    }

    @Override
    public void run() {
        if (inner.get() != null) {
            inner.get().run();
        }
    }
}
