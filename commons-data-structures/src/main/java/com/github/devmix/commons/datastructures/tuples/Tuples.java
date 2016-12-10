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

package com.github.devmix.commons.datastructures.tuples;

/**
 * @author Sergey Grachev
 */
public final class Tuples {

    private static final Pair EMPTY_PAIR = new Pair() {
        @Override
        public Object first() {
            return null;
        }

        @Override
        public Object second() {
            return null;
        }
    };

    private Tuples() {
    }

    @SuppressWarnings("unchecked")
    public static <F, S> Pair<F, S> emptyPair() {
        return EMPTY_PAIR;
    }

    public static Tuple newTuple(final Object... objects) {
        return new GenericTuple(objects);
    }

    public static <F, S> Pair<F, S> newPair(final F first, final S second) {
        return new GenericPair<>(first, second);
    }

    public static <F, S, T> Triple<F, S, T> newTriple(final F first, final S second, final T third) {
        return new GenericTriple<>(first, second, third);
    }
}
