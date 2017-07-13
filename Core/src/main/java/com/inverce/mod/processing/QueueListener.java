package com.inverce.mod.processing;

import com.inverce.mod.events.annotation.Listener;

public interface QueueListener extends Listener {
    void onQueueFinished(ProcessingQueue queue);

    void onQueueStarted(ProcessingQueue queue);

    void onQueueCancelled(ProcessingQueue queue);

    void onJobFinished(ProcessingQueue queue, JobResult<?, ?> job);

    void onJobStarted(ProcessingQueue queue, Object item, Processor<?, ?> processor);
}
