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
import com.github.devmix.commons.i18n.api.LanguageDataSource;
import com.github.devmix.commons.i18n.api.LanguageOptions;
import com.github.devmix.commons.i18n.core.utils.Constants;
import com.github.devmix.commons.i18n.core.values.CompositeValue;
import com.github.devmix.commons.i18n.core.values.KeyValues;
import com.github.devmix.commons.i18n.core.values.ReferenceValue;
import com.github.devmix.commons.i18n.core.values.Value;
import com.github.devmix.commons.i18n.core.values.string.StringParser;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;

import static com.github.devmix.commons.i18n.api.LanguageOptions.Feature;
import static com.github.devmix.commons.i18n.api.LanguageOptions.standard;
import static com.github.devmix.commons.i18n.core.values.reference.ArgumentsFactory.none;

/**
 * @author Sergey Grachev
 */
final class LanguageImpl implements Language {

    private static final Comparator<String> COMPARATOR = new Comparator<String>() {
        @Override
        public int compare(final String o1, final String o2) {
            return o1.compareTo(o2);
        }
    };

    private final Map<String, Value> entries = new ConcurrentHashMap<>();
    private final LanguageDataSource ds;
    private final LanguageOptions options;
    private Locale locale;

    public LanguageImpl(final LanguageDataSource ds, final LanguageOptions options) {
        this.ds = ds;
        this.options = options;
        switchLocale(options.locale());
    }

    public LanguageImpl(final LanguageDataSource ds) {
        this(ds, standard());
    }

    private Map<String, Value> optimize(final Map<String, Value> buffer) {
        final Map<String, Value> result = new HashMap<>();
        for (final Map.Entry<String, Value> entry : buffer.entrySet()) {
            final String key = entry.getKey();
            final Value value = entry.getValue();
            if (value instanceof ReferenceValue) {
                final ReferenceValue reference = (ReferenceValue) value;
                if (reference.isImmutable()) {
                    result.put(key, StringParser.tryParse(reference.evaluate(key, buffer, null, locale)));
                } else {
                    result.put(key, value);
                }
            } else {
                result.put(key, value);
            }
        }
        return result;
    }

    @Override
    public Set<String> keys() {
        return Collections.unmodifiableSet(entries.keySet());
    }

    @Override
    public void switchLocale(final Locale locale) {
        final Map<String, String> data = ds.load(locale);

        final Map<String, Value> buffer;
        if (!data.isEmpty()) {
            buffer = new TreeMap<>(COMPARATOR);
            for (final Map.Entry<String, String> entry : data.entrySet()) {
                buffer.put(entry.getKey(), KeyValues.parse(entry.getValue()));
            }
        } else {
            buffer = Collections.emptyMap();
        }

        synchronized (entries) {
            entries.clear();
            if (!buffer.isEmpty()) {
                entries.putAll(optimize(buffer));
            }
            this.locale = locale;
        }
    }

    @Nullable
    @Override
    public String of(final String key) {
        return of(key, null, Constants.EMPTY_ARRAY);
    }

    @Nullable
    @Override
    public String of(final String key, @Nullable final Value.Arguments params) {
        return of(key, params, Constants.EMPTY_ARRAY);
    }

    @Nullable
    @Override
    public String of(final String key, @Nullable final Object... args) {
        return of(key, null, args);
    }

    @Nullable
    @Override
    public String of(final String key, @Nullable final Value.Arguments params, @Nullable final Object... args) {
        final String result;
        final Value value = entries.get(key);
        if (value == null) {
            result = null;
        } else if (value instanceof ReferenceValue) {
            result = ((ReferenceValue) value).evaluate(key, entries, params, locale);
        } else if (value instanceof CompositeValue) {
            result = ((CompositeValue) value).evaluate(params, locale);
        } else {
            result = value.toString();
        }
        return result != null && args != null && args.length > 0
                ? String.format(result, args)
                : (result == null && options.hasFeature(Feature.RETURN_KEY_INSTEAD_EMPTY_VALUE) ? key : result);
    }

    @Override
    public List<String> listOf(final String prefix) {
        return listOf(prefix, none(), Constants.EMPTY_ARRAY);
    }

    @Override
    public List<String> listOf(final String prefix, @Nullable final Value.Arguments params) {
        return listOf(prefix, params, Constants.EMPTY_ARRAY);
    }

    @Override
    public List<String> listOf(final String prefix, @Nullable final Value.Arguments params, @Nullable final Object... args) {
        final String listName = Objects.requireNonNull(prefix) + '.';

        List<String> result = null;
        for (final String key : entries.keySet()) {
            if (!key.startsWith(listName)) {
                continue;
            }
            if (result == null) {
                result = new ArrayList<>(1);
            }
            result.add(of(key, params, args));
        }

        return result == null ? Collections.<String>emptyList() : result;
    }

    @Override
    public Map<String, String> mapOf(final String prefix) {
        return mapOf(prefix, none(), Constants.EMPTY_ARRAY);
    }

    @Override
    public Map<String, String> mapOf(final String prefix, @Nullable final Value.Arguments params) {
        return mapOf(prefix, params, Constants.EMPTY_ARRAY);
    }

    @Override
    public Map<String, String> mapOf(final String prefix, @Nullable final Value.Arguments params, @Nullable final Object... args) {
        final String listName = Objects.requireNonNull(prefix) + '.';
        final int listNameLength = listName.length();

        Map<String, String> result = null;
        for (final String key : entries.keySet()) {
            if (!key.startsWith(listName)) {
                continue;
            }
            if (result == null) {
                result = new HashMap<>(1);
            }
            result.put(key.substring(listNameLength), of(key, params, args));
        }

        return result == null ? Collections.<String, String>emptyMap() : result;
    }

    public Locale getLocale() {
        return locale;
    }
}
