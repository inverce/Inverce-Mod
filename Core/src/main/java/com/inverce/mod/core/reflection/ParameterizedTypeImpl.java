/*
 * Copyright (C) 2008 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Modifications copyright (C) 2013 Inverce
 */
package com.inverce.mod.core.reflection;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

import static com.inverce.mod.core.verification.Preconditions.checkArgument;
import static com.inverce.mod.core.verification.Preconditions.checkNotNull;

final class ParameterizedTypeImpl implements ParameterizedType, Serializable {
    @Nullable
    private final Type ownerType;
    private final Type rawType;
    private final Type[] typeArguments;

    public ParameterizedTypeImpl(@Nullable Type ownerType, Type rawType, @NonNull Type... typeArguments) {
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
        parseTypeArguments();
    }

    private void parseTypeArguments() {
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

    @Nullable
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
        appendTypeArguments(stringBuilder, length);
        return stringBuilder.append(">").toString();
    }

    private void appendTypeArguments(@NonNull StringBuilder stringBuilder, int length) {
        stringBuilder
                .append(Types.typeToString(rawType))
                .append("<")
                .append(Types.typeToString(typeArguments[0]));

        for (int i = 1; i < length; i++) {
            stringBuilder.append(", ").append(Types.typeToString(typeArguments[i]));
        }
    }

    private static final long serialVersionUID = 0;
}
