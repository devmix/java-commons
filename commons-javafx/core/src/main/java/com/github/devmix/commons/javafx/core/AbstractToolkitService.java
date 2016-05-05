/*
 * Commons Library
 * Copyright (c) 2015-2016 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

package com.github.devmix.commons.javafx.core;

import com.github.devmix.commons.javafx.api.ToolkitService;
import com.github.devmix.commons.javafx.api.i18n.I18n;
import com.github.devmix.commons.javafx.api.i18n.I18nFactory;
import com.github.devmix.commons.javafx.api.utils.Utils;
import com.github.devmix.commons.javafx.api.utils.UtilsFactory;
import com.github.devmix.commons.javafx.api.views.View;
import com.github.devmix.commons.javafx.api.views.ViewsFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.Nullable;
import java.util.Objects;

import static com.github.devmix.commons.javafx.api.toolkit.Attributes.I18N_FACTORY;
import static com.github.devmix.commons.javafx.api.toolkit.Attributes.UTILS_FACTORY;
import static com.github.devmix.commons.javafx.api.toolkit.Attributes.VIEW_FACTORY;

/**
 * @author Sergey Grachev
 */
public abstract class AbstractToolkitService<V extends View, U extends Utils, I extends I18n>
        implements ToolkitService<V, U, I> {

    protected Logger LOG = LoggerFactory.getLogger(getClass());

    protected ViewsFactory<V> viewsFactory;
    protected I18nFactory<I> i18nFactory;
    protected UtilsFactory<U> utilsFactory;

    private I i18n;
    private U utils;

    private final Object i18nLock = new Object();
    private final Object utilsLock = new Object();

    @SuppressWarnings("unchecked")
    @Override
    public ToolkitService set(final Attribute attribute, final Object value) {
        if (I18N_FACTORY.equals(attribute)) {
            if (!(value instanceof I18nFactory<?>)) {
                throw new IllegalArgumentException("Factory must implements " + I18nFactory.class + " interface. Current is " + value);
            }
            setI18nFactory((I18nFactory<I>) value);
        } else if (VIEW_FACTORY.equals(attribute)) {
            if (!(value instanceof ViewsFactory)) {
                throw new IllegalArgumentException("Factory must implements " + ViewsFactory.class + " interface. Current is " + value);
            }
            setViewsFactory((ViewsFactory<V>) value);
        } else if (UTILS_FACTORY.equals(attribute)) {
            if (!(value instanceof UtilsFactory<?>)) {
                throw new IllegalArgumentException("Factory must implements " + UtilsFactory.class + " interface. Current is " + value);
            }
            setUtilsFactory((UtilsFactory<U>) value);
        } else {
            LOG.warn("Unknown attribute {}", attribute);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Nullable
    @Override
    public <T> T get(final Attribute attribute) {
        if (I18N_FACTORY.equals(attribute)) {
            return (T) i18nFactory;
        } else if (VIEW_FACTORY.equals(attribute)) {
            return (T) viewsFactory;
        } else if (UTILS_FACTORY.equals(attribute)) {
            return (T) utilsFactory;
        } else {
            LOG.warn("Unknown attribute {}", attribute);
        }
        return null;
    }

    @Override
    public I i18n() {
        if (i18n == null) {
            synchronized (i18nLock) {
                if (i18n == null) {
                    i18n = Objects.requireNonNull(i18nFactory.create());
                }
            }
        }
        return i18n;
    }

    @Override
    public U utils() {
        if (utils == null) {
            synchronized (utilsLock) {
                if (utils == null) {
                    utils = Objects.requireNonNull(utilsFactory.createUtils(this));
                }
            }
        }
        return utils;
    }

    @Override
    public V newView() {
        return Objects.requireNonNull(viewsFactory.create(this));
    }

    public ToolkitService setViewsFactory(final ViewsFactory<V> factory) {
        this.viewsFactory = Objects.requireNonNull(factory);
        return this;
    }

    public ToolkitService setUtilsFactory(final UtilsFactory<U> factory) {
        this.utilsFactory = Objects.requireNonNull(factory);
        synchronized (utilsLock) {
            this.utils = null;
        }
        return this;
    }

    public ToolkitService setI18nFactory(final I18nFactory<I> factory) {
        this.i18nFactory = Objects.requireNonNull(factory);
        synchronized (i18nLock) {
            this.i18n = null;
        }
        return this;
    }

}
