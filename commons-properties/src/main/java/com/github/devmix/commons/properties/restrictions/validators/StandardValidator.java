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

package com.github.devmix.commons.properties.restrictions.validators;

import com.github.devmix.commons.properties.Property;
import com.github.devmix.commons.properties.exceptions.PropertyConversionException;
import com.github.devmix.commons.properties.exceptions.PropertyValidationException;
import com.github.devmix.commons.properties.restrictions.List;
import com.github.devmix.commons.properties.restrictions.Number;
import com.github.devmix.commons.properties.restrictions.Pattern;
import com.github.devmix.commons.properties.restrictions.Validator;
import org.apache.commons.lang3.StringUtils;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

import static com.github.devmix.commons.properties.Caches.patternOf;
import static com.github.devmix.commons.properties.Caches.restrictionsOf;
import static com.github.devmix.commons.properties.Caches.typeOf;
import static com.github.devmix.commons.properties.Property.Type;
import static com.github.devmix.commons.properties.converters.Converters.basic;

/**
 * @author Sergey Grachev
 */
final class StandardValidator implements Validator {

    @Override
    public void validate(final Property property, @Nullable final Object value) {
        final Annotation[] annotations = restrictionsOf(property);
        if (annotations.length == 0) {
            return;
        }

        for (final Annotation annotation : annotations) {
            final Type type = typeOf(property);
            final boolean valid;
            if (annotation instanceof Number.Min) {
                valid = validate(type, value, (Number.Min) annotation);
            } else if (annotation instanceof Number.Max) {
                valid = validate(type, value, (Number.Max) annotation);
            } else if (annotation instanceof Number.Range) {
                valid = validate(type, value, (Number.Range) annotation);
            } else if (annotation instanceof Pattern.String) {
                valid = validate(type, value, (Pattern.String) annotation);
            } else if (annotation instanceof List.String) {
                valid = validate(type, value, (List.String) annotation);
            } else if (annotation instanceof List.Number) {
                valid = validate(type, value, (List.Number) annotation);
            } else if (annotation instanceof List.Enum) {
                valid = validate(type, value, (List.Enum) annotation);
            } else {
                valid = true;
            }

            if (!valid) {
                throw new PropertyValidationException(type, value, annotation);
            }
        }
    }

    private static boolean validate(final Type type, @Nullable final Object value, final Number.Min restriction) {
        return basic().asDouble(type, value) >= restriction.value();
    }

    private static boolean validate(final Type type, @Nullable final Object value, final Number.Max restriction) {
        return basic().asDouble(type, value) <= restriction.value();
    }

    private static boolean validate(final Type type, @Nullable final Object value, final Number.Range restriction) {
        if (restriction.value().length == 0) {
            return true;
        }

        final double v = basic().asDouble(type, value);
        final double[] values = restriction.value();
        for (int min = 0, max = 1, length = values.length; min < length; min += 2, max += 2) {
            if (v >= values[min] && (max == length || v <= values[max])) {
                return true;
            }
        }

        return false;
    }

    private static boolean validate(final Type type, @Nullable final Object value, final Pattern.String restriction) {
        final String v = basic().asString(type, value);
        //noinspection ConstantConditions
        return StringUtils.isBlank(v) ?
                StringUtils.isBlank(restriction.value()) : patternOf(restriction).matcher(v).matches();
    }

    private static boolean validate(final Type type, @Nullable final Object value, final List.String restriction) {
        final String v = basic().asString(type, value);
        for (final String item : restriction.value()) {
            if (item.equals(v)) {
                return true;
            }
        }
        return false;
    }

    private boolean validate(final Type type, @Nullable final Object value, final List.Number restriction) {
        final double v = basic().asDouble(type, value);
        for (final double item : restriction.value()) {
            if (item == v) {
                return true;
            }
        }
        return false;
    }

    private boolean validate(final Type type, @Nullable final Object value, final List.Enum restriction) {
        try {
            basic().asEnum(type, value, restriction.value());
        } catch (final PropertyConversionException e) {
            return false;
        }
        return true;
    }
}
