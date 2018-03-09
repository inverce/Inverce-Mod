package com.inverce.mod.core.configuration;

import android.support.annotation.NonNull;

import com.inverce.mod.core.configuration.extended.BoxedValue;
import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.functional.IPredicate;
import com.inverce.mod.core.functional.ISupplier;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Value<T> extends ReadOnlyValue<T> {
    protected IPredicate<T> validator;
    protected IConsumer<T> setter;
    protected ChangeValueHandler<T> changeValueHandler;

    protected Value() {
        changeValueHandler = new ChangeValueHandler<>();
    }

    public Value(T value) {
        this(value, p -> true);
    }

    public Value(T value, IPredicate<T> validator) {
        this();
        BoxedValue<T> box = new BoxedValue<>(value);
        setSetter(box::setValue);
        setGetter(box::getValue);
        setValidator(validator);
        set(value);
    }

    public Value(ISupplier<T> supplier, IConsumer<T> setter) {
        this(supplier, setter, p -> true);
    }

    public Value(ISupplier<T> supplier, IConsumer<T> setter, IPredicate<T> validator) {
        super(supplier);
        this.changeValueHandler = new ChangeValueHandler<>();
        this.validator = validator;
        this.setter = setter;
    }

    @NonNull
    public ValueChangedEvent<ValueChanged<T>> changeValueEvent() {
        return changeValueHandler;
    }

    public boolean set(T e) {
        if (validator.test(e)) {
            setter.accept(e);
            changeValueHandler.postNewValue(this, e);
            return true;
        }
        return false;
    }

    protected void setValidator(IPredicate<T> validator) {
        this.validator = validator;
    }

    protected void setSetter(IConsumer<T> setter) {
        this.setter = setter;
    }

    public ReadOnlyValue<T> asReadOnly() {
        return new ReadOnlyValue<>(this);
    }

}
