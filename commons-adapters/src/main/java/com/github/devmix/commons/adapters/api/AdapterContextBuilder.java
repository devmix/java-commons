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

package com.github.devmix.commons.adapters.api;

import com.github.devmix.commons.adapters.api.annotations.Adapter;
import com.github.devmix.commons.adapters.api.exceptions.AdapterGenerationException;

/**
 * Interface for builder of adapters context.
 *
 * @author Sergey Grachev
 */
public interface AdapterContextBuilder {

    /**
     * Add package which can contains adapter classes
     *
     * @param packageName full name of package
     * @return instance of builder
     */
    AdapterContextBuilder addPackage(String packageName);

    /**
     * Forced type of processing for adapters. This parameter not overrides adapters which have attribute with value
     * {@link com.github.devmix.commons.adapters.api.annotations.Adapter.Processing#IGNORE}.
     *
     * @param processing type of processing which will be applied to all adapters
     * @return instance of builder
     */
    AdapterContextBuilder forceProcessing(Adapter.Processing processing);

    /**
     * Scan all packages for adapters and create context
     *
     * @return new context
     * @throws AdapterGenerationException
     */
    AdaptersContext build() throws AdapterGenerationException;
}
