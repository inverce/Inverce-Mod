package com.inverce.utils.logging;

import android.content.Context;

public class LoggerModule {
    private LoggerModule() { }

    public static void init(Context context) {
        Log.init(context);
    }
}
