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
import com.github.devmix.commons.properties.TestPersistence;
import com.github.devmix.commons.properties.annotations.Group;
import com.github.devmix.commons.properties.annotations.Value;
import com.github.devmix.commons.properties.storages.annotations.Levels;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import static com.github.devmix.commons.properties.storages.StorageTest.TestProperties.LEVEL_0_1;
import static com.github.devmix.commons.properties.storages.StorageTest.TestProperties.LEVEL_1_1;
import static com.github.devmix.commons.properties.storages.StorageTest.TestProperties.LEVEL_1_2;
import static com.github.devmix.commons.properties.storages.StorageTest.TestProperties.LEVEL_1_3;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
public final class StorageTest extends AbstractPropertiesTest {

    @Test
    public void testInheritance() {
        testInheritance(LEVEL_0_1);
    }

    @Test
    public void testBatchGet() {
        testBatchGet(LEVEL_1_1, LEVEL_1_2, LEVEL_1_3);
    }

    @Test
    public void testBatchPut() {
        testBatchPut(LEVEL_1_1, LEVEL_1_2, LEVEL_1_3);
    }

    @Test
    public void testSubSet() {
        testSubSet(LEVEL_1_1, LEVEL_1_2, LEVEL_1_3);
    }

    @Test
    public void testDefaultValues() {
        testDefaultValues(LEVEL_1_1, LEVEL_1_2);
    }

    public static void testDefaultValues(final Property LEVEL_1_1, final Property LEVEL_1_2) {
        Storage storage = newStorage();

        assertThat(storage.get(LEVEL_1_1).get()).isEqualTo(null);
        assertThat(storage.get(LEVEL_1_2).get()).isEqualTo(0);

        storage = newStorage();
        assertThat(storage.get(1, new Property[]{LEVEL_1_1}).get(LEVEL_1_1).get()).isEqualTo(null);
        assertThat(storage.get(1, new Property[]{LEVEL_1_2}).get(LEVEL_1_2).get()).isEqualTo(0);
    }

    public static void testSubSet(final Property LEVEL_1_1, final Property LEVEL_1_2, final Property LEVEL_1_3) {
        final TestPersistence persistence = new TestPersistence() {
            @Override
            public Map<Property, Object> get(final int level, final Set<Property> properties) {
                super.get(level, properties);
                final Map<Property, Object> result = new HashMap<>(properties.size());
                for (final Property property : properties) {
                    result.put(property, 0);
                }
                return result;
            }
        };
        final Storage storage = newStorage();

        final Property.Values values =
                storage.get(1, LEVEL_1_1, LEVEL_1_2);

        // check subset of newValues
        assertThat(values.get(LEVEL_1_1).asInt()).isEqualTo(0);
        values.put(LEVEL_1_1, 1);
        assertThat(values.get(LEVEL_1_1).asInt()).isEqualTo(1);

        // check storage
        assertThat(storage.get(1, LEVEL_1_1).asInt()).isEqualTo(1);
        values.put(LEVEL_1_1, 2);
        assertThat(storage.get(3, LEVEL_1_1).asInt()).isEqualTo(2);

        final Map<Property, Object> data = new LinkedHashMap<>();
        data.put(LEVEL_1_1, 1);
        data.put(LEVEL_1_2, 2);
        data.put(LEVEL_1_3, 3);
        values.put(data);

        assertThat(values.get(LEVEL_1_1).asInt()).isEqualTo(1);
        assertThat(values.get(LEVEL_1_2).asInt()).isEqualTo(2);
        assertThat(values.get(LEVEL_1_3).asInt()).isEqualTo(0); // out of subset
    }

    public static void testBatchPut(final Property LEVEL_1_1, final Property LEVEL_1_2, final Property LEVEL_1_3) {
        final Storage storage = newStorage();
        final Map<Property, Object> data = new LinkedHashMap<>();
        data.put(LEVEL_1_1, 1);
        data.put(LEVEL_1_2, 2);
        data.put(LEVEL_1_3, 3);
        storage.putAll(data, 2);

        assertThat(storage.get(1, LEVEL_1_1).asInt()).isEqualTo(0);
        assertThat(storage.get(1, LEVEL_1_2).asInt()).isEqualTo(0);
        assertThat(storage.get(1, LEVEL_1_3).asInt()).isEqualTo(0);

        assertThat(storage.get(2, LEVEL_1_1).asInt()).isEqualTo(1);
        assertThat(storage.get(2, LEVEL_1_2).asInt()).isEqualTo(2);
        assertThat(storage.get(2, LEVEL_1_3).asInt()).isEqualTo(3);
    }

