package com.inverce.mod.navigation.interfaces;

import com.inverce.mod.events.Event;
import com.inverce.mod.events.annotation.Listener;
import com.inverce.mod.navigation.requests.NavigationRequest;

/**
 * Standard util for integrating Navigation request utils with FX: EventBus
 */
public interface ImNavEventHandler extends Listener, NavigationHandler {
    public static class Util {
        public static NavigationHandler poster() {
            return new NavigationHandler() {
                @Override
                public void handleRequest(NavigationRequest request) {
                    Event.Bus.post(ImNavEventHandler.class)
                            .handleRequest(request);
                }
            };
        }
    }
}
