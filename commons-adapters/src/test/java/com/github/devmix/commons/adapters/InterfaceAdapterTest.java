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

import com.github.devmix.commons.adapters.api.annotations.Adapter;
import com.github.devmix.commons.adapters.api.annotations.DelegateMethod;
import com.github.devmix.commons.adapters.api.exceptions.AdapterGenerationException;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
@SuppressWarnings("groupsTestNG")
@Test(groups = {"unit"})
public class InterfaceAdapterTest extends AbstractTest {

    public void test() throws AdapterGenerationException {
        final AdapterInterface bean = create(AdapterInterface.class);

        assertThat(bean.set(12).get()).isEqualTo(12);
    }

    static final class Foo {
        public Integer integer;

        public void setInteger(final Integer value) {
            this.integer = value;
        }

        public Integer getInteger() {
            return this.integer;
        }
    }

    @Adapter(adaptee = Foo.class)
    interface AdapterInterface {

        @DelegateMethod(from = "set", to = "setInteger")
        AdapterInterface set(Integer value);

        @DelegateMethod(from = "get", to = "getInteger")
        Integer get();
    }
}
