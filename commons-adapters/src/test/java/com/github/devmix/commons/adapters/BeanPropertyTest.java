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
import com.github.devmix.commons.adapters.api.annotations.BeanProperty;
import com.github.devmix.commons.adapters.api.exceptions.AdapterGenerationException;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
@SuppressWarnings("groupsTestNG")
@Test(groups = {"unit"})
public class BeanPropertyTest extends AbstractTest {

    public static final String STRING_VALUE = "hello";
    public static final int INT_VALUE = 12345;

    public void testSimple() throws AdapterGenerationException {
        final Simple bean = create(Simple.class);

        bean.setInteger(INT_VALUE);
        assertThat(bean.getInteger()).isEqualTo(INT_VALUE);

        bean.setString(STRING_VALUE);
        assertThat(bean.getString()).isEqualTo(STRING_VALUE);
    }

    public void testGeneric() throws AdapterGenerationException {
        @SuppressWarnings("unchecked")
        final Generic<Integer, String> bean = create(Generic.class);

        bean.setInteger(INT_VALUE);
        assertThat(bean.getInteger()).isEqualTo(INT_VALUE);

        bean.setString(STRING_VALUE);
        assertThat(bean.getString()).isEqualTo(STRING_VALUE);
    }

    public void testWithAdaptee() throws AdapterGenerationException {
        final WithAdaptee bean = create(WithAdaptee.class);

        bean.setInteger(INT_VALUE);
        assertThat(bean.getInteger()).isEqualTo(INT_VALUE);

        bean.setString(STRING_VALUE);
        assertThat(bean.getString()).isEqualTo(STRING_VALUE);
    }

    public void testAbstract() throws AdapterGenerationException {
        final Abstract bean = create(Abstract.class);

        bean.setInteger(INT_VALUE);
        assertThat(bean.getInteger()).isEqualTo(INT_VALUE);
    }

    public void testAbstractGeneric() throws AdapterGenerationException {
        @SuppressWarnings("unchecked")
        final AbstractGeneric<Integer> bean = create(AbstractGeneric.class);

        bean.setInteger(INT_VALUE);
        assertThat(bean.getInteger()).isEqualTo(INT_VALUE);
    }

    public void testAbstractInterface() throws AdapterGenerationException {
        final AbstractInterface bean = create(AbstractInterface.class);

        bean.setInteger(INT_VALUE);
        assertThat(bean.getInteger()).isEqualTo(INT_VALUE);
    }

    public void testAbstractGenericIncomplete() throws AdapterGenerationException {
        @SuppressWarnings("unchecked")
        final AbstractGenericIncompleteImpl<Integer> bean = create(AbstractGenericIncompleteImpl.class);

        bean.setInteger(INT_VALUE);
        assertThat(bean.getInteger()).isEqualTo(INT_VALUE);
    }

    public void testMethodAnnotation() throws AdapterGenerationException {
        @SuppressWarnings("unchecked")
        final MethodAnnotation bean = create(MethodAnnotation.class);

        bean.setInteger(INT_VALUE);
        assertThat(bean.getInteger()).isEqualTo(INT_VALUE);
    }

    public static final class Foo {
        public Integer integer;
        private String string;

        public String getString() {
            return string;
        }

        public void setString(final String string) {
            this.string = string;
        }
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Adapter(adaptee = Foo.class)
    @BeanProperty
    public interface WithAdaptee {
        Integer getInteger();

        void setInteger(Integer value);

        String getString();

        void setString(String value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Adapter
    @BeanProperty
    public interface Simple {
        Integer getInteger();

        void setInteger(Integer value);

        String getString();

        void setString(String value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Adapter
    @BeanProperty
    public interface Generic<I, S> {
        I getInteger();

        void setInteger(I value);

        S getString();

        void setString(S value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Adapter
    @BeanProperty
    public static abstract class Abstract {
        public abstract Integer getInteger();

        public abstract void setInteger(Integer value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Adapter
    @BeanProperty
    public static abstract class AbstractGeneric<I> {
        public abstract I getInteger();

        public abstract void setInteger(I value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    public interface AbstractInterface {
        Integer getInteger();

        void setInteger(Integer value);
    }

    @Adapter
    @BeanProperty
    public static abstract class AbstractWithInterface implements AbstractInterface {

    }

    // -----------------------------------------------------------------------------------------------------------------
    @Adapter
    @BeanProperty
    public static abstract class AbstractGenericIncompleteImpl<I> {
        protected I integer;

        public I getInteger() {
            return this.integer;
        }

        public abstract void setInteger(I value);
    }

    // -----------------------------------------------------------------------------------------------------------------
    @Adapter
    public interface MethodAnnotation {
        @BeanProperty
        Integer getInteger();

        @BeanProperty
        void setInteger(Integer value);
    }
}
