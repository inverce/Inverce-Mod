package com.example.test.not_empty;

import com.inverce.mod.events.annotation.EventInfo;
import com.inverce.mod.events.annotation.Listener;
import com.inverce.mod.events.annotation.ThreadPolicy;

public interface Intre extends Listener{

    @EventInfo(thread = ThreadPolicy.BgThread)
    void pps();
}
