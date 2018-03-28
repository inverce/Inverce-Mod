package com.inverce.mod.processing;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.Callable;

public interface Processor<ITEM, RESULT> {
    @NonNull
    RESULT processJob(ITEM item) throws Exception;

    Processor<Callable<?>, ?> CALLABLE = (callable) -> callable.call();
    @Nullable
    Processor<Runnable, Void> RUNNABLES = p -> {
        p.run();
        return null;
    };

    static class EX {
        static <T, R, M> Processor<T, R> map(@NonNull Processor<M, R> processor, @NonNull Processor<T, M> mapper) {
            return t -> processor.processJob(mapper.processJob(t));
        }
    }
}
