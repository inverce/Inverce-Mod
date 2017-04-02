package com.inverce.mod.core.collections;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.inverce.mod.core.collections.TraversalMethod.*;

@IntDef(value = { DFS, BFS, ASC, DESC }, flag = true)
@SuppressWarnings("PointlessBitwiseExpression")
@Retention(RetentionPolicy.SOURCE)
public @interface TraversalMethod {
    // format is:
    // 00{IS_DESC}{IS_ASC}{IS_BFS}{IS_DFS}{CHILD_ORDER_SET}{SEARCH_METHOD_SET}

    int DFS  = (1 << 0) + (1 << 2);
    int BFS  = (1 << 0) + (1 << 3);
    int ASC  = (1 << 1) + (1 << 4);
    int DESC = (1 << 1) + (1 << 5);
}
