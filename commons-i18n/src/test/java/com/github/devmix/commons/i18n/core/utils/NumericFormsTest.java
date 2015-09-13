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

package com.github.devmix.commons.i18n.core.utils;

import org.testng.annotations.Test;

import java.util.Locale;

import static com.github.devmix.commons.i18n.core.utils.NumericForms.formOf;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
@Test
public final class NumericFormsTest {

    public void test() {
        final Locale locale = new Locale("ru");
        assertThat(formOf(1, locale)).isEqualTo(0);
        assertThat(formOf(2, locale)).isEqualTo(1);
        assertThat(formOf(11, locale)).isEqualTo(2);
    }
}
