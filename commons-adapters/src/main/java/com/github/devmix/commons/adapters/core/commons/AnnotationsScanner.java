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

package com.github.devmix.commons.adapters.core.commons;

import com.github.devmix.commons.adapters.api.annotations.BeanProperty;
import com.github.devmix.commons.adapters.api.annotations.DelegateMethod;

import javax.annotation.Nullable;

/**
 * Utility class for scanning metadata of adapters in provided types
 *
 * @param <C> type of adapter class
 * @param <A> type of adapter annotation
 * @param <S> type of adaptee method
 * @param <M> type of adapter method
 * @author Sergey Grachev
 */
public interface AnnotationsScanner<C, A, S, M> {

    /**
     * Search adapter annotation in the class and convert it into appropriate format
     *
     * @param object adapter for scanning
     * @return converted type or null if there no annotation
     */
    @Nullable
    A adapterFor(C object);

    /**
     * Method for search an non-private method which provides the adaptee instance for the adapter
     *
     * @param object adapter for scanning
     * @return adaptee provider or null
     */
    S adapteeFor(C object);

    /**
     * Method for search all delegation rules which will be applied to all methods of adapter
     *
     * @param object adapter for scanning
     * @return list of rules
     */
    DelegateMethod[] globalDelegateMethodsOf(C object);

    /**
     * Method for search all delegation rules which will be applied to current method of adapter
     *
     * @param method adapter for scanning
     * @return list of rules
     */
    DelegateMethod[] methodDelegateMethodsOf(M method);

    /**
     * Method for search all JavaBean rules which will be applied to all methods of adapter
     *
     * @param object adapter for scanning
     * @return list of rules
     */
    BeanProperty[] globalBeanPropertiesOf(C object);

    /**
     * Method for search all JavaBean rules which will be applied to current method of adapter
     *
     * @param method adapter for scanning
     * @return list of rules
     */
    BeanProperty[] methodBeanPropertiesOf(M method);
}
