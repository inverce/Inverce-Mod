package com.inverce.mod.processing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public class QueueListenerAdapter extends QueueListener {
    @Override
    public void onQueueStarted(@NonNull ProcessingQueue queue) {

    }

    @Override
    public void onQueueFinished(@NonNull ProcessingQueue queue) {

    }

    @Override
    public void onQueueCancelled(@NonNull ProcessingQueue queue) {

    }

    @Override
    public void onJobStarted(@NonNull ProcessingQueue queue, Object item, @NonNull Processor<?, ?> processor) {

    }

    @Override
    public void onJobResult(@NonNull ProcessingQueue queue, @NonNull Job<?, ?> job, @Nullable Object result) {

    }

    @Override
    public void onJobFailure(@NonNull ProcessingQueue queue, @NonNull Job<?, ?> job, @NonNull Exception exception) {

    }
}
