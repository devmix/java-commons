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

/**
 * @author Sergey Grachev
 */
public final class LanguageFactory {

    private static final Language EMPTY = new EmptyLanguageImpl();

    private LanguageFactory() {
    }

    public static Language empty() {
        return EMPTY;
    }

    public static Language create(final LanguageDataSource ds, final LanguageOptions options) {
        return new LanguageImpl(ds, options);
    }

    public static Language create(final LanguageDataSource ds) {
        return new LanguageImpl(ds);
    }
}
