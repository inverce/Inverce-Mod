package com.inverce.mod.navigation;

import android.support.annotation.CheckResult;

public class Back extends ActionStackFinalize {
    int count = 1;
    String toTag = null;
    Class<?> toClass = null;

    Back() {
        super(null);
    }

    @CheckResult
    public ActionStackFinalize twice() {
        return times(2);
    }

    @CheckResult
    public ActionStackFinalize times(int count) {
        this.count = count;
        return new ActionStackFinalize(this);
    }

    @CheckResult
    public ActionStackFinalize to(String tag) {
        this.toTag = tag;
        return new ActionStackFinalize(this);
    }

    @CheckResult
    public ActionStackFinalize to(Class<?> clazz) {
        this.toClass = clazz;
        return new ActionStackFinalize(this);
    }
}
