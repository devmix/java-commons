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

package com.github.devmix.commons.properties.values;

import com.github.devmix.commons.properties.Property;
import com.github.devmix.commons.properties.exceptions.PropertyNonWritableException;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
final class NullValues implements Property.Values {

    @Override
    public Set<Property> properties() {
        return Collections.emptySet();
    }

    @Nullable
    @Override
    public Property.Immutable get(final Property property) {
        return ValuesBuilder.nullValue();
    }

    @Override
    public Property.Values put(final Property property, @Nullable final Object value) {
        throw new PropertyNonWritableException();
    }

    @Override
    public Property.Values put(final Map<Property, Object> values) {
        throw new PropertyNonWritableException();
    }

    @Override
    public Property.Values put(Property.Values values) {
        throw new PropertyNonWritableException();
    }

    @Override
    public Property.Values clear(final Property property) {
        throw new PropertyNonWritableException();
    }

    @Override
    public boolean has(final Property property) {
        return false;
    }

    @Override
    public Map<String, Object> asRaw() {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, Object> asRaw(final Property... properties) {
        return Collections.emptyMap();
    }
}
