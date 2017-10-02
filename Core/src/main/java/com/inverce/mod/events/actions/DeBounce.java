package com.inverce.mod.events.actions;

import android.support.annotation.NonNull;
import android.view.View;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Log;
import com.inverce.mod.core.functional.Aggregator;
import com.inverce.mod.core.functional.IConsumer;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"WeakerAccess", "unused"})
public class DeBounce<T> {
    private ScheduledFuture<?> feature;
    private IConsumer<T> report;
    private long delay = 500;
    private T evt;

    public DeBounce() { }

    public DeBounce<T> setConsumer(IConsumer<T> report) {
        this.report = report;
        return this;
    }

    public DeBounce<T> setDelay(long delay) {
        this.delay = delay;
        return this;
    }

    private boolean defaultEquals(T a, T b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static <T> Aggregator<T> aggregateUseNew() { return (A, B) -> B; }
    public static <T> Aggregator<T> aggregateUseOld() { return (A, B) -> B; }

    public synchronized void post(@NonNull T element) {
        post(element, this::defaultEquals, aggregateUseNew());
    }

    public synchronized void post(@NonNull T element, IsEqual<T> equals) {
        post(element, equals, aggregateUseNew());
    }

    public synchronized void post(@NonNull T element, Aggregator<T> aggregate) {
        post(element, this::defaultEquals, aggregate);
    }

    public synchronized void post(@NonNull T element, IsEqual<T> equals, Aggregator<T> aggregate) {
        if (evt != null) {
            if (!equals.isEqual(evt, element)) {
                // last is not the same as this one we should report it immediately
                internalReport();
                cancel();
                this.evt = element;
                scheduleTask();
            } else {
                this.evt = aggregate.apply(evt, element);
                scheduleTask();
            }
        } else {
            this.evt = element;
            scheduleTask();
        }
    }

    private synchronized void internalReport() {
        if (evt != null) {
            report.accept(evt);
            Log.w("reporting event");
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

    public static View.OnClickListener deBounce(int ms, View.OnClickListener onClickListener) {
        return new View.OnClickListener() {
            DeBounce<View> deBounce = new DeBounce<View>()
                    .setDelay(ms)
                    .setConsumer(onClickListener::onClick);
            @Override
            public void onClick(View v) {
                deBounce.post(v);
            }
        };
    }
}
