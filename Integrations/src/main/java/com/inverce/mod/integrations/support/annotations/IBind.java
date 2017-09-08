package com.inverce.mod.integrations.support.annotations;

import android.support.v7.widget.RecyclerView;

public interface IBind<I, VH extends RecyclerView.ViewHolder> {
    void onBindViewHolder(VH holder, I item, int position);
}
