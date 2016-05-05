/*
 * Commons Library
 * Copyright (c) 2016 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

import org.apache.commons.collections.map.AbstractReferenceMap;
import org.apache.commons.collections.map.ReferenceMap;
import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.reflect.ConstructorUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Sergey Grachev
 */
final class AdapterDescriptor {

    private static final Logger LOG = LoggerFactory.getLogger(AdapterDescriptor.class);
    private static final Constructor<?> NO_CONSTRUCTOR = AdapterDescriptor.class.getConstructors()[0];

    @SuppressWarnings("unchecked")
    private final Map<ConstructorCacheKey, Constructor<?>> constructorsCache =
            new ReferenceMap(AbstractReferenceMap.HARD, AbstractReferenceMap.SOFT);

    private final Class<?> adapteeClass;
    private final Class<?> generatedClass;

    public AdapterDescriptor(final Class<?> adapteeClass, final Class<?> generatedClass) {
        this.adapteeClass = adapteeClass;
        this.generatedClass = generatedClass;
    }

    public Class<?> getGeneratedClass() {
        return generatedClass;
    }

    public Class<?> getAdapteeClass() {
        return adapteeClass;
    }

    @Nullable
    public Constructor<?> findConstructor(final Object... args) {
        final ConstructorCacheKey key = new ConstructorCacheKey(ClassUtils.toClass(args));

        Constructor<?> constructor;
        synchronized (constructorsCache) {
            constructor = constructorsCache.get(key);
            if (constructor == null) {
                constructor = ConstructorUtils.getMatchingAccessibleConstructor(generatedClass, key.parameters);
                constructorsCache.put(key, constructor == null ? NO_CONSTRUCTOR : constructor);
            }
        }

        return NO_CONSTRUCTOR == constructor ? null : constructor;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T createSilent(final Object... constructorArgs) {
        final Constructor<?> constructor = findConstructor(constructorArgs);
        if (constructor != null) {
            try {
                return (T) constructor.newInstance(constructorArgs);
            } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
                LOG.error("Not possible create new instance of adapter", e);
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    public <T> T createSilent() {
        try {
            return (T) getGeneratedClass().newInstance();
        } catch (IllegalAccessException | InstantiationException e) {
            LOG.error("Not possible create new instance of adapter", e);
        }
        return null;
    }

    private static final class ConstructorCacheKey {
        private final Class<?>[] parameters;
        private final int hashCode;

        public ConstructorCacheKey(final Class<?>[] parameters) {
            this.parameters = parameters;
            this.hashCode = Arrays.hashCode(parameters);
        }

        @Override
        public boolean equals(final Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            final ConstructorCacheKey that = (ConstructorCacheKey) o;

            // Probably incorrect - comparing Object[] arrays with Arrays.equals
            return hashCode == that.hashCode && Arrays.equals(parameters, that.parameters);
        }

        @Override
        public int hashCode() {
            return this.hashCode;
        }
    }
}
