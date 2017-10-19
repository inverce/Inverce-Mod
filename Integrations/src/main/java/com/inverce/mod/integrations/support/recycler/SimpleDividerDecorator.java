package com.inverce.mod.integrations.support.recycler;

import android.support.annotation.DrawableRes;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.DividerItemDecoration;

import com.inverce.mod.core.IM;

public class SimpleDividerDecorator extends DividerItemDecoration {
    public SimpleDividerDecorator(@DrawableRes int resource) {
        this(resource, DividerItemDecoration.VERTICAL);
    }

    public SimpleDividerDecorator(@DrawableRes int resource, int orientation) {
        super(IM.context(), orientation);
        setDrawable(ActivityCompat.getDrawable(IM.context(), resource));
    }
}