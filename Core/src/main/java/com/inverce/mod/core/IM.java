package com.inverce.mod.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.inverce.mod.core.internal.IMInternal;

import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;

/**
 */
@SuppressWarnings("WeakerAccess")
@SuppressLint("StaticFieldLeak")
public class IM {
    private static IMInternal internal = IMInternal.get();

    public static Context context() {
        return internal.getContext();
    }

    public static Application application() {
        return (Application) context().getApplicationContext();
    }

    public static Activity activity() {
        return internal.getActivity();
    }

    public static Resources resources() {
        return context().getResources();
    }

    public static ScheduledThreadPoolExecutor onBg() {
        return internal.getBgExecutor();
    }

    public static Executor onUi() {
        return internal.getUiExecutor();
    }


}
