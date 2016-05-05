/*
 * Commons Library
 * Copyright (c) 2015 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

package com.github.devmix.commons.swing.core.views;

import com.github.devmix.commons.collections.maps.WeakValueMap;
import com.github.devmix.commons.swing.api.decorators.ComponentDecorator;
import com.github.devmix.commons.swing.api.views.ComponentsRegistry;
import org.apache.commons.lang3.StringUtils;

import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Sergey Grachev
 */
public class DefaultComponentsRegistry<T extends ComponentDecorator<?>>
        implements ComponentsRegistry<T> {

    private final Map<String, T> nameToDecorator = new WeakValueMap<>(ConcurrentHashMap::new);
    private final Map<Component, WeakReference<T>> componentToDecorator = new WeakHashMap<>();

    @Override
    public T find(final String name) {
        return nameToDecorator.get(name);
    }

    @Override
    public boolean isExists(final String name) {
        return nameToDecorator.containsKey(name);
    }

    @Override
    public ComponentsRegistry register(final T decorator) {
        final Component component = decorator.$();
        final String name = component.getName();
        if (!StringUtils.isBlank(name)) {
            nameToDecorator.put(name, decorator);
        }
        componentToDecorator.put(component, new WeakReference<>(decorator));
        component.addPropertyChangeListener(this::onPropertyChange);
        return this;
    }

    private void onPropertyChange(final PropertyChangeEvent e) {
        if (!"name".equals(e.getPropertyName())) {
            return;
        }

        final String newValue = (String) e.getNewValue();
        final String oldValue = (String) e.getOldValue();
        if (StringUtils.equals(newValue, oldValue)) {
            return;
        }

        final Component component = StringUtils.isBlank(oldValue) ? null : nameToDecorator.get(oldValue).$();
        if (component != null) {
            nameToDecorator.remove(oldValue);
        }

        if (StringUtils.isNotBlank(newValue)) {
            @SuppressWarnings("SuspiciousMethodCalls")
            final WeakReference<T> reference = componentToDecorator.get(e.getSource());
            final T decorator = reference != null ? reference.get() : null;
            if (decorator != null) {
                nameToDecorator.put(newValue, decorator);
            }
        }
    }
}
