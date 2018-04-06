package com.inverce.mod.core.configuration;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ChangeValueHandler<T> implements ValueChangedEvent<ValueChanged<T>> {
    @Nullable
    protected List<ValueChanged<T>> list = null;

    @Nullable
    private List<ValueChanged<T>> list() {
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    @Override
    public void addListener(@NonNull ValueChanged<T> listener) {
        list().add(listener);
    }

    @Override
    public void removeListener(ValueChanged<T> listener) {
        list().remove(listener);
    }

    @Override
    public void clear() {
        list().clear();
    }

    public void postNewValue(Value<T> preference, T value) {
        for (ValueChanged<T> event: new ArrayList<>(list())) {
            event.valueChanged(preference, value);
        }
    }

}
