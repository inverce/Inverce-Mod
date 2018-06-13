package com.inverce.mod.v2.core.threadpool

import android.os.Handler
import android.os.HandlerThread
import java.util.concurrent.Executor

open class DefaultHandlerThread : HandlerThread("Handler-thread", Thread.NORM_PRIORITY), Executor {
    protected val mHandler by lazy { Handler(looper) }

    override fun execute(command: Runnable) {
        mHandler.post(command)
    }
}