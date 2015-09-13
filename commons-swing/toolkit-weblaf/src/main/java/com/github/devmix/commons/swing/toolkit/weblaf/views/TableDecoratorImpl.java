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

import com.alee.laf.table.WebTable;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.decorators.standard.TableDecorator;
import com.github.devmix.commons.swing.core.listeners.SingletonMouseAdapter;

import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * @author Sergey Grachev
 */
abstract class TableDecoratorImpl extends WebTable implements TableDecorator {

    private static final long serialVersionUID = -5527974482931272811L;

    private final ViewWebLaF view;
    private SingletonMouseAdapter mouseAdapter;

    public TableDecoratorImpl(final ViewWebLaF view) {
        this.view = view.register(this);
    }

    @Override
    public View view() {
        return view;
    }

    @Override
    public TableDecorator onMouseClicked(final Consumer<MouseEvent> listener) {
        mouseAdapter().setMouseClicked(listener);
        return this;
    }

    @Override
    public TableDecorator onMouseReleased(final Consumer<MouseEvent> listener) {
        mouseAdapter().setMouseReleased(listener);
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
