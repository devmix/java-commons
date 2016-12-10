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
import com.github.devmix.commons.properties.annotations.Value;
import com.github.devmix.commons.properties.exceptions.PropertyValidationException;
import com.github.devmix.commons.properties.restrictions.Number;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.devmix.commons.properties.storages.StorageValidationTest.TestProperties.NUMBER_MIN_MAX;
import static org.testng.FileAssert.fail;

/**
 * @author Sergey Grachev
 */
public class StorageValidationTest extends AbstractPropertiesTest {

    @Test
    public void testValidation() {
        final Storage storage = newStorage();

        try {
            storage.put(NUMBER_MIN_MAX, 0);
            fail();
        } catch (final PropertyValidationException ignore) {
        }

        storage.put(NUMBER_MIN_MAX, 1);
        storage.put(NUMBER_MIN_MAX, 10);

        final Map<Property, Object> data = new LinkedHashMap<>();
        data.put(NUMBER_MIN_MAX, 1);
        data.put(NUMBER_MIN_MAX, 2);
        storage.putAll(data, 0);

        data.put(NUMBER_MIN_MAX, 11);

        try {
            storage.putAll(data, 0);
            fail();
        } catch (final PropertyValidationException ignore) {
        }
    }

    enum TestProperties implements Property {

        @Number.Min(1) @Number.Max(10) @Value(type = Type.INT, value = "1")
        NUMBER_MIN_MAX,

        @Number.Range({1, 5, 8, 10, 20}) @Value(type = Type.INT, value = "1")
        NUMBER_RANGE
    }
}
