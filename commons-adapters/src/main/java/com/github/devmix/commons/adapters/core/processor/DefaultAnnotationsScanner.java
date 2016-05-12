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
import com.github.devmix.commons.adapters.api.annotations.BeanProperty;
import com.github.devmix.commons.adapters.api.annotations.DelegateMethod;
import com.github.devmix.commons.adapters.api.annotations.DelegateMethods;
import com.github.devmix.commons.adapters.core.commons.AbstractCachedAnnotationsScanner;
import com.github.devmix.commons.adapters.core.utils.ProcessorUtils;

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
    private final TypeElement elementDelegateMethod;
    private final TypeElement elementDelegateMethods;
    private final TypeElement elementBeanProperty;

    public DefaultAnnotationsScanner(final Elements elements, final Types types) {
        this.elements = elements;
        this.types = types;
        this.elementDelegateMethod = elements.getTypeElement(DelegateMethod.class.getName());
        this.elementDelegateMethods = elements.getTypeElement(DelegateMethods.class.getName());
        this.elementBeanProperty = elements.getTypeElement(BeanProperty.class.getName());
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

        return new MirrorAdapter(adaptee, adapter.processing());
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
    protected DelegateMethod[] loadGlobalDelegateMethods(final TypeElement object) {
        final Set<DelegateMethod> result = findDelegateMethods(object.getAnnotationMirrors(), new HashSet<DelegateMethod>());
        return result.toArray(new DelegateMethod[result.size()]);
    }

    @Nullable
    @Override
    protected DelegateMethod[] loadMethodDelegateMethods(final ExecutableElement method) {
        final Set<DelegateMethod> result = findDelegateMethods(method.getAnnotationMirrors(), new HashSet<DelegateMethod>());
        return result.toArray(new DelegateMethod[result.size()]);
    }

    @Nullable
    @Override
    protected BeanProperty[] loadGlobalBeanProperties(final TypeElement object) {
        final Set<BeanProperty> result = findBeanProperties(object.getAnnotationMirrors(), new HashSet<BeanProperty>());
        return result.toArray(new BeanProperty[result.size()]);
    }

    @Nullable
    @Override
    protected BeanProperty[] loadMethodBeanProperties(final ExecutableElement method) {
        final Set<BeanProperty> result = findBeanProperties(method.getAnnotationMirrors(), new HashSet<BeanProperty>());
        return result.toArray(new BeanProperty[result.size()]);
    }

    private Set<BeanProperty> findBeanProperties(final List<? extends AnnotationMirror> annotations, final Set<BeanProperty> result) {
        for (final AnnotationMirror annotationMirror : annotations) {
            final DeclaredType annotationType = annotationMirror.getAnnotationType();
            if (types.isSameType(elementBeanProperty.asType(), annotationType)) {
                result.add(convertBeanProperty(ProcessorUtils.getAnnotationValues(annotationMirror)));
            } else {
                final Element element = annotationType.asElement();
                if (element.getAnnotation(BeanProperty.class) != null) {
                    findBeanProperties(element.getAnnotationMirrors(), result);
                }
            }
        }
        return result;
    }

    private BeanProperty convertBeanProperty(final Map<? extends ExecutableElement, ? extends AnnotationValue> fields) {
        boolean skip = false;
        for (final Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : fields.entrySet()) {
            final String fieldName = entry.getKey().getSimpleName().toString();
            final String fieldValue = String.valueOf(entry.getValue().getValue());
            if ("skip".equals(fieldName)) {
                skip = Boolean.parseBoolean(fieldValue);
            }
        }
        return new MirrorBeanProperty(skip);
    }

    private Set<DelegateMethod> findDelegateMethods(final List<? extends AnnotationMirror> annotations, final Set<DelegateMethod> result) {
        for (final AnnotationMirror annotationMirror : annotations) {
            final DeclaredType annotationType = annotationMirror.getAnnotationType();
            if (types.isSameType(elementDelegateMethod.asType(), annotationType)) {
                result.add(convertDelegateMethod(ProcessorUtils.getAnnotationValues(annotationMirror)));
            } else if (types.isSameType(elementDelegateMethods.asType(), annotationType)) {
                result.addAll(convertDelegateMethods(ProcessorUtils.getAnnotationValues(annotationMirror)));
            } else {
                final Element element = annotationType.asElement();
                if (element.getAnnotation(DelegateMethod.class) != null || element.getAnnotation(DelegateMethods.class) != null) {
                    findDelegateMethods(element.getAnnotationMirrors(), result);
                }
            }
        }
        return result;
    }

    private DelegateMethod convertDelegateMethod(final Map<? extends ExecutableElement, ? extends AnnotationValue> fields) {
        String from = null;
        String to = null;
        DelegateMethod.ReturnValue returnValue = null;
        for (final Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : fields.entrySet()) {
            final String fieldName = entry.getKey().getSimpleName().toString();
            final String fieldValue = String.valueOf(entry.getValue().getValue());
            if ("from".equals(fieldName)) {
                from = fieldValue;
            } else if ("to".equals(fieldName)) {
                to = fieldValue;
            } else if ("returnValue".equals(fieldName)) {
                returnValue = DelegateMethod.ReturnValue.valueOf(fieldValue);
            }
        }
        return new MirrorDelegateMethod(from, to, returnValue);
    }

    private Set<DelegateMethod> convertDelegateMethods(final Map<? extends ExecutableElement, ? extends AnnotationValue> fields) {
        if (fields.isEmpty()) {
            return Collections.emptySet();
        }
        final Set<DelegateMethod> result = new HashSet<>();
        for (final Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry : fields.entrySet()) {
            final String fieldName = entry.getKey().getSimpleName().toString();
            final AnnotationValue fieldValue = entry.getValue();
            if ("value".equals(fieldName)) {
                @SuppressWarnings("unchecked")
                final Collection<AnnotationMirror> rules = (Collection<AnnotationMirror>) fieldValue.getValue();
                for (final AnnotationMirror annotationMirror : rules) {
                    result.add(convertDelegateMethod(ProcessorUtils.getAnnotationValues(annotationMirror)));
                }
            }
        }
        return result;
    }
}
