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

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Sergey Grachev
 */
@Retention(RUNTIME)
@Target({ElementType.TYPE})
@SuppressWarnings("ClassExplicitlyAnnotation")
public @interface Group {

    String value() default "";

    String subGroup() default "";

    String separator() default ".";

    static final class Instance implements Group {

        private final String group;
        private final String subGroup;
        private final String separator;

        public Instance(final String group, final String subGroup, final String separator) {
            this.group = group;
            this.subGroup = subGroup;
            this.separator = separator == null ? "." : separator;
        }

        @Override
        public String value() {
            return group;
        }

        @Override
        public String subGroup() {
            return subGroup;
        }

        @Override
        public String separator() {
            return separator;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Group.class;
        }
    }
}
