package com.inverce.utils;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;

import com.inverce.logging.Log;
import com.inverce.utils.events.Event;
import com.inverce.utils.events.annotation.Listener;
import com.inverce.utils.stateless.EventsModule;
import com.inverce.utils.tools.Lifecycle;
import com.inverce.utils.tools.UtilityModule;

import static com.inverce.utils.tools.Util.nonNull;

/**
 * Class used as root of all the happiness, it the only thing you must initialize
 */
public final class IM {
    static Context context;
    static Application app;

    @SuppressWarnings("")
    public static Context context() {
        return context;
    }

    private static Context contextPlus() {
        return nonNull(Lifecycle.activityState(), context);
    }

    public static Application application() {
        return app;
    }

    public static Resources resources() {
        return contextPlus().getResources();
    }

    /**
     * Utility method to initialize all modules
     *
     * @param context Application context
     */
    public static void install(Application context) {
        IM.context = IM.app = context;
        EventsModule.init();
        UtilityModule.init(context);
        Log.i(R.string.im_mod_tag, R.string.im_welcome);

        Event.Bus
            .channel(33)
            .post(Listener.class).getClass();

        Event.Bus.post(Listener.class);
    }
}
