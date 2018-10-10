package com.inverce.mod.processing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.WorkerThread;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.functional.IConsumer;
import com.inverce.mod.core.threadpool.NamedThreadPool;
import com.inverce.mod.processing.Processor.EX;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import static com.inverce.mod.core.verification.Preconditions.checkArgument;
import static com.inverce.mod.core.verification.Preconditions.checkNotNull;
import static com.inverce.mod.core.verification.Preconditions.checkState;
import static com.inverce.mod.v2.core.verification.Preconditions.checkArgument;
import static com.inverce.mod.v2.core.verification.Preconditions.checkState;

@SuppressWarnings({"unused", "WeakerAccess"})
public class ProcessingQueue {
    protected List<Job<?, ?>> processing, awaiting;
    protected List<Thread> activeThreads;
    protected Settings cfg;
    protected QueueListener events;

    public static ProcessingQueue create() {
        return new ProcessingQueue();
    }

    private ProcessingQueue() {
        awaiting = Collections.synchronizedList(new ArrayList<>());
        activeThreads = Collections.synchronizedList(new ArrayList<>());
        processing = Collections.synchronizedList(new ArrayList<>());
        events = new QueueListenerAdapter();
        cfg = new Settings();
        cfg.asynchronous = true;
        cfg.failureAction = FailureAction.ABORT;
        cfg.threadFactory = new NamedThreadPool("ProcessingQueue#" + hashCode());
    }

    @NonNull
    public ProcessingQueue setAsynchronous(boolean asynchronous) {
        checkState(!cfg.isStarted, "ProcessingQueue already isStarted");
        cfg.asynchronous = asynchronous;
        return this;
    }

    @NonNull
    public ProcessingQueue setPoolSize(int poolSize) {
        checkArgument(poolSize > 0, "Pool size must be greater than 0");
        cfg.poolSize = poolSize;
        return this;
    }

    @NonNull
    public ProcessingQueue setFailureAction(FailureAction failureAction) {
        checkState(!cfg.isStarted, "ProcessingQueue already isStarted");
        cfg.failureAction = failureAction;
        return this;
    }

    @NonNull
    public ProcessingQueue setThreadFactory(@NonNull ThreadFactory threadFactory) {
        checkState(!cfg.isStarted, "ProcessingQueue already isStarted");
        checkNotNull(threadFactory, "Factory cannot be null");
        cfg.threadFactory = threadFactory;
        return this;
    }

    @NonNull
    public ProcessingQueue setContinuous(boolean continuous) {
        checkState(!cfg.isStarted, "ProcessingQueue already isStarted");
        cfg.isContinuous = continuous;
        return this;
    }

    public List<Job<?, ?>> getProcessing() {
        return Collections.unmodifiableList(new ArrayList<>(processing));
    }

    public List<Job<?, ?>> getAwaiting() {
        return Collections.unmodifiableList(new ArrayList<>(awaiting));
    }

    @NonNull
    public ProcessingQueue setListener(@Nullable QueueListener events) {
        this.events = events == null ? new QueueListenerAdapter() : events;
        return this;
    }

    @NonNull
    public <T> ProcessingQueue processTask(@NonNull TaskMapper<T> handler, @NonNull List<T> list) {
        return processInternal(EX.map(Processor.RUNNABLES, handler::processJob), list, false);
    }

    @NonNull
    public <T> ProcessingQueue process(@NonNull IConsumer<T> handler, @NonNull List<T> list) {
        return processInternal(EX.map(Processor.RUNNABLES, o -> () -> handler.accept(o)), list, false);
    }

    @NonNull
    public <T, R> ProcessingQueue process(@NonNull Processor<T, R> processor, @NonNull List<T> list) {
        return processInternal(processor, list, false);
    }

    @NonNull
    public <T> ProcessingQueue processTaskIfNotAdded(@NonNull TaskMapper<T> handler, @NonNull List<T> list) {
        return processInternal(EX.map(Processor.RUNNABLES, handler::processJob), list, true);
    }

    @NonNull
    public <T> ProcessingQueue processIfNotAdded(@NonNull IConsumer<T> handler, @NonNull List<T> list) {
        return processInternal(EX.map(Processor.RUNNABLES, o -> () -> handler.accept(o)), list, true);
    }

    @NonNull
    public <T, R> ProcessingQueue processIfNotAdded(@NonNull Processor<T, R> processor, @NonNull List<T> list) {
        return processInternal(processor, list, true);
    }

