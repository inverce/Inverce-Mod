package com.inverce.mod.events;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;

/* internal */ class DefaultUiExecutor implements Executor {
    private Handler handler;

    public DefaultUiExecutor() {
        handler = new Handler(Looper.getMainLooper());
    }

    @Override
    public void execute(@NonNull Runnable command) {
        handler.post(command);
    }
}

