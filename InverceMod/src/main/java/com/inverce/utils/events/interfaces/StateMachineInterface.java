package com.inverce.utils.events.interfaces;

import com.inverce.utils.events.Event;

public interface StateMachineInterface<INTERFACE extends Listener, STATES extends Enum<?>, CONTEXT> extends Listener {
    INTERFACE post();
    Event<INTERFACE> event();
    STATES[] states();
}
