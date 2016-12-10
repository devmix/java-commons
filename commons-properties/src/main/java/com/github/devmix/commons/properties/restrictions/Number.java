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

package com.github.devmix.commons.properties.restrictions;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * @author Sergey Grachev
 */
@SuppressWarnings("ClassExplicitlyAnnotation")
public interface Number {

    @Retention(RUNTIME)
    @Target({TYPE, FIELD})
    @Restriction
    @interface Max {
        double value();

        static final class Instance implements Max {
            private final double value;

            public Instance(final double value) {
                this.value = value;
            }

            @Override
            public double value() {
                return value;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Max.class;
            }
        }
    }

    @Retention(RUNTIME)
    @Target({TYPE, FIELD})
    @Restriction
    @interface Min {
        double value();

        static final class Instance implements Min {
            private final double value;

            public Instance(final double value) {
                this.value = value;
            }

            @Override
            public double value() {
                return value;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Min.class;
            }
        }
    }

    /**
     * Examples:<br>
     * [min_1]<br>
     * [min_1, max_1, min_2]<br>
     * [min_1, max_1[,..., min_N, max_N]]
     */
    @Retention(RUNTIME)
    @Target({TYPE, FIELD})
    @Restriction
    @interface Range {
        double[] value();

        static final class Instance implements Range {
            private final double[] value;

            public Instance(final double... value) {
                this.value = value;
            }

            @Override
            public double[] value() {
                return value;
            }

            @Override
            public Class<? extends Annotation> annotationType() {
                return Range.class;
            }
        }
    }
}
