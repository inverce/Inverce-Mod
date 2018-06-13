package com.inverce.mod.core;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;

import com.inverce.mod.core.internal.IMInternal;

/**
 * Provides easy access to context and executors.
 * Some extra goodies are also included
 */
@SuppressWarnings("WeakerAccess")
@SuppressLint("StaticFieldLeak")
public class IM {
    @NonNull
    private static IMInternal internal = IMInternal.get();

    /**
     * Provides current context, this will be either activity or application based on whats available.
     *
     * @return the context
     */
    @NonNull
    public static Context context() {
        if (internal.getActivity() != null) {
            return internal.getActivity();
        }
        return internal.getContext();
    }

    /**
     * Enables usage of IM utilities in debug mode for Specified view,
     * does nothing in running application
     *
     * @param view the view
     */
    public static void enableInEditModeForView(@NonNull View view) {
        if (view.isInEditMode()) {
            IMInternal.get().setInEdit(true);
            IMInitializer.initialize(view.getContext());
        }
    }
}
