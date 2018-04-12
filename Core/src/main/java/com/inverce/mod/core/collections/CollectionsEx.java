package com.inverce.mod.core.collections;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class CollectionsEx {
    @NonNull
    public static <T> List<T> join(@Nullable List<? extends T> A, @Nullable List<? extends T> B) {
        ArrayList<T> list = A != null ? new ArrayList<T>(A) : new ArrayList<T>();
        if (B != null) {
            list.addAll(B);
        }
        return list;
    }

    public static <T> boolean equals(@Nullable List<? extends T> A, @Nullable List<? extends T> B) {
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
