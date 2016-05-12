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

package com.github.devmix.commons.adapters.core.contexts;

import com.github.devmix.commons.adapters.api.AdaptersContext;
import com.github.devmix.commons.adapters.api.annotations.Adaptee;
import com.github.devmix.commons.adapters.api.annotations.Adapter;
import com.github.devmix.commons.adapters.api.annotations.DelegateMethod;
import com.github.devmix.commons.adapters.api.annotations.DelegateMethods;
import com.github.devmix.commons.adapters.api.exceptions.AdapterGenerationException;
import javafx.util.Callback;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * TODO check all cases
 *
 * @author Sergey Grachev
 */
@SuppressWarnings("groupsTestNG")
@Test(groups = {"unit"})
public class DefaultAdapterContextBuilderTest {

    @Test
    public void test() throws AdapterGenerationException {
        final AdaptersContext ctx = AdaptersContextBuilders.standard()
                .addPackage(DefaultAdapterContextBuilderTest.class.getPackage().getName())
                .build();

        assertFooAdapter(ctx.create(ConcreteAdapteeAdapterImpl.class, new Object()));
        assertFooAdapter(ctx.create(ConcreteAdapteeAdapter.class, new Object()));
        assertFooAdapter(ctx.<ConcreteAdapteeAdapter<?>>createByAdaptee(ConcreteAdaptee.class, new Object()));
    }

    private void assertFooAdapter(final ConcreteAdapteeAdapter<?> adapter) {
        assertThat(adapter).isNotNull();
        assertThat(adapter.adaptee().getIntField()).isEqualTo(0);
        assertThat(adapter.withIntField(1).adaptee().getIntField()).isEqualTo(1);
        assertThat(adapter.withFooField()).isInstanceOf(AdapterInt.class);
        assertThat(adapter.withFoo2Field()).isInstanceOf(ConcreteAdaptee.class);

        final ConcreteAdaptee foo = adapter.withStringAndInt("s1", 2).adaptee();
        assertThat(foo.getStringField()).isEqualTo("s1");
        assertThat(foo.getIntField()).isEqualTo(2);
    }

    public static final class ConcreteAdaptee<V> {

        private String stringField;
        private int intField;

        public String getStringField() {
            return stringField;
        }

        public void setStringField(final String stringField) {
            this.stringField = stringField;
        }

        public int getIntField() {
            return intField;
        }

        public void setIntField(final int intField) {
            this.intField = intField;
        }

        public ConcreteAdaptee setFooField() {
            return this;
        }

        public ConcreteAdaptee setFoo2Field() {
            return this;
        }

        public void setStringAndInt(final String stringField, final int intField) {
            this.stringField = stringField;
            this.intField = intField;
        }

        @SuppressWarnings("unchecked")
        public <E01,
                E02 extends E01,
                E03 extends Number,
                E04 extends Number & Serializable,
                E05 extends Class<Number>,
                E06 extends Class<List<Number>>,
                E07 extends Class<? extends Number>,
                E08 extends Class<? extends Number>,
                E09 extends Class<E01>,
                E10 extends Class<? extends E10>,
                E11 extends Class<? extends Class<? extends E11>>
                > Class<E11>
        setSomeNumber(final E01 A01, final E02 A02, final E03 A03, final E04 A04, final E05 A05, final E06 A06, final E07 A07, final E08 A08, final E09 A09, final E10 A10, final E11 A11,
                      final E01 A101,
                      final Number A102,
                      final Class<Number> A103,
                      final Class<? extends Number> A104,
                      final Class<E01> A105,
                      final Class<? extends E10> A106,
                      final Class<? extends Class<? extends E11>> A107,
                      final Class<? super E01> A108,
                      final E01[] A109,
                      final E01... A110) {
            return null;
        }

        public List<Set<?>> setList() {
            return null;
        }

        public void setReturnDynAdapter(final V v) {
        }

        public void setReturnDynAdapter2(final Callback<Map.Entry<V, V>, Boolean> arg0) {

        }

        public <T, D extends Map<T, D>> D setMap(final Class<D> m) {
            return null;
        }

        public void setOverride() {
        }

        public void setOverride(final Object o1) {
        }

        public void setOverride(final Object o1, final Object o2) {
        }
    }

    public abstract class BaseAdaptee {

