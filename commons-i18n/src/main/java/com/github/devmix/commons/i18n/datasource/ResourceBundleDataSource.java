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

import com.github.devmix.commons.i18n.api.LanguageDataSource;

import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
public final class ResourceBundleDataSource implements LanguageDataSource {

    private final String baseName;

    public ResourceBundleDataSource(final String baseName) {
        this.baseName = baseName;
    }

    @Override
    public Map<String, String> load(final Locale locale) {
        final ResourceBundle bundle = ResourceBundle.getBundle(baseName, locale);

        final Set<String> keys = bundle.keySet();
        if (keys.isEmpty()) {
            return Collections.emptyMap();
        }

        final Map<String, String> result = new HashMap<>();
        for (final String key : bundle.keySet()) {
            result.put(key, bundle.getString(key));
        }

        return result;
    }
}
