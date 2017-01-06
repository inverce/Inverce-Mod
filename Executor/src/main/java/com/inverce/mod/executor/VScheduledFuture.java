package com.inverce.mod.executor;

import android.support.annotation.NonNull;

import java.util.concurrent.Delayed;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RunnableScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static java.util.concurrent.TimeUnit.MICROSECONDS;
import static java.util.concurrent.TimeUnit.MILLISECONDS;

class VScheduledFuture extends FutureTask<Void> implements RunnableScheduledFuture<Void> {
    private static final AtomicLong sequencer = new AtomicLong();
    /**
     * Period in milis for repeating tasks.
     * A positive value indicates fixed-rate execution.
     * A negative value indicates fixed-delay execution.
     * A value of 0 indicates a non-repeating (one-shot) task.
     */
    private final long period, sequenceNumber;
    private final VAExecutor.VRunWrapper wrapper;
    long groupId = -1, taskId = -1;
    boolean runOnUiInstead = false;
    /**
     * The time the task is enabled to execute in milis units
     */
    private long time;

    /**
     * One time
     */
    VScheduledFuture(VAExecutor.VRunWrapper r, long triggerTime) {
        this(r, triggerTime, 0);
    }

    /**
     * Creates a periodic action with given millis-based initial
     * trigger time and period.
     */
    VScheduledFuture(VAExecutor.VRunWrapper r, long triggerTime, long period) {
        super(r, null);
        wrapper = r;
        this.groupId = wrapper.groupId;
        this.taskId = wrapper.taskId;
        this.time = triggerTime;
        this.period = period;
        this.sequenceNumber = sequencer.getAndIncrement();
    }

    public VScheduledFuture runOnUi() {
        this.runOnUiInstead = true;
        this.wrapper.runOnUi = true;
        return this;
    }

    public boolean isPeriodic() {
        return period != 0;
    }

    public long getDelay(@NonNull TimeUnit unit) {
        return unit.convert(time - VAExecutor.now(), MILLISECONDS);
    }

    public int compareTo(@NonNull Delayed other) {
        if (other == this) // compare zero if same object
            return 0;

        if (other instanceof VScheduledFuture) {
            VScheduledFuture x = (VScheduledFuture) other;
            long diff = time - x.time;
            if (diff < 0)
                return -1;
            else if (diff > 0)
                return 1;
            else if (sequenceNumber < x.sequenceNumber)
                return -1;
            else
                return 1;
        }

        long diff = getDelay(MILLISECONDS) - other.getDelay(MICROSECONDS);
        return (diff < 0) ? -1 : (diff > 0) ? 1 : 0;
    }

    private void setNextRunTime() {
        long p = period;
        if (p > 0)
            time += p;
        else
            time = VAExecutor.now() - p;
    }

    public boolean cancel(boolean mayInterruptIfRunning) {
        boolean cancelled = super.cancel(mayInterruptIfRunning);
        wrapper.clear();
        return cancelled;
    }

    public void run() {
        boolean periodic = isPeriodic();
        if (VAExecutor.get().isShutdown())
            cancel(false);
        else if (!periodic) {
            super.run();
            wrapper.clear();
        } else if (super.runAndReset()) {
            setNextRunTime();
            VAExecutor.get().queueRaw(this);
        }
    }

}
