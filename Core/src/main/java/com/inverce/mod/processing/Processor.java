package com.inverce.mod.processing;

import java.util.concurrent.Callable;

public interface Processor<ITEM, RESULT> {
    RESULT processJob(ITEM item) throws Exception;

    Processor<Callable<?>, ?> CALLABLE = (callable) -> callable.call();
    Processor<Runnable, Void> RUNNABLES = p -> {
        p.run();
        return null;
    };

    static class EX {
        static <T, R, M> Processor<T, R> map(Processor<M, R> processor, Processor<T, M> mapper) {
            return t -> processor.processJob(mapper.processJob(t));
        }
    }
}
