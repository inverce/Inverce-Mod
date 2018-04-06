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
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;

import static com.inverce.mod.core.verification.Preconditions.checkArgument;
import static com.inverce.mod.core.verification.Preconditions.checkNotNull;

/**
 * The WildcardType interface supports multiple upper bounds and multiple
 * lower bounds. We only support what the Java 6 language needs - at most one
 * bound. If a lower bound is set, the upper bound must be Object.class.
 */
final class WildcardTypeImpl implements WildcardType, Serializable {
    private final Type upperBound;
    @Nullable
    private final Type lowerBound;

    public WildcardTypeImpl(@NonNull Type[] upperBounds, @NonNull Type[] lowerBounds) {
        checkArgument(lowerBounds.length <= 1);
        checkArgument(upperBounds.length == 1);

        if (lowerBounds.length == 1) {
            checkNotNull(lowerBounds[0]);
            Types.checkNotPrimitive(lowerBounds[0]);
            checkArgument(upperBounds[0] == Object.class);
            this.lowerBound = Types.canonicalize(lowerBounds[0]);
            this.upperBound = Object.class;

        } else {
            checkNotNull(upperBounds[0]);
            Types.checkNotPrimitive(upperBounds[0]);
            this.lowerBound = null;
            this.upperBound = Types.canonicalize(upperBounds[0]);
        }
    }

    public Type[] getUpperBounds() {
        return new Type[]{upperBound};
    }

    @Nullable
    public Type[] getLowerBounds() {
        return lowerBound != null ? new Type[]{lowerBound} : Types.EMPTY_TYPE_ARRAY;
    }

    @Override
    public boolean equals(Object other) {
        return other instanceof WildcardType
                && Types.equals(this, (WildcardType) other);
    }

    @Override
    public int hashCode() {
        // this equals Arrays.hashCode(getLowerBounds()) ^ Arrays.hashCode(getUpperBounds());
        return (lowerBound != null ? 31 + lowerBound.hashCode() : 1)
                ^ (31 + upperBound.hashCode());
    }

    @Override
    public String toString() {
        if (lowerBound != null) {
            return "? super " + Types.typeToString(lowerBound);
        } else if (upperBound == Object.class) {
            return "?";
        } else {
            return "? extends " + Types.typeToString(upperBound);
        }
    }

    private static final long serialVersionUID = 0;
}
