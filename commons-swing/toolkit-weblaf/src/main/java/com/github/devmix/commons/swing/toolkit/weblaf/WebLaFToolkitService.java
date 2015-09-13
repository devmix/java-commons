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

package com.github.devmix.commons.swing.toolkit.weblaf;

import com.github.devmix.commons.swing.api.ToolkitService;
import com.github.devmix.commons.swing.api.View;
import com.github.devmix.commons.swing.api.bindings.Binder;
import com.github.devmix.commons.swing.api.utils.Utils;
import com.github.devmix.commons.swing.core.bindings.DefaultBinder;
import com.github.devmix.commons.swing.core.views.AbstractToolkitService;
import com.github.devmix.commons.swing.toolkit.weblaf.utils.UtilsImpl;
import com.github.devmix.commons.swing.toolkit.weblaf.views.ViewWebLaF;

/**
 * @author Sergey Grachev
 */
public final class WebLaFToolkitService extends AbstractToolkitService implements ToolkitService {

    private static final Utils UTILS = new UtilsImpl();

    @Override
    public String id() {
        return "weblaf";
    }

    @Override
    public Binder createBinder() {
        return new DefaultBinder();
    }

    @Override
    public View createView() {
        return new ViewWebLaF();
    }

    @Override
    public Utils utils() {
        return UTILS;
    }
}
