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

package com.github.devmix.commons.properties.exceptions;

/**
 * @author Sergey Grachev
 */
public abstract class PropertyException extends RuntimeException {
    private static final long serialVersionUID = 6146038411282723617L;

    public PropertyException() {
    }

    public PropertyException(final Throwable cause) {
        super(cause);
    }

    public PropertyException(final String message) {
        super(message);
    }
}
