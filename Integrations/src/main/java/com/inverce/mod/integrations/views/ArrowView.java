package com.inverce.mod.integrations.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.ColorInt;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.inverce.mod.integrations.R;

public class ArrowView extends AppCompatTextView {
    @ColorInt
    int arrowColor;
    boolean arrowOnLeft = false;
    Paint arrowPaint;
    float arrowHeadAspect;
    Path path;

    public ArrowView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public ArrowView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public ArrowView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        if(attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArrowView, defStyleAttr, 0);
            arrowColor = a.getColor(R.styleable.ArrowView_arrowColor, 0xFFF0F0F0);
            arrowOnLeft = a.getInt(R.styleable.ArrowView_direction, 0) == 0;
            arrowHeadAspect = a.getFloat(R.styleable.ArrowView_arrowHeadAspect, .3f);
            a.recycle();
        } else {
            arrowColor = 0xFFF0F0F0;
            arrowOnLeft = true;
            arrowHeadAspect = .3f;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (arrowPaint == null || arrowPaint.getColor() != arrowColor) {
            arrowPaint = new Paint();
            arrowPaint.setColor(arrowColor);
            arrowPaint.setStyle(Paint.Style.FILL);
            arrowPaint.setAntiAlias(true);
        }

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int arrowWidth = (int) (height * arrowHeadAspect);

        if (path == null) {
            path = new Path();

            if (arrowOnLeft) {
                path.moveTo(0, height / 2);
                path.lineTo(arrowWidth, 0);
                path.lineTo(width, 0);
                path.lineTo(width, height);
                path.lineTo(arrowWidth, height);
                path.lineTo(0, height / 2);
            } else {
                path.moveTo(0, 0);
                path.lineTo(width - arrowWidth, 0);
                path.lineTo(width, height / 2);
                path.lineTo(width - arrowWidth, height);
                path.lineTo(0, height);
                path.lineTo(0, 0);
            }
        }
        canvas.drawPath(path, arrowPaint);

        super.onDraw(canvas);
    }
}
