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

package com.github.devmix.commons.i18n.core.values.reference;

import com.github.devmix.commons.i18n.core.exceptions.CircularReferenceException;
import com.github.devmix.commons.i18n.core.values.CompositeValue;
import com.github.devmix.commons.i18n.core.values.ReferenceValue;
import com.github.devmix.commons.i18n.core.values.Value;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
final class DefaultReferenceValue implements ReferenceValue {

    private final Operation[] operations;
    private final boolean isImmutable;

    DefaultReferenceValue(final String text) {
        this.operations = ReferenceParser.parse(text);
        this.isImmutable = isAllOperationsImmutable();
    }

    @Override
    public boolean isImmutable() {
        return isImmutable;
    }

    @Nullable
    @Override
    public String evaluate(final String key, final Map<String, Value> context, final Arguments params, final Locale locale) {
        return evaluateRecursive(key, this, params, locale, context, new LinkedHashSet<String>());
    }

    private boolean isAllOperationsImmutable() {
        for (final Operation o : operations) {
            if (!o.isImmutable()) {
                return false;
            }
        }
        return true;
    }

    @Nullable
    private static String evaluateRecursive(final String key, final DefaultReferenceValue value, final Arguments params,
                                            final Locale locale, final Map<String, Value> context, final Set<String> dependencies) {
        if (value.operations.length == 0) {
            return null;
        }

        if (dependencies.contains(key)) {
            // circular dependency
            final StringBuilder sb = new StringBuilder();
            for (final String dependencyKey : dependencies) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(dependencyKey);
            }
            throw new CircularReferenceException(sb.toString());
        }

        dependencies.add(key);

        final StringBuilder sb = new StringBuilder();
        for (final Operation operation : value.operations) {
            final OperationCode code = operation.getCode();
            if (OperationCode.TEXT.equals(code)) {
                sb.append(operation.argumentsAsString());
            } else if (OperationCode.REFERENCE.equals(code)) {
                final Object[] args = operation.argumentsAsArray();
                final String relativeKey = (String) args[0];
                final String absoluteKey;
                if (relativeKey.startsWith("/")) {
                    absoluteKey = absoluteKey("", relativeKey);
                } else {
                    absoluteKey = absoluteKey(key, relativeKey);
                    args[0] = '/' + absoluteKey;
                }
                final Value nextValue = context.get(absoluteKey);
                if (nextValue != null) {
                    if (nextValue instanceof DefaultReferenceValue) {
                        sb.append(evaluateRecursive(absoluteKey, (DefaultReferenceValue) nextValue,
                                ArgumentsFactory.merge(params, (Arguments) args[1]), locale, context, dependencies));
                    } else if (nextValue instanceof CompositeValue) {
                        sb.append(((CompositeValue) nextValue)
                                .evaluate(ArgumentsFactory.merge(params, (Arguments) args[1]), locale));
                    } else {
                        sb.append(nextValue);
                    }
                }
            }
        }

        return sb.toString();
    }

    private static String absoluteKey(final String rootKey, final String relativeKey) {
        final StringBuilder path = new StringBuilder(relativeKey.startsWith("/") ? "" : rootKey);
        for (final String node : relativeKey.split("/")) {
            if ("..".equals(node)) {
                path.delete(path.lastIndexOf("."), path.length());
            } else if (!"".equals(node)) {
                if (path.length() > 0) {
                    path.append('.');
                }
                path.append(node);
            }
        }
        return path.toString();
    }

    @Override
    public String toString() {
        return "reference{" + Arrays.toString(operations) + '}';
    }
}
