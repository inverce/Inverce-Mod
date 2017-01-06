package com.inverce.mod.core;

import android.annotation.SuppressLint;
import android.content.Context;

import com.inverce.mod.core.internal.IMInternal;
import com.inverce.mod.core.internal.initialization.IMInitializer;

/**
 * 1. Yes this does not require you to initialize anything
 */
@SuppressLint("StaticFieldLeak")
public class IM {
    private static IMInternal internal = IMInternal.get();

    public static Context getContext() {
        return internal.getContext();
    }

    public static void initializeInProcess(Context context) {
        IMInitializer.initialize(context);
    }

}
