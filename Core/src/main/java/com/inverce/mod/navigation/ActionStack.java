package com.inverce.mod.navigation;

public class ActionStack {
    protected ActionStack previous;
    ActionStack(ActionStack previous) {
        this.previous = previous;
    }

    Manager findManager() {
        if (this instanceof Manager) {
            return (Manager) this;
        } else {
            return previous != null ? previous.findManager() : null;
        }
    }
}
