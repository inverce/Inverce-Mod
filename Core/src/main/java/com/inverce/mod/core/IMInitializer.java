package com.inverce.mod.core;

import android.content.Context;

import com.inverce.mod.core.internal.IMInternal;

public class IMInitializer {
    public static void initialize(Context context) {
        IMInternal.get().initialize(context);
        Lifecycle.initialize();
    }
}
