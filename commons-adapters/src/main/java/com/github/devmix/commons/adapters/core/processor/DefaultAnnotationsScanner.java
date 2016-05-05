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

package com.github.devmix.commons.adapters.core.processor;

import com.github.devmix.commons.adapters.api.annotations.Adaptee;
import com.github.devmix.commons.adapters.api.annotations.Adapter;
import com.github.devmix.commons.adapters.api.annotations.DelegateRule;
import com.github.devmix.commons.adapters.api.annotations.DelegateRules;
import com.github.devmix.commons.adapters.core.commons.AbstractCachedAnnotationsScanner;
import com.github.devmix.commons.adapters.core.utils.AdapterUtils;

import javax.annotation.Nullable;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
final class DefaultAnnotationsScanner extends AbstractCachedAnnotationsScanner<TypeElement, MirrorAdapter, ExecutableElement, ExecutableElement> {

    private final Elements elements;
    private final Types types;
    private final TypeElement elementDelegateRule;
    private final TypeElement elementDelegateRules;

    public DefaultAnnotationsScanner(final Elements elements, final Types types) {
        this.elements = elements;
        this.types = types;
        this.elementDelegateRule = elements.getTypeElement(DelegateRule.class.getName());
        this.elementDelegateRules = elements.getTypeElement(DelegateRules.class.getName());
    }

    @Nullable
    @Override
    protected MirrorAdapter loadAdapterFor(final TypeElement object) {
        final Adapter adapter = object.getAnnotation(Adapter.class);
        if (adapter == null) {
            return null;
        }

        TypeMirror adaptee = null;
        try {
            adapter.adaptee();
        } catch (final MirroredTypeException e) {
            adaptee = e.getTypeMirror();
        }

        assert adaptee != null;

        return new MirrorAdapter(adaptee, null, adapter.processing());
    }

    @Nullable
    @Override
    protected ExecutableElement loadAdapteeMethod(final TypeElement object) {
        ExecutableElement result = null;
        for (final Element element : elements.getAllMembers(object)) {
            if (element.getAnnotation(Adaptee.class) != null && !element.getModifiers().contains(Modifier.PRIVATE)) {
                result = (ExecutableElement) element;
            }
        }
        return result;
    }

    @Nullable
    @Override
    protected DelegateRule[] loadGlobalDelegateRules(final TypeElement object) {
        final Set<DelegateRule> result = findDelegateRules(object.getAnnotationMirrors(), new HashSet<DelegateRule>());
        return result.toArray(new DelegateRule[result.size()]);
    }

    @Nullable
    @Override
    protected DelegateRule[] loadMethodDelegateRules(final ExecutableElement method) {
        final Set<DelegateRule> result = findDelegateRules(method.getAnnotationMirrors(), new HashSet<DelegateRule>());
        return result.toArray(new DelegateRule[result.size()]);
    }

    private Set<DelegateRule> findDelegateRules(final List<? extends AnnotationMirror> annotations, final Set<DelegateRule> result) {
        for (final AnnotationMirror annotationMirror : annotations) {
            final DeclaredType annotationType = annotationMirror.getAnnotationType();
            if (types.isSameType(elementDelegateRule.asType(), annotationType)) {
                result.add(convertDelegateRule(AdapterUtils.getAnnotationValues(annotationMirror)));
            } else if (types.isSameType(elementDelegateRules.asType(), annotationType)) {
                result.addAll(convertDelegateRules(AdapterUtils.getAnnotationValues(annotationMirror)));
            } else {
                final Element element = annotationType.asElement();
                if (element.getAnnotation(DelegateRule.class) != null || element.getAnnotation(DelegateRules.class) != null) {
                    findDelegateRules(element.getAnnotationMirrors(), result);
                }
            }
        }
        return result;
    }

    private DelegateRule convertDelegateRule(final Map<? extends ExecutableElement, ? extends AnnotationValue> fields) {
        String from = null;
        String to = null;
        DelegateRule.ReturnValue returnValue = null;
        for (final Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : fields.entrySet()) {
            final String fieldName = entry.getKey().getSimpleName().toString();
            final String fieldValue = String.valueOf(entry.getValue().getValue());
            if ("from".equals(fieldName)) {
                from = fieldValue;
            } else if ("to".equals(fieldName)) {
                to = fieldValue;
            } else if ("returnValue".equals(fieldName)) {
                returnValue = DelegateRule.ReturnValue.valueOf(fieldValue);
            }
        }
        return new MirrorDelegateRule(from, to, returnValue);
    }

    private Set<DelegateRule> convertDelegateRules(final Map<? extends ExecutableElement, ? extends AnnotationValue> fields) {
        if (fields.isEmpty()) {
            return Collections.emptySet();
        }
        final Set<DelegateRule> result = new HashSet<>();
        for (final Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : fields.entrySet()) {
            final String fieldName = entry.getKey().getSimpleName().toString();
            final AnnotationValue fieldValue = entry.getValue();
            if ("value".equals(fieldName)) {
                @SuppressWarnings("unchecked")
                final Collection<AnnotationMirror> rules = (Collection<AnnotationMirror>) fieldValue.getValue();
                for (final AnnotationMirror annotationMirror : rules) {
                    result.add(convertDelegateRule(AdapterUtils.getAnnotationValues(annotationMirror)));
                }
            }
        }
        return result;
    }
}
