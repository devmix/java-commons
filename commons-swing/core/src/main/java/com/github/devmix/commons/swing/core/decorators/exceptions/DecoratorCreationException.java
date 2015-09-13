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

package com.github.devmix.commons.swing.core.decorators.exceptions;

/**
 * @author Sergey Grachev
 */
public class DecoratorCreationException extends RuntimeException {

    private static final long serialVersionUID = -1745128983979950905L;

    private final Class<?> decoratorClass;

    public DecoratorCreationException(final Class<?> decoratorClass, final String message) {
        this(decoratorClass, message, null);
    }

    public DecoratorCreationException(final Class<?> decoratorClass, final String message, final Throwable cause) {
        super(message, cause);
        this.decoratorClass = decoratorClass;
    }

    public Class<?> getDecoratorClass() {
        return decoratorClass;
    }
}
