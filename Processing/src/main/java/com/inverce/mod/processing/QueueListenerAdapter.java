package com.inverce.mod.processing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class QueueListenerAdapter extends QueueListener {
    @Override
    public void onQueueStarted(@NonNull ProcessingQueue queue) {
        // leave empty for user to override
    }

    @Override
    public void onQueueFinished(@NonNull ProcessingQueue queue) {
        // leave empty for user to override
    }

    @Override
    public void onQueueCancelled(@NonNull ProcessingQueue queue) {
        // leave empty for user to override
    }

    @Override
    public void onJobStarted(@NonNull ProcessingQueue queue, Object item, @NonNull Processor<?, ?> processor) {
        // leave empty for user to override
    }

    @Override
    public void onJobResult(@NonNull ProcessingQueue queue, @NonNull Job<?, ?> job, @Nullable Object result) {
        // leave empty for user to override
    }

    @Override
    public void onJobFailure(@NonNull ProcessingQueue queue, @NonNull Job<?, ?> job, @NonNull Exception exception) {
        // leave empty for user to override
    }
}
