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
