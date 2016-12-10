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

import com.github.devmix.commons.properties.annotations.Key;

/**
 * @author Sergey Grachev
 */
public final class PropertyKeyException extends PropertyException {

    private static final long serialVersionUID = 8186062745404380089L;

    private final Key key;

    public PropertyKeyException(final Key key, final Exception e) {
        super(e);
        this.key = key;
    }

    public PropertyKeyException(final Key key, final String message) {
        super(message);
        this.key = key;
    }

    public Key getKey() {
        return key;
    }
}
