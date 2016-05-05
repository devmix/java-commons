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

package com.github.devmix.commons.javafx.api;

import com.github.devmix.commons.javafx.api.i18n.I18n;
import com.github.devmix.commons.javafx.api.utils.Utils;
import com.github.devmix.commons.javafx.api.views.View;

import javax.annotation.Nullable;

/**
 * @author Sergey Grachev
 */
public interface ToolkitService<V extends View, U extends Utils, I extends I18n> {

    String id();

    I i18n();

    V newView();

    U utils();

    ToolkitService set(Attribute attribute, Object value);

    @Nullable
    <T> T get(Attribute attribute);

    interface Attribute {
    }
}
