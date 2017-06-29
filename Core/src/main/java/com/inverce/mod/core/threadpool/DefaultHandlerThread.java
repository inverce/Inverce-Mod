package com.inverce.mod.core.threadpool;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;


public class DefaultHandlerThread extends HandlerThread implements Executor {
    Handler handler;
    public DefaultHandlerThread() {
        super("Handler-thread", Thread.NORM_PRIORITY);
    }

    public Handler getHandler() {
        if (handler == null) {
            Looper looper = getLooper();
            if (looper != null) {
                handler = new Handler(getLooper());
            }
        }
        if (handler == null) {
            throw new IllegalStateException("Looper not prepared, or thread is no longer active");
        }
        return handler;
    }

    @Override
    public void execute(@NonNull Runnable command) {
        getHandler().post(command);
    }
}
