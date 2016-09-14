package com.inverce.logging;

import android.content.Context;
import android.support.annotation.StringRes;

@SuppressWarnings("unused")
public class Log {
    private static boolean DEBUG_MODE = true;
    private static final String BASE_TAG = "||";
    private static final String libraryPackage = "com.empik.lib";
    private static       String ApplicationPackage = "com.example.exampleapp";

    public static final int VERBOSE = 2, DEBUG = 3, INFO = 4, WARN = 5, ERROR = 6;
    public static final int EXCEPTION = 7, ASSERT = 8, NONE = Integer.MAX_VALUE;
    private static final int FULL = 1, SIMPLER = 2, SIMPLEST = 3;

    private static int LOGGING_LEVEL = VERBOSE;

    private static LogListener listener;
    private static Context ApplicationContext;

    public static void setListener(LogListener listener) {
        Log.listener = listener;
    }

    public static void setLogLevel(int LOGGING_LEVEL) {
        Log.LOGGING_LEVEL = LOGGING_LEVEL;
    }

    public static void setDebugMode(boolean debugMode) {
        DEBUG_MODE = debugMode;
    }

    public static void init(Context context) {
        ApplicationPackage = context.getPackageName();
        ApplicationContext = context.getApplicationContext();
    }

    private static Context context() {
        return ApplicationContext;
    }

    public static void handleMsg(int lvl, int tag, int msg, Object ... o) {
        if (!(DEBUG_MODE && LOGGING_LEVEL <= lvl)) {
            return;
        }

        if (tag == -1) {
            handleMsg(lvl, null, context().getString(msg, o));
        } else {
            handleMsg(lvl, context().getString(tag), context().getString(msg, o));
        }
    }

    public static void handleMsg(int lvl, String tag, String msg, Object ... o) {
        if (!(DEBUG_MODE && LOGGING_LEVEL <= lvl && msg != null)) {
            return;
        }

        tag = (tag != null) ? (BASE_TAG + "." + tag) : BASE_TAG;
        msg = (o.length > 0) ? String.format(msg, o) : msg;

        if (listener != null && lvl < EXCEPTION) {
            if (listener.handleMsg(lvl, tag, msg)) {
                return;
            }
        }
        switch (lvl) {
            case VERBOSE : android.util.Log.v(tag, msg); break;
            case DEBUG   : android.util.Log.d(tag, msg); break;
            case INFO    : android.util.Log.i(tag, msg); break;
            case WARN    : android.util.Log.w(tag, msg); break;
            case ERROR   : android.util.Log.e(tag, msg); break;
            case ASSERT  : handleExc(ASSERT, tag, msg, new AssertionError(msg)); break;
        }
    }

    public static void handleExc(int lvl, int tag, int msg, Throwable o) {
        if (!(DEBUG_MODE && LOGGING_LEVEL <= lvl)) {
            return;
        }

        if (tag == -1) {
            handleExc(lvl, null, context().getString(msg), o);
        } else {
            handleExc(lvl, context().getString(tag), context().getString(msg), o);
        }
    }

    public static void handleExc(int simple_lvl, String tag, String msg, Throwable o) {
        if (!(DEBUG_MODE && LOGGING_LEVEL <= EXCEPTION)) {
            return;
        }

        if (listener != null) {
            listener.handleExc(simple_lvl, tag, msg, o);
        }

        tag = (tag != null) ? (BASE_TAG + "." + tag) : BASE_TAG;
        msg = (msg != null ? msg + ": " : "" ) + "<" + o.getClass().getSimpleName() + "> "+ o.getMessage();

        android.util.Log.e(tag, msg);

        switch (simple_lvl) {
            case FULL: o.printStackTrace(); break;
            case SIMPLER:
            default:
                android.util.Log.w("", msg);
                StackTraceElement[] stackTrace = o.getStackTrace();
                for (StackTraceElement aStackTrace : stackTrace) {
                    String className = aStackTrace.getClassName();
                    if (className.contains(libraryPackage) || className.contains(ApplicationPackage)) {
                        android.util.Log.w(tag, aStackTrace.toString());
                    }
                }

                if (o.getCause() != null)
                    android.util.Log.w(tag, "Caused by: " + "<" + o.getCause().getClass().getSimpleName() + "> "+ o.getCause().getMessage());
                break;
        }
    }

    public static void v(String tag, String message, Object ... o) {                                handleMsg(VERBOSE,      tag,    message,  o); }
    public static void v(String tag, String message) {                                              handleMsg(VERBOSE,      tag,    message    ); }
    public static void v(String message) {                                                          handleMsg(VERBOSE,      null,   message    ); }
    public static void v(String message, Object ... o) {                                            handleMsg(VERBOSE,      null,   message,  o); }
    public static void d(String tag, String message, Object ... o) {                                handleMsg(DEBUG,        tag,    message,  o); }
    public static void d(String tag, String message) {                                              handleMsg(DEBUG,        tag,    message    ); }
    public static void d(String message) {                                                          handleMsg(DEBUG,        null,   message    ); }
    public static void d(String message, Object ... o) {                                            handleMsg(DEBUG,        null,   message,  o); }
    public static void i(String tag, String message, Object ... o) {                                handleMsg(INFO, tag, message, o); }
    public static void i(String tag, String message) {                                              handleMsg(INFO,         tag,    message    ); }
    public static void i(String message) {                                                          handleMsg(INFO,         null,   message    ); }
    public static void i(String message, Object ... o) {                                            handleMsg(INFO,         null,   message,  o); }
    public static void w(String tag, String message, Object ... o) {                                handleMsg(WARN,         tag,    message,  o); }
    public static void w(String tag, String message) {                                              handleMsg(WARN,         tag,    message    ); }
    public static void w(String message) {                                                          handleMsg(WARN,         null,   message    ); }
    public static void w(String message, Object ... o) {                                            handleMsg(WARN,         null,   message,  o); }
    public static void e(String tag, String message, Object ... o) {                                handleMsg(ERROR,        tag,    message,  o); }
    public static void e(String tag, String message) {                                              handleMsg(ERROR,        tag,    message    ); }
    public static void e(String message) {                                                          handleMsg(ERROR,        null,   message    ); }
    public static void e(String message, Object ... o) {                                            handleMsg(ERROR,        null,   message,  o); }
    public static void a(String tag, String message, Object ... o) {                                handleMsg(ASSERT,       tag,    message,  o); }
    public static void a(String tag, String message) {                                              handleMsg(ASSERT,       tag,    message    ); }
    public static void a(String message) {                                                          handleMsg(ASSERT,       null,   message    ); }
    public static void a(String message, Object ... o) {                                            handleMsg(ASSERT,       null,   message,  o); }

