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
import com.github.devmix.commons.swing.api.bindings.ComponentBinder;
import com.github.devmix.commons.swing.api.bindings.standard.ColumnBinding;
import com.github.devmix.commons.swing.api.decorators.ComponentDecorator;
import com.github.devmix.commons.swing.core.utils.SoftValueMap;
import javassist.util.proxy.Proxy;
import javassist.util.proxy.ProxyFactory;
import org.jdesktop.beansbinding.AutoBinding;
import org.jdesktop.beansbinding.Binding;
import org.jdesktop.beansbinding.Converter;

import javax.swing.*;
import java.lang.ref.WeakReference;
import java.lang.reflect.Method;
import java.util.Objects;
import java.util.function.Function;

import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ;
import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_ONCE;
import static org.jdesktop.beansbinding.AutoBinding.UpdateStrategy.READ_WRITE;

/**
 * @author Sergey Grachev
 */
final class DefaultBinderContext<C extends Controller, V extends View> implements BinderContext<C, V> {

    private final SoftValueMap<Class<C>, C> CONTROLLER_CACHE = new SoftValueMap<>();

    private final Binder binder;
    private final C controller;
    private final V view;
    private final ComponentBinderImpl componentBinder;
    private C controllerProxy;
    private String invokedPropertyName;

    public DefaultBinderContext(final Binder binder, final C controller, final V view) {
        this.binder = binder;
        this.controller = controller;
        this.view = view;
        this.componentBinder = new ComponentBinderImpl();
    }

    @Override
    public ComponentBinder<C, V> bind(final String name) {
        componentBinder.attachComponent(Objects.requireNonNull(view.name(name)));
        return componentBinder;
    }

    @Override
    public void bind() {
        controllerProxy = null;
        binder.bind();
    }

    @SuppressWarnings("unchecked")
    private synchronized C ensureControllerProxy() {
        final Class<C> clazz = (Class<C>) controller.getClass();
        controllerProxy = CONTROLLER_CACHE.get(clazz);
        if (controllerProxy == null) {
            final ProxyFactory f = new ProxyFactory();
            f.setSuperclass(clazz);
            try {
                controllerProxy = (C) f.createClass().newInstance();
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            ((Proxy) controllerProxy).setHandler(this::intercept);

            CONTROLLER_CACHE.put(clazz, controllerProxy);
        }
        return controllerProxy;
    }

    private Object intercept(final Object self, final Method thisMethod, final Method proceed,
                             final Object[] args) {
        final String name = thisMethod.getName();
        if (name.startsWith("get") || name.startsWith("set")) {
            final StringBuilder sb = new StringBuilder(name).delete(0, 3);
            sb.setCharAt(0, Character.toLowerCase(sb.charAt(0)));
            invokedPropertyName = sb.toString();
        }
        return null;
    }

    private static enum PropertyType {
        STRING,
        COLUMNS
    }

    private final class ComponentBinderImpl implements ComponentBinder<C, V> {

        private WeakReference<ComponentDecorator<?>> component;
        private Object property;
        private PropertyType propertyType;
        private AutoBinding.UpdateStrategy strategy = READ_WRITE;
        private Converter<?, ?> converter;

        public void attachComponent(final ComponentDecorator<?> component) {
            cleanup();
            this.component = new WeakReference<>(component);
        }

        private void cleanup() {
            strategy = READ_WRITE;
            property = null;
            propertyType = null;
            component = null;
            converter = null;
        }

        @Override
        public ComponentBinder<C, V> read() {
            strategy = READ;
            return this;
        }

        @Override
        public ComponentBinder<C, V> readOnce() {
            strategy = READ_ONCE;
            return this;
        }

        @Override
        public ComponentBinder<C, V> readWrite() {
            strategy = READ_WRITE;
            return this;
        }

        @Override
        public ComponentBinder<C, V> property(final String propertyName) {
            property = propertyName;
            propertyType = PropertyType.STRING;
            return this;
        }

        @Override
        public ComponentBinder<C, V> property(final ColumnBinding... columns) {
            property = columns;
            propertyType = PropertyType.COLUMNS;
            return this;
        }

        @Override
        public ComponentBinder<C, V> converter(final Converter<?, ?> converter) {
            this.converter = converter;
            return this;
        }

        @Override
        public ComponentBinder<C, V> selectedElement() {
            return property("selectedElement");
        }

        @Override
        public ComponentBinder<C, V> selectedItem() {
            return property("selectedItem");
        }

        @SuppressWarnings("unchecked")
        @Override
        public BinderContext<C, V> to(final String propertyName) {
            final ComponentDecorator<?> c = component.get();
            if (c != null) {
                final Binding binding;
                if (property != null) {
                    binding = bindToProperty(propertyName, c);
                } else {
                    binding = binder.bind(strategy, controller, propertyName, c);
                }
                if (binding != null) {
                    if (converter != null) {
                        binding.setConverter(converter);
                    }
                }
            }
            cleanup();
            return DefaultBinderContext.this;
        }

        private Binding bindToProperty(final String propertyName, final ComponentDecorator<?> decorator) {
            switch (propertyType) {
                case COLUMNS:
                    if (decorator instanceof JTable) {
                        return binder.bindTable(strategy, controller, propertyName,
                                (JTable) decorator, (ColumnBinding[]) property);
                    }
                    throw new IllegalArgumentException("Component is not JTable");

                default:
                    return binder.bind(strategy, controller, propertyName,
                            decorator, String.valueOf(property));
            }
        }

        @Override
        public BinderContext<C, V> to(final Function<C, ?> controllerProperty) {
            controllerProperty.apply(ensureControllerProxy());
            to(invokedPropertyName);
            return DefaultBinderContext.this;
        }
    }
}
