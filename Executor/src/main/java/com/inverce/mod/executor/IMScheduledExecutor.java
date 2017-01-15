package com.inverce.mod.executor;

import com.inverce.mod.executor.internal.GenericWrapTaskScheduledExecutor;
import com.inverce.mod.executor.internal.IMWeakCallable;
import com.inverce.mod.executor.internal.IMWeakRunnable;

import java.util.concurrent.Callable;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;

public class IMScheduledExecutor extends GenericWrapTaskScheduledExecutor {
    public IMScheduledExecutor(int corePoolSize) {
        super(corePoolSize);
    }

    public IMScheduledExecutor(int corePoolSize, ThreadFactory threadFactory) {
        super(corePoolSize, threadFactory);
    }

    public IMScheduledExecutor(int corePoolSize, RejectedExecutionHandler handler) {
        super(corePoolSize, handler);
    }

    public IMScheduledExecutor(int corePoolSize, ThreadFactory threadFactory, RejectedExecutionHandler handler) {
        super(corePoolSize, threadFactory, handler);
    }

    @Override
    protected Runnable wrap(Runnable runnable) {
        if (runnable instanceof IMWeakRunnable) {
            return runnable;
        }
        return new IMWeakRunnable(runnable);
    }

    @Override
    protected <T> Callable<T> wrap(Callable<T> callable) {
        if (callable instanceof IMWeakCallable) {
            return callable;
        }
        return new IMWeakCallable<>(callable);
    }


}
