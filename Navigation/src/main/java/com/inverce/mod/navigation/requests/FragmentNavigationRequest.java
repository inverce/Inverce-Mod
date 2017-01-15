package com.inverce.mod.navigation.requests;

import android.support.annotation.AnimRes;
import android.support.annotation.CheckResult;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;

public class FragmentNavigationRequest extends NavigationRequest<FragmentNavigationRequest> {
    protected Class<? extends Fragment> target;
    protected transient Fragment hint; // we cant serialize it, so we will not try
    protected boolean hintSetFlag;

    @LayoutRes
    protected int containerId = 0;

    @AnimRes
    int enter, exit, popEnter, popExit;

    public FragmentNavigationRequest(Fragment fragment) {
        this(fragment.getClass());
        hint = fragment;
        hintSetFlag = true;
        if (fragment.getArguments() != null) {
            extras = fragment.getArguments();
        }
    }

    public FragmentNavigationRequest(Class<? extends Fragment> clazz) {
        this.target = clazz;
    }

    /**
     * Sets optional identifier of the container this fragment is
     * to be placed in.  If 0, it will not be placed in a container.
     */
    @CheckResult
    public FragmentNavigationRequest setContainer(@LayoutRes int containerId) {
        this.containerId = containerId;
        return this;
    }

    /**
     * Set specific animation resources to run for the fragments that are
     * entering and exiting in this transaction. The <code>popEnter</code>
     * and <code>popExit</code> animations will be played for enter/exit
     * operations specifically when popping the back stack.
     */
    @CheckResult
    public FragmentNavigationRequest setCustomAnimations(@AnimRes int enter, @AnimRes int exit, @AnimRes int popEnter, @AnimRes int popExit) {
        this.enter = enter;
        this.exit = exit;
        this.popEnter = popEnter;
        this.popExit = popExit;
        return this;
    }

    /**
     * Set specific animation resources to run for the fragments that are
     * entering and exiting in this transaction. These animations will not be
     * played when popping the back stack.
     */
    @CheckResult
    public FragmentNavigationRequest setCustomAnimations(@AnimRes int enter, @AnimRes int exit) {
        return setCustomAnimations(enter, exit, 0, 0);
    }

    public Class<? extends Fragment> getTarget() {
        return target;
    }

    public Fragment getHint() {
        return hint;
    }

    public boolean isHintFlagSet() {
        return hintSetFlag;
    }

    public int getContainerId() {
        return containerId;
    }

    @AnimRes
    public int[] getAnimationSet() {
        return new int[] { enter, exit, popEnter, popExit };
    }
}
