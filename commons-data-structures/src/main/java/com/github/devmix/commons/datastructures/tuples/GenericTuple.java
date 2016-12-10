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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Sergey Grachev
 */
final class GenericTuple implements Tuple, Serializable {

    private static final long serialVersionUID = 6534566067686827824L;

    private final List<? super Object> objects;

    GenericTuple(final Object... objects) {
        this.objects = new ArrayList<>();
        Collections.addAll(this.objects, objects);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T get(final int i) {
        return (T) objects.get(i);
    }

    @Override
    public List<?> list() {
        return Collections.unmodifiableList(objects);
    }

    @Override
    public Object[] array() {
        return objects.toArray();
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final GenericTuple that = (GenericTuple) o;
        return !(objects != null ? !objects.equals(that.objects) : that.objects != null);
    }

    @Override
    public int hashCode() {
        return objects != null ? objects.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "objects=" + objects +
                '}';
    }
}
