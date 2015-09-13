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

package com.github.devmix.commons.swing.toolkit.weblaf.views;

import com.alee.laf.tabbedpane.WebTabbedPane;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.decorators.ComponentDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.TabbedPaneDecorator;

import java.awt.*;

/**
 * @author Sergey Grachev
 */
abstract class TabbedPaneDecoratorImpl extends WebTabbedPane implements TabbedPaneDecorator {

    private static final long serialVersionUID = 383694889992086374L;

    private final ViewWebLaF view;

    public TabbedPaneDecoratorImpl(final ViewWebLaF view) {
        this.view = view.register(this);
    }

    @Override
    public final View view() {
        return view;
    }

    public TabbedPaneDecorator a(final String title, final Component component) {
        this.add(title, component);
        return this;
    }

    public TabbedPaneDecorator a(final String title, final ComponentDecorator<?> component) {
        this.add(title, component.$());
        return this;
    }
}
