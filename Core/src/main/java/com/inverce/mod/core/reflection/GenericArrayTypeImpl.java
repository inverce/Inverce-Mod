package com.inverce.mod.core.reflection;

import java.io.Serializable;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;

final class GenericArrayTypeImpl implements GenericArrayType, Serializable {
    private final Type componentType;

    public GenericArrayTypeImpl(Type componentType) {
        this.componentType = Types.canonicalize(componentType);
    }

    public Type getGenericComponentType() {
        return componentType;
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof GenericArrayType
                && Types.equals(this, (GenericArrayType) o);
    }

    @Override
    public int hashCode() {
        return componentType.hashCode();
    }

    @Override
    public String toString() {
        return Types.typeToString(componentType) + "[]";
    }

    private static final long serialVersionUID = 0;
}
