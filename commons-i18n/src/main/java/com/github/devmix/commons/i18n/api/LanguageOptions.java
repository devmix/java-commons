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

import java.util.EnumMap;
import java.util.Locale;

/**
 * @author Sergey Grachev
 */
public final class LanguageOptions {

    private static final LanguageOptions STANDARD = new LanguageOptions();

    private final EnumMap<Feature, Object> features = new EnumMap<>(Feature.class);
    private Locale locale;

    private LanguageOptions() {
    }

    public static LanguageOptions create() {
        return new LanguageOptions();
    }

    public static LanguageOptions standard() {
        return STANDARD;
    }

    public LanguageOptions enableFeature(final Feature feature) {
        features.put(feature, null);
        return this;
    }

    public LanguageOptions disableFeature(final Feature feature) {
        features.remove(feature);
        return this;
    }

    public LanguageOptions locale(final Locale locale) {
        this.locale = locale;
        return this;
    }

    public Locale locale() {
        return locale == null ? Locale.getDefault() : locale;
    }

    public boolean hasFeature(final Feature feature) {
        return features.containsKey(feature);
    }

    public enum Feature {
        RETURN_KEY_INSTEAD_EMPTY_VALUE;
    }
}
