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

import com.github.devmix.commons.properties.annotations.Group;
import org.testng.annotations.Test;

import static com.github.devmix.commons.properties.Caches.groupOf;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
public class GroupsTest {

    @Test
    public void testTestGroupInheritance() {
        assertThat(groupOf(TestGroupInheritance.prop1))
                .isEqualTo("level1.");

        assertThat(groupOf(TestGroupInheritance.level2.prop2))
                .isEqualTo("level1.level2.");

        assertThat(groupOf(TestGroupInheritance.level2.level3.prop3))
                .isEqualTo("level1.level2.level3.");

        assertThat(groupOf(TestGroupInheritance.level2.level3_1.prop3))
                .isEqualTo("level1.level2.level3_1.sub.");
    }

    @Group("level1")
    enum TestGroupInheritance implements Property {
        prop1;

        @Group
        enum level2 implements Property {
            prop2;

            @Group
            enum level3 implements Property {
                prop3;
            }

            @Group(subGroup = "sub")
            enum level3_1 implements Property {
                prop3;
            }
        }
    }
}
