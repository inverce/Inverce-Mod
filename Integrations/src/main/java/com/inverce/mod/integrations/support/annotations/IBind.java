package com.inverce.mod.integrations.support.annotations;

import android.support.v7.widget.RecyclerView;

import com.inverce.mod.integrations.support.recycler.BindViewHolder;

public interface IBind<I, VH extends RecyclerView.ViewHolder> {
    void onBindViewHolder(VH holder, I item, int position);

    interface ToView<T, V extends android.view.View> {
        void bind(T item, V view, int position);
    }

    interface ToHolder<T> {
        void bind(T item, BindViewHolder vh, int position);
    }
}
