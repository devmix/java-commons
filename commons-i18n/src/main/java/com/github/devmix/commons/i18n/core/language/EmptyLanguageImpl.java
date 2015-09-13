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

package com.github.devmix.commons.i18n.core.language;

import com.github.devmix.commons.i18n.api.Language;
import com.github.devmix.commons.i18n.core.values.Value;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
final class EmptyLanguageImpl implements Language {

    @Override
    public Set<String> keys() {
        return Collections.emptySet();
    }

    @Override
    public void switchLocale(final Locale locale) {
    }

    @Nullable
    @Override
    public String of(final String key) {
        return null;
    }

    @Nullable
    @Override
    public String of(final String key, @Nullable final Value.Arguments params) {
        return null;
    }

    @Nullable
    @Override
    public String of(final String key, @Nullable final Object... args) {
        return null;
    }

    @Nullable
    @Override
    public String of(final String key, @Nullable final Value.Arguments params, @Nullable final Object... args) {
        return null;
    }

    @Override
    public List<String> listOf(final String prefix) {
        return Collections.emptyList();
    }

    @Override
    public List<String> listOf(final String prefix, @Nullable final Value.Arguments params) {
        return Collections.emptyList();
    }

    @Override
    public List<String> listOf(final String prefix, @Nullable final Value.Arguments params, @Nullable final Object... args) {
        return Collections.emptyList();
    }

    @Override
    public Map<String, String> mapOf(final String prefix) {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> mapOf(final String prefix, @Nullable final Value.Arguments params) {
        return Collections.emptyMap();
    }

    @Override
    public Map<String, String> mapOf(final String prefix, @Nullable final Value.Arguments params, @Nullable final Object... args) {
        return Collections.emptyMap();
    }
}
