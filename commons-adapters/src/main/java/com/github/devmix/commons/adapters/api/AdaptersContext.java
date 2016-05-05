/*
 * Commons Library
 * Copyright (c) 2016 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

import javax.annotation.Nullable;

/**
 * Context which contains all necessary information about available adapters and provides caching at runtime
 *
 * @author Sergey Grachev
 */
public interface AdaptersContext {

    /**
     * Find and create adapter with corresponding class
     *
     * @param adapterClass required adapter
     * @param <T>          cast to this type
     * @return return new instance of adapter or null if adapter not found
     */
    @Nullable
    <T> T findAndCreate(Class<? extends T> adapterClass);

    /**
     * Find and create adapter with corresponding class and constructor
     *
     * @param adapterClass    required adapter
     * @param constructorArgs arguments for constructor of adapter
     * @param <T>             cast to this type
     * @return return new instance of adapter or null if adapter not found
     */
    @Nullable
    <T> T findAndCreate(Class<? extends T> adapterClass, Object... constructorArgs);

    /**
     * Find and create adapter for specific adaptee
     *
     * @param adapteeClass required adaptee
     * @param <T>          cast to this type
     * @return return new instance of adapter or null if adapter not found
     */
    @Nullable
    <T> T findAndCreateByAdaptee(Class<?> adapteeClass);

    /**
     * Find and create adapter for specific adaptee with this arguments of constructor
     *
     * @param adapteeClass    required adaptee
     * @param constructorArgs arguments for constructor of adapter
     * @param <T>             cast to this type
     * @return return new instance of adapter or null if adapter not found
     */
    @Nullable
    <T> T findAndCreateByAdaptee(Class<?> adapteeClass, Object... constructorArgs);
}
