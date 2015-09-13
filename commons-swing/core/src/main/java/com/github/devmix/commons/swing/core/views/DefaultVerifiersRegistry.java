/*
 * Commons Library
 * Copyright (c) 2015 Sergey Grachev (sergey.grachev@yahoo.com). All rights reserved.
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

package com.github.devmix.commons.swing.core.views;

import com.github.devmix.commons.swing.api.decorators.ComponentDecorator;
import com.github.devmix.commons.swing.api.verifiers.ComponentDecoratorVerifier;
import com.github.devmix.commons.swing.api.verifiers.ComponentVerifier;
import com.github.devmix.commons.swing.api.verifiers.VerifierIterator;
import com.github.devmix.commons.swing.api.verifiers.VerifierStatus;
import com.github.devmix.commons.swing.api.views.VerifiersRegistry;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * @author Sergey Grachev
 */
public class DefaultVerifiersRegistry implements VerifiersRegistry {

    private final Map<ComponentDecorator<?>, ComponentDecoratorVerifier> verifiers = new WeakHashMap<>();

    @Override
    public ComponentDecoratorVerifier put(final ComponentDecorator<?> decorator, @Nullable final ComponentDecoratorVerifier verifier) {
        if (verifier == null) {
            return verifiers.remove(decorator);
        } else {
            return verifiers.put(decorator, verifier);
        }
    }

    @Override
    public ComponentDecoratorVerifier remove(final ComponentDecorator<?> decorator) {
        return verifiers.remove(decorator);
    }

    @Override
    public VerifierStatus verifyAll() {
        VerifierStatus status = VerifierStatus.VALID;
        for (final Map.Entry<ComponentDecorator<?>, ComponentDecoratorVerifier> entry : verifiers.entrySet()) {
            status = VerifierStatus.max(entry.getValue().verify(entry.getKey().$()), status);
        }
        return status;
    }

    @Override
    public VerifierStatus verify(final ComponentDecorator<?> decorator) {
        final ComponentVerifier verifier = verifiers.get(decorator);
        if (verifier != null) {
            return verifier.verify(decorator.$());
        }
        return VerifierStatus.VALID;
    }

    @Override
    public VerifierStatus iterate(final VerifierIterator iterator) {
        VerifierStatus status = VerifierStatus.VALID;
        for (final Map.Entry<ComponentDecorator<?>, ComponentDecoratorVerifier> entry : verifiers.entrySet()) {
            final ComponentDecorator<?> decorator = entry.getKey();
            status = VerifierStatus.max(iterator.verify(decorator, entry.getValue()), status);
        }
        return status;
    }
}
