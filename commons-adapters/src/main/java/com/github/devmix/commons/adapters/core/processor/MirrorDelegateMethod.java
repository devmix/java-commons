/*
 * Commons Library
 * Copyright (c) 2015-2016 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
 *
 * This software is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this software.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.github.devmix.commons.adapters.core.processor;

import com.github.devmix.commons.adapters.api.annotations.DelegateMethod;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * @author Sergey Grachev
 */
@SuppressWarnings("ClassExplicitlyAnnotation")
final class MirrorDelegateMethod implements DelegateMethod {

    private final String from;
    private final String to;
    private final ReturnValue returnValue;

    public MirrorDelegateMethod(final String from, final String to, final ReturnValue returnValue) {
        this.from = from;
        this.to = to;
        this.returnValue = returnValue;
    }

    @Override
    public String to() {
        return to;
    }

    @Override
    public String from() {
        return from;
    }

    @Override
    public ReturnValue returnValue() {
        return returnValue;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return DelegateMethod.class;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MirrorDelegateMethod that = (MirrorDelegateMethod) o;
        return Objects.equals(from, that.from) &&
                Objects.equals(to, that.to) &&
                returnValue == that.returnValue;
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, returnValue);
    }
}
