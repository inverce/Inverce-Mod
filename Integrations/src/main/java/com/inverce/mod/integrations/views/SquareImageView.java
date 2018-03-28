package com.inverce.mod.integrations.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.inverce.mod.integrations.R;

public class SquareImageView extends AppCompatImageView {
    protected enum MajorDim {
        WIDTH(0), HEIGHT(1);
        private int id;
        MajorDim(int id) {
            this.id = id;
        }
        @NonNull
        static MajorDim fromId(int id) {
            for (MajorDim majorDim : values())
                if (majorDim.id == id)
                    return majorDim;
            throw new IllegalArgumentException();
        }
    }

    private MajorDim majorDim = MajorDim.WIDTH;

    public SquareImageView(@NonNull Context context) {
        super(context);
    }

    public SquareImageView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SquareImageView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView, defStyleAttr, 0);
            majorDim = MajorDim.fromId(a.getInt(R.styleable.SquareImageView_majorDim, 0));
            a.recycle();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int dim = getMeasuredWidth();
        if (MajorDim.HEIGHT.equals(majorDim)) {
            dim = getMeasuredHeight();
        }
        setMeasuredDimension(dim, dim);
    }

    @NonNull
    public SquareImageView setMajorDim(MajorDim majorDim) {
        this.majorDim = majorDim;
        return this;
    }
}