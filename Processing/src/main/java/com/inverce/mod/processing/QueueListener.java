package com.inverce.mod.processing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

public abstract class QueueListener {
    public abstract void onQueueStarted     (@NonNull ProcessingQueue queue);
    public abstract void onQueueFinished    (@NonNull ProcessingQueue queue);
    public abstract void onQueueCancelled   (@NonNull ProcessingQueue queue);
    public abstract void onJobStarted       (@NonNull ProcessingQueue queue, Object item, @NonNull Processor<?, ?> processor);
    public abstract void onJobResult        (@NonNull ProcessingQueue queue, @NonNull Job<?, ?> job, @Nullable Object result);
    public abstract void onJobFailure       (@NonNull ProcessingQueue queue, @NonNull Job<?, ?> job, @NonNull Exception exception);
}
