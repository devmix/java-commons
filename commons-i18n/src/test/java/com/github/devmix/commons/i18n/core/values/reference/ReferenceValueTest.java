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

package com.github.devmix.commons.i18n.core.values.reference;

import com.github.devmix.commons.i18n.core.TestConstants;
import com.github.devmix.commons.i18n.core.values.CompositeValue;
import com.github.devmix.commons.i18n.core.values.KeyValues;
import com.github.devmix.commons.i18n.core.values.ReferenceValue;
import com.github.devmix.commons.i18n.core.values.Value;
import org.testng.annotations.Test;

import java.util.Collections;
import java.util.Map;

import static com.github.devmix.commons.i18n.core.values.reference.ArgumentsFactory.none;
import static com.github.devmix.commons.i18n.core.values.reference.ArgumentsFactory.plural;
import static org.fest.assertions.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
@Test
public final class ReferenceValueTest {

    public static final String COMPOSITE_KEY = "group.attribute-1";
    public static final String COMPOSITE_VALUE =
            "attribute${( plural : 's', numeric: ['','s','s2'], short:'a', cap: 'A' )}";

    public static final String REFERENCE_KEY = "group.attribute-2";
    public static final String REFERENCE_VALUE_WITH_GLOBAL_PARAMS =
            "some ${../attribute-1()}";
    public static final String REFERENCE_VALUE_WITH_DEFAULT_PARAMS =
            "some ${../attribute-1(p:true)}";
    public static final String REFERENCE_VALUE_WITH_ABSOLUTE_PATH =
            "some ${/group/attribute-1()}";

    public void test() {
        final Map<String, Value> context = createContext();
        final ReferenceValue reference = createReferenceValue(REFERENCE_VALUE_WITH_GLOBAL_PARAMS);

        assertThat(reference.evaluate(REFERENCE_KEY, context, none(), TestConstants.LOCALE))
                .isEqualTo("some attribute");

        assertThat(reference.evaluate(REFERENCE_KEY, context, plural(), TestConstants.LOCALE))
                .isEqualTo("some attributes");
    }

    public void testWithDefaultParams() {
        final Map<String, Value> context = createContext();
        final ReferenceValue reference = createReferenceValue(REFERENCE_VALUE_WITH_DEFAULT_PARAMS);

        assertThat(reference.evaluate(REFERENCE_KEY, context, none(), TestConstants.LOCALE))
                .isEqualTo("some attributes");
    }

    public void testWithAbsolutePath() {
        final Map<String, Value> context = createContext();
        final ReferenceValue reference = createReferenceValue(REFERENCE_VALUE_WITH_ABSOLUTE_PATH);

        assertThat(reference.evaluate(REFERENCE_KEY, context, none(), TestConstants.LOCALE))
                .isEqualTo("some attribute");
    }

    private static ReferenceValue createReferenceValue(final String value) {
        final Value valueReference = KeyValues.parse(value);
        assertThat(valueReference).isInstanceOf(ReferenceValue.class);

        final ReferenceValue reference = (ReferenceValue) valueReference;

        assertThat(reference.isImmutable()).isFalse();
        return reference;
    }

    private static Map<String, Value> createContext() {
        final Value valueComposite = KeyValues.parse(COMPOSITE_VALUE);
        assertThat(valueComposite).isInstanceOf(CompositeValue.class);
        return Collections.singletonMap(COMPOSITE_KEY, valueComposite);
    }
}
