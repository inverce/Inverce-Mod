package com.inverce.mod.navigation;

import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.RestrictTo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.inverce.mod.core.IM;
import com.inverce.mod.core.Log;
import com.inverce.mod.core.functional.ISupplier;
import com.inverce.mod.core.verification.Preconditions;

import java.util.ArrayList;
import java.util.List;

/**
 * Simplifies navigation in Multiple Activity - Multiple Fragment Pages scenario
 *
 * THIS IS WORK IN PROGRESS VERSION - UNSTABLE
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public class Navigator {
    private Navigator() { }

    public static void test() {
        Navigator.on()
                .back().twice()
                .and()
                .replaceWith((Fragment) null)
                .and()
                .forwardTo((Fragment) null)
                .commit();
    }

    /**
     * Returns navigator stack builder for currently displaying activity
     * @return builder
     */
    @CheckResult
    public static ActionCreateAll on() {
        return on(() -> {
            FragmentActivity activity = IM.activitySupport();
            return activity != null ? activity.getSupportFragmentManager() : null;
        });
    }

    /**
     * Returns navigator stack builder for specified activity
     * @return builder
     */
    @CheckResult
    public static ActionCreateAll on(@NonNull FragmentActivity activity) {
        return on(activity::getSupportFragmentManager);
    }

    /**
     * Returns navigator stack builder for specified fragment manager
     * @return builder
     */
    @CheckResult
    public static ActionCreateAll on(@NonNull FragmentManager manager) {
        return on(() -> manager);
    }

    /**
     * Returns navigator stack builder for specifier fragment via support child fragment manager
     * @return builder
     */
    @CheckResult
    public static ActionCreateAll on(@NonNull Fragment fragment) {
        return on(fragment::getChildFragmentManager);
    }

    /**
     * Returns navigator stack builder with fragment manager supplier
     * @return builder
     */
    @CheckResult
    public static ActionCreateAll on(@NonNull ISupplier<FragmentManager> managerSupplier) {
        return new ActionCreateAll(new Manager(managerSupplier));
    }


    @RestrictTo(RestrictTo.Scope.LIBRARY_GROUP)
    public static void submitInvertedStack(@NonNull ActionStack stack) {
        List<ActionStack> list = new ArrayList<>(1);
        Preconditions.checkNotNull(stack, "Nope, null stack, is no good.");
        list.add(stack);
        while (stack.previous != null) {
            list.add(stack.previous);
            stack = stack.previous;
        }

        Preconditions.checkArgument(list.size() > 1, "Error. There are no actions in stack");
        Preconditions.checkArgument(list.get(0) instanceof Manager, "Error. Manager not found");
        Manager manager = ((Manager) list.get(0));
        list.remove(0);
        IM.onUi().execute(() -> submitStack(manager, list));
    }

    protected static void submitStack(Manager manager, List<ActionStack> stack) {
        FragmentManager fm = manager.managerSupplier.get();
        Preconditions.checkNotNull(fm, "Error. Fragment manager is not present");

        for (ActionStack action: stack) {
            if (action instanceof Back) {
                handleBack(manager, fm, (Back) action);
            } else if (action instanceof Reset) {
                handleReset(manager, fm, (Reset) action);
            } else if (action instanceof Replace) {
                handleReplace(manager, fm, (Replace) action);
            } else if (action instanceof Forward){
                handleForward(manager, fm, (Forward) action);
            } else {
                Log.w("Unknown action: " + action);
            }
        }
    }

    private static void handleForward(Manager manager, FragmentManager fm, Forward action) {
        fm
                .beginTransaction()
                .replace(manager.container, action.fragmentSupplier.get())
                .addToBackStack(null)
                .commit();
    }

    private static void handleReplace(Manager manager, FragmentManager fm, Replace action) {
        // remove last
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStackImmediate();
        }
        // move to state
        handleForward(manager, fm, action);
    }

    private static void handleReset(Manager manager, FragmentManager fm, Reset action) {
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            fm.popBackStack();
        }
        fm.executePendingTransactions();
    }

    private static void handleBack(Manager manager, FragmentManager fm, Back back) {
        for (int i = 0; i < fm.getBackStackEntryCount(); i++) {
            boolean clear = false;
            Fragment top = fm.findFragmentById(manager.container);
            if (back.count > 0) { back.count--; clear = true; }
            clear |= (top != null && back.toTag != null && !back.toTag.equals(top.getTag()));
            clear |= (top != null && back.toClass != null && !back.toClass.isInstance(top));

            if (clear) {
                fm.popBackStack();
            } else {
                break;
            }
        }
        fm.executePendingTransactions();
    }

    private static class Manager extends ActionStack {
        ISupplier<FragmentManager> managerSupplier;
        int container = 0;
        Manager(ISupplier<FragmentManager> managerSupplier) {
            super(null);
            this.managerSupplier = managerSupplier;
        }
    }
}