    public static void testInheritance(final Property LEVEL_0_1) {
        final Storage storage = newStorage();

        assertThat(storage.get(LEVEL_0_1).asInt()).isEqualTo(0); // level 0
        storage.put(LEVEL_0_1, 1); // level 0
        assertThat(storage.get(LEVEL_0_1).asInt()).isEqualTo(1); // level 0

        storage.put(LEVEL_0_1, 2, 1);
        assertThat(storage.get(1, LEVEL_0_1).asInt()).isEqualTo(2);
        assertThat(storage.get(2, LEVEL_0_1).asInt()).isEqualTo(2);
        assertThat(storage.get(3, LEVEL_0_1).asInt()).isEqualTo(2);
        assertThat(storage.get(4, LEVEL_0_1).asInt()).isEqualTo(2);

        storage.put(LEVEL_0_1, 3, 3);
        assertThat(storage.get(2, LEVEL_0_1).asInt()).isEqualTo(2);
        assertThat(storage.get(3, LEVEL_0_1).asInt()).isEqualTo(3);
        assertThat(storage.get(4, LEVEL_0_1).asInt()).isEqualTo(3);

        storage.put(LEVEL_0_1, null, 3);
        assertThat(storage.get(4, LEVEL_0_1).asInt()).isEqualTo(2);
    }

    public static void testBatchGet(final Property LEVEL_1_1, final Property LEVEL_1_2, final Property LEVEL_1_3) {
        final Storage storage = StorageBuilder.newMemory(new TestPersistence() {
            @Override
            public Map<Property, Object> get(final int level, final Set<Property> properties) {
                super.get(level, properties);
                final Map<Property, Object> result = new HashMap<>(properties.size());
                if (level == 2) {
                    for (final Property property : properties) {
                        if (LEVEL_1_1.equals(property)) {
                            result.put(property, 1);
                        } else if (LEVEL_1_2.equals(property)) {
                            result.put(property, 2);
                        } else if (LEVEL_1_3.equals(property)) {
                            result.put(property, 3);
                        }
                    }
                }
                return result;
            }
        });

        final Property.Values values = storage.get(2, LEVEL_1_1, LEVEL_1_2, LEVEL_1_3);

        assertThat(storage.get(1, LEVEL_1_1).asInt()).isEqualTo(0);
        assertThat(storage.get(1, LEVEL_1_2).asInt()).isEqualTo(0);
        assertThat(storage.get(1, LEVEL_1_3).asInt()).isEqualTo(0);

        assertThat(values.get(LEVEL_1_1).asInt()).isEqualTo(1);
        assertThat(values.get(LEVEL_1_2).asInt()).isEqualTo(2);
        assertThat(values.get(LEVEL_1_3).asInt()).isEqualTo(3);

        assertThat(storage.get(2, LEVEL_1_1).asInt()).isEqualTo(1);
        assertThat(storage.get(2, LEVEL_1_2).asInt()).isEqualTo(2);
        assertThat(storage.get(2, LEVEL_1_3).asInt()).isEqualTo(3);
    }

    @Test(enabled = false)
    public void testMT() throws InterruptedException {
        final TestPersistence persistence = new TestPersistence();
        final Storage storage = newStorage();
        final Map<Property, Object> data = new LinkedHashMap<>();
        data.put(LEVEL_1_1, 1);
        data.put(LEVEL_1_3, 3);

        final AtomicLong t = new AtomicLong(System.currentTimeMillis());
        final ExecutorService service = Executors.newFixedThreadPool(32);
        for (int i = 0; i < 32; i++) {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    while (!Thread.currentThread().isInterrupted()) {
                        storage.get(LEVEL_1_1).asInt();
                        storage.get(1, LEVEL_1_1, LEVEL_1_2, LEVEL_1_3)
                                .put(LEVEL_1_1, 2)
                                .put(data)
                                .asRaw();
                        storage.put(LEVEL_1_1, 4, 1);
                        storage.put(LEVEL_1_3, 4, 1);
                        storage.put(LEVEL_1_1, 5, 2);
                        storage.get(3, LEVEL_1_1);
                        storage.get(4, LEVEL_1_2);
                        Thread.yield();
                        if (System.currentTimeMillis() - t.get() > 1000) {
                            t.set(System.currentTimeMillis());
                            persistence.printCounters();
                            persistence.resetCounters();
                        }
                    }
                }
            });
        }
        service.awaitTermination(1, TimeUnit.DAYS);
    }

    @Group(value = "G1", subGroup = "sg1")
    enum TestProperties implements Property {

        @Levels @Value(type = Type.INT)
        LEVEL_0_1,

        @Levels(min = 1) @Value(type = Type.INT)
        LEVEL_1_1,
        @Levels(min = 1) @Value(type = Type.INT, value = "0")
        LEVEL_1_2,
        @Levels(min = 1) @Value(type = Type.INT)
        LEVEL_1_3
    }
}
