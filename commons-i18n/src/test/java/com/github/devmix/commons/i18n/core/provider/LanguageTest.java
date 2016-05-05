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

package com.github.devmix.commons.i18n.core.provider;

import com.github.devmix.commons.i18n.api.Language;
import com.github.devmix.commons.i18n.api.LanguageOptions;
import com.github.devmix.commons.i18n.core.language.LanguageFactory;
import com.github.devmix.commons.i18n.datasource.ResourceBundleDataSource;
import org.testng.annotations.Test;

import java.util.Map;

import static com.github.devmix.commons.i18n.core.TestConstants.LOCALE;
import static com.github.devmix.commons.i18n.core.values.reference.ArgumentsFactory.none;
import static com.github.devmix.commons.i18n.core.values.reference.ArgumentsFactory.numeric;
import static com.github.devmix.commons.i18n.core.values.reference.ArgumentsFactory.plural;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
@Test
public final class LanguageTest {

    public void testSimple() {
        final Language language = LanguageFactory
                .create(new ResourceBundleDataSource("test-lang"), LanguageOptions.create().locale(LOCALE));

        assertThat(language.keys()).contains(
                "simple.attribute-1",
                "simple.attribute-2",
                "simple.attribute-3",
                "simple.attribute-2");

        assertThat(language.of("simple.attribute-99"))
                .isNull();

        assertThat(language.of("simple.attribute-1"))
                .isEqualTo("value");

        assertThat(language.of("simple.attribute-1", plural()))
                .isEqualTo("values");

        assertThat(language.of("simple.attribute-2", none()))
                .isEqualTo("%s value");

        assertThat(language.of("simple.attribute-2", plural()))
                .isEqualTo("%s value");

        assertThat(language.of("simple.attribute-3", plural()))
                .isEqualTo("%s values");

        assertThat(language.of("simple.attribute-3", numeric(2)))
                .isEqualTo("%s values");

        assertThat(language.of("simple.attribute-3", numeric(11)))
                .isEqualTo("%s values2");

        assertThat(language.of("simple.attribute-4", none()))
                .isEqualTo("%s values");
    }

    public void testList() {
        final Language language = LanguageFactory
                .create(new ResourceBundleDataSource("test-lang"), LanguageOptions.create().locale(LOCALE));

        assertThat(language.keys()).contains(
                "list.item-1",
                "list.item-2",
                "list.item-3",
                "list.item-2");

        assertThat(language.listOf("list")).contains(
                "%s value",
                "%s value",
                "%s values");

        assertThat(language.listOf("list", plural())).contains(
                "%s value",
                "%s values",
                "%s values");

        assertThat(language.listOf("list", numeric(11), 11)).contains(
                "11 value",
                "11 values2",
                "11 values2");
    }

    public void testMap() {
        final Language language = LanguageFactory
                .create(new ResourceBundleDataSource("test-lang"), LanguageOptions.create().locale(LOCALE));

        assertThat(language.keys()).contains(
                "map.key-1",
                "map.key-2",
                "map.key-3");

        Map<String, String> map = language.mapOf("map");
        assertThat(map.get("key-1")).isEqualTo("%s value");
        assertThat(map.get("key-2")).isEqualTo("%s value");
        assertThat(map.get("key-3")).isEqualTo("%s values");

        map = language.mapOf("map", plural());
        assertThat(map.get("key-1")).isEqualTo("%s value");
        assertThat(map.get("key-2")).isEqualTo("%s values");
        assertThat(map.get("key-3")).isEqualTo("%s values");

        map = language.mapOf("map", numeric(11), 11);
        assertThat(map.get("key-1")).isEqualTo("11 value");
        assertThat(map.get("key-2")).isEqualTo("11 values2");
        assertThat(map.get("key-3")).isEqualTo("11 values2");
    }
}
