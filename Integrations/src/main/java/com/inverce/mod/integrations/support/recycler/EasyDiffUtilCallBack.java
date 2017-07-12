package com.inverce.mod.integrations.support.recycler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.ArrayList;
import java.util.List;

public class EasyDiffUtilCallBack<T> extends DiffUtil.Callback {
    @NonNull
    private List<? extends T> oldList;

    @NonNull
    private List<? extends T> newList;

    public EasyDiffUtilCallBack(List<? extends T> newList, List<? extends T> oldList) {
        this.newList = newList != null ? newList : new ArrayList<T>();
        this.oldList = oldList != null ? oldList : new ArrayList<T>();
    }

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}