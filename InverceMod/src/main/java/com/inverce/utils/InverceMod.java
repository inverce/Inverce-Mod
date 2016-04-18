package com.inverce.utils;

import android.app.Application;

import com.inverce.utils.events.EventsModule;
import com.inverce.utils.logging.Log;
import com.inverce.utils.logging.LoggerModule;
import com.inverce.utils.tools.UtilityModule;

/**
 * Class used as root of all the happiness, it the only thing you must initialize
 */
public final class InverceMod {

    /**
     * Utility method to initialize all modules
     * @param context Application context
     */
    public static void install(Application context) {
        EventsModule.init();
        UtilityModule.init(context);
        LoggerModule.init(context);
        Log.i(R.string.im_mod_tag, R.string.im_w_notwrapped);

        Log.i(R.string.im_mod_tag, R.string.im_welcome);
    }
}
