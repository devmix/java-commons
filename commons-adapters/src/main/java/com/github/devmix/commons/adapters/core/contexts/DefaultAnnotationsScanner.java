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
import com.github.devmix.commons.adapters.api.annotations.DelegateRule;
import com.github.devmix.commons.adapters.api.annotations.DelegateRules;
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
    protected DelegateRule[] loadGlobalDelegateRules(final Class<?> object) {
        final Set<DelegateRule> result = findDelegateRules(object.getAnnotations(), new HashSet<DelegateRule>());
        return result.toArray(new DelegateRule[result.size()]);
    }

    @Nullable
    @Override
    protected DelegateRule[] loadMethodDelegateRules(final CtMethod method) {
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

        final Set<DelegateRule> result = findDelegateRules(annotations, new HashSet<DelegateRule>());
        return result.toArray(new DelegateRule[result.size()]);
    }

    private Set<DelegateRule> findDelegateRules(final Annotation[] annotations, final Set<DelegateRule> result) {
        for (final Annotation annotation : annotations) {
            if (annotation instanceof DelegateRule) {
                result.add((DelegateRule) annotation);
            } else if (annotation instanceof DelegateRules) {
                Collections.addAll(result, ((DelegateRules) annotation).value());
            } else {
                final Class<? extends Annotation> qualifier = annotation.annotationType();
                if (qualifier.isAnnotationPresent(DelegateRule.class) || qualifier.isAnnotationPresent(DelegateRules.class)) {
                    findDelegateRules(qualifier.getAnnotations(), result);
                }
            }
        }
        return result;
    }
}
