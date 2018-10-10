package com.inverce.mod.processing;

import android.support.annotation.NonNull;

import com.inverce.mod.v2.core.IM;

public class Job<ITEM, RESULT> implements IConsumer<ProcessingQueue> {
    protected ITEM item;
    protected Processor<ITEM, RESULT> processor;
    protected Thread thread;

    public Job(ITEM item, Processor<ITEM, RESULT> processor) {
        this.item = item;
        this.processor = processor;
    }

    @Override
    public void accept(@NonNull ProcessingQueue queue) {
        try {
            RESULT result = processor.processJob(item);
            IM.onBg().execute(() -> queue.finishJob(new JobResult<>(this, result)));
        } catch (Exception ex) {
            IM.onBg().execute(() -> queue.finishJob(new JobResult<>(this, ex)));
        }
    }

    public ITEM getItem() {
        return item;
    }

    public Processor<ITEM, RESULT> getProcessor() {
        return processor;
    }

    public Thread getThread() {
        return thread;
    }
}
