package com.inverce.mod.core.threadpool;

import android.support.annotation.NonNull;

import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DynamicScheduledExecutor extends ThreadPoolExecutor implements ScheduledExecutorService {
    private static ScheduledThreadPoolExecutor scheduler;

    public DynamicScheduledExecutor() {
        super(0, Integer.MAX_VALUE, 1, TimeUnit.SECONDS, new SynchronousQueue<>());
        setThreadFactory(new NamedThreadPool("Dynamic"));
        if (scheduler == null) {
            scheduler = new ScheduledThreadPoolExecutor(1, new NamedThreadPool("Dynamic-Scheduler"));
            scheduler.setKeepAliveTime(1, TimeUnit.SECONDS);
            scheduler.allowCoreThreadTimeOut(true);
        }
    }

    @Override
    public void setKeepAliveTime(long time, TimeUnit unit) {
        super.setKeepAliveTime(time, unit);
        scheduler.setKeepAliveTime(time, unit);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> schedule(@NonNull Runnable command, long delay, @NonNull TimeUnit unit) {
        return scheduler.schedule(wrap(command), delay,  unit);
    }

    @NonNull
    @Override
    public <V> ScheduledFuture<V> schedule(@NonNull Callable<V> callable, long delay, @NonNull TimeUnit unit) {
        return scheduler.schedule(callable, delay, unit);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(@NonNull Runnable command, long initialDelay, long period, @NonNull TimeUnit unit) {
        return scheduler.scheduleAtFixedRate(wrap(command), initialDelay, period, unit);
    }

    @NonNull
    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable command, long initialDelay, long delay, @NonNull TimeUnit unit) {
        return scheduler.scheduleWithFixedDelay(wrap(command), initialDelay, delay, unit);
    }

    private Runnable wrap(Runnable runnable) {
        //noinspection Convert2Lambda
        return new Runnable() {
            @Override
            public void run() {
                execute(runnable);
            }
        };
    }
}