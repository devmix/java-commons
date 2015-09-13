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

import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.decorators.standard.MenuItemDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.PopupMenuDecorator;

import javax.swing.*;

/**
 * @author Sergey Grachev
 */
abstract class PopupMenuDecoratorImpl extends JPopupMenu implements PopupMenuDecorator {

    private static final long serialVersionUID = 454359107402963707L;

    private final ViewWebLaF view;

    public PopupMenuDecoratorImpl(final ViewWebLaF view) {
        this.view = view.register(this);
    }

    @Override
    public View view() {
        return view;
    }

    public PopupMenuDecorator a(final JMenuItem menuItem) {
        add(menuItem);
        return this;
    }

    public PopupMenuDecorator a(final MenuItemDecorator menuItem) {
        add(menuItem.$());
        return this;
    }
}
