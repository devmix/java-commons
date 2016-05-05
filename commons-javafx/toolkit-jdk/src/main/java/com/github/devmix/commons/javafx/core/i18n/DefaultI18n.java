/*
 * Commons Library
 * Copyright (c) 2015-2016 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

package com.github.devmix.commons.javafx.core.i18n;

import com.github.devmix.commons.i18n.api.Language;
import com.github.devmix.commons.i18n.api.LanguageOptions;
import com.github.devmix.commons.i18n.core.language.LanguageFactory;
import com.github.devmix.commons.i18n.datasource.ResourceBundleDataSource;

/**
 * @author Sergey Grachev
 */
public final class DefaultI18n extends AbstractI18n<Language> {

    public DefaultI18n() {
        super(getLanguage("i18n"));
    }

    public DefaultI18n(final String resourceBundleName) {
        super(getLanguage(resourceBundleName));
    }

    private static Language getLanguage(final String resourceBundleName) {
        return LanguageFactory.create(new ResourceBundleDataSource(resourceBundleName),
                LanguageOptions.create().enableFeature(LanguageOptions.Feature.RETURN_KEY_INSTEAD_EMPTY_VALUE));
    }
}
