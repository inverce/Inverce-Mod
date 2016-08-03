package com.inverce.utils.stateless.annotations;

import com.inverce.utils.events.Event;
import com.inverce.utils.events.annotation.Listener;

public interface StateMachineInterface<INTERFACE extends Listener, STATES extends Enum<?>, CONTEXT> extends Listener {
    INTERFACE post();
    Event<INTERFACE> event();
    STATES[] states();
}
