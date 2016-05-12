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

package com.github.devmix.commons.adapters;

import com.github.devmix.commons.adapters.api.AdaptersContext;
import com.github.devmix.commons.adapters.api.exceptions.AdapterGenerationException;
import com.github.devmix.commons.adapters.core.contexts.AdaptersContextBuilders;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
public abstract class AbstractTest {

    private static AdaptersContext CONTEXT;
    private static final Object LOCK = new Object();

    protected static AdaptersContext ctx() throws AdapterGenerationException {
        if (CONTEXT == null) {
            synchronized (LOCK) {
                if (CONTEXT == null) {
                    CONTEXT = AdaptersContextBuilders.standard()
                            .addPackage("com.github.devmix.commons.adapters")
                            .build();
                }
            }
        }
        return CONTEXT;
    }

    protected static <T> T create(final Class<? extends T> adapterClass) throws AdapterGenerationException {
        final T o = ctx().create(adapterClass);
        assertThat(o).isNotNull();
        return o;
    }
}
