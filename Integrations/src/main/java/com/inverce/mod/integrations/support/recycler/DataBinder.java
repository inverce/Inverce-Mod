package com.inverce.mod.integrations.support.recycler;

import android.content.res.Resources;
import android.os.Build;
import android.support.annotation.IdRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Ui;
import com.inverce.mod.core.functional.IFunction;
import com.inverce.mod.integrations.support.annotations.IBind;
import com.inverce.mod.integrations.support.annotations.MapValue;
import com.inverce.mod.integrations.support.annotations.MapValue.ToDrawable;
import com.inverce.mod.integrations.support.annotations.MapValue.ToDrawableRes;
import com.inverce.mod.integrations.support.annotations.MapValue.ToStringRes;

import java.util.ArrayList;
import java.util.List;

/**
 * Created on 12/10/2017.
 */
public class DataBinder<T> implements IBind<T, BindViewHolder> {
    ToView<String, ImageView> loadImage;

    List<IBind<T, BindViewHolder>> tasks;

    public Resources res() {
        return IM.resources();
    }

    public DataBinder() {
        loadImage = (u, i) -> {
            throw new IllegalStateException("Image processor not specified");
        };
        tasks = new ArrayList<>();
    }

    public <V extends android.view.View> DataBinder<T> bind(ToHolder<T> bind) {
        tasks.add((holder, item, position) -> bind.bind(item, holder));
        return this;
    }

    public <V extends android.view.View> DataBinder<T> bind(IFunction<BindViewHolder, V> view, ToView<T, V> bind) {
        tasks.add((holder, item, position) -> {
            V v = view.apply(holder);
            if (v != null) bind.bind(item, v);
        });
        return this;
    }

    public <V extends android.view.View> DataBinder<T> bind(@IdRes int res, ToView<T, V> bind) {
        return bind(h -> h.get(res), bind);
    }

    public DataBinder<T> bindText(@IdRes int id, MapValue<T, String> map) {
        return bind(p -> (TextView) p.get(id), (item, view) -> view.setText(map.get(item)));
    }

    public DataBinder<T> bindTextRes(@IdRes int id, ToStringRes<T> map) {
        return bind(p -> (TextView) p.get(id), (item, view) -> view.setText(map.get(item)));
    }

    public DataBinder<T> bindImageRes(@IdRes int id, ToDrawableRes<T> map) {
        return bind(p -> (ImageView) p.get(id), (item, view) -> view.setImageResource(map.get(item)));
    }

    public DataBinder<T> bindImage(@IdRes int id, MapValue<T, String> map) {
        return bind(p -> (ImageView) p.get(id), (item, view) -> loadImage.bind(map.get(item), view));
    }

    public DataBinder<T> bindBackgroundRes(@IdRes int id, ToDrawableRes<T> map) {
        return bind(p -> p.get(id), (item, view) -> view.setBackgroundResource(map.get(item)));
    }

    public DataBinder<T> bindBackground(@IdRes int id, ToDrawable<T> map) {
        return bind(p -> p.get(id), (item, view) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                view.setBackground(map.get(item));
            } else {
                view.setBackgroundDrawable(map.get(item));
            }
        });
    }

    public DataBinder<T> bindVisibility(@IdRes int id, MapValue<T, Boolean> map) {
        return bind(p -> p.get(id), (item, view) -> Ui.visible(view, map.get(item)));
    }

    public DataBinder<T> bindOnClickListener(@IdRes int id, MapValue<T, View.OnClickListener> map) {
        return bind(p -> p.get(id), (item, view) -> view.setOnClickListener(map.get(item)));
    }

    @Override
    public synchronized void onBindViewHolder(BindViewHolder holder, T item, int position) {
        for (IBind<T, BindViewHolder> bind : tasks) {
            bind.onBindViewHolder(holder, item, position);
        }
    }

    public DataBinder setLoadImage(ToView<String, ImageView> loadImage) {
        this.loadImage = loadImage;
        return this;
    }
}
