package com.inverce.mod.actions;

import android.view.View;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Log;
import com.inverce.mod.core.functional.IBiFunction;
import com.inverce.mod.core.functional.IConsumer;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

@SuppressWarnings({"WeakerAccess", "unused"})
public class DeBounce<T> {
    private ScheduledFuture<?> feature;
    private IConsumer<T> report;
    private long delay;
    private T evt;

    public DeBounce(int delayMs) {
        this.delay = delayMs;
    }

    private boolean defaultEquals(T a, T b) {
        return (a == b) || (a != null && a.equals(b));
    }

    public static <T> Aggregator<T> aggregateUseNew() { return (A, B) -> B; }
    public static <T> Aggregator<T> aggregateUseOld() { return (A, B) -> B; }

    public synchronized void report(T element, IConsumer<T> report) {
        report(element, this::defaultEquals, aggregateUseNew(), report);
    }

    public synchronized void report(T element, IsEqual<T> equals, IConsumer<T> report) {
        report(element, equals, aggregateUseNew(), report);
    }

    public synchronized void report(T element, Aggregator<T> aggregate, IConsumer<T> report) {
        report(element, this::defaultEquals, aggregate, report);
    }

    public synchronized void report(T element, IsEqual<T> equals, Aggregator<T> aggregate, IConsumer<T> report) {
        if (evt != null) {
            if (!equals.apply(evt, element)) {
                // last is not the same as this one we should report it immediately
                internalReport();
                cancel();
                this.evt = element;
                this.report = report;
                scheduleTask();
            } else {
                this.evt = aggregate.apply(evt, element);
                this.report = report;
                scheduleTask();
            }
        } else {
            this.evt = element;
            this.report = report;
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

    public interface Aggregator<T> extends IBiFunction<T, T, T> {  }
    public interface IsEqual<T> extends IBiFunction<T, T, Boolean> {  }
    public static View.OnClickListener deBounce(int ms, View.OnClickListener onClickListener) {
        return new View.OnClickListener() {
            DeBounce<View> deBounce = new DeBounce<>(ms);
            @Override
            public void onClick(View v) {
                deBounce.report(v, onClickListener::onClick);
            }
        };
    }
}
