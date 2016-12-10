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

import com.github.devmix.commons.properties.identity.Identity;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Sergey Grachev
 */
@Retention(RUNTIME)
@Target({ElementType.FIELD})
@SuppressWarnings("ClassExplicitlyAnnotation")
public @interface Key {

    String value() default "";

    Class<? extends Identity.NameProvider> provider() default Identity.NameProvider.class;

    static final class Instance implements Key {

        private final String name;
        private final Class<? extends Identity.NameProvider> provider;

        public Instance(final String name, final Class<? extends Identity.NameProvider> provider) {
            this.name = name;
            this.provider = provider;
        }

        public Instance(final String name) {
            this(name, Identity.NameProvider.class);
        }

        public Instance(final Class<? extends Identity.NameProvider> provider) {
            this(null, provider);
        }

        @Override
        public String value() {
            return name;
        }

        @Override
        public Class<? extends Identity.NameProvider> provider() {
            return provider;
        }

        @Override
        public Class<? extends Annotation> annotationType() {
            return Key.class;
        }
    }
}
