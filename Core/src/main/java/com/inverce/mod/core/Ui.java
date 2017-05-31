package com.inverce.mod.core;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Looper;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.StateSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;

/**
 * The Ui utilities
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Ui {
    private static String PADDING_NPE = "To set padding you must provide VIEW";

    /**
     * Is on ui thread.
     *
     * @return the boolean
     */
    public static boolean isUiThread() {
        return Looper.myLooper() == Looper.getMainLooper();
    }

    /**
     * Make selector state for specified colors
     *
     * @param drawable    the drawable
     * @param pressedRes  the pressed res
     * @param disabledRes the disabled res
     * @return the state list drawable
     */
    public static StateListDrawable makeSelector(Drawable drawable, @ColorRes int pressedRes, @ColorRes int disabledRes) {
        StateListDrawable state = new StateListDrawable();
        LayerDrawable pressed = new LayerDrawable(new Drawable[]{
                drawable,
                new ColorDrawable(ActivityCompat.getColor(IM.context(), pressedRes))
        });
        LayerDrawable disabled = new LayerDrawable(new Drawable[]{
                drawable,
                new ColorDrawable(ActivityCompat.getColor(IM.context(), disabledRes))
        });
        state.addState(new int[]{android.R.attr.state_pressed}, pressed);
        state.addState(new int[]{android.R.attr.state_focused}, disabled);
        state.addState(StateSet.WILD_CARD, drawable);
        return state;
    }

    /**
     * Changes visibility of view in safe and simple manner.
     *
     * @param view    the view
     * @param visible the visibility
     * @return whatever view will be visible
     */
    public static boolean visible(View view, boolean visible) {
        return visible(view, visible, true);
    }

    /**
     * Changes visibility of view in safe and simple manner.
     *
     * @param view    the view
     * @param visible the visibility
     * @param gone    whatever use GONE or INVISIBLE when visible is false
     * @return whatever view will be visible
     */
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

    /**
     * Run on next layout.
     *
     * @param rootView the root view
     * @param run      the run
     */
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

    /**
     * Hide soft input.
     *
     * @param view the view
     */
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
