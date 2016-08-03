package com.inverce.utils.tools;

import android.app.Application;
import android.content.Context;

public class UtilityModule {
    public static void init(Context context) {
        Lifecycle.init((Application) context);
        Ui.init(context);
    }
}
