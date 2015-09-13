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

package com.github.devmix.commons.swing.toolkit.weblaf.utils;

import com.github.devmix.commons.swing.api.utils.SwingUtils;
import com.github.devmix.commons.swing.api.utils.Utils;

/**
 * @author Sergey Grachev
 */
public final class UtilsImpl implements Utils {

    private static final SwingUtils USER_INTERFACE_UTILS = new SwingUtilsImpl();

    @Override
    public SwingUtils swing() {
        return USER_INTERFACE_UTILS;
    }
}
