package com.inverce.mod.navigation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.inverce.mod.core.functional.IFunction;
import com.inverce.mod.core.functional.ISupplier;

class Manager extends ActionStack {
    ISupplier<FragmentManager> managerSupplier;
    IFunction<Fragment, String> tagSupplier = p -> null;
    IFunction<Fragment, String> nameSupplier = p -> null;

    int container = Navigator.defaultContainer;

    Manager(ISupplier<FragmentManager> managerSupplier) {
        super(null);
        this.managerSupplier = managerSupplier;
    }
}
