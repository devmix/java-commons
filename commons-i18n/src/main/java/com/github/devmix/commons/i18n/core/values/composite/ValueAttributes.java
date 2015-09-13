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

package com.github.devmix.commons.i18n.core.values.composite;

import com.github.devmix.commons.i18n.core.values.Value;

import java.util.Arrays;

/**
 * @author Sergey Grachev
 */
final class ValueAttributes implements Value.Attributes {

    private final Plural plural;
    private final Numeric numeric;
    private final ShortName shortName;
    private final Capitalize capitalize;

    private ValueAttributes(final Plural plural, final ShortName shortName, final Numeric numeric, final Capitalize capitalize) {
        this.plural = plural;
        this.shortName = shortName;
        this.numeric = numeric;
        this.capitalize = capitalize;
    }

    public static Plural plural(final String text, final boolean asPrefix, final boolean asSuffix) {
        return new PluralImpl(text, asPrefix, asSuffix);
    }

    public static Capitalize capitalize(final String text, final boolean toLower, final int rangeStart, final int rangeEnd) {
        return new CapitalizeImpl(text, toLower, rangeStart, rangeEnd);
    }

    public static Numeric numeric(final String[] forms) {
        return new NumericImpl(forms);
    }

    public static ShortName shortName(final String text) {
        return new ShortNameImpl(text);
    }

    public static Value.Attributes create(final Plural plural, final ShortName shortName,
                                          final Numeric numeric, final Capitalize capitalize) {
        return new ValueAttributes(plural, shortName, numeric, capitalize);
    }

    @Override
    public Value.Attributes.Plural plural() {
        return plural;
    }

    @Override
    public Value.Attributes.ShortName shortName() {
        return shortName;
    }

    @Override
    public Value.Attributes.Numeric numeric() {
        return numeric;
    }

    @Override
    public Capitalize capitalize() {
        return capitalize;
    }

    @Override
    public String toString() {
        return "attrs{" +
                "p:" + plural +
                ", n:" + numeric +
                ", s:" + shortName +
                ", c:" + capitalize +
                '}';
    }

    private static final class PluralImpl implements Value.Attributes.Plural {

        private final String text;
        private final boolean asPrefix;
        private final boolean asSuffix;

        public PluralImpl(final String text, final boolean asPrefix, final boolean asSuffix) {
            this.asPrefix = asPrefix;
            this.asSuffix = asSuffix;
            this.text = text;
        }

        @Override
        public String text() {
            return text;
        }

        @Override
        public boolean asPrefix() {
            return asPrefix;
        }

        @Override
        public boolean asSuffix() {
            return asSuffix;
        }

        @Override
        public String toString() {
            return "{" +
                    '\'' + text + '\'' +
                    ", asPrefix=" + asPrefix +
                    ", asSuffix=" + asSuffix +
                    '}';
        }
    }

    private static final class NumericImpl implements Value.Attributes.Numeric {

        private final String[] forms;

        public NumericImpl(final String[] forms) {
            this.forms = forms;
        }

        @Override
        public String[] forms() {
            return forms;
        }

        @Override
        public String toString() {
            return '{' + Arrays.toString(forms) + '}';
        }
    }

    private static final class ShortNameImpl implements Value.Attributes.ShortName {

        private final String text;

        public ShortNameImpl(final String text) {
            this.text = text;
        }

        @Override
        public String text() {
            return text;
        }

        @Override
        public String toString() {
            return '{' + text + '}';
        }
    }

    private static final class CapitalizeImpl implements Capitalize {

        private final String text;
        private final boolean toLower;
        private final int rangeStart;
        private final int rangeEnd;

        public CapitalizeImpl(final String text, final boolean toLower, final int rangeStart, final int rangeEnd) {
            this.text = text;
            this.toLower = toLower;
            this.rangeStart = rangeStart;
            this.rangeEnd = rangeEnd;
        }

        @Override
        public String text() {
            return text;
        }

        @Override
        public boolean toLower() {
            return toLower;
        }

        @Override
        public int rangeStart() {
            return rangeStart;
        }

        @Override
        public int rangeEnd() {
            return rangeEnd;
        }

        @Override
        public String toString() {
            return "{" +
                    '\'' + text + '\'' +
                    ", toLower=" + toLower +
                    ", rangeStart=" + rangeStart +
                    ", rangeEnd=" + rangeEnd +
                    '}';
        }
    }
}
