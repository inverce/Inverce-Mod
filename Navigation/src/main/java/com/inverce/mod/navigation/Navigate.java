package com.inverce.mod.navigation;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.v4.app.Fragment;

import com.inverce.mod.navigation.requests.ActivityNavigationRequest;
import com.inverce.mod.navigation.requests.FragmentNavigationRequest;

public final class Navigate {
    private Navigate() {
    }

    /**
     * Navigate to specific fragment instance
     */
    @CheckResult
    public static FragmentNavigationRequest to(Fragment fragment) {
        return new FragmentNavigationRequest(fragment);
    }

    @CheckResult
    public static FragmentNavigationRequest to(Class<? extends Fragment> clazz) {
        return new FragmentNavigationRequest(clazz);
    }

    @CheckResult
    public static FragmentNavigationRequest to(Class<? extends Fragment> clazz, Bundle args) {
        return new FragmentNavigationRequest(clazz).setExtras(args);
    }

    @CheckResult
    public static ActivityNavigationRequest toActivity(Class<? extends Activity> clazz) {
        return new ActivityNavigationRequest(clazz);
    }

    @CheckResult
    public static ActivityNavigationRequest toActivity(Class<? extends Activity> clazz, Bundle args) {
        return new ActivityNavigationRequest(clazz).setExtras(args);
    }
}
