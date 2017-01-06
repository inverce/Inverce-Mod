package com.inverce.mod.stateless;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public final class EventsModule {
    private EventsModule() { }
    public static void init() {
        StateMachine.init();
    }

    /* internal */ static <T> Type[] genericTypes(Class<? extends T> clazz, Class<T> origin) {
        ParameterizedType type = (ParameterizedType)(clazz.getGenericSuperclass());
        return type.getActualTypeArguments();
    }
}
