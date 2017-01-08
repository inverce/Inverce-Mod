package com.inverce.mod.navigation;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.inverce.mod.events.annotation.Listener;

import java.io.Serializable;

public class BasePage<Actions extends Listener> extends Fragment {
    private interface CONST {
        int SERIALIZER_TYPE = 1;
        int PARCERABLE_TYPE = 1;
    }
    /** Easier new instance **/
    public static <T extends BasePage> T newInstance(Class<T> clazz) {
        try {
            return clazz.newInstance();
        } catch (Exception ex) {
            throw new InstantiationException("Could not create fragment", ex);
        }
    }

    public static <T extends BasePage> T newInstance(Class<T> clazz, Bundle args) {
        T fragment = newInstance(clazz);
        fragment.setArguments(args);
        return fragment;
    }

    public static <T extends BasePage> T newInstance(Class<T> clazz, Parcelable data) {
        Bundle args = new Bundle();
        args.putParcelable("data", data);
        args.putLong("data-serializer-type", CONST.PARCERABLE_TYPE);
        return newInstance(clazz, args);
    }

    public static <T extends BasePage> T newInstance(Class<T> clazz, Serializable data) {
        Bundle args = new Bundle();
        args.putSerializable("data", data);
        args.putLong("data-serializer-type", CONST.SERIALIZER_TYPE);
        return newInstance(clazz, args);
    }


    Actions actions;

    public BasePage() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public Actions getActions() {
        return actions;
    }
}
