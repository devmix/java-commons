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
import com.github.devmix.commons.adapters.api.annotations.Adaptee;
import com.github.devmix.commons.adapters.api.annotations.Adapter;
import com.github.devmix.commons.adapters.api.annotations.DelegateMethod;
import com.github.devmix.commons.adapters.api.annotations.DelegateMethods;
import com.github.devmix.commons.adapters.api.exceptions.AdapterGenerationException;
import com.github.devmix.commons.adapters.core.contexts.AdaptersContextBuilders;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
@SuppressWarnings("groupsTestNG")
@Test(groups = {"unit"})
public class GenericAdapterTest {

    public void test() throws AdapterGenerationException {
        final AdaptersContext ctx = AdaptersContextBuilders.standard()
                .addPackage(GenericAdapterTest.class.getPackage().getName())
                .build();

        final NumberAdapter<Long, Number> number = ctx.createByAdaptee(Number.class);

        assert number != null;

        assertThat(number).isNotNull();
        assertThat(number.set(10L).thenPlus(5L).thenMinus(2L).get()).isEqualTo(13L);

        final java.lang.Number asNumber = number.cast();

        assertThat(asNumber).isEqualTo(13L);
    }

    public static final class Number {

        private Long value;

        public void initialize(final Long value) {
            this.value = value;
        }

        public void doPlus(final Long value) {
            this.value += value;
        }

        public void doMinus(final Long value) {
            this.value -= value;
        }

        public Long getValue() {
            return value;
        }

        public <S> S getValueCast() {
            //noinspection unchecked
            return (S) value;
        }
    }

    public interface NumberAdapter<T, A> {

        @Adaptee
        A adaptee();

        NumberAdapter<T, A> thenPlus(T value);

        NumberAdapter<T, A> thenMinus(T value);

        NumberAdapter<T, A> set(T value);

        T get();

        <S> S cast();
    }

    @Adapter(processing = Adapter.Processing.AUTO)
    @DelegateMethods({
            @DelegateMethod(from = "then(.*)", to = "do(.*)"),
            @DelegateMethod(from = "get", to = "getValue"),
            @DelegateMethod(from = "set", to = "initialize"),
            @DelegateMethod(from = "cast", to = "getValueCast", returnValue = DelegateMethod.ReturnValue.RESULT)
    })
    public static abstract class NumberAdapterImpl implements NumberAdapter<Long, Number> {

        private final Number number = new Number();

        @Adaptee
        public Number adaptee() {
            return number;
        }
    }
}
