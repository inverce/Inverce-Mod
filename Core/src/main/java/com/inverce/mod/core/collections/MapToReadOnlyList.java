package com.inverce.mod.core.collections;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.inverce.mod.core.functional.IFunction;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

public class MapToReadOnlyList<T> extends AbstractList<T> {
    @NonNull
    private final List<?> list;
    @NonNull
    private final IFunction<Object, T> map;

    public <Y> MapToReadOnlyList(@Nullable List<Y> list, @NonNull IFunction<Y, T> map) {
        this.list = list != null ? list : new ArrayList<>();
        //noinspection unchecked // well this is some nice quirk of java, we cant use IFx<?,T> as map since you cant cast capture<?> to capture<?>
        this.map = p -> map.apply((Y) p);
    }

    public MapToReadOnlyList(List<?> list) {
        //noinspection unchecked, let the user think he got it covered, if cast is not possible we will simply throw ClassCastException on first get
        this(list, p -> (T) p);
    }

    @NonNull
    @Override
    public T get(int index) {
        return map.apply(list.get(index));
    }

    @Override
    public int size() {
        return list.size();
    }
}
