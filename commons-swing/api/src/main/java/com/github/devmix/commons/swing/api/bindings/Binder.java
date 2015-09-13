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
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.Binding;

import javax.swing.*;

/**
 * @author Sergey Grachev
 */
public interface Binder {

    <C extends Controller, V extends View> BinderContext<C, V> with(C controller, V view);

    void bind();

    void unbind();

    Binding bind(
            UpdateStrategy strategy, Object source, String sourceProperty, Object target);

    Binding bind(
            UpdateStrategy strategy, Object source, String sourceProperty, Object target, String targetProperty);

    Binding bindComboBox(
            UpdateStrategy strategy, Object source, String sourceProperty, JComboBox target);

    Binding bindList(
            UpdateStrategy strategy, Object source, String sourceProperty, JList target);

    Binding bindTable(
            UpdateStrategy strategy, Object source, String sourceProperty, JTable target, ColumnBinding[] targetColumns);
}
