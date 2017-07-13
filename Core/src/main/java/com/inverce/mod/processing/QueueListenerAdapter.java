package com.inverce.mod.processing;

public class QueueListenerAdapter implements QueueListener {

    @Override
    public void onQueueFinished(ProcessingQueue queue) {

    }

    @Override
    public void onQueueStarted(ProcessingQueue queue) {

    }

    @Override
    public void onQueueCancelled(ProcessingQueue queue) {

    }

    @Override
    public void onJobFinished(ProcessingQueue queue, JobResult<?, ?> job) {

    }

    @Override
    public void onJobStarted(ProcessingQueue queue, Object item, Processor<?, ?> processor) {

    }
}
