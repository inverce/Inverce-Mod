package com.inverce.mod.core.configuration;

import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.functional.IPredicate;
import com.inverce.mod.core.functional.ISupplier;
import com.inverce.mod.events.interfaces.MultiEvent;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Value<T> extends ReadOnlyValue<T> {
    private IPredicate<T> validator;
    private IConsumer<T> setter;
    private ChangeValueHandler<T> changeValueHandler;

    protected Value() {
        changeValueHandler = new ChangeValueHandler<>();
    }

    public Value(T value) {
        this(value, P -> true);
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
        this.validator = validator;
        this.setter = setter;
    }

    public MultiEvent<ValueChanged<T>> changeValueEvent() {
        return changeValueHandler;
    }

    public boolean set(T e) {
        if (validator.test(e)) {
            setter.consume(e);
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

    @Override
    public String toString() {
        return String.valueOf(get());
    }

    public ReadOnlyValue<T> asReadOnly() {
        return new ReadOnlyValue<>(this);
    }

}
