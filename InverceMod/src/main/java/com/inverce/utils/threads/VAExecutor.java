package com.inverce.utils.threads;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class VAExecutor extends IMThreadsBase {

    private static VAExecutor instance;
    final Object mLock = new Object();
    final Handler uiHandler;
    final VThread threadRoot;
    ArrayList<VThread> workerPool;
    final ArrayList<Long> pastClears, mBlackList;
    final int workerLeaseTime = 16000, minWorkerLeaseTime = 1000, leaseDecrementPower = 4, MAX_BLACK_LIST_SIZE = 20;
    // note in miliseconds, how much time must pass for worker to be released (default 500 ms), should not be less than ~50
    static boolean isDebugMode = false;

    private VAExecutor() {
        workerPool = new ArrayList<>();
        uiHandler = new Handler(Looper.getMainLooper());
        threadRoot = new VThread();
        threadRoot.start();
        pastClears = new ArrayList<>(10);
        mBlackList = new ArrayList<>(20);
    }

    public static VAExecutor get() {
        return instance != null ? instance : (instance = new VAExecutor());
    }

    static public boolean isOnMainThread() {
        try {
            return Looper.getMainLooper().getThread() == Thread.currentThread();
        } catch (Exception ignored) {
            return false;
        }
    }

    public VRunWrapper wrap(Runnable run) {
        return (run instanceof VRunWrapper) ? (VRunWrapper) run : new VRunWrapper(run);
    }

    protected String log(String msg) {
        // if debug
//        Log.e("Executors", msg);
        return msg;
    }

    @Nullable
    public ScheduledFuture<?> scheduleUI(@NonNull Runnable command) {
        if (checkBlackList(command))
            return null;
        if (isOnMainThread()) {
            command.run();
            if (command instanceof VARunnable) {
                ((VARunnable) command).clear();
            }
        } else {
            uiHandler.post(new VRunWrapper(command, true));
        }
        return null;
    }

    public ScheduledFuture<?> scheduleUI(@NonNull Runnable command, long delay, @NonNull TimeUnit unit) {
        if (checkBlackList(command))
            return null;

        return queueRaw(new VScheduledFuture(wrap(command), now() + MILLISECONDS.convert(delay, unit)).runOnUi());
    }

    @Nullable
    public ScheduledFuture<?> scheduleUI(@NonNull Runnable command, long delayMillis) {
        if (checkBlackList(command))
            return null;
        return scheduleUI(command, delayMillis, MILLISECONDS);
    }

    public void execute(@NonNull Runnable command) {
        if (checkBlackList(command))
            return;
        queueRaw(new VScheduledFuture(wrap(command), now()));
    }

    @Override
    public ScheduledFuture<?> schedule(@NonNull Runnable command, long delay, @NonNull TimeUnit unit) {
        if (checkBlackList(command))
            return null;
        return queueRaw(new VScheduledFuture(wrap(command), now() + MILLISECONDS.convert(delay, unit)));
    }

    @Override
    public <V> ScheduledFuture<V> schedule(@NonNull Callable<V> callable, long delay, @NonNull TimeUnit unit) {
        throw new UnsupportedOperationException(log("Callable not supported yet"));
    }

    @Override
    public ScheduledFuture<?> scheduleAtFixedRate(@NonNull Runnable command, long initialDelay, long period, @NonNull TimeUnit unit) {
        if (checkBlackList(command))
            return null;
        return queueRaw(new VScheduledFuture(wrap(command), now() + initialDelay, MILLISECONDS.convert(period, unit)));
    }

    @Override
    public ScheduledFuture<?> scheduleWithFixedDelay(@NonNull Runnable command, long initialDelay, long delay, @NonNull TimeUnit unit) {
        if (checkBlackList(command))
            return null;
        return scheduleAtFixedRate(command, initialDelay, delay, unit);
    }

    @NonNull
    @Override
    public <T> Future<T> submit(@NonNull Callable<T> task) {
        throw new UnsupportedOperationException(log("Callable not supported yet"));
    }

    @NonNull
    @Override
    public <T> Future<T> submit(@NonNull Runnable task, T result) {
        throw new UnsupportedOperationException(log("Submiting to queue not supported"));
    }

    @NonNull
    @Override
    public Future<?> submit(@NonNull Runnable task) {
        if (checkBlackList(task))
            return null;
        return queueRaw(new VScheduledFuture(wrap(task), now()));
    }

    @NonNull
    @Override
    public <T> List<Future<T>> invokeAll(@NonNull Collection<? extends Callable<T>> tasks) throws InterruptedException {
        throw new UnsupportedOperationException(log("Multi invoke not supported"));
    }

    @NonNull
    @Override
    public <T> List<Future<T>> invokeAll(@NonNull Collection<? extends Callable<T>> tasks, long timeout, @NonNull TimeUnit unit) throws InterruptedException {
        throw new UnsupportedOperationException(log("Multi invoke not supported"));
    }

    @NonNull
    @Override
    public <T> T invokeAny(@NonNull Collection<? extends Callable<T>> tasks) throws InterruptedException, ExecutionException {
        throw new UnsupportedOperationException(log("Multi invoke not supported"));
    }

    @Override
    public <T> T invokeAny(@NonNull Collection<? extends Callable<T>> tasks, long timeout, @NonNull TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        throw new UnsupportedOperationException(log("Multi invoke not supported"));
    }

    public void cancelTasks(long groupId, boolean mayInterruptIfRunning) {
        cancelTasks(groupId, -1, mayInterruptIfRunning);
    }

    public synchronized void cancelTasks(long groupId, long taskId, boolean mayInterrupt) {
        synchronized (mLock) {
            for (VScheduledFuture task : threadRoot.getQueue()) {
                if (groupId != -1 && groupId == task.groupId || taskId != -1 && taskId == task.taskId) {
                    task.cancel(mayInterrupt);
                    threadRoot.getQueue().remove(task);
                }
            }

            for (VThread thread : workerPool) {
                if (thread.isAvailable()) {
                    synchronized (thread.mLock) {
                        for (VScheduledFuture task : thread.getQueue()) {
                            if (groupId != -1 && groupId == task.groupId || taskId != -1 && taskId == task.taskId) {
                                task.cancel(mayInterrupt);
                                thread.getQueue().remove(task);
                            }
                        }

                        if (thread.currentFeature != null && (groupId != -1 && groupId == thread.currentFeature.groupId || taskId != -1 && taskId == thread.currentFeature.taskId)) {
                            thread.currentFeature.cancel(mayInterrupt);
                            thread.currentFeature = null;
                            thread.expectedInterrupt = true;

                            thread.isClosing = true;
                            if (!thread.getQueue().isEmpty()) {
                                for (VScheduledFuture task : thread.getQueue()) {
                                    VAExecutor.get().queueRaw(task);
                                }
                            }

                            thread.interrupt();
                        } else if ((groupId != -1 && groupId == thread.groupId || taskId != -1 && taskId == thread.taskId) && thread.getQueue().isEmpty()) {
                            thread.currentFeature = null;
                            thread.expectedInterrupt = true;
                            thread.isClosing = true;
                            thread.interrupt();
                        }
                    }
                }
            }
            if (!pastClears.contains(groupId)) {
                pastClears.add(groupId);
                while (pastClears.size() > 10) { pastClears.remove(0); }
            }
        }
    }

    public boolean isExist(long taskId) {
        synchronized (mLock) {
            for (VScheduledFuture vScheduledFuture : threadRoot.getQueue()) {
                if (vScheduledFuture.taskId == taskId)
                    return true;
            }

            for (VThread thread : workerPool) {
                if (thread.isAvailable()) {
                    synchronized (thread.mLock) {
                        for (VScheduledFuture vScheduledFuture : thread.getQueue()) {
                            if (vScheduledFuture.taskId == taskId)
                                return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    void runOnWorkerInternal(VScheduledFuture task) throws InterruptedException {
        synchronized (mLock) {
            boolean taskOffered = false;
            for (int i = 0; i < workerPool.size(); i++) {
                VThread worker = workerPool.get(i);
                if (worker != null && worker.isAvailable() && worker.offerTask(task)) {
                    taskOffered = true;
                    break;
                }
            }

            if (!taskOffered) {

                if (workerPool.size() == 0) {
                    VThread.taskCounter.set(0);
                }

                int lease = (int) Math.max(minWorkerLeaseTime, workerLeaseTime / Math.pow(workerPool.size(), leaseDecrementPower));
                VThread newWorker = new VThread(lease, task);
                workerPool.add(newWorker);
                newWorker.start();
            }
        }
    }

    protected VScheduledFuture queueRaw(VScheduledFuture task) {
        synchronized (mLock) {
            if (mBlackList.contains(task.groupId)) {
                task.cancel(true);
                return null;
            }
        }
        threadRoot.getQueue().offer(task);
        synchronized (threadRoot) {
            threadRoot.notify();
        }
        return task;
    }

    public void blackList(long groupId) {
        synchronized (mLock) {
            mBlackList.add(groupId);
            while (mBlackList.size() > MAX_BLACK_LIST_SIZE) { mBlackList.remove(0); }
        }
    }

    protected boolean checkBlackList(Runnable command) {
        if (command == null) {
            return true;
        }
        synchronized (mLock) {
            if (command instanceof VARunnable && mBlackList.contains(((VARunnable) command).groupId))
                return true;
            if (command instanceof VScheduledFuture && mBlackList.contains(((VScheduledFuture) command).groupId))
                return true;
        }
        return false;
    }

    void removeFromPool(VThread worker) throws InterruptedException {
        synchronized (mLock) {
            workerPool.remove(worker);
        }
    }

    static class VRunWrapper implements Runnable {
        public boolean runOnUi;
        Runnable inner;
        boolean clearAfterRun = false;
        long groupId = -1, taskId = -1;

        public VRunWrapper(Runnable inner, boolean clearAfterRun) {
            this.inner = inner;
            this.clearAfterRun = clearAfterRun;
            if (inner instanceof VARunnable) {
                groupId = ((VARunnable) inner).groupId;
                taskId = ((VARunnable) inner).taskId;
            }
        }

        public VRunWrapper(Runnable inner) {
            this(inner, false);
        }

        public void run() {
            if (inner != null) {
                if (runOnUi) {
                    VAExecutor.get().scheduleUI(inner);
                } else {
                    inner.run();
                }
            }
            if (clearAfterRun) {
                clear();
            }
        }

        public void clear() {
            if (inner != null && inner instanceof VARunnable) {
                //    ((VARunnable) inner).clear();
            }
            inner = null;
        }
    }

}