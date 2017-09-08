package com.inverce.mod.integrations.support.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.lang.ref.WeakReference;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static com.inverce.mod.core.Ui.hideSoftInput;

public class HideSoftKeyboardOnScrollListener extends RecyclerView.OnScrollListener {
    WeakReference<View> rootView;
    boolean isScrolling;

    public HideSoftKeyboardOnScrollListener(View rootView) {
        this.rootView = new WeakReference<>(rootView);
    }

    @Override
    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
        super.onScrollStateChanged(recyclerView, newState);
        isScrolling = newState == SCROLL_STATE_DRAGGING;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (rootView.get() != null && isScrolling) {
            hideSoftInput(rootView.get().findFocus());
        }
    }
}