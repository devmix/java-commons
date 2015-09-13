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

package com.github.devmix.commons.swing.api.bindings;

import com.github.devmix.commons.swing.api.Controller;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.bindings.standard.ColumnBinding;
import org.jdesktop.beansbinding.Converter;

import java.util.function.Function;

/**
 * @author Sergey Grachev
 */
public interface ComponentBinder<C extends Controller, V extends View> {

    ComponentBinder<C, V> converter(Converter<?, ?> converter);

    ComponentBinder<C, V> property(String propertyName);

    ComponentBinder<C, V> property(ColumnBinding... columns);

    ComponentBinder<C, V> read();

    ComponentBinder<C, V> readOnce();

    ComponentBinder<C, V> readWrite();

    ComponentBinder<C, V> selectedElement();

    ComponentBinder<C, V> selectedItem();

    BinderContext<C, V> to(String propertyName);

    BinderContext<C, V> to(Function<C, ?> controllerProperty);
}
