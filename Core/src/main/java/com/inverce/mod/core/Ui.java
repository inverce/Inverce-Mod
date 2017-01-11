package com.inverce.mod.core;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.StateSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

@SuppressWarnings({"unused", "WeakerAccess"})
public class Ui {
    private static String PADDING_NPE = "To set padding you must provide VIEW";

    public static boolean isUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    public static boolean isOnUiThread() {
        return Looper.getMainLooper().getThread() == Thread.currentThread();
    }

    public static StateListDrawable makeSelector(Drawable drawable, @ColorRes int pressedRes, @ColorRes int disabledRes) {
        Resources res = IM.context().getResources();
        StateListDrawable state = new StateListDrawable();
        LayerDrawable pressed = new LayerDrawable(new Drawable[]{
                drawable,
                new ColorDrawable(res.getColor(pressedRes))
        });
        LayerDrawable disabled = new LayerDrawable(new Drawable[]{
                drawable,
                new ColorDrawable(res.getColor(disabledRes))
        });
        state.addState(new int[]{android.R.attr.state_pressed}, pressed);
        state.addState(new int[]{android.R.attr.state_focused}, disabled);
        state.addState(StateSet.WILD_CARD, drawable);
        return state;
    }

    public static boolean visible(View view, boolean visible) {
        return visible(view, visible, true);
    }

    public static boolean visible(View view, boolean visible, boolean gone) {
        if (view != null) {
            if (visible) {
                view.setVisibility(View.VISIBLE);
                return true;
            } else {
                if (gone) {
                    view.setVisibility(View.GONE);
                } else {
                    view.setVisibility(View.INVISIBLE);
                }
                return false;
            }
        }
        return false;
    }

    public static LayoutInflater getInflater() {
        Activity activity = IM.activity();
        return LayoutInflater.from(activity != null? activity : IM.context());
    }

    public static void runOnNextLayout(final View rootView, final Runnable run) {
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    //noinspection deprecation
                    rootView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
                run.run();
            }
        });
    }

    public static void hideSoftInput(View view) {
        try {
            InputMethodManager imm = (InputMethodManager) IM.context().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        } catch (Exception ignored) { /* safely ignore, as ex in here means we could not hide keyboard */ }
    }

    @SuppressWarnings("ResourceType")
    public static class Layout {

        @Nullable
        View view;
        ViewGroup.LayoutParams params;

        public Layout(@NonNull View view) {
            this.view = view;
            this.params = view.getLayoutParams();
        }

        public Layout(@NonNull ViewGroup.LayoutParams params) {
            this.params = params;
        }

        public static Layout on(View view) {
            return new Layout(view);
        }

        public static Layout on(ViewGroup.LayoutParams params) {
            return new Layout(params);
        }

        private void checkView(String exMsg) throws NullPointerException {
            if (view == null) {
                throw new NullPointerException(exMsg);
            }
        }

        public Layout width(int newValue, boolean usePixels) {
            if (params != null)
                params.width = usePixels ? newValue : Screen.dpToPx(newValue);
            return this;
        }

        public Layout height(int newValue, boolean usePixels) {
            if (params != null)
                params.height = usePixels ? newValue : Screen.dpToPx(newValue);
            return this;
        }


        public Layout topPadding(int padding, boolean usePixels) {
            checkView(PADDING_NPE);
            assert view != null;
            view.setPadding(view.getPaddingLeft(), usePixels ? padding : Screen.dpToPx(padding), view.getPaddingRight(), view.getPaddingBottom());
            return this;
        }

        public Layout leftPadding(int padding, boolean usePixels) {
            checkView(PADDING_NPE);
            assert view != null;
            view.setPadding(usePixels ? padding : Screen.dpToPx(padding), view.getPaddingTop(), view.getPaddingRight(), view.getPaddingBottom());
            return this;
        }

        public Layout rightPadding(int padding, boolean usePixels) {
            checkView(PADDING_NPE);
            assert view != null;
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), usePixels ? padding : Screen.dpToPx(padding), view.getPaddingBottom());
            return this;
        }

        public Layout bottomPadding(int padding, boolean usePixels) {
            checkView(PADDING_NPE);
            assert view != null;
            view.setPadding(view.getPaddingLeft(), view.getPaddingTop(), view.getPaddingRight(), usePixels ? padding : Screen.dpToPx(padding));
            return this;
        }

        public void done() {
            if (view != null) {
                view.setLayoutParams(params);
            }
        }
    }

    @SuppressWarnings("ResourceType")
    public static class Margin extends Layout {
        ViewGroup.MarginLayoutParams params;

        Margin(View view) {
            super(view);
            this.params = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        }

        Margin(ViewGroup.MarginLayoutParams params) {
            super(params);
            this.params = params;
        }

        public static Margin on(View view) {
            return new Margin(view);
        }

        public static Margin on(ViewGroup.MarginLayoutParams params) {
            return new Margin(params);
        }

        public Margin top(int margin, boolean usePixels) {
            params.topMargin = usePixels ? margin : Screen.dpToPx(margin);
            return this;
        }

        public Margin left(int margin, boolean usePixels) {
            params.leftMargin = usePixels ? margin : Screen.dpToPx(margin);
            return this;
        }

        public Margin right(int margin, boolean usePixels) {
            params.rightMargin = usePixels ? margin : Screen.dpToPx(margin);
            return this;
        }

        public Margin bottom(int margin, boolean usePixels) {
            params.bottomMargin = usePixels ? margin : Screen.dpToPx(margin);
            return this;
        }

        public void done() {
            if (view != null) {
                view.setLayoutParams(params);
            }
        }
    }
}
