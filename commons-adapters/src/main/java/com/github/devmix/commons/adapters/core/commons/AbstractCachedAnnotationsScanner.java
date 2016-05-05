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

import com.github.devmix.commons.adapters.api.annotations.DelegateRule;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract implementation of scanner which supports caching.
 *
 * @author Sergey Grachev
 * @see AnnotationsScanner
 */
public abstract class AbstractCachedAnnotationsScanner<C, A, S, M> implements AnnotationsScanner<C, A, S, M> {

    private static final DelegateRule[] EMPTY_RULES = new DelegateRule[0];

    private final Map<C, DelegateRule[]> delegateRules = new HashMap<>();
    private final Map<C, A> adapters = new HashMap<>();
    private final Map<C, S> adapteeMethods = new HashMap<>();

    @Nullable
    protected abstract A loadAdapterFor(C object);

    @Nullable
    protected abstract S loadAdapteeMethod(C object);

    @Nullable
    protected abstract DelegateRule[] loadGlobalDelegateRules(C object);

    @Nullable
    protected abstract DelegateRule[] loadMethodDelegateRules(M method);

    @Nullable
    @Override
    public A adapterFor(final C object) {
        final A cached = adapters.get(object);
        if (cached != null) {
            return cached;
        }
        adapters.put(object, loadAdapterFor(object));
        return adapters.get(object);
    }

    @Nullable
    @Override
    public S adapteeFor(final C object) {
        final S cached = adapteeMethods.get(object);
        if (cached != null) {
            return cached;
        }
        adapteeMethods.put(object, loadAdapteeMethod(object));
        return adapteeMethods.get(object);
    }

    @Override
    public DelegateRule[] globalDelegateRulesFor(final C object) {
        final DelegateRule[] cached = delegateRules.get(object);
        if (cached != null) {
            return cached;
        }
        final DelegateRule[] rules = loadGlobalDelegateRules(object);
        if (rules != null) {
            delegateRules.put(object, rules);
        } else {
            delegateRules.put(object, EMPTY_RULES);
        }
        return delegateRules.get(object);
    }

    @Override
    public DelegateRule[] methodDelegateRulesOf(final M method) {
        return loadMethodDelegateRules(method);
    }
}
