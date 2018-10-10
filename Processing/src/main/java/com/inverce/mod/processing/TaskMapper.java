package com.inverce.mod.processing;

import android.support.annotation.NonNull;

public interface TaskMapper<ITEM> {
    @NonNull
    Runnable processJob(ITEM item) throws Exception;
}
