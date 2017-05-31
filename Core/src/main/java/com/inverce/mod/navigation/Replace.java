package com.inverce.mod.navigation;

import android.support.v4.app.Fragment;

import com.inverce.mod.core.functional.ISupplierEx;

public class Replace extends Forward {
    Replace(ActionStack previous, ISupplierEx<Fragment> fragmentSupplier) {
        super(previous, fragmentSupplier);
    }
}
