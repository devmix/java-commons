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

package com.github.devmix.commons.swing.core.utils;

import com.github.devmix.commons.swing.api.decorators.ComponentDecorator;

import java.awt.*;

/**
 * @author Sergey Grachev
 */
public final class DecoratorUtils {

    private DecoratorUtils() {
    }

    public static void add(final Container container, final ComponentDecorator<?> component) {
        if (component != null) {
            container.add(component.$());
        }
    }

    public static void add(final Container container, final ComponentDecorator<?>... components) {
        if (components != null && components.length > 0) {
            if (components.length == 1) {
                add(container, components[0]);
            } else {
                for (final ComponentDecorator<?> component : components) {
                    add(container, component);
                }
            }
        }
    }

    public static void add(final Container container, final Component component) {
        if (component != null) {
            container.add(component);
        }
    }

    public static void add(final Container container, final Component... components) {
        if (components != null && components.length > 0) {
            if (components.length == 1) {
                add(container, components[0]);
            } else {
                for (final Component component : components) {
                    add(container, component);
                }
            }
        }
    }

    public static void add(final Container container, final ComponentDecorator component, final String constraints) {
        container.add(component.$(), constraints);
    }

    public static void layout(final Container container, final LayoutManager layoutManager) {
        container.setLayout(layoutManager);
    }
}
