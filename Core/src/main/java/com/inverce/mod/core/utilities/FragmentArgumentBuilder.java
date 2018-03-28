package com.inverce.mod.core.utilities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;

public class FragmentArgumentBuilder<T extends Fragment> extends SubBundleBuilder<T> {
    public FragmentArgumentBuilder(@NonNull T parent) {
        super(parent, parent.getArguments() != null ? parent.getArguments() : new Bundle());
    }

    @Override
    public T create() {
        this.parent.setArguments(this.extras);
        return super.create();
    }
}
