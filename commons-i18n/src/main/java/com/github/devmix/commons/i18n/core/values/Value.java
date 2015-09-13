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

package com.github.devmix.commons.i18n.core.values;

/**
 * @author Sergey Grachev
 */
public interface Value {

    interface Arguments {

        Plural plural();

        ShortName shortName();

        Numeric numeric();

        Capitalize capitalize();

        interface Plural {
            boolean exists();
        }

        interface ShortName {
            boolean exists();
        }

        interface Numeric {
            boolean exists();

            int number();
        }

        interface Capitalize {
            boolean exists();
        }
    }

    interface Attributes {

        Plural plural();

        ShortName shortName();

        Numeric numeric();

        Capitalize capitalize();

        interface Plural {
            String text();

            boolean asPrefix();

            boolean asSuffix();
        }

        interface ShortName {
            String text();
        }

        interface Numeric {
            String[] forms();
        }

        interface Capitalize {
            String text();

            boolean toLower();

            int rangeStart();

            int rangeEnd();
        }
    }
}
