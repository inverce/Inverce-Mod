package com.inverce.mod.core.utilities;

import com.inverce.mod.core.functional.IFunction;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Compare<T> implements Comparator<T> {
    private List<Comparator<T>> comparators;

    public Compare() {
        this.comparators = new ArrayList<>();
    }

    private <Y> int compareNulls(Y a, Y b) {
        if (a == null) {
            return b == null ? 0 : -1;
        } else if (b == null) {
            return 1;
        } else {
            return Integer.MAX_VALUE;
        }
    }

    private <Y> void add(IFunction<T, Y> getter, boolean ascending, Comparator<Y> comparator) {
        comparators.add((_a, _b) -> {
            int proxy = ascending ? 1 : -1;
            int check = compareNulls(_a, _b);
            if (check != Integer.MAX_VALUE) return check * proxy;
            Y a = getter.apply(_a);
            Y b = getter.apply(_b);
            check = compareNulls(a, b);
            if (check != Integer.MAX_VALUE) return check * proxy;
            check = comparator.compare(a, b);
            return check * proxy;
        });
    }

    public Compare<T> byString(IFunction<T, String> getter, boolean asc) {
        this.add(getter, asc, String::compareTo);
        return this;
    }

    public Compare<T> byInt(IFunction<T, Integer> getter, boolean asc) {
        this.add(getter, asc, (a, b) -> Integer.compare(a, b));
        return this;
    }


    @Override
    public int compare(T A, T B) {
        for (int i = 0; i < comparators.size(); i++) {
            int compare = comparators.get(i).compare(A, B);
            if (compare != 0) {
                return compare;
            }
        }
        return 0;
    }
}
