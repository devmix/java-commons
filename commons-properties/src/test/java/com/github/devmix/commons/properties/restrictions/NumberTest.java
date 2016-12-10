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
import com.github.devmix.commons.properties.values.ValuesBuilder;
import org.testng.annotations.Test;

import static com.github.devmix.commons.properties.restrictions.NumberTest.TestProperties.NUMBER_MIN_MAX;
import static com.github.devmix.commons.properties.restrictions.NumberTest.TestProperties.NUMBER_RANGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.testng.Assert.fail;

/**
 * @author Sergey Grachev
 */
public final class NumberTest extends AbstractPropertiesTest {

    @Test
    public void testTypesConversion() {
        testConversion(NUMBER_MIN_MAX);
//        System.out.println(NUMBER_MIN_MAX.key());
    }

    public static void testConversion(final Property NUMBER_MIN_MAX) {
        final Property.Mutable mutable = ValuesBuilder.newMutable(NUMBER_MIN_MAX, "1");

        try {
            ValuesBuilder.newMutable(NUMBER_MIN_MAX, null);
            fail();
        } catch (final PropertyValidationException e) {
            assertThat(e.getValue()).isEqualTo(null);
            assertThat(e.getRestriction()).isInstanceOf(Number.Min.class);
            assertThat(((Number.Min) e.getRestriction()).value()).isEqualTo(1);
        }

        try {
            mutable.set("0");
            fail();
        } catch (final PropertyValidationException e) {
            assertThat(e.getValue()).isEqualTo(0);
            assertThat(e.getRestriction()).isInstanceOf(Number.Min.class);
            assertThat(((Number.Min) e.getRestriction()).value()).isEqualTo(1);
        }

        mutable.set("1");

        try {
            mutable.set("11");
            fail();
        } catch (final PropertyValidationException e) {
            assertThat(e.getValue()).isEqualTo(11);
            assertThat(e.getRestriction()).isInstanceOf(Number.Max.class);
            assertThat(((Number.Max) e.getRestriction()).value()).isEqualTo(10);
        }
    }

    @Test
    public void testNumberMinMax() {
        final Validator v = newValidator();

        try {
            v.validate(NUMBER_MIN_MAX, null);
            fail();
        } catch (final PropertyValidationException e) {
            assertThat(e.getValue()).isEqualTo(null);
            assertThat(e.getRestriction()).isInstanceOf(Number.Min.class);
            assertThat(((Number.Min) e.getRestriction()).value()).isEqualTo(1);
        }

        try {
            v.validate(NUMBER_MIN_MAX, 0);
            fail();
        } catch (final PropertyValidationException e) {
            assertThat(e.getValue()).isEqualTo(0);
            assertThat(e.getRestriction()).isInstanceOf(Number.Min.class);
            assertThat(((Number.Min) e.getRestriction()).value()).isEqualTo(1);
        }

        v.validate(NUMBER_MIN_MAX, 1);
        v.validate(NUMBER_MIN_MAX, 2);
        v.validate(NUMBER_MIN_MAX, 10);

        try {
            v.validate(NUMBER_MIN_MAX, 11);
            fail();
        } catch (final PropertyValidationException e) {
            assertThat(e.getValue()).isEqualTo(11);
            assertThat(e.getRestriction()).isInstanceOf(Number.Max.class);
            assertThat(((Number.Max) e.getRestriction()).value()).isEqualTo(10);
        }
    }

    @Test
    public void testRange() {
        final Validator v = newValidator();

        v.validate(NUMBER_RANGE, 1);
        v.validate(NUMBER_RANGE, 5);

        try {
            v.validate(NUMBER_RANGE, 6);
            fail();
        } catch (final PropertyValidationException e) {
            assertThat(e.getValue()).isEqualTo(6);
            assertThat(e.getRestriction()).isInstanceOf(Number.Range.class);
        }

        v.validate(NUMBER_RANGE, 8);
        v.validate(NUMBER_RANGE, 9);
        v.validate(NUMBER_RANGE, 10);

        try {
            v.validate(NUMBER_RANGE, 11);
            fail();
        } catch (final PropertyValidationException e) {
            assertThat(e.getValue()).isEqualTo(11);
            assertThat(e.getRestriction()).isInstanceOf(Number.Range.class);
        }

        v.validate(NUMBER_RANGE, 20);
        v.validate(NUMBER_RANGE, 21);
        v.validate(NUMBER_RANGE, 22);
    }

    enum TestProperties implements Property {

        @Number.Min(1) @Number.Max(10) @Value(type = Type.INT, value = "1")
        NUMBER_MIN_MAX,

        @Number.Range({1, 5, 8, 10, 20}) @Value(type = Type.INT, value = "1")
        NUMBER_RANGE;
    }
}
