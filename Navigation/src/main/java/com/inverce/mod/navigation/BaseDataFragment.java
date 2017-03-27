package com.inverce.mod.navigation;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;

import com.inverce.mod.events.annotation.Listener;

import java.io.Serializable;

public class BaseDataFragment<Actions extends Listener, Data> extends BaseFragment<Actions> {
    public static <T extends BaseDataFragment<?, D>, D extends Serializable> T newInstance(Class<T> clazz, D data) {
        return BaseFragment.newBuilder(clazz)
                .with("data", data)
                .with("data_type", "Serializable")
                .create();
    }
    public static <T extends BaseDataFragment<?, D>, D extends Parcelable> T newInstance(Class<T> clazz, D data) {
        return BaseFragment.newBuilder(clazz)
                .with("data", data)
                .with("data_type", "Parcelable")
                .create();
    }

    @Nullable
    Data data;

    @SuppressWarnings("unchecked")
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        String type = getArgumentString("data_type", "");
        switch (type) {
            case "Serializable": data = getArgumentSerializable("data");
            case "Parcelable":   data = getArgumentParcelable("data");
        }
    }

    @Nullable
    public Data getData() {
        return data;
    }
}
