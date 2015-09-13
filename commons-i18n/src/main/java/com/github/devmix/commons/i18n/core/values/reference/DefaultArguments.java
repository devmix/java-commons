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

package com.github.devmix.commons.i18n.core.values.reference;

import com.github.devmix.commons.i18n.core.values.Value;

/**
 * @author Sergey Grachev
 */
final class DefaultArguments implements Value.Arguments {

    private final Plural plural;
    private final ShortName shortName;
    private final Numeric numeric;
    private final Capitalize capitalize;

    DefaultArguments(final Plural plural, final ShortName shortName, final Numeric numeric, final Capitalize capitalize) {
        this.plural = plural;
        this.shortName = shortName;
        this.numeric = numeric;
        this.capitalize = capitalize;
    }

    @Override
    public Value.Arguments.Plural plural() {
        return plural;
    }

    @Override
    public Value.Arguments.ShortName shortName() {
        return shortName;
    }

    @Override
    public Value.Arguments.Numeric numeric() {
        return numeric;
    }

    @Override
    public Capitalize capitalize() {
        return capitalize;
    }

    @Override
    public String toString() {
        return "params{" +
                "p:" + plural +
                ", s:" + shortName +
                ", n:" + numeric +
                ", c:" + capitalize +
                '}';
    }
}
