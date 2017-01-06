package com.inverce.mod.core.internal;

import android.annotation.SuppressLint;
import android.content.Context;

@SuppressLint("StaticFieldLeak")
public class IMInternal {
    private static IMInternal internal;
    private Context context;

    public static IMInternal get() {
        return internal != null ? internal : (internal = new IMInternal());
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }
}
