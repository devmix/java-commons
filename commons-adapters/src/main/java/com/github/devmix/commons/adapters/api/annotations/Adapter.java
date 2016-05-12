/*
 * Commons Library
 * Copyright (c) 2016 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

package com.github.devmix.commons.adapters.api.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Classes annotated by this annotation will be processed as adapters. Depending on value of {@link #processing()}
 * field it will be processed in runtime or at compile time
 *
 * @author Sergey Grachev
 */
@Retention(RUNTIME)
@Target(TYPE)
public @interface Adapter {

    /**
     * Exact type of adaptee. By default, this type determined as return type of method annotated by annotation
     * {@link Adaptee}.
     */
    Class<?> adaptee() default void.class;

    /**
     * Determines when adapter must be processed. By default is {@link Processing#AUTO}
     *
     * @see Processing
     */
    Processing processing() default Processing.AUTO;

    /**
     * Determines when adapter must be processed.
     */
    enum Processing {
        /**
         * Annotation processor creates adapter classes during compilation and then  in runtime  "AdapterContext"
         * creates additional adapter classes which not created during compilation time.
         */
        AUTO,

        /**
         * Adapter must be created only at runtime.
         */
        RUNTIME,

        /**
         * Adapter must be created only at compile time.
         */
        COMPILE,

        /**
         * Ignore this adapter.
         */
        IGNORE
    }
}
