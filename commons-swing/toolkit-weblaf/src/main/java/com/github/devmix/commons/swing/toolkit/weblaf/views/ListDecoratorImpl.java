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

import com.alee.laf.list.WebList;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.decorators.standard.ListDecorator;
import com.github.devmix.commons.swing.core.listeners.SingletonMouseAdapter;

import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * @author Sergey Grachev
 */
abstract class ListDecoratorImpl extends WebList implements ListDecorator {

    private static final long serialVersionUID = -3210944337490790247L;

    private final ViewWebLaF view;
    private SingletonMouseAdapter mouseAdapter;

    public ListDecoratorImpl(final ViewWebLaF view) {
        this.view = view.register(this);
    }

    @Override
    public View view() {
        return view;
    }

    @Override
    public ListDecorator onMouseReleased(final Consumer<MouseEvent> listener) {
        mouseAdapter().setMouseReleased(listener);
        return this;
    }

    @Override
    public ListDecorator preferredWidth(final int minimumWidth) {
        setPreferredWidth(minimumWidth);
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ListDecorator listData(final Object[] listData) {
        setListData(listData);
        return this;
    }

    private synchronized SingletonMouseAdapter mouseAdapter() {
        if (mouseAdapter == null) {
            mouseAdapter = new SingletonMouseAdapter();
            this.addMouseListener(mouseAdapter);
        }
        return mouseAdapter;
    }
}
