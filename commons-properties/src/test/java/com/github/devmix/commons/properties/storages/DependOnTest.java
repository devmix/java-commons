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

package com.github.devmix.commons.properties.storages;

import com.github.devmix.commons.properties.AbstractPropertiesTest;
import com.github.devmix.commons.properties.Property;
import com.github.devmix.commons.properties.annotations.DependOn;
import com.github.devmix.commons.properties.annotations.Value;
import org.testng.annotations.Test;

import static com.github.devmix.commons.properties.annotations.DependOn.Field;

/**
 * @author Sergey Grachev
 */
public final class DependOnTest extends AbstractPropertiesTest {

    @Test
    public void test() {
        final Storage storage = newStorage();
//        final Property[] dependencies = Caches.dependenciesOf(TestProperties.P2);
//        storage.getDependencies(TestProperties.P2);
    }

    private enum TestProperties implements Property {

        @Value(type = Type.BOOLEAN)
        @DependOn({@Field({"P2", "P3"}), @Field(of = TestProperties.class, value = {"P4", "P5"})})
        EXTERNAL_DEPENDENCY,

        @Value(type = Type.BOOLEAN)
        P1,

        @Value(type = Type.BOOLEAN)
        @DependOn(@Field("P3"))
        P2,

        @Value(type = Type.BOOLEAN)
        @DependOn(@Field("P2"))
        P3,

        @Value(type = Type.BOOLEAN)
        P4,

        @Value(type = Type.BOOLEAN)
        P5,

        @Value(type = Type.BOOLEAN)
        P6,
        //
        ;
    }
}
