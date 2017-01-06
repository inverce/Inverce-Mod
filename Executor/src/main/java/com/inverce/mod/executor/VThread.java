package com.inverce.mod.executor;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

class VThread extends Thread {
    static AtomicInteger taskCounter = new AtomicInteger(0);
    final int selfCloseTimeout;
    final int threadId;
    final Object mLock = new Object();
    PriorityBlockingQueue<VScheduledFuture> queue;
    VScheduledFuture currentFeature;
    boolean isMain, isClosing, isRunningTask, expectedInterrupt = false;
    long groupId, taskId;

    public VThread(boolean isMain, int selfCloseTimeout, VScheduledFuture firstTask) {
        super((Runnable) null, isMain ? "thread-task-watcher" : "va-ad-pool-0-thread-" + taskCounter.getAndIncrement());
        this.threadId = taskCounter.get();
        this.queue = new PriorityBlockingQueue<>(30, new Comparator<VScheduledFuture>() {
            @Override
            public int compare(VScheduledFuture lhs, VScheduledFuture rhs) {
                return lhs.compareTo(rhs);
            }
        });
        if (firstTask != null) {
            queue.offer(firstTask);
        }

        this.isMain = isMain;
        this.isClosing = false;
        this.isRunningTask = false;
        this.selfCloseTimeout = selfCloseTimeout;
    }

    /**
     * Create root thread
     */
    public VThread() {
        this(true, 0, null);
    }

    /**
     * Create worker thread
     *
     * @param task first task to run on worker
     */
    public VThread(int selfCloseTimeout, VScheduledFuture task) {
        this(false, selfCloseTimeout, task);
    }

    public PriorityBlockingQueue<VScheduledFuture> getQueue() {
        return queue;
    }


    @Override
    public void run() {
        super.run();
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_BACKGROUND);

        try {
            for (; ; ) {
                VScheduledFuture task = (isMain) ? queue.take() : queue.poll(selfCloseTimeout, TimeUnit.MILLISECONDS);
                if (task == null) {
                    synchronized (mLock) {
                        isClosing = true;
                    }
                    VAExecutor.get().removeFromPool(this);
                    /* we need to stop */
                    break;
                } else if (task.isCancelled()) {
                    /* meh task was canceled, we ignore */
                    VAExecutor.get().log("Task was canceled ignoring");
                } else {
                    long delay = task.getDelay(TimeUnit.MILLISECONDS);
                    if (delay <= 0) {
                        if (isMain) {
//                                Log.w("Watcher", "Giving task to pool");
                            VAExecutor.get().runOnWorkerInternal(task); // give task to worker so this one want be used
                        } else {
                            try {
                                synchronized (mLock) {
                                    isRunningTask = true;
                                    currentFeature = task;
                                    groupId = task.groupId;
                                    taskId = task.taskId;
                                }
                                task.run();
//                                } catch (InterruptedException iEx) { throw iEx; } // rethrow
                            } catch (Exception ex) {
                                if (!task.isDone()) { // if task had exception, cancel it
                                    task.cancel(true);
                                }
                                if (VAExecutor.isDebugMode) {
                                    ex.printStackTrace();
                                }
                            } finally {
                                synchronized (mLock) {
                                    isRunningTask = false;
                                    currentFeature = null;
                                }
                            }
                        }
                    } else {
                        queue.put(task); // put back what we have taken, as this is not time for this fellow
                        synchronized (this) {
                            wait(delay);
                        }
                    }

                }
            }
        } catch (InterruptedException e) {
            this.isClosing = true;
            synchronized (VAExecutor.get().mLock) {
                VAExecutor.get().workerPool.remove(this);
            }
            if (!expectedInterrupt) {
                if (VAExecutor.isDebugMode) {
                    e.printStackTrace();
                }
            }
        }
    }

    public boolean offerTask(VScheduledFuture task) {
        try {
            return queue.offer(task);
        } catch (Exception ex) {
            return false;
        } finally {
            synchronized (this) {
                notify();
            }
        }
    }

    public boolean isAvailable() {
        synchronized (mLock) {
            return !isClosing && !isRunningTask;
        }
    }
}