    @NonNull
    <T, R> ProcessingQueue processInternal(@NonNull Processor<T, R> processor, @NonNull List<T> list, boolean checkExist) {
        checkNotNull(processor, "Processor connot be null");
        checkNotNull(list, "You must specify elements");
        checkArgument(!cfg.isCancelled, "Cant add task to cancelled queue");
        checkArgument(!cfg.isDone || cfg.isContinuous, "Adding more task after queue started supported with continous mode");

        for (T item : list) {
            if (!(checkExist && contains(item))) {
                awaiting.add(new Job<>(item, processor));
            }
        }

        if (cfg.isContinuous && cfg.isStarted) {
            IM.onBg().execute(this::fillQueue);
        }

        return this;
    }


    public boolean isStarted() {
        return cfg.isStarted;
    }

    public boolean isCancelled() {
        return cfg.isCancelled;
    }

    public boolean isFinished() {
        return cfg.isDone && !cfg.isContinuous;
    }

    public synchronized void start() {
        checkArgument(!cfg.isStarted);
        checkArgument(!cfg.isDone);
        checkArgument(awaiting.size() > 0, "You need to add at least one item to process");

        cfg.isStarted = true;
        IM.onBg().execute(this::fillQueue);
        events.onQueueStarted(this);
    }

    @WorkerThread
    private synchronized boolean offerJob(@NonNull Job<?, ?> job) {
        int max = cfg.asynchronous ? cfg.poolSize : 1;

        if (processing.size() >= max) {
            return false;
        }

        awaiting.remove(job);
        processing.add(job);

        Thread thread = cfg.threadFactory.newThread(() -> {
            job.accept(ProcessingQueue.this);
        });

        job.thread = thread;
        thread.start();
        activeThreads.add(thread);

        events.onJobStarted(this, job.item, job.processor);
        return true;
    }

    @WorkerThread
    synchronized void finishJob(@NonNull JobResult<?, ?> jobResult) {
        processing.remove(jobResult.job);
        activeThreads.remove(jobResult.job.thread);

        if (jobResult.exception == null) {
            events.onJobResult(this, jobResult.job, jobResult.result);
        } else {
            events.onJobFailure(this, jobResult.job, jobResult.exception);
        }

        if (jobResult.exception != null && cfg.failureAction == FailureAction.ABORT) {
            cancel();
            cfg.isDone = true;
//            events.onQueueFinished(this);
            return;
        }

        if (awaiting.size() > 0 && !cfg.isCancelled) {
            fillQueue();
        }

        if (processing.size() == 0 && awaiting.size() == 0 && !cfg.isCancelled) {
            cfg.isDone = true;
            events.onQueueFinished(this);
        }
    }

    @WorkerThread
    private synchronized void fillQueue() {
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

    public synchronized boolean contains(Object item) {
        for (Job<?, ?> job: new ArrayList<>(processing)) {
            if (job.getItem().equals(item)) {
                return true;
            }
        }
        for (Job<?, ?> job: new ArrayList<>(awaiting)) {
            if (job.getItem().equals(item)) {
                return true;
            }
        }
        return false;
    }


    /**
     * This will NOT cancel the queue
     * @param item
     * @return
     */
    public synchronized boolean cancelItem(Object item) {
        for (Job<?, ?> job: new ArrayList<>(awaiting)) {
            if (job.getItem().equals(item)) {
                awaiting.remove(job);
                return true;
            }
        }
        for (Job<?, ?> job: new ArrayList<>(processing)) {
            if (job.getItem().equals(item)) {
                synchronized (job.getThread()) {
                    job.getThread().interrupt();
                }
                activeThreads.remove(job.thread);
                processing.remove(job);
                return true;
            }
        }
        return false;
    }

    public synchronized void cancel() {
        cfg.isCancelled = true;
        cfg.isFinishing = true;
        cfg.isStarted = false;
        cfg.isDone = !cfg.isContinuous;

        for (Thread t: activeThreads) {
            synchronized (t) {
                t.interrupt();
            }
        }
        activeThreads.clear();

        // give queue a moment to handle thread interruptions
        IM.onBg().schedule(() -> {
            events.onQueueCancelled(this);
            cfg.isFinishing = false;
        }, 100, TimeUnit.MILLISECONDS);
    }

    public enum FailureAction {
        ABORT, IGNORE
    }

    private static class Settings {
        boolean asynchronous, isContinuous,
                isStarted, isCancelled, isDone, isFinishing, isWaitingToStart;
        FailureAction failureAction;
        ThreadFactory threadFactory;
        int poolSize = 8;
    }
}
