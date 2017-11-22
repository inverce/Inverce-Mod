package com.inverce.mod.core.configuration;


import com.inverce.mod.events.interfaces.MultiEvent;

import java.util.ArrayList;
import java.util.List;

public class ChangeValueHandler<T> implements MultiEvent<ValueChanged<T>> {
    protected List<ValueChanged<T>> list = null;

    private List<ValueChanged<T>> list() {
        if (list == null) {
            list = new ArrayList<>();
        }
        return list;
    }

    @Override
    public void addListener(ValueChanged<T> listener) {
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
