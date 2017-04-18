package com.example.test;

import android.content.Context;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.AppCompatButton;
import android.util.AttributeSet;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Screen;

public class TestView extends AppCompatButton {
    public TestView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        IM.enableInEditModeForView(this);

        int someDp = Screen.dpToPx(30);
        int someColor = ActivityCompat.getColor(IM.context(), R.color.colorPrimary);

        this.setBackgroundColor(someColor);
    }
}
