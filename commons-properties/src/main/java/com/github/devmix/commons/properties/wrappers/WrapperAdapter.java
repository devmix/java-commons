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

package com.github.devmix.commons.properties.wrappers;

import com.github.devmix.commons.properties.Caches;
import com.github.devmix.commons.properties.Property;
import com.github.devmix.commons.properties.annotations.Group;
import com.github.devmix.commons.properties.annotations.Key;
import com.github.devmix.commons.properties.storages.annotations.Levels;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

/**
 * @author Sergey Grachev
 */
public abstract class WrapperAdapter implements Property.Wrapper {

    @Override
    public Type type() {
        return Type.STRING;
    }

    @Nullable
    @Override
    public Key key() {
        return null;
    }

    @Nullable
    @Override
    public Group group() {
        return null;
    }

    @Nullable
    @Override
    public String nullAs() {
        return null;
    }

    @Nullable
    @Override
    public Levels levels() {
        return Caches.class.getAnnotation(Levels.class);
    }

    @Nullable
    @Override
    public Annotation[] restrictions() {
        return null;
    }
}
