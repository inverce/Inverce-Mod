package com.inverce.mod.integrations.support.recycler;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;

public class SimpleDividerDecorator extends DividerItemDecoration {
    public SimpleDividerDecorator(Context context, @DrawableRes int resource) {
        this(context, resource, DividerItemDecoration.VERTICAL);
    }

    public SimpleDividerDecorator(Context context, @DrawableRes int resource, int orientation) {
        super(context, orientation);
        setDrawable(ActivityCompat.getDrawable(context, resource));
    }
}