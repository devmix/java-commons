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

package com.github.devmix.commons.i18n.api;

import com.github.devmix.commons.i18n.core.values.Value;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
public interface Language {

    Set<String> keys();

    /**
     * @throws com.github.devmix.commons.i18n.core.exceptions.CircularReferenceException
     */
    void switchLocale(Locale locale);

    @Nullable
    String of(String key);

    @Nullable
    String of(String key, @Nullable Value.Arguments params);

    @Nullable
    String of(String key, @Nullable Object... args);

    @Nullable
    String of(String key, @Nullable Value.Arguments params, @Nullable Object... args);

    List<String> listOf(String prefix);

    List<String> listOf(String prefix, @Nullable Value.Arguments params);

    List<String> listOf(String prefix, @Nullable Value.Arguments params, @Nullable Object... args);

    Map<String, String> mapOf(String prefix);

    Map<String, String> mapOf(String prefix, @Nullable Value.Arguments params);

    Map<String, String> mapOf(String prefix, @Nullable Value.Arguments params, @Nullable Object... args);
}
