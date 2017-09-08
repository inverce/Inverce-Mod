package com.inverce.mod.integrations.support.annotations;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public interface ICreateVH<VH extends RecyclerView.ViewHolder> {
    VH onCreateViewHolder(ViewGroup parent, LayoutInflater inflater);
}
