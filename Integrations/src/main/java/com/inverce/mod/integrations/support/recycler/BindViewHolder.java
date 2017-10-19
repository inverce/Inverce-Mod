package com.inverce.mod.integrations.support.recycler;

import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.inverce.mod.integrations.support.annotations.IBinder;

public class BindViewHolder<T> extends RecyclerView.ViewHolder implements IBinder<T> {
    DataBinder<T> binder;
    public BindViewHolder(View itemView) {
        this(itemView, new DataBinder<>());
    }

    public BindViewHolder(View itemView, DataBinder<T> binder) {
        super(itemView);
        this.binder = binder;
    }

    @Override
    public void onBindViewHolder(T item, int position) {
        binder.onBindViewHolder(this, item, position);
    }

    public DataBinder<T> getBinder() {
        return binder;
    }

    public BindViewHolder setBinder(DataBinder<T> binder) {
        this.binder = binder;
        return this;
    }

    public <V extends View> V get(@IdRes int res) {
        return null;
    }

    public interface BindView<T, V extends View> {
        void bind(T item, V view);
    }

    public interface MapValue<T, Y>  {
        Y get(T item);
    }

    public interface MapToDrawableRes<T>  {
        @DrawableRes
        int get(T item);
    }

    public interface MapToStringRes<T>  {
        @StringRes
        int get(T item);
    }

}
