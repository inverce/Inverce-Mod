package com.inverce.mod.navigation.requests;

import android.app.Activity;

public class ActivityNavigationRequest extends NavigationRequest<ActivityNavigationRequest> {
    protected Class<? extends Activity> target;

    public ActivityNavigationRequest(Class<? extends Activity> clazz) {
        target = clazz;
    }

    public Class<? extends Activity> getTarget() {
        return target;
    }
}
