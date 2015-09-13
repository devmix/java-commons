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

package com.github.devmix.commons.i18n.core.values.reference;

import javax.annotation.Nullable;

import static com.github.devmix.commons.i18n.core.values.Value.Arguments;

/**
 * @author Sergey Grachev
 */
public final class ArgumentsFactory {

    private static final BooleanParameter BOOLEAN_PARAMETER_FALSE = new BooleanParameter(false);
    private static final BooleanParameter BOOLEAN_PARAMETER_TRUE = new BooleanParameter(true);
    private static final IntegerParameter INTEGER_PARAMETER_0 = new IntegerParameter(0);
    private static final IntegerParameter INTEGER_PARAMETER_1 = new IntegerParameter(1);
    private static final IntegerParameter INTEGER_PARAMETER_2 = new IntegerParameter(2);
    private static final IntegerParameter INTEGER_PARAMETER_3 = new IntegerParameter(3);
    private static final IntegerParameter NUMERIC_NOT_EXISTS = new IntegerParameter();

    private static final DefaultArguments PLURAL = create(true, false, false, 0, false);
    private static final DefaultArguments SHORT_NAME = create(false, true, false, 0, false);
    private static final DefaultArguments CAPITALIZE = create(false, false, false, 0, true);
    private static final DefaultArguments INHERITED = create(false, false, false, 0, false);
    private static final DefaultArguments NONE = create(false, false, false, 0, false);

    private ArgumentsFactory() {
    }

    public static Arguments inherited() {
        return INHERITED;
    }

    public static Arguments none() {
        return NONE;
    }

    public static Arguments plural() {
        return PLURAL;
    }

    public static Arguments shortName() {
        return SHORT_NAME;
    }

    public static Arguments capitalize() {
        return CAPITALIZE;
    }

    public static Arguments numeric(final int form) {
        return create(false, false, true, form, false);
    }

    public static Arguments merge(@Nullable final Arguments params, @Nullable final Arguments defaultParams) {
        if (params == null || params == none()) {
            return defaultParams == none() ? null : defaultParams;
        }

        if (defaultParams == null || defaultParams == none()) {
            return params == none() ? null : params;
        }

        final boolean plural = params.plural().exists() || defaultParams.plural().exists();
        final boolean shortName = params.shortName().exists() || defaultParams.shortName().exists();
        final boolean numeric = params.numeric().exists() || defaultParams.numeric().exists();
        final int numericNumber = params.numeric().exists() ? params.numeric().number() : defaultParams.numeric().number();
        final boolean capitalize = params.shortName().exists() || defaultParams.capitalize().exists();

        return plural || shortName || numeric || capitalize
                ? create(plural, shortName, numeric, numericNumber, capitalize)
                : none();
    }

    public static DefaultArguments create(final boolean plural, final boolean shortName,
                                          final boolean numericExists, final int numeric, final boolean capitalize) {
        return new DefaultArguments(
                createBoolean(plural),
                createBoolean(shortName),
                numericExists ? createInteger(numeric) : NUMERIC_NOT_EXISTS,
                createBoolean(capitalize));
    }

    private static Arguments.Numeric createInteger(final int value) {
        if (0 == value) {
            return INTEGER_PARAMETER_0;
        } else if (1 == value) {
            return INTEGER_PARAMETER_1;
        } else if (2 == value) {
            return INTEGER_PARAMETER_2;
        } else if (3 == value) {
            return INTEGER_PARAMETER_3;
        }
        return new IntegerParameter(value);
    }

    private static BooleanParameter createBoolean(final boolean value) {
        return value ? BOOLEAN_PARAMETER_TRUE : BOOLEAN_PARAMETER_FALSE;
    }

    static class BooleanParameter implements Arguments.Plural, Arguments.ShortName, Arguments.Capitalize {

        private final boolean exists;

        public BooleanParameter(final boolean exists) {
            this.exists = exists;
        }

        @Override
        public boolean exists() {
            return exists;
        }

        @Override
        public String toString() {
            return String.valueOf(exists);
        }
    }

    private static final class IntegerParameter extends BooleanParameter implements Arguments.Numeric {

        private final int number;

        public IntegerParameter(final int number) {
            super(true);
            this.number = number;
        }

        public IntegerParameter() {
            super(false);
            this.number = -1;
        }

        @Override
        public int number() {
            return number;
        }

        @Override
        public String toString() {
            return String.valueOf(number);
        }
    }
}
