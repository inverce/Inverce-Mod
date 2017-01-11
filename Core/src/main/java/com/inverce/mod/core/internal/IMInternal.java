package com.inverce.mod.core.internal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SuppressLint("StaticFieldLeak")
public class IMInternal {
    private static IMInternal internal;
    private Context context;
    private Handler uiHandler;
    private ExecutorService bgExecutorService;
    private Executor uiExecutor;
    private WeakReference<Activity> activity = new WeakReference<>(null);

    public static IMInternal get() {
        return internal != null ? internal : (internal = new IMInternal());
    }

    public void setContext(Context context) {
        this.context = context;
        this.uiHandler = new Handler(Looper.getMainLooper());
        this.bgExecutorService = Executors.newCachedThreadPool();
        this.uiExecutor = new DefaultUiExecutor();
    }

    public Context getContext() {
        return context;
    }

    public Handler getUiHandler() {
        return uiHandler;
    }

    public ExecutorService getBgExecutor() {
        return bgExecutorService;
    }

    public Executor getUiExecutor() {
        return uiExecutor;
    }

    public void setActivity(Activity activity) {
        this.activity = new WeakReference<>(activity);
    }

    public Activity getActivity() {
        return activity.get();
    }
}
