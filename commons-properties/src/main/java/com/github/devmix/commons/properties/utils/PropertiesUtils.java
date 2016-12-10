/*
 * Commons Library
 * Copyright (c) 2013-2016 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

package com.github.devmix.commons.properties.utils;

import com.github.devmix.commons.properties.Property;
import com.github.devmix.commons.properties.annotations.Key;
import com.github.devmix.commons.properties.exceptions.PropertyKeyException;
import com.github.devmix.commons.properties.identity.Identity;
import com.github.devmix.commons.properties.identity.StandardNameProvider;
import com.github.devmix.commons.properties.restrictions.Restriction;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Sergey Grachev
 */
public final class PropertiesUtils {

    private PropertiesUtils() {
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public static <T extends Annotation> T findAnnotation(final Property property, final Class<T> annotationClass) {
        final Class clazz = property.getClass();
        if (clazz.isEnum()) {
            try {
                final String name = ((Enum) property).name();
                return clazz.getField(name).getAnnotation(annotationClass);
            } catch (final NoSuchFieldException ignore) {
                return null;
            }
        }
        return (T) clazz.getAnnotation(annotationClass);
    }

    public static Identity.NameProvider providerOf(final Key key) {
        final Class<? extends Identity.NameProvider> clazz = key.provider();
        if (clazz.equals(Identity.NameProvider.class) || clazz.equals(StandardNameProvider.class)) {
            return Identity.standard();
        }
        try {
            return clazz.newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            throw new PropertyKeyException(key, e);
        }
    }

    @Nullable
    public static Annotation[] filterRestrictions(final Annotation[] annotations) {
        if (annotations.length > 0) {
            final List<Annotation> restrictions = new LinkedList<>();
            for (final Annotation annotation : annotations) {
                if (annotation.annotationType().isAnnotationPresent(Restriction.class)) {
                    restrictions.add(annotation);
                }
            }
            if (!restrictions.isEmpty()) {
                return restrictions.toArray(new Annotation[restrictions.size()]);
            }
        }
        return null;
    }
}
