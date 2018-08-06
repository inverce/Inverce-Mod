package com.inverce.mod.v2.core

import android.support.annotation.StringRes
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


    @JvmOverloads @JvmStatic fun v(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = logger.v(message, *o, tag = tag)
    @JvmOverloads @JvmStatic fun d(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = logger.d(message, *o, tag = tag)
    @JvmOverloads @JvmStatic fun i(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = logger.i(message, *o, tag = tag)
    @JvmOverloads @JvmStatic fun w(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = logger.w(message, *o, tag = tag)
    @JvmOverloads @JvmStatic fun e(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = logger.e(message, *o, tag = tag)
    @JvmOverloads @JvmStatic fun a(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = logger.a(message, *o, tag = tag)

    @JvmOverloads @JvmStatic fun v(@StringRes message: Int, vararg o: Any = emptyArray())  = logger.v(message, *o)
    @JvmOverloads @JvmStatic fun d(@StringRes message: Int, vararg o: Any = emptyArray())  = logger.d(message, *o)
    @JvmOverloads @JvmStatic fun i(@StringRes message: Int, vararg o: Any = emptyArray())  = logger.i(message, *o)
    @JvmOverloads @JvmStatic fun w(@StringRes message: Int, vararg o: Any = emptyArray())  = logger.w(message, *o)
    @JvmOverloads @JvmStatic fun e(@StringRes message: Int, vararg o: Any = emptyArray())  = logger.e(message, *o)
    @JvmOverloads @JvmStatic fun a(@StringRes message: Int, vararg o: Any = emptyArray())  = logger.a(message, *o)

    @JvmOverloads @JvmStatic fun ex (t: Throwable, message: String? = null, vararg o: Any = emptyArray(), tag: String? = null) = logger.ex (t, message?:"", *o, tag = tag)
    @JvmOverloads @JvmStatic fun exs(t: Throwable, message: String? = null, vararg o: Any = emptyArray(), tag: String? = null) = logger.exs(t, message ?: "", *o, tag = tag)
    @JvmOverloads @JvmStatic fun exm(t: Throwable, message: String? = null, vararg o: Any = emptyArray(), tag: String? = null) = logger.exm(t, message ?: "", *o, tag = tag)
    @JvmOverloads @JvmStatic fun ex (t: Throwable, @StringRes message: Int, vararg o: Any = emptyArray(), @StringRes tag: Int = 0) = logger.ex (t, message, *o, tag = tag)
    @JvmOverloads @JvmStatic fun exs(t: Throwable, @StringRes message: Int, vararg o: Any = emptyArray(), @StringRes tag: Int = 0) = logger.exs(t, message, *o, tag = tag)
    @JvmOverloads @JvmStatic fun exm(t: Throwable, @StringRes message: Int, vararg o: Any = emptyArray(), @StringRes tag: Int = 0) = logger.exm(t, message, *o, tag = tag)
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

    protected fun str(@StringRes id: Int) = if (id != 0) resources.getString(id) else null

    protected fun dispatchMessage(lvl: Int, tag: String, msg: String) {
        when (lvl) {
            VERBOSE -> android.util.Log.v(tag, msg)
            DEBUG -> android.util.Log.d(tag, msg)
            INFO -> android.util.Log.i(tag, msg)
            WARN -> android.util.Log.w(tag, msg)
            ERROR -> android.util.Log.e(tag, msg)
            EXCEPTION, Log.ASSERT -> handleExc(Log.ASSERT, AssertionError(msg), tag, msg)
        }
    }

    fun shouldLog(lvl: Int) = (pVal { enable } ?: false) && (pVal { logLevel } ?: VERBOSE) <= lvl

    fun handleMsg(lvl: Int, tag: String?, msg: String?, vararg o: Any) {
        if (!shouldLog(lvl) || msg == null) return
        if (pVal { listener }?.handleMsg(lvl, tag, msg) == true) return
        dispatchMessage(lvl, "$baseTag${tag ?: ""}", String.format(Locale.getDefault(), msg, *o))
    }

    fun handleExc(simple_lvl: Int, t: Throwable, tag: String?, msg: String?, vararg o: Any) {
        if (pVal { listener }?.handleExc(simple_lvl, t, tag, msg, *o) == true) return
        android.util.Log.e("$baseTag${tag ?: ""}", String.format(Locale.getDefault(), msg ?: "$t", *o))

        val pack = context.packageName

        when (simple_lvl) {
            EX_FULL -> t.printStackTrace()
            EX_SIMPLER -> {
                android.util.Log.w("", msg)
                t.stackTrace
                    ?.filter { !it.className.startsWith(pack) }
                    ?.forEach { android.util.Log.w(tag, it.toString()) }

                t.cause?.let {
                    android.util.Log.w(tag, "Caused by: " + "<" + it.cause?.javaClass?.simpleName.toString() + "> " + it.cause?.message.toString())
                }
            }
            EX_SIMPLEST -> {
                android.util.Log.w(tag, "Caused by: " + "<" + t.javaClass.simpleName.toString() + "> " + t.message.toString())
                t.cause?.let {
                    android.util.Log.w(tag, "Caused by: " + "<" + it.cause?.javaClass?.simpleName.toString() + "> " + it.cause?.message.toString())
                }
            }
        }
    }

    @JvmOverloads fun v(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = handleMsg(VERBOSE, tag, message, *o)
    @JvmOverloads fun d(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = handleMsg(DEBUG, tag, message, *o)
    @JvmOverloads fun i(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = handleMsg(INFO, tag, message, *o)
    @JvmOverloads fun w(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = handleMsg(WARN, tag, message, *o)
    @JvmOverloads fun e(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = handleMsg(ERROR, tag, message, *o)
    @JvmOverloads fun a(message: String?, vararg o: Any = emptyArray(), tag: String? = null)  = handleMsg(ASSERT, tag, message, *o)
    @JvmOverloads fun v(@StringRes message: Int, vararg o: Any = emptyArray()) = handleMsg(VERBOSE,  null, str(message), *o)
    @JvmOverloads fun d(@StringRes message: Int, vararg o: Any = emptyArray()) = handleMsg(DEBUG,    null, str(message), *o)
    @JvmOverloads fun i(@StringRes message: Int, vararg o: Any = emptyArray()) = handleMsg(INFO,     null, str(message), *o)
    @JvmOverloads fun w(@StringRes message: Int, vararg o: Any = emptyArray()) = handleMsg(WARN,     null, str(message), *o)
    @JvmOverloads fun e(@StringRes message: Int, vararg o: Any = emptyArray()) = handleMsg(ERROR,    null, str(message), *o)
    @JvmOverloads fun a(@StringRes message: Int, vararg o: Any = emptyArray()) = handleMsg(ASSERT,   null, str(message), *o)
    @JvmOverloads fun ex (t: Throwable, message: String, vararg o: Any = emptyArray(), tag: String? = null) = handleExc(EX_FULL, t, tag, message, *o)
    @JvmOverloads fun exs(t: Throwable, message: String, vararg o: Any = emptyArray(), tag: String? = null) = handleExc(EX_SIMPLER, t, tag, message, *o)
    @JvmOverloads fun exm(t: Throwable, message: String, vararg o: Any = emptyArray(), tag: String? = null) = handleExc(EX_SIMPLEST, t, tag, message, *o)
    @JvmOverloads fun ex (t: Throwable, @StringRes message: Int, vararg o: Any = emptyArray(), @StringRes tag: Int = 0) = handleExc(EX_FULL, t, str(tag), str(message), *o)
    @JvmOverloads fun exs(t: Throwable, @StringRes message: Int, vararg o: Any = emptyArray(), @StringRes tag: Int = 0) = handleExc(EX_SIMPLER, t, str(tag), str(message), *o)
    @JvmOverloads fun exm(t: Throwable, @StringRes message: Int, vararg o: Any = emptyArray(), @StringRes tag: Int = 0) = handleExc(EX_SIMPLEST, t, str(tag), str(message), *o)
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
