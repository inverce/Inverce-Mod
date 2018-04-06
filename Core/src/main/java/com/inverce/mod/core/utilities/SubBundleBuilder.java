package com.inverce.mod.core.utilities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.inverce.mod.core.functional.IConsumer;

import java.io.Serializable;

public class SubBundleBuilder<TYPE> extends SubBuilder<TYPE> {
    private IConsumer<Bundle> setter;
    protected Bundle extras;

    public SubBundleBuilder(TYPE parent, @Nullable Bundle extras) {
        super(parent);
        this.extras = extras == null ? new Bundle() : extras;
    }

    @Override
    public TYPE create() {
        if (setter != null) {
            setter.accept(extras);
        }
        return super.create();
    }

    public SubBundleBuilder(TYPE baseFragment, Bundle arguments, IConsumer<Bundle> setter) {
        this(baseFragment, arguments);
        this.setter = setter;
    }

    @NonNull
    @CheckResult
    public SubBundleBuilder<TYPE> with(@NonNull String name, Serializable data) {
        extras.putSerializable(name, data);
        return this;
    }

    @NonNull
    @CheckResult
    public SubBundleBuilder<TYPE> with(@NonNull String name, Parcelable data) {
        extras.putParcelable(name, data);
        return this;
    }

    @NonNull
    @CheckResult
    public SubBundleBuilder<TYPE> with(@NonNull String name, String data) {
        extras.putString(name, data);
        return this;
    }

    @NonNull
    @CheckResult
    public SubBundleBuilder<TYPE> with(@NonNull String name, long data) {
        extras.putLong(name, data);
        return this;
    }

    @NonNull
    @CheckResult
    public SubBundleBuilder<TYPE> with(@NonNull String name, double data) {
        extras.putDouble(name, data);
        return this;
    }

    /**
     * Pass data as standard "data" parameter
     */
    @NonNull
    @CheckResult
    public SubBundleBuilder<TYPE> with(@NonNull Serializable data) {
        return with("data", data);
    }

    /**
     * Pass data as standard "data" parameter
     */
    @NonNull
    @CheckResult
    public SubBundleBuilder<TYPE> with(@NonNull Parcelable data) {
        return with("data", data);
    }

    /**
     * Add all mappings from provided bundle
     */
    @NonNull
    @CheckResult
    public SubBundleBuilder<TYPE> with(@NonNull Bundle data) {
        extras.putAll(data);
        return this;
    }

    @NonNull
    @CheckResult
    public SubBundleBuilder<TYPE> setup(@NonNull IConsumer<Bundle> setup) {
        setup.accept(extras);
        return this;
    }
}
