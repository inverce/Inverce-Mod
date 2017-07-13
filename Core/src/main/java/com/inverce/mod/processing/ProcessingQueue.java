package com.inverce.mod.processing;

import android.support.annotation.WorkerThread;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.threadpool.NamedThreadPool;
import com.inverce.mod.events.Event;
import com.inverce.mod.processing.Processor.EX;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.function.Consumer;

import static com.inverce.mod.core.verification.Preconditions.checkArgument;
import static com.inverce.mod.core.verification.Preconditions.checkNotNull;
import static com.inverce.mod.core.verification.Preconditions.checkState;

public class ProcessingQueue {
    List<Job<?, ?>> processing, finished, awaiting;
    List<Thread> activeThreads;
    Settings cfg;
    QueueListener events;

    public static ProcessingQueue create() {
        return new ProcessingQueue();
    }

    private ProcessingQueue() {
        awaiting = Collections.synchronizedList(new ArrayList<>());
        activeThreads = Collections.synchronizedList(new ArrayList<>());
        cfg = new Settings();
        cfg.asynchronous = false;
        cfg.failureAction = FailureAction.ABORT;
        cfg.threadFactory = new NamedThreadPool("ProcessingQueue#" + hashCode());
    }

    public ProcessingQueue setAsynchronous(boolean asynchronous) {
        checkState(!cfg.isStarted, "ProcessingQueue already isStarted");
        cfg.asynchronous = asynchronous;
        return this;
    }

    public ProcessingQueue setPoolSize(int poolSize) {
        checkArgument(poolSize < 1, "Pool size must be greater than 0");
        cfg.poolSize = poolSize;
        return this;
    }

    public ProcessingQueue setFailureAction(FailureAction failureAction) {
        checkState(!cfg.isStarted, "ProcessingQueue already isStarted");
        cfg.failureAction = failureAction;
        return this;
    }

    public ProcessingQueue setThreadFactory(ThreadFactory threadFactory) {
        checkState(!cfg.isStarted, "ProcessingQueue already isStarted");
        checkNotNull(threadFactory, "Factory cannot be null");
        cfg.threadFactory = threadFactory;
        return this;
    }

    public ProcessingQueue setListener(QueueListener events) {
        if (events == null) {
            events = new Event<>(QueueListener.class).post();
        }
        this.events = events;
        return this;
    }

    public <T> ProcessingQueue processTask(TaskMapper<T> handler, List<T> list) {
        return process(EX.map(Processor.RUNNABLES, handler::processJob), list);
    }

    public <T> ProcessingQueue process(Consumer<T> handler, List<T> list) {
        return process(EX.map(Processor.RUNNABLES, o -> () -> handler.accept(o)), list);
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
        checkArgument(!cfg.isStarted);
        checkArgument(!cfg.isDone);
        checkArgument(awaiting.size() > 0, "You need to add at least one item to process");

        processing = Collections.synchronizedList(new ArrayList<>());
        finished = Collections.synchronizedList(new ArrayList<>());

        cfg.isStarted = true;
        IM.onBg().execute(this::fillQueue);
        events.onQueueStarted(this);
    }

    @WorkerThread
    private boolean offerJob(Job<?, ?> job) {
        int max = cfg.asynchronous ? cfg.poolSize : 1;

        if (processing.size() >= max) {
            return false;
        }

        awaiting.remove(job);
        processing.add(job);

        Thread thread = cfg.threadFactory.newThread(() -> {
            job.consume(ProcessingQueue.this);
        });

        thread.start();
        activeThreads.add(thread);

        events.onJobStarted(this, job.item, job.processor);
        return true;
    }

    @WorkerThread
    void finishJob(JobResult<?, ?> jobResult) {
        processing.remove(jobResult.job);
        finished.add(jobResult.job);

        events.onJobFinished(this, jobResult);

        if (awaiting.size() > 0 && !cfg.isCancelled) {
            fillQueue();
        }

        if (processing.size() == 0 && awaiting.size() == 0 && !cfg.isCancelled) {
            cfg.isDone = true;
            events.onQueueFinished(this);
        }
    }

    @WorkerThread
    private void fillQueue() {
        if (cfg.asynchronous) {
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
        cfg.isCancelled = true;

        for (Thread t: activeThreads) {
            synchronized (t) {
                t.interrupt();
            }
        }

        events.onQueueCancelled(this);
    }

    public enum FailureAction {
        ABORT, IGNORE
    }

    private static class Settings {
        boolean asynchronous, isStarted, isCancelled, isDone;
        FailureAction failureAction;
        ThreadFactory threadFactory;
        int poolSize = 8;
    }
}
