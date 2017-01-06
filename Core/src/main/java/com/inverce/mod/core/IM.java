package com.inverce.mod.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;

import com.inverce.mod.core.internal.IMInternal;
import com.inverce.mod.core.internal.initialization.IMInitializer;

/**
 */
@SuppressLint("StaticFieldLeak")
public class IM {
    private static IMInternal internal = IMInternal.get();

    public static Context getContext() {
        return internal.getContext();
    }

    public static Resources resources() {
        return getContext().getResources();
    }

    public static void initializeInProcess(Context context) {
        IMInitializer.initialize(context);
    }

}
