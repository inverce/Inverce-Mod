package com.inverce.mod.core.collections;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Implementation of arrayList with values stored in weak references
 * @param <T>
 */
public class WeakArrayList<T> extends AbstractList<T> {
    @NonNull
    protected ArrayList<WeakReference<T>> impl = new ArrayList<>(1);

    @Override @Nullable
    public T get(int index) {
        return impl.get(index).get();
    }

    @Override
    public int size() {
        return impl.size();
    }

    @Nullable
    @Override
    public T set(int index, T element) {
        WeakReference<T> old = impl.set(index, new WeakReference<>(element));
        return old != null ? old.get() : null;
    }

    @Override
    public void add(int index, T element) {
        impl.add(index, new WeakReference<>(element));
    }

    @Nullable
    @Override
    public T remove(int index) {
        WeakReference<T> old = impl.remove(index);
        return old != null ? old.get() : null;
    }

    public int clearEmptyReferences() {
        int cleared = 0;
        Iterator<T> it = iterator();
        while (it.hasNext()) {
            if (it.next() == null) {
                it.remove();
                cleared++;
            }
        }
        return cleared;
    }
}