        private int baseField;

        public int getBaseField() {
            return baseField;
        }

        public void setBaseField(final int baseField) {
            this.baseField = baseField;
        }
    }

    public interface Adapter3<A, V extends String & Serializable> {
        A withReturnDynAdapter(V v);

        A withReturnDynAdapter2(Callback<Map.Entry<V, V>, Boolean> arg0);
    }

    public interface Adapter2<A, V extends String & Serializable> extends Adapter3<A, V> {

    }

    public interface AdapterBase<T, T2 extends List<Set<?>>> {
        T adaptee();

        @SuppressWarnings("TypeParameterHidesVisibleType")
        <T, D extends Map<T, D>> D withMap(Class<D> decoratorClass);

        @SuppressWarnings({"unused", "unchecked"})
        <E01,
                E02 extends E01,
                E03 extends Number,
                E04 extends Number & Serializable,
                E05 extends Class<Number>,
                E06 extends Class<List<Number>>,
                E07 extends Class<? extends Number>,
                E08 extends Class<? extends Number>,
                E09 extends Class<E01>,
                E10 extends Class<? extends E10>,
                E11 extends Class<? extends Class<? extends E11>>
                > Class<E11>
        withSomeNumber(E01 A01, E02 A02, E03 A03, E04 A04, E05 A05, E06 A06, E07 A07, E08 A08, E09 A09, E10 A10, E11 A11,
                       E01 A101,
                       Number A102,
                       Class<Number> A103,
                       Class<? extends Number> A104,
                       Class<E01> A105,
                       Class<? extends E10> A106,
                       Class<? extends Class<? extends E11>> A107,
                       Class<? super E01> A108,
                       E01[] A109,
                       E01... A110);

        T2 withList();

        void withOverride();

        void withOverride(Object o1);

        void withOverride(Object o1, Object o2);
    }

    public interface AdapterInt<V extends String & Serializable> {

        @DelegateMethod(to = "set(.*)", from = "with2(.*)")
        ConcreteAdapteeAdapter<V> with2IntField(int value);

        ConcreteAdapteeAdapter<V> withIntField(int value);

        ConcreteAdapteeAdapter<V> withStringAndInt(String stringField, int intField);

        ConcreteAdapteeAdapter<V> withFooField();

        ConcreteAdaptee withFoo2Field();
    }

    public interface AdapterString {
        //        @RuleDelegateWithToSet
        @DelegateMethod(from = "with(.*)", to = "set(.*)")
        @DelegateMethods({
                @DelegateMethod(from = "with(.*)", to = "set(.*)"),
                @DelegateMethod(from = "with(.*)", to = "set(.*)")
        })
        ConcreteAdapteeAdapter withStringField(String value);
    }

    public interface ConcreteAdapteeAdapter<V extends String & Serializable> extends
            AdapterBase<ConcreteAdaptee<V>, List<Set<?>>>,
            AdapterInt<V>,
            AdapterString,
            Adapter2<ConcreteAdapteeAdapter<V>, V> {

    }

    //    @RuleDelegateWithToSet
    @Adapter(processing = Adapter.Processing.AUTO)
    @DelegateMethod(from = "with(.*)", to = "set(.*)")
    @DelegateMethods({
            @DelegateMethod(from = "with(.*)", to = "set(.*)"),
            @DelegateMethod(from = "with(.*)", to = "set(.*)")
    })
    public static abstract class ConcreteAdapteeAdapterImpl<V extends String & Serializable>
            extends AbstractFooAdapterImpl<V, ConcreteAdaptee<V>>
            implements ConcreteAdapteeAdapter<V> {

        public ConcreteAdapteeAdapterImpl(final Object o1) {
            super(o1);
        }

        public ConcreteAdapteeAdapterImpl(final Object o1, final Object o2, final Object o3) {
            super(o1, o2);
        }
    }

    public static abstract class AbstractFooAdapterImpl<X, Y> {

        @SuppressWarnings("unchecked")
        protected final Y adaptee = (Y) new ConcreteAdaptee<>();

        public AbstractFooAdapterImpl(final Object o1) {
        }

        public AbstractFooAdapterImpl(final Object o1, final Object o2) {
        }

        @Adaptee
        public Y adaptee() {
            return adaptee;
        }
    }
}
