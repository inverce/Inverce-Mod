package com.inverce.mod.core.utilities;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.CheckResult;

import com.inverce.mod.core.functional.IConsumer;

import java.io.Serializable;

public class SubBundleBuilder<TYPE> extends SubBuilder<TYPE> {
    protected Bundle extras;

    public SubBundleBuilder(TYPE parent, Bundle extras) {
        super(parent);
        this.extras = extras;
    }

    @CheckResult
    public SubBundleBuilder<TYPE> with(String name, Serializable data) {
        extras.putSerializable(name, data);
        return this;
    }

    @CheckResult
    public SubBundleBuilder<TYPE> with(String name, Parcelable data) {
        extras.putParcelable(name, data);
        return this;
    }

    @CheckResult
    public SubBundleBuilder<TYPE> with(String name, String data) {
        extras.putString(name, data);
        return this;
    }

    @CheckResult
    public SubBundleBuilder<TYPE> with(String name, long data) {
        extras.putLong(name, data);
        return this;
    }

    @CheckResult
    public SubBundleBuilder<TYPE> with(String name, double data) {
        extras.putDouble(name, data);
        return this;
    }

    /**
     * Pass data as standard "data" parameter
     */
    @CheckResult
    public SubBundleBuilder<TYPE> with(Serializable data) {
        return with("data", data);
    }

    /**
     * Pass data as standard "data" parameter
     */
    @CheckResult
    public SubBundleBuilder<TYPE> with(Parcelable data) {
        return with("data", data);
    }

    /**
     * Add all mappings from provided bundle
     */
    @CheckResult
    public SubBundleBuilder<TYPE> with(Bundle data) {
        extras.putAll(data);
        return this;
    }

    @CheckResult
    public SubBundleBuilder<TYPE> setup(IConsumer<Bundle> setup) {
        setup.consume(extras);
        return this;
    }
}
