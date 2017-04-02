package com.inverce.mod.core.configuration;

import com.inverce.mod.core.functional.ISupplier;
import com.inverce.mod.core.utilities.SubBuilder;

import java.util.ArrayList;

public class Preset {
    public static Preset.Builder create() {
        // create inner class for specific instance of parent,
        // inner classes have reference to parent (unless static ^^)
        return new Preset().new Builder();
    }

    private ArrayList<Record<?>> records;

    Preset() {
        records = new ArrayList<>();
    }

    public Preset apply() {
        for (Record record: records) {
            record.apply();
        }
        return this;
    }

    private class Record<T> {
        ValuePreference<T> preference;
        ISupplier<T> value;

        public Record(ValuePreference<T> preference, ISupplier<T> value) {
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

        public <T, Y extends ValuePreference<T>> Builder add(Y preference, T value) {
            return addSupplier(preference, () -> value);
        }

        public <T, Y extends ValuePreference<T>> Builder addSupplier(Y preference, ISupplier<T> value) {
            Preset.this.records.add(new Record<>(preference, value));
            return this;
        }
    }
}
