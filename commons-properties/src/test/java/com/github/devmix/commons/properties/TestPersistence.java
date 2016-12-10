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

package com.github.devmix.commons.properties;

import com.github.devmix.commons.properties.storages.Storage;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
public class TestPersistence implements Storage.Persistence {

    private long singleGet = 0;
    private long multiGet = 0;
    private long singlePut = 0;
    private long multiPut = 0;

    @Nullable
    @Override
    public Object get(final int level, final Property property) {
        singleGet++;
//            System.out.println("GET " + level + " " + property);
        return null;
    }

    @Override
    public Map<Property, Object> get(final int level, final Set<Property> properties) {
        multiGet++;
//            System.out.println("GET " + level + " " + properties);
        return Collections.emptyMap();
    }

    @Override
    public void put(final int level, @Nullable final Object value) {
        singlePut++;
//            System.out.println("PUT " + level + " " + value);
    }

    @Override
    public void put(final int level, final Map<Property, Object> values) {
        multiPut++;
//            System.out.println("PUT " + level + " " + values);
    }

    public void resetCounters() {
        synchronized (this) {
            singleGet = multiGet = singlePut = multiPut = 0;
        }
    }

    public void printCounters() {
        synchronized (this) {
            System.out.println(singleGet + "/" + multiGet + "/" + singlePut + "/" + multiPut);
        }
    }
}
