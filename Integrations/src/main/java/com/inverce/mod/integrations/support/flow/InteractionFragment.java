package com.inverce.mod.integrations.support.flow;

import android.content.Context;
import android.support.annotation.Nullable;

public class InteractionFragment<ACTIONS> extends BaseFragment {
    @Nullable
    protected ACTIONS actions;

    @Nullable
    public ACTIONS getActions() {
        return actions;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            //noinspection unchecked
            actions = (ACTIONS) context;
        } catch (Exception ex) {
            throw new IllegalStateException("Parent activity must implement proper interface", ex);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        actions = null;
    }
}
