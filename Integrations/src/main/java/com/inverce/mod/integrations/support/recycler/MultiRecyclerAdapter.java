package com.inverce.mod.integrations.support.recycler;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.inverce.mod.core.functional.IFunction;
import com.inverce.mod.core.functional.IPredicate;
import com.inverce.mod.integrations.support.annotations.IBind;
import com.inverce.mod.integrations.support.annotations.IBinder;
import com.inverce.mod.integrations.support.annotations.ICreateVH;
import com.inverce.mod.v2.integrations.recycler.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MultiRecyclerAdapter<ITEM> extends RecyclerAdapter<ITEM, RecyclerView.ViewHolder> {
    protected List<MultiInfo<?, ?>> types;
    protected SparseArray<MultiInfo<?, ?>> typesSparse;

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
    public final RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

    public <I extends ITEM, VH extends RecyclerView.ViewHolder> int register(IPredicate<ITEM> checkType, IBind<I, VH> binder, @NonNull IFunction<View, VH> createViewHolder, @LayoutRes int layout) {
        return register(checkType, binder, new ICreateVH<VH>() {
            @Override
            public VH onCreateViewHolder(ViewGroup parent, @NonNull LayoutInflater inflater) {
                return createViewHolder.apply(inflater.inflate(layout, parent, false));
            }
        });
    }

    public <I extends ITEM, VH extends RecyclerView.ViewHolder & IBinder<I>> int register(IPredicate<ITEM> checkType, ICreateVH<VH> createViewHolder) {
        return register(checkType, new IBind<I, VH>() {
            @Override
            public void onBindViewHolder(@NonNull VH vh, I item, int position) {
                vh.onBindViewHolder(item, position);
            }
        }, createViewHolder);
    }

    public <I extends ITEM, VH extends RecyclerView.ViewHolder & IBinder<I>> int register(IPredicate<ITEM> checkType, @NonNull IFunction<View, VH> createViewHolder, @LayoutRes int layout) {
        return register(checkType, new IBind<I, VH>() {
            @Override
            public void onBindViewHolder(@NonNull VH vh, I item, int position) {
                vh.onBindViewHolder(item, position);
            }
        }, new ICreateVH<VH>() {
            @Override
            public VH onCreateViewHolder(ViewGroup parent, @NonNull LayoutInflater inflater) {
                return createViewHolder.apply(inflater.inflate(layout, parent, false));
            }
        });
    }

    public <I extends ITEM> int register(IPredicate<ITEM> checkType, DataBinder<I> binder, @LayoutRes int layout) {
        return register(checkType, binder, BindViewHolder::new, layout);
    }

    protected class MultiInfo<I extends ITEM, VH extends RecyclerView.ViewHolder> {
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

}
