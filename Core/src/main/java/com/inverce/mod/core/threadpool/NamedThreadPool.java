package com.inverce.mod.core.threadpool;

import android.support.annotation.NonNull;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class NamedThreadPool implements ThreadFactory {
    protected static final AtomicInteger poolNumber = new AtomicInteger(1);
    protected final ThreadGroup group;
    protected final AtomicInteger threadNumber = new AtomicInteger(1);
    @NonNull
    protected final String namePrefix;

    public NamedThreadPool(String name) {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();
        namePrefix = name + "-" + poolNumber.getAndIncrement() + "-thread-";
    }

    @NonNull
    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r, namePrefix + threadNumber.getAndIncrement(), 0);
        if (t.isDaemon())
            t.setDaemon(false);
        if (t.getPriority() != Thread.NORM_PRIORITY)
            t.setPriority(Thread.NORM_PRIORITY);
        return t;
    }
}
