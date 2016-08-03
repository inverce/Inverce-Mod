package com.inverce.utils.stateless.annotations;

import com.inverce.utils.events.annotation.ThreadPolicy;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({METHOD})
public @interface State {
    String value() default "*From*Context*";
    ThreadPolicy threadPolicy() default ThreadPolicy.CallingThread;
}
