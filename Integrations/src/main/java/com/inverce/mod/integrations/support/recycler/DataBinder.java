package com.inverce.mod.integrations.support.recycler;

import android.content.res.Resources;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.functional.IFunction;
import com.inverce.mod.integrations.support.annotations.IBind;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 12/10/2017.
 */
public class DataBinder<T> implements IBind<T, BindViewHolder<T>> {
    BindViewHolder.BindView<String, ImageView> loadImage;

    List<IBind<T, BindViewHolder<T>>> tasks;

    public Resources res() {
        return IM.resources();
    }

    public DataBinder() {
        loadImage = (u, i) -> {
            throw new IllegalStateException("Image processor not specified");
        };
        tasks = new ArrayList<>();
    }

    public <V extends View> DataBinder<T> bind(IFunction<BindViewHolder<T>, V> view, BindViewHolder.BindView<T, V> bind) {
        tasks.add((holder, item, position) -> bind.bind(item, view.apply(holder)));
        return this;
    }

    public <V extends View> DataBinder<T> bind(@IdRes int res, BindViewHolder.BindView<T, V> bind) {
        tasks.add((holder, item, position) -> bind.bind(item, holder.get(res)));
        return this;
    }

    public DataBinder<T> bindText(@IdRes int id, @StringRes int res) {
        return bindText(id, p -> res().getString(res));
    }

    public DataBinder<T> bindText(@IdRes int id, BindViewHolder.MapValue<T, String> map) {
        return bind(p -> (TextView) p.get(id), (item, view) -> view.setText(map.get(item)));
    }

    public DataBinder<T> bindImage(@IdRes int id, BindViewHolder.MapToDrawableRes<T> map) {
        return bind(p -> (ImageView) p.get(id), (item, view) -> view.setImageResource(map.get(item)));
    }

    public DataBinder<T> bindImage(@IdRes int id, BindViewHolder.MapValue<T, String> map) {
        return bind(p -> (ImageView) p.get(id), (item, view) -> loadImage.bind(map.get(item), view));
    }

    public DataBinder<T> bindVisibility(@IdRes int id, BindViewHolder.MapValue<T, String> map) {
        return bind(p -> (ImageView) p.get(id), (item, view) -> loadImage.bind(map.get(item), view));
    }

    @Override
    public synchronized void onBindViewHolder(BindViewHolder<T> holder, T item, int position) {
        for (IBind<T, BindViewHolder<T>> bind : tasks) {
            bind.onBindViewHolder(holder, item, position);
        }
    }
}
