package com.inverce.mod.core.internal.initialization;

import android.content.Context;

import com.inverce.mod.core.internal.IMInternal;

public class IMInitializer {
    public static void initialize(Context context) {
        IMInternal.get().setContext(context);
    }
}
