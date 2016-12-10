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

package com.github.devmix.commons.datastructures.constants;

/**
 * @author Sergey Grachev
 */
public final class Constants {

    private Constants() {
    }

    public static final class Objects {
        private static final Object OBJECT = new Object();

        public static Object object() {
            return OBJECT;
        }
    }

    public static final class Strings {
        private static final String EMPTY = "";

        public static String empty() {
            return EMPTY;
        }
    }

    public static final class Arrays {
        private static final byte[] EMPTY_BYTES = new byte[0];
        private static final Class[] EMPTY_CLASSES = new Class[0];
        private static final Object[] EMPTY_OBJECTS = new Object[0];

        public static byte[] emptyBytes() {
            return EMPTY_BYTES;
        }

        public static Class[] emptyClasses() {
            return EMPTY_CLASSES;
        }

        public static Object[] emptyObjects() {
            return EMPTY_OBJECTS;
        }
    }
}
