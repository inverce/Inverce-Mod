package com.inverce.mod.stateless.annotations;

import com.inverce.mod.events.Event;
import com.inverce.mod.events.annotation.Listener;

public interface StateMachineInterface<INTERFACE extends Listener, STATES extends Enum<?>, CONTEXT> extends Listener {
    INTERFACE post();
    Event<INTERFACE> event();
    STATES[] states();
}
