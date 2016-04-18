package com.inverce.utils.events.test;

import com.inverce.utils.events.annotations.CallOnThread;
import com.inverce.utils.events.core.ThreadPolicy;
import com.inverce.utils.events.interfaces.Listener;

/**
 * Created by Dominik on 2016-04-15.
 */
public interface DD extends Listener {
    @CallOnThread(ThreadPolicy.BgThread)
    void testMe();
}
