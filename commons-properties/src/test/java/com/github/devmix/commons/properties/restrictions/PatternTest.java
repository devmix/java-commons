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

package com.github.devmix.commons.properties.restrictions;

import com.github.devmix.commons.properties.AbstractPropertiesTest;
import com.github.devmix.commons.properties.Property;
import com.github.devmix.commons.properties.annotations.Value;
import com.github.devmix.commons.properties.exceptions.PropertyValidationException;
import org.testng.annotations.Test;

import static com.github.devmix.commons.properties.restrictions.PatternTest.TestProperties.PATTERN_STRING;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.fail;

/**
 * @author Sergey Grachev
 */
public final class PatternTest extends AbstractPropertiesTest {

    @Test
    public void testPattern() {
        final Validator v = newValidator();

        v.validate(PATTERN_STRING, "1");
        v.validate(PATTERN_STRING, "10");

        try {
            v.validate(PATTERN_STRING, "100");
            fail();
        } catch (final PropertyValidationException e) {
            assertThat(e.getValue()).isEqualTo("100");
            assertThat(e.getRestriction()).isInstanceOf(Pattern.String.class);
        }
    }

    enum TestProperties implements Property {

        @Pattern.String("\\d{1,2}") @Value(value = "1")
        PATTERN_STRING;
    }
}
