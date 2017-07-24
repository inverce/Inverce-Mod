package com.inverce.mod.navigation;

import android.support.annotation.CheckResult;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;

import com.inverce.mod.core.functional.IFunction;

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

    @CheckResult
    public ActionCreateAll into(@IdRes int container) {
        findManager().container = container;
        return this;
    }

    @CheckResult
    public ActionCreateAll backStackNameProvider(IFunction<Fragment, String> supplier) {
        if (supplier == null) supplier = p -> null;
        findManager().nameSupplier = supplier;
        return this;
    }

    @CheckResult
    public ActionCreateAll tagProvider(IFunction<Fragment, String> supplier) {
        if (supplier == null) supplier = p -> null;
        findManager().tagSupplier = supplier;
        return this;
    }
}
