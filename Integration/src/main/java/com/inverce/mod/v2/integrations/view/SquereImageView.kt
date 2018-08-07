package com.inverce.mod.v2.integrations.view

import android.content.Context
import android.support.v7.widget.AppCompatImageView
import android.util.AttributeSet
import com.inverce.mod.integrations.R

open class SquareImageView : AppCompatImageView {
    var majorDim = MajorDim.WIDTH
    enum class MajorDim constructor(val id: Int) {
        WIDTH(0), HEIGHT(1);
    }

    constructor(context: Context) : super(context)

    @JvmOverloads constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        if (attrs != null) {
            val a = context.obtainStyledAttributes(attrs, R.styleable.SquareImageView, defStyleAttr, 0)
            val dim = a.getInt(R.styleable.SquareImageView_majorDim, 0)
            majorDim = MajorDim.values().first { it.id == dim }
            a.recycle()
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        var dim = measuredWidth
        if (MajorDim.HEIGHT == majorDim) {
            dim = measuredHeight
        }
        setMeasuredDimension(dim, dim)
    }
}