package com.inverce.mod.core.collections;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T extends TreeNode<T>>  {
    private List<T> children;

    public TreeNode() {
        this(new ArrayList<>());
    }

    public TreeNode(@Nullable List<T> children) {
        if (children != null) {
            this.children = new ArrayList<>(children);
        } else {
            this.children = new ArrayList<>();
        }
    }

    @NonNull
    public List<T> getChildren() {
        return children;
    }

    public synchronized int inSize() {
        int inSize = children.size();
        for (T child: children) {
            inSize += child.inSize();
        }
        return inSize;
    }
}
