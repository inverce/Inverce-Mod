package com.inverce.mod.core.interfaces;

import android.app.Activity;
import android.os.Bundle;

import com.inverce.mod.events.annotation.Listener;

public interface ActivityStateListener extends Listener {
    void activityStateChanged(LifecycleState state, Activity activity, Bundle extra);
}
