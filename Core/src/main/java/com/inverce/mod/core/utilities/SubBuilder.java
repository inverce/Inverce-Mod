package com.inverce.mod.core.utilities;

import android.support.annotation.CheckResult;

public class SubBuilder<Parent> {
    protected Parent parent;

    public SubBuilder(Parent parent) {
        this.parent = parent;
    }

    @CheckResult
    public Parent done() {
        return parent;
    }
}
