/*
 * Commons Library
 * Copyright (c) 2013-2016 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

package com.github.devmix.commons.properties.storages;

import com.github.devmix.commons.properties.Property;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
public interface Storage {

    /**
     * By default, the minimum level is 0 or by value of @Level if it's defined on the property
     *
     * @throws com.github.devmix.commons.properties.exceptions.PropertyLevelViolationException
     * @see Storage#get(int, Property)
     */
    Property.Immutable get(Property property);

    /**
     * @param level minimal level of visibility of property
     * @throws com.github.devmix.commons.properties.exceptions.PropertyLevelViolationException
     */
    Property.Immutable get(int level, Property property);

    /**
     * @param level minimal level of visibility of property
     * @throws com.github.devmix.commons.properties.exceptions.PropertyLevelViolationException
     */
    Property.Values get(int level, Property... properties);

    void put(Property property, @Nullable Object value);

    void put(Property property, @Nullable Object value, int level);

    void putAll(Map<Property, Object> values, int level);

    interface Persistence {

        @Nullable
        Object get(int level, Property property);

        Map<Property, Object> get(int level, Set<Property> properties);

        void put(int level, @Nullable Object value);

        void put(int level, Map<Property, Object> values);
    }
}
