package com.inverce.mod.integrations.bugfixes.android;


import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransactionIndicesBugFix;

public class SupportFixes {
    public static void fixFragmentOrdering(FragmentManager fragmentManager) {
        FragmentTransactionIndicesBugFix.reorderIndices(fragmentManager);
    }

}
