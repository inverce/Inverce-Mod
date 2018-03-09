//package com.inverce.mod.core.collections;
//
//import android.support.annotation.NonNull;
//
//import java.util.concurrent.PriorityBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created on 22/02/2018.
// */
//
//public class LimitedPriorytyQueue {
//    package eurocash.pl.leo.core.lists;
//
//import android.support.annotation.NonNull;
//
//import java.util.concurrent.PriorityBlockingQueue;
//import java.util.concurrent.ThreadPoolExecutor;
//import java.util.concurrent.TimeUnit;
//
//    public class LifoExecutor extends ThreadPoolExecutor {
//        public LifoExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit) {
//            super(corePoolSize, maximumPoolSize, keepAliveTime, unit, new TimeBlockingQueue());
//        }
//        static class TimeBlockingQueue extends PriorityBlockingQueue<Runnable> {
//            public TimeBlockingQueue() {
//                super(5, (arg0, arg1) -> {
//                    Long t1 = arg0 instanceof Pair ? ((Pair) arg0).timestamp : Long.MAX_VALUE;
//                    Long t2 = arg1 instanceof Pair ? ((Pair) arg1).timestamp : Long.MAX_VALUE;
//                    // compare in reverse to get oldest first. Could also do
//                    // -t1.compareTo(t2);
//                    return t2.compareTo(t1);
//                });
//            }
//
//            @Override
//            public boolean add(Runnable r) {
//                if (r instanceof Pair)
//                    return super.add(r);
//                return super.add(new Pair(r));
//            }
//
//            @Override
//            public boolean offer(@NonNull Runnable r) {
//                if (r instanceof Pair)
//                    return super.offer(r);
//                return super.offer(new Pair(r));
//            }
//
//            @Override
//            public boolean offer(Runnable r, long timeout, @NonNull TimeUnit unit) {
//                if (r instanceof Pair)
//                    return super.offer(r, timeout, unit);
//                return super.offer(new Pair(r), timeout, unit);
//            }
//        }
//
//        static class Pair implements Runnable {
//            long timestamp;
//            Runnable runnable;
//
//            Pair(Runnable r) {
//                this.timestamp = System.currentTimeMillis();
//                this.runnable = r;
//            }
//
//            @Override
//            public void run() {
//                this.runnable.run();
//            }
//        }
//
//    }
//
//}
