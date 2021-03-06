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

package com.github.devmix.commons.properties;

import com.github.devmix.commons.properties.annotations.Group;
import com.github.devmix.commons.properties.annotations.Key;
import com.github.devmix.commons.properties.storages.annotations.Levels;
import org.joda.time.LocalTime;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.util.Map;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
public interface Property {

    interface Wrapper extends Property {

        Type type();

        @Nullable
        Key key();

        @Nullable
        Group group();

        @Nullable
        String value();

        @Nullable
        Levels levels();

        @Nullable
        Annotation[] restrictions();
    }

    interface Values {

        Set<Property> properties();

        /**
         * @return value of parameter,
         * {@link com.github.devmix.commons.properties.values.ValuesBuilder#nullValue() nullValue} or new parameter with
         * value by default ({@link com.github.devmix.commons.properties.values.ValuesBuilder#newValues(boolean) newValues})
         */
        Immutable get(Property property);

        Values put(Property property, @Nullable Object value);

        Values put(Map<Property, Object> values);

        Values put(Values values);

        Values clear(Property property);

        boolean has(Property property);

        Map<String, Object> asRaw();

        Map<String, Object> asRaw(Property... properties);
    }

    interface Immutable {

        Property property();

        boolean isNonNull();

        @Nullable
        Object get();

        boolean asBoolean();

        byte asByte();

        short asShort();

        int asInt();

        long asLong();

        float asFloat();

        double asDouble();

        char asChar();

        @Nullable
        String asString();

        @Nullable
        LocalTime asTime();

        <T extends Enum<T>> T asEnum(Class<T> clazz);
    }

    interface Mutable extends Immutable {

        Mutable set(@Nullable Object value);
    }

    interface Converter {

        @Nullable
        Object asOf(Type type, @Nullable Object value);

        boolean asBoolean(Type type, @Nullable Object value);

        byte asByte(Type type, @Nullable Object value);

        short asShort(Type type, @Nullable Object value);

        int asInt(Type type, @Nullable Object value);

        long asLong(Type type, @Nullable Object value);

        float asFloat(Type type, @Nullable Object value);

        double asDouble(Type type, @Nullable Object value);

        char asChar(Type type, @Nullable Object value);

        @Nullable
        String asString(Type type, @Nullable Object value);

        @Nullable
        LocalTime asTime(Type type, @Nullable Object value);

        @Nullable
        <T extends Enum<T>> T asEnum(Type type, @Nullable Object value, Class<T> clazz);
    }

    enum Type {
        BOOLEAN,
        BYTE,
        SHORT,
        INT,
        LONG,
        FLOAT,
        DOUBLE,
        CHAR,
        STRING,
        TIME,

        // virtual types

        NULL,
        // asEnum(STRING)
        ENUM
    }
}
