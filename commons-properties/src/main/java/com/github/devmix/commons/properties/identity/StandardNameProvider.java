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

package com.github.devmix.commons.properties.identity;

import com.github.devmix.commons.properties.Property;

/**
 * @author Sergey Grachev
 */
public class StandardNameProvider implements Identity.NameProvider {

    @Override
    public String keyOf(final Property property) {
        final Class clazz = property.getClass();
        if (clazz.isEnum()) {
            return ((Enum) property).name();
        } else {
            return clazz.getName();
        }
    }
}
