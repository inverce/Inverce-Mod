package com.inverce.mod.core.configuration;

import android.support.annotation.NonNull;

import com.inverce.mod.core.functional.ISupplier;
import com.inverce.mod.core.utilities.SubBuilder;

import java.util.ArrayList;

public class Preset {
    public static Preset.Builder create() {
        // create inner class for specific instance of parent,
        // inner classes have reference to parent (unless static ^^)
        return new Preset().new Builder();
    }

    protected ArrayList<Record<?>> records;

    protected Preset() {
        records = new ArrayList<>();
    }

    @NonNull
    public Preset apply() {
        for (Record record: records) {
            record.apply();
        }
        return this;
    }

    protected class Record<T> {
        Value<T> preference;
        ISupplier<T> value;

        public Record(Value<T> preference, ISupplier<T> value) {
            this.preference = preference;
            this.value = value;
        }

        void apply() {
            preference.set(value.get());
        }
    }

    public class Builder extends SubBuilder<Preset> {
        Builder() {
            super(Preset.this);
        }

        @NonNull
        public <T, Y extends Value<T>> Builder add(Y preference, T value) {
            return addSupplier(preference, () -> value);
        }

        @NonNull
        public <T, Y extends Value<T>> Builder addSupplier(Y preference, ISupplier<T> value) {
            Preset.this.records.add(new Record<>(preference, value));
            return this;
        }
    }
}
