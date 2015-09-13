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

package com.github.devmix.commons.i18n.core.values.composite;

import com.github.devmix.commons.i18n.core.values.CompositeValue;
import com.github.devmix.commons.i18n.core.values.KeyValues;
import com.github.devmix.commons.i18n.core.values.Value;
import org.testng.annotations.Test;

import static com.github.devmix.commons.i18n.core.TestConstants.LOCALE;
import static com.github.devmix.commons.i18n.core.values.reference.ArgumentsFactory.capitalize;
import static com.github.devmix.commons.i18n.core.values.reference.ArgumentsFactory.create;
import static com.github.devmix.commons.i18n.core.values.reference.ArgumentsFactory.none;
import static com.github.devmix.commons.i18n.core.values.reference.ArgumentsFactory.numeric;
import static com.github.devmix.commons.i18n.core.values.reference.ArgumentsFactory.plural;
import static com.github.devmix.commons.i18n.core.values.reference.ArgumentsFactory.shortName;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
@Test
public final class CompositeValueTest {

    public static final String VALUE =
            "attribute${( plural : 's', numeric: ['','s','s2'], short:'a', cap: 'A' )}";

    public void parseTest() {
        final Value value = KeyValues.parse(VALUE);

        assertThat(value).isInstanceOf(CompositeValue.class);

        final CompositeValue composite = (CompositeValue) value;

        assertThat(composite.evaluate(none(), LOCALE)).isEqualTo("attribute");

        assertThat(composite.evaluate(capitalize(), LOCALE)).isEqualTo("Attribute");

        assertThat(composite.evaluate(shortName(), LOCALE)).isEqualTo("a");

        assertThat(composite.evaluate(plural(), LOCALE)).isEqualTo("attributes");

        assertThat(composite.evaluate(numeric(1), LOCALE)).isEqualTo("attribute");
        assertThat(composite.evaluate(numeric(2), LOCALE)).isEqualTo("attributes");
        assertThat(composite.evaluate(numeric(11), LOCALE)).isEqualTo("attributes2");

        assertThat(composite.evaluate(create(true, true, false, -1, true), LOCALE))
                .isEqualTo("a");

        assertThat(composite.evaluate(create(true, false, false, -1, true), LOCALE))
                .isEqualTo("Attributes");

        assertThat(composite.evaluate(create(true, false, true, 11, true), LOCALE))
                .isEqualTo("Attributes2");
    }
}
