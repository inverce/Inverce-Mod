package com.inverce.utils.logging;

public interface LogListener {
    /**
     * @return Return true, if you want to handle displaying logs
     */
    boolean handleMsg(int lvl, String tag, String msg);

    /**
     * @return Return true, if you want to handle displaying logs
     */
    boolean handleExc(int simple_lvl, String tag, String msg, Throwable o);
}
