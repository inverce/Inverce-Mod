package com.inverce.utils.stateless.test;

import com.inverce.utils.events.annotation.EventInfo;
import com.inverce.utils.events.annotation.ThreadPolicy;
import com.inverce.utils.events.annotation.Listener;

/**
 * Created by Dominik on 2016-04-15.
 */
public interface DD extends Listener {
    @EventInfo(ThreadPolicy.BgThread)
    void testMe();
}
