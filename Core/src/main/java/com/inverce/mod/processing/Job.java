package com.inverce.mod.processing;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.functional.IConsumer;

class Job<ITEM, RESULT> implements IConsumer<ProcessingQueue> {
    ITEM item;
    Processor<ITEM, RESULT> processor;

    public Job(ITEM item, Processor<ITEM, RESULT> processor) {
        this.item = item;
        this.processor = processor;
    }

    @Override
    public void consume(ProcessingQueue queue) {
        try {
            RESULT result = processor.processJob(item);
            IM.onBg().execute(() -> queue.finishJob(new JobResult<>(this, result)));
        } catch (Exception ex) {
            IM.onBg().execute(() -> queue.finishJob(new JobResult<>(this, ex)));
        }
    }
}
