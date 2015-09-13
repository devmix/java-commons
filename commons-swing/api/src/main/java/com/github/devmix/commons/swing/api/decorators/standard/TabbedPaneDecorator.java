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

package com.github.devmix.commons.swing.api.decorators.standard;

import com.github.devmix.commons.swing.api.decorators.ComponentDecorator;
import com.github.devmix.commons.swing.api.decorators.ComponentViewDecorator;
import com.github.devmix.commons.swing.api.decorators.ContainerDecorator;
import com.github.devmix.commons.swing.api.decorators.SetupCallable;

import javax.swing.*;
import java.awt.*;

/**
 * @author Sergey Grachev
 */
public interface TabbedPaneDecorator extends ComponentViewDecorator<JTabbedPane>, ContainerDecorator<JTabbedPane> {

    @Override
    JTabbedPane $();

    @Override
    <C extends JTabbedPane> TabbedPaneDecorator $(SetupCallable<C> setup);

    @Override
    TabbedPaneDecorator name(String name);

    TabbedPaneDecorator a(Component component);

    TabbedPaneDecorator a(ComponentDecorator component);

    TabbedPaneDecorator a(Component... components);

    TabbedPaneDecorator a(ComponentDecorator<?>... components);

    TabbedPaneDecorator a(Component component, String constraints);

    TabbedPaneDecorator a(ComponentDecorator<?> component, String constraints);

    TabbedPaneDecorator a(String title, Component component);

    TabbedPaneDecorator a(String title, ComponentDecorator<?> component);
}
