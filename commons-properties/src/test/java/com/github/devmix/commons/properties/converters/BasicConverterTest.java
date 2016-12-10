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

package com.github.devmix.commons.properties.converters;

import com.github.devmix.commons.properties.Property;
import org.joda.time.LocalTime;
import org.testng.annotations.Test;

import static com.github.devmix.commons.properties.Property.Type.STRING;
import static com.github.devmix.commons.properties.Property.Type.TIME;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
public final class BasicConverterTest {

    @Test
    public void testTime() {
        final Property.Converter converter = Converters.basic();
        assertThat(converter.asString(TIME, new LocalTime(10, 10, 10, 10))).isEqualTo("10:10:10:010");
        assertThat(converter.asString(TIME, new LocalTime(10, 10, 10))).isEqualTo("10:10:10");
        assertThat(converter.asString(TIME, new LocalTime(10, 10))).isEqualTo("10:10");

        assertThat(converter.asTime(STRING, "10:10:10:010")).isEqualTo(new LocalTime(10, 10, 10, 10));
        assertThat(converter.asTime(STRING, "10:10:10")).isEqualTo(new LocalTime(10, 10, 10));
        assertThat(converter.asTime(STRING, "10:10")).isEqualTo(new LocalTime(10, 10));
    }
}
