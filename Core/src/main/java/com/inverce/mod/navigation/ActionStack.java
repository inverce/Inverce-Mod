package com.inverce.mod.navigation;

public class ActionStack {
    protected ActionStack previous;
    ActionStack(ActionStack previous) {
        this.previous = previous;
    }
}
