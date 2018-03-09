package com.inverce.mod.core.collections;

import java.util.ArrayList;
import java.util.List;

public class CollectionsEx {
    public <T> List<T> join(List<? extends T> A, List<? extends T> B) {
        ArrayList<T> list = A != null ? new ArrayList<T>(A) : new ArrayList<T>();
        if (B != null) {
            list.addAll(B);
        }
        return list;
    }

}
