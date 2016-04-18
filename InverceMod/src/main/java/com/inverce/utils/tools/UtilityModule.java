package com.inverce.utils.tools;

import android.content.Context;

public class UtilityModule {
    public static void init(Context context) {
        States.init(context);
        Screen.init(context);
        Ui.init(context);
    }
}
