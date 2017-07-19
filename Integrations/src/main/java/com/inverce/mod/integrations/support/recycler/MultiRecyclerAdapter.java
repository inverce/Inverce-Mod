package com.inverce.mod.integrations.support.recycler;

import android.support.annotation.LayoutRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inverce.mod.core.functional.IFunction;
import com.inverce.mod.core.functional.IPredicate;

import java.util.ArrayList;
import java.util.List;

public class MultiRecyclerAdapter<ITEM> extends RecyclerAdapter<ITEM, RecyclerView.ViewHolder>  {
    private List<MultiInfo<?, ?>> types;
    private SparseArray<MultiInfo<?, ?>> typesSparse;

    public MultiRecyclerAdapter() {
        types = new ArrayList<>();
        typesSparse = new SparseArray<>();
    }

    @Override
    public final void onBindViewHolder(RecyclerView.ViewHolder holder, ITEM item, int position) {
        int type = getItemViewType(position);
        MultiInfo<?, ?> info = typesSparse.get(type);
        info.onBindViewHolder(holder, item, position);
    }

    @Override
    public final RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        MultiInfo<?, ?> info = typesSparse.get(viewType);
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return info.createViewHolder.onCreateViewHolder(parent, inflater);
    }

    @Override
    public final int getItemViewType(int position) {
        ITEM item = getItem(position);
        for (MultiInfo<?, ?> type : types) {
            if (type.checkType.test(item)) {
                return type.registeredType;
            }
        }
        return -1;
    }

    public <I extends ITEM, VH extends RecyclerView.ViewHolder> int register(IPredicate<ITEM> checkType, IBind<I, VH> binder, ICreateVH<VH> createViewHolder) {
        MultiInfo<I, VH> info = new MultiInfo<>(checkType, binder, createViewHolder);
        types.add(info);
        info.registeredType = types.size();
        typesSparse.put(info.registeredType, info);
        return info.registeredType;
    }

    public <I extends ITEM, VH extends RecyclerView.ViewHolder> int register(IPredicate<ITEM> checkType, IBind<I, VH> binder, IFunction<View, VH> createViewHolder, @LayoutRes int layout) {
        return register(checkType, binder, (parent, inflater) -> createViewHolder.apply(inflater.inflate(layout, parent, false)));
    }

    public <I extends ITEM, VH extends RecyclerView.ViewHolder & IBinder<I>> int register(IPredicate<ITEM> checkType, ICreateVH<VH> createViewHolder) {
        return register(checkType, VH::onBindViewHolder, createViewHolder);
    }

    public <I extends ITEM, VH extends RecyclerView.ViewHolder & IBinder<I>> int register(IPredicate<ITEM> checkType, IFunction<View, VH> createViewHolder, @LayoutRes int layout) {
        return register(checkType, VH::onBindViewHolder, (parent, inflater) -> createViewHolder.apply(inflater.inflate(layout, parent, false)));
    }

    private class MultiInfo<I extends ITEM, VH extends RecyclerView.ViewHolder> {
        int registeredType;
        IPredicate<ITEM> checkType;
        IBind<I, VH> binder;
        ICreateVH<VH> createViewHolder;

        public MultiInfo(IPredicate<ITEM> checkType, IBind<I, VH> binder, ICreateVH<VH> createViewHolder) {
            this.checkType = checkType;
            this.binder = binder;
            this.createViewHolder = createViewHolder;
        }

        public void onBindViewHolder(RecyclerView.ViewHolder holder, ITEM item, int position) {
            //noinspection unchecked
            binder.onBindViewHolder((VH)holder, (I)item, position);
        }
    }

    public interface IBind<I, VH extends RecyclerView.ViewHolder> {
        void onBindViewHolder(VH holder, I item, int position);
    }

    public interface ICreateVH<VH extends RecyclerView.ViewHolder> {
        VH onCreateViewHolder(ViewGroup parent, LayoutInflater inflater);
    }

    public interface IBinder<I> {
        void onBindViewHolder(I item, int position);
    }
}
