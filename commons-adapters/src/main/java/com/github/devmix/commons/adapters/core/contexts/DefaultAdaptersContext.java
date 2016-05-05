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

import com.github.devmix.commons.adapters.api.AdaptersContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sergey Grachev
 */
final class DefaultAdaptersContext implements AdaptersContext {

    private static final Logger LOG = LoggerFactory.getLogger(DefaultAdaptersContext.class);

    private final Map<Class<?>, AdapterDescriptor> adapters;
    private final Map<Class<?>, AdapterDescriptor> interfaceToAdapterCache = new ConcurrentHashMap<>();
    private final Map<Class<?>, AdapterDescriptor> adapteeToAdapterCache = new ConcurrentHashMap<>();

    public DefaultAdaptersContext(final Map<Class<?>, AdapterDescriptor> adapters) {
        this.adapters = adapters;
    }

    @Nullable
    @Override
    public <T> T findAndCreate(final Class<? extends T> adapterClass) {
        final AdapterDescriptor descriptor = findDescriptorByAdapter(adapterClass);
        return descriptor == null ? null : descriptor.<T>createSilent();
    }

    @Nullable
    @Override
    public <T> T findAndCreate(final Class<? extends T> adapterClass, final Object... constructorArgs) {
        final AdapterDescriptor descriptor = findDescriptorByAdapter(adapterClass);
        return descriptor == null ? null : descriptor.<T>createSilent(constructorArgs);
    }

    @Nullable
    @Override
    public <T> T findAndCreateByAdaptee(final Class<?> adapteeClass) {
        final AdapterDescriptor descriptor = findDescriptorByAdaptee(adapteeClass);
        return descriptor == null ? null : descriptor.<T>createSilent();
    }

    @Nullable
    @Override
    public <T> T findAndCreateByAdaptee(final Class<?> adapteeClass, final Object... constructorArgs) {
        final AdapterDescriptor descriptor = findDescriptorByAdaptee(adapteeClass);
        return descriptor == null ? null : descriptor.<T>createSilent(constructorArgs);
    }

    @Nullable
    private <T> AdapterDescriptor findDescriptorByAdapter(final Class<? extends T> adapterClass) {
        AdapterDescriptor descriptor = adapters.get(adapterClass);
        if (descriptor == null) {
            descriptor = interfaceToAdapterCache.get(adapterClass);
            if (descriptor == null && !interfaceToAdapterCache.containsKey(adapterClass)) {
                for (final Map.Entry<Class<?>, AdapterDescriptor> entry : adapters.entrySet()) {
                    final Class<?>[] interfaces = entry.getKey().getInterfaces();
                    if (interfaces.length > 0) {
                        for (final Class<?> interfaceClass : interfaces) {
                            if (interfaceClass.equals(adapterClass)) {
                                descriptor = entry.getValue();
                                interfaceToAdapterCache.put(adapterClass, descriptor);
                            }
                        }
                    }
                }
            }
        }
        return descriptor;
    }

    @Nullable
    private AdapterDescriptor findDescriptorByAdaptee(final Class<?> adapteeClass) {
        AdapterDescriptor descriptor = adapteeToAdapterCache.get(adapteeClass);
        if (descriptor == null && !adapteeToAdapterCache.containsKey(adapteeClass)) {
            for (final Map.Entry<Class<?>, AdapterDescriptor> entry : adapters.entrySet()) {
                final AdapterDescriptor nextDescriptor = entry.getValue();
                if (nextDescriptor.getAdapteeClass().equals(adapteeClass)) {
                    descriptor = entry.getValue();
                    adapteeToAdapterCache.put(adapteeClass, descriptor);
                }
            }
        }
        return descriptor;
    }
}
