/*
 * Commons Library
 * Copyright (c) 2013-2016 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

package com.github.devmix.commons.properties;

import com.github.devmix.commons.properties.restrictions.Validator;
import com.github.devmix.commons.properties.restrictions.validators.Validators;
import com.github.devmix.commons.properties.storages.Storage;
import com.github.devmix.commons.properties.storages.StorageBuilder;

/**
 * @author Sergey Grachev
 */
public abstract class AbstractPropertiesTest {

    protected static Storage newStorage() {
        return StorageBuilder.newMemory(new TestPersistence());
    }

    protected Validator newValidator() {
        return Validators.newStandard();
    }
}
