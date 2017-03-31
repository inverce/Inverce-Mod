package com.inverce.mod.core.collections;

import android.support.annotation.NonNull;

import java.util.AbstractCollection;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import static com.inverce.mod.core.collections.TraversalMethod.ASC;
import static com.inverce.mod.core.collections.TraversalMethod.BFS;
import static com.inverce.mod.core.collections.TraversalMethod.DESC;
import static com.inverce.mod.core.collections.TraversalMethod.DFS;

public class TraverseTreeCollection<T extends TreeNode<T>> extends AbstractCollection<T> {
    private final int flags;
    private final T node;

    public TraverseTreeCollection(T node) {
        this(node, DFS | ASC);
    }

    public TraverseTreeCollection(T node, @TraversalMethod int flags) {
        if ((flags & 1) != 1) {
            throw new IllegalStateException("Specify method. DFS or BFS");
        }
        if ((flags & DFS & BFS)== (DFS & BFS)) {
            throw new IllegalStateException("Only one of DFS or BFS is allowed");
        }
        if ((flags & ASC & DESC)== (ASC & DESC)) {
            throw new IllegalStateException("Only one of ASC or DESC is allowed");
        }
        if (node == null) {
            throw new IllegalStateException("Node must not be null");
        }

        this.node = node;
        this.flags = flags;
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
        int inSize = size(), currentItem;
        Stack<Integer> stack;
        Stack<T> nodes;

        Iter() {
            this.stack = new Stack<>();
            this.nodes = new Stack<>();
            this.stack.push(0);
            this.nodes.push(node);
        }

        @Override
        public boolean hasNext() {
            return inSize != currentItem;
        }

        @Override
        public T next() {
            return null;
        }
    }

    static class ReverseOrderList<E> extends AbstractList<E> {
        private List<E> inner;

        public ReverseOrderList(List<E> inner) {
            if (inner == null) {
                inner = new ArrayList<>();
            }
            this.inner = inner;
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
