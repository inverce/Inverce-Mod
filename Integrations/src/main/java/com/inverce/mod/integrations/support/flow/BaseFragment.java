package com.inverce.mod.integrations.support.flow;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import com.inverce.mod.core.interfaces.LifecycleState;
import com.inverce.mod.core.utilities.SubBundleBuilder;

public class BaseFragment extends Fragment {

    @Nullable
    public SubBundleBuilder<BaseFragment> setArguments() {
        return new SubBundleBuilder<>(this, getArguments(), this::setArguments);
    }

    @NonNull
    LifecycleState lifecycleState = LifecycleState.NotCreated;

    @NonNull
    public LifecycleState getLifecycleState() {
        return lifecycleState;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleState = LifecycleState.Created;
    }

    @Override
    public void onStart() {
        super.onStart();
        lifecycleState = LifecycleState.Started;
    }

    @Override
    public void onResume() {
        super.onResume();
        lifecycleState = LifecycleState.Resumed;
    }

    @Override
    public void onPause() {
        super.onPause();
        lifecycleState = LifecycleState.Paused;
    }

    @Override
    public void onStop() {
        super.onStop();
        lifecycleState = LifecycleState.Stopped;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        lifecycleState = LifecycleState.Destroyed;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        lifecycleState = LifecycleState.SaveInstanceState;
    }
}
