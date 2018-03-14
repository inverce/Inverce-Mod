package com.inverce.mod.core.collections;

import java.util.ArrayList;
import java.util.List;

public class CollectionsEx {
    public static <T> List<T> join(List<? extends T> A, List<? extends T> B) {
        ArrayList<T> list = A != null ? new ArrayList<T>(A) : new ArrayList<T>();
        if (B != null) {
            list.addAll(B);
        }
        return list;
    }

    public static <T> boolean equals(List<? extends T> A, List<? extends T> B) {
        if (A == null || B == null) return A == B;
        if (A.size() != B.size()) return false;

        for (int i = 0; i < A.size(); i++) {
            if (!A.get(i).equals(B.get(i))) {
                return false;
            }
        }
        return true;
    }
}
