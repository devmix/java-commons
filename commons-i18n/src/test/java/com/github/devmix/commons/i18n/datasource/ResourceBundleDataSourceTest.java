/*
 * Commons Library
 * Copyright (c) 2015 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

package com.github.devmix.commons.i18n.datasource;

import org.testng.annotations.Test;

import java.util.Locale;
import java.util.Map;

import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
@Test
public final class ResourceBundleDataSourceTest {

    public void test() {
        final Map<String, String> data = new ResourceBundleDataSource("test-lang").load(Locale.ENGLISH);

        assertThat(data.get("key-1")).isEqualTo("value-1");
        assertThat(data.get("key-2")).isEqualTo("value-2");
    }
}
