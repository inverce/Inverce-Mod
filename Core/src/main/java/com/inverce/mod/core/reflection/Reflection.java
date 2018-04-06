package com.inverce.mod.core.reflection;

import android.support.annotation.NonNull;

import com.inverce.mod.core.collections.CacheFunctionMap;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Reflection {
    private static CacheFunctionMap<Class<?>, Set<Class<?>>> superInterfaces;

    static {
        superInterfaces = new CacheFunctionMap<>(Reflection::getImplementedInterfacesInternal);
    }

    public static Set<Class<?>> getImplementedInterfaces(Class<?> clazz) {
        return superInterfaces.get(clazz);
    }

    private static Set<Class<?>> getImplementedInterfacesInternal(@NonNull Class<?> clazz) {
        if (clazz == Object.class) {
            return new HashSet<>();
        }

        Set<Class<?>> toReturn = getImplementedInterfacesInternal(clazz.getSuperclass());
        Class<?>[] interfaces = clazz.getInterfaces();

        for (Class<?> i: interfaces) {
            Class<?>[] inner = i.getInterfaces();
            if (inner != null) {
                Collections.addAll(toReturn, inner);
            }
        }
        Collections.addAll(toReturn, interfaces);
        return toReturn;
    }
}
