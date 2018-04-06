package com.inverce.mod.core.collections;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.Stack;

import static com.inverce.mod.core.collections.TraversalMethod.ASC;
import static com.inverce.mod.core.collections.TraversalMethod.BFS;
import static com.inverce.mod.core.collections.TraversalMethod.DESC;
import static com.inverce.mod.core.collections.TraversalMethod.DFS;
// todo make sure
public class TraverseTreeCollection<T extends TreeNode<T>> extends AbstractCollection<T> {
    protected final boolean isDfs, isAsc;
    @Nullable
    protected final T node;

    public TraverseTreeCollection(T node) {
        this(node, DFS | ASC);
    }

    public TraverseTreeCollection(@Nullable T node, @TraversalMethod int flags) {
        if ((flags & 1) != 1) {
            throw new IllegalStateException("Specify method. DFS or BFS");
        }
        if ((flags & (DFS | BFS))== (DFS | BFS)) {
            throw new IllegalStateException("Only one of DFS or BFS is allowed");
        }
        if ((flags & (ASC | DESC))== (ASC | DESC)) {
            throw new IllegalStateException("Only one of ASC or DESC is allowed");
        }
        if (node == null) {
            throw new IllegalStateException("Node must not be null");
        }

        this.node = node;
        this.isDfs = (flags & DFS) == DFS;
        this.isAsc = (flags & ASC) == ASC;
    }

    @NonNull
    @Override
    public Iterator<T> iterator() {
        return new Iter();
    }

    @Override
    public int size() {
        return node.inSize();
    }

    private class Iter implements Iterator<T> {
        Stack<T> box;
        Queue<T> que;

        Iter() {
            this.box = new Stack<>();
            this.que = new LinkedList<>();
            if (isDfs) {
                this.box.push(node);
            } else {
                pushChildren(que, node);
            }
        }

        @Override
        public boolean hasNext() {
            return isDfs ? !box.empty() : !que.isEmpty();
        }

        @Override
        public T next() {
            if (!hasNext ( )) {
                throw new NoSuchElementException("tree ran out of elements");
            }
            T node;

            if (isDfs) {
                node = box.pop();
                pushChildren(box, node);
            } else {
                node = que.remove();
                pushChildren(que, node);
            }
            return node;
        }

        private void pushChildren(@NonNull Collection<T> into, @NonNull T node) {
            if (isAsc) {
                into.addAll(new ReverseOrderList<>(node.getChildren()));
            } else {
                into.addAll(node.getChildren());
            }
        }

    }

    private static class ReverseOrderList<E> extends AbstractList<E> {
        @Nullable
        private List<E> inner;

        ReverseOrderList(@Nullable List<E> inner) {
            this.inner = inner != null ? inner : new ArrayList<>();
        }

        @Override
        public E get(int index) {
            return inner.get(inner.size() - 1 - index);
        }

        @Override
        public int size() {
            return inner.size();
        }
    }
}
