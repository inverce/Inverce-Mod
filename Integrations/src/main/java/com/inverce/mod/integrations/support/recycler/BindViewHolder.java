package com.inverce.mod.integrations.support.recycler;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.inverce.mod.core.Ui;
import com.inverce.mod.core.functional.IConsumer;

import static android.view.View.NO_ID;

public class BindViewHolder extends RecyclerView.ViewHolder {
    protected SparseArray<View> children;
    protected boolean childrenInflated;

    public BindViewHolder(View itemView) {
        super(itemView);
        children = new SparseArray<>();
    }

    public SparseArray<View> inflateChildren() {
        if (childrenInflated) return children;
        childrenInflated = true;
        return children = searchForViews(itemView, children);
    }

    public SparseArray<View> getChildren() {
        if (childrenInflated) {
            return inflateChildren();
        }
        return children;
    }

    protected static SparseArray<View> searchForViews(View view, SparseArray<View> children) {
        if (view instanceof ViewGroup) {
            ViewGroup vg = (ViewGroup) view;
            for (int i = 0; i<vg.getChildCount(); i++) {
                searchForViews(vg.getChildAt(i), children);
            }
        }
        if (view.getId() != NO_ID) {
            children.put(view.getId(), view);
        }
        return  children;
    }

    @SuppressWarnings("unchecked")
    public <V extends View> V get(@IdRes int res) {
        V v = (V) children.get(res);
        if (v == null && !childrenInflated && children.indexOfKey(res) < 0) {
            children.put(res, v = (V) itemView.findViewById(res));
            return v;
        }
        return v;
    }

    public boolean has(@IdRes int res) {
        return get(res) != null;
    }

    public <V extends View> boolean ifHas(@IdRes int res, IConsumer<V> consumer) {
        V v = get(res);
        if (v  != null) {
            consumer.accept(v);
            return true;
        }
        return false;
    }

    public BindViewHolder bindText(@IdRes int id, String map) {
        TextView v = get(id);
        if (v  != null) v.setText(map);
        return this;
    }

    public BindViewHolder bindText(@IdRes int id, @StringRes int res) {
        TextView v = get(id);
        if (v  != null) v.setText(res);
        return this;
    }

    public BindViewHolder bindImage(@IdRes int id, @DrawableRes int res) {
        ImageView v = get(id);
        if (v  != null) v.setImageResource(res);
        return this;
    }

    public BindViewHolder bindBackground(@IdRes int id, @DrawableRes int res) {
        View v = get(id);
        if (v  != null) v.setBackgroundResource(res);
        return this;
    }

    public BindViewHolder bindBackground(@IdRes int id, Drawable res) {
        View v = get(id);
        if (v  != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                v.setBackground(res);
            } else {
                v.setBackgroundDrawable(res);
            }
        }
        return this;
    }

    public BindViewHolder bindVisibility(@IdRes int id, boolean visible) {
        View v = get(id);
        Ui.visible(v, visible);
        return this;
    }

    public BindViewHolder bindOnClickListener(@IdRes int id, View.OnClickListener onClickListener) {
        View v = get(id);
        if (v  != null) v.setOnClickListener(onClickListener);
        return this;
    }
}
