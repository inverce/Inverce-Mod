package com.inverce.mod.navigation;

import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.v4.app.Fragment;

import com.inverce.mod.core.functional.ISupplierEx;

public class ActionCreate extends ActionStack {
    ActionCreate(ActionStack previous) {
        super(previous);
    }

    @CheckResult
    public ActionStackFinalize forwardTo(ISupplierEx<Fragment> supplierEx) {
        return new ActionStackFinalize(new Forward(this, supplierEx));
    }

    @CheckResult
    public ActionStackFinalize forwardTo(Fragment fragment) {
        return forwardTo(() -> fragment);
    }

    @CheckResult
    public ActionStackFinalize forwardTo(Class<? extends Fragment> fragmentClass) {
        return forwardTo(fragmentClass::newInstance);
    }

    @CheckResult
    public ActionStackFinalize forwardTo(Class<? extends Fragment> fragmentClass, Bundle arguments) {
        return forwardTo(() -> {
            Fragment fragment = fragmentClass.newInstance();
            fragment.setArguments(arguments);
            return fragment;
        });
    }

    @CheckResult
    public ActionStackFinalize replaceWith(ISupplierEx<Fragment> supplierEx) {
        return new ActionStackFinalize(new Replace(this, supplierEx));
    }

    @CheckResult
    public ActionStackFinalize replaceWith(Fragment fragment) {
        return replaceWith(() -> fragment);
    }

    @CheckResult
    public ActionStackFinalize replaceWith(Class<? extends Fragment> fragmentClass) {
        return replaceWith(fragmentClass::newInstance);
    }

    @CheckResult
    public ActionStackFinalize replaceWith(Class<? extends Fragment> fragmentClass, Bundle arguments) {
        return replaceWith(() -> {
            Fragment fragment = fragmentClass.newInstance();
            fragment.setArguments(arguments);
            return fragment;
        });
    }
}
