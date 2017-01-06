package com.inverce.mod.events;

import java.util.List;


/* WIP */
public abstract class AsyncFeature<T> {
    AsyncFeature() { }

    void invoke() { }

    public static <Y> AsyncFeature<Y> yield(Y el) {
        return new AsyncSingleResult<>(el);
    }

    public static <Y> AsyncFeature<Y> yieldMany(List<Y> elements) {
        return new AsyncMultiResult<>(elements);
    }

    public abstract void onResult();
    public boolean onException(Throwable t) {
        return false;
    }

    static class AsyncMultiResult<T> extends AsyncFeature<T> {
        private List<T> data;
        public AsyncMultiResult(List<T> data) { this.data = data; }
        @Override public void onResult() { throw new IllegalStateException(); }
    }

    static class AsyncSingleResult<T> extends AsyncFeature<T> {
        private T data;
        public AsyncSingleResult(T data) { this.data = data; }
        @Override public void onResult() { throw new IllegalStateException(); }
    }

}
