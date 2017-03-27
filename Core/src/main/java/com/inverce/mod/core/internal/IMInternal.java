package com.inverce.mod.core.internal;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@SuppressLint("StaticFieldLeak")
public class IMInternal {
    private static IMInternal internal;
    private Context context;
    private Handler uiHandler;
    private ScheduledThreadPoolExecutor bgExecutorService;
    private Executor uiExecutor;
    private WeakReference<Activity> activity = new WeakReference<>(null);

    public static IMInternal get() {
        return internal != null ? internal : (internal = new IMInternal());
    }

    public void initialize(Context context) {
        this.context = context;
        this.uiHandler = new Handler(Looper.getMainLooper());
        this.bgExecutorService = new ScheduledThreadPoolExecutor(0);
        this.bgExecutorService.setKeepAliveTime(1, TimeUnit.SECONDS);
        this.uiExecutor = new DefaultUiExecutor();
    }

    public Context getContext() {
        return context;
    }

    public Handler getUiHandler() {
        return uiHandler;
    }

    public ScheduledThreadPoolExecutor getBgExecutor() {
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
