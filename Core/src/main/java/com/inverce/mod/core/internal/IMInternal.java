package com.inverce.mod.core.internal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;

import com.inverce.mod.core.Lifecycle;

import java.lang.ref.WeakReference;

@SuppressLint("StaticFieldLeak")
@RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
public class IMInternal {
    private static IMInternal internal;
    private Context context;
    @NonNull
    private WeakReference<Activity> activity = new WeakReference<>(null);
    private static boolean inEdit;

    @NonNull
    public static IMInternal get() {
        return internal != null ? internal : (internal = new IMInternal());
    }

    public boolean isInEdit() {
        return inEdit;
    }

    public void setInEdit(boolean inEdit) {
        IMInternal.inEdit = inEdit;
    }

    public void initialize(Context context) {
        // todo check if already initialized
        this.context = context;
        Lifecycle.initialize();
    }

    public Context getContext() {
        return context;
    }

    public void setActivity(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    public Activity getActivity() {
        return activity.get();
    }

}
