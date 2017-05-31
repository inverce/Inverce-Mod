package com.inverce.mod.navigation;

import android.support.v4.app.Fragment;

import com.inverce.mod.core.functional.ISupplier;
import com.inverce.mod.core.functional.ISupplierEx;

public class Forward extends ActionStackFinalize {
    ISupplier<Fragment> fragmentSupplier;

    Forward(ActionStack previous, ISupplierEx<Fragment> fragmentSupplier) {
        super(previous);
        this.fragmentSupplier = () -> {
            try {
                return fragmentSupplier.get();
            } catch (Exception e) {
                return null;
            }
        };
    }
}
