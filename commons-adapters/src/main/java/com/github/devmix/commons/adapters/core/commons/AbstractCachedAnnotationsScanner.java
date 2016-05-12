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

package com.github.devmix.commons.adapters.core.commons;

import com.github.devmix.commons.adapters.api.annotations.BeanProperty;
import com.github.devmix.commons.adapters.api.annotations.DelegateMethod;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of scanner which supports caching.
 *
 * @author Sergey Grachev
 * @see AnnotationsScanner
 */
public abstract class AbstractCachedAnnotationsScanner<C, A, S, M> implements AnnotationsScanner<C, A, S, M> {

    private static final DelegateMethod[] EMPTY_DELEGATE_METHODS = new DelegateMethod[0];
    private static final BeanProperty[] EMPTY_BEAN_PROPERTIES = new BeanProperty[0];

    private final Map<C, A> adapters = new HashMap<>();
    private final Map<C, S> adapteeMethods = new HashMap<>();
    private final Map<C, DelegateMethod[]> globalDelegateMethods = new HashMap<>();
    private final Map<M, DelegateMethod[]> methodDelegateMethods = new HashMap<>();
    private final Map<C, BeanProperty[]> globalBeanProperties = new HashMap<>();
    private final Map<M, BeanProperty[]> methodBeanProperties = new HashMap<>();

    @Nullable
    protected abstract A loadAdapterFor(C object);

    @Nullable
    protected abstract S loadAdapteeMethod(C object);

    @Nullable
    protected abstract DelegateMethod[] loadGlobalDelegateMethods(C object);

    @Nullable
    protected abstract DelegateMethod[] loadMethodDelegateMethods(M method);

    @Nullable
    protected abstract BeanProperty[] loadGlobalBeanProperties(C object);

    @Nullable
    protected abstract BeanProperty[] loadMethodBeanProperties(M method);

    @Nullable
    @Override
    public A adapterFor(final C object) {
        final A cached = adapters.get(object);
        if (cached != null) {
            return cached;
        }
        return put(adapters, object, loadAdapterFor(object), null);
    }

    @Nullable
    @Override
    public S adapteeFor(final C object) {
        final S cached = adapteeMethods.get(object);
        if (cached != null) {
            return cached;
        }
        return put(adapteeMethods, object, loadAdapteeMethod(object), null);
    }

    @Override
    public DelegateMethod[] globalDelegateMethodsOf(final C object) {
        final DelegateMethod[] cached = globalDelegateMethods.get(object);
        if (cached != null) {
            return cached;
        }
        return put(globalDelegateMethods, object, loadGlobalDelegateMethods(object), EMPTY_DELEGATE_METHODS);
    }

    @Override
    public DelegateMethod[] methodDelegateMethodsOf(final M method) {
        final DelegateMethod[] cached = methodDelegateMethods.get(method);
        if (cached != null) {
            return cached;
        }
        return put(methodDelegateMethods, method, loadMethodDelegateMethods(method), EMPTY_DELEGATE_METHODS);
    }

    @Override
    public BeanProperty[] globalBeanPropertiesOf(final C object) {
        final BeanProperty[] cached = globalBeanProperties.get(object);
        if (cached != null) {
            return cached;
        }
        return put(globalBeanProperties, object, loadGlobalBeanProperties(object), EMPTY_BEAN_PROPERTIES);
    }

    @Override
    public BeanProperty[] methodBeanPropertiesOf(final M method) {
        final BeanProperty[] cached = methodBeanProperties.get(method);
        if (cached != null) {
            return cached;
        }
        return put(methodBeanProperties, method, loadMethodBeanProperties(method), EMPTY_BEAN_PROPERTIES);
    }

    private <K, V> V put(final Map<K, V> map, final K key, @Nullable final V value, final V defaultValue) {
        if (value != null) {
            map.put(key, value);
            return value;
        }
        map.put(key, defaultValue);
        return defaultValue;
    }
}
