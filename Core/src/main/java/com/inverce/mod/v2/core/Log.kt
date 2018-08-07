package com.inverce.mod.v2.core

import android.support.annotation.StringRes
import com.inverce.mod.v2.core.LoggerInternal.Companion.printLocalCauseByStack
import com.inverce.mod.v2.core.LoggerInternal.Companion.printLocalExceptionStack
import com.inverce.mod.v2.core.LoggerInternal.Companion.str
import java.util.*

/**
 * Wrapper around android Log class, with additional benefits.
 */
object Log {
    val logger: Logger = Logger().apply {
        logLevel = VERBOSE
        enable = true
        listener = null
    }

    const val VERBOSE = Logger.VERBOSE
    const val DEBUG = Logger.DEBUG
    const val INFO = Logger.INFO
    const val WARN = Logger.WARN
    const val ERROR = Logger.ERROR
    const val EXCEPTION = Logger.EXCEPTION
    const val ASSERT = Logger.ASSERT
    const val NONE = Logger.NONE

    const val EX_FULL = Logger.EX_FULL
    const val EX_SIMPLER = Logger.EX_SIMPLER
    const val EX_SIMPLEST = Logger.EX_SIMPLEST

    var logLevel: Int?
        get() = logger.logLevel
        set(value) { logger.logLevel = value }

    var enable: Boolean?
        get() = logger.enable
        set(value) { logger.enable = value }

    var listener: LogListener?
        get() = logger.listener
        set(value) { logger.listener = value }


