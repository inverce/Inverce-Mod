package com.inverce.mod.events.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD})
public @interface EventInfo {

    /**
     * Specifies thread on which callbacks should be run
     */
    ThreadPolicy thread() default ThreadPolicy.CallingThread;

    /**
     * Used when returning AsyncFeature, specifies whatever yield only first value or wait for all calls to return
     */
    boolean yieldAll() default false;
}
