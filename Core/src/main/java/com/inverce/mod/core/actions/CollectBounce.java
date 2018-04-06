package com.inverce.mod.core.actions;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Log;
import com.inverce.mod.core.functional.Aggregator;
import com.inverce.mod.core.functional.IConsumer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"WeakerAccess", "unused"})
public class CollectBounce<T> {
    @Nullable
    protected ScheduledFuture<?> feature;
    protected IConsumer<List<T>> report;
    protected long delay = 500;
    protected List<T> events;
    protected boolean emitOnDelay;

    public CollectBounce() {
        events = Collections.synchronizedList(new ArrayList<>());
    }

    @NonNull
    public CollectBounce<T> setConsumer(@NonNull IConsumer<List<T>> report) {
        this.report = report;
        return this;
    }

    @NonNull
    public CollectBounce<T> setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    @NonNull
    public CollectBounce<T> emitAfterDelay() {
        this.emitOnDelay = true;
        return this;
    }

    @NonNull
    public CollectBounce<T> emitWhenNoNewEvent() {
        this.emitOnDelay = false;
        return this;
    }

    private boolean defaultEquals(@Nullable T a, T b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static <T> Aggregator<T> aggregateUseNew() { return (A, B) -> B; }
    public static <T> Aggregator<T> aggregateUseOld() { return (A, B) -> B; }

    public synchronized void post(T element) {
        events.add(element);

        if (!emitOnDelay) {
            cancel();
        }

        if (!emitOnDelay || feature == null) {
            scheduleTask();
        }
    }

    private synchronized void internalReport() {
        if (events != null) {
            report.accept(events);
            events.clear();
            Log.w("reporting event");
            feature = null;
        }
    }

    public void cancel() {
        if (feature != null) {
            feature.cancel(false);
            feature = null;
        }
    }

    private void scheduleTask() {
        cancel();
        feature = IM.onBg().schedule(this::internalReport, delay, TimeUnit.MILLISECONDS);
    }
}
