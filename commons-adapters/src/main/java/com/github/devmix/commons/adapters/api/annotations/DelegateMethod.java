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

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Rule which specify how delegate invocations of adapter methods to adaptee. If class annotated by this annotation
 * then rules will be applied to each method of class. If method annotated by this annotation then rule applied only for
 * this method and can override any of global rules.
 *
 * @author Sergey Grachev
 */
@Retention(RUNTIME)
@Target({METHOD, ANNOTATION_TYPE, TYPE})
public @interface DelegateMethod {

    /**
     * Pattern of adapter methods for matching to adaptee methods. Is can be exact method name or regular expression
     * with only one capturing group. Where capturing group will be contained part of name of adaptee method. Example,
     * for delegating to all of 'with' methods it can be - "with(.*)"
     */
    String from() default "";

    /**
     * Pattern of adaptee methods for matching to adapter methods. Is can be exact method name or regular expression
     * with only one capturing group. Where capturing group will be contained part of name of adapter method. Example,
     * for delegating to all of 'set' methods it can be - "set(.*)"
     */
    String to() default "";

    /**
     * Determines what kind of result must be returned from adapter method. By default used auto detection.
     *
     * @see ReturnValue
     */
    ReturnValue returnValue() default ReturnValue.AUTO;

    /**
     * Determines what kind of result must be returned from adapter method.
     */
    enum ReturnValue {
        /**
         * 1. No return value if return type of adapter method is void.<br/>
         * 2. Return result of invocation of adaptee method if return type of adapter method is accessible from return
         * type of adaptee method<br/>
         * 3. Return current instance of adapter if return type of adapter method is accessible from adapter type<br/>
         * 4. Return NULL otherwise
         */
        AUTO,

        /**
         * Return current adapter instance
         */
        THIS,

        /**
         * Return result of invocation of adaptee method
         */
        RESULT
    }
}
