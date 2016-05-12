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

import com.github.devmix.commons.adapters.api.annotations.BeanProperty;

import java.lang.annotation.Annotation;
import java.util.Objects;

/**
 * @author Sergey Grachev
 */
@SuppressWarnings("ClassExplicitlyAnnotation")
final class MirrorBeanProperty implements BeanProperty {

    private final boolean skip;

    public MirrorBeanProperty(final boolean skip) {
        this.skip = skip;
    }

    @Override
    public boolean skip() {
        return skip;
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return BeanProperty.class;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MirrorBeanProperty that = (MirrorBeanProperty) o;
        return skip == that.skip;
    }

    @Override
    public int hashCode() {
        return Objects.hash(skip);
    }
}
