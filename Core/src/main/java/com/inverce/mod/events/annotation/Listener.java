package com.inverce.mod.events.annotation;

/**
 * This is marker class to not allow anything as subscribed listener mostly security and leak reasons,
 * basically only instances that implements or are subclasses of this interface are allowed
 */
public interface Listener {
}