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

import com.alee.extended.breadcrumb.WebBreadcrumb;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.decorators.extended.BreadcrumbDecorator;

/**
 * @author Sergey Grachev
 */
abstract class BreadcrumbDecoratorImpl extends WebBreadcrumb implements BreadcrumbDecorator {

    private static final long serialVersionUID = 6703022647884769891L;

    private final ViewWebLaF view;

    public BreadcrumbDecoratorImpl(final ViewWebLaF view) {
        this.view = view.register(this);
    }

    @Override
    public View view() {
        return view;
    }

    @Override
    public BreadcrumbDecorator decorated(final boolean decorated) {
        setUndecorated(!decorated);
        return this;
    }
}
