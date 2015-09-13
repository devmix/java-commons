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

package com.github.devmix.commons.swing.core.bindings;

import com.github.devmix.commons.swing.api.Controller;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.bindings.Binder;
import com.github.devmix.commons.swing.api.bindings.BinderContext;
import com.github.devmix.commons.swing.api.bindings.standard.ColumnBinding;
import com.github.devmix.commons.swing.core.utils.BindingUtils;
import org.jdesktop.beansbinding.AutoBinding.UpdateStrategy;
import org.jdesktop.beansbinding.BeanProperty;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.BindingGroup;
import org.jdesktop.beansbinding.Bindings;
import org.jdesktop.swingbinding.JComboBoxBinding;
import org.jdesktop.swingbinding.JListBinding;
import org.jdesktop.swingbinding.JTableBinding;
import org.jdesktop.swingbinding.SwingBindings;

import javax.swing.*;

/**
 * @author Sergey Grachev
 */
public class DefaultBinder extends BindingGroup implements Binder {

    @Override
    public <C extends Controller, V extends View> BinderContext<C, V> with(final C controller, final V view) {
        return new DefaultBinderContext<>(this, controller, view);
    }

    @Override
    public Binding bind(
            final UpdateStrategy strategy, final Object source, final String sourceProperty, final Object target) {

        if (target instanceof JList) {
            return bindList(strategy, source, sourceProperty, (JList) target);
        } else if (target instanceof JComboBox) {
            return bindComboBox(strategy, source, sourceProperty, (JComboBox) target);
        } else if (target instanceof JFormattedTextField) {
            return bind(strategy, source, sourceProperty, target, "value");
        } else {
            return bind(strategy, source, sourceProperty, target, "text");
        }
    }

    public Binding bindList(
            final UpdateStrategy strategy,
            final Object source, final String sourceProperty, final JList target) {

        final String name = BindingUtils.bindingNameOf(source, sourceProperty, target, null);
        final JListBinding binding = SwingBindings.createJListBinding(
                strategy, source, BeanProperty.create(sourceProperty), target,
                name);

        super.addBinding(binding);

        return binding;
    }

    @Override
    public Binding bindComboBox(
            final UpdateStrategy strategy,
            final Object source, final String sourceProperty, final JComboBox target) {

        final String name = BindingUtils.bindingNameOf(source, sourceProperty, target, null);
        final JComboBoxBinding binding = SwingBindings.createJComboBoxBinding(
                strategy, source, BeanProperty.create(sourceProperty), target,
                name);

        super.addBinding(binding);

        return binding;
    }

    @SuppressWarnings("unchecked")
    @Override
    public Binding bindTable(
            final UpdateStrategy strategy,
            final Object source, final String sourceProperty,
            final JTable target, final ColumnBinding[] targetColumns) {

        final String name = BindingUtils.bindingNameOf(source, sourceProperty, target, null);
        final JTableBinding binding = SwingBindings.createJTableBinding(
                strategy, source, BeanProperty.create(sourceProperty), target,
                name);

        for (final ColumnBinding column : targetColumns) {
            final JTableBinding.ColumnBinding cb = binding.addColumnBinding(
                    BeanProperty.create(column.getPropertyName()));
            cb.setColumnName(column.getTitle());
            cb.setColumnClass(column.getColumnClass());
            cb.setEditable(column.isEditable());
        }

        super.addBinding(binding);

        return binding;
    }

    @Override
    public Binding bind(
            final UpdateStrategy strategy, final Object source, final String sourceProperty,
            final Object target, final String targetProperty) {

        final String name = BindingUtils.bindingNameOf(source, sourceProperty, target, targetProperty);
        final Binding binding = Bindings.createAutoBinding(
                strategy, source, BeanProperty.create(sourceProperty),
                target, BeanProperty.create(targetProperty),
                name);

        super.addBinding(binding);

        return binding;
    }
}
