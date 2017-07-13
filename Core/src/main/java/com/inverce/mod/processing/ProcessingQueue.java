package com.inverce.mod.processing;

import android.support.annotation.WorkerThread;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.threadpool.NamedThreadPool;
import com.inverce.mod.events.Event;
import com.inverce.mod.events.annotation.Listener;
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
    boolean asynchronous, started, cancelled;
    FailureAction failureAction;
    ThreadFactory threadFactory;
    int poolSize = 8;
    Events events;

    public static ProcessingQueue create() {
        return new ProcessingQueue();
    }

    private ProcessingQueue() {
        awaiting = Collections.synchronizedList(new ArrayList<>());
        activeThreads = Collections.synchronizedList(new ArrayList<>());
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

    public ProcessingQueue setEventsListener(Events events) {
        if (events == null) {
            events = new Event<>(Events.class).post();
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
        checkArgument(!started);
        checkArgument(awaiting.size() > 0, "You need to add at least one item to process");

        processing = Collections.synchronizedList(new ArrayList<>());
        finished = Collections.synchronizedList(new ArrayList<>());

        started = true;
        IM.onBg().execute(this::fillQueue);
        events.onQueueStarted();
    }

    @WorkerThread
    private boolean offerJob(Job<?, ?> job) {
        int max = asynchronous ? poolSize : 1;

        if (processing.size() >= max) {
            return false;
        }

        awaiting.remove(job);
        processing.add(job);

        Thread thread = threadFactory.newThread(() -> {
            job.consume(ProcessingQueue.this);
        });

        thread.start();
        activeThreads.add(thread);

        events.onJobStarted(job);
        return true;
    }

    @WorkerThread
    void finishJob(JobResult<?, ?> jobResult) {
        processing.remove(jobResult.job);
        finished.add(jobResult.job);

        events.onJobFinished(jobResult);

        if (awaiting.size() > 0 && !cancelled) {
            fillQueue();
        }

        if (processing.size() == 0 && awaiting.size() == 0 && !cancelled) {
            events.onQueueFinished();
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
        this.cancelled = true;

        for (Thread t: activeThreads) {
            synchronized (t) {
                t.interrupt();
            }
        }

        events.onQueueCancelled();
    }

    public enum FailureAction {
        ABORT, IGNORE
    }

    public interface Events extends Listener {
        void onQueueFinished();
        void onQueueStarted();
        void onQueueCancelled();
        void onJobFinished(JobResult<?, ?> job);
        void onJobStarted(Job<?, ?> job);
    }
}
