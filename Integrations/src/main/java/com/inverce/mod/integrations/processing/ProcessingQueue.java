package com.inverce.mod.integrations.processing;

import android.support.annotation.WorkerThread;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.threadpool.NamedThreadPool;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadFactory;

import static com.inverce.mod.core.verification.Preconditions.checkArgument;
import static com.inverce.mod.core.verification.Preconditions.checkNotNull;
import static com.inverce.mod.core.verification.Preconditions.checkState;

public class ProcessingQueue {
    List<Job<?, ?>> processing, finished, awaiting;
    boolean asynchronous, started, cancelled;
    FailureAction failureAction;
    ThreadFactory threadFactory;
    int poolSize = 8;

    public ProcessingQueue() {
        awaiting = Collections.synchronizedList(new ArrayList<>());
        asynchronous = false;
        failureAction = FailureAction.ABORT;
        threadFactory = new NamedThreadPool("ProcessingQueue#" + hashCode());
    }

    public ProcessingQueue setAsynchronous(boolean asynchronous) {
        checkState(!started, "ProcessingQueue already started");
        this.asynchronous = asynchronous;
        return this;
    }

    public ProcessingQueue setPoolSize(int poolSize) {
        checkArgument(poolSize < 1, "Pool size must be greater than 0");
        this.poolSize = poolSize;
        return this;
    }

    public ProcessingQueue setFailureAction(FailureAction failureAction) {
        checkState(!started, "ProcessingQueue already started");
        this.failureAction = failureAction;
        return this;
    }

    public ProcessingQueue setThreadFactory(ThreadFactory threadFactory) {
        checkState(!started, "ProcessingQueue already started");
        checkNotNull(threadFactory, "Factory cannot be null");
        this.threadFactory = threadFactory;
        return this;
    }

    public <T, R> ProcessingQueue process(Processor<T, R> processor, T item) {
        process(processor, Collections.singletonList(item));
        return this;
    }

    public <T, R> ProcessingQueue process(Processor<T, R> processor, List<T> list) {
        checkNotNull(processor);
        checkNotNull(list);
        for (T item : list) {
            awaiting.add(new Job<>(item, processor));
        }
        return this;
    }

    public synchronized void start() {
        checkArgument(!started);
        checkArgument(awaiting.size() > 0, "You need to add at least one item to process");

        processing = Collections.synchronizedList(new ArrayList<>());
        finished = Collections.synchronizedList(new ArrayList<>());

        started = true;
        IM.onBg().execute(this::fillQueue);
    }

    @WorkerThread
    private boolean offerJob(Job<?, ?> job) {
        int max = asynchronous ? poolSize : 1;

        if (processing.size() >= max) {
            return false;
        }

        awaiting.remove(job);
        processing.add(job);

        job.startJob(this);

        return true;
    }

    @WorkerThread
    void finishJob(JobResult<?, ?> jobResult) {
        processing.remove(jobResult.job);
        finished.add(jobResult.job);

        // handle post actions

        if (awaiting.size() > 0 && !cancelled) {
            fillQueue();
        }

        if (processing.size() == 0 && awaiting.size() == 0 && !cancelled) {
            // dispatch finished
        }
    }

    @WorkerThread
    private void fillQueue() {
        if (asynchronous) {
            for (Job<?, ?> job : new ArrayList<>(awaiting)) {
                if (!offerJob(job)) {
                    break;
                }
            }
        } else {
            offerJob(awaiting.get(0));
        }
    }

    public synchronized void cancel() {
        // handle other clean up
    }

    public enum FailureAction {
        ABORT, IGNORE
    }

}
