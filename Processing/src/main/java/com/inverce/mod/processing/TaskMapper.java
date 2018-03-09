package com.inverce.mod.processing;

public interface TaskMapper<ITEM> {
    Runnable processJob(ITEM item) throws Exception;
}
