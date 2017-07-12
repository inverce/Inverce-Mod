package com.inverce.mod.integrations.processing;

public interface Processor<ITEM, RESULT> {
    RESULT processJob(ITEM item) throws Exception;
}