    public static void v(@StringRes int tag, @StringRes int message, Object ... o) {                handleMsg(VERBOSE,      tag,    message,  o); }
    public static void v(@StringRes int tag, @StringRes int message) {                              handleMsg(VERBOSE,      tag,    message    ); }
    public static void v(@StringRes int message) {                                                  handleMsg(VERBOSE,      -1,     message    ); }
    public static void v(@StringRes int message, Object ... o) {                                    handleMsg(VERBOSE,      -1,     message,  o); }
    public static void d(@StringRes int tag, @StringRes int message, Object ... o) {                handleMsg(DEBUG,        tag,    message,  o); }
    public static void d(@StringRes int tag, @StringRes int message) {                              handleMsg(DEBUG,        tag,    message    ); }
    public static void d(@StringRes int message) {                                                  handleMsg(DEBUG,        -1,     message    ); }
    public static void d(@StringRes int message, Object ... o) {                                    handleMsg(DEBUG,        -1,     message,  o); }
    public static void i(@StringRes int tag, @StringRes int message, Object ... o) {                handleMsg(INFO,         tag,    message,  o); }
    public static void i(@StringRes int tag, @StringRes int message) {                              handleMsg(INFO,         tag,    message    ); }
    public static void i(@StringRes int message) {                                                  handleMsg(INFO,         -1,     message    ); }
    public static void i(@StringRes int message, Object ... o) {                                    handleMsg(INFO,         -1,     message,  o); }
    public static void w(@StringRes int tag, @StringRes int message, Object ... o) {                handleMsg(WARN,         tag,    message,  o); }
    public static void w(@StringRes int tag, @StringRes int message) {                              handleMsg(WARN,         tag,    message    ); }
    public static void w(@StringRes int message) {                                                  handleMsg(WARN,         -1,     message    ); }
    public static void w(@StringRes int message, Object ... o) {                                    handleMsg(WARN,         -1,     message,  o); }
    public static void e(@StringRes int tag, @StringRes int message, Object ... o) {                handleMsg(ERROR,        tag,    message,  o); }
    public static void e(@StringRes int tag, @StringRes int message) {                              handleMsg(ERROR,        tag,    message    ); }
    public static void e(@StringRes int message) {                                                  handleMsg(ERROR,        -1,     message    ); }
    public static void e(@StringRes int message, Object ... o) {                                    handleMsg(ERROR,        -1,     message,  o); }
    public static void a(@StringRes int tag, @StringRes int message, Object ... o) {                handleMsg(ASSERT,       tag,    message,  o); }
    public static void a(@StringRes int tag, @StringRes int message) {                              handleMsg(ASSERT, tag, message); }
    public static void a(@StringRes int message) {                                                  handleMsg(ASSERT, -1, message); }
    public static void a(@StringRes int message, Object ... o) {                                    handleMsg(ASSERT, -1, message, o); }

    public static void ex(Throwable o) {                                                            handleExc(FULL,         null, null, o); }
    public static void exs(Throwable o) {                                                           handleExc(SIMPLER,      null, null, o); }
    public static void exm(Throwable o) {                                                           handleExc(SIMPLEST,     null, null, o); }

    public static void ex(String tag, String message, Throwable o) {                                handleExc(FULL,         tag,  message, o); }
    public static void ex(String message, Throwable o) {                                            handleExc(FULL,         null, message, o); }
    public static void exs(String tag, String message, Throwable o) {                               handleExc(SIMPLER,      tag,  message, o); }
    public static void exs(String message, Throwable o) {                                           handleExc(SIMPLER,      null, message, o); }
    public static void exm(String tag, String message, Throwable o) {                               handleExc(SIMPLEST,     tag,  message, o); }
    public static void exm(String message, Throwable o) {                                           handleExc(SIMPLEST,     null, message, o); }

    public static void ex(@StringRes int tag, @StringRes int message, Throwable o) {                handleExc(FULL,         tag,  message, o); }
    public static void ex(@StringRes int message, Throwable o) {                                    handleExc(FULL,         -1,   message, o); }
    public static void exs(@StringRes int tag, @StringRes int message, Throwable o) {               handleExc(SIMPLER,      tag,  message, o); }
    public static void exs(@StringRes int message, Throwable o) {                                   handleExc(SIMPLER,      -1,   message, o); }
    public static void exm(@StringRes int tag, @StringRes int message, Throwable o) {               handleExc(SIMPLEST,     tag,  message, o); }
    public static void exm(@StringRes int message, Throwable o) {                                   handleExc(SIMPLEST,     -1,   message, o); }

}