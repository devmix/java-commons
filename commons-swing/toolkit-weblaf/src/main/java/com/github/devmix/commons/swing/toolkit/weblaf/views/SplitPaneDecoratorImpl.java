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

import com.alee.laf.splitpane.WebSplitPane;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.decorators.ComponentDecorator;
import com.github.devmix.commons.swing.api.decorators.standard.SplitPaneDecorator;

import java.awt.*;

/**
 * @author Sergey Grachev
 */
abstract class SplitPaneDecoratorImpl extends WebSplitPane implements SplitPaneDecorator {

    private static final long serialVersionUID = -5349104013415626653L;

    private final ViewWebLaF view;

    public SplitPaneDecoratorImpl(final ViewWebLaF view) {
        this.view = view.register(this);
    }

    @Override
    public View view() {
        return view;
    }

    @Override
    public SplitPaneDecorator continuousLayout(final boolean newContinuousLayout) {
        setContinuousLayout(newContinuousLayout);
        return this;
    }

    @Override
    public SplitPaneDecorator dividerLocation(final int location) {
        setDividerLocation(location);
        return this;
    }

    @Override
    public SplitPaneDecorator horizontal() {
        setOrientation(WebSplitPane.HORIZONTAL_SPLIT);
        return this;
    }

    @Override
    public SplitPaneDecorator left(final Component component) {
        setLeftComponent(component);
        return this;
    }

    @Override
    public SplitPaneDecorator left(final ComponentDecorator component) {
        setLeftComponent(component.$());
        return this;
    }

    @Override
    public SplitPaneDecorator margin(final int spacing) {
        setMargin(spacing);
        return this;
    }

    @Override
    public SplitPaneDecorator oneTouchExpandable(final boolean newValue) {
        setOneTouchExpandable(newValue);
        return this;
    }

    @Override
    public SplitPaneDecorator orientation(final int orientation) {
        setOrientation(orientation);
        return this;
    }

    @Override
    public SplitPaneDecorator preferredSize(final Dimension preferredSize) {
        setPreferredSize(preferredSize);
        return this;
    }

    @Override
    public SplitPaneDecorator right(final Component component) {
        setRightComponent(component);
        return this;
    }

    @Override
    public SplitPaneDecorator right(final ComponentDecorator component) {
        setRightComponent(component.$());
        return this;
    }
}
