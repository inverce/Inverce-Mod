package com.inverce.mod.core.threadpool;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class UIScheduler implements Executor {
    protected static ScheduledThreadPoolExecutor scheduler;
    @NonNull
    protected final Handler uiHandler;

    public UIScheduler() {
        uiHandler = new Handler(Looper.getMainLooper());
        if (scheduler == null) {
            scheduler = new ScheduledThreadPoolExecutor(1, new NamedThreadPool("UI-Scheduler"));
            scheduler.setKeepAliveTime(2, TimeUnit.SECONDS);
            scheduler.allowCoreThreadTimeOut(true);
        }
    }

    @NonNull
    public ScheduledFuture<?> schedule(@NonNull Runnable command, long delay, @NonNull TimeUnit unit) {
        return scheduler.schedule(wrap(command), delay,  unit);
    }

    @NonNull
    public <V> ScheduledFuture<V> schedule(@NonNull Callable<V> callable, long delay, @NonNull TimeUnit unit) {
        return scheduler.schedule(callable, delay, unit);
    }

    @NonNull
    public ScheduledFuture<?> scheduleAtFixedRate(@NonNull Runnable command, long initialDelay, long period, @NonNull TimeUnit unit) {
        return scheduler.scheduleAtFixedRate(wrap(command), initialDelay, period, unit);
    }

    @NonNull
    public ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable command, long initialDelay, long delay, @NonNull TimeUnit unit) {
        return scheduler.scheduleWithFixedDelay(wrap(command), initialDelay, delay, unit);
    }

    private Runnable wrap(@NonNull Runnable runnable) {
        //noinspection Convert2Lambda
        return new Runnable() {
            @Override
            public void run() {
                execute(runnable);
            }
        };
    }

    @Override
    public void execute(@NonNull Runnable command) {
        uiHandler.post(command);
    }
}
