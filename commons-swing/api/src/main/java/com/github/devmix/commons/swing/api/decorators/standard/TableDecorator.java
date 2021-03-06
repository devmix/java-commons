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

import com.github.devmix.commons.swing.api.decorators.ComponentViewDecorator;
import com.github.devmix.commons.swing.api.decorators.SetupCallable;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.util.function.Consumer;

/**
 * @author Sergey Grachev
 */
public interface TableDecorator extends ComponentViewDecorator<JTable> {

    @Override
    JTable $();

    @Override
    <C extends JTable> TableDecorator $(SetupCallable<C> setup);

    @Override
    TableDecorator name(String name);

    TableDecorator enableFeature(Feature feature);

    TableDecorator disableFeature(Feature feature);

    TableDecorator onMouseClicked(Consumer<MouseEvent> listener);

    TableDecorator onMouseReleased(Consumer<MouseEvent> listener);

    TableDecorator popupMenu(PopupMenuDecorator menu);

    enum Feature {
        SELECT_ROW_ON_RIGHT_CLICK
    }
}
