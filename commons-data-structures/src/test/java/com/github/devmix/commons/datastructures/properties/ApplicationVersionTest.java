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

package com.github.devmix.commons.datastructures.properties;

import com.github.devmix.commons.datastructures.ApplicationVersion;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
public final class ApplicationVersionTest {

    @Test
    public void testParse() {
        final ApplicationVersion version = ApplicationVersion.parse("1.2.3");
        assertThat(version.getMajor()).isEqualTo(1);
        assertThat(version.getMinor()).isEqualTo(2);
        assertThat(version.getBuild()).isEqualTo(3);
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void testParseIllegalArgumentException() {
        ApplicationVersion.parse(".2.3");
    }

    @Test(expectedExceptions = NumberFormatException.class)
    public void testParseNumberFormatException() {
        ApplicationVersion.parse("1.d.3");
    }

    @Test
    public void testGreater() {
        assertThat(new ApplicationVersion(3, 4, 5).isGreater(new ApplicationVersion(3, 4, 5))).isFalse();
        assertThat(new ApplicationVersion(3, 4, 5).isGreater(new ApplicationVersion(2, 4, 5))).isTrue();
        assertThat(new ApplicationVersion(3, 4, 5).isGreater(new ApplicationVersion(3, 3, 5))).isTrue();
        assertThat(new ApplicationVersion(3, 4, 5).isGreater(new ApplicationVersion(3, 4, 4))).isTrue();
    }

    @Test
    public void testLess() {
        assertThat(new ApplicationVersion(3, 4, 5).isLess(new ApplicationVersion(3, 4, 5))).isFalse();
        assertThat(new ApplicationVersion(2, 4, 5).isLess(new ApplicationVersion(3, 4, 5))).isTrue();
        assertThat(new ApplicationVersion(3, 3, 5).isLess(new ApplicationVersion(3, 4, 5))).isTrue();
        assertThat(new ApplicationVersion(3, 4, 4).isLess(new ApplicationVersion(3, 4, 5))).isTrue();
    }

    @Test
    public void testEquals() {
        assertThat(new ApplicationVersion(3, 4, 5).isEquals(new ApplicationVersion(3, 4, 5))).isTrue();
        assertThat(new ApplicationVersion(2, 4, 5).isEquals(new ApplicationVersion(3, 4, 5))).isFalse();
    }
}
