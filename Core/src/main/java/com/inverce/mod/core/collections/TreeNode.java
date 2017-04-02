package com.inverce.mod.core.collections;

import java.util.ArrayList;
import java.util.List;

public class TreeNode<T extends TreeNode<T>>  {
    private List<T> children;

    public TreeNode() {
        this(new ArrayList<>());
    }

    public TreeNode(List<T> children) {
        if (children != null) {
            this.children = new ArrayList<>(children);
        } else {
            this.children = new ArrayList<>();
        }
    }

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
