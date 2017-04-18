package com.inverce.mod.core;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;

import com.inverce.mod.core.internal.IMInternal;
import com.inverce.mod.core.threadpool.DynamicScheduledExecutor;
import com.inverce.mod.core.threadpool.UIScheduler;

@SuppressWarnings("WeakerAccess")
@SuppressLint("StaticFieldLeak")
public class IM {
    private static IMInternal internal = IMInternal.get();

    public static Context context() {
        if (internal.getActivity() != null) {
            return internal.getActivity();
        }
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

    public static LayoutInflater inflater() {
        return LayoutInflater.from(context());
    }

    public static DynamicScheduledExecutor onBg() {
        return internal.getBgExecutor();
    }

    public static UIScheduler onUi() {
        return internal.getUiExecutor();
    }

    public static void enableInEditModeForView(@NonNull View view) {
        if (view.isInEditMode()) {
            IMInternal.get().setInEdit(true);
            IMInitializer.initialize(view.getContext());
        }
    }
}
