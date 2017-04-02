package com.inverce.mod.core.collections;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

@SuppressWarnings({"WeakerAccess", "unused"})
public class FlatArrayList<E> extends AbstractList<E> implements List<E>, RandomAccess, Cloneable, java.io.Serializable {
    protected FlatArrayListStore<E> container;
    protected boolean optimize = false;
    protected List<E> cache;

    public FlatArrayList() {
        this(false);
    }

    /**
     * @param optimizeAccess - if set to true, container will cache sizes of sublist
     *                       and optimize access to items. When changing sub containers
     *                       this container should be notified via refresh() method;
     */
    public FlatArrayList(boolean optimizeAccess) {
        this.container = new FlatArrayListStore<E>();
        this.cache = new ArrayList<>(0);
        this.optimize = optimizeAccess;
    }

    @Override
    public E get(int index) {
        return optimize ? cache.get(index) : getFlatElement(index);
    }

    @Override
    public int size() {
        return optimize ? cache.size() : calculateSize();
    }

    boolean flatError = false;
    private synchronized E getFlatElement(int index) {
        int size = size();
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(String.format(Locale.getDefault(), "Accessing %d when size is %d", index, size));
        }

        int current = 0, next;
        List<E> box;
        for (int i = 0; i < container.size(); i++) {
            box = container.get(i);
            next = current + box.size();
            if (index >= current && index < next) {
                return box.get(index - current);
            } else {
                current = next;
            }
        }

        if (flatError) {
            flatError = false;
            throw new IllegalStateException("Reached size and not found index.");
        } else {
            flatError = true;
            E el = getFlatElement(index);
            flatError = false;
            return el;
        }
    }

    private synchronized int calculateSize() {
        int size = 0;
        for (List<E> el: container) {
            size += el.size();
        }
        return size;
    }

    public synchronized void refresh() {
        List<E> newCache = new ArrayList<>();
        for (List<E> el: container) {
            newCache.addAll(el);
        }
        this.cache = newCache;
    }

    public FlatArrayListStore<E> store() {
        return container;
    }

    @SuppressWarnings("unused")
    public class FlatArrayListStore<T> extends ArrayList<List<T>> {
        private List<T> singleton(T element) {
            ArrayList<T> singleton = new ArrayList<>(1);
            singleton.add(element);
            return singleton;
        }

        public T getSingle(int index) {
            List<T> el = super.get(index);
            if (el.size() == 1) {
                return el.get(0);
            } else {
                throw new NoSuchElementException("No singleton object at position: " + index);
            }
        }

        public List<T> setSingle(int index, T element) {
            return super.set(index, singleton(element));
        }

        public boolean addSingle(T element) {
            return super.add(singleton(element));
        }

        public void addSingle(int index, T element) {
            super.add(index, singleton(element));
        }

        public boolean removeSingle(T c) {
            if (c == null) {
                return false;
            }

            for (int i = 0; i < size(); i++) {
                List<T> box = get(i);
                if (box.size() == 1 && c.equals(box.get(0))) {
                    return remove(i) != null;
                }
            }
            return false;
        }
    }
}

