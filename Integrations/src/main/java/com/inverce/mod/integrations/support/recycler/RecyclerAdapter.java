package com.inverce.mod.integrations.support.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Ui;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerAdapter<ITEM, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected List<? extends ITEM> data = new ArrayList<>();

    public ITEM getItem(int position) {
        return data.get(position);
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        onBindViewHolder(holder, getItem(position), position);
    }

    public abstract void onBindViewHolder(VH holder, ITEM item, int position);

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<? extends ITEM> elements) {
        setData(elements, false);
    }

    public void setData(List<? extends ITEM> elements, boolean useDiffUtil) {
        if (!Ui.isUiThread()) {
            List<? extends ITEM> finalElements = elements;
            IM.onUi().execute(() -> setData(finalElements, useDiffUtil));
            return;
        }

        if (elements == null) {
            elements = new ArrayList<>();
        }
        if (useDiffUtil) {
            List<? extends ITEM> oldItems = new ArrayList<>(data);
            DiffUtil.calculateDiff(new EasyDiffUtilCallBack<>(elements, oldItems))
                    .dispatchUpdatesTo(this);
        } else {
            data = elements;
            notifyDataSetChanged();
        }
    }

    protected View inflate(ViewGroup parent, @LayoutRes int res) {
        return LayoutInflater.from(parent.getContext())
                .inflate(res, parent, false);
    }
}
