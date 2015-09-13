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

import java.util.Arrays;

/**
 * @author Sergey Grachev
 */
final class Operation {

    private final OperationCode code;
    private final Object arguments;

    public Operation(final OperationCode code, final Object args) {
        this.code = code;
        this.arguments = args;
    }

    public OperationCode getCode() {
        return code;
    }

    public Object argumentsAsString() {
        return arguments;
    }

    public Object[] argumentsAsArray() {
        return (Object[]) arguments;
    }

    @Override
    public String toString() {
        return "operation{" +
                "code=" + code +
                ", args=" + (arguments instanceof Object[] ? Arrays.toString((Object[]) arguments) : arguments) +
                '}';
    }

    public boolean isImmutable() {
        if (OperationCode.REFERENCE == code) {
            final Object[] args = argumentsAsArray();
            return args.length > 1 && args[1] == ArgumentsFactory.none();
        }
        return true;
    }
}