    @JvmOverloads @JvmStatic fun v(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = logger.handleMsg(VERBOSE, tag, message, o)
    @JvmOverloads @JvmStatic fun d(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = logger.handleMsg(DEBUG, tag, message, o)
    @JvmOverloads @JvmStatic fun i(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = logger.handleMsg(INFO, tag, message, o)
    @JvmOverloads @JvmStatic fun w(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = logger.handleMsg(WARN, tag, message, o)
    @JvmOverloads @JvmStatic fun e(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = logger.handleMsg(ERROR, tag, message, o)
    @JvmOverloads @JvmStatic fun a(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = logger.handleMsg(ASSERT, tag, message, o)

    @JvmOverloads @JvmStatic fun v(@StringRes message: Int, vararg o: Any = emptyArray())  = logger.handleMsg(VERBOSE,  null, str(message), o)
    @JvmOverloads @JvmStatic fun d(@StringRes message: Int, vararg o: Any = emptyArray())  = logger.handleMsg(DEBUG,    null, str(message), o)
    @JvmOverloads @JvmStatic fun i(@StringRes message: Int, vararg o: Any = emptyArray())  = logger.handleMsg(INFO,     null, str(message), o)
    @JvmOverloads @JvmStatic fun w(@StringRes message: Int, vararg o: Any = emptyArray())  = logger.handleMsg(WARN,     null, str(message), o)
    @JvmOverloads @JvmStatic fun e(@StringRes message: Int, vararg o: Any = emptyArray())  = logger.handleMsg(ERROR,    null, str(message), o)
    @JvmOverloads @JvmStatic fun a(@StringRes message: Int, vararg o: Any = emptyArray())  = logger.handleMsg(ASSERT,   null, str(message), o)

    @JvmOverloads @JvmStatic fun ex (t: Throwable, message: String? = null, vararg o: Any = emptyArray(), tag: String? = null) = logger.handleExc(EX_FULL, t, tag, message, o)
    @JvmOverloads @JvmStatic fun exs(t: Throwable, message: String? = null, vararg o: Any = emptyArray(), tag: String? = null) = logger.handleExc(EX_SIMPLER, t, tag, message, o)
    @JvmOverloads @JvmStatic fun exm(t: Throwable, message: String? = null, vararg o: Any = emptyArray(), tag: String? = null) = logger.handleExc(EX_SIMPLEST, t, tag, message, o)
    @JvmOverloads @JvmStatic fun ex (t: Throwable, @StringRes message: Int, vararg o: Any = emptyArray(), @StringRes tag: Int = 0) = logger.handleExc(EX_FULL, t, str(tag), str(message), o)
    @JvmOverloads @JvmStatic fun exs(t: Throwable, @StringRes message: Int, vararg o: Any = emptyArray(), @StringRes tag: Int = 0) = logger.handleExc(EX_SIMPLER, t, str(tag), str(message), o)
    @JvmOverloads @JvmStatic fun exm(t: Throwable, @StringRes message: Int, vararg o: Any = emptyArray(), @StringRes tag: Int = 0) = logger.handleExc(EX_SIMPLEST, t, str(tag), str(message), o)
}

open class Logger(protected val baseTag: String = "|>") {
    companion object {
        const val VERBOSE = 2
        const val DEBUG = 3
        const val INFO = 4
        const val WARN = 5
        const val ERROR = 6
        const val EXCEPTION = 7
        const val ASSERT = 8
        const val NONE = Integer.MAX_VALUE

        const val EX_FULL = -1
        const val EX_SIMPLER = -2
        const val EX_SIMPLEST = -3
    }

    /**
     * If null use Log defaults
     */
    var logLevel: Int? = null
    var enable: Boolean? = null
    var listener: LogListener? = null

    private inline fun <T> pVal(pVal: (Logger) -> T?): T? = when {
        this == Log.logger -> pVal(this)
        else -> pVal(this) ?: pVal(Log.logger)
    }

    protected fun dispatchMessage(lvl: Int, tag: String, msg: String) {
        when (lvl) {
            VERBOSE -> android.util.Log.v(tag, msg)
            DEBUG -> android.util.Log.d(tag, msg)
            INFO -> android.util.Log.i(tag, msg)
            WARN -> android.util.Log.w(tag, msg)
            ERROR -> android.util.Log.e(tag, msg)
            EXCEPTION, ASSERT -> handleExc(ASSERT, AssertionError(msg), tag, msg, emptyArray())
        }
    }

    fun shouldLog(lvl: Int) = (pVal { enable } ?: false) && (pVal { logLevel } ?: VERBOSE) <= lvl

    fun handleMsg(lvl: Int, tag: String?, msg: String?, o: Array<out Any>) {
        if (!shouldLog(lvl) || msg == null) return
        if (pVal { listener }?.handleMsg(lvl, tag, msg) == true) return
        dispatchMessage(lvl, "$baseTag${tag ?: ""}", String.format(Locale.getDefault(), msg, o))
    }

    fun handleExc(simple_lvl: Int, t: Throwable, tag: String?, msg: String?, o: Array<out Any>) {
        if (pVal { listener }?.handleExc(simple_lvl, t, tag, msg, o) == true) return
        android.util.Log.e("$baseTag${tag ?: ""}", String.format(Locale.getDefault(), msg ?: "$t", o))
        val pack = context.packageName
        when (simple_lvl) {
            EX_FULL -> t.printStackTrace()
            EX_SIMPLER -> printLocalExceptionStack(tag, pack, t)
            EX_SIMPLEST -> printLocalCauseByStack(tag, 2, t)
        }
    }

    @JvmOverloads fun v(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = handleMsg(VERBOSE, tag, message, o)
    @JvmOverloads fun d(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = handleMsg(DEBUG, tag, message, o)
    @JvmOverloads fun i(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = handleMsg(INFO, tag, message, o)
    @JvmOverloads fun w(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = handleMsg(WARN, tag, message, o)
    @JvmOverloads fun e(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = handleMsg(ERROR, tag, message, o)
    @JvmOverloads fun a(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = handleMsg(ASSERT, tag, message, o)
    @JvmOverloads fun v(@StringRes message: Int, vararg o: Any = emptyArray()) = handleMsg(VERBOSE,  null, str(message), o)
    @JvmOverloads fun d(@StringRes message: Int, vararg o: Any = emptyArray()) = handleMsg(DEBUG,    null, str(message), o)
    @JvmOverloads fun i(@StringRes message: Int, vararg o: Any = emptyArray()) = handleMsg(INFO,     null, str(message), o)
    @JvmOverloads fun w(@StringRes message: Int, vararg o: Any = emptyArray()) = handleMsg(WARN,     null, str(message), o)
    @JvmOverloads fun e(@StringRes message: Int, vararg o: Any = emptyArray()) = handleMsg(ERROR,    null, str(message), o)
    @JvmOverloads fun a(@StringRes message: Int, vararg o: Any = emptyArray()) = handleMsg(ASSERT,   null, str(message), o)
    @JvmOverloads fun ex (t: Throwable, message: String, vararg o: Any = emptyArray(), tag: String? = null) = handleExc(EX_FULL, t, tag, message, o)
    @JvmOverloads fun exs(t: Throwable, message: String, vararg o: Any = emptyArray(), tag: String? = null) = handleExc(EX_SIMPLER, t, tag, message, o)
    @JvmOverloads fun exm(t: Throwable, message: String, vararg o: Any = emptyArray(), tag: String? = null) = handleExc(EX_SIMPLEST, t, tag, message, o)
    @JvmOverloads fun ex (t: Throwable, @StringRes message: Int, vararg o: Any = emptyArray(), @StringRes tag: Int = 0) = handleExc(EX_FULL, t, str(tag), str(message), o)
    @JvmOverloads fun exs(t: Throwable, @StringRes message: Int, vararg o: Any = emptyArray(), @StringRes tag: Int = 0) = handleExc(EX_SIMPLER, t, str(tag), str(message), o)
    @JvmOverloads fun exm(t: Throwable, @StringRes message: Int, vararg o: Any = emptyArray(), @StringRes tag: Int = 0) = handleExc(EX_SIMPLEST, t, str(tag), str(message), o)
}

interface LogListener {
    /**
     * @return Return true, if you want to handle displaying logs
     */
    fun handleMsg(lvl: Int, tag: String?, msg: String?): Boolean

    /**
     * @return Return true, if you want to handle displaying logs
     */
    fun handleExc(simple_lvl: Int, t: Throwable, tag: String?, msg: String?, vararg o: Any): Boolean
}


internal class LoggerInternal {
    companion object {
        fun str(@StringRes id: Int) = if (id != 0) resources.getString(id) else null
        fun Throwable.printCause(tag: String?) {
            android.util.Log.w(tag, "Caused by: <${this.javaClass.simpleName}> ${this.message.toString()}")
        }

        fun printLocalCauseByStack(tag: String?, depth: Int, it: Throwable?): Unit = when {
            depth == 0 || it == null -> Unit
            else -> {
                it.printCause(tag)
                printLocalCauseByStack(tag, depth -1, it.cause)
            }
        }

        fun printLocalExceptionStack(tag: String?, pack: String, it: Throwable) {
            it.cause?.printCause(tag)
            it.stackTrace
                    ?.filter { !it.className.startsWith(pack) }
                    ?.forEach { android.util.Log.w(tag, it.toString()) }
        }
    }

}