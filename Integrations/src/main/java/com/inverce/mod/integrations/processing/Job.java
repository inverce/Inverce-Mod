package com.inverce.mod.integrations.processing;

import com.inverce.mod.core.IM;

class Job<ITEM, RESULT> {
    ITEM item;
    Processor<ITEM, RESULT> processor;

    public Job(ITEM item, Processor<ITEM, RESULT> processor) {
        this.item = item;
        this.processor = processor;
    }

    protected void startJob(ProcessingQueue queue) {
        queue.threadFactory.newThread(() -> {
            try {
                RESULT result = processor.processJob(item);
                IM.onBg().execute(() -> queue.finishJob(new JobResult<>(this, result)));
            } catch (Exception ex) {
                IM.onBg().execute(() -> queue.finishJob(new JobResult<>(this, ex)));
            }
        }).start();
    }

}
