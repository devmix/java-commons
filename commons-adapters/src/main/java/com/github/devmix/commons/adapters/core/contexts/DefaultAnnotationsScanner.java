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

package com.github.devmix.commons.adapters.core.contexts;

import com.github.devmix.commons.adapters.api.annotations.Adaptee;
import com.github.devmix.commons.adapters.api.annotations.Adapter;
import com.github.devmix.commons.adapters.api.annotations.BeanProperty;
import com.github.devmix.commons.adapters.api.annotations.DelegateMethod;
import com.github.devmix.commons.adapters.api.annotations.DelegateMethods;
import com.github.devmix.commons.adapters.core.commons.AbstractCachedAnnotationsScanner;
import javassist.CtMethod;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
final class DefaultAnnotationsScanner extends AbstractCachedAnnotationsScanner<Class<?>, Adapter, Method, CtMethod> {

    @Nullable
    @Override
    protected Adapter loadAdapterFor(final Class<?> object) {
        return object.getAnnotation(Adapter.class);
    }

    @Nullable
    @Override
    protected Method loadAdapteeMethod(final Class<?> object) {
        for (final Method method : object.getMethods()) {
            if (!method.isSynthetic() && method.isAnnotationPresent(Adaptee.class)) {
                return method;
            }
        }
        return null;
    }

    @Nullable
    @Override
    protected DelegateMethod[] loadGlobalDelegateMethods(final Class<?> object) {
        final Set<DelegateMethod> result = findDelegateRules(object.getAnnotations(), new HashSet<DelegateMethod>());
        return result.toArray(new DelegateMethod[result.size()]);
    }

    @Nullable
    @Override
    protected DelegateMethod[] loadMethodDelegateMethods(final CtMethod method) {
        final Object[] objects;
        try {
            objects = method.getAnnotations();
        } catch (final ClassNotFoundException ignore) {
            return null;
        }

        if (objects.length == 0) {
            return null;
        }

        final Annotation[] annotations = new Annotation[objects.length];
        for (int i = 0, objectsLength = objects.length; i < objectsLength; i++) {
            annotations[i] = (Annotation) objects[i];
        }

        final Set<DelegateMethod> result = findDelegateRules(annotations, new HashSet<DelegateMethod>());
        return result.toArray(new DelegateMethod[result.size()]);
    }

    @Nullable
    @Override
    protected BeanProperty[] loadGlobalBeanProperties(final Class<?> object) {
        return null;
    }

    @Nullable
    @Override
    protected BeanProperty[] loadMethodBeanProperties(final CtMethod method) {
        return null;
    }

    private Set<DelegateMethod> findDelegateRules(final Annotation[] annotations, final Set<DelegateMethod> result) {
        for (final Annotation annotation : annotations) {
            if (annotation instanceof DelegateMethod) {
                result.add((DelegateMethod) annotation);
            } else if (annotation instanceof DelegateMethods) {
                Collections.addAll(result, ((DelegateMethods) annotation).value());
            } else {
                final Class<? extends Annotation> qualifier = annotation.annotationType();
                if (qualifier.isAnnotationPresent(DelegateMethod.class) || qualifier.isAnnotationPresent(DelegateMethods.class)) {
                    findDelegateRules(qualifier.getAnnotations(), result);
                }
            }
        }
        return result;
    }
}
