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

package com.github.devmix.commons.properties.identity;

import com.github.devmix.commons.properties.Property;
import com.github.devmix.commons.properties.annotations.Group;
import com.github.devmix.commons.properties.annotations.Key;
import com.github.devmix.commons.properties.wrappers.WrapperAdapter;
import org.testng.annotations.Test;

import javax.annotation.Nullable;

import static com.github.devmix.commons.properties.Caches.idOf;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
public final class IdentityTest {

    @Test
    public void testKeys() {
        assertThat(idOf(TestProperties.property_1))
                .isEqualTo("property_1"); // default

        assertThat(idOf(TestProperties.PROPERTY_2))
                .isEqualTo("prop_2"); // with defined name of key

        assertThat(idOf(TestProperties.PROPERTY_3))
                .isEqualTo("prv.PROPERTY_3"); // with id created by provider

        assertThat(idOf(TestProperties.property_4))
                .isEqualTo("property_4"); // with empty @Key annotation

        assertThat(idOf(TestWithGroupProperties.PROPERTY_2))
                .isEqualTo("grp.prop_2"); // with group
    }

    @Test
    public void testGroups() {
        assertThat(idOf(TestProperties.property_1))
                .isEqualTo("property_1");

        assertThat(idOf(TestWithGroupProperties.property_1))
                .isEqualTo("grp.property_1");

        assertThat(idOf(TestWithSubGroupProperties.property_1))
                .isEqualTo("grp.sub.property_1");

        assertThat(idOf(TestWithSeparatorProperties.property_1))
                .isEqualTo("grp-sub-property_1");
    }

    @Test
    public void testWrapperKeys() {
        assertThat(idOf(new TestWrapper()))
                .isEqualTo(TestWrapper.class.getName());

        assertThat(idOf(new TestWrapper(new Key.Instance("p1"))))
                .isEqualTo("p1");

        assertThat(idOf(new TestWrapper(new Key.Instance(TestKeyNameProvider.class))))
                .isEqualTo("prv.TestWrapper");
    }

    @Test
    public void testWrapperGroups() {
        assertThat(idOf(new TestWrapper(new Key.Instance("p1"), new Group.Instance("grp", null, null))))
                .isEqualTo("grp.p1");

        assertThat(idOf(new TestWrapper(new Key.Instance("p1"), new Group.Instance("grp", "sub", null))))
                .isEqualTo("grp.sub.p1");

        assertThat(idOf(new TestWrapper(new Key.Instance("p1"), new Group.Instance("grp", "sub", "-"))))
                .isEqualTo("grp-sub-p1");
    }

    enum TestProperties implements Property {

        property_1,

        @Key(value = "prop_2")
        PROPERTY_2,

        @Key(provider = TestKeyNameProvider.class)
        PROPERTY_3,

        @Key
        property_4,;
    }

    @Group(value = "grp")
    enum TestWithGroupProperties implements Property {

        property_1,

        @Key(value = "prop_2")
        PROPERTY_2,;
    }

    @Group(value = "grp", subGroup = "sub")
    enum TestWithSubGroupProperties implements Property {

        property_1;
    }

    @Group(value = "grp", subGroup = "sub", separator = "-")
    enum TestWithSeparatorProperties implements Property {

        property_1;
    }

    public static final class TestKeyNameProvider implements Identity.NameProvider {

        @Override
        public String keyOf(final Property property) {
            final Class clazz = property.getClass();
            if (clazz.isEnum()) {
                return "prv." + ((Enum) property).name();
            } else {
                return "prv." + clazz.getSimpleName();
            }
        }
    }

    private static class TestWrapper extends WrapperAdapter {

        private final Key key;
        private final Group group;

        private TestWrapper(final Key key, final Group group) {
            this.key = key;
            this.group = group;
        }

        private TestWrapper() {
            this(null, null);
        }

        private TestWrapper(final Key key) {
            this(key, null);
        }

        private TestWrapper(final Group group) {
            this(null, group);
        }

        @Nullable
        @Override
        public Key key() {
            return key;
        }

        @Nullable
        @Override
        public Group group() {
            return group;
        }
    }
}
