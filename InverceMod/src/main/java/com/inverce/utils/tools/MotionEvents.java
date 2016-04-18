package com.inverce.utils.tools;

import android.view.MotionEvent;
import android.view.View;

@SuppressWarnings("unused")
public class MotionEvents {

    public static View.OnTouchListener nestedScroll() {
        return new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();

                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }
                v.onTouchEvent(event);
                return false;
            }
        };
    }

    public static View.OnTouchListener multi(final View.OnTouchListener... listeners) {
        return new View.OnTouchListener() {
            View.OnTouchListener[] inner;
            {
                inner = listeners;
            }

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                for (View.OnTouchListener li : inner) {
                    if (li.onTouch(v, event)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
