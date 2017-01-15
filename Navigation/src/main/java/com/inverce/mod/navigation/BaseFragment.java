package com.inverce.mod.navigation;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.inverce.mod.core.utilities.SubBundleBuilder;
import com.inverce.mod.events.annotation.Listener;

import java.io.Serializable;

public class BaseFragment<Actions extends Listener> extends Fragment {
    /** Easier new instance **/
    public static <T extends BaseFragment> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception ex) {
            throw new InstantiationException("Could not create fragment", ex);
        }
    }

    public static <T extends BaseFragment> SubBundleBuilder<T> newBuilder(Class<T> clazz) {
        return new FragmentArgumentsBuilder<>(newInstance(clazz), new Bundle());
    }

    Actions actions;
    public BaseFragment() { }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public Actions getActions() {
        return actions;
    }

    /* EASY ARGUMENTS */

    public BaseFragment addArgumentInt(String name, int value) {
        Bundle args = getArguments() != null ? getArguments() : new Bundle();
        args.putInt(name, value);
        setArguments(args);
        return this;
    }

    public BaseFragment addArgumentString(String name, String value) {
        Bundle args = getArguments() != null ? getArguments() : new Bundle();
        args.putString(name, value);
        setArguments(args);
        return this;
    }

    public BaseFragment addArgumentSerializable(String name, Serializable value) {
        Bundle args = getArguments() != null ? getArguments() : new Bundle();
        args.putSerializable(name, value);
        setArguments(args);
        return this;
    }


    public int getArgumentInt(String name, int fallback) {
        Bundle args = getArguments();
        return args == null ? fallback : args.getInt(name, fallback);
    }

    public long getArgumentLong(String name, long fallback) {
        Bundle args = getArguments();
        return args == null ? fallback : args.getLong(name, fallback);
    }

    public String getArgumentString(String name, String fallback) {
        Bundle args = getArguments();
        return args == null ? fallback : args.getString(name, fallback);
    }

    @SuppressWarnings("unchecked")
    public <T extends Serializable> T getArgumentSerializable(String name) {
        Bundle args = getArguments();
        return args == null ? null : (T) args.getSerializable(name);
    }

    @SuppressWarnings("unchecked")
    public <T extends Parcelable> T getArgumentParcelable(String name) {
        Bundle args = getArguments();
        return args == null ? null : (T) args.getParcelable(name);
    }

    private static class FragmentArgumentsBuilder<T extends BaseFragment<?>> extends SubBundleBuilder<T> {
        public FragmentArgumentsBuilder(T parent, Bundle extras) {
            super(parent, extras);
        }
        @Override
        public T done() {
            parent.setArguments(this.extras);
            return super.done();
        }
    }

}
