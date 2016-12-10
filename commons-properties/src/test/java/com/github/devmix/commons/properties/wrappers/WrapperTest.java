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

import com.github.devmix.commons.properties.Property;
import com.github.devmix.commons.properties.annotations.Group;
import com.github.devmix.commons.properties.annotations.Key;
import com.github.devmix.commons.properties.restrictions.Number;
import com.github.devmix.commons.properties.restrictions.NumberTest;
import com.github.devmix.commons.properties.storages.StorageTest;
import com.github.devmix.commons.properties.storages.annotations.Levels;
import org.testng.annotations.Test;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;

/**
 * @author Sergey Grachev
 */
public final class WrapperTest {

    // STORAGE

    @Test
    public void testStorageInheritance() {
        StorageTest.testInheritance(
                new TestWrapper("p1", Property.Type.INT, null, new Levels.Instance(0, 0xFF)));
    }

    @Test
    public void testStorageBatchGet() {
        StorageTest.testBatchGet(
                new TestWrapper("LEVEL_1_1", Property.Type.INT, null, new Levels.Instance(1, 0xFF)),
                new TestWrapper("LEVEL_1_2", Property.Type.INT, null, new Levels.Instance(1, 0xFF)),
                new TestWrapper("LEVEL_1_3", Property.Type.INT, null, new Levels.Instance(1, 0xFF)));
    }

    @Test
    public void testStorageBatchPut() {
        StorageTest.testBatchPut(
                new TestWrapper("LEVEL_1_1", Property.Type.INT, null, new Levels.Instance(1, 0xFF)),
                new TestWrapper("LEVEL_1_2", Property.Type.INT, null, new Levels.Instance(1, 0xFF)),
                new TestWrapper("LEVEL_1_3", Property.Type.INT, null, new Levels.Instance(1, 0xFF)));
    }

    @Test
    public void testStorageSubSet() {
        StorageTest.testSubSet(
                new TestWrapper("LEVEL_1_1", Property.Type.INT, null, new Levels.Instance(1, 0xFF)),
                new TestWrapper("LEVEL_1_2", Property.Type.INT, null, new Levels.Instance(1, 0xFF)),
                new TestWrapper("LEVEL_1_3", Property.Type.INT, null, new Levels.Instance(1, 0xFF)));
    }

    @Test
    public void testStorageDefaultValues() {
        StorageTest.testDefaultValues(
                new TestWrapper("LEVEL_1_1", Property.Type.INT, null, new Levels.Instance(1, 0xFF)),
                new TestWrapper("LEVEL_1_2", Property.Type.INT, "0", new Levels.Instance(1, 0xFF)));
    }

    // VALIDATORS

    @Test
    public void testValidatorsTypesConversion() {
        NumberTest.testConversion(new TestWrapper("NUMBER_MIN_MAX", Property.Type.INT, "1", null,
                new Annotation[]{new Number.Min.Instance(1), new Number.Max.Instance(10)}));
    }

    private static class TestWrapper extends WrapperAdapter {

        private final String nullAs;
        private final Type type;
        private final Key key;
        private final Levels levels;
        private final Annotation[] restrictions;

        private TestWrapper(final String key, final Type type, @Nullable final String nullAs,
                            final Levels levels, final Annotation[] restrictions) {
            this.nullAs = nullAs;
            this.type = type;
            this.key = new Key.Instance(key);
            this.levels = levels;
            this.restrictions = restrictions;
        }

        private TestWrapper(final String key, final Type type, @Nullable final String nullAs,
                            final Levels levels) {
            this.nullAs = nullAs;
            this.type = type;
            this.key = new Key.Instance(key);
            this.levels = levels;
            this.restrictions = null;
        }

        private TestWrapper(final Key key, final Type type) {
            this.nullAs = null;
            this.type = type;
            this.key = key;
            this.levels = null;
            this.restrictions = null;
        }

        @Nullable
        @Override
        public Key key() {
            return key;
        }

        @Override
        public Group group() {
            return null;
        }

        @Override
        public Type type() {
            return type;
        }

        @Nullable
        @Override
        public String nullAs() {
            return nullAs;
        }

        @Nullable
        @Override
        public Levels levels() {
            return levels;
        }

        @Nullable
        @Override
        public Annotation[] restrictions() {
            return restrictions;
        }
    }
}
