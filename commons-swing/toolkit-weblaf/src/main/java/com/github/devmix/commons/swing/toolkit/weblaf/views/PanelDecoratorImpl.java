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

import com.alee.laf.panel.WebPanel;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.decorators.standard.PanelDecorator;

/**
 * @author Sergey Grachev
 */
abstract class PanelDecoratorImpl extends WebPanel implements PanelDecorator {

    private static final long serialVersionUID = 2371104016876883779L;

    private final ViewWebLaF view;

    public PanelDecoratorImpl(final ViewWebLaF view) {
        this.view = view.register(this);
    }

    @Override
    public View view() {
        return view;
    }

    @Override
    public PanelDecorator margin(final int spacing) {
        setMargin(spacing);
        return this;
    }

    @Override
    public PanelDecorator margin(final int top, final int left, final int bottom, final int right) {
        setMargin(top, left, bottom, right);
        return this;
    }
}
