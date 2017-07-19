package com.inverce.mod.navigation;

import android.support.annotation.CheckResult;

public class ActionCreateAll extends ActionCreate {
    ActionCreateAll(ActionStack previous) {
        super(previous);
    }

    @CheckResult
    public Back back() {
        return new Back();
    }

    @CheckResult
    public Reset reset() {
        return new Reset();
    }

}
