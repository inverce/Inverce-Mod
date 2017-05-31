package com.inverce.mod.navigation;

import android.support.annotation.CheckResult;

import com.inverce.mod.core.IM;

import java.util.concurrent.TimeUnit;

public class ActionCreateFinalize extends ActionCreate {
    ActionCreateFinalize(ActionStack previous) {
        super(previous);
    }

    public void after(long time, TimeUnit unit) {
        IM.onBg().schedule(this::commit, time, unit);
    }

    public void commit() {
        Navigator.submitInvertedStack(this);
    }

    @CheckResult
    public ActionCreate and() {
        return new ActionCreate(previous);
    }
}
