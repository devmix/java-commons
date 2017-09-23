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

package com.github.devmix.commons.properties.annotations;

import com.github.devmix.commons.properties.Property;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Sergey Grachev
 */
@Retention(RUNTIME)
@Target({FIELD})
@SuppressWarnings("ClassExplicitlyAnnotation")
public @interface Value {

    /**
     * Default value
     */
    String value() default "";

    Property.Type type() default Property.Type.STRING;

    boolean hidden() default false;

    boolean secure() default false;

    static final class Instance implements Value {
        private final Property.Type type;
        private final String value;
        private final boolean hidden;
        private final boolean secure;

        public Instance(final Property.Type type, final String value, final boolean hidden, final boolean secure) {
            this.type = type;
            this.value = value == null ? "" : value;
            this.hidden = hidden;
            this.secure = secure;
        }

        public Instance(final Property.Type type, final String value) {
            this(type, value, false, false);
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Value.class;
        }

        @Override
        public Property.Type type() {
            return type;
        }

        @Override
        public String value() {
            return value;
        }

        @Override
        public boolean hidden() {
            return hidden;
        }

        @Override
        public boolean secure() {
            return secure;
        }
    }
}
