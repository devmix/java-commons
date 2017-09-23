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

import com.github.devmix.commons.properties.Caches;
import com.github.devmix.commons.properties.Property;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.github.devmix.commons.properties.Caches.idOf;
import static com.github.devmix.commons.properties.converters.Converters.basic;
import static com.github.devmix.commons.properties.values.ValuesBuilder.nullValue;

/**
 * @author Sergey Grachev
 */
final class DefaultValues implements Property.Values, Serializable {

    private static final long serialVersionUID = -8139458314878168072L;

    private final Map<String, Property.Mutable> storage = new HashMap<>();
    private final boolean autoCreate;

    public DefaultValues(final boolean autoCreate) {
        this.autoCreate = autoCreate;
    }

    @Override
    public Set<Property> properties() {
        synchronized (storage) {
            if (storage.isEmpty()) {
                return Collections.emptySet();
            }

            final Set<Property> result = new HashSet<>(storage.size());
            for (final Property.Mutable mutable : storage.values()) {
                result.add(mutable.property());
            }

            return result;
        }
    }

    @Override
    public Property.Immutable get(final Property property) {
        final String id = idOf(property);
        Property.Mutable value;
        synchronized (storage) {
            value = storage.get(id);
            if (value == null) {
                if (autoCreate) {
                    final Object defaultValue = Caches.valueOf(basic(), property);
                    value = ValuesBuilder.newMutable(property, defaultValue);
                    storage.put(id, value);
                } else {
                    return nullValue();
                }
            }
        }
        return value;
    }

    @Override
    public Property.Values put(final Property property, @Nullable final Object value) {
        if (value == null) {
            clear(property);
        } else {
            final String key = idOf(property);
            synchronized (storage) {
                final Property.Mutable exists = storage.get(key);
                if (exists != null) {
                    exists.set(value);
                } else {
                    storage.put(key, ValuesBuilder.newMutable(property, value));
                }
            }
        }
        return this;
    }

    @Override
    public Property.Values put(final Map<Property, Object> values) {
        if (!values.isEmpty()) {
            synchronized (storage) {
                for (final Map.Entry<Property, Object> entry : values.entrySet()) {
                    put(entry.getKey(), entry.getValue());
                }
            }
        }
        return this;
    }

    @Override
    public Property.Values put(final Property.Values values) {
        final Set<Property> properties = values.properties();
        if (!properties.isEmpty()) {
            synchronized (storage) {
                for (final Property property : properties) {
                    final Property.Immutable value = values.get(property);
                    if (value != nullValue()) {
                        put(property, value.get());
                    }
                }
            }
        }
        return this;
    }

    @Override
    public Property.Values clear(final Property property) {
        synchronized (storage) {
            storage.remove(idOf(property));
        }
        return this;
    }

    @Override
    public boolean has(final Property property) {
        synchronized (storage) {
            return storage.containsKey(idOf(property));
        }
    }

    @Override
    public Map<String, Object> asRaw() {
        synchronized (storage) {
            if (storage.isEmpty()) {
                return Collections.emptyMap();
            }

            final Map<String, Object> result = new HashMap<>(storage.size());
            for (final Map.Entry<String, Property.Mutable> entry : storage.entrySet()) {
                result.put(entry.getKey(), entry.getValue().get());
            }

            return result;
        }
    }

    @Override
    public Map<String, Object> asRaw(final Property... properties) {
        if (properties.length == 0) {
            return Collections.emptyMap();
        }

        synchronized (storage) {
            final Map<String, Object> result = new HashMap<>(properties.length);
            for (final Property property : properties) {
                result.put(idOf(property), get(property).get());
            }
            return result;
        }
    }
}
