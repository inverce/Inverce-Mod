package com.inverce.mod.core.internal;

import android.support.annotation.NonNull;

import java.util.concurrent.Executor;


/* internal */ class DefaultUiExecutor implements Executor {
    @Override
    public void execute(@NonNull Runnable command) {
        IMInternal.get().getUiHandler().post(command);
    }
}
