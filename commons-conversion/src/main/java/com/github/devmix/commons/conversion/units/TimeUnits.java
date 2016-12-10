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

package com.github.devmix.commons.conversion.units;

import com.github.devmix.commons.datastructures.tuples.Pair;
import com.github.devmix.commons.datastructures.tuples.Tuples;

/**
 * @author Sergey Grachev
 */
public enum TimeUnits {

    NANOSECOND(0.0000000001),
    MICROSECOND(0.0000001),
    MILLISECOND(0.001),
    // BASE
    SECOND(1),
    MINUTE(60),
    HOUR(3600),
    DAY(86400),
    WEEK(604800),
    MONTH(2629743.8),
    YEAR(31556926);

    public static final TimeUnits BASE = SECOND;

    private final double value;

    TimeUnits(final double value) {
        this.value = value;
    }

    public double convert(final double value, final TimeUnits to) {
        return value * this.value / to.value;
    }

    public Pair<Double, TimeUnits> reduce(final double value, final TimeUnits... allowed) {
        if (value < this.value) {
            return Tuples.newPair(value, this);
        }

        TimeUnits to = this;
        final double baseValue = value * this.value / BASE.value;
        for (final TimeUnits unit : allowed.length == 0 ? values() : allowed) {
            if (baseValue >= unit.value && to.value < unit.value) {
                to = unit;
            }
        }

        return Tuples.newPair(baseValue / to.value, to);
    }
}
