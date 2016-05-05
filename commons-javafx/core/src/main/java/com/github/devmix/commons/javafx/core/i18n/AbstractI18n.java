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
import com.github.devmix.commons.javafx.api.i18n.I18n;

/**
 * @author Sergey Grachev
 */
public abstract class AbstractI18n<T extends Language> implements I18n<T> {

    protected final T language;

    public AbstractI18n(final T language) {
        this.language = language;
    }

    @Override
    public T translation() {
        return language;
    }
}
