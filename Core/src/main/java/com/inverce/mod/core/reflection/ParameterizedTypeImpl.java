package com.inverce.mod.core.reflection;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import static com.inverce.mod.core.verification.Preconditions.checkArgument;
import static com.inverce.mod.core.verification.Preconditions.checkNotNull;

/**
 * Created by Inverce on 2017-03-27.
 */
final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
    private final Type ownerType;
    private final Type rawType;
    private final Type[] typeArguments;

    public ParameterizedTypeImpl(Type ownerType, Type rawType, Type... typeArguments) {
        // require an owner type if the raw type needs it
        if (rawType instanceof Class<?>) {
            Class<?> rawTypeAsClass = (Class<?>) rawType;
            boolean isStaticOrTopLevelClass = Modifier.isStatic(rawTypeAsClass.getModifiers())
                    || rawTypeAsClass.getEnclosingClass() == null;
            checkArgument(ownerType != null || isStaticOrTopLevelClass);
        }

        this.ownerType = ownerType == null ? null : Types.canonicalize(ownerType);
        this.rawType = Types.canonicalize(rawType);
        this.typeArguments = typeArguments.clone();
        for (int t = 0, length = this.typeArguments.length; t < length; t++) {
            checkNotNull(this.typeArguments[t]);
            Types.checkNotPrimitive(this.typeArguments[t]);
            this.typeArguments[t] = Types.canonicalize(this.typeArguments[t]);
        }
    }

    public Type[] getActualTypeArguments() {
        return typeArguments.clone();
    }

    public Type getRawType() {
        return rawType;
    }

    public Type getOwnerType() {
        return ownerType;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof ParameterizedType
                && Types.equals(this, (ParameterizedType) other);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(typeArguments)
                ^ rawType.hashCode()
                ^ Types.hashCodeOrZero(ownerType);
    }

    @Override
    public String toString() {
        int length = typeArguments.length;
        if (length == 0) {
            return Types.typeToString(rawType);
        }

        StringBuilder stringBuilder = new StringBuilder(30 * (length + 1));
        stringBuilder.append(Types.typeToString(rawType)).append("<").append(Types.typeToString(typeArguments[0]));
        for (int i = 1; i < length; i++) {
            stringBuilder.append(", ").append(Types.typeToString(typeArguments[i]));
        }
        return stringBuilder.append(">").toString();
    }

    private static final long serialVersionUID = 0;
}
